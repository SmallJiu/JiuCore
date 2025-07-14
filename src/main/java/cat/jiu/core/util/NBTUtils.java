package cat.jiu.core.util;

import com.google.common.collect.Lists;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import net.minecraft.nbt.*;
import net.minecraft.util.StringUtil;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.StringJoiner;

public class NBTUtils {
    public static Tag get(CompoundTag data, String k, Tag failBack) {
        if (data.contains(k)) {
            return data.get(k);
        }
        return failBack;
    }
    public static byte get(CompoundTag data, String k, byte failBack) {
        if (data.contains(k)) {
            return data.getByte(k);
        }
        return failBack;
    }
    public static short get(CompoundTag data, String k, short failBack) {
        if (data.contains(k)) {
            return data.getShort(k);
        }
        return failBack;
    }
    public static int get(CompoundTag data, String k, int failBack) {
        if (data.contains(k)) {
            return data.getInt(k);
        }
        return failBack;
    }
    public static long get(CompoundTag data, String k, long failBack) {
        if (data.contains(k)) {
            return data.getLong(k);
        }
        return failBack;
    }
    public static String get(CompoundTag data, String k, String failBack) {
        if (data.contains(k)) {
            return data.getString(k);
        }
        return failBack;
    }
    public static boolean get(CompoundTag data, String k, boolean failBack) {
        if (data.contains(k)) {
            return data.getBoolean(k);
        }
        return failBack;
    }
    public static CompoundTag get(CompoundTag data, String k, CompoundTag failBack) {
        if (data.contains(k)) {
            return data.getCompound(k);
        }
        return failBack;
    }
    public static ListTag get(CompoundTag data, String k, int type, ListTag failBack) {
        if (data.contains(k)) {
            return data.getList(k, type);
        }
        return failBack;
    }
    public static float get(CompoundTag data, String k, float failBack) {
        if (data.contains(k)) {
            return data.getFloat(k);
        }
        return failBack;
    }
    public static double get(CompoundTag data, String k, double failBack) {
        if (data.contains(k)) {
            return data.getDouble(k);
        }
        return failBack;
    }
    public static BigInteger get(CompoundTag data, String k, BigInteger failBack) {
        String string = get(data, k, (String) null);
        if (!StringUtil.isNullOrEmpty(string)) {
            return new BigInteger(string);
        }
        return failBack;
    }
    public static BigDecimal get(CompoundTag data, String k, BigDecimal failBack) {
        String string = get(data, k, (String) null);
        if (!StringUtil.isNullOrEmpty(string)) {
            return new BigDecimal(string);
        }
        return failBack;
    }

    public static JsonElement toJson(Tag base) {
        if(base instanceof NumericTag) {
            return new JsonPrimitive(((NumericTag) base).getAsNumber());
        }else if(base instanceof StringTag) {
            JsonArray num_array = getNumberArray((StringTag) base);
            if(num_array != null) return num_array;

            return new JsonPrimitive(base.getAsString());
        }else if(base instanceof CompoundTag) {
            return toJson((CompoundTag) base);
        }else if(base instanceof ListTag) {
            return toJson((ListTag) base);
        }else if(base instanceof IntArrayTag) {
            JsonArray array = new JsonArray();
            for(int i : ((IntArrayTag)base).getAsIntArray()) {
                array.add(i);
            }
            return array;
        }else if(base instanceof ByteArrayTag) {
            JsonArray array = new JsonArray();
            for(byte i : ((ByteArrayTag)base).getAsByteArray()) {
                array.add(i);
            }
            return array;
        }
        return null;
    }

    public static JsonArray toJson(ListTag list) {
        JsonArray array = new JsonArray();
        for (Tag inbt : list) {
            JsonElement e = toJson(inbt);
            if (e != null) {
                array.add(e);
            }
        }
        return array;
    }

    public static JsonObject toJson(CompoundTag nbt) {
        JsonObject obj = new JsonObject();
        for(String key : nbt.getAllKeys()) {
            JsonElement e = toJson(nbt.get(key));
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

    private static JsonArray getNumberArray(StringTag str) {
        String s = str.getAsString().toLowerCase();
        if(s.contains("short_array@")) {
            JsonArray num_array = new JsonArray();
            String[] num_strs = custemSplitString("@", s);
            if(num_strs.length >= 2) {
                String nums = num_strs[1];
                if(nums.contains(",")) {
                    for (Short num : toNumberArray(Short.class, custemSplitString(",", nums))) {
                        num_array.add(num);
                    }
                }else {
                    num_array.add(Short.parseShort(nums));
                }
            }
            return num_array;
        }else if(s.contains("double_array@")) {
            JsonArray num_array = new JsonArray();
            String[] num_strs = custemSplitString("@", s);
            if(num_strs.length >= 2) {
                String nums = num_strs[1];
                if(nums.contains(",")) {
                    for (Double num : toNumberArray(Double.class, custemSplitString(",", nums))) {
                        num_array.add(num);
                    }
                }else {
                    num_array.add(Double.parseDouble(nums));
                }
            }
            return num_array;
        }else if(s.contains("float_array@")) {
            JsonArray num_array = new JsonArray();
            String[] num_strs = custemSplitString("@", s);
            if(num_strs.length >= 2) {
                String nums = num_strs[1];
                if(nums.contains(",")) {
                    for (Float num : toNumberArray(Float.class, custemSplitString(",", nums))) {
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
        }
        return null;
    }
    private static <T> String toString(T[] args) {
        if(args == null || args.length == 0) {
            return "null";
        }
        List<String> l = Lists.newArrayList();
        for(T i : args) {
            l.add(i.toString());
        }
        return toString(l.toArray(new String[0]));
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

    private static String[] custemSplitString(String arg, String separator){
        if(StringUtils.isEmpty(arg)) {
            return new String[] {"null"};
        }
        return arg.split("" + separator);
    }

    private static String toString(String[] args) {
        if(args == null || args.length == 0) {
            return "null";
        }
        StringJoiner j = new StringJoiner(",");
        for(String arg : args) {
            j.add(arg);
        }
        return j.toString();
    }
}
