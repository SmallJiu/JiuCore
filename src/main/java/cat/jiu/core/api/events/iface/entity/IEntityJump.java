package cat.jiu.core.api.events.iface.entity;

import cat.jiu.core.api.IJiuEvent;
import net.minecraft.entity.EntityLivingBase;

public interface IEntityJump extends IJiuEvent{
	void onEntityJump(EntityLivingBase entity);
}
