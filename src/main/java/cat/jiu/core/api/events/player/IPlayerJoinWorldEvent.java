package cat.jiu.core.api.events.player;

import cat.jiu.core.api.IJiuEvent;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface IPlayerJoinWorldEvent extends IJiuEvent{
	/**
	 * 
	 * @param player Entity Player
	 * @param world Entity World
	 * @param pos Entity Pos
	 * 
	 * @author small_jiu
	 */
	void onPlayerJoinWorld(EntityPlayer player, World world, BlockPos pos);
}
