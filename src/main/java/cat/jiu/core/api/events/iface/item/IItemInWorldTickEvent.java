package cat.jiu.core.api.events.iface.item;

import cat.jiu.core.api.IJiuEvent;
import net.minecraft.entity.item.EntityItem;

public interface IItemInWorldTickEvent extends IJiuEvent {
	/**
	 * 
	 * @param item Entity Item
	 * @author small_jiu
	 */
	void onItemInWorldTick(EntityItem item);
}
