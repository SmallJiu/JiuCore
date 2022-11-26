package cat.jiu.core.api.events.iface.player;

import cat.jiu.core.api.IJiuEvent;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public interface IPlayerEatFoodStop extends IJiuEvent{
	void onPlayerEatFoodStop(ItemStack stack, EntityPlayer player);
}
