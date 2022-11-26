package cat.jiu.core.util.mc;

import net.minecraft.nbt.*;

public class SimpleNBTTagList extends NBTTagList {
	public void append() {
		super.appendTag(new NBTTagNull());
	}
	public void append(String data) {
		super.appendTag(new NBTTagString(data));
	}
	public void append(int data) {
		super.appendTag(new NBTTagInt(data));
	}
	public void append(byte data) {
		super.appendTag(new NBTTagByte(data));
	}
	public void append(boolean data) {
		super.appendTag(new NBTTagByte((byte) (data?1:0)));
	}
	public void append(short data) {
		super.appendTag(new NBTTagShort(data));
	}
	public void append(long data) {
		super.appendTag(new NBTTagLong(data));
	}
	public void append(float data) {
		super.appendTag(new NBTTagFloat(data));
	}
	public void append(double data) {
		super.appendTag(new NBTTagDouble(data));
	}
	public void append(int[] data) {
		super.appendTag(new NBTTagIntArray(data));
	}
	public void append(byte[] data) {
		super.appendTag(new NBTTagByteArray(data));
	}
	public void append(long[] data) {
		super.appendTag(new NBTTagLongArray(data));
	}
	public void append(NBTTagList data) {
		super.appendTag(data);
	}
	public void append(NBTTagCompound data) {
		super.appendTag(data);
	}
}
