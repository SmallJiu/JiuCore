package cat.jiu.core.api;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.StringJoiner;
import java.util.stream.Stream;
import java.util.Map.Entry;

import com.google.common.collect.Lists;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.*;

/**
 * if you don't want to required JiuCore, but want to use json to itemstack function, copy this class to you mod project<p>
 * on you copied, you must keep this notes and keep class final, but you can rename class and method
 * @see cat.jiu.core.util.ItemUtils#toStack(JsonElement)
 * @see cat.jiu.core.util.ItemUtils#toStacks(JsonElement)
 * @version 0.0.1
 * @author small_jiu
 */
public final class JiuCore_JsonToStackUtil {
	public static ItemStack toStack(JsonElement e) {
		if(e==null) return null;
		if(e.isJsonObject()) {
			return toStack(e.getAsJsonObject());
		}else if(e.isJsonPrimitive()) {
			return toStack(e.getAsString());
		}
		return null;
	}
	
	public static List<ItemStack> toStacks(JsonElement e) {
		if(e==null) return null;
		if(e.isJsonObject()) {
			return toStacks(e.getAsJsonObject());
		}else if(e.isJsonArray()) {
			return toStacks(e.getAsJsonArray());
		}
		return null;
	}
	
	public static JsonArray toJsonArray(List<ItemStack> stacks, boolean useStringStack) {
		JsonArray array = new JsonArray();
		stacks.stream().forEach(stack -> {
			if(useStringStack) {
				String s = toString(stack);
				if(s!=null)array.add(s);
			}else {
				JsonElement e = toJson(stack);
				if(e!=null)array.add(e);
			}
		});
		return array;
	}
	
	public static JsonObject toJsonObject(List<ItemStack> stacks, boolean useStringStack) {
		JsonObject obj = new JsonObject();
		for(int i = 0; i < stacks.size(); i++) {
			ItemStack stack = stacks.get(i);
			String slot = Integer.toString(i);
			if(useStringStack) {
				String s = toString(stack);
				if(s!=null)obj.addProperty(slot, s);
			}else {
				JsonElement e = toJson(stack);
				if(e!=null)obj.add(slot, e);
			}
		}
		return obj;
	}
	
	public static JsonObject toJson(ItemStack arg) {
		if(arg == null || arg.isEmpty()) return null;
		JsonObject obj = new JsonObject();
		
		obj.addProperty("name", arg.getItem().getRegistryName().toString());
		obj.addProperty("count", arg.getCount());
		obj.addProperty("meta", arg.getMetadata());
		if(arg.hasTagCompound()) {
			JsonObject nbt = toJson(arg.getTagCompound());
			if(nbt != null) obj.add("nbt", nbt);
		}
		
		return obj;
	}
	
	private static String toString(ItemStack arg) {
		if(arg == null || arg.isEmpty()) return null;
		StringJoiner j = new StringJoiner("@");
		j.add(arg.getItem().getRegistryName().toString());
		j.add(Integer.toString(arg.getCount()));
		j.add(Integer.toString(arg.getMetadata()));
		if(arg.hasTagCompound()) {
			JsonObject nbt = toJson(arg.getTagCompound());
			if(nbt != null) j.add(nbt.toString());
		}
		return j.toString();
	}
	
	public static JsonArray toJson(NBTTagList list) {
		JsonArray array = new JsonArray();
		for(int i = 0; i < list.tagCount(); i++) {
			JsonElement e = toJson(list.get(i));
			if(e != null) {
				array.add(e);
			}
		}
		return array;
	}
	
	public static JsonObject toJson(NBTTagCompound nbt) {
		JsonObject obj = new JsonObject();
		for(String key : nbt.getKeySet()) {
			JsonElement e = toJson(nbt.getTag(key));
			if(e != null) {
				if(e.isJsonPrimitive()) {
					JsonPrimitive pri = (JsonPrimitive) e;
					if(pri.isNumber()) {
						addNumber(obj, key, pri);
					}else if(pri.isString()) {
						if(!obj.has("string")) {
							obj.add("string", new JsonObject());
						}
						obj.get("string").getAsJsonObject().add(key, pri);
					}else if(pri.isBoolean()) {
						if(!obj.has("boolean")) {
							obj.add("boolean", new JsonObject());
						}
						obj.get("boolean").getAsJsonObject().add(key, pri);
					}
				}else if(e.isJsonObject()) {
					if(!obj.has("tags")) obj.add("tags", new JsonObject()); 
					obj.get("tags").getAsJsonObject().add(key, e);
				}else if(e.isJsonArray()) {
					obj.add(key, e);
				}
			}
		}
		return obj;
	}
	
