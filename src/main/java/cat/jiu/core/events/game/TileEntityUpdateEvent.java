package cat.jiu.core.events.game;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event;

public class TileEntityUpdateEvent extends Event {
	public final TileEntity tile;
	public TileEntityUpdateEvent(TileEntity tile) {
		this.tile = tile;
	}
	
	public static class Pre extends TileEntityUpdateEvent {
		public Pre(TileEntity tile) {
			super(tile);
		}
	}
	
	public static class Post extends TileEntityUpdateEvent {
		public Post(TileEntity tile) {
			super(tile);
		}
	}
	
	public static class Packet extends TileEntityUpdateEvent {
		public Packet(TileEntity tile) {
			super(tile);
		}
		/**
		 * 获取 TileEntity 更新数据的事件
		 * @author small_jiu
		 */
		public static class Getter extends TileEntityUpdateEvent {
			protected NBTTagCompound packet;
			public Getter(TileEntity tile) {
				super(tile);
				this.packet = tile.getUpdateTag();
			}
			public NBTTagCompound getPacket() {
				return packet;
			}
			public void setPacket(NBTTagCompound packet) {
				this.packet = packet;
			}
		}
		/**
		 * 更新 TileEntity 数据的事件
		 * @author small_jiu
		 */
		@Cancelable
		public static class Handler extends TileEntityUpdateEvent {
			public final NBTTagCompound packet;
			public Handler(TileEntity tile, NBTTagCompound packet) {
				super(tile);
				this.packet = packet;
			}
		}
	}
}
