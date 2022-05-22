package cat.jiu.core.util.helpers;

import java.math.BigInteger;

import cat.jiu.core.util.JiuUtils;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class NBTTagCompoundUtils {
	public boolean hasNBT(ItemStack stack, String nbtName) {
		return this.getItemNBT(stack).hasKey(nbtName);
	}
	
	public NBTTagCompound getItemNBT(ItemStack stack) {
		NBTTagCompound nbt = stack.getTagCompound();
		return nbt != null ? nbt : new NBTTagCompound();
	}
	
	public ItemStack removeItemNBT(ItemStack stack, String nbtName) {
		NBTTagCompound nbt = this.getItemNBT(stack);
		if(this.hasNBT(stack, nbtName)) nbt.removeTag(nbtName);
		return stack;
	}
	
	public ItemStack setNBT(ItemStack stack, NBTTagCompound nbt) {
		if(nbt != null) stack.setTagCompound(nbt);
		return stack;
	}
	
	//=================================================================================================//
	public NBTTagCompound setNBT(NBTTagCompound nbt, String nbtName, String value) {
		nbt.setString(nbtName, value);
		return nbt;
	}
	
	public NBTTagCompound addNBT(NBTTagCompound nbt, String nbtName, String value) {
		return this.setNBT(nbt, nbtName, this.getNBTString(nbt, nbtName) + value);
	}
	
	public String getNBTString(NBTTagCompound nbt, String nbtName) {
		return nbt.getString(nbtName);
	}
	//=================================================================================================//
	public NBTTagCompound setNBT(NBTTagCompound nbt, String nbtName, int value) {
		nbt.setInteger(nbtName, value);
		return nbt;
	}
	
	public NBTTagCompound addNBT(NBTTagCompound nbt, String nbtName, int value) {
		return this.setNBT(nbt, nbtName, this.getNBTInt(nbt, nbtName) + value);
	}
	
	public NBTTagCompound subtractNBT(NBTTagCompound nbt, String nbtName, int value) {
		return this.setNBT(nbt, nbtName, this.getNBTInt(nbt, nbtName) - value);
	}
	
	public int getNBTInt(NBTTagCompound nbt, String nbtName) {
		return nbt.getInteger(nbtName);
	}
	//=================================================================================================//
	public NBTTagCompound setNBT(NBTTagCompound nbt, String nbtName, byte value) {
		nbt.setByte(nbtName, value);
		return nbt;
	}
	
	public NBTTagCompound addNBT(NBTTagCompound nbt, String nbtName, byte value) {
		return this.setNBT(nbt, nbtName, this.getNBTByte(nbt, nbtName) + value);
	}
	
	public NBTTagCompound subtractNBT(NBTTagCompound nbt, String nbtName, byte value) {
		return this.setNBT(nbt, nbtName, this.getNBTByte(nbt, nbtName) - value);
	}
	
	public byte getNBTByte(NBTTagCompound nbt, String nbtName) {
		return nbt.getByte(nbtName);
	}
	//=================================================================================================//
	public NBTTagCompound setNBT(NBTTagCompound nbt, String nbtName, long value) {
		nbt.setLong(nbtName, value);
		return nbt;
	}
	
	public NBTTagCompound addNBT(NBTTagCompound nbt, String nbtName, long value) {
		return this.setNBT(nbt, nbtName, this.getNBTLong(nbt, nbtName) + value);
	}
	
	public NBTTagCompound subtractNBT(NBTTagCompound nbt, String nbtName, long value) {
		return this.setNBT(nbt, nbtName, this.getNBTLong(nbt, nbtName) - value);
	}
	
	public long getNBTLong(NBTTagCompound nbt, String nbtName) {
		return nbt.getLong(nbtName);
	}
	//=================================================================================================//
	public NBTTagCompound setNBT(NBTTagCompound nbt, String nbtName, double value) {
		nbt.setDouble(nbtName, value);
		return nbt;
	}
	
	public NBTTagCompound addNBT(NBTTagCompound nbt, String nbtName, double value) {
		return this.setNBT(nbt, nbtName, this.getNBTDouble(nbt, nbtName) + value);
	}
	
	public NBTTagCompound subtractNBT(NBTTagCompound nbt, String nbtName, double value) {
		return this.setNBT(nbt, nbtName, this.getNBTDouble(nbt, nbtName) - value);
	}
	
	public double getNBTDouble(NBTTagCompound nbt, String nbtName) {
		return nbt.getDouble(nbtName);
	}
	//=================================================================================================//
	public NBTTagCompound setNBT(NBTTagCompound nbt, String nbtName, float value) {
		nbt.setFloat(nbtName, value);
		return nbt;
	}
	
	public NBTTagCompound addNBT(NBTTagCompound nbt, String nbtName, float value) {
		return this.setNBT(nbt, nbtName, this.getNBTFloat(nbt, nbtName) + value);
	}
	
	public NBTTagCompound subtractNBT(NBTTagCompound nbt, String nbtName, float value) {
		return this.setNBT(nbt, nbtName, this.getNBTFloat(nbt, nbtName) - value);
	}
	
	public float getNBTFloat(NBTTagCompound nbt, String nbtName) {
		return nbt.getFloat(nbtName);
	}
	//=================================================================================================//
	public NBTTagCompound setNBT(NBTTagCompound nbt, String nbtName, BigInteger value) {
		nbt.setString(nbtName, value.toString());
		return nbt;
	}
	
	public NBTTagCompound addNBT(NBTTagCompound nbt, String nbtName, BigInteger value) {
		return this.setNBT(nbt, nbtName, this.getItemBigInteger(nbt, nbtName).add(value));
	}
	
	public NBTTagCompound subtractNBT(NBTTagCompound nbt, String nbtName, BigInteger value) {
		return this.setNBT(nbt, nbtName, this.getItemBigInteger(nbt, nbtName).subtract(value));
	}
	
	public BigInteger getItemBigInteger(NBTTagCompound nbt, String nbtName) {
		String v = nbt.getString(nbtName);
		if(v.equals("")) {
			return BigInteger.ZERO;
		}
		return new BigInteger(v);
	}
	//=================================================================================================//
	public NBTTagCompound setNBT(NBTTagCompound nbt, String nbtName, int[] value) {
		nbt.setIntArray(nbtName, value);
		return nbt;
	}
	
	public NBTTagCompound addIntToNBTIntArray(NBTTagCompound nbt, String nbtName, int value) {
		int[] array = this.getNBTIntArray(nbt, nbtName);
		int leg = array.length+1;
		int[] add = new int[leg];
		
		for(int i = 0; i < leg-1; ++i) {
			add[i] = array[i];
		}
		add[leg] = value;
		
		return this.setNBT(nbt, nbtName, add);
	}
	
	public int[] getNBTIntArray(NBTTagCompound nbt, String nbtName) {
		return nbt.getIntArray(nbtName);
	}
	//=================================================================================================//
	public NBTTagCompound setNBT(NBTTagCompound nbt, String nbtName, String[] value) {
		nbt.setString(nbtName, JiuUtils.other.toString(value));
		return nbt;
	}
	
	public NBTTagCompound addStringToNBTStringArray(NBTTagCompound nbt, String nbtName, String value) {
		String[] array = this.getNBTStringArray(nbt, nbtName);
		int leg = array.length + 1;
		String[] add = new String[leg];
		
		for(int i = 0; i < leg-1; ++i) {
			add[i] = array[i];
		}
		
		add[leg] = value;
		return this.setNBT(nbt, nbtName, add);
	}
	 
	public String[] getNBTStringArray(NBTTagCompound nbt, String nbtName) {
		String s = nbt.getString(nbtName);
		if(s.equals("")) {
			return new String[] {"null"};
		}
		return JiuUtils.other.custemSplitString(s, ",");
	}
	//=================================================================================================//
	public NBTTagCompound setNBT(NBTTagCompound nbt, String nbtName, boolean value) {
		nbt.setBoolean(nbtName, value);
		return nbt;
	}
	
	public NBTTagCompound reverseNBT(NBTTagCompound nbt, String nbtName) {
		return this.setNBT(nbt, nbtName, !this.getItemNBTBoolean(nbt, nbtName));
	}
	
	public boolean getItemNBTBoolean(NBTTagCompound nbt, String nbtName) {
		return nbt.getBoolean(nbtName);
	}
}
