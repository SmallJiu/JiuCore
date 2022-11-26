package cat.jiu.core.api;

import java.util.List;
import java.util.Set;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

public interface IJiuEvent {
	@SuppressWarnings("unchecked")
	default Set<Class<? extends IJiuEvent>> getSuperEventClass() {
		List<Class<? extends IJiuEvent>> list = Lists.newArrayList();
		for(Class<?> iface : getClass().getInterfaces()) {
			if(isJiuEvent(iface)) {
				list.add((Class<? extends IJiuEvent>) iface);
			}
		}
		return Sets.newHashSet(list);
	}
	
	public static boolean isJiuEvent(Class<?> iface) {
		return IJiuEvent.class.isAssignableFrom(iface);
	}
}
