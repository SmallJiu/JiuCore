package cat.jiu.core.util.helpers;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;

public final class JsonUtil {
	static final Gson gson = new GsonBuilder().serializeNulls().create();
	static final JsonParser parser = new JsonParser();
	
	@SuppressWarnings("unchecked")
	public static <T extends JsonElement> T copy(T json) {
		if(json==null) return null;
		
		try {
			Method method = json.getClass().getDeclaredMethod("deepCopy");
			method.setAccessible(true);
			return (T) method.invoke(json);
		}catch(NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e0) {
			e0.printStackTrace();
			throw new RuntimeException(e0);
		}
	}
	
	@SuppressWarnings({"unchecked"})
	public <T extends JsonElement> T getElement(Class<T> type, JsonObject obj, String... keys) {
		JsonType jsonType = JsonType.getType(type);
		T result = null;
		
		lable: for(String key : keys) {
			if(obj.has(key)) {
				JsonElement e = obj.get(key);
				if(e != null) {
					switch(jsonType) {
						case Object:
							if(e.isJsonObject()) {
								result = (T) e.getAsJsonObject();
								break lable;
							}
						case Array:
							if(e.isJsonArray()) {
								result = (T) e.getAsJsonArray();
								break lable;
							}
						case Primitive:
							if(e.isJsonPrimitive()) {
								result = (T) e.getAsJsonPrimitive();
								break lable;
							}
						case Element:
								result = (T) e;
								break lable;
					}
				}
			}
		}
		return result;
	}
	
	static enum JsonType {
		Object, Array, Primitive, Element;
		static <T extends JsonElement> JsonType getType(Class<T> type) {
			if(type == JsonObject.class) {
				return JsonType.Object;
			}else if(type == JsonArray.class) {
				return JsonType.Array;
			}else if(type == JsonPrimitive.class) {
				return JsonType.Primitive;
			}
			return JsonType.Element;
		}
	}
	
	public boolean toJsonFile(String path, Object src, boolean format) {
		String json = src instanceof JsonElement ? String.valueOf(src) : gson.toJson(src);
		
		try {
			File file = new File(path);
	        if (!file.getParentFile().exists()) { // 如果父目录不存在，创建父目录
	            file.getParentFile().mkdirs();
	        }
	        if (file.exists()) { // 如果已存在,删除旧文件
	            file.delete();
	        }
	        
	        file.createNewFile();
	        OutputStreamWriter write = new OutputStreamWriter(new FileOutputStream(file));
            write.write(format ? this.formatJson(json) : json);
            write.flush();
            write.close();
	        return true;
		} catch (Exception e) {e.printStackTrace();return false;}
	}
	
