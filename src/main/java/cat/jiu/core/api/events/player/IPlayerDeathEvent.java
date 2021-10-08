package cat.jiu.core.api.events.player;

import cat.jiu.core.api.IJiuEvent;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface IPlayerDeathEvent extends IJiuEvent{
	/**
	 * 
	 * @param player Player Entity
	 * @param world Player World
	 * @param pos Player Pos
	 * 
	 * @author small_jiu
	 */
	void onPlayerDeath(EntityPlayer player, World world, BlockPos pos);
}
