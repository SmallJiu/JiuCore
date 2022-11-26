package cat.jiu.core.api.events.iface.player;

import cat.jiu.core.api.IJiuEvent;
import net.minecraft.entity.player.EntityPlayer;

public interface IPlayerJump extends IJiuEvent{
	void onPlayerJump(EntityPlayer player);
}
