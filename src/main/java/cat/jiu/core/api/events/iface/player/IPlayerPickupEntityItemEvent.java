package cat.jiu.core.api.events.iface.player;

import cat.jiu.core.api.IJiuEvent;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;

public interface IPlayerPickupEntityItemEvent extends IJiuEvent{
	/**
	 * 
	 * @param player Entity Player
	 * @param eitem Player get the EntityItemStack, you can set item to change player get the item
	 * @author small_jiu
	 */
	void onPlayerPickupEntityItem(EntityPlayer player, EntityItem eitem);
}
