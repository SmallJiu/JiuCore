package cat.jiu.core.api.events.player;

public interface IPlayerSendMessage {
	/**
	 * 
	 * @param originalMessage original Message, you can set to change Player send message
	 * @param message 
	 * 
	 * @author small_jiu
	 */
	void onSendMessage(String originalMessage, String message);
}
