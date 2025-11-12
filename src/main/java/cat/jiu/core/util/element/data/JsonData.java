package cat.jiu.core.util.element.data;

import cat.jiu.core.api.IData;
import cat.jiu.core.util.ArrayUtils;
import cat.jiu.core.util.JsonUtils;
import cat.jiu.core.util.NBTUtils;
import com.google.gson.*;
import net.minecraft.nbt.Tag;

import java.io.File;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

public class JsonData {
    public static final NullData NULL = new NullData();
    public static final MapData EMPTY_MAP = new MapData(null){
        @Override public boolean isEmpty() {return true;}
        @Override public int size() {return 0;}
    };
    public static final ListData EMPTY_LIST = new ListData(null){
        @Override public boolean isEmpty() {return true;}
        @Override public int size() {return 0;}
    };

    public static MapData readMap(File file, String charset) throws Exception {
        return map(JsonUtils.parseThrow(file, charset));
    }
    public static ListData readList(File file, String charset) throws Exception {
        return list(JsonUtils.parseThrow(file, charset));
    }
    public static MapData readMap(String file, String charset) throws Exception {
        return map(JsonUtils.parseThrow(file, charset));
    }
    public static ListData readList(String file, String charset) throws Exception {
        return list(JsonUtils.parseThrow(file, charset));
    }

    public static MapData map() {
        return new MapData(null);
    }
    public static ListData list() {
        return new ListData(null);
    }
    public static MapData map(JsonObject data) {
        return new MapData(data);
    }
    public static ListData list(JsonArray data) {
        return new ListData(data);
    }
    public static PrimitiveData primitive(JsonElement data) {
        return new PrimitiveData(data);
    }
    public static IData<?> create(JsonElement data) {
        if (data instanceof JsonObject) {
            return data.getAsJsonObject().size() == 0 ? EMPTY_MAP : map((JsonObject) data);
        }
        if (data instanceof JsonArray) {
            return data.getAsJsonArray().isEmpty() ? EMPTY_LIST : list((JsonArray) data);
        }
        if (data == null || data instanceof JsonNull) {
            return JsonData.NULL;
        }
        return primitive(data);
    }

    public static class NullData implements IData.INullData<JsonNull> {
        @Override
        public JsonNull getData() {
            return JsonNull.INSTANCE;
        }
    }

    public static class MapData implements IData.IMapData<JsonObject> {
        public final JsonObject data;
        public final Map<String, IData<?>> cache = new HashMap<>();
        public MapData(JsonObject data) {
            this.data = data == null ? new JsonObject() : data;
        }
        
        @Override
        public JsonObject getData() {
            return this == EMPTY_MAP ? new JsonObject() : this.data;
        }

        @Override
        public boolean containsKey(String key) {
            return !this.isEmpty() && this.getData().has(key);
        }
        
        @Override
        public int size() {
            return this.getData().size();
        }

        @Override
        public MapData copy() {
            return map(this.getData().deepCopy());
        }

        @Override
        public boolean writeToFile(File file, boolean compressed, String charset) throws Exception {
            file.getParentFile().mkdirs();
            file.createNewFile();
            return JsonUtils.toJsonFileThrow(file, this.getData(), compressed, charset);
        }

        @Override
        public IMapData<?> emptyMap() {
            return EMPTY_MAP;
        }

        @Override
        public IListData<?> emptyList() {
            return EMPTY_LIST;
        }
        
        @Override
        public void foreach(BiConsumer<String, IData<?>> consumer) {
            if (this.isEmpty()) {
                return;
            }
            this.getData().keySet().forEach(k -> consumer.accept(k, this.getData(k, this.nullData())));
        }

        @Override
        public MapData newMap() {
            return map();
        }

        @Override
        public ListData newList() {
            return list();
        }

        @Override
        public PrimitiveData newPrimitive() {
            return new PrimitiveData();
        }

        @Override
        public NullData nullData() {
            return NULL;
        }

        @Override
        public Object get(String key, Object failback) {
            if (this.isEmpty()) {
                return failback;
            }
            if (this.containsKey(key)) {
                return this.getData().get(key);
            }
            return failback;
        }

