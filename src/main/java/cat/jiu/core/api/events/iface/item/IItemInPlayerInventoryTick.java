package cat.jiu.core.api.events.iface.item;

import cat.jiu.core.api.IJiuEvent;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public interface IItemInPlayerInventoryTick extends IJiuEvent {
	void onItemInPlayerInventoryTick(EntityPlayer player, ItemStack invStack, int slot);
}
