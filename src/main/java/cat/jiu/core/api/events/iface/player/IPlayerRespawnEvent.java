package cat.jiu.core.api.events.iface.player;

import cat.jiu.core.api.IJiuEvent;
import net.minecraft.entity.player.EntityPlayer;

public interface IPlayerRespawnEvent extends IJiuEvent{
	/**
	 * 
	 * @param player Entity Player
	 * @param dim Respawn to Dim
	 * @param endConquered TODO
	 * @author small_jiu
	 */
	void onPlayerRespawn(EntityPlayer player, int dim, boolean endConquered);
}
