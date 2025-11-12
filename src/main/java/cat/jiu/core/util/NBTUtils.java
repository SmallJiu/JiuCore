package cat.jiu.core.util;

import com.google.gson.*;
import net.minecraft.nbt.*;
import net.minecraft.util.StringUtil;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Map;

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

    public static ListTag asList(double[] value) {
        ListTag list = new ListTag();
        for (double v : value) {
            list.add(DoubleTag.valueOf(v));
        }
        return list;
    }
    public static ListTag asList(float[] value) {
        ListTag list = new ListTag();
        for (float v : value) {
            list.add(FloatTag.valueOf(v));
        }
        return list;
    }
    public static ListTag asList(short[] value) {
        ListTag list = new ListTag();
        for (short v : value) {
            list.add(ShortTag.valueOf(v));
        }
        return list;
    }

    public static JsonElement toJson(Tag base) {
        if(base instanceof NumericTag) {
            return new JsonPrimitive(((NumericTag) base).getAsNumber());
        }else if(base instanceof StringTag) {
            return new JsonPrimitive(base.getAsString());
        }else if(base instanceof CompoundTag) {
            return toJson((CompoundTag) base);
        }else if(base instanceof CollectionTag) {
            return toJson((CollectionTag<?>) base);
        }
        return JsonNull.INSTANCE;
    }

    public static JsonArray toJson(CollectionTag<?> list) {
        JsonArray array = new JsonArray();
        for (Tag tag : list) {
            array.add(toJson(tag));
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
                        obj.getAsJsonObject("string").add(key, pri);
                    }else if(pri.isBoolean()) {
                        if(!obj.has("boolean")) {
                            obj.add("boolean", new JsonObject());
                        }
                        obj.getAsJsonObject("boolean").add(key, pri);
                    }
                }else if(e.isJsonObject()) {
                    if(!obj.has("tags")) {
                        obj.add("tags", new JsonObject());
                    }
                    obj.getAsJsonObject("tags").add(key, e);
                }else if(e.isJsonArray()) {
                    obj.add(key, e);
                }
            }
        }
        return obj;
    }

    public static JsonObject toNormalJson(JsonObject obj) {
        JsonObject object = new JsonObject();
        for(Map.Entry<String, JsonElement> objTags : obj.entrySet()) {
            String key = objTags.getKey();
            JsonElement value = objTags.getValue();
            if("string".equals(key)) {
                for(Map.Entry<String, JsonElement> entry : value.getAsJsonObject().entrySet()) {
                    object.addProperty(entry.getKey(), entry.getValue().getAsString());
                }
                continue;
            }
            if("boolean".equals(key)) {
                for(Map.Entry<String, JsonElement> entry : value.getAsJsonObject().entrySet()) {
                    object.addProperty(entry.getKey(), entry.getValue().getAsBoolean());
                }
                continue;
            }
            if("int".equals(key)) {
                for(Map.Entry<String, JsonElement> entry : value.getAsJsonObject().entrySet()) {
                    object.addProperty(entry.getKey(), entry.getValue().getAsInt());
                }
                continue;
            }
            if("long".equals(key)) {
                for(Map.Entry<String, JsonElement> entry : value.getAsJsonObject().entrySet()) {
                    object.addProperty(entry.getKey(), entry.getValue().getAsLong());
                }
                continue;
            }
            if("float".equals(key)) {
                for(Map.Entry<String, JsonElement> entry : value.getAsJsonObject().entrySet()) {
                    object.addProperty(entry.getKey(), entry.getValue().getAsFloat());
                }
                continue;
            }
            if("double".equals(key)) {
                for(Map.Entry<String, JsonElement> entry : value.getAsJsonObject().entrySet()) {
                    object.addProperty(entry.getKey(), entry.getValue().getAsDouble());
                }
                continue;
            }
            if("byte".equals(key)) {
                for(Map.Entry<String, JsonElement> entry : value.getAsJsonObject().entrySet()) {
                    object.addProperty(entry.getKey(), entry.getValue().getAsByte());
                }
                continue;
            }
            if("short".equals(key)) {
                for(Map.Entry<String, JsonElement> entry : value.getAsJsonObject().entrySet()) {
                    object.addProperty(entry.getKey(), entry.getValue().getAsShort());
                }
                continue;
            }
            if("int_array".equals(key)) {
                for(Map.Entry<String, JsonElement> entry : value.getAsJsonObject().entrySet()) {
                    object.add(entry.getKey(), entry.getValue().getAsJsonArray());
                }
                continue;
            }
            if("short_array".equals(key)) {
                for(Map.Entry<String, JsonElement> entry : value.getAsJsonObject().entrySet()) {
                    object.add(entry.getKey(), entry.getValue().getAsJsonArray());
                }
                continue;
            }
            if("byte_array".equals(key)) {
                for(Map.Entry<String, JsonElement> entry : value.getAsJsonObject().entrySet()) {
                    object.add(entry.getKey(), entry.getValue().getAsJsonArray());
                }
                continue;
            }
            if("double_array".equals(key)) {
                for(Map.Entry<String, JsonElement> entry : value.getAsJsonObject().entrySet()) {
                    object.add(entry.getKey(), entry.getValue().getAsJsonArray());
                }
                continue;
            }
            if("float_array".equals(key)) {
                for(Map.Entry<String, JsonElement> entry : value.getAsJsonObject().entrySet()) {
                    object.add(entry.getKey(), entry.getValue().getAsJsonArray());
                }
                continue;
            }
            if("long_array".equals(key)) {
                for(Map.Entry<String, JsonElement> entry : value.getAsJsonObject().entrySet()) {
                    object.add(entry.getKey(), entry.getValue().getAsJsonArray());
                }
                continue;
            }
            if("tags".equals(key)) {
                for(Map.Entry<String, JsonElement> tags : value.getAsJsonObject().entrySet()) {
                    object.add(tags.getKey(), toNormalJson(tags.getValue().getAsJsonObject()));
                }
                continue;
            }
            if(value.isJsonObject()) {
                object.add(key, toNormalJson(value.getAsJsonObject()));
                continue;
            }
            if(value.isJsonArray()) {
                object.add(key, value.getAsJsonArray());
            }
        }
        return object;
    }

    public static CompoundTag toNBT(JsonObject obj) {
        CompoundTag tag = new CompoundTag();
        for(Map.Entry<String, JsonElement> objTags : obj.entrySet()) {
            String key = objTags.getKey();
            JsonElement value = objTags.getValue();
            if("string".equals(key)) {
                for(Map.Entry<String, JsonElement> entry : value.getAsJsonObject().entrySet()) {
                    tag.putString(entry.getKey(), entry.getValue().getAsString());
                }
                continue;
            }
            if("boolean".equals(key)) {
                for(Map.Entry<String, JsonElement> entry : value.getAsJsonObject().entrySet()) {
                    tag.putBoolean(entry.getKey(), entry.getValue().getAsBoolean());
                }
                continue;
            }
            if("int".equals(key)) {
                for(Map.Entry<String, JsonElement> entry : value.getAsJsonObject().entrySet()) {
                    tag.putInt(entry.getKey(), entry.getValue().getAsInt());
                }
                continue;
            }
            if("long".equals(key)) {
                for(Map.Entry<String, JsonElement> entry : value.getAsJsonObject().entrySet()) {
                    tag.putLong(entry.getKey(), entry.getValue().getAsLong());
                }
                continue;
            }
            if("float".equals(key)) {
                for(Map.Entry<String, JsonElement> entry : value.getAsJsonObject().entrySet()) {
                    tag.putFloat(entry.getKey(), entry.getValue().getAsFloat());
                }
                continue;
            }
            if("double".equals(key)) {
                for(Map.Entry<String, JsonElement> entry : value.getAsJsonObject().entrySet()) {
                    tag.putDouble(entry.getKey(), entry.getValue().getAsDouble());
                }
                continue;
            }
            if("byte".equals(key)) {
                for(Map.Entry<String, JsonElement> entry : value.getAsJsonObject().entrySet()) {
                    tag.putByte(entry.getKey(), entry.getValue().getAsByte());
                }
                continue;
            }
            if("short".equals(key)) {
                for(Map.Entry<String, JsonElement> entry : value.getAsJsonObject().entrySet()) {
                    tag.putShort(entry.getKey(), entry.getValue().getAsShort());
                }
                continue;
            }
            if("int_array".equals(key)) {
                for(Map.Entry<String, JsonElement> entry : value.getAsJsonObject().entrySet()) {
                    tag.putIntArray(entry.getKey(), ArrayUtils.toIntArray(entry.getValue().getAsJsonArray()));
                }
                continue;
            }
            if("short_array".equals(key)) {
                for(Map.Entry<String, JsonElement> entry : value.getAsJsonObject().entrySet()) {
                    tag.put(entry.getKey(), asList(ArrayUtils.toShortArray(entry.getValue().getAsJsonArray())));
                }
                continue;
            }
            if("byte_array".equals(key)) {
                for(Map.Entry<String, JsonElement> entry : value.getAsJsonObject().entrySet()) {
                    tag.putByteArray(entry.getKey(), ArrayUtils.toByteArray(entry.getValue().getAsJsonArray()));
                }
                continue;
            }
            if("double_array".equals(key)) {
                for(Map.Entry<String, JsonElement> entry : value.getAsJsonObject().entrySet()) {
                    tag.put(entry.getKey(), asList(ArrayUtils.toDoubleArray(entry.getValue().getAsJsonArray())));
                }
                continue;
            }
            if("float_array".equals(key)) {
                for(Map.Entry<String, JsonElement> entry : value.getAsJsonObject().entrySet()) {
                    tag.put(entry.getKey(), asList(ArrayUtils.toFloatArray(entry.getValue().getAsJsonArray())));
                }
                continue;
            }
            if("long_array".equals(key)) {
                for(Map.Entry<String, JsonElement> entry : value.getAsJsonObject().entrySet()) {
                    tag.putLongArray(entry.getKey(), ArrayUtils.toLongArray(entry.getValue().getAsJsonArray()));
                }
                continue;
            }
            if("tags".equals(key)) {
                for(Map.Entry<String, JsonElement> tags : value.getAsJsonObject().entrySet()) {
                    tag.put(tags.getKey(), toNBT(tags.getValue().getAsJsonObject()));
                }
                continue;
            }
            if(value.isJsonObject()) {
                tag.put(key, toNBT(value.getAsJsonObject()));
                continue;
            }
            if(value.isJsonArray()) {
                tag.put(key, toNBT(value.getAsJsonArray()));
            }
        }
        return tag;
    }

    public static ListTag toNBT(JsonArray array) {
        ListTag list = new ListTag();
        for(int i = 0; i < array.size(); i++) {
            JsonElement array_element = array.get(i);
            if(array_element.isJsonObject()) {
                list.add(toNBT(array_element.getAsJsonObject()));
                continue;
            }
            if(array_element.isJsonArray()) {
                list.add(toNBT(array_element.getAsJsonArray()));
                continue;
            }
            if(array_element.isJsonPrimitive()) {
                JsonPrimitive pri = array_element.getAsJsonPrimitive();
                if(pri.isString()) {
                    list.add(StringTag.valueOf(pri.getAsString()));
                    continue;
                }
                if(pri.isBoolean()) {
                    list.add(ByteTag.valueOf(pri.getAsBoolean()));
                    continue;
                }
                if(pri.isNumber()) {
                    Number num = pri.getAsNumber();
                    if(num instanceof Integer) {
                        list.add(IntTag.valueOf((Integer) num));
                        continue;
                    }
                    if(num instanceof Double) {
                        list.add(DoubleTag.valueOf((Double) num));
                        continue;
                    }
                    if(num instanceof Byte) {
                        list.add(ByteTag.valueOf((Byte) num));
                        continue;
                    }
                    if(num instanceof Long) {
                        list.add(LongTag.valueOf((Long) num));
                        continue;
                    }
                    if(num instanceof Float) {
                        list.add(FloatTag.valueOf((Float) num));
                        continue;
                    }
                    if(num instanceof Short) {
                        list.add(ShortTag.valueOf((Short) num));
                    }
                }
            }
        }
        return list;
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
}
