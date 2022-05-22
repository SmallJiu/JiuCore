package cat.jiu.core.util.system.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.io.IOUtils;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import scala.collection.mutable.StringBuilder;

public class JsonUtil {
	public boolean toJsonFile(String path, Object src) {
		String json = new GsonBuilder().create().toJson(src);
		
		try {
			File file = new File(path);
	        if (!file.getParentFile().exists()) { // 如果父目录不存在，创建父目录
	            file.getParentFile().mkdirs();
	        }
	        if (file.exists()) { // 如果已存在,删除旧文件
	            file.delete();
	        }
	        
	        file.createNewFile();
	        Writer write = new OutputStreamWriter(new FileOutputStream(file), "UTF-8");
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
		return new GsonBuilder().create().fromJson(result, clazz);
	}
	
	public <T> T[] readArray(String path, Class<T> clazz) {
		return null;
	}
	
	public String formatJson(String json) {
        StringBuffer result = new StringBuffer();
        int length = json.length();
        int number = 0;
        char key = 0;
        
        for (int i = 0; i < length; i++) {
            // 1、获取当前字符。
            key = json.charAt(i);
            
            // 2、如果当前字符是前方括号/前花括号做如下处理：
            if ((key == '[') || (key == '{')) {
                //增加str
                result.append(key);
                
                // （3）前方括号/前花括号，的后面必须换行。打印：换行。
                result.append('\n');
                
                // （4）每出现一次前方括号、前花括号；缩进次数增加一次。打印：新行缩进。
                number++;
                result.append(indent(number));
                
                continue;
            }
            
            // 3、如果当前字符是后方括号、后花括号做如下处理：
            if ((key == ']') || (key == '}')) {
                // （1）后方括号、后花括号，的前面必须换行。打印：换行。
                result.append('\n');
                
                // （2）每出现一次后方括号、后花括号；缩进次数减少一次。打印：缩进。
                number--;
                result.append(indent(number));
                
                //增加str
                result.append(key);
                continue;
            }
            
            //如果是","，则换行并缩进，并且不改变缩进次数
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