	private static void addNumber(JsonObject obj, String key, JsonPrimitive pri) {
		Number num = pri.getAsNumber();
		if(num instanceof Integer) {
			if(!obj.has("int")) {
				obj.add("int", new JsonObject());
			}
			obj.get("int").getAsJsonObject().add(key, pri);
		}else if(num instanceof Float) {
			if(!obj.has("float")) {
				obj.add("float", new JsonObject());
			}
			obj.get("float").getAsJsonObject().add(key, pri);
		}else if(num instanceof Short) {
			if(!obj.has("short")) {
				obj.add("short", new JsonObject());
			}
			obj.get("short").getAsJsonObject().add(key, pri);
		}else if(num instanceof Byte) {
			if(!obj.has("byte")) {
				obj.add("byte", new JsonObject());
			}
			obj.get("byte").getAsJsonObject().add(key, pri);
		}else if(num instanceof Double) {
			if(!obj.has("double")) {
				obj.add("double", new JsonObject());
			}
			obj.get("double").getAsJsonObject().add(key, pri);
		}else if(num instanceof Long) {
			if(!obj.has("long")) {
				obj.add("long", new JsonObject());
			}
			obj.get("long").getAsJsonObject().add(key, pri);
		}
	}
	
	private static JsonElement toJson(NBTBase base) {
		if(base instanceof NBTPrimitive) {
			NBTPrimitive pri = (NBTPrimitive) base;
			if(pri instanceof NBTTagByte) {
				return new JsonPrimitive(pri.getByte());
			}else if(pri instanceof NBTTagInt) {
				return new JsonPrimitive(pri.getInt());
			}else if(pri instanceof NBTTagLong) {
				return new JsonPrimitive(pri.getLong());
			}else if(pri instanceof NBTTagDouble) {
				return new JsonPrimitive(pri.getDouble());
			}else if(pri instanceof NBTTagFloat) {
				return new JsonPrimitive(pri.getFloat());
			}else if(pri instanceof NBTTagShort) {
				return new JsonPrimitive(pri.getShort());
			}
		}else if(base instanceof NBTTagString) {
			JsonArray num_array = getNumberArray((NBTTagString) base);
			if(num_array != null) return num_array;
			
			return new JsonPrimitive(((NBTTagString)base).getString());
		}else if(base instanceof NBTTagCompound) {
			return toJson((NBTTagCompound)base);
		}else if(base instanceof NBTTagList) {
			return toJson((NBTTagList)base);
		}else if(base instanceof NBTTagIntArray) {
			JsonArray array = new JsonArray();
			for(int i : ((NBTTagIntArray)base).getIntArray()) {
				array.add(i);
			}
			return array;
		}else if(base instanceof NBTTagByteArray) {
			JsonArray array = new JsonArray();
			for(byte i : ((NBTTagByteArray)base).getByteArray()) {
				array.add(i);
			}
			return array;
		}
		return JsonNull.INSTANCE;
	}
	
	private static JsonArray getNumberArray(NBTTagString str) {
		String s = str.getString().toLowerCase();
		if(s.contains("short_array@")) {
			JsonArray num_array = new JsonArray();
			String[] num_strs = splitString("@", s);
			if(num_strs.length >= 2) {
				String nums = num_strs[1];
				if(nums.contains(",")) {
					for (Short num : toNumberArray(Short.class, splitString(",", nums))) {
						num_array.add(num);
					}
				}else {
					num_array.add(Short.parseShort(nums));
				}
			}
			return num_array;
		}else if(s.contains("double_array@")) {
			JsonArray num_array = new JsonArray();
			String[] num_strs = splitString("@", s);
			if(num_strs.length >= 2) {
				String nums = num_strs[1];
				if(nums.contains(",")) {
					for (Double num : toNumberArray(Double.class, splitString(",", nums))) {
						num_array.add(num);
					}
				}else {
					num_array.add(Double.parseDouble(nums));
				}
			}
			return num_array;
		}else if(s.contains("float_array@")) {
			JsonArray num_array = new JsonArray();
			String[] num_strs = splitString("@", s);
			if(num_strs.length >= 2) {
				String nums = num_strs[1];
				if(nums.contains(",")) {
					for (Float num : toNumberArray(Float.class, splitString(",", nums))) {
						num_array.add(num);
					}
				}else {
					num_array.add(Float.parseFloat(nums));
				}
			}
			return num_array;
		}
		return null;
	}
	
