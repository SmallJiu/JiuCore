package cat.jiu.core.api;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import cat.jiu.core.JiuCore;
import cat.jiu.core.net.SimpleMsgHandler;
import cat.jiu.core.net.msg.MsgUpdateTileEntity;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.management.PlayerChunkMapEntry;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.WorldServer;

public interface ITileEntityUpdate {
	/**
	 * 收到更新包后的回调方法
	 * @param nbt 收到的更新包
	 */
	void onTileEntityUpdatePacket(@Nonnull NBTTagCompound nbt);
	/**
	 * 获取更新包
	 * @param nbt 确定更新时提供的一个新的包，可自己重新new或使用提供的包
	 * @return 需要更新的有效的数据, 为 'null' 时则放弃本次更新
	 */
	@Nullable
	NBTTagCompound getTileEntityUpdatePacket(@Nonnull NBTTagCompound nbt);
	
	static void updateTileEntity(TileEntity te) {
		PlayerChunkMapEntry trackingEntry = ((WorldServer)te.getWorld()).getPlayerChunkMap().getEntry(te.getPos().getX() >> 4, te.getPos().getZ() >> 4);
        if (trackingEntry != null) {
            for(EntityPlayerMP player : trackingEntry.getWatchingPlayers()) {
            	NBTTagCompound packet = te instanceof ITileEntityUpdate ? ((ITileEntityUpdate)te).getTileEntityUpdatePacket(new NBTTagCompound()) : te.getUpdateTag();
            	if(packet!=null) {
            		SimpleMsgHandler.sendMessageToPlayer(JiuCore.MODID, new MsgUpdateTileEntity(te.getPos(), packet), player);
            	}
            }
        }
	}
}
