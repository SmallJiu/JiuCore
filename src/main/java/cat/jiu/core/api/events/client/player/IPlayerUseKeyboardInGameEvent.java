package cat.jiu.core.api.events.client.player;

import org.lwjgl.input.Keyboard;

import cat.jiu.core.api.IJiuEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public interface IPlayerUseKeyboardInGameEvent extends IJiuEvent{
	/**
	 * 
	 * @param key Key number, number look this {@link Keyboard}
	 * 
	 * @author small_jiu
	 */
	void onPlayerUseKeyboardInGame(int key);
}
