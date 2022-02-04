package cat.jiu.core.api.events.client.player;

import org.lwjgl.input.Mouse;

import cat.jiu.core.api.IJiuEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public interface IPlayerUseMouseInGameEvent extends IJiuEvent{
	/**
	 * 
	 * key 0 = Left.<p>
	 * key 1 = right.<p>
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
