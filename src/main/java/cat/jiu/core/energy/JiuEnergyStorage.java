package cat.jiu.core.energy;

import net.minecraftforge.energy.EnergyStorage;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class JiuEnergyStorage extends EnergyStorage {
	public JiuEnergyStorage() {
		this(Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, 0);
	}
	
	public JiuEnergyStorage(int maxEnergy) {
		this(maxEnergy, Integer.MAX_VALUE, Integer.MAX_VALUE, 0);
	}
	
	public JiuEnergyStorage(int maxEnergy, int maxInput) {
		this(maxEnergy, maxInput, Integer.MAX_VALUE, 0);
	}
	
	public JiuEnergyStorage(int maxEnergy, int maxInput, int maxOutput) {
		this(maxEnergy, maxInput, maxOutput, 0);
	}
	
	// 最大能量存储, 最大能量输入, 最大能量输出, 能量初始值
	public JiuEnergyStorage(int maxEnergy, int maxInput, int maxOutput, int energy) {
		super(maxEnergy, maxInput, maxOutput, energy);
	}
	
	private ItemStack stack = null;
	
	public JiuEnergyStorage(ItemStack stack) {
		this(Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, getEnergy(stack));
	}
	
	public JiuEnergyStorage(ItemStack stack, int maxEnergy) {
		this(maxEnergy, Integer.MAX_VALUE, Integer.MAX_VALUE, getEnergy(stack));
	}
	
	public JiuEnergyStorage(ItemStack stack, int maxEnergy, int maxInput) {
		this(maxEnergy, maxInput, Integer.MAX_VALUE, getEnergy(stack));
	}
	
	public JiuEnergyStorage(ItemStack stack, int maxEnergy, int maxInput, int maxOutput) {
		super(maxEnergy, maxInput, maxOutput, getEnergy(stack));
		this.stack = stack;
	}
	
	@Override
	public int receiveEnergy(int maxReceive, boolean simulate) {
		return super.receiveEnergy(maxReceive, simulate);
	}
	
	public int inputEnergy(int energy, boolean simulate) {
		return this.receiveEnergy(energy, simulate);
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
		return this.stack != null ? getEnergy(this.stack) : this.energy;
	}
	
	public int setEnergyStored(int i) {
		int j = (this.energy = i) >= this.capacity ? this.capacity : i;
		
		this.energy = j;
		if(!this.stack.isEmpty()) {
			setEnergy(this.stack, j);
		}
		return j;
	}
	
	public int addEnergyStored() {
		return this.addEnergyStored(1);
	}
	
	public int addEnergyStored(int i) {
		int j = (this.energy + i) > this.capacity ? this.capacity : (this.energy + i);
		this.energy = j;
		if(!this.stack.isEmpty()) {
			addEnergy(stack, j);
		}
		return j;
	}
	
	public int consumesEnergyStored() {
		return this.consumesEnergyStored(1);
	}
	
	public int consumesEnergyStored(int i) {
		this.energy = (this.energy - i) <= 0 ? 0 : (this.energy - i);
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
	
	public JiuEnergyStorage addMaxEnergyStored() {
		return this.addMaxEnergyStored(1);
	}
	
	public JiuEnergyStorage addMaxEnergyStored(int i) {
		this.capacity = (this.capacity + i);
		return this;
	}
	
	@Override
	public boolean canReceive() {
		return super.canReceive();
	}
	
	public boolean canInput() {
		return this.canReceive();
	}
	
	public JiuEnergyStorage setMaxInput(int i) {
		this.maxReceive = i;
		return this;
	}
	
	public JiuEnergyStorage addMaxInput() {
		return this.addMaxInput(1);
	}
	
	public JiuEnergyStorage addMaxInput(int i) {
		this.maxReceive = (this.maxReceive + i);
		return this;
	}
	
	@Override
	public boolean canExtract() {
		return super.canExtract();
	}
	
	public boolean canOutput() {
		return this.canExtract();
	}
	
	public JiuEnergyStorage setMaxOutput(int i) {
		this.maxExtract = i;
		return this;
	}
	
	public JiuEnergyStorage addMaxOutput() {
		return this.addMaxOutput(1);
	}
	
	public JiuEnergyStorage addMaxOutput(int i) {
		this.maxExtract = (this.maxExtract + i);
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