	private static String[] splitString(String arg, String separator){
		if(arg==null || arg.isEmpty() || arg.equals("")) {
			return new String[] {"null"};
		}
		return arg.split("\\" + separator);
	}
	
	@SuppressWarnings("unchecked")
	private static <T extends Number> T[] toNumberArray(Class<T> num, String[] strs) {
		if(num == Long.class) {
			Long[] numss = new Long[strs.length];
			for (int i = 0; i < strs.length; i++) {
				numss[i] = Long.parseLong(strs[i]);
			}
			return (T[]) numss;
		}else if(num == Integer.class) {
			Integer[] numss = new Integer[strs.length];
			for (int i = 0; i < strs.length; i++) {
				numss[i] = Integer.parseInt(strs[i]);
			}
			return (T[]) numss;
		}else if(num == Short.class) {
			Short[] numss = new Short[strs.length];
			for (int i = 0; i < strs.length; i++) {
				numss[i] = Short.parseShort(strs[i]);
			}
			return (T[]) numss;
		}else if(num == Byte.class) {
			Byte[] numss = new Byte[strs.length];
			for (int i = 0; i < strs.length; i++) {
				numss[i] = Byte.parseByte(strs[i]);
			}
			return (T[]) numss;
		}else if(num == Double.class) {
			Double[] numss = new Double[strs.length];
			for (int i = 0; i < strs.length; i++) {
				numss[i] = Double.parseDouble(strs[i]);
			}
			return (T[]) numss;
		}else if(num == Float.class) {
			Float[] numss = new Float[strs.length];
			for (int i = 0; i < strs.length; i++) {
				numss[i] = Float.parseFloat(strs[i]);
			}
			return (T[]) numss;
		}else if(num == BigInteger.class) {
			BigInteger[] numss = new BigInteger[strs.length];
			for (int i = 0; i < strs.length; i++) {
				numss[i] = new BigInteger(strs[i]);
			}
			return (T[]) numss;
		}else if(num == BigDecimal.class) {
			BigDecimal[] numss = new BigDecimal[strs.length];
			for (int i = 0; i < strs.length; i++) {
				numss[i] = new BigDecimal(strs[i]);
			}
			return (T[]) numss;
		}
		
		return null;
	}
	
	private static List<ItemStack> toStacks(JsonArray array) {
		if(array.size() == 0) return null;
		List<ItemStack> stacks = Lists.newArrayList();
		array.forEach(e -> {
			if(e.isJsonArray()) {
				List<ItemStack> stackss = toStacks(e);
				if(stackss != null) stacks.addAll(stackss);
			}else {
				ItemStack s = toStack(e);
				if(s != null && !s.isEmpty()) {
					stacks.add(s);
				}
			}
		});
		if(stacks.isEmpty()) return null;
		return stacks;
	}
	
	private static List<ItemStack> toStacks(JsonObject obj) {
		if(obj.size() == 0) return null;
		List<ItemStack> stacks = Lists.newArrayList();
		obj.entrySet().stream().forEach(es -> {
			JsonElement e = es.getValue();
			if(e.isJsonArray()) {
				List<ItemStack> stackss = toStacks(e);
				if(stackss != null) stacks.addAll(stackss);
			}else {
				ItemStack s = toStack(e);
				if(s != null && !s.isEmpty()) {
					stacks.add(s);
				}
			}
		});
		if(stacks.isEmpty()) return null;
		return stacks;
	}
	
	private static ItemStack toStack(JsonObject obj) {
		if(obj==null) return null;
		if(!obj.has("name")) {
			if(!obj.has("clazz")) {
				return null;
			}
		}
		String name = obj.has("name") ? 
				obj.get("name").getAsString() : 
				obj.get("clazz").getAsString();
		Item item = Item.getByNameOrId(name);
		if(item == null) return null;
		int count = obj.has("count") ? obj.get("count").getAsInt() : obj.has("amount") ? obj.get("amount").getAsInt() : 1;
		int meta = obj.has("meta") ? obj.get("meta").getAsInt() : obj.has("data") ? obj.get("data").getAsInt() : 0;
		NBTTagCompound nbt = obj.has("nbt") ? toNBT(obj.get("nbt").getAsJsonObject()) : null;

		return setNBT(new ItemStack(item, count, meta), nbt);
	}
	
