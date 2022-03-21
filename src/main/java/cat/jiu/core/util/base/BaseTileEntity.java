package cat.jiu.core.util.base;

import cat.jiu.core.energy.CapabilityJiuEnergy;
import cat.jiu.core.energy.JiuEnergyStorage;
import cat.jiu.core.util.JiuUtils;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;

public class BaseTileEntity {
	public static abstract class Normal extends TileEntity implements ITickable {
		@Override
		public abstract void update();
		@Override
		public void readFromNBT(NBTTagCompound nbt) {
			super.readFromNBT(nbt);
			this.readNBT(nbt);
		}
		/**
		 * Don't need super {@link #readFromNBT(NBTTagCompound)}.<p>
		 * But you need super the extends class {@link #readNBT(NBTTagCompound)}.
		 */
		public abstract void readNBT(NBTTagCompound nbt);
		
		@Override
		public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
			this.writeNBT(nbt);
			return super.writeToNBT(nbt);
		}
		/**
		 * Don't need super {@link #writeToNBT(NBTTagCompound)}.<p>
		 * But you need super the extends class {@link #writeNBT(NBTTagCompound)}.
		 */
		public abstract void writeNBT(NBTTagCompound nbt);
	}
	
	public static class FEEnergy extends Normal {
		protected final JiuEnergyStorage storage;
		public long energy = 0;
		
		public FEEnergy(JiuEnergyStorage storage) {
			this.storage = storage;
			this.energy = this.storage.getEnergyStoredWithLong();
		}
		
		@Override
		public void update() {
			this.reloadEnergy();
		}
		
		protected void reloadEnergy() {
			this.energy = this.storage.getEnergyStoredWithLong();
		}
		
		protected long getEnergy() {
			return this.storage.getEnergyStoredWithLong();
		}
		
		public int sendEnergyTo(int energy, EnumFacing side, boolean simulate) {
			return JiuUtils.energy.sendFEEnergyTo(this, energy, side, simulate);
		}
		
		public int sendEnergyToAll(int energy, boolean simulate) {
			return JiuUtils.energy.sendFEEnergyToAll(this, energy, simulate);
		}
		
		@Override
		public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
			if(capability == CapabilityEnergy.ENERGY) {
				return true;
			}
			if(capability == CapabilityJiuEnergy.ENERGY) {
				return true;
			}
			return super.hasCapability(capability, facing);
		}
		
		@Override
		public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
			if(capability == CapabilityEnergy.ENERGY) {
				return CapabilityEnergy.ENERGY.cast(this.storage.toFEStorage());
			}
			if(capability == CapabilityJiuEnergy.ENERGY) {
				return CapabilityJiuEnergy.ENERGY.cast(this.storage);
			}
			return super.getCapability(capability, facing);
		}
		
		@Override
		public void readNBT(NBTTagCompound nbt) {
			this.storage.readFromNBT(nbt);
			this.energy = this.storage.getEnergyStoredWithLong();
		}
		
		@Override
		public void writeNBT(NBTTagCompound nbt) {
			this.storage.writeToNBT(nbt);
		}
	}
}
