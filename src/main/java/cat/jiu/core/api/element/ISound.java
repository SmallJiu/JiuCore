package cat.jiu.core.api.element;

import cat.jiu.core.api.IData;
import cat.jiu.core.api.serializable.IDataSerializable;
import cat.jiu.core.util.Utils;
import cat.jiu.core.util.element.data.JsonData;
import cat.jiu.core.util.element.data.NBTData;
import cat.jiu.core.util.element.sound.SoundJmp123;
import cat.jiu.core.util.element.sound.SoundMC;
import cat.jiu.core.util.registry.DynamicRegistry2;
import com.google.gson.JsonObject;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.function.Supplier;

public interface ISound extends IDataSerializable<IData.IMapData<?>>, Supplier<ResourceLocation> {
    String ID_NAME = "id";
    DynamicRegistry2<ResourceLocation, ISound> REGISTRY = new DynamicRegistry2<ResourceLocation, ISound>("jiucore", "element/sound")
            .setKeyGetter(
                    data->Utils.location(data.getString(ID_NAME, ""))
            )
            .register(register->{
                register.register(SoundMC.ID, SoundMC::new);
                register.register(SoundJmp123.ID, SoundJmp123::new);
            });



    ResourceLocation getSoundID();
    @Override
    default ResourceLocation get(){
        return this.getSoundID();
    }

    String getAudioFile();

    @OnlyIn(Dist.CLIENT)
    void play();
    boolean isPlayed();

    @OnlyIn(Dist.CLIENT)
    void pause(boolean pauseSound);
    boolean isPaused();

    @OnlyIn(Dist.CLIENT)
    void stop();
    boolean isStopped();

    float getSoundVolume();
    ISound setSoundVolume(float volume);
    default float getFullSoundVolume() {
        return 1f;
    }

    float getSoundPitch();
    ISound setSoundPitch(float pitch);

    boolean isSoundLooping();
    ISound setSoundLooping(boolean isLooping);

    SoundSource getSoundChannel();
    ISound setSoundChannel(SoundSource source);
    static SoundSource getSoundChannelByName(String name){
        for (SoundSource value : SoundSource.values()) {
            if(value.getName().contentEquals(name)){
                return value;
            }
        }
        return null;
    }

    float getFloatDuration();
    default long getDuration() {
        return (long) this.getFloatDuration();
    }

    float getFloatElapse();
    default long getElapse() {
        return (long)this.getFloatElapse();
    }

    default float getSurplusPart() {
        return 1.0f - ((getFloatElapse() - getFloatDuration()) * 1.0f / getFloatDuration() + 1f);
    }

    default ISound copy() {
        return this;
    }

    @Deprecated(since = "1.20.1-0.0.1-2025.8.10")
    default JsonObject write(JsonObject data) {
        return (JsonObject) this.write(JsonData.map(data)).getData();
    }
    @Deprecated(since = "1.20.1-0.0.1-2025.8.10")
    default void read(JsonObject data) {
        this.read(JsonData.map(data));
    }

    @Deprecated(since = "1.20.1-0.0.1-2025.8.10")
    default CompoundTag write(CompoundTag data) {
        return (CompoundTag) this.write(NBTData.map(data)).getData();
    }
    @Deprecated(since = "1.20.1-0.0.1-2025.8.10")
    default void read(CompoundTag data) {
        this.read(NBTData.map(data));
    }

    abstract class BaseSound implements ISound {
        public final ResourceLocation id;
        private float volume = 1f, pitch = 1f;
        private boolean canLooping = false;
        private SoundSource channel = SoundSource.PLAYERS;

        public BaseSound(String mod, String id) {
            this(Utils.location(mod, id));
        }
        public BaseSound(String id) {
            this(Utils.location(id));
        }
        public BaseSound(ResourceLocation id) {
            this.id = id;
        }

        @Override
        public ResourceLocation getSoundID() {
            return this.id;
        }

        @Override
        public float getSoundVolume() {
            return this.volume;
        }

        @Override
        public ISound setSoundVolume(float volume) {
            this.volume = volume;
            return this;
        }

        @Override
        public float getSoundPitch() {
            return this.pitch;
        }

        @Override
        public ISound setSoundPitch(float pitch) {
            this.pitch = pitch;
            return this;
        }

        @Override
        public boolean isSoundLooping() {
            return this.canLooping;
        }

        @Override
        public ISound setSoundLooping(boolean isLooping) {
            this.canLooping = isLooping;
            return this;
        }

        @Override
        public SoundSource getSoundChannel() {
            return this.channel;
        }

        @Override
        public ISound setSoundChannel(SoundSource channel) {
            this.channel = channel;
            return this;
        }

        @Override
        public IData.IMapData<?> write(IData.IMapData<?> data) {
            data.putData(ID_NAME, String.valueOf(this.getSoundID()));
            data.putData("channel", this.getSoundChannel().getName());
            data.putData("looping", this.isSoundLooping());
            data.putData("volume", this.getSoundVolume());
            data.putData("pitch", this.getSoundPitch());
            return data;
        }

        @Override
        public void read(IData.IMapData<?> data) {
            this.setSoundChannel(getSoundChannelByName(data.getString("channel", SoundSource.PLAYERS.getName())));
            this.setSoundLooping(data.getBoolean("looping", false));
            this.setSoundVolume(data.getFloat("volume", 1f));
            this.setSoundPitch(data.getFloat("pitch", 1f));
        }
    }
}
