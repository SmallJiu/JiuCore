package cat.jiu.core.api.events.player;

import cat.jiu.core.api.IJiuEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public interface IPlayerSendMessage extends IJiuEvent{
	/**
	 * 
	 * @param originalMessage original Message, you can set to change Player send message
	 * @param message 
	 * 
	 * @author small_jiu
	 * @return 
	 */
	String onSendMessage(String originalMessage, String message);
}
