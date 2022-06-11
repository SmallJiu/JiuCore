package cat.jiu.core.api.values;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import cat.jiu.core.JiuCore;
import cat.jiu.core.commands.CommandJiuCore;
import cat.jiu.core.util.JiuUtils;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;

public class Values {
	static final HashMap<UUID, HashMap<String, BigInteger>> values = Maps.newHashMap();
	static final HashMap<String, HashMap<String, String>> language = Maps.newHashMap();
	static final HashMap<String, BigInteger> default_value = Maps.newHashMap();
	static final ArrayList<String> value_list = Lists.newArrayList();
	static final UUID Initialization = new UUID(0,0);
	static int changeTime = 0;
	
	public static boolean hasValue(String valueID) {
		return value_list.contains(valueID);
	}
	
	public static String getValueName(String valueID) {
		String s = JiuUtils.other.upperFirst(valueID);
		boolean lag = false;
		try { Class.forName("net.minecraft.client.Minecraft"); lag = true; }catch(Throwable e) {e.fillInStackTrace();}
		
		if(lag) {
			String langCode = Minecraft.getMinecraft().getLanguageManager().getCurrentLanguage().getLanguageCode();
			if(language.containsKey(langCode)) {
				if(language.get(langCode).containsKey(valueID)) {
					s = language.get(langCode).get(valueID);
				}
			}
		}else {
			if(language.containsKey("en_us")) {
				if(language.get("en_us").containsKey(valueID)) {
					s = language.get("en_us").get(valueID);
				}
			}
		}
		return s;
	}
	
	/**
	 * add custom value to map and storage file, Default Value is Zero
	 * @param valueID The custom value name
	 * 
	 * @author small_jiu
	 */
	public static void addValue(String valueID) {
		addValue(valueID, BigInteger.ZERO);
	}
	
	public static void addValue(String valueID, long defaultValue) {
		addValue(valueID, BigInteger.valueOf(defaultValue));
	}
	
	/**
	 * add custom value to map and storage file
	 * @param valueID The custom value name
	 * @param defaultValue Initial value on add custom value
	 * 
	 * @author small_jiu
	 */
	public static void addValue(String valueID, BigInteger defaultValue) {
		if("name".equals(valueID)) return;
		String s = valueID.toUpperCase();
		for(int i = 0; i < s.length(); i++) {
			char letter = s.charAt(i);
			if(!JiuCore.CHAR_LETTERS.contains(letter) && letter != '_') {
				throw new RuntimeException("Value name must be ALL Letter: " + valueID + " -> " + letter);
			}
		}
		
		if(!value_list.contains(valueID)) {
			value_list.add(valueID);
			default_value.put(valueID, defaultValue);
			
			for(Entry<UUID, HashMap<String, BigInteger>> value : values.entrySet()) {
				if(!value.getValue().containsKey(valueID)) {
					value.getValue().put(valueID, defaultValue);
				}
			}
			
			if(!values.containsKey(Initialization)) {
				values.put(Initialization, Maps.newHashMap());
			}
			if(!values.get(Initialization).containsKey(valueID)) {
				values.get(Initialization).put(valueID, defaultValue);
			}
			
			saveValue();
		}else {
			JiuCore.getLogOS().error(valueID + " is Already added.");
		}
	}
	
	/**
	 * set player owner value
	 * @param valueID The value name
	 * @param uid The Player UUID
	 * @param value Number of value
	 * @param writeTime 
	 * @return finish state
	 * 
	 * @author small_jiu
	 */
	public static ValueStateType set(String valueID, UUID uid, BigInteger value, int writeTime) {
		if(!value_list.contains(valueID)) return ValueStateType.NOT_FOUND_VALUE;
		if(uid.equals(Initialization)) return ValueStateType.Initialization;
		if(!values.containsKey(uid)) values.put(uid, Maps.newHashMap());
		
		Map<String, BigInteger> map = values.get(uid);
		
		if(!map.containsKey(valueID)) {
			if(value_list.contains(valueID)) {
				map.put(valueID, default_value.get(valueID));
			}
		}
		
		if(map.containsKey(valueID)) {
			map.replace(valueID, value);
			changeTime += 1;
			
			if(changeTime >= writeTime) {
				changeTime = 0;
				saveValue();
				return ValueStateType.SUCCESS;
			}
			
			return ValueStateType.NOT_ARRIVAL_TIME;
		}else {
			return ValueStateType.NOT_FOUND_VALUE;
		}
	}
	
