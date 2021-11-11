package cat.jiu.core.api.events.entity;

import cat.jiu.core.api.IJiuEvent;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface IEntityJump extends IJiuEvent{
	void onEntityJump(EntityLivingBase entity, BlockPos ePos, World eWorld);
}
