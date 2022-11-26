package cat.jiu.core.api.events.iface.game;

import java.util.Random;

import cat.jiu.core.api.IJiuEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraftforge.event.terraingen.OreGenEvent.GenerateMinable.EventType;

public interface IOreGenerate extends IJiuEvent{
	void onOreGenerate(World world, Chunk chunk, BlockPos pos, Random rand, WorldGenerator generator, EventType type);
}
