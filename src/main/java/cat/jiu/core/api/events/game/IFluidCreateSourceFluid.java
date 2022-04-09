package cat.jiu.core.api.events.game;

import cat.jiu.core.api.IJiuEvent;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface IFluidCreateSourceFluid extends IJiuEvent{
	void onFluidCreateSourceFluid(BlockPos pos, World world, IBlockState placedBlock);
}
