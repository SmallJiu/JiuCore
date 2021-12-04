/*
package cat.jiu.core.util;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTSizeTracker;

public class NBTTagStringArray extends NBTBase {
	/** The array of saved integers 
	private String[] intArray;

	NBTTagStringArray() {
	}

	public NBTTagStringArray(String[] array) {
		this.intArray = array;
	}

	public NBTTagStringArray(List<String> list) {
		this(toArray(list));
	}

	private static String[] toArray(List<String> list) {
		String[] aint = new String[list.size()];

		for (int i = 0; i < list.size(); ++i) {
			String integer = list.get(i);
			aint[i] = integer;
		}

		return aint;
	}

	/**
	 * Write the actual data contents of the tag, implemented in NBT extension
	 * classes
	 
	
	public void write(DataOutput output) {
		output.writeInt(this.intArray.length);

		for (String i : this.intArray) {
			output.writeInt(i);
		}
	}

	public void read(DataInput input, int depth, NBTSizeTracker sizeTracker) {
		sizeTracker.read(192L);
		int i = input.readInt();
		sizeTracker.read((long) (32 * i));
		this.intArray = new String[i];

		for (int j = 0; j < i; ++j) {
			this.intArray[j] = input.readInt();
		}
	}

	/**
	 * Gets the type byte for the tag.
	 
	public byte getId() {
		return 11;
	}

	public String toString() {
		StringBuilder stringbuilder = new StringBuilder("[I;");

		for (int i = 0; i < this.intArray.length; ++i) {
			if (i != 0) {
				stringbuilder.append(',');
			}

			stringbuilder.append(this.intArray[i]);
		}

		return stringbuilder.append(']').toString();
	}

	/**
	 * Creates a clone of the tag.
	 
	public NBTTagStringArray copy() {
		String[] aint = new String[this.intArray.length];
		System.arraycopy(this.intArray, 0, aint, 0, this.intArray.length);
		return new NBTTagStringArray(aint);
	}

	public boolean equals(Object o) {
		return super.equals(o) && Arrays.equals(this.intArray, ((NBTTagStringArray) o).intArray);
	}

	public int hashCode() {
		return super.hashCode() ^ Arrays.hashCode(this.intArray);
	}

	public String[] getStringArray() {
		return this.intArray;
	}
}
*/