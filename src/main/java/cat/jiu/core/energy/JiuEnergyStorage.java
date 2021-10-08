package cat.jiu.core.energy;

import net.minecraftforge.energy.EnergyStorage;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class JiuEnergyStorage extends EnergyStorage {
	
	public JiuEnergyStorage() {
		this(1000, 1000, 1000, 0);
	}
	
	public JiuEnergyStorage(int capacity) {
		this(capacity, capacity, capacity, 0);
	}

	public JiuEnergyStorage(int capacity, int maxTransfer) {
		this(capacity, maxTransfer, maxTransfer, 0);
	}

	public JiuEnergyStorage(int capacity, int maxReceive, int maxExtract) {
		this(capacity, maxReceive, maxExtract, 0);
	}

	public JiuEnergyStorage(int capacity, int maxReceive, int maxExtract, int energy) {
		super(capacity, maxReceive, maxExtract, energy);
	}

	@Override
	public int receiveEnergy(int maxReceive, boolean simulate) {
		return super.receiveEnergy(maxReceive, simulate);
	}

	@Override
	public int extractEnergy(int maxExtract, boolean simulate) {
		return super.extractEnergy(maxExtract, simulate);
	}

	@Override
	public int getEnergyStored() {
		return super.getEnergyStored();
	}

	@Override
	public int getMaxEnergyStored() {
		return super.getMaxEnergyStored();
	}

	@Override
	public boolean canReceive() {
		return super.canReceive();
	}

	@Override
	public boolean canExtract() {
		return super.canExtract();
	}
	
	public static int getEnergy(ItemStack stack) {
		NBTTagCompound nbt = stack.getTagCompound();
		if(nbt == null || !nbt.hasKey("Energy")) {
			return 0;
		}else {
			return nbt.getInteger("Energy");
		}
	}
	
	public void readFromNBT(NBTTagCompound nbt) {
		if(nbt != null) {
			this.energy = nbt.getInteger("Energy");
			this.capacity = nbt.getInteger("Capacity");
			this.maxExtract = nbt.getInteger("MaxExtract");
			this.maxReceive = nbt.getInteger("MaxReceive");
		}
	}
	
	public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
		if(nbt == null) {
			nbt = new NBTTagCompound();
		}
		nbt.setInteger("Energy", this.energy);
		nbt.setInteger("Capacity", this.capacity);
		nbt.setInteger("MaxExtract", this.maxExtract);
		nbt.setInteger("MaxReceive", this.maxReceive);
		
		return nbt;
	}
}
