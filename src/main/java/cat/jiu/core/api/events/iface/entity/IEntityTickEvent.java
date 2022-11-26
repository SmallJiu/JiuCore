package cat.jiu.core.api.events.iface.entity;

import cat.jiu.core.api.IJiuEvent;
import net.minecraft.entity.Entity;

public interface IEntityTickEvent extends IJiuEvent{
	/**
	 * 
	 * @param entity Entity
	 * @author small_jiu
	 */
	void onEntityTick(Entity entity);
}
