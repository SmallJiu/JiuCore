package cat.jiu.core.api.events.player;

import cat.jiu.core.api.IJiuEvent;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface IPlayerInVoidEvent extends IJiuEvent{
	/**
	 * Only activated on (y >= -61 and <= 0)
	 * 
	 * @param player Entity Player
	 * @param world is server world
	 * @param pos Player Pos
	 * 
	 * @author small_jiu
	 */
	void onPlayerInVoidTick(EntityPlayer player, World world, BlockPos pos);
}
