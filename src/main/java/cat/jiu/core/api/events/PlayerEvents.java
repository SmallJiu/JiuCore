package cat.jiu.core.api.events;

import cat.jiu.core.api.events.iface.player.IPlayerEvent;
import cat.jiu.core.util.JiuCoreEvents;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class PlayerEvents implements IPlayerEvent {
	public PlayerEvents() {
		JiuCoreEvents.addEvent(this);
	}
	@Override
	public void onPlayerPlaceFluid(EntityPlayer player, BlockPos pos, World world, IBlockState placedBlock, IBlockState placeedAgainst) {}
	@Override
	public void onPlayerLoggedOut(EntityPlayer player, int dim) {}
	@Override
	public void onPlayerLoggedIn(EntityPlayer player, int dim) {}
	@Override
	public void onPlayerInVoidTick(EntityPlayer player) {}
	@Override
	public int onPlayerBreakBlock(EntityPlayer player, World world, BlockPos pos, IBlockState state, int exps) {return exps;}
	@Override
	public void onPlayerJump(EntityPlayer player) {}
	@Override
	public void onPlayerTick(EntityPlayer player) {}
	@Override
	public void onPlayerPlaceBlock(EntityPlayer player, BlockPos pos, World world, IBlockState placedBlock, IBlockState placeedAgainst) {}
	@Override
	public void onPlayerInFluidTick(EntityPlayer player, IBlockState state) {}
	@Override
	public void onPlayerDeath(EntityPlayer player) {}
	@Override
	public void onPlayerJoinWorld(EntityPlayer player, World world, BlockPos pos, int dim) {}
}
