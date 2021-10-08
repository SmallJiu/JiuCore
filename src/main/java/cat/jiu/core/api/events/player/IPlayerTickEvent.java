package cat.jiu.core.api.events.player;

import cat.jiu.core.api.IJiuEvent;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface IPlayerTickEvent extends IJiuEvent{
	/**
	 * 
	 * @param player The Player
	 * @param world Player World
	 * @param pos Player Pos
	 * 
	 * @author small_jiu
	 */
	void onPlayerTick(EntityPlayer player, World world, BlockPos pos);
}
