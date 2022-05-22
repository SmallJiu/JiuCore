package cat.jiu.core.api.events;

import cat.jiu.core.api.events.entity.IEntityEvent;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class EntityEvents implements IEntityEvent {
	@Override
	public void onEntityDeath(Entity entity, World world, BlockPos pos) {}
	@Override
	public void onEntityPlaceBlock(Entity player, BlockPos pos, World world, IBlockState placedBlock, IBlockState placeedAgainst) {}
	@Override
	public void onEntityInVoidTick(Entity entity, World world, BlockPos pos) {}
	@Override
	public void onEntityJoinWorld(Entity entity, World world, BlockPos pos, int dim) {}
	@Override
	public void onEntityTick(Entity entity, World world, BlockPos pos) {}
	@Override
	public void onEntityJump(EntityLivingBase entity, BlockPos ePos, World eWorld) {}
	@Override
	public void onEntityInFluidTick(Entity entity, World world, BlockPos pos, IBlockState state) {}
}
