package cat.jiu.core.api.events.player;

import org.lwjgl.input.Mouse;

import cat.jiu.core.api.IJiuEvent;

public interface IPlayerUseMouseInGameEvent extends IJiuEvent{
	/**
	 * 
	 * key 0 = Left.
	 * key 1 = right.
	 * key 2 = middle.
	 * 
	 * @param key Key number, number look this {@link Mouse}
	 * @param x Mouse click x
 	 * @param y Mouse click y
	 * 
	 * @author small_jiu
	 */
	void onPlayerUseMouseInGame(int key, int x, int y);
}
