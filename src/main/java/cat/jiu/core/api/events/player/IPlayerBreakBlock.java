package cat.jiu.core.api.events.player;

import cat.jiu.core.api.IJiuEvent;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface IPlayerBreakBlock extends IJiuEvent{
	/**
	 * 
	 * @param player the player
	 * @param world the world
	 * @param pos the block pos
	 * @param state the block
	 * @param exps Player pickup XP vaule, you can set vaule to change player get the xp vaule
	 * 
	 * @author small_jiu
	 */
	void onPlayerBreakBlock(EntityPlayer player, World world, BlockPos pos, IBlockState state, int exps);
}
