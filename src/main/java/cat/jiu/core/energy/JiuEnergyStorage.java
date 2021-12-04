package cat.jiu.core.energy;

import net.minecraftforge.energy.EnergyStorage;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class JiuEnergyStorage extends EnergyStorage {
	public JiuEnergyStorage() {
		this(Integer.MAX_VALUE, 1000, 1000, 0);
	}
	
	public JiuEnergyStorage(int maxEnergy) {
		this(maxEnergy, 1000, 1000, 0);
	}
	
	public JiuEnergyStorage(int maxEnergy, int maxInput) {
		this(maxEnergy, maxInput, 1000, 0);
	}
	
	public JiuEnergyStorage(int maxEnergy, int maxInput, int maxOutput) {
		this(maxEnergy, maxInput, maxOutput, 0);
	}
	
	// 最大能量存储, 最大能量输入, 最大能量输出, 能量初始值
	public JiuEnergyStorage(int maxEnergy, int maxInput, int maxOutput, int energy) {
		super(maxEnergy, maxInput, maxOutput, energy);
	}
	
	@Override
	public int receiveEnergy(int maxReceive, boolean simulate) {
		return super.receiveEnergy(maxReceive, simulate);
	}
	
	public int inputEnergy(int energy, boolean simulate) {
		return this.receiveEnergy(energy, simulate);
	}
	
	public int addEnergy(int energy) {
		return this.inputEnergy(energy, false);
	}
	
	@Override
	public int extractEnergy(int maxExtract, boolean simulate) {
		return super.extractEnergy(maxExtract, simulate);
	}
	
	public int outputEnergy(int energy, boolean simulate) {
		return this.extractEnergy(energy, simulate);
	}
	
	@Override
	public int getEnergyStored() {
		return this.energy;
	}
	
	public int setEnergyStored(int energy) {
		this.energy = energy;
		return this.energy;
	}
	
	@Override
	public int getMaxEnergyStored() {
		return this.capacity;
	}
	
	public JiuEnergyStorage setMaxEnergyStored(int i) {
		this.capacity = i;
		return this;
	}
	
	@Override
	public boolean canReceive() {
		return super.canReceive();
	}
	
	public boolean canInput() {
		return this.canReceive();
	}
	
	public JiuEnergyStorage setMaxInput(int energy) {
		this.maxReceive = energy;
		return this;
	}
	
	public JiuEnergyStorage addMaxInput() {
		return this.addMaxInput(1);
	}
	
	public JiuEnergyStorage addMaxInput(int energy) {
		this.maxReceive = (this.maxReceive + energy);
		return this;
	}
	
	@Override
	public boolean canExtract() {
		return super.canExtract();
	}
	
	public boolean canOutput() {
		return this.canExtract();
	}
	
	public JiuEnergyStorage setMaxOutput(int energy) {
		this.maxExtract = energy;
		return this;
	}
	
	public JiuEnergyStorage addMaxOutput() {
		return this.addMaxOutput(1);
	}
	
	public JiuEnergyStorage addMaxOutput(int energy) {
		this.maxExtract = (this.maxExtract + energy);
		return this;
	}
	
	public ItemStack addEnergyToItemStack(ItemStack stack) {
		NBTTagCompound nbt = stack.getTagCompound() != null ? stack.getTagCompound() : new NBTTagCompound();
		nbt.setInteger("Energy", this.energy);
		stack.setTagCompound(nbt);
		return stack;
	}
	
	public void readFromNBT(NBTTagCompound nbt) {
		if(nbt != null) {
			this.energy = nbt.getInteger("Energy");
			this.capacity = nbt.getInteger("MaxEnergyStored");
			this.maxExtract = nbt.getInteger("MaxOutput");
			this.maxReceive = nbt.getInteger("MaxInput");
		}
	}
	
	public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
		if(nbt == null) {
			nbt = new NBTTagCompound();
		}
		nbt.setInteger("Energy", this.energy);
		nbt.setInteger("MaxEnergyStored", this.capacity);
		nbt.setInteger("MaxOutput", this.maxExtract);
		nbt.setInteger("MaxInput", this.maxReceive);
		
		return nbt;
	}
	
	public static ItemStack setEnergy(ItemStack stack, int energy) {
		NBTTagCompound nbt = stack.getTagCompound() != null ? stack.getTagCompound() : new NBTTagCompound();
		nbt.setInteger("Energy", energy);
		stack.setTagCompound(nbt);
		return stack;
	}
	
	public static ItemStack addEnergy(ItemStack stack, int energy) {
		NBTTagCompound nbt = stack.getTagCompound() != null ? stack.getTagCompound() : new NBTTagCompound();
		nbt.setInteger("Energy", (getEnergy(stack) + energy));
		stack.setTagCompound(nbt);
		return stack;
	}
	
	public static int getEnergy(ItemStack stack) {
		NBTTagCompound nbt = stack.getTagCompound() != null ? stack.getTagCompound() : new NBTTagCompound();
		
		return nbt.getInteger("Energy");
	}
}
