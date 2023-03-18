package cat.jiu.core.events.game;

import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fml.common.eventhandler.Event;

public class TileEntityUpdataEvent extends Event {
	public final TileEntity tile;
	public TileEntityUpdataEvent(TileEntity tile) {
		this.tile = tile;
	}
	
	public static class Pre extends TileEntityUpdataEvent {
		public Pre(TileEntity tile) {
			super(tile);
		}
	}
	
	public static class Post extends TileEntityUpdataEvent {
		public Post(TileEntity tile) {
			super(tile);
		}
	}
}
