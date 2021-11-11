package cat.jiu.core.api.events.player;

import cat.jiu.core.api.IJiuEvent;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface IPlayerLoggedOutEvent extends IJiuEvent{
	/**
	 * 
	 * @param player Entity Player
	 * @param world Entity World
	 * @param pos Entity Pos
	 * 
	 * @author small_jiu
	 * @param dim 
	 */
	void onPlayerLoggedOut(EntityPlayer player, World world, BlockPos pos, int dim);
}
