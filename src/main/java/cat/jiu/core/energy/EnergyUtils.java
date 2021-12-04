package cat.jiu.core.energy;

import java.math.BigInteger;

import cat.jiu.core.util.JiuUtils;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class EnergyUtils {
	private final ItemStack stack;
	private NBTTagCompound nbt;
	private final String energyName;
	
	public EnergyUtils(ItemStack stack, String energyName) {
		this.stack = stack;
		this.energyName = energyName;
		this.nbt = JiuUtils.nbt.getItemNBT(stack);
	}
	
	public void setEnergy(int energy) {
		JiuUtils.nbt.setItemNBT(stack, this.energyName, energy);
		this.nbt = this.stack.getTagCompound();
	}
	
	public void addEnergy(int energy) {
		JiuUtils.nbt.addItemNBT(stack, this.energyName, energy);
		this.nbt = this.stack.getTagCompound();
	}
	
	public void setEnergy(long energy) {
		JiuUtils.nbt.setItemNBT(stack, this.energyName, energy);
		this.nbt = this.stack.getTagCompound();
	}
	
	public void addEnergy(long energy) {
		JiuUtils.nbt.addItemNBT(stack, this.energyName, energy);
		this.nbt = this.stack.getTagCompound();
	}
	
	public void setEnergy(BigInteger energy) {
		JiuUtils.nbt.setItemNBT(stack, this.energyName, energy);
		this.nbt = this.stack.getTagCompound();
	}
	
	public void addEnergy(BigInteger energy) {
		JiuUtils.nbt.addItemNBT(stack, this.energyName, energy);
		this.nbt = this.stack.getTagCompound();
	}
	
	public void consumesEnergy(BigInteger energy) {
		this.setEnergy(this.getEnergyWithBigInteger().subtract(energy));
		this.stack.setTagCompound(this.nbt);
		this.nbt = this.stack.getTagCompound();
	}
	
	public BigInteger getEnergyWithBigInteger() {
		String v = this.nbt.getString(this.energyName);
		if(v.equals("")) {
			return BigInteger.ZERO;
		}
		return new BigInteger(v);
	}
	
	public long getEnergyWihtLong() { return this.nbt.getLong(this.energyName); }
	public int getEnergyWithInt() { return this.nbt.getInteger(this.energyName); }
	public NBTTagCompound getNBT() { return this.nbt; }
}
