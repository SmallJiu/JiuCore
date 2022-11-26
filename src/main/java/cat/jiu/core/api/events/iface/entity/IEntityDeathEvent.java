package cat.jiu.core.api.events.iface.entity;

import cat.jiu.core.api.IJiuEvent;
import net.minecraft.entity.Entity;

public interface IEntityDeathEvent extends IJiuEvent{
	/**
	 * 
	 * @param entity Entity
	 * @author small_jiu
	 */
	void onEntityDeath(Entity entity);
}
