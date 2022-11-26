package cat.jiu.core.api.events.iface.entity;

import cat.jiu.core.api.IJiuEvent;
import net.minecraft.entity.Entity;

public interface IEntityInVoidEvent extends IJiuEvent{
	/**
	 * Only activated on (y >= -61 and <= 0)
	 * 
	 * @param entity Entity
	 * @author small_jiu
	 */
	void onEntityInVoidTick(Entity entity);
}
