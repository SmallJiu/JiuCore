package cat.jiu.core.api.events.client.unfinsh;

import org.lwjgl.input.Mouse;

import cat.jiu.core.api.IJiuEvent;
import cat.jiu.core.api.annotation.Unfinish;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@Unfinish
@SideOnly(Side.CLIENT)
public interface IPlayerKeepUseMouseInGuiEvent extends IJiuEvent{
	/**
	 * 
	 * @param gui Player open the gui
	 * @param key Key number, number look this {@link Mouse}
	 * @param x Mouse click x
 	 * @param y Mouse click y
	 * 
	 * @author small_jiu
	 */
	void onPlayerKeepUseMouseInGui(GuiScreen gui, int key, int x, int y);
}
