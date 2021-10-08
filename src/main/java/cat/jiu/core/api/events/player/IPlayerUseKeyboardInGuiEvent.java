package cat.jiu.core.api.events.player;

import org.lwjgl.input.Keyboard;

import cat.jiu.core.api.IJiuEvent;
import net.minecraft.client.gui.GuiScreen;

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
