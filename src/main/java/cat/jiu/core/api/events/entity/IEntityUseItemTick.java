package cat.jiu.core.api.events.entity;

import cat.jiu.core.api.IJiuEvent;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;

public interface IEntityUseItemTick extends IJiuEvent{
	void onEntityUseItemTick(ItemStack stack, Entity entity);
}
