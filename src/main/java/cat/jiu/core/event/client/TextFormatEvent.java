package cat.jiu.core.event.client;

import net.neoforged.bus.api.Event;
import net.neoforged.bus.api.ICancellableEvent;

public class TextFormatEvent extends Event implements ICancellableEvent {
	private String result;
	public final String key;
	public final Object[] args;
	public TextFormatEvent(String key, Object[] args) {
		this.key = key;
		this.args = args;
	}
	public String getFormatResult() {
		return result;
	}
	public void setFormatResult(String result) {
		this.result = result;
		this.setCanceled(true);
	}
}
