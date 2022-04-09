package cat.jiu.core.api.events.game;

import java.util.Random;

import cat.jiu.core.api.IJiuEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

public interface IOreGeneratePost extends IJiuEvent {
	public void onOreGeneratePost(World world, Chunk chunk, BlockPos pos, Random rand);
}
