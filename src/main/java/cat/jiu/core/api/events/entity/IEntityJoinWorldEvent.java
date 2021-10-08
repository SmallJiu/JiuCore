package cat.jiu.core.api.events.entity;

import cat.jiu.core.api.IJiuEvent;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface IEntityJoinWorldEvent extends IJiuEvent{
	/**
	 * 
	 * @param entity Entity
	 * @param world Entity World
	 * @param pos Entity Pos
	 * 
	 * @author small_jiu
	 */
	void onEntityJoinWorld(Entity entity, World world, BlockPos pos);
}