	public static NBTTagCompound toNBT(JsonObject obj) {
		NBTTagCompound nbt = new NBTTagCompound();
		for(Entry<String, JsonElement> objTags : obj.entrySet()) {
			String key = objTags.getKey();
			JsonElement value = objTags.getValue();
			if(value.isJsonObject()) {
				JsonObject valueObj = value.getAsJsonObject();
				Stream<Entry<String, JsonElement>> stream = valueObj.entrySet().stream();
				if("string".equals(key)) {
					stream.forEach(nbts -> nbt.setString(nbts.getKey(), nbts.getValue().getAsString()));
				}else if("boolean".equals(key)) {
					stream.forEach(nbts -> nbt.setBoolean(nbts.getKey(), nbts.getValue().getAsBoolean()));
				}else if("int".equals(key)) {
					stream.forEach(nbts -> nbt.setInteger(nbts.getKey(), nbts.getValue().getAsInt()));
				}else if("long".equals(key)) {
					stream.forEach(nbts -> nbt.setLong(nbts.getKey(), nbts.getValue().getAsLong()));
				}else if("float".equals(key)) {
					stream.forEach(nbts -> nbt.setFloat(nbts.getKey(), nbts.getValue().getAsFloat()));
				}else if("double".equals(key)) {
					stream.forEach(nbts -> nbt.setDouble(nbts.getKey(), nbts.getValue().getAsDouble()));
				}else if("byte".equals(key)) {
					stream.forEach(nbts -> nbt.setByte(nbts.getKey(), nbts.getValue().getAsByte()));
				}else if("short".equals(key)) {
					stream.forEach(nbts -> nbt.setShort(nbts.getKey(), nbts.getValue().getAsShort()));
				}else if("int_array".equals(key)) {
					stream.forEach(nbts -> {
						JsonArray array = nbts.getValue().getAsJsonArray();
						int[] int_array = new int[array.size()];
						for(int i = 0; i < array.size(); i++) {
							int_array[i] = array.get(i).getAsInt();
						}
						nbt.setIntArray(nbts.getKey(), int_array);
					});
				}else if("short_array".equals(key)) {
					stream.forEach(nbts -> {
						JsonArray array = nbts.getValue().getAsJsonArray();
						short[] num_array = new short[array.size()];
						for(int i = 0; i < array.size(); i++) {
							num_array[i] = array.get(i).getAsShort();
						}
						setNBT(nbt, nbts.getKey(), num_array);
					});
				}else if("byte_array".equals(key)) {
					stream.forEach(nbts -> {
						JsonArray array = nbts.getValue().getAsJsonArray();
						byte[] num_array = new byte[array.size()];
						for(int i = 0; i < array.size(); i++) {
							num_array[i] = array.get(i).getAsByte();
						}
						nbt.setByteArray(nbts.getKey(), num_array);
					});
				}else if("double_array".equals(key)) {
					stream.forEach(nbts -> {
						JsonArray array = nbts.getValue().getAsJsonArray();
						double[] num_array = new double[array.size()];
						for(int i = 0; i < array.size(); i++) {
							num_array[i] = array.get(i).getAsDouble();
						}
						setNBT(nbt, nbts.getKey(), num_array);
					});
				}else if("float_array".equals(key)) {
					stream.forEach(nbts -> {
						JsonArray array = nbts.getValue().getAsJsonArray();
						float[] num_array = new float[array.size()];
						for(int i = 0; i < array.size(); i++) {
							num_array[i] = array.get(i).getAsFloat();
						}
						setNBT(nbt, nbts.getKey(), num_array);
					});
				}else if("tags".equals(key)) {
					stream.forEach(tags -> nbt.setTag(tags.getKey(), toNBT(tags.getValue().getAsJsonObject())));
				}else {
					nbt.setTag(key, toNBT(valueObj)); 
				}
			}else if(value.isJsonArray()) {
				nbt.setTag(key, toNBT(value.getAsJsonArray())); 
			}
		}
		
		return nbt;
	}
	
