package cat.jiu.core.util.base;

import java.math.BigInteger;

import cat.jiu.core.capability.CapabilityJiuEnergy;
import cat.jiu.core.capability.JiuEnergyStorage;
import cat.jiu.core.util.JiuUtils;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;

public class BaseTileEntity {
	public static abstract class Normal extends TileEntity implements ITickable {
		@Override
		public final void update() {
			this.markDirty();
			this.tick(this.world, this.pos, this.world.getBlockState(this.pos));
		}
		public abstract void tick(World world, BlockPos pos, IBlockState state);
		@Override
		public final void readFromNBT(NBTTagCompound nbt) {
			super.readFromNBT(nbt);
			this.readNBT(nbt);
		}
		public abstract void readNBT(NBTTagCompound nbt);
		
		@Override
		public final NBTTagCompound writeToNBT(NBTTagCompound nbt) {
			super.writeToNBT(nbt);
			this.writeNBT(nbt);
			return nbt;
		}
		public abstract void writeNBT(NBTTagCompound nbt);
		
		@SuppressWarnings("unchecked")
		protected <T extends TileEntity> T getInstance() {
			return (T) this.blockType.createTileEntity(this.world, this.world.getBlockState(this.pos));
		}
	}
	
	public static abstract class Energy extends Normal {
		protected JiuEnergyStorage storage;
		public long energy = 0;
		
		public Energy(JiuEnergyStorage storage) {
			this.storage = storage;
			this.energy = this.storage.getEnergyStoredWithLong();
		}
		
		@Override
		public final void tick(World world, BlockPos pos, IBlockState state) {
			this.energy = this.storage.getEnergyStoredWithLong();
			this.updateTE(this.world, this.pos, this.world.getBlockState(this.pos));
		}
		
		public abstract void updateTE(World world, BlockPos pos, IBlockState state);
		
		public BigInteger getEnergy() {
			return this.storage.getEnergyStoredWithBigInteger();
		}
		
		public int sendEnergyTo(int energy, EnumFacing side, boolean simulate) {
			return JiuUtils.energy.sendFEEnergyTo(this, energy, side, simulate);
		}
		
		public int sendEnergyToAll(int energy, boolean simulate) {
			return JiuUtils.energy.sendFEEnergyToAll(this, energy, simulate);
		}
		
		@Override
		public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
			if(capability == CapabilityEnergy.ENERGY
			|| capability == CapabilityJiuEnergy.ENERGY) {
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
		public final void readNBT(NBTTagCompound nbt) {
			if(this.storage == null) this.storage = JiuEnergyStorage.empty();
			this.storage.readFromNBT(nbt.getCompoundTag("energy"), false);
			this.energy = this.storage.getEnergyStoredWithLong();
			this.readFromTeNBT(nbt);
		}
		public abstract void readFromTeNBT(NBTTagCompound nbt);
		
		@Override
		public final void writeNBT(NBTTagCompound nbt) {
			nbt.setTag("energy", this.storage.writeToNBT(null, false));
			this.writeToTeNBT(nbt);
		}
		public abstract void writeToTeNBT(NBTTagCompound nbt);
	}
}
