package cat.jiu.core.api.events.iface.item;

import cat.jiu.core.api.IJiuEvent;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;

public interface IItemInPlayerArmorTick extends IJiuEvent{
	void onItemInPlayerArmorTick(EntityPlayer player, ItemStack invStack, EntityEquipmentSlot slot);
}
