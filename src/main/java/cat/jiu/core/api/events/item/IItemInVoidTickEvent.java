package cat.jiu.core.api.events.item;

import cat.jiu.core.api.IJiuEvent;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface IItemInVoidTickEvent extends IJiuEvent {
	/**
	 * Only activated on (y >= -61 and <= 0)
	 * 
	 * @param item Entity Item
	 * @param world Item World
	 * @param pos Item Pos
	 * 
	 * @author small_jiu
	 */
	void onItemInVoidTick(EntityItem item, World world, BlockPos pos);
}
