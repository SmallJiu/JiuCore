package cat.jiu.core.util.helpers;

import java.math.BigInteger;

import cat.jiu.core.util.JiuUtils;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.INBTSerializable;

public final class NBTUtils {
	public boolean hasNBT(ItemStack stack, String nbtName) {
		if(stack.getTagCompound() == null) {
			return false;
		}
		return stack.getTagCompound().hasKey(nbtName);
	}
	
	public NBTTagCompound getItemNBT(ItemStack stack) {
		NBTTagCompound nbt = stack.getTagCompound();
		return nbt != null ? nbt : new NBTTagCompound();
	}
	
	public void removeItemNBT(ItemStack stack, String nbtName) {
		NBTTagCompound nbt = this.getItemNBT(stack);
		nbt.removeTag(nbtName);
		stack.setTagCompound(nbt);
	}
	//=================================================================================================//
	public void setItemNBT(ItemStack stack, String nbtName, String value) {
		NBTTagCompound nbt = this.getItemNBT(stack);
		nbt.setString(nbtName, value);
		stack.setTagCompound(nbt);
	}
	
	public void addItemNBT(ItemStack stack, String nbtName, String value) {
		this.setItemNBT(stack, nbtName, this.getItemNBTString(stack, nbtName) + value);
	}
	
	public String getItemNBTString(ItemStack stack, String nbtName) {
		NBTTagCompound nbt = this.getItemNBT(stack);
		return nbt.getString(nbtName);
	}
	//=================================================================================================//
	public void setItemNBT(ItemStack stack, String nbtName, int value) {
		NBTTagCompound nbt = this.getItemNBT(stack);
		nbt.setInteger(nbtName, value);
		stack.setTagCompound(nbt);
	}
	
	public void addItemNBT(ItemStack stack, String nbtName, int value) {
		this.setItemNBT(stack, nbtName, this.getItemNBTInt(stack, nbtName) + value);
	}
	
	public void subtractItemNBT(ItemStack stack, String nbtName, int value) {
		this.setItemNBT(stack, nbtName, this.getItemNBTInt(stack, nbtName) - value);
	}
	
	public int getItemNBTInt(ItemStack stack, String nbtName) {
		NBTTagCompound nbt = this.getItemNBT(stack);
		return nbt.getInteger(nbtName);
	}
	//=================================================================================================//
	public void setItemNBT(ItemStack stack, String nbtName, long value) {
		NBTTagCompound nbt = this.getItemNBT(stack);
		nbt.setLong(nbtName, value);
		stack.setTagCompound(nbt);
	}
	
	public void addItemNBT(ItemStack stack, String nbtName, long value) {
		this.setItemNBT(stack, nbtName, this.getItemNBTLong(stack, nbtName) + value);
	}
	
	public long getItemNBTLong(ItemStack stack, String nbtName) {
		NBTTagCompound nbt = this.getItemNBT(stack);
		return nbt.getLong(nbtName);
	}
	//=================================================================================================//
	public void setItemNBT(ItemStack stack, String nbtName, double value) {
		NBTTagCompound nbt = this.getItemNBT(stack);
		nbt.setDouble(nbtName, value);
		stack.setTagCompound(nbt);
	}
	
	public void addItemNBT(ItemStack stack, String nbtName, double value) {
		this.setItemNBT(stack, nbtName, this.getItemNBTDouble(stack, nbtName) + value);
	}
	
	public double getItemNBTDouble(ItemStack stack, String nbtName) {
		NBTTagCompound nbt = this.getItemNBT(stack);
		return nbt.getDouble(nbtName);
	}
	//=================================================================================================//
	public void setItemNBT(ItemStack stack, String nbtName, float value) {
		NBTTagCompound nbt = this.getItemNBT(stack);
		nbt.setFloat(nbtName, value);
		stack.setTagCompound(nbt);
	}
	
	public void addItemNBT(ItemStack stack, String nbtName, float value) {
		this.setItemNBT(stack, nbtName, this.getItemNBTFloat(stack, nbtName) + value);
	}
	
