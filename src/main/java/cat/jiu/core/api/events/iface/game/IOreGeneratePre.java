package cat.jiu.core.api.events.iface.game;

import java.util.Random;

import cat.jiu.core.api.IJiuEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

public interface IOreGeneratePre extends IJiuEvent {
	public void onOreGeneratePre(World world, Chunk chunk, BlockPos pos, Random rand);
}
