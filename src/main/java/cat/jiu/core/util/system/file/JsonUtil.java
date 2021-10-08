package cat.jiu.core.util.system.file;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

@Deprecated
public class JsonUtil {
	
	private JsonUtil() {}
	
	private static Gson gson = null;
	static {
		if(gson == null) {
			gson= new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
		}
	}
	
	public static String toJson(Object o) {
		String str = null;
		if(gson != null) {
			str = gson.toJson(o);
		}
		return str;
	}
	
	public static String toJson(Object... objs) {
		List<Object> list = new ArrayList<>();
		
		for(Object obj : objs) {
			list.add(obj);
		}
		String str = gson.toJsonTree(list).toString();
		return str;
	}
}
