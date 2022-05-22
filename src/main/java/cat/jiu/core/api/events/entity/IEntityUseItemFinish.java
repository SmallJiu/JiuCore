package cat.jiu.core.api.events.entity;

import cat.jiu.core.api.IJiuEvent;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;

public interface IEntityUseItemFinish extends IJiuEvent{
	void onEntityUseItemFinish(ItemStack stack, Entity entity);
}
