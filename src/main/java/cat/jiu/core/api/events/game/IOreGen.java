package cat.jiu.core.api.events.game;

import java.util.Random;

import cat.jiu.core.api.IJiuEvent;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface IOreGen extends IJiuEvent{
	void onOreGenerate(World world, BlockPos pos, IBlockState state, Random rand);
}
