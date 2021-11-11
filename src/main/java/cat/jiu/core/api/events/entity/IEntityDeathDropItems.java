package cat.jiu.core.api.events.entity;

import java.util.List;

import cat.jiu.core.api.IJiuEvent;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;

public interface IEntityDeathDropItems extends IJiuEvent{
	void onEntityDeathDropItems(EntityLivingBase entity, DamageSource source, List<EntityItem> drops, List<ItemStack> items, int lootingLevel, boolean recentlyHit);
}
