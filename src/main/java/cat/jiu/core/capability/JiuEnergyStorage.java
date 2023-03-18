package cat.jiu.core.capability;

import java.math.BigInteger;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import cat.jiu.core.api.IJiuEnergyStorage;
import cat.jiu.core.util.JiuUtils;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;

public class JiuEnergyStorage implements IJiuEnergyStorage {
	private static final JiuEnergyStorage EMPTY = new JiuEnergyStorage(0, 0, 0, 0);
	public static JiuEnergyStorage empty() {return EMPTY.copy();}
	protected BigInteger energy;
    protected BigInteger maxEnergy;
    protected BigInteger maxReceive;
    protected BigInteger maxExtract;
	
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
	public BigInteger getMaxExtractWithBigInteger() {
		return maxExtract;
	}
	@Override
	public BigInteger getMaxReceiveWithBigInteger() {
		return maxReceive;
	}
	
	@Override
	public boolean canExtract() {
		return JiuUtils.big_integer.greater(this.maxExtract, BigInteger.ZERO);
	}
	
	@Override
	public boolean canReceive() {
		return JiuUtils.big_integer.greater(this.maxReceive, BigInteger.ZERO);
	}
	
	public void setEnergy(long energy) {
		this.setEnergy(BigInteger.valueOf(energy));
	}

	@Override
	public void setEnergy(BigInteger energy) {
		this.energy = energy;
		this.check();
	}
	
	public void setMaxEnergy(long energy) {
		this.setMaxEnergy(BigInteger.valueOf(energy));
	}

	@Override
	public void setMaxEnergy(BigInteger energy) {
		this.maxEnergy = energy;
		this.check();
	}
	
	public void setMaxInput(long energy) {
		this.maxReceive = BigInteger.valueOf(energy);
	}

	@Override
	public void setMaxReceive(BigInteger energy) {
		this.maxReceive = energy;
	}
	
	public void setMaxOutput(long energy) {
		this.maxExtract = BigInteger.valueOf(energy);
	}

	@Override
	public void setMaxExtract(BigInteger energy) {
		this.maxExtract = energy;
	}
	
	public int addEnergyToItemStack(ItemStack stack) {
		if(stack.hasCapability(CapabilityEnergy.ENERGY, null)) {
			IEnergyStorage storage = stack.getCapability(CapabilityEnergy.ENERGY, null);
			long i = 0;
			int[] energys = JiuUtils.other.perseInt(this.getMaxEnergyStoredWithBigInteger());
			for(int j = 0; j < energys.length; j++) {
				i += storage.receiveEnergy((int) this.extractEnergyWithLong(energys[j], false), false);
			}
			return (int) i;
		}
		return 0;
	}
	
	public void check() {
		if(JiuUtils.big_integer.greater(this.energy, this.maxEnergy)) {
			this.energy = JiuUtils.big_integer.copy(this.maxEnergy);
		}else if(JiuUtils.big_integer.less(this.energy, BigInteger.ZERO)) {
			this.energy = BigInteger.ZERO;
		}
	}
	
	@Override
	public JiuEnergyStorage copy() {
		return new JiuEnergyStorage(this.maxEnergy, this.maxReceive, this.maxExtract, this.energy);
	}
	
	@Deprecated
	public void readFromNBT(@Nonnull NBTTagCompound nbt, boolean readAll) {
		this.read(nbt);
	}
	@Deprecated
	public NBTTagCompound writeToNBT(@Nullable NBTTagCompound nbt, boolean saveAll) {
		return this.write(nbt);
	}
	
	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
		return IJiuEnergyStorage.super.hasCapability(capability, facing) || capability == CapabilityEnergy.ENERGY;
	}
	
	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
		T result = IJiuEnergyStorage.super.getCapability(capability, facing);
		if(result!=null) {
			return result;
		}
		if(capability == CapabilityJiuEnergy.ENERGY) {
			return CapabilityJiuEnergy.ENERGY.cast(this);
		}
		return null;
	}
}
