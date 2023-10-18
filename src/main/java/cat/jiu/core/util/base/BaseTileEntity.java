package cat.jiu.core.util.base;

import java.math.BigInteger;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import cat.jiu.core.JiuCore;
import cat.jiu.core.api.ITileEntityUpdate;
import cat.jiu.core.capability.CapabilityJiuEnergy;
import cat.jiu.core.capability.JiuEnergyStorage;
import cat.jiu.core.net.SimpleMsgHandler;
import cat.jiu.core.net.msg.MsgUpdateTileEntity;
import cat.jiu.core.util.JiuUtils;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.management.PlayerChunkMapEntry;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;

public class BaseTileEntity {
	public static class Normal extends TileEntity implements ITickable, ITileEntityUpdate {
		private long tick;
		private long lastUpdateTick;
		private int updateIntervalTick = 5;
		private boolean needUpdate = false;
		/**请重写以下方法，以获得更新数据<p>
		 * {@link #getTileEntityUpdatePacket(NBTTagCompound)}<p>
		 * {@link #onTileEntityUpdatePacket(NBTTagCompound)}
		 * @param tick 更新间隔，以Tick为单位
		 */
		public final BaseTileEntity.Normal setUpdataIntervalTick(int tick) {
			this.updateIntervalTick = tick;
			return this;
		}
		/**如果需要更新TileEntity，请重写<p>
		 * {@link #getTileEntityUpdatePacket(NBTTagCompound)}<p>
		 * {@link #onTileEntityUpdatePacket(NBTTagCompound)}
		 * @param needUpdate 是否需要更新此TileEntity
		 */
		public final BaseTileEntity.Normal setNeedUpdate(boolean needUpdate) {
			this.needUpdate = needUpdate;
			return this;
		}
		public long getAliveTick() {
			return this.tick;
		}
		@Override
		public void update() {
			this.markDirty();
			this.tick++;
			if(!this.world.isRemote && this.needUpdate) {
				if(this.tick - this.lastUpdateTick >= this.updateIntervalTick) {
					this.lastUpdateTick = this.tick;
					
					PlayerChunkMapEntry trackingEntry = ((WorldServer)this.world).getPlayerChunkMap().getEntry(this.pos.getX() >> 4, this.pos.getZ() >> 4);
			        if (trackingEntry != null) {
			            for(EntityPlayerMP player : trackingEntry.getWatchingPlayers()) {
			            	this.sendUpdateToClient(player);
			            }
			        }
				}
			}
			this.tick(this.world, this.pos, this.world.getBlockState(this.pos));
		}
		@Deprecated
		public void tick(World world, BlockPos pos, IBlockState state) {};
		/**
		 * 发送更新到客户端的回调方法，重写可发送自己的网络包
		 * @param player
		 */
		protected void sendUpdateToClient(EntityPlayerMP player) {
			NBTTagCompound packet = this.getTileEntityUpdatePacket(new NBTTagCompound());
        	if(packet!=null) {
        		SimpleMsgHandler.sendMessageToPlayer(JiuCore.MODID, new MsgUpdateTileEntity(this.getPos(), packet), player);
        	}
		}
		
		@Override
		public void onTileEntityUpdatePacket(@Nonnull NBTTagCompound nbt) {}
		@Override
		@Nullable
		public NBTTagCompound getTileEntityUpdatePacket(@Nonnull NBTTagCompound nbt) {
			return null;
		}
		@Override
		public void readFromNBT(NBTTagCompound nbt) {
			super.readFromNBT(nbt);
			this.readNBT(nbt);
		}
		@Deprecated
		public void readNBT(NBTTagCompound nbt) {};
		
		@Override
		public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
			super.writeToNBT(nbt);
			this.writeNBT(nbt);
			return nbt;
		}
		@Deprecated
		public void writeNBT(NBTTagCompound nbt) {};
		
		@SuppressWarnings("unchecked")
		protected <T extends TileEntity> T getInstance() {
			return (T) this.blockType.createTileEntity(this.world, this.world.getBlockState(this.pos));
		}
	}
	
	public static class Energy extends Normal {
		public final JiuEnergyStorage storage;
		public long energy;
		
		public Energy(JiuEnergyStorage storage) {
			this.storage = storage;
			this.energy = this.storage.getEnergyStoredWithLong();
			this.setNeedUpdate(true);
		}
		
		@Override
		public void onTileEntityUpdatePacket(@Nonnull NBTTagCompound nbt) {
			this.storage.read(nbt.getCompoundTag("energy"));
		}
		
		@Nullable
		@Override
		public NBTTagCompound getTileEntityUpdatePacket(@Nonnull NBTTagCompound nbt) {
			nbt.setTag("energy", this.storage.write(new NBTTagCompound()));
			return nbt;
		}
		@Override
		public void update() {
			super.update();
			this.energy = this.storage.getEnergyStoredWithLong();
		}
		
		@Deprecated
		public void tick(World world, BlockPos pos, IBlockState state) {
			this.updateTE(this.world, this.pos, this.world.getBlockState(this.pos));
		}
		@Deprecated
		public void updateTE(World world, BlockPos pos, IBlockState state) {};
		
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
		public void readFromNBT(NBTTagCompound nbt) {
			super.readFromNBT(nbt);
			this.storage.readFrom(nbt.getCompoundTag("energy"));
			this.energy = this.storage.getEnergyStoredWithLong();
		}
		
		@Deprecated
		public void readNBT(NBTTagCompound nbt) {
			this.readFromTeNBT(nbt);
		}
		@Deprecated
		public void readFromTeNBT(NBTTagCompound nbt) {};

		@Override
		public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
			nbt = super.writeToNBT(nbt);
			nbt.setTag("energy", this.storage.writeTo(NBTTagCompound.class));
			return nbt;
		}
		@Deprecated
		public void writeNBT(NBTTagCompound nbt) {
			this.writeToTeNBT(nbt);
		}
		@Deprecated
		public void writeToTeNBT(NBTTagCompound nbt) {};
	}
}
