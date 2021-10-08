package cat.jiu.core.api.events.unfinsh;

import org.lwjgl.input.Keyboard;

import cat.jiu.core.api.IJiuEvent;

public interface IPlayerKeepUseKeyboardInGameEvent extends IJiuEvent{
	/**
	 * 
	 * @param key Key number, number look this {@link Keyboard}
	 * 
	 * @author small_jiu
	 */
	void onKeepPlayerUseKeyboardInGame(int key);
}
