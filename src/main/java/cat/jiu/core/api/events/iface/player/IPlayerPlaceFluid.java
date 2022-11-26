package cat.jiu.core.api.events.iface.player;

import cat.jiu.core.api.IJiuEvent;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface IPlayerPlaceFluid extends IJiuEvent{
	void onPlayerPlaceFluid(EntityPlayer player, BlockPos pos, World world, IBlockState placedBlock, IBlockState placeedAgainst);
}
