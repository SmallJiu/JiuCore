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
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;

public class JiuEnergyStorage implements IJiuEnergyStorage, ICapabilityProvider {
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
	public boolean canExtract() {
		return !BigInteger.ZERO.equals(this.maxExtract);
	}
	
	@Override
	public boolean canReceive() {
		return !BigInteger.ZERO.equals(this.maxReceive);
	}
	
	public void setEnergy(long energy) {
		this.setEnergy(BigInteger.valueOf(energy));
	}
	
	public void setEnergy(BigInteger energy) {
		this.energy = energy;
		this.check();
	}
	
	public void setMaxEnergy(long energy) {
		this.setMaxEnergy(BigInteger.valueOf(energy));
	}
	
	public void setMaxEnergy(BigInteger energy) {
		this.maxEnergy = energy;
		this.check();
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
	
	public void readFromNBT(@Nonnull NBTTagCompound nbt, boolean readAll) {
		if(nbt != null) {
			this.energy = JiuUtils.big_integer.create(nbt.getString("Energy"));
			if(readAll) {
				this.maxEnergy = JiuUtils.big_integer.create(nbt.getString("MaxEnergyStored"));
				this.maxExtract = JiuUtils.big_integer.create(nbt.getString("MaxOutput"));
				this.maxReceive = JiuUtils.big_integer.create(nbt.getString("MaxInput"));
			}
		}
	}
	
	public NBTTagCompound writeToNBT(@Nullable NBTTagCompound nbt, boolean saveAll) {
		if(nbt == null) {
			nbt = new NBTTagCompound();
		}
		nbt.setString("Energy", this.energy.toString());
		if(saveAll) {
			nbt.setString("MaxEnergyStored", this.maxEnergy.toString());
			nbt.setString("MaxOutput", this.maxExtract.toString());
			nbt.setString("MaxInput", this.maxReceive.toString());
		}
		
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
