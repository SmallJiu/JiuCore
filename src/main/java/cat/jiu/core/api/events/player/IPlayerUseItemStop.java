package cat.jiu.core.api.events.player;

import cat.jiu.core.api.IJiuEvent;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public interface IPlayerUseItemStop extends IJiuEvent{
	void onPlayerUseItemStop(ItemStack stack, EntityPlayer player);
}
