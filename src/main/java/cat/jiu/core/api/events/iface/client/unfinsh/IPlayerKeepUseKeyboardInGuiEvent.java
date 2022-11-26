package cat.jiu.core.api.events.iface.client.unfinsh;

import org.lwjgl.input.Keyboard;

import cat.jiu.core.api.IJiuEvent;
import cat.jiu.core.api.annotation.Unfinish;
import net.minecraft.client.gui.GuiScreen;

@Unfinish
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
