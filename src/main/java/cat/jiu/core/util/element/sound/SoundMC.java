package cat.jiu.core.util.element.sound;

import cat.jiu.core.api.IData;
import cat.jiu.core.api.element.ISound;
import cat.jiu.core.util.Utils;
import cat.jiu.core.util.timer.MillisTimer;
import com.google.gson.JsonObject;
import com.mojang.blaze3d.audio.Channel;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.Sound;
import net.minecraft.client.sounds.ChannelAccess;
import net.minecraft.client.sounds.SoundEngine;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.registries.ForgeRegistries;
import org.lwjgl.openal.AL10;

import javax.sound.sampled.AudioFormat;

public class SoundMC extends ISound.BaseSound {
    public static final ResourceLocation ID = Utils.location("jiucore", "element/sound/mc");

    protected Object mcSound;
    protected SoundEvent soundEvent;
    protected long duration = 0,
            currentStart, currentDuration, elapse = 0;

    public SoundMC() {
        super(ID);
    }

    public SoundMC(SoundEvent soundEvent) {
        this();
        this.soundEvent = soundEvent;
    }

    @Deprecated(since = "1.20.1-0.0.1-2025.8.10")
    public SoundMC(CompoundTag data) {
        this();
        this.read(data);
    }
    @Deprecated(since = "1.20.1-0.0.1-2025.8.10")
    public SoundMC(JsonObject data) {
        this();
        this.read(data);
    }
    public SoundMC(IData.IMapData<?> data) {
        this();
        this.read(data);
    }

    @Override
    public SoundMC copy() {
        SoundMC sound = new SoundMC(this.getSoundEvent());
        sound.setDuration(this.getDuration());
        return sound;
    }

    public SoundEvent getSoundEvent() {
        return soundEvent;
    }

    public SoundMC setSoundEvent(SoundEvent soundEvent) {
        this.soundEvent = soundEvent;
        return this;
    }

    @OnlyIn(Dist.CLIENT)
    public cat.jiu.core.util.client.FollowPosSoundInstance getSoundInstance() {
        return (cat.jiu.core.util.client.FollowPosSoundInstance) mcSound;
    }

    protected boolean followEntity;
    protected Entity entity;
    protected BlockPos pos;
    public void setFollowEntity(Entity entity) {
        this.followEntity = true;
        this.entity = entity;
    }

    public void setFollowPos(BlockPos pos) {
        this.followEntity = false;
        this.pos = pos;
    }

    @Override
    public String getAudioFile() {
        return this.getSoundEvent().getLocation().toString();
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void play() {
        if (this.mcSound != null) {
            Minecraft.getInstance().getSoundManager().stop(this.getSoundInstance());
            this.mcSound = null;
        }
        this.mcSound = new cat.jiu.core.util.client.FollowPosSoundInstance(this);
        this.getSoundInstance().setFollowing(this.followEntity, this.entity, this.pos);

        Minecraft.getInstance().getSoundManager().play(this.getSoundInstance());

        if (false) {
            try {
                SoundEngine engine = Minecraft.getInstance().getSoundManager().soundEngine;
                engine.soundBuffers.getCompleteBuffer(Sound.SOUND_LISTER.idToFile(this.getSoundEvent().getLocation())).thenAccept(soundBuffer -> {
                    this.setDuration(soundBuffer.data.getLong());
                });
                ChannelAccess.ChannelHandle handle = engine.instanceToChannel.get(this.getSoundInstance());
                if (handle != null) {
                    Channel channel = handle.channel;
                    if (channel != null && channel.stream != null) {
                        AudioFormat format = channel.stream.getFormat();
                        this.setDuration((long) (format.getFrameRate() / format.getFrameSize()));
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        this.currentStart = System.currentTimeMillis();
        this.currentDuration = this.currentStart + this.duration;
        this.pause = false;
    }

    @Override
    public boolean isPlayed() {
        return this.mcSound != null && !this.getSoundInstance().isStopped();
    }

    protected boolean pause = true;
    @OnlyIn(Dist.CLIENT)
    @Override
    public void pause(boolean pauseSound) {
        if (pauseSound) {
            this.pause = true;
            this.elapse = this.currentDuration - (this.currentDuration - System.currentTimeMillis());
            Minecraft.getInstance().getSoundManager().pause();
        }else {
            this.pause = false;
            Minecraft.getInstance().getSoundManager().resume();
        }
    }

    @Override
    public boolean isPaused() {
        return this.pause;
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void stop() {
        this.pause = true;
        Minecraft.getInstance().getSoundManager().stop(this.getSoundInstance());
    }

    @Override
    public boolean isStopped() {
        return this.mcSound==null || this.getSoundInstance().isStopped();
    }

    @Override
    public float getFloatDuration() {
        return this.duration;
    }
    @Override
    public long getDuration() {
        return this.duration;
    }

    public SoundMC setDuration(int minutes, int seconds, int millis) {
        return this.setDuration(MillisTimer.parseMillis(minutes, seconds, millis));
    }
    public SoundMC setDuration(MillisTimer duration) {
        return this.setDuration(duration.getNotStartedMillis());
    }
    public SoundMC setDuration(long duration) {
        this.duration = duration;
        return this;
    }

    @Override
    public float getFloatElapse() {
        return this.isPaused() ? this.elapse : this.currentDuration - (this.currentDuration - System.currentTimeMillis());
    }

    @Override
    public IData.IMapData<?> write(IData.IMapData<?> data) {
        data.putData("sound", ForgeRegistries.SOUND_EVENTS.getKey(this.getSoundEvent()));
        data.putData("duration", this.getDuration());
        return super.write(data);
    }

    @Override
    public void read(IData.IMapData<?> data) {
        this.setSoundEvent(ForgeRegistries.SOUND_EVENTS.getValue(data.getLocation("sound", SoundEvents.MUSIC_DISC_CAT.getLocation())));
        this.setDuration(data.getLong("duration", 1));
        super.read(data);
    }
}
