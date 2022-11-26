package cat.jiu.core.api.events.iface.player;

import cat.jiu.core.api.IJiuEvent;
import net.minecraft.entity.player.EntityPlayer;

public interface IPlayerDeathEvent extends IJiuEvent{
	/**
	 * 
	 * @param player Player Entity
	 * @author small_jiu
	 */
	void onPlayerDeath(EntityPlayer player);
}
