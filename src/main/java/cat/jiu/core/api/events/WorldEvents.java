package cat.jiu.core.api.events;

import java.util.Random;

import cat.jiu.core.api.events.iface.game.IWorldEvent;
import cat.jiu.core.util.JiuCoreEvents;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraftforge.event.terraingen.OreGenEvent.GenerateMinable.EventType;

public class WorldEvents implements IWorldEvent {
	public WorldEvents() {
		JiuCoreEvents.addEvent(this);
	}
	@Override
	public void onOreGeneratePre(World world, Chunk chunk, BlockPos pos, Random rand) {}
	@Override
	public void onOreGenerate(World world, Chunk chunk, BlockPos pos, Random rand, WorldGenerator generator, EventType type) {}
	@Override
	public void onOreGeneratePost(World world, Chunk chunk, BlockPos pos, Random rand) {}
	@Override
	public IBlockState onFluidPlaceBlock(World world, BlockPos pos, IBlockState newState, IBlockState oldState) {return newState;}
	@Override
	public void onFluidCreateSourceFluid(BlockPos pos, World world, IBlockState placedBlock) {}
}
