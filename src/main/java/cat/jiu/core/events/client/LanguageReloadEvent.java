package cat.jiu.core.events.client;

import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class LanguageReloadEvent extends Event {
	public final String languageCode;
	public LanguageReloadEvent(String languageCode) {
		this.languageCode = languageCode;
	}
}