	public static NBTTagList toNBT(JsonArray array) {
		NBTTagList list = new NBTTagList();
		for(int i = 0; i < array.size(); i++) {
			JsonElement array_element = array.get(i);
			if(array_element.isJsonObject()) {
				list.appendTag(toNBT(array_element.getAsJsonObject()));
			}else if(array_element.isJsonArray()) {
				list.appendTag(toNBT(array_element.getAsJsonArray()));
			}else if(array_element.isJsonPrimitive()) {
				JsonPrimitive pri = array_element.getAsJsonPrimitive();
				if(pri.isString()) {
					list.appendTag(new NBTTagString(pri.getAsString()));
				}else if(pri.isBoolean()) {
					list.appendTag(new NBTTagByte(pri.getAsBoolean() ? (byte)1 : (byte)0));
				}else if(pri.isNumber()) {
					Number num = pri.getAsNumber();
					if(num instanceof Integer) {
						list.appendTag(new NBTTagInt((Integer) num));
					}else if(num instanceof Double) {
						list.appendTag(new NBTTagDouble((Double) num));
					}else if(num instanceof Byte) {
						list.appendTag(new NBTTagByte((Byte) num)); 
					}else if(num instanceof Long) {
						list.appendTag(new NBTTagLong((Long) num)); 
					}else if(num instanceof Float) {
						list.appendTag(new NBTTagFloat((Float) num)); 
					}else if(num instanceof Short) {
						list.appendTag(new NBTTagShort((Short) num)); 
					}
				}
			}
		}
		return list;
	}
	static final JsonParser parser = new JsonParser();
	
	private static ItemStack toStack(String stack) {
		if(stack.contains("@")) {
			String[] name = stack.split("@");
			Item item = Item.getByNameOrId(name[0]);
			if(item!=null) {
				int meta = 0;
				int amount = 1;
				NBTTagCompound nbt = null;
				
				switch(name.length) {
					case 4:
						nbt = toNBT(parser.parse(name[3]).getAsJsonObject());
					case 3:
						meta = Integer.parseInt(name[2]);
					case 2:
						amount = Integer.parseInt(name[1]);
						break;
				}
				return setNBT(new ItemStack(item, amount, meta), nbt);
			}
		}else {
			return new ItemStack(Item.getByNameOrId(stack));
		}
		return null;
	}
	
	private static NBTTagCompound setNBT(NBTTagCompound nbt, String nbtName, double[] value) {
		nbt.setString(nbtName, "double_array@" + toString(toArray(value)));
		return nbt;
	}
	private static NBTTagCompound setNBT(NBTTagCompound nbt, String nbtName, float[] value) {
		nbt.setString(nbtName, "float_array@" + toString(toArray(value)));
		return nbt;
	}
	private static NBTTagCompound setNBT(NBTTagCompound nbt, String nbtName, short[] value) {
		nbt.setString(nbtName, "short_array@" + toString(toArray(value)));
		return nbt;
	}
	
	private static ItemStack setNBT(ItemStack stack, NBTTagCompound nbt) {
		if(stack!=null && !stack.isEmpty() && nbt != null) stack.setTagCompound(nbt);
		return stack;
	}
	
	private static <T> String toString(T[] args) {
		if(args == null || args.length == 0) {
			return "null";
		}
		String[] strs = new String[args.length];
		for(int i = 0; i < args.length; i++) {
			strs[i] = String.valueOf(args[i]);
		}
		
		StringJoiner j = new StringJoiner(",");
		for(String arg : strs) {
			j.add(arg);
		}
		return j.toString();
	}
	
	private static Short[] toArray(short[] args) {
		Short[] arg = new Short[args.length];
		for (int i = 0; i < arg.length; i++) {
			arg[i] = args[i];
		}
		return arg;
	}
	
	private static Double[] toArray(double[] args) {
		Double[] arg = new Double[args.length];
		for (int i = 0; i < arg.length; i++) {
			arg[i] = args[i];
		}
		return arg;
	}
	
	private static Float[] toArray(float[] args) {
		Float[] arg = new Float[args.length];
		for (int i = 0; i < arg.length; i++) {
			arg[i] = args[i];
		}
		return arg;
	}
}
