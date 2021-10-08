package cat.jiu.core.api.events.unfinsh;

import org.lwjgl.input.Keyboard;

import cat.jiu.core.api.IJiuEvent;
import net.minecraft.client.gui.GuiScreen;

public interface IPlayerKeepUseKeyboardInGuiEvent extends IJiuEvent{
	/**
	 * 
	 * @param gui Player Open The Gui
	 * @param key Key number, number look this {@link Keyboard}
	 * 
	 * @author small_jiu
	 */
	void onPlayerKeepUseKeyboardInGui(GuiScreen gui, int key);
}
