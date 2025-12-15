package cat.jiu.core.util.client;

import cat.jiu.core.api.IData;
import cat.jiu.core.util.element.data.*;
import com.google.common.collect.Maps;
import com.google.gson.JsonObject;
import jmp123.PlayBack;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.ClientPlayerNetworkEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.Cancelable;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.loading.FMLLoader;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.concurrent.ConcurrentMap;

@SuppressWarnings("unused")
@Mod.EventBusSubscriber(Dist.CLIENT)
public class AudioSystem {
    public static final Logger LOGGER = LogManager.getLogger("JiuCore/AudioSystem");
    static final ConcurrentMap<ResourceLocation, Audio> PRELOAD_AUDIO = Maps.newConcurrentMap();
    public static ResourceLocation preload(ResourceLocation id, Audio audio) {
        PRELOAD_AUDIO.put(id, audio);
        return id;
    }
    public static boolean hasPreloadAudio(ResourceLocation id) {
        return PRELOAD_AUDIO.containsKey(id);
    }
    public static Audio getPreloadAudio(ResourceLocation id) {
        return PRELOAD_AUDIO.get(id);
    }
    public static UUID play(ResourceLocation preloadAudioID) {
        if (hasPreloadAudio(preloadAudioID)) {
            return play(getPreloadAudio(preloadAudioID));
        }
        return null;
    }

    static final ConcurrentMap<UUID, Sound> MAP = Maps.newConcurrentMap();

    public static UUID play(Audio audio) {
        if (audio == null || !FMLLoader.getDist().isClient()) {
            return null;
        }
        if (audio.getUUID() != null) {
            stop(audio.getUUID());
        }
        UUID id = UUID.randomUUID();
        jmp123.output.Audio jmp_audio = new jmp123.output.Audio();
        PlayBack player = new PlayBack(jmp_audio);
        Thread thread = new Thread(()->{
                do {
                    if (!MinecraftForge.EVENT_BUS.post(new Event.Loop.Pre(id, audio))){
                        boolean success = false;
                        try {
                            play(audio, id, jmp_audio, player, false, 0);
                            audio.addLoopCount();
                            success = true;
                        }catch (Exception e){
                            LOGGER.error("Unable to play audio. Try other file. file: ' {} ', loop: {}, uid: {}, info: {}", audio.getFile(), audio.getLoopCount(), id, e);

                            // TODO 按顺序尝试播放音频文件
                            if (audio.hasRetryFiles()) {
                                for (int retryIndex = 0; retryIndex < audio.getRetryFiles().size(); retryIndex++) {
                                    try {
                                        play(audio, id, jmp_audio, player, true, retryIndex);
                                        audio.addLoopCount();
                                        success = true;
                                        break;
                                    } catch (Exception ex) {
                                        LOGGER.error("Unable to play other audio. index: {}, file: ' {} ', loop: {}, uid: {}, info: {}", retryIndex, audio.getRetryFile(retryIndex), audio.getLoopCount(), id, e);
                                    }
                                }
                            }
                        }
                        MinecraftForge.EVENT_BUS.post(new Event.Loop.Post(id, audio, success));
                        try {
                            Thread.sleep(audio.getLoopDelay());
                        } catch (Exception ignored) {}
                    }
                }while (!audio.isClose() && audio.isCanLopping());
                stop(id);
        });
        MAP.put(id, new Sound(audio, jmp_audio, player, thread));
        thread.start();
        return id;
    }
    static void play(Audio audio, UUID id, jmp123.output.Audio jmp_audio, PlayBack player, boolean retry, int retryIndex) throws IOException {
        player.open(retry ? audio.getRetryFile(retryIndex) : audio.getFile(), null);
        float level = getMinecraftVolume(audio.getSoundChannel());
        Event.Played event = new Event.Played(id, audio, level);
        if (!MinecraftForge.EVENT_BUS.post(event)) {
            level = event.getVolume();
            setVolume(id, level);
            player.start(false);
        }
    }