	/**
	 * give player value
	 * @param valueID The value name
	 * @param uid The Player UUID
	 * @param value Number of value
	 * @param writeTime 
	 * @return finish state
	 * 
	 * @author small_jiu
	 */
	public static ValueStateType add(String valueID, UUID uid, BigInteger value, int writeTime) {
		if(!value_list.contains(valueID)) return ValueStateType.NOT_FOUND_VALUE;
		if(uid.equals(Initialization)) return ValueStateType.Initialization;
		if(!values.containsKey(uid)) values.put(uid, Maps.newHashMap());
		
		Map<String, BigInteger> map = values.get(uid);
		if(!map.containsKey(valueID) || map.get(valueID) == null) {
			if(value_list.contains(valueID)) {
				map.put(valueID, default_value.get(valueID));
			}
		}
		map.replace(valueID, map.get(valueID).add(value));
		changeTime += 1;
		if(changeTime >= writeTime) {
			changeTime = 0;
			saveValue();
			return ValueStateType.SUCCESS;
		}
		return ValueStateType.NOT_ARRIVAL_TIME;
	}
	
	/**
	 * take player owner value not equal negative num
	 * @param valueID The value name
	 * @param uid The Player UUID
	 * @param value Number of value
	 * @return true if remainder >= zero
	 * 
	 * @author small_jiu
	 */
	public static boolean canSubtract(String valueID, UUID uid, BigInteger value) {
		if(!uid.equals(Initialization)) {
			if(values.containsKey(uid) && values.get(uid).containsKey(valueID)) {
				return JiuUtils.big_integer.greaterOrEqual(get(valueID, uid).subtract(value), BigInteger.ZERO);
			}
		}
		return false;
	}
	
	/**
	 * take player owner value
	 * @param valueID The value name
	 * @param uid The Player UUID
	 * @param value Number of value
	 * @param writeTime 
	 * @return finish state
	 * 
	 * @author small_jiu
	 */
	public static ValueStateType subtract(String valueID, UUID uid, BigInteger value, int writeTime) {
		if(!value_list.contains(valueID)) return ValueStateType.NOT_FOUND_VALUE;
		if(uid.equals(Initialization)) return ValueStateType.Initialization;
		if(uid == null || valueID == null || value == null) return ValueStateType.NULL;
		if(!values.containsKey(uid)) values.put(uid, Maps.newHashMap());
		
		Map<String, BigInteger> map = values.get(uid);
		if(!map.containsKey(valueID)) {
			if(value_list.contains(valueID)) {
				map.put(valueID, default_value.get(valueID));
			}
		}
		if(BigInteger.ZERO.max(map.get(valueID).subtract(value)).equals(map.get(valueID).subtract(value))) {
			map.replace(valueID, map.get(valueID).subtract(value));
			changeTime += 1;
			if(changeTime >= writeTime) {
				changeTime = 0;
				saveValue();
				return ValueStateType.SUCCESS;
			}
			return ValueStateType.NOT_ARRIVAL_TIME;
		}else {
			return ValueStateType.ZERO;
		}
	}
	
	/**
	 * reset player owner value to Default Value
	 * 
	 * @param valueID The value name
	 * @param uid The Player UUID
	 * @param writeTime 
	 * @return finish state
	 * 
	 * @author small_jiu
	 */
	public static ValueStateType remove(String valueID, UUID uid) {
		return set(valueID, uid, default_value.get(valueID), 0);
	}
	
