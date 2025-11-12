package cat.jiu.core.util.element.sound;

import cat.jiu.core.api.IData;
import cat.jiu.core.api.element.ISound;
import cat.jiu.core.util.Utils;
import cat.jiu.core.util.client.AudioSystem;
import cat.jiu.core.util.element.data.NBTData;
import com.google.gson.JsonObject;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.UUID;

public class SoundJmp123 extends ISound.BaseSound {
    public static final ResourceLocation ID = Utils.location("jiucore", "element/sound/jmp123");

    private AudioSystem.Audio audio;
    private UUID uid;
    public SoundJmp123() {
        super(ID);
    }

    public SoundJmp123(AudioSystem.Audio audio) {
        this();
        this.audio = audio;
        this.setSoundChannel(audio.getSoundChannel());
    }
    @Deprecated(since = "1.20.1-0.0.1-2025.8.10")
    public SoundJmp123(CompoundTag data) {
        this();
        this.read(data);
    }
    @Deprecated(since = "1.20.1-0.0.1-2025.8.10")
    public SoundJmp123(JsonObject data) {
        this();
        this.read(data);
    }
    public SoundJmp123(IData.IMapData<?> data) {
        this();
        this.read(data);
    }

    public AudioSystem.Audio getAudio() {
        return audio;
    }

    public SoundJmp123 setAudio(AudioSystem.Audio audio) {
        this.audio = audio;
        if (audio!=null) this.setSoundChannel(audio.getSoundChannel());
        return this;
    }

    @Override
    public SoundJmp123 copy() {
        return new SoundJmp123(AudioSystem.Audio.create(this.getAudio().write(NBTData.map())));
    }

    public UUID getUUID() {
        return uid;
    }

    @Override
    public String getAudioFile() {
        return this.audio != null ? this.audio.getFile() : "";
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void play() {
        if (this.isStopped()) {
            this.uid = AudioSystem.play(this.getAudio());
        }
    }

    @Override
    public boolean isPlayed() {
        return !this.isStopped();
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void pause(boolean pauseSound) {
        if (this.isPlayed()) {
            AudioSystem.pause(this.uid, pauseSound);
        }
    }

    @Override
    public boolean isPaused() {
        return AudioSystem.isPaused(this.uid);
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void stop() {
        if (this.isPlayed()) {
            AudioSystem.stop(this.uid);
        }
    }

    @Override
    public boolean isStopped() {
        return this.uid == null || AudioSystem.isClose(this.uid);
    }

    @Override
    public float getFloatDuration() {
        return this.isPlayed() ? AudioSystem.getFloatDuration(this.uid) : 0;
    }

    @Override
    public float getFloatElapse() {
        return this.isPlayed() ? AudioSystem.getFloatElapse(this.uid) : 0;
    }

    @Override
    public float getFullSoundVolume() {
        return 80f;
    }

    @Override
    public ISound setSoundLooping(boolean isLooping) {
        this.getAudio().setCanLopping(isLooping);
        return super.setSoundLooping(isLooping);
    }

    @Override
    public boolean isSoundLooping() {
        return this.getAudio().isCanLopping();
    }

    @Override
    public SoundSource getSoundChannel() {
        return this.getAudio() != null ? this.getAudio().getSoundChannel() : super.getSoundChannel();
    }

    @Override
    public IData.IMapData<?> write(IData.IMapData<?> data) {
        data.putData("audio", this.audio.write(data.newMap()));
        return super.write(data);
    }

    @Override
    public void read(IData.IMapData<?> data) {
        this.setAudio(AudioSystem.Audio.create(data.getMap("audio", data.emptyMap())));
        super.read(data);
    }
}
