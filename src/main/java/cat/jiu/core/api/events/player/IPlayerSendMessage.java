package cat.jiu.core.api.events.player;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public interface IPlayerSendMessage {
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
