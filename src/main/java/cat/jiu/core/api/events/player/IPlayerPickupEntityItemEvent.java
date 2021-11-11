package cat.jiu.core.api.events.player;

import cat.jiu.core.api.IJiuEvent;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface IPlayerPickupEntityItemEvent extends IJiuEvent{
	/**
	 * 
	 * @param player Entity Player
	 * @param world Entity World
	 * @param pos Entity Pos
	 * @param eitem Player get the EntityItemStack, you can set item to change player get the item
	 * 
	 * @author small_jiu
	 */
	void onPlayerPickupEntityItem(EntityPlayer player, EntityItem eitem, World world, BlockPos pos);
}
