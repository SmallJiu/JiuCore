package cat.jiu.core.api.events.iface.player;

import cat.jiu.core.api.IJiuEvent;
import net.minecraft.entity.player.EntityPlayer;

public interface IPlayerTickEvent extends IJiuEvent{
	/**
	 * 
	 * @param player The Player
	 * @author small_jiu
	 */
	void onPlayerTick(EntityPlayer player);
}
