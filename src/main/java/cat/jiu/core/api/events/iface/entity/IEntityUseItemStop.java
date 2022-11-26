package cat.jiu.core.api.events.iface.entity;

import cat.jiu.core.api.IJiuEvent;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;

public interface IEntityUseItemStop extends IJiuEvent{
	void onEntityUseItemStop(ItemStack stack, Entity entity);
}
