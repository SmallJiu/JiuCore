package cat.jiu.core.capability;

import java.math.BigInteger;

import cat.jiu.core.api.IJiuEnergyStorage;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;

public class JiuEnergyStorage implements IJiuEnergyStorage, ICapabilityProvider {
	protected BigInteger energy;
    protected BigInteger maxEnergy;
    protected BigInteger maxReceive;
    protected BigInteger maxExtract;
	
	public JiuEnergyStorage() {
		this(Long.MAX_VALUE);
	}
	
	public JiuEnergyStorage(long maxEnergy) {
		this(maxEnergy, 1000, 1000, 0);
	}
	
	public JiuEnergyStorage(long maxEnergy, long maxInput) {
		this(maxEnergy, maxInput, 1000, 0);
	}
	
	public JiuEnergyStorage(long maxEnergy, long maxInput, long maxOutput) {
		this(maxEnergy, maxInput, maxOutput, 0);
	}
	
	public JiuEnergyStorage(long maxEnergy, long maxInput, long maxOutput, long energy) {
		this(BigInteger.valueOf(maxEnergy), BigInteger.valueOf(maxInput), BigInteger.valueOf(maxOutput), BigInteger.valueOf(energy));
	}
	
	public JiuEnergyStorage(BigInteger maxEnergy) {
		this(maxEnergy, BigInteger.valueOf(1000));
	}
	
	public JiuEnergyStorage(BigInteger maxEnergy, BigInteger maxInput) {
		this(maxEnergy, maxInput, BigInteger.valueOf(1000));
	}
	
	public JiuEnergyStorage(BigInteger maxEnergy, BigInteger maxInput, BigInteger maxOutput) {
		this(maxEnergy, maxInput, maxOutput, BigInteger.ZERO);
	}
	
	// 最大能量存储, 最大能量输入, 最大能量输出, 能量初始值
	public JiuEnergyStorage(BigInteger maxEnergy, BigInteger maxInput, BigInteger maxOutput, BigInteger energy) {
		this.maxEnergy = maxEnergy;
		this.maxReceive = maxInput;
		this.maxExtract = maxOutput;
		this.energy = BigInteger.ZERO.max(maxEnergy.min(energy));
	}
	
	@Override
	public BigInteger receiveEnergyWithBigInteger(BigInteger maxReceive, boolean simulate) {
		if (!this.canReceive()) {
			return BigInteger.ZERO;
		}
		
		BigInteger energyReceived = this.maxEnergy.subtract(this.energy).min(this.maxReceive.min(maxReceive));
        if(!simulate) {
        	this.energy = this.energy.add(energyReceived);
        }
        
        return energyReceived;
	}
	
	@Override
	public BigInteger extractEnergyWithBigInteger(BigInteger maxExtract, boolean simulate) {
		if(!this.canExtract()) {
			return BigInteger.ZERO;
		}
		
		BigInteger energyExtracted = this.energy.min(this.maxExtract.min(maxExtract));
        if(!simulate) {
        	this.energy = this.energy.subtract(energyExtracted);
        }
        return energyExtracted;
	}
	
	@Override
	public BigInteger getEnergyStoredWithBigInteger() {
		return this.energy;
	}
	
	@Override
	public BigInteger getMaxEnergyStoredWithBigInteger() {
		return this.maxEnergy;
	}
	
	@Override
	public boolean canExtract() {
		return !BigInteger.ZERO.equals(this.maxExtract);
	}
	
	@Override
	public boolean canReceive() {
		return !BigInteger.ZERO.equals(this.maxReceive);
	}
	
	public void setEnergy(long energy) {
		this.energy = BigInteger.valueOf(energy);
	}
	
	public void setEnergy(BigInteger energy) {
		this.energy = energy;
	}
	
	public void setMaxEnergy(long energy) {
		this.maxEnergy = BigInteger.valueOf(energy);
	}
	
	public void setMaxEnergy(BigInteger energy) {
		this.maxEnergy = energy;
	}
	
	public void setMaxInput(long energy) {
		this.maxReceive = BigInteger.valueOf(energy);
	}
	
	public void setMaxInput(BigInteger energy) {
		this.maxReceive = energy;
	}
	
	public void setMaxOutput(long energy) {
		this.maxExtract = BigInteger.valueOf(energy);
	}
	
	public void setMaxOutput(BigInteger energy) {
		this.maxExtract = energy;
	}
	
	public int addEnergyToItemStack(ItemStack stack) {
		if(stack.hasCapability(CapabilityEnergy.ENERGY, null)) {
			IEnergyStorage storage = stack.getCapability(CapabilityEnergy.ENERGY, null);
			return storage.receiveEnergy(this.extractEnergyWithBigInteger(BigInteger.valueOf(energy.intValue()), false).intValue(), false);
		}
		return 0;
	}
	
	public void readFromNBT(NBTTagCompound nbt) {
		if(nbt != null) {
			String energy = nbt.getString("Energy");
			this.energy = new BigInteger(energy.equals("") || energy.isEmpty() ? "0" : energy);
			
			String maxEnergy = nbt.getString("MaxEnergyStored");
			this.maxEnergy = new BigInteger(maxEnergy.equals("") || maxEnergy.isEmpty() ? "0" : maxEnergy);
			
			String maxOut = nbt.getString("MaxOutput");
			this.maxExtract = new BigInteger(maxOut.equals("") || maxOut.isEmpty() ? "0" : maxOut);
			
			String maxIn = nbt.getString("MaxInput");
			this.maxReceive = new BigInteger(maxIn.equals("") || maxIn.isEmpty() ? "0" : maxIn);
		}
	}
	
	public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
		if(nbt == null) {
			nbt = new NBTTagCompound();
		}
		nbt.setString("Energy", this.energy.toString());
		nbt.setString("MaxEnergyStored", this.maxEnergy.toString());
		nbt.setString("MaxOutput", this.maxExtract.toString());
		nbt.setString("MaxInput", this.maxReceive.toString());
		
		return nbt;
	}
	
	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
		if(capability == CapabilityEnergy.ENERGY
		|| capability == CapabilityJiuEnergy.ENERGY) {
			return true;
		}
		return false;
	}
	
	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
		if (capability == CapabilityEnergy.ENERGY) {
            return CapabilityEnergy.ENERGY.cast(this.toFEStorage());
        }
		if(capability == CapabilityJiuEnergy.ENERGY) {
			return CapabilityJiuEnergy.ENERGY.cast(this);
		}
		return null;
	}
}
