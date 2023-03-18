package cat.jiu.core.events.entity.player;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.eventhandler.Event;

public class PlayerPlaceEvent extends Event {
	public final EntityPlayer player;
	public final BlockPos pos;
	public final World world;
	public final IBlockState placedBlock;
	public final IBlockState placeedAgainst;
	
	protected PlayerPlaceEvent(EntityPlayer player, BlockPos pos, World world, IBlockState placedBlock, IBlockState placeedAgainst) {
		this.player = player;
		this.pos = pos;
		this.world = world;
		this.placedBlock = placedBlock;
		this.placeedAgainst = placeedAgainst;
	}
	
	public static class PlaceFluid extends PlayerPlaceEvent {
		public PlaceFluid(EntityPlayer player, BlockPos pos, World world, IBlockState placedBlock, IBlockState placeedAgainst) {
			super(player, pos, world, placedBlock, placeedAgainst);
		}
	}
	
	public static class PlaceBlock extends PlayerPlaceEvent {
		public PlaceBlock(EntityPlayer player, BlockPos pos, World world, IBlockState placedBlock, IBlockState placeedAgainst) {
			super(player, pos, world, placedBlock, placeedAgainst);
		}
	}
}
