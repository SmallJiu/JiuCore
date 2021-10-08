package cat.jiu.core.api.events.player;

import org.lwjgl.input.Keyboard;

import cat.jiu.core.api.IJiuEvent;

public interface IPlayerUseKeyboardInGameEvent extends IJiuEvent{
	/**
	 * 
	 * @param key Key number, number look this {@link Keyboard}
	 * 
	 * @author small_jiu
	 */
	void onPlayerUseKeyboardInGame(int key);
}