        @Override
        public IData<?> getData(String key, IData<?> failback) {
            if (this.isEmpty()) {
                return failback;
            }
            if (this.cache.containsKey(key)) {
                return this.cache.get(key);
            }
            if (this.containsKey(key)) {
                JsonElement element = this.getData().get(key);
                if (element.isJsonObject()) {
                    IData.IMapData<?> map = map(element.getAsJsonObject());
                    this.cache.put(key, map);
                    return map;
                }else if (element.isJsonArray()) {
                    IData.IListData<?> list = list(element.getAsJsonArray());
                    this.cache.put(key, list);
                    return list;
                }else if (element.isJsonPrimitive()){
                    IData.IPrimitiveData<?> primitive = primitive(element.getAsJsonPrimitive());
                    this.cache.put(key, primitive);
                    return primitive;
                }else {
                    return NULL;
                }
            }
            return failback;
        }

        @Override
        public IData.IMapData<?> getMap(String key, IData.IMapData<?> failback) {
            if (this.isEmpty()) {
                return failback;
            }
            if (this.cache.containsKey(key)) {
                return this.cache.get(key).getAsMap();
            }
            if (this.containsKey(key)) {
                JsonElement element = this.getData().get(key);
                if (element instanceof JsonObject) {
                    if (!this.cache.containsKey(key)) {
                        MapData data = map(element.getAsJsonObject());
                        this.cache.put(key, data);
                        return data;
                    }
                }
            }
            return failback;
        }

        @Override
        public IData.IListData<?> getList(String key, Class<?> typeClass, IData.IListData<?> failback) {
            if (this.isEmpty()) {
                return failback;
            }
            if (this.cache.containsKey(key)) {
                return this.cache.get(key).getAsList();
            }
            if (this.containsKey(key)) {
                JsonElement element = this.getData().get(key);
                if (element instanceof JsonArray) {
                    if (!this.cache.containsKey(key)) {
                        ListData data = list(element.getAsJsonArray());
                        this.cache.put(key, data);
                        return data;
                    }
                }
            }
            return failback;
        }

        @Override
        public BigInteger getBigInteger(String key, BigInteger failback) {
            if (this.isEmpty()) {
                return failback;
            }
            if (this.containsKey(key)) {
                return this.getData().get(key).getAsBigInteger();
            }
            return failback;
        }

        @Override
        public BigDecimal getBigDecimal(String key, BigDecimal failback) {
            if (this.isEmpty()) {
                return failback;
            }
            if (this.containsKey(key)) {
                return this.getData().get(key).getAsBigDecimal();
            }
            return failback;
        }

        @Override
        public Number getNumber(String key, Number failback) {
            if (this.isEmpty()) {
                return failback;
            }
            if (this.containsKey(key)) {
                JsonPrimitive primitive = this.getData().get(key).getAsJsonPrimitive();
                if (primitive.isBoolean()) {
                    return primitive.getAsBoolean() ? 1 : 0;
                }
                return primitive.getAsNumber();
            }
            return failback;
        }

        @Override
        public boolean getBoolean(String key, boolean failback) {
            if (this.isEmpty()) {
                return failback;
            }
            if (this.containsKey(key)) {
                JsonPrimitive primitive = this.getData().get(key).getAsJsonPrimitive();
                if (primitive.isNumber()) {
                    return primitive.getAsInt() >= 1;
                }
                return primitive.getAsBoolean();
            }
            return failback;
        }

        @Override
        public String getString(String key, String failback) {
            if (this.isEmpty()) {
                return failback;
            }
            if (this.containsKey(key)) {
                return this.getData().get(key).getAsString();
            }
            return failback;
        }


        @Override
        public IMapData<JsonObject> putNull(String key) {
            this.getData().add(key, null);
            return this;
        }

        @Override
        public MapData putData(String key, IData.IMapData<?> data) {
            if (data.getData() instanceof JsonElement) {
                this.getData().add(key, (JsonElement) data.getData());
                this.cache.put(key, data);
            } else if (data.getData() instanceof Tag) {
                this.getData().add(key, NBTUtils.toJson((Tag) data.getData()));
                this.cache.put(key, data);
            }
            return this;
        }

        @Override
        public MapData putData(String key, IData.IListData<?> data) {
            if (data.getData() instanceof JsonElement) {
                this.getData().add(key, (JsonElement) data.getData());
                this.cache.put(key, data);
            } else if (data.getData() instanceof Tag) {
                this.getData().add(key, NBTUtils.toJson((Tag) data.getData()));
                this.cache.put(key, data);
            }
            return this;
        }

        @Override
        public MapData putData(String key, Number data) {
            this.getData().addProperty(key, data);
            return this;
        }

        @Override
        public MapData putData(String key, boolean data) {
            this.getData().addProperty(key, data);
            return this;
        }

        @Override
        public MapData putData(String key, String data) {
            this.getData().addProperty(key, data);
            return this;
        }
    }

