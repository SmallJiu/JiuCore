package cat.jiu.core.api.events.iface.player;

import cat.jiu.core.api.IJiuEvent;
import net.minecraft.entity.player.EntityPlayer;

public interface IPlayerLoggedOutEvent extends IJiuEvent{
	/**
	 * 
	 * @param player Entity Player
	 * @param dim 
	 * @author small_jiu
	 */
	void onPlayerLoggedOut(EntityPlayer player, int dim);
}
