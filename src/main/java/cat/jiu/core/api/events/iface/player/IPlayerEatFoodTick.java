package cat.jiu.core.api.events.iface.player;

import cat.jiu.core.api.IJiuEvent;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public interface IPlayerEatFoodTick extends IJiuEvent{
	void onPlayerEatFoodTick(ItemStack stack, EntityPlayer player);
}