    public static class ListData implements IData.IListData<JsonArray> {
        public final JsonArray data;
        public final Map<Integer, IData<?>> cache = new HashMap<>();
        public ListData(JsonArray data) {
            this.data = data == null ? new JsonArray() : data;
        }

        @Override
        public JsonArray getData() {
            return this == EMPTY_LIST ? new JsonArray() : this.data;
        }

        @Override
        public int size() {
            return this.getData().size();
        }

        @Override
        public ListData copy() {
            return list(this.getData().deepCopy());
        }

        @Override
        public boolean writeToFile(File file, boolean compressed, String charset) throws Exception {
            file.getParentFile().mkdirs();
            file.createNewFile();
            return JsonUtils.toJsonFileThrow(file, this.getData(), compressed, charset);
        }

        @Override
        public IMapData<?> emptyMap() {
            return EMPTY_MAP;
        }

        @Override
        public IListData<?> emptyList() {
            return EMPTY_LIST;
        }

        @Override
        public MapData newMap() {
            return map();
        }

        @Override
        public ListData newList() {
            return list();
        }

        @Override
        public PrimitiveData newPrimitive() {
            return new PrimitiveData();
        }

        @Override
        public NullData nullData() {
            return NULL;
        }

        @Override
        public Object get(int index, Object failback) {
            if (this.isEmpty()) {
                return failback;
            }
            if (this.containsKey(index)) {
                return this.getData().get(index);
            }
            return failback;
        }

        @Override
        public IData<?> getData(int index, IData<?> failback) {
            if (this.isEmpty()) {
                return failback;
            }
            if (this.cache.containsKey(index)) {
                return this.cache.get(index);
            }
            if (this.containsKey(index)) {
                JsonElement element = this.getData().get(index);
                if (element.isJsonObject()) {
                    IData.IMapData<?> map = map(element.getAsJsonObject());
                    this.cache.put(index, map);
                    return map;
                }else if (element.isJsonArray()) {
                    IData.IListData<?> list = list(element.getAsJsonArray());
                    this.cache.put(index, list);
                    return list;
                }else if (element.isJsonPrimitive()){
                    IData.IPrimitiveData<?> primitive = primitive(element.getAsJsonPrimitive());
                    this.cache.put(index, primitive);
                    return primitive;
                }else {
                    return NULL;
                }
            }
            return failback;
        }

        @Override
        public IData.IMapData<?> getMap(int key, IData.IMapData<?> failback) {
            if (this.isEmpty()) {
                return failback;
            }
            if (this.cache.containsKey(key)) {
                return this.cache.get(key).getAsMap();
            }
            if (this.containsKey(key)) {
                JsonElement element = this.getData().get(key);
                if (element instanceof JsonObject) {
                    if (!this.cache.containsKey(key)) {
                        MapData data = new MapData(element.getAsJsonObject());
                        this.cache.put(key, data);
                        return data;
                    }
                }
            }
            return failback;
        }

        @Override
        public IData.IListData<?> getList(int key, Class<?> typeClass, IData.IListData<?> failback) {
            if (this.isEmpty()) {
                return failback;
            }
            if (this.cache.containsKey(key)) {
                return this.cache.get(key).getAsList();
            }
            if (this.containsKey(key)) {
                JsonElement element = this.getData().get(key);
                if (element instanceof JsonArray) {
                    if (!this.cache.containsKey(key)) {
                        ListData data = new ListData(element.getAsJsonArray());
                        this.cache.put(key, data);
                        return data;
                    }
                }
            }
            return failback;
        }

        @Override
        public BigInteger getBigInteger(int index, BigInteger failback) {
            if (this.isEmpty()) {
                return failback;
            }
            if (this.containsKey(index)) {
                return this.getData().get(index).getAsBigInteger();
            }
            return failback;
        }

        @Override
        public BigDecimal getBigDecimal(int index, BigDecimal failback) {
            if (this.isEmpty()) {
                return failback;
            }
            if (this.containsKey(index)) {
                return this.getData().get(index).getAsBigDecimal();
            }
            return failback;
        }

        @Override
        public Number getNumber(int index, Number failback) {
            if (this.isEmpty()) {
                return failback;
            }
            JsonElement element = this.getData().get(index);
            return element instanceof JsonPrimitive ? element.getAsNumber() : failback;
        }

        @Override
        public boolean getBoolean(int index, boolean failback) {
            if (this.isEmpty()) {
                return failback;
            }
            JsonElement element = this.getData().get(index);
            return element instanceof JsonPrimitive ? element.getAsBoolean() : failback;
        }

