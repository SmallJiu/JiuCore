package cat.jiu.core.util.mc;

import java.io.DataInput;
import java.io.DataOutput;

import cat.jiu.core.util.base.BaseNBT;
import net.minecraft.nbt.NBTSizeTracker;

public class NBTTagNull extends BaseNBT {
	public static final NBTTagNull INSTANCE = new NBTTagNull();
	
	public void read(DataInput input, int depth, NBTSizeTracker tracker) {tracker.read(8L);}
	public void write(DataOutput output) {}
	public String toString() {return "NULL";}
	public byte getId() {return -1;}
	public NBTTagNull copy() {return new NBTTagNull();}
}
