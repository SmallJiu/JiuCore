package cat.jiu.core.api.events;

import cat.jiu.core.api.events.iface.entity.IEntityEvent;
import cat.jiu.core.util.JiuCoreEvents;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class EntityEvents implements IEntityEvent {
	public EntityEvents() {
		JiuCoreEvents.addEvent(this);
	}
	@Override
	public void onEntityDeath(Entity entity) {}
	@Override
	public void onEntityPlaceBlock(Entity player, BlockPos pos, World world, IBlockState placedBlock, IBlockState placeedAgainst) {}
	@Override
	public void onEntityInVoidTick(Entity entity) {}
	@Override
	public void onEntityJoinWorld(Entity entity, World world, BlockPos pos, int dim) {}
	@Override
	public void onEntityTick(Entity entity) {}
	@Override
	public void onEntityJump(EntityLivingBase entity) {}
	@Override
	public void onEntityInFluidTick(Entity entity, IBlockState state) {}
}
