package cat.jiu.core.api.events.entity;

import cat.jiu.core.api.IJiuEvent;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface IEntityInFluidEvent extends IJiuEvent{
	/**
	 * 
	 * @param entity Entity
	 * @param world Entity World
	 * @param pos Entity Pos
	 * @param state Fluid State
	 * 
	 * @author small_jiu
	 */
	void onEntityInFluidTick(Entity entity, World world, BlockPos pos, IBlockState state);
}