        @Override
        public String getString(int index, String failback) {
            if (this.isEmpty()) {
                return failback;
            }
            JsonElement element = this.getData().get(index);
            return element instanceof JsonPrimitive ? element.getAsString() : failback;
        }



        @Override
        public IListData<JsonArray> putNull() {
            this.getData().add(JsonNull.INSTANCE);
            return this;
        }

        @Override
        public ListData putData(IData.IMapData<?> data) {
            if (data.getData() instanceof JsonElement) {
                int index = this.size();
                this.getData().add((JsonElement) data.getData());
                this.cache.put(index, data);
            } else if (data.getData() instanceof Tag) {
                int index = this.size();
                this.getData().add(NBTUtils.toJson((Tag) data.getData()));
                this.cache.put(index, data);
            }
            return this;
        }

        @Override
        public ListData putData(IData.IListData<?> data) {
            if (data.getData() instanceof JsonElement) {
                int index = this.size();
                this.getData().add((JsonElement) data.getData());
                this.cache.put(index, data);
            } else if (data.getData() instanceof Tag) {
                int index = this.size();
                this.getData().add(NBTUtils.toJson((Tag) data.getData()));
                this.cache.put(index, data);
            }
            return this;
        }

        @Override
        public ListData putData(Number data) {
            this.getData().add(data);
            return this;
        }

        @Override
        public ListData putData(boolean data) {
            this.getData().add(data);
            return this;
        }

        @Override
        public ListData putData(String data) {
            this.getData().add(data);
            return this;
        }
    }
    
    public static class PrimitiveData implements IData.IPrimitiveData<JsonElement> {
        public JsonElement data;

        public PrimitiveData() {
            this(new JsonPrimitive("0"));
        }
        public PrimitiveData(JsonElement data) {
            this.data = data;
        }

        @Override
        public JsonElement getData() {
            return this.data;
        }

        @Override
        public PrimitiveData setData(String data) {
            this.data = new JsonPrimitive(data);
            return this;
        }

        @Override
        public PrimitiveData setData(boolean data) {
            this.data = new JsonPrimitive(data);
            return this;
        }

        @Override
        public PrimitiveData setData(Number data) {
            this.data = new JsonPrimitive(data);
            return this;
        }

        @Override
        public PrimitiveData setData(IListData<?> data) {
            this.data = new JsonArray();
            data.transfer(list(this.getData().getAsJsonArray()));
            return this;
        }

        @Override
        public PrimitiveData setData(Object[] data) {
            this.data = new JsonArray();
            for (Object t : data) {
                if (t instanceof String) {
                    this.getData().getAsJsonArray().add((String) t);
                }else if (t instanceof Boolean) {
                    this.getData().getAsJsonArray().add((Boolean) t);
                }else if (t instanceof Number) {
                    this.getData().getAsJsonArray().add((Number) t);
                }else {
                    this.getData().getAsJsonArray().add(String.valueOf(t));
                }
            }
            return this;
        }

        @Override
        public PrimitiveData copy() {
            return primitive(this.getData().deepCopy());
        }

        @Override
        public byte[] getAsByteArray() {
            if (this.getData() instanceof JsonArray) {
                return ArrayUtils.toArray(ArrayUtils.asArray(this.getData().getAsJsonArray(), Byte[]::new, JsonElement::getAsByte, ArrayUtils.EMPTY_BYTE_ARRAY_));
            }
            return ArrayUtils.EMPTY_BYTE_ARRAY;
        }

        @Override
        public short[] getAsShortArray() {
            if (this.getData() instanceof JsonArray) {
                return ArrayUtils.toArray(ArrayUtils.asArray(this.getData().getAsJsonArray(), Short[]::new, JsonElement::getAsShort, ArrayUtils.EMPTY_SHORT_ARRAY_));
            }
            return ArrayUtils.EMPTY_SHORT_ARRAY;
        }

        @Override
        public int[] getAsIntArray() {
            if (this.getData() instanceof JsonArray) {
                return ArrayUtils.toArray(ArrayUtils.asArray(this.getData().getAsJsonArray(), Integer[]::new, JsonElement::getAsInt, ArrayUtils.EMPTY_INT_ARRAY_));
            }
            return ArrayUtils.EMPTY_INT_ARRAY;
        }

        @Override
        public long[] getAsLongArray() {
            if (this.getData() instanceof JsonArray) {
                return ArrayUtils.toArray(ArrayUtils.asArray(this.getData().getAsJsonArray(), Long[]::new, JsonElement::getAsLong, ArrayUtils.EMPTY_LONG_ARRAY_));
            }
            return ArrayUtils.EMPTY_LONG_ARRAY;
        }

