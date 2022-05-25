package cat.jiu.core.api.events.player;

import java.util.List;

import cat.jiu.core.api.IJiuEvent;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;

public interface IPlayerDeathDropItems extends IJiuEvent{
	void onPlayerDeathDropItems(EntityPlayer player, DamageSource source, List<EntityItem> drops, List<ItemStack> items, int lootingLevel, boolean recentlyHit);
}
