package cat.jiu.core.api.events.iface.player;

import cat.jiu.core.api.IJiuEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public interface IPlayerSendMessage extends IJiuEvent{
	/**
	 * 
	 * @param oriMsg original Message, you can set to change Player send message
	 * @param message 
	 * 
	 * @author small_jiu
	 * @return 
	 */
	StringBuilder onSendMessage(String originalMsg, StringBuilder message);
}
