package cat.jiu.core.api.events.iface.player;

import cat.jiu.core.api.IJiuEvent;
import net.minecraft.entity.player.EntityPlayer;

public interface IPlayerPickupXPEvent extends IJiuEvent{
	/**
	 * 
	 * @param player Entity Player
	 * @param xpVaule Player pickup XP vaule, you can set vaule to change player get the xp vaule
	 * @author small_jiu
	 * @return 
	 */
	int onPlayerPickupXP(EntityPlayer player, int xpVaule);
}
