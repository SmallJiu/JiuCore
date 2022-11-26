package cat.jiu.core.api.events.iface.player;

import cat.jiu.core.api.IJiuEvent;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;

public interface IPlayerInFluidEvent extends IJiuEvent{
	/**
	 * 
	 * @param player Entity Player
	 * @param state Fluid state
	 * @author small_jiu
	 */
	void onPlayerInFluidTick(EntityPlayer player, IBlockState state);
}
