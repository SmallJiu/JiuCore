package cat.jiu.core.api.events.client.unfinsh;

import org.lwjgl.input.Mouse;

import cat.jiu.core.api.IJiuEvent;
import cat.jiu.core.api.annotation.Unfinish;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@Unfinish
@SideOnly(Side.CLIENT)
public interface IPlayerKeepUseMouseInGameEvent extends IJiuEvent{
	/**
	 * 
	 * @param key Key number, number look this {@link Mouse}
	 * @param x Mouse click x
 	 * @param y Mouse click y
	 * 
	 * @author small_jiu
	 */
	void onKeepPlayerUseMouseInGame(int key, int x, int y);
}
