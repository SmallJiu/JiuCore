package cat.jiu.core.api.events.iface.item;

import cat.jiu.core.api.IJiuEvent;
import net.minecraft.entity.item.EntityItem;

public interface IItemInVoidTickEvent extends IJiuEvent {
	/**
	 * Only activated on (y >= -61 and <= 0)
	 * 
	 * @param item Entity Item
	 * @author small_jiu
	 */
	void onItemInVoidTick(EntityItem item);
}
