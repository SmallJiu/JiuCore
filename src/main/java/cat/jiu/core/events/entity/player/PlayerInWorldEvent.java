package cat.jiu.core.events.entity.player;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.player.PlayerEvent;

public class PlayerInWorldEvent extends PlayerEvent {
	protected PlayerInWorldEvent(EntityPlayer player) {
		super(player);
	}
	public static class InVoid extends PlayerInWorldEvent {
		public InVoid(EntityPlayer player) {
			super(player);
		}
	}
	public static class InFluid extends PlayerInWorldEvent {
		public final IBlockState fluid;
		public InFluid(EntityPlayer player, IBlockState fluid) {
			super(player);
			this.fluid = fluid;
		}
	}
}
