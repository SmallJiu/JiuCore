package cat.jiu.core.api.events.iface.player;

import cat.jiu.core.api.IJiuEvent;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface IPlayerJoinWorldEvent extends IJiuEvent{
	/**
	 * 
	 * @param player The Player
	 * @param world Entity World
	 * @param pos Player Pos
	 * 
	 * @author small_jiu
	 * @param dim 
	 */
	void onPlayerJoinWorld(EntityPlayer player, World world, BlockPos pos, int dim);
}