	public float getItemNBTFloat(ItemStack stack, String nbtName) {
		NBTTagCompound nbt = this.getItemNBT(stack);
		return nbt.getFloat(nbtName);
	}
	//=================================================================================================//
	public void setItemNBT(ItemStack stack, String nbtName, BigInteger value) {
		NBTTagCompound nbt = this.getItemNBT(stack);
		nbt.setString(nbtName, value.toString());
		stack.setTagCompound(nbt);
	}
	
	public void addItemNBT(ItemStack stack, String nbtName, BigInteger value) {
		this.setItemNBT(stack, nbtName, this.getItemNBTBigInteger(stack, nbtName).add(value));
	}
	
	public BigInteger getItemNBTBigInteger(ItemStack stack, String nbtName) {
		String v = this.getItemNBT(stack).getString(nbtName);
		if(v.equals("")) {
			return BigInteger.ZERO;
		}
		return new BigInteger(v);
	}
	//=================================================================================================//
	public void setItemNBT(ItemStack stack, String nbtName, int[] value) {
		NBTTagCompound nbt = this.getItemNBT(stack);
		nbt.setIntArray(nbtName, value);
		stack.setTagCompound(nbt);
	}
	
	public void addIntToItemNBTIntArray(ItemStack stack, String nbtName, int value) {
		int leg = this.getItemNBTIntArray(stack, nbtName).length + 1;
		int[] add = new int[leg];
		
		for(int i = 0; i < leg-1; ++i) {
			add[i] = this.getItemNBTIntArray(stack, nbtName)[i];
		}
		add[leg] = value;
		
		this.setItemNBT(stack, nbtName, add);
	}
	
	public int[] getItemNBTIntArray(ItemStack stack, String nbtName) {
		NBTTagCompound nbt = this.getItemNBT(stack);
		return nbt.getIntArray(nbtName);
	}
	//=================================================================================================//
	public void setItemNBT(ItemStack stack, String nbtName, String[] value) {
		NBTTagCompound nbt = this.getItemNBT(stack);
		nbt.setString(nbtName, JiuUtils.other.toString(value));
		stack.setTagCompound(nbt);
	}
	
	public void addStringToItemNBTStringArray(ItemStack stack, String nbtName, String value) {
		int leg = this.getItemNBTStringArray(stack, nbtName).length + 1;
		String[] add = new String[leg];
		
		for(int i = 0; i < leg-1; ++i) {
			add[i] = this.getItemNBTStringArray(stack, nbtName)[i];
		}
		
		add[leg] = value;
		this.setItemNBT(stack, nbtName, JiuUtils.other.toString(add));
	}
	
	public String[] getItemNBTStringArray(ItemStack stack, String nbtName) {
		NBTTagCompound nbt = this.getItemNBT(stack);
		String s = nbt.getString(nbtName);
		if(s.equals("")) {
			return new String[] {"null"};
		}
		return JiuUtils.other.custemSplitString(s, ",");
	}
	//=================================================================================================//
	@SuppressWarnings("rawtypes")
	public void writeSlotItemToNBT(NBTTagCompound nbt, String nbtName, INBTSerializable slot) {
		nbt.setTag(nbtName, slot.serializeNBT());
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void readSlotItemFromNBT(INBTSerializable slot, NBTTagCompound nbt, String nbtName) {
		slot.deserializeNBT(nbt.getTag(nbtName));
	}
	//=================================================================================================//
	public void setItemNBT(ItemStack stack, String nbtName, boolean value) {
		NBTTagCompound nbt = this.getItemNBT(stack);
		nbt.setBoolean(nbtName, value);
		stack.setTagCompound(nbt);
	}
	
	public boolean getItemNBTBoolean(ItemStack stack, String nbtName) {
		NBTTagCompound nbt = this.getItemNBT(stack);
		return nbt.getBoolean(nbtName);
	}
}
