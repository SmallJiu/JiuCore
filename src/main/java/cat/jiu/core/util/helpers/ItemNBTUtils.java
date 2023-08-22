package cat.jiu.core.util.helpers;

import java.math.BigInteger;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

public final class ItemNBTUtils extends NBTTagCompoundUtils {
	public ItemStack setItemNBT(ItemStack stack, String nbtName, String value) {
		stack.setTagCompound(super.setNBT(super.getItemNBT(stack), nbtName, value));
		return stack;
	}
	
	public ItemStack addItemNBT(ItemStack stack, String nbtName, String value) {
		stack.setTagCompound(super.addNBT(super.getItemNBT(stack), nbtName, value));
		return stack;
	}
	
	public String getItemNBTString(ItemStack stack, String nbtName) {
		return super.getNBTString(super.getItemNBT(stack), nbtName);
	}
	//=================================================================================================//
	public ItemStack setItemNBT(ItemStack stack, String nbtName, int value) {
		stack.setTagCompound(super.setNBT(super.getItemNBT(stack), nbtName, value));
		return stack;
	}
	
	public ItemStack addItemNBT(ItemStack stack, String nbtName, int value) {
		stack.setTagCompound(super.addNBT(super.getItemNBT(stack), nbtName, value));
		return stack;
	}
	
	public ItemStack subtractItemNBT(ItemStack stack, String nbtName, int value) {
		stack.setTagCompound(super.subtractNBT(super.getItemNBT(stack), nbtName, value));
		return stack;
	}
	
	public int getItemNBTInt(ItemStack stack, String nbtName) {
		return super.getNBTInt(super.getItemNBT(stack), nbtName);
	}
	//=================================================================================================//
	public ItemStack setItemNBT(ItemStack stack, String nbtName, byte value) {
		stack.setTagCompound(super.setNBT(super.getItemNBT(stack), nbtName, value));
		return stack;
	}
	
	public ItemStack addItemNBT(ItemStack stack, String nbtName, byte value) {
		stack.setTagCompound(super.addNBT(super.getItemNBT(stack), nbtName, value));
		return stack;
	}
	
	public ItemStack subtractItemNBT(ItemStack stack, String nbtName, byte value) {
		stack.setTagCompound(super.subtractNBT(super.getItemNBT(stack), nbtName, value));
		return stack;
	}
	
	public byte getItemNBTByte(ItemStack stack, String nbtName) {
		return super.getNBTByte(super.getItemNBT(stack), nbtName);
	}
	//=================================================================================================//
	public ItemStack setItemNBT(ItemStack stack, String nbtName, long value) {
		stack.setTagCompound(super.setNBT(super.getItemNBT(stack), nbtName, value));
		return stack;
	}
	
	public ItemStack addItemNBT(ItemStack stack, String nbtName, long value) {
		stack.setTagCompound(super.addNBT(super.getItemNBT(stack), nbtName, value));
		return stack;
	}
	
	public ItemStack subtractItemNBT(ItemStack stack, String nbtName, long value) {
		stack.setTagCompound(super.subtractNBT(super.getItemNBT(stack), nbtName, value));
		return stack;
	}
	
	public long getItemNBTLong(ItemStack stack, String nbtName) {
		return super.getNBTLong(super.getItemNBT(stack), nbtName);
	}
	//=================================================================================================//
	public ItemStack setItemNBT(ItemStack stack, String nbtName, double value) {
		stack.setTagCompound(super.setNBT(super.getItemNBT(stack), nbtName, value));
		return stack;
	}
	
	public ItemStack addItemNBT(ItemStack stack, String nbtName, double value) {
		stack.setTagCompound(super.addNBT(super.getItemNBT(stack), nbtName, value));
		return stack;
	}
	
	public ItemStack subtractItemNBT(ItemStack stack, String nbtName, double value) {
		stack.setTagCompound(super.subtractNBT(super.getItemNBT(stack), nbtName, value));
		return stack;
	}
	
	public double getItemNBTDouble(ItemStack stack, String nbtName) {
		return super.getNBTDouble(super.getItemNBT(stack), nbtName);
	}
	//=================================================================================================//
	public ItemStack setItemNBT(ItemStack stack, String nbtName, float value) {
		stack.setTagCompound(super.setNBT(super.getItemNBT(stack), nbtName, value));
		return stack;
	}
	
