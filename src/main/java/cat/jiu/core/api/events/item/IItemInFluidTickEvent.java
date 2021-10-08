package cat.jiu.core.api.events.item;

import cat.jiu.core.api.IJiuEvent;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface IItemInFluidTickEvent extends IJiuEvent {
	/**
	 * 
	 * @param item Entity Item
	 * @param world Item World, it is server world
	 * @param pos Item Pos
	 * @param state Fluid state
	 * 
	 * @author small_jiu
	 */
	void onItemInFluidTick(EntityItem item, World world, BlockPos pos, IBlockState state);
}