	/**
	 * This method only deserializes Number, Boolean, and String.<p> 
	 * If it looks like {@code ItemStack} or other, please write your own method
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Map toMap(JsonObject obj) {
		Map map = Maps.newHashMap();
		for(Entry<String, JsonElement> objValues : obj.entrySet()) {
			String key = objValues.getKey();
			JsonElement value = objValues.getValue();
			if(value.isJsonPrimitive()) {
				if(this.isNumber(value)) {
					map.put(key, value.getAsNumber());
				}else if(this.isBoolean(value)) {
					map.put(key, value.getAsBoolean());
				}else {
					map.put(key, value.getAsString());
				}
			}else if(value.isJsonObject()) {
				map.put(key, this.toMap(value.getAsJsonObject()));
			}else if(value.isJsonArray()) {
				map.put(key, this.toList(value.getAsJsonArray()));
			}
		}
		return map;
	}
	
	/**
	 * This method only deserializes Number, Boolean, and String.<p> 
	 * If it looks like {@code ItemStack} or other, please write your own method
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List toList(JsonArray array) {
		List list = Lists.newArrayList();
		for (JsonElement value : array) {
			if(value.isJsonPrimitive()) {
				if(this.isNumber(value)) {
					list.add(value.getAsNumber());
				}else if(this.isBoolean(value)){
					list.add(value.getAsBoolean());
				}else {
					list.add(value.getAsString());
				}
			}else if(value.isJsonObject()) {
				list.add(this.toMap(value.getAsJsonObject()));
			}else if(value.isJsonArray()) {
				list.add(this.toList(value.getAsJsonArray()));
			}
		}
		return list;
	}
	
	/**
	 * This method only deserializes Number, Boolean, and String.<p> 
	 * If it looks like {@code ItemStack} or other, please write your own method
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public <V> JsonArray toJsonArray(List<V> list) {
		JsonArray array = new JsonArray();
		
		for (int i = 0; i < list.size(); i++) {
			V value = list.get(i);
			if(value instanceof List) {
				array.add(this.toJsonArray((List)value));
			}if(value instanceof Set) {
				array.add(this.toJsonArray((Set)value));
			}else if(value instanceof Map) {
				array.add(this.toJsonObject((Map)value));
			}else {
				if(value instanceof Number) {
					array.add((Number)value);
				}else if(value instanceof Boolean) {
					array.add((Boolean)value);
				}else {
					array.add(String.valueOf(value));
				}
			}
		}
		return array;
	}
	
	/**
	 * This method only deserializes Number, Boolean, and String.<p> 
	 * If it looks like {@code ItemStack} or other, please write your own method
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public <V> JsonArray toJsonArray(Set<V> list) {
		JsonArray array = new JsonArray();
		
		for (V value : list) {
			if(value instanceof List) {
				array.add(this.toJsonArray((List)value));
			}if(value instanceof Set) {
				array.add(this.toJsonArray((Set)value));
			}else if(value instanceof Map) {
				array.add(this.toJsonObject((Map)value));
			}else {
				if(value instanceof Number) {
					array.add((Number)value);
				}else if(value instanceof Boolean) {
					array.add((Boolean)value);
				}else {
					array.add(String.valueOf(value));
				}
			}
		}
		return array;
	}
	
	/**
	 * This method only deserializes Number, Boolean, and String.<p> 
	 * If it looks like {@code ItemStack} or other, please write your own method
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public <K, V> JsonObject toJsonObject(Map<K, V> map) {
		JsonObject obj = new JsonObject();
		
		for(Entry<K, V> mapEntry : map.entrySet()) {
			if(mapEntry.getValue() instanceof Map) {
				obj.add(mapEntry.getKey().toString(), this.toJsonObject((Map) mapEntry.getValue()));
			}else if(mapEntry.getValue() instanceof List) {
				obj.add(mapEntry.getKey().toString(), this.toJsonArray((List)mapEntry.getValue()));
			}else if(mapEntry.getValue() instanceof Set) {
				obj.add(mapEntry.getKey().toString(), this.toJsonArray((Set)mapEntry.getValue()));
			}else {
				if(mapEntry.getValue() instanceof Number) {
					obj.addProperty(mapEntry.getKey().toString(), (Number)mapEntry.getValue());
				}else if(mapEntry.getValue() instanceof Boolean) {
					obj.addProperty(mapEntry.getKey().toString(), (Boolean)mapEntry.getValue());
				}else {
					obj.addProperty(mapEntry.getKey().toString(), String.valueOf(mapEntry.getValue()));
				}
			}
		}
		return obj;
	}
	
	public boolean isString(JsonElement e) {
		if(e.isJsonPrimitive()) {
			return e.getAsJsonPrimitive().isString();
		}
		return false;
	}
	
	public boolean isNumber(JsonElement e) {
		if(e.isJsonPrimitive()) {
			return e.getAsJsonPrimitive().isNumber();
		}
		return false;
	}
	
	public boolean isBoolean(JsonElement e) {
		if(e.isJsonPrimitive()) {
			return e.getAsJsonPrimitive().isBoolean();
		}
		return false;
	}
	
	public boolean isString(JsonObject obj, String path) {
		if(obj.has(path)) {
			return isString(obj.get(path));
		}
		return false;
	}
	
	public boolean isNumber(JsonObject obj, String path) {
		if(obj.has(path)) {
			return isNumber(obj.get(path));
		}
		return false;
	}
	
	public boolean isBoolean(JsonObject obj, String path) {
		if(obj.has(path)) {
			return isBoolean(obj.get(path));
		}
		return false;
	}
	
	public <T> T readObject(JsonElement json, Class<T> clazz) {
		return gson.fromJson(json.toString(), clazz);
	}
	
	@SuppressWarnings("unchecked")
	public <T> T[] readArray(JsonElement json, Class<T> clazz) {
		T[] result = null;
		
		if(json.isJsonObject()) {
			JsonObject obj = json.getAsJsonObject();
			result = (T[]) new Object[obj.size()];
			int i = 0;
			for(Entry<String, JsonElement> entry : obj.entrySet()) {
				result[i] = this.readObject(entry.getValue(), clazz);
				i++;
			}
		}else if(json.isJsonArray()) {
			JsonArray array = json.getAsJsonArray();
			result = (T[]) new Object[array.size()];
			for(int i = 0; i < array.size(); i++) {
				result[i] = this.readObject(array.get(i), clazz);
			}
		}else {
			throw new IllegalStateException("Not a JSON Collection: " + json);
		}
		
		return result;
	}
	
	public String formatJson(String json) {
        StringBuffer result = new StringBuffer();
        int indentNumber = 0;
        
        for (int i = 0; i < json.length(); i++) {
        	char key = json.charAt(i);
            
            if (key == '[' || key == '{') {
        		result.append(key);
            	if(i-1 > 0) {
            		if(json.charAt(i-1) != '\"') {
                        result.append('\n');
                        indentNumber++;
                        result.append(indent(indentNumber));
            		}
            	}else {
                    result.append('\n');
                    indentNumber++;
                    result.append(indent(indentNumber));
            	}
                continue;
            }
            
            if (key == ']' || key == '}') {
            	if(i+1 < json.length()) {
            		if(json.charAt(i+1) != '\"') {
                		result.append('\n');
                        indentNumber--;
                        result.append(indent(indentNumber));
                	}
            	}else {
            		result.append('\n');
                    indentNumber--;
                    result.append(indent(indentNumber));
            	}
                result.append(key);
                continue;
            }
            
            if (key == ',') {
            	result.append(key);
            	if(canNextLine(json.charAt(i-1))) {
                    result.append('\n');
                    result.append(indent(indentNumber));
            	}else {
            		if(json.substring(i-4, i).equals("true") 
            		|| json.substring(i-5, i).equals("false")) {
                        result.append('\n');
                        result.append(indent(indentNumber));
            		}
            	}
                continue;
            }
            
            if(key == ':') {
        		result.append(key);
            	if(json.charAt(i-1) == '"') {
	            	result.append(' ');
	            	continue;
            	}
            }
            result.append(key);
        }
        
        result.append('\n');
        return result.toString();
    }
	
	private boolean canNextLine(char c) {
		if(c == '}' || c == ']' || c == '"') {
			return true;
		}
		if(c=='0' || c=='1' || c=='2' || c=='3' || c=='4' || c=='5' || c=='6' || c=='7' || c=='8' || c=='9') {
			return true;
		}
		return false;
	}
	
	private String indent(int number) {
        StringBuffer result = new StringBuffer();
        for (int i = 0; i < number; i++) {
            result.append("	");
        }
        return result.toString();
    }
}
