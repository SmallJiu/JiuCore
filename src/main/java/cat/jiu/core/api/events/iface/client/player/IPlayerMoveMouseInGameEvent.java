package cat.jiu.core.api.events.iface.client.player;

import cat.jiu.core.api.IJiuEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public interface IPlayerMoveMouseInGameEvent extends IJiuEvent{
	/**
	 * @param x Mouse click x
 	 * @param y Mouse click y
	 * 
	 * @author small_jiu
	 */
	void onPlayerMoveMouseInGame(int x, int y);
}
