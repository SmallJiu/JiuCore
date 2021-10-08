package cat.jiu.core.api.events.player;

import cat.jiu.core.api.IJiuEvent;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface IPlayerInFluidEvent extends IJiuEvent{
	/**
	 * 
	 * @param player Entity Player
	 * @param world is server world
	 * @param pos Item Pos
	 * @param state Fluid state
	 * 
	 * @author small_jiu
	 */
	void onPlayerInFluidTick(EntityPlayer player, World world, BlockPos pos, IBlockState state);
}
