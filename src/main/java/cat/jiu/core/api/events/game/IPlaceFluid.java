package cat.jiu.core.api.events.game;

import cat.jiu.core.api.IJiuEvent;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface IPlaceFluid extends IJiuEvent{
	void onPlaceFluid(BlockPos pos, World world, IBlockState placedBlock);
}
