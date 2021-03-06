package cat.jiu.core.util.system.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.io.IOUtils;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

public final class JsonUtil {
	final Gson gson = new GsonBuilder().create();
	
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
	
	enum JsonType {
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
	
	public boolean toJsonFile(String path, Object src) {
		String json = gson.toJson(src);
		
		try {
			File file = new File(path);
	        if (!file.getParentFile().exists()) { // ??????????????????????????????????????????
	            file.getParentFile().mkdirs();
	        }
	        if (file.exists()) { // ???????????????,???????????????
	            file.delete();
	        }
	        
	        file.createNewFile();
	        OutputStreamWriter write = new OutputStreamWriter(new FileOutputStream(file), "UTF-8");
            write.write(this.formatJson(json));
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
			}else if(value instanceof Map) {
				array.add(this.toJsonObject((Map)value));
			}else {
				if(value instanceof Number) {
					array.add((Number)value);
				}else if(value instanceof Boolean) {
					array.add((Boolean)value);
				}else {
					array.add(value.toString());
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
			}else {
				if(mapEntry.getValue() instanceof Number) {
					obj.addProperty(mapEntry.getKey().toString(), (Number)mapEntry.getValue());
				}else if(mapEntry.getValue() instanceof Boolean) {
					obj.addProperty(mapEntry.getKey().toString(), (Boolean)mapEntry.getValue());
				}else {
					obj.addProperty(mapEntry.getKey().toString(), mapEntry.getValue().toString());
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
	
	public <T> T readObject(String path, Class<T> clazz) {
		String result = "";
		try(InputStream stream = new FileInputStream(new File(path))) {
			 result = IOUtils.toString(stream, StandardCharsets.UTF_8);
		}catch (IOException e) {
            e.printStackTrace();
        }
		return gson.fromJson(result, clazz);
	}
	
	public <T> T[] readArray(String path, Class<T> clazz) {
		return null;
	}
	
	public String formatJson(String json) {
        StringBuilder result = new StringBuilder();
        int length = json.length();
        int number = 0;
        char key = 0;
        
        for (int i = 0; i < length; i++) {
            // 1????????????????????????
            key = json.charAt(i);
            
            // 2????????????????????????????????????/??????????????????????????????
            if ((key == '[') || (key == '{')) {
                //??????str
                result.append(key);
                
                // ???3???????????????/?????????????????????????????????????????????????????????
                result.append('\n');
                
                // ???4???????????????????????????????????????????????????????????????????????????????????????????????????
                number++;
                result.append(indent(number));
                
                continue;
            }
            
            // 3?????????????????????????????????????????????????????????????????????
            if ((key == ']') || (key == '}')) {
                // ???1???????????????????????????????????????????????????????????????????????????
                result.append('\n');
                
                // ???2?????????????????????????????????????????????????????????????????????????????????????????????
                number--;
                result.append(indent(number));
                
                //??????str
                result.append(key);
                continue;
            }
            
            //?????????","???????????????????????????????????????????????????
            if ((key == ',')) {
                result.append(key);
                result.append('\n');
                result.append(indent(number));
                continue;
            }
            
            if(key == ':') {
            	result.append(key);
            	result.append(' ');
            	continue;
            }
            
            result.append(key);
        }
        
        result.append('\n');
        return result.toString();
    }
	
	private String indent(int number) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < number; i++) {
            result.append("	");
        }
        return result.toString();
    }
}
