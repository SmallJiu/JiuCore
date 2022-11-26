package cat.jiu.core.api.events.iface.player;

import cat.jiu.core.api.IJiuEvent;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

public interface IPlayerCraftedItemEvent extends IJiuEvent{
	/**
	 * 
	 * @param player Entity Player
	 * @param gui The craft gui
	 * @param gui 
	 * @param stack Player get ItemStack
	 * @author small_jiu
	 */
	void onPlayerCraftedItemInGui(EntityPlayer player, IInventory gui, ItemStack stack);
}
