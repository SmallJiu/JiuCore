package cat.jiu.core.api.events.client.player;

import org.lwjgl.input.Keyboard;

import cat.jiu.core.api.IJiuEvent;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public interface IPlayerUseKeyboardInGuiEvent extends IJiuEvent{
	/**
	 * 
	 * @param gui Player Open The Gui
	 * @param key Key number, number look this {@link Keyboard}
	 * 
	 * @author small_jiu
	 */
	void onPlayerUseKeyboardInGui(GuiScreen gui, int key);
}
