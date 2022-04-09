package cat.jiu.core.api.events.player;

import cat.jiu.core.api.IJiuEvent;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface IPlayerPickupXPEvent extends IJiuEvent{
	/**
	 * 
	 * @param player Entity Player
	 * @param xpVaule Player pickup XP vaule, you can set vaule to change player get the xp vaule
	 * @param world Player World
	 * @param pos Player Pos
	 * @author small_jiu
	 * @return 
	 */
	int onPlayerPickupXP(EntityPlayer player, int xpVaule, World world, BlockPos pos);
}
