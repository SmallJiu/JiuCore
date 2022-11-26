package cat.jiu.core.util;

import cat.jiu.core.api.IJiuEvent;
import cat.jiu.core.api.events.iface.player.IPlayerBreakBlock;

public class FindEvent {
	static <T extends IJiuEvent> boolean post(Class<T> eventClass) {
		if(eventClass == IPlayerBreakBlock.class) {
//			return true;
		}
		
		return false;
	}
}