    public static void pause(UUID id){
        if (id!=null && MAP.containsKey(id) && !getPlayer(id).isPaused()) {
            getPlayer(id).pause();
            MinecraftForge.EVENT_BUS.post(new Event.Pause(id, MAP.get(id).audio, true));
        }
    }
    public static boolean isPaused(UUID id){
        if (id!=null && MAP.containsKey(id)) {
            return getPlayer(id).isPaused();
        }
        return true;
    }
    public static void start(UUID id){
        if (id!=null && MAP.containsKey(id) && getPlayer(id).isPaused()) {
            getPlayer(id).pause();
            MinecraftForge.EVENT_BUS.post(new Event.Pause(id, MAP.get(id).audio, false));
        }
    }
    public static void stop(UUID id){
        pause(id);
        if (id!=null && MAP.containsKey(id)) {
            getPlayer(id).stop();
            getPlayer(id).close();
            Sound sound = MAP.get(id);
            sound.thread.stop();
            sound.close = true;
            sound.audio.uid = null;
            MinecraftForge.EVENT_BUS.post(new Event.Stopped(id, sound.audio));
            MAP.remove(id);
        }
    }

    public static void pause(UUID id, boolean pause) {
        if (pause) {
            pause(id);
        }else {
            start(id);
        }
    }

    public static float getVolume(UUID id){
        if (id!=null && MAP.containsKey(id)) {
            return MAP.get(id).jmpAudio.getFloatControl().getValue();
        }
        return -40;
    }
    public static void setVolume(UUID id, float volume){
        float level = getAudioVolume(volume);
        if (id!=null && MAP.containsKey(id)) {

            Event.SetVolume event = new Event.SetVolume(id, MAP.get(id).audio, level);
            if (MinecraftForge.EVENT_BUS.post(event)) return;
            level = event.getVolume();

            getPlayer(id).setVolume(level);
            MAP.get(id).jmpAudio.setLineGain(level);
        }
    }

    public static float getAudioVolume(float volume) {
        return -40f + (40f * volume);
    }

    /**
     * current audio elapse
     * @return millis
     */
    public static long getElapse(UUID id){
        return (long) getFloatElapse(id);
    }

    public static float getFloatElapse(UUID id){
        if (id!=null && MAP.containsKey(id)) {
            return (getPlayer(id).getHeader().getFrames() * getPlayer(id).getHeader().getFrameDuration()) * 1000;
        }
        return 0;
    }

    /**
     *  current audio duration
     * @return millis
     */
    public static long getDuration(UUID id){
        return (long) getFloatDuration(id);
    }
    public static float getFloatDuration(UUID id){
        if (id!=null && MAP.containsKey(id)) {
            return getPlayer(id).getHeader().getDuration() * 1000;
        }
        return 0;
    }

    public static float getSurplusPart(UUID id) {
        if (id!=null && MAP.containsKey(id)) {
            return 1.0f - (float) (((getElapse(id) - getDuration(id)) * 1.0 / getDuration(id)) + 1);
        }
        return 0.0F;
    }

    public static boolean isClose(UUID id) {
        return id==null || !MAP.containsKey(id) || MAP.get(id).close;
    }
    public static boolean isPlayed(UUID id) {
        return id!=null && MAP.containsKey(id) && !MAP.get(id).close;
    }

    static PlayBack getPlayer(UUID id){
        return MAP.get(id).player;
    }

    public static void pauseAll(){
        for (UUID uuid : MAP.keySet()) {
            pause(uuid);
        }
    }
    public static void startAll(){
        for (UUID uuid : MAP.keySet()) {
            start(uuid);
        }
    }
    public static void stopAll(){
        for (UUID uuid : MAP.keySet()) {
            stop(uuid);
        }
    }
    public static void setAllVolume(float volume){
        for (UUID uuid : MAP.keySet()) {
            setVolume(uuid, volume);
        }
    }

