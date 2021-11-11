package cat.jiu.core.api.events.entity;

import cat.jiu.core.api.IJiuEvent;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface IEntityUseItemStop extends IJiuEvent{
	void onEntityUseItemStop(ItemStack stack, Entity entity, World world, BlockPos pos);
}