	public ItemStack addItemNBT(ItemStack stack, String nbtName, float value) {
		stack.setTagCompound(super.addNBT(super.getItemNBT(stack), nbtName, value));
		return stack;
	}
	
	public ItemStack subtractItemNBT(ItemStack stack, String nbtName, float value) {
		stack.setTagCompound(super.subtractNBT(super.getItemNBT(stack), nbtName, value));
		return stack;
	}
	
	public float getItemNBTFloat(ItemStack stack, String nbtName) {
		return super.getNBTFloat(super.getItemNBT(stack), nbtName);
	}
	//=================================================================================================//
	public ItemStack setItemNBT(ItemStack stack, String nbtName, BigInteger value) {
		stack.setTagCompound(super.setNBT(super.getItemNBT(stack), nbtName, value));
		return stack;
	}
	
	public ItemStack addItemNBT(ItemStack stack, String nbtName, BigInteger value) {
		stack.setTagCompound(super.addNBT(super.getItemNBT(stack), nbtName, value));
		return stack;
	}
	
	public ItemStack subtractItemNBT(ItemStack stack, String nbtName, BigInteger value) {
		stack.setTagCompound(super.subtractNBT(super.getItemNBT(stack), nbtName, value));
		return stack;
	}
	
	public BigInteger getItemNBTBigInteger(ItemStack stack, String nbtName) {
		return super.getItemBigInteger(super.getItemNBT(stack), nbtName);
	}
	//=================================================================================================//
	public ItemStack setItemNBT(ItemStack stack, String nbtName, int[] value) {
		stack.setTagCompound(super.setNBT(super.getItemNBT(stack), nbtName, value));
		return stack;
	}
	
	public ItemStack addIntToItemNBTIntArray(ItemStack stack, String nbtName, int value) {
		stack.setTagCompound(super.addIntToNBTIntArray(super.getItemNBT(stack), nbtName, value));
		return stack;
	}
	
	public int[] getItemNBTIntArray(ItemStack stack, String nbtName) {
		return super.getNBTIntArray(super.getItemNBT(stack), nbtName);
	}
	//=================================================================================================//
	public ItemStack setItemNBT(ItemStack stack, String nbtName, boolean value) {
		stack.setTagCompound(super.setNBT(super.getItemNBT(stack), nbtName, value));
		return stack;
	}
	
	public ItemStack reverseItemNBT(ItemStack stack, String nbtName) {
		stack.setTagCompound(super.reverseNBT(super.getItemNBT(stack), nbtName));
		return stack;
	}
	
	public boolean getItemNBTBoolean(ItemStack stack, String nbtName) {
		return super.getItemNBTBoolean(super.getItemNBT(stack), nbtName);
	}
	//=================================================================================================//
	public ItemStack setItemNBT(ItemStack stack, String nbtName, String[] value) {
		stack.setTagCompound(super.setNBT(super.getItemNBT(stack), nbtName, value));
		return stack;
	}
	
	public ItemStack addStringToItemNBTStringArray(ItemStack stack, String nbtName, String value) {
		stack.setTagCompound(super.addStringToNBTStringArray(super.getItemNBT(stack), nbtName, value));
		return stack;
	}
	
	public String[] getItemNBTStringArray(ItemStack stack, String nbtName) {
		return super.getNBTStringArray(super.getItemNBT(stack), nbtName);
	}
	//=================================================================================================//
	public <T extends NBTBase> ItemStack setItemNBT(ItemStack stack, String nbtName, T value) {
		stack.setTagCompound(super.setNBT(super.getItemNBT(stack), nbtName, value));
		return stack;
	}
	public NBTTagCompound getItemNBTTag(ItemStack stack, String nbtName) {
		return super.getItemNBTTag(super.getItemNBT(stack), nbtName);
	}
	
	public NBTTagList getItemNBTTagList(ItemStack stack, String nbtName, Class<? extends NBTBase> clazz) {
		return super.getItemNBTTagList(super.getItemNBT(stack), nbtName, clazz);
	}
}
