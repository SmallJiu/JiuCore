package cat.jiu.core.api.events.item;

import cat.jiu.core.api.IJiuEvent;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public interface IItemInPlayerHandTick extends IJiuEvent{
	void onItemInPlayerHandTick(EntityPlayer player, ItemStack mainHand, ItemStack offHand);
}
