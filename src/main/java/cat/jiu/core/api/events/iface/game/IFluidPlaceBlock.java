package cat.jiu.core.api.events.iface.game;

import cat.jiu.core.api.IJiuEvent;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface IFluidPlaceBlock extends IJiuEvent{
	/**
	 * 
	 * @param world
	 * @param pos
	 * @param newState the new block, you can set 'newState' to change the block
	 * @param oldState
	 * 
	 * @author small_jiu
	 * @return newState
	 */
	IBlockState onFluidPlaceBlock(World world, BlockPos pos, IBlockState newState, IBlockState oldState);
}
