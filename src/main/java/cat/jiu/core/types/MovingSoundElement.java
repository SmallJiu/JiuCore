package cat.jiu.core.types;

import javax.annotation.Nullable;

import cat.jiu.core.api.ITimer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;

public class MovingSoundElement {
	protected final SoundEvent sound;
	protected final float volume;
	protected final float pitch;
	protected final SoundCategory category;
	protected final ITimer time;
	
	public MovingSoundElement(ITimer playTime, SoundEvent sound, float volume, float pitch, SoundCategory category) {
		this.sound = sound;
		this.volume = volume;
		this.pitch = pitch;
		this.category = category;
		this.time = playTime;
	}
	
	public SoundEvent getSound() {return sound;}
	public float getSoundVolume() {return volume;}
	public float getSoundPitch() {return pitch;}
	public SoundCategory getSoundCategory() {return category;}
	public ITimer getTime() {return time;}
	
	public NBTTagCompound writeToNBT(@Nullable NBTTagCompound nbt) {
		if(nbt==null) nbt = new NBTTagCompound();
		
		nbt.setInteger("sound", SoundEvent.REGISTRY.getIDForObject(this.sound));
		nbt.setFloat("pitch", this.pitch);
		nbt.setFloat("volume", this.volume);
		nbt.setString("category", this.category.getName());
		nbt.setTag("lastMillis", this.time.writeTo(NBTTagCompound.class));
		
		return nbt;
	}
	
	public static MovingSoundElement readFromeNBT(NBTTagCompound nbt) {
		if(nbt==null || nbt.getSize()<=0) return null;
		return new MovingSoundElement(
				ITimer.from(nbt.getCompoundTag("lastMillis")),
				SoundEvent.REGISTRY.getObjectById(nbt.getInteger("sound")),
				nbt.getFloat("volume"),
				nbt.getFloat("pitch"),
				SoundCategory.getByName(nbt.getString("category")));
	}
}
