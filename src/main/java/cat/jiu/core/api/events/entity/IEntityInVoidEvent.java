package cat.jiu.core.api.events.entity;

import cat.jiu.core.api.IJiuEvent;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface IEntityInVoidEvent extends IJiuEvent{
	/**
	 * Only activated on (y >= -61 and <= 0)
	 * 
	 * @param entity Entity
	 * @param world Entity World
	 * @param pos Entity Pos
	 * 
	 * @author small_jiu
	 */
	void onEntityInVoidTick(Entity entity, World world, BlockPos pos);
}
