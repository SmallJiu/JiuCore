package cat.jiu.core.api;

import com.google.gson.JsonObject;

import cat.jiu.core.util.JiuUtils;
import net.minecraft.nbt.NBTTagCompound;

public interface ITime {
	/**
	 * the core method<p>
	 * use {@code ticks} to format time
	 * 
	 * @param ticks all tick
	 * @author small_jiu
	 */
	void check(long ticks);
	
	long getDay();
	long getHour();
	long getMinute();
	long getSecond();
	long getTick();
	long getTicks();
	long getAllTicks();
	
	ITime setDay(long day);
	ITime setHour(long hour);
	ITime setMinute(long minute);
	ITime setSecond(long second);
	ITime setTick(long tick);
	ITime setAllTicks(long allTicks);
	default ITime setTicks(long ticks) {
		this.check(ticks);
		return this;
	}
	
	ITime copy();
	
	default String toStringTime() {
		return JiuUtils.other.addJoins(":", this.getTick(), this.getSecond(), this.getMinute(), this.getHour(), this.getDay());
	}
	/**
	 * like {@link net.minecraft.util.ITickable}
	 * @param subtractTick Per tick to subtract time ticks
	 */
	default void update(int subtractTick) {
		this.check(this.getTicks() - subtractTick);
	}
	default void update() {this.update(1);}
	default void readFromNBT(NBTTagCompound nbt) {
		this.check(nbt.getLong("ticks"));
	}
	
	default NBTTagCompound writeToNBT(NBTTagCompound nbt, boolean writeAll) {
		if(writeAll) {
			nbt.setLong("day", this.getDay());
			nbt.setLong("hour", this.getHour());
			nbt.setLong("minute", this.getMinute());
			nbt.setLong("second", this.getSecond());
			nbt.setLong("tick", this.getTick());
		}
		nbt.setLong("ticks", this.getTicks());
		return nbt;
	}
	
	default void toTime(JsonObject obj) {
		this.check(obj.get("ticks").getAsLong());
	}
	default JsonObject toJson(boolean writeAll) {
		JsonObject obj = new JsonObject();
		if(writeAll) {
			obj.addProperty("day", this.getDay());
			obj.addProperty("hour", this.getHour());
			obj.addProperty("minute", this.getMinute());
			obj.addProperty("second", this.getSecond());
			obj.addProperty("tick", this.getTick());
		}
		obj.addProperty("ticks", this.getTicks());
		
		return obj;
	}
}