	/**
	 * get player owner value
	 * @param valueID The value name
	 * @param uid The Player UUID
	 * @return Player own value
	 * 
	 * @author small_jiu
	 */
	public static BigInteger get(String valueID, UUID uid) {
		if(uid == null || uid.equals(Initialization) || !value_list.contains(valueID)) {
			return BigInteger.ZERO;
		}
		if(!values.containsKey(uid)) {
			values.put(uid, Maps.newHashMap());
		}
		Map<String, BigInteger> map = values.get(uid);
		if(!map.containsKey(valueID)) {
			if(value_list.contains(valueID)) {
				map.put(valueID, default_value.get(valueID));
			}
		}
		return map.get(valueID);
	}
	
	/**
	 * use this method to load storage file
	 * 
	 * @return finish state
	 * 
	 * @author small_jiu
	 */
	public static ValueStateType loadFromFile() {
		values.clear();
		File file = new File("./values/value.json");
		if(!file.exists()) {
			loadCustomValues();
			return ValueStateType.FILE_NOT_FOUND;
		}else {
			try(FileInputStream in = new FileInputStream(file)) {
				JsonElement obj = new JsonParser().parse(new InputStreamReader(in, StandardCharsets.UTF_8));
				if(obj.isJsonObject()) {
					for(Entry<String, JsonElement> valuesObj : ((JsonObject) obj).entrySet()) {
						UUID uid = UUID.fromString(valuesObj.getKey());
						values.put(uid, Maps.newHashMap());
						JsonObject valueObj = valuesObj.getValue().getAsJsonObject();
						
						for(Entry<String, JsonElement> values : valueObj.entrySet()) {
							if(!values.getKey().equals("name")) {
								Values.values.get(uid).put(values.getKey(), values.getValue().getAsBigInteger());
							}
							
						}
					}
				}
				
				loadCustomValues();
				return ValueStateType.SUCCESS;
			} catch (IOException e) {
				e.printStackTrace();
				return ValueStateType.IOError;
			}
		}
	}
	
