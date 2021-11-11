package cat.jiu.core.api.events.client.player;

import org.lwjgl.input.Mouse;

import cat.jiu.core.api.IJiuEvent;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public interface IPlayerUseMouseInGuiEvent extends IJiuEvent{
	/**
	 * 
	 * key 0 = Left.
	 * key 1 = right.
	 * key 2 = middle.
	 * 
	 * @param gui Player open the gui
	 * @param key Key number, number look this {@link Mouse}
	 * @param x Mouse click x
 	 * @param y Mouse click y
	 * 
	 * @author small_jiu
	 */
	void onPlayerUseMouseInGui(GuiScreen gui, int key, int x, int y);
}
