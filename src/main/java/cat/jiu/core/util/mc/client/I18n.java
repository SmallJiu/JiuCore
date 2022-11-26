package cat.jiu.core.util.mc.client;

import java.util.IllegalFormatException;
import java.util.Map;

import com.google.common.collect.Maps;

public class I18n {
	private static final Map<String, String> keys = Maps.newHashMap();
	public static String format(boolean json, String key, Object... args) {
		if(json) {
			try {
				return String.format(keys.get(key), args);
			}catch(IllegalFormatException e) {
				return "Format error: " + e.getLocalizedMessage();
			}
		}else {
			return net.minecraft.client.resources.I18n.format(key, args);
		}
	}
	public static boolean hasKey(boolean json, String key) {
		if(json) {
			return keys.containsKey(key);
		}else {
			return net.minecraft.client.resources.I18n.hasKey(key);
		}
	}
}