	private static void loadCustomValues() {
		File file = new File("./values/custom.json");
		if(!file.exists()) {
			return;
		}
		try(FileInputStream in = new FileInputStream(file)) {
			JsonObject obj = new JsonParser().parse(new InputStreamReader(in, StandardCharsets.UTF_8)).getAsJsonObject();
			
			JsonObject defaultValue = obj.has("default") ? obj.get("default").getAsJsonObject() : null;
			
			JsonArray array = obj.get("custom").getAsJsonArray();
			for (int i = 0; i < array.size(); i++) {
				String value = array.get(i).getAsString();
				CommandJiuCore.unRegisterValue.add(value);
				
				if(defaultValue != null) {
					if(defaultValue.has(value)) {
						Values.addValue(value, defaultValue.get(value).getAsBigInteger());
					}else {
						Values.addValue(value);
					}
				}else {
					Values.addValue(value);
				}
			}
			
			if(obj.has("language")) {
				language.clear();
				for(Entry<String, JsonElement> valuesObj : obj.get("language").getAsJsonObject().entrySet()) {
					String langName = valuesObj.getKey();
					language.put(langName, Maps.newHashMap());
					
					for(Entry<String, JsonElement> values : valuesObj.getValue().getAsJsonObject().entrySet()) {
						language.get(langName).put(values.getKey(), values.getValue().getAsString());
					}
				}
				if(!language.containsKey("zh_cn")) {
					language.put("zh_cn", Maps.newHashMap());
				}
				if(!language.containsKey("en_us")) {
					language.put("en_us", Maps.newHashMap());
				}
				
				language.get("zh_cn").put("coin", "\u786c\u5e01");
				language.get("en_us").put("coin", "Coin");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void saveValue() {
		for(int i = 0; i < 10; i++) {
			if(Values.saveValues()) {
				JiuCore.getLogOS().info("Save values to file success, retry count: {}", i);
				return;
			}
		}
		JiuCore.getLogOS().error("Can't save values to File!");
	}
	
	private static boolean saveValues() {
		if(!values.isEmpty()) {
			JsonObject obj = JiuUtils.json.toJsonObject(values);
			for(Entry<String, JsonElement> objValue : obj.entrySet()) {
				objValue.getValue().getAsJsonObject().addProperty("name", JiuUtils.entity.getName(UUID.fromString(objValue.getKey())));
			}
			return JiuUtils.json.toJsonFile("./values/value.json", obj);
		}else {
			return false;
		}
	}
	
	/**
	 * SUCCESS: All done<p>
	 * UNABLE_WRITE: success, but not write to storage file<p>
	 * NULL: UUID is null<p>
	 * ZERO: After subtract it will be negative number<p>
	 * NOT_FOUND_VALUE: Not found of value name<p>
	 * FILE_NOT_FOUND: Not found values storage file<p>
	 * FAIL: unknown Error<p>
	 * IOError: IOException<p>
	 * NOT_ARRIVAL_TIME: The number of changes did not reach the specified number<p>
	 * Initialization name can NOT be Initialization
	 * 
	 * 
	 * @author small_jiu
	 */
	public static enum ValueStateType {
		/** All done */ SUCCESS,
		/** success, but not write to storage file */ UNABLE_WRITE,
		/** UUID is null */ NULL,
		/** After subtract, it will be negative number */ ZERO,
		/** Not found of value name */ NOT_FOUND_VALUE,
		/** Not found value's storage file */ FILE_NOT_FOUND,
		/** unknown Error */ FAIL,
		/** The number of changes did not reach the specified number */NOT_ARRIVAL_TIME,
		/** IOException */ IOError,
		/** name can NOT be Initialization*/ Initialization;
	}
	
	/** {@link #remove(String, UUID, int)} */
	public static ValueStateType remove(String valueID, String name) { return remove(valueID, JiuUtils.entity.getUUID(name)); }
	/** {@link #get(String, UUID)} */
	public static BigInteger get(String valueID, String name) { return get(valueID, JiuUtils.entity.getUUID(name)); }
	/** {@link #add(String, UUID, BigInteger, int)} */
	public static ValueStateType add(String valueID, UUID uid, long value, int writeTime) {return add(valueID, uid, BigInteger.valueOf(value), writeTime);}
	/** {@link #subtract(String, UUID, BigInteger, int)} */
	public static ValueStateType subtract(String valueID, UUID uid, long value, int writeTime) { return subtract(valueID, uid, BigInteger.valueOf(value), writeTime); }
	/** {@link #set(String, UUID, BigInteger, int)} */
	public static ValueStateType set(String valueID, UUID uid, long value, int writeTime) { return set(valueID, uid, BigInteger.valueOf(value), writeTime); }
	
	/** {@link #add(String, UUID, BigInteger, int)} */
	public static ValueStateType add(String valueID, String name, BigInteger value, int writeTime) { return add(valueID, EntityPlayer.getOfflineUUID(name), value, writeTime); }
	/** {@link #subtract(String, UUID, BigInteger, int)} */
	public static ValueStateType subtract(String valueID, String name, BigInteger value, int writeTime) { return subtract(valueID, JiuUtils.entity.getUUID(name), value, writeTime); }
	/** {@link #set(String, UUID, BigInteger, int)} */
	public static ValueStateType set(String valueID, String name, BigInteger value, int writeTime) { return set(valueID, JiuUtils.entity.getUUID(name), value, writeTime); }
	
	/** {@link #add(String, UUID, BigInteger, int)} */
	public static ValueStateType add(String valueID, String name, long value, int writeTime) { return add(valueID, name, BigInteger.valueOf(value), writeTime); }
	/** {@link #subtract(String, UUID, BigInteger, int)} */
	public static ValueStateType subtract(String valueID, String name, long value, int writeTime) { return subtract(valueID, name, BigInteger.valueOf(value), writeTime); }
	/** {@link #set(String, UUID, BigInteger, int)} */
	public static ValueStateType set(String valueID, String name, long value, int writeTime) { return set(valueID, name, BigInteger.valueOf(value), writeTime); }
	
	/** {@link #canSubtract(String, UUID, BigInteger)}} */
	public static boolean canSubtract(String valueID, UUID uid, long value) {return canSubtract(valueID, uid, BigInteger.valueOf(value));};
	/** {@link #canSubtract(String, UUID, BigInteger)}} */
	public static boolean canSubtract(String valueID, String name, BigInteger value) {return canSubtract(valueID, JiuUtils.entity.getUUID(name), value);};
	/** {@link #canSubtract(String, UUID, BigInteger)}} */
	public static boolean canSubtract(String valueID, String name, long value) {return canSubtract(valueID, JiuUtils.entity.getUUID(name), BigInteger.valueOf(value));};
	
	/*	
	 * Aliases method
	*/
	/** {@link #add(String, UUID, BigInteger, int)} */
	public static ValueStateType give(String valueID, String name, long value, int writeTime) { return add(valueID, name, BigInteger.valueOf(value), writeTime); }
	/** {@link #add(String, UUID, BigInteger, int)} */
	public static ValueStateType give(String valueID, UUID uid, long value, int writeTime) {return add(valueID, uid, BigInteger.valueOf(value), writeTime);}
	/** {@link #add(String, UUID, BigInteger, int)} */
	public static ValueStateType give(String valueID, String name, BigInteger value, int writeTime) { return add(valueID, JiuUtils.entity.getUUID(name), value, writeTime); }
	
	/** {@link #subtract(String, UUID, BigInteger, int)} */
	public static ValueStateType take(String valueID, String name, long value, int writeTime) { return subtract(valueID, name, BigInteger.valueOf(value), writeTime); }
	/** {@link #subtract(String, UUID, BigInteger, int)} */
	public static ValueStateType take(String valueID, UUID uid, long value, int writeTime) { return subtract(valueID, uid, BigInteger.valueOf(value), writeTime); }
	/** {@link #subtract(String, UUID, BigInteger, int)} */
	public static ValueStateType take(String valueID, String name, BigInteger value, int writeTime) { return subtract(valueID, JiuUtils.entity.getUUID(name), value, writeTime); }
	
	/** {@link #canSubtract(String, UUID, BigInteger)}} */
	public static boolean canTake(String valueID, UUID uid, long value) {return canSubtract(valueID, uid, BigInteger.valueOf(value));};
	/** {@link #canSubtract(String, UUID, BigInteger)}} */
	public static boolean canTake(String valueID, String name, BigInteger value) {return canSubtract(valueID, JiuUtils.entity.getUUID(name), value);};
	/** {@link #canSubtract(String, UUID, BigInteger)}} */
	public static boolean canTake(String valueID, String name, long value) {return canSubtract(valueID, JiuUtils.entity.getUUID(name), BigInteger.valueOf(value));};
	
	/** {@link #remove(String, UUID, BigInteger)} */
	public static ValueStateType reset(String valueID, String name) {return remove(valueID, name);}
	/** {@link #remove(String, UUID, BigInteger)} */
	public static ValueStateType reset(String valueID, UUID uid) {return remove(valueID, uid);}
	/** {@link #remove(String, UUID, BigInteger)} */
	public static ValueStateType purge(String valueID, String name) {return remove(valueID, name);}
	/** {@link #remove(String, UUID, BigInteger, int)} */
	public static ValueStateType purge(String valueID, UUID uid) {return remove(valueID, uid);}
	
	/** {@link #get(String, UUID)} */
	public static BigInteger look(String valueID, String name) {return get(valueID, name);}
	/** {@link #get(String, UUID)} */
	public static BigInteger look(String valueID, UUID uid) {return get(valueID, uid);}
	/** {@link #get(String, UUID)} */
	public static BigInteger other(String valueID, String name) {return get(valueID, name);}
	/** {@link #get(String, UUID)} */
	public static BigInteger other(String valueID, UUID uid) {return get(valueID, uid);}
}
