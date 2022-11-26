package cat.jiu.core.api.events.iface.player;

import cat.jiu.core.api.IJiuEvent;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public interface IPlayerSmeltedItemEvent extends IJiuEvent {
	/**
	 * 
	 * @param player Entity Player
	 * @param stack Player craft ItemStack
	 * @author small_jiu
	 */
	void onPlayerSmeltedItem(EntityPlayer player, ItemStack stack);
}
