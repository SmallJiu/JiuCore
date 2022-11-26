package cat.jiu.core.api.events.iface.player;

import cat.jiu.core.api.IJiuEvent;
import net.minecraft.entity.player.EntityPlayer;

public interface IPlayerInVoidEvent extends IJiuEvent{
	/**
	 * Only activated on (y >= -61 and <= 0)
	 * 
	 * @param player Entity Player
	 * @author small_jiu
	 */
	void onPlayerInVoidTick(EntityPlayer player);
}
