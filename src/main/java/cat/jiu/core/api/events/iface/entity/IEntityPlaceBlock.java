package cat.jiu.core.api.events.iface.entity;

import cat.jiu.core.api.IJiuEvent;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface IEntityPlaceBlock extends IJiuEvent{
	void onEntityPlaceBlock(Entity player, BlockPos pos, World world, IBlockState placedBlock, IBlockState placeedAgainst);
}