    @OnlyIn(Dist.CLIENT)
    public static float getMinecraftVolume(SoundSource sc){
        return Minecraft.getInstance().options.getSoundSourceVolume(sc) * Minecraft.getInstance().options.getSoundSourceVolume(SoundSource.MASTER);
    }

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public static void onExitServer(ClientPlayerNetworkEvent.LoggingOut event) {
        AudioSystem.stopAll();
    }

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        if (!MAP.isEmpty()) {
            for (UUID uuid : MAP.keySet()) {
                setVolume(uuid, getMinecraftVolume(MAP.get(uuid).audio.soundChannel));
            }
        }
    }

    static class Sound {
        final Audio audio;
        final jmp123.output.Audio jmpAudio;
        final PlayBack player;
        final Thread thread;
        boolean close = false;
        public Sound(Audio audio, jmp123.output.Audio jmpAudio, PlayBack player, Thread thread) {
            this.audio = audio;
            this.jmpAudio = jmpAudio;
            this.player = player;
            this.thread = thread;
        }
    }

    public static class Audio {
        protected UUID uid;
        protected String file;
        protected List<String> retryFiles;
        protected SoundSource soundChannel;
        protected boolean canLopping = false, infiniteLopping = false;
        protected int maxLoopCount = 5, loopCount = 0;
        protected long loopDelay = 500;

        public Audio(File file, SoundSource soundChannel) {
            this(file.getPath(), soundChannel);
        }
        public Audio(String file, SoundSource soundChannel) {
            this.file = file;
            this.soundChannel = soundChannel;
        }

        public Audio(String fileName, InputStream audioStream, SoundSource soundChannel) {
            this(new File(fileName), audioStream, soundChannel);
        }
        public Audio(File file, InputStream audioStream, SoundSource soundChannel) {
            this(streamToFile(file, audioStream), soundChannel);
        }
        public static File streamToFile(File file, InputStream audioStream) {
            if (FMLLoader.getDist().isClient() && !file.exists() && audioStream != null) {
                try {
                    file.getParentFile().mkdirs();
                    file.createNewFile();
                    IOUtils.copy(audioStream, new FileOutputStream(file));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return file;
        }

        /**
         * @return set to actual id after played, set to null after stopped.
         */
        public UUID getUUID() {
            return uid;
        }

        public UUID play() {
            return this.uid = AudioSystem.play(this);
        }

        public void pause(boolean pause) {
            AudioSystem.pause(this.getUUID(), pause);
        }
        public void pause(){
            AudioSystem.pause(this.getUUID());
        }
        public void start(){
            AudioSystem.start(this.getUUID());
        }
        public void stop(){
            AudioSystem.stop(this.getUUID());
        }
        public boolean isPause(){
            return AudioSystem.isPaused(this.getUUID());
        }
        public boolean isClose(){
            return AudioSystem.isClose(this.getUUID());
        }
        public boolean isPlayed(){
            return AudioSystem.isPlayed(this.getUUID());
        }
        public float getVolume(){
            return AudioSystem.getVolume(this.getUUID());
        }
        public void setVolume(float volume) {
            AudioSystem.setVolume(this.getUUID(), volume);
        }
        public long getElapse(){
            return AudioSystem.getElapse(this.getUUID());
        }
        public long getDuration(){
            return AudioSystem.getDuration(this.getUUID());
        }

        public Audio setAudioFile(String file) {
            this.file = file;
            return this;
        }
        public String getFile() {
            return file;
        }
        File fileObj;

        /**
         * @return if audio file is a network url, it will return a empty file.
         */
        public File getFileObject(){
            if (this.fileObj == null) {
                this.fileObj = new File(this.getFile());
            }
            return this.fileObj;
        }

        public Audio addRetryFile(File file) {
            return this.addRetryFile(file.getPath());
        }
        public Audio addRetryFile(String file) {
            if (file != null) {
                if (this.retryFiles == null) {
                    this.retryFiles = new ArrayList<>();
                }
                this.retryFiles.add(file);
            }
            return this;
        }
        public String getRetryFile(int index) {
            return this.retryFiles.get(index);
        }
        public List<String> getRetryFiles() {
            return retryFiles;
        }
        public boolean hasRetryFiles(){
            return this.retryFiles != null && !this.retryFiles.isEmpty();
        }

        Map<String, File> retryFileObjs;
        @Nullable
        public File getRetryFileObject(int index) {
            if (this.retryFileObjs == null) {
                this.retryFileObjs = new HashMap<>();
            }
            String retryFile = this.getRetryFile(index);
            if (!retryFile.startsWith("http")){
                if (!this.retryFileObjs.containsKey(retryFile)) {
                    this.retryFileObjs.put(retryFile, new File(retryFile));
                }
                return this.retryFileObjs.get(retryFile);
            }
            return null;
        }
        @Deprecated
        public Audio setRetryFile(String retryFile) {
            this.addRetryFile(retryFile);
            return this;
        }
        @Deprecated
        public String getRetryFile() {
            return this.hasRetryFiles() ? this.getRetryFile(0) : null;
        }
        @Deprecated
        public File getRetryFileObject() {
            return this.hasRetryFiles() ? this.getRetryFileObject(0) : null;
        }

        @Deprecated
        public SoundSource getCategory() {
            return this.getSoundChannel();
        }
        public SoundSource getSoundChannel() {
            return soundChannel!=null ? soundChannel : SoundSource.PLAYERS;
        }
        public Audio setSoundChannel(SoundSource soundChannel) {
            this.soundChannel = soundChannel;
            return this;
        }

        public boolean isInfiniteLoop() {
            return infiniteLopping;
        }

        public Audio setInfiniteLopping(boolean infiniteLopping) {
            this.infiniteLopping = infiniteLopping;
            return this;
        }

        boolean stopLopping;
        public Audio stopLopping() {
            this.stopLopping = true;
            return this;
        }
        public Audio startLopping() {
            this.stopLopping = false;
            this.loopCount = 0;
            return this;
        }

        public int getLoopCount() {
            return loopCount;
        }

        public void addLoopCount() {
            this.loopCount++;
        }

        public boolean isCanLopping() {
            boolean loop = this.isInfiniteLoop();
            if (this.getMaxLoopCount() > 1){
                loop = this.getLoopCount() <= this.getMaxLoopCount();
            }
            return this.canLopping && loop && !this.stopLopping;
        }

        public Audio setCanLopping(boolean canLopping) {
            this.canLopping = canLopping;
            return this;
        }

        public int getMaxLoopCount() {
            return maxLoopCount;
        }

        public Audio setMaxLoopCount(int maxLoopCount) {
            this.maxLoopCount = maxLoopCount;
            return this;
        }

        public long getLoopDelay() {
            return loopDelay;
        }

        /**
         * @param loopDelay millis
         */
        public Audio setLoopDelay(long loopDelay) {
            this.loopDelay = loopDelay;
            return this;
        }
        public IData.IMapData<?> write(IData.IMapData<?> data) {
            data.putData("file", this.getFile().replace('\\', '/'));
            if (this.getRetryFiles() != null && !this.getRetryFiles().isEmpty()) {
                IData.IListData<?> retros = data.newList();
                for (String retryFile : this.getRetryFiles()) {
                    retros.putData(retryFile.replace('\\', '/'));
                }
                data.putData("retryFiles", retros);
            }
            data.putData("channel", this.getSoundChannel().getName());
            if (this.isCanLopping()) {
                data.putData("canLoop", this.isCanLopping());
                data.putData("loopDelay", this.getLoopDelay());
                if (this.isInfiniteLoop()) {
                    data.putData("infiniteLoop", this.isInfiniteLoop());
                }
                if (this.getMaxLoopCount() > 1) {
                    data.putData("maxLoopCount", this.getMaxLoopCount());
                }
            }
            return data;
        }
        public void read(IData.IMapData<?> data) {
            this.setAudioFile(data.getString("file", "file_not_found")); // 音频文件路径

            // 尝试继续播放的文件
            this.addRetryFile(data.getString("retryFile", null));
            data.getList("retryFiles", String.class).foreach((index, value) ->
                this.addRetryFile(value.getAsPrimitive().getAsString())
            );
            this.setSoundChannel(getSoundChannelByName(data.getString("channel", SoundSource.PLAYERS.getName()))); // MC原版的音频音量通道
            this.setCanLopping(data.getBoolean("canLoop", false)); // 是否可以循环播放
            this.setLoopDelay(data.getLong("loopDelay", 0L)); // 循环一次的间隙
            this.setInfiniteLopping(data.getBoolean("infiniteLoop", false)); // 是否可以无限循环
            this.setMaxLoopCount(data.getInt("maxLoopCount", 0)); // 最大循环次数
        }
        @Deprecated(since = "1.20.1-0.0.1-2025.8.10")
        public JsonObject write(JsonObject data) {
            this.write(JsonData.map(data));
            return data;
        }
        @Deprecated(since = "1.20.1-0.0.1-2025.8.10")
        public CompoundTag write(CompoundTag data) {
            this.write(NBTData.map(data));
            return data;
        }

        public static Audio create(CompoundTag data) {
            return create(NBTData.map(data));
        }
        public static Audio create(JsonObject data) {
            return create(JsonData.map(data));
        }
        public static Audio create(IData.IMapData<?> data) {
            Audio instance = new Audio("file_not_found", SoundSource.PLAYERS);
            instance.read(data);
            return instance;
        }
        static SoundSource getSoundChannelByName(String name){
            for (SoundSource value : SoundSource.values()) {
                if(value.getName().equalsIgnoreCase(name)){
                    return value;
                }
            }
            return null;
        }
    }

    public static class Event extends net.minecraftforge.eventbus.api.Event {
        public final UUID uid;
        public final Audio audio;
        protected Event(UUID uid, Audio audio) {
            this.uid = uid;
            this.audio = audio;
        }
        public boolean is(UUID uuid) {
            return this.uid.equals(uuid);
        }
        public boolean is(String file) {
            return this.audio.getFile().equals(file)
               || (this.audio.hasRetryFiles() && this.audio.getRetryFiles().contains(file));
        }
        @Cancelable
        public static class Played extends Event {
            protected float volume;
            public Played(UUID uid, Audio audio, float volume) {
                super(uid, audio);
                this.volume = volume;
            }
            public float getVolume() {
                return volume;
            }
            public void setVolume(float volume) {
                this.volume = volume;
            }
        }
        public static class Loop {
            @Cancelable
            public static class Pre extends Event {
                public Pre(UUID uid, Audio audio) {
                    super(uid, audio);
                }

                @Override
                public void setCanceled(boolean cancel) {
                    super.setCanceled(cancel);
                    if (cancel) {
                        this.audio.addLoopCount();
                    }
                }
            }
            public static class Post extends Event {
                public final boolean successToPlay;
                public Post(UUID uid, Audio audio, boolean successToPlay) {
                    super(uid, audio);
                    this.successToPlay = successToPlay;
                }
            }
        }
        public static class Stopped extends Event {
            public Stopped(UUID uid, Audio audio) {
                super(uid, audio);
            }
        }
        public static class Pause extends Event {
            public final boolean pause;
            public Pause(UUID uid, Audio audio, boolean pause) {
                super(uid, audio);
                this.pause = pause;
            }
        }
        @Cancelable
        public static class SetVolume extends Event {
            protected float volume;
            public SetVolume(UUID uid, Audio audio, float volume) {
                super(uid, audio);
                this.volume = volume;
            }
            public float getVolume() {
                return volume;
            }
            public void setVolume(float volume) {
                this.volume = volume;
            }
        }
    }
}