        @Override
        public float[] getAsFloatArray() {
            if (this.getData() instanceof JsonArray) {
                return ArrayUtils.toArray(ArrayUtils.asArray(this.getData().getAsJsonArray(), Float[]::new, JsonElement::getAsFloat, ArrayUtils.EMPTY_FLOAT_ARRAY_));
            }
            return ArrayUtils.EMPTY_FLOAT_ARRAY;
        }

        @Override
        public double[] getAsDoubleArray() {
            if (this.getData() instanceof JsonArray) {
                return ArrayUtils.toArray(ArrayUtils.asArray(this.getData().getAsJsonArray(), Double[]::new, JsonElement::getAsDouble, ArrayUtils.EMPTY_DOUBLE_ARRAY_));
            }
            return ArrayUtils.EMPTY_DOUBLE_ARRAY;
        }

        @Override
        public BigInteger[] getAsBigIntegerArray() {
            if (this.getData() instanceof JsonArray) {
                return ArrayUtils.asArray(this.getData().getAsJsonArray(), BigInteger[]::new, JsonElement::getAsBigInteger, ArrayUtils.EMPTY_BIG_INTEGER_ARRAY);
            }
            return ArrayUtils.EMPTY_BIG_INTEGER_ARRAY;
        }

        @Override
        public BigDecimal[] getAsBigDecimalArray() {
            if (this.getData() instanceof JsonArray) {
                return ArrayUtils.asArray(this.getData().getAsJsonArray(), BigDecimal[]::new, JsonElement::getAsBigDecimal, ArrayUtils.EMPTY_BIG_DECIMAL_ARRAY);
            }
            return ArrayUtils.EMPTY_BIG_DECIMAL_ARRAY;
        }

        @Override
        public Number getAsNumber() {
            if (this.getData() instanceof JsonPrimitive) {
                return this.getData().getAsNumber();
            }
            return 0;
        }
        @Override
        public boolean isNumber() {
            if (this.getData() instanceof JsonPrimitive) {
                if (this.getData().getAsJsonPrimitive().isNumber()) {
                    return true;
                }else {
                    try {
                        new BigDecimal(this.getAsString());
                        return true;
                    } catch (Exception ignored) {
                    }
                }
            }
            return false;
        }

        @Override
        public Number[] getAsNumberArray() {
            if (this.getData() instanceof JsonArray) {
                return ArrayUtils.asArray(this.getData().getAsJsonArray(), Number[]::new, JsonElement::getAsNumber, ArrayUtils.EMPTY_NUMBER_ARRAY);
            }
            return ArrayUtils.EMPTY_NUMBER_ARRAY;
        }

        @Override
        public boolean getAsBoolean() {
            if (this.getData() instanceof JsonPrimitive) {
                return this.getData().getAsBoolean();
            }
            return false;
        }

        @Override
        public boolean isBoolean() {
            if (this.getData() instanceof JsonPrimitive) {
                return this.getData().getAsJsonPrimitive().isBoolean();
            }
            return false;
        }

        @Override
        public boolean[] getAsBooleanArray() {
            if (this.getData() instanceof JsonArray) {
                return ArrayUtils.toArray(ArrayUtils.asArray(this.getData().getAsJsonArray(), Boolean[]::new, JsonElement::getAsBoolean, ArrayUtils.EMPTY_BOOLEAN_ARRAY_));
            }
            return ArrayUtils.EMPTY_BOOLEAN_ARRAY;
        }

        @Override
        public String getAsString() {
            if (this.getData() instanceof JsonPrimitive) {
                return this.getData().getAsString();
            }
            return "";
        }

        @Override
        public boolean isString() {
            if (this.getData() instanceof JsonPrimitive) {
                return this.getData().getAsJsonPrimitive().isString();
            }
            return false;
        }

        @Override
        public String[] getAsStringArray() {
            if (this.getData() instanceof JsonArray) {
                return ArrayUtils.asArray(this.getData().getAsJsonArray(), String[]::new, JsonElement::getAsString, ArrayUtils.EMPTY_STRING_ARRAY);
            }
            return ArrayUtils.EMPTY_STRING_ARRAY;
        }

        @Override
        public IListData<?> getAsArray() {
            if (this.isArray()) {
                return list(this.getData().getAsJsonArray());
            }
            return EMPTY_LIST;
        }

        @Override
        public boolean isArray() {
            return this.getData() instanceof JsonArray;
        }
    }
}
