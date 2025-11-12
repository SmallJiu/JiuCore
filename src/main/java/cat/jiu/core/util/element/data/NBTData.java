package cat.jiu.core.util.element.data;

import cat.jiu.core.api.IData;
import cat.jiu.core.util.ArrayUtils;
import cat.jiu.core.util.NBTUtils;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.internal.LazilyParsedNumber;
import com.google.gson.internal.LinkedTreeMap;
import net.minecraft.nbt.*;
import it.unimi.dsi.fastutil.io.FastBufferedOutputStream;
import it.unimi.dsi.fastutil.io.FastBufferedInputStream;

import java.io.*;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.file.Files;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class NBTData {
    public static final NullData NULL = new NullData();
    public static final MapData EMPTY_MAP = new MapData(null) {
        @Override public boolean isEmpty() {return true;}
        @Override public int size() {return 0;}
    };
    public static final ListData EMPTY_LIST = new ListData(null) {
        @Override public boolean isEmpty() {return true;}
        @Override public int size() {return 0;}
    };

    public static MapData readMap(String file, boolean compressed) throws Exception {
        return map(compressed ? NbtIo.readCompressed(new File(file)) : NbtIo.read(new File(file)));
    }
    public static MapData readMap(File file, boolean compressed) throws Exception {
        return map(compressed ? NbtIo.readCompressed(file) : NbtIo.read(file));
    }
    public static ListData readList(String file, boolean compressed) throws Exception {
        return readList(new File(file), compressed);
    }
    public static ListData readList(File file, boolean compressed) throws Exception {
        try (
                FileInputStream fileinputstream = new FileInputStream(file);
                DataInputStream input = new DataInputStream(compressed ? new FastBufferedInputStream(new GZIPInputStream(fileinputstream)) : fileinputstream)
        ) {
            NbtAccounter accounter = NbtAccounter.UNLIMITED;
            byte b0 = input.readByte();
            accounter.accountBytes(1); // Forge: Count everything!
            if (b0 != 0)  {
                accounter.readUTF(input.readUTF()); //Forge: Count this string.
                accounter.accountBytes(4); //Forge: 4 extra bytes for the object allocation.
                try {
                    Tag tag = TagTypes.getType(b0).load(input, 0, accounter);
                    if (tag instanceof CollectionTag) {
                        return list((CollectionTag<?>) tag);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return list();
    }

    public static MapData map() {
        return new MapData(null);
    }
    public static ListData list() {
        return new ListData(null);
    }
    public static MapData map(CompoundTag data){
        return new MapData(data);
    }
    public static ListData list(CollectionTag<? extends Tag> data){
        return new ListData(data);
    }
    public static PrimitiveData primitive(Tag tag) {
        return new PrimitiveData(tag);
    }
    public static IData<?> create(Tag tag) {
        if (tag instanceof CompoundTag) {
            return ((CompoundTag) tag).isEmpty() ? EMPTY_MAP : map((CompoundTag) tag);
        }
        if (tag instanceof CollectionTag<?>) {
            return ((CollectionTag<?>) tag).isEmpty() ? EMPTY_LIST : list((CollectionTag<?>) tag);
        }
        if (tag == null || tag instanceof EndTag) {
            return NBTData.NULL;
        }
        return primitive(tag);
    }

    public static int getType(Class<?> clazz) {
        if (clazz == Byte.class) return Tag.TAG_BYTE;
        if (clazz == Short.class) return Tag.TAG_SHORT;
        if (clazz == Integer.class) return Tag.TAG_INT;
        if (clazz == Long.class) return Tag.TAG_LONG;

        if (clazz == Float.class) return Tag.TAG_FLOAT;
        if (clazz == Double.class) return Tag.TAG_DOUBLE;

        if (clazz == String.class) return Tag.TAG_STRING;

        if (clazz == byte[].class) return Tag.TAG_BYTE_ARRAY;
        if (clazz == int[].class) return Tag.TAG_INT_ARRAY;
        if (clazz == long[].class) return Tag.TAG_LONG_ARRAY;

        if (Number.class.isAssignableFrom(clazz)) return Tag.TAG_ANY_NUMERIC;

        if (Map.class.isAssignableFrom(clazz)
        || IData.IMapData.class.isAssignableFrom(clazz)
        || JsonObject.class == clazz
        || CompoundTag.class == clazz)  {
            return Tag.TAG_COMPOUND;
        }

        if (List.class.isAssignableFrom(clazz)
        || IData.IListData.class.isAssignableFrom(clazz)
        || JsonArray.class == clazz
        || ListTag.class == clazz)  {
            return Tag.TAG_LIST;
        }
        return Tag.TAG_END;
    }

    public static class NullData implements IData.INullData<EndTag> {
        @Override
        public EndTag getData() {
            return EndTag.INSTANCE;
        }
    }

    public static class MapData implements IData.IMapData<CompoundTag> {
        public final CompoundTag data;
        public final Map<String, IData<?>> cache = new HashMap<>();
        public MapData(CompoundTag data) {
            this.data = data == null ? new LinkedCompoundTag() : data;
        }

        public static class LinkedCompoundTag extends CompoundTag {
            public LinkedCompoundTag() {
                super(new LinkedTreeMap<>());
            }
        }

        @Override
        public CompoundTag getData() {
            return this == EMPTY_MAP ? new LinkedCompoundTag() : this.data;
        }

        @Override
        public int size() {
            return this.getData().size();
        }

        @Override
        public MapData copy() {
            return map(this.getData().copy());
        }

        @Override
        public boolean writeToFile(File file, boolean compressed, String charset) throws Exception {
            file.getParentFile().mkdirs();
            file.createNewFile();
            if (compressed) {
                NbtIo.writeCompressed(this.getData(), file);
            }else {
                NbtIo.write(this.getData(), file);
            }
            return true;
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
        public boolean containsKey(String key) {
            return this != EMPTY_MAP && !this.isEmpty() && this.getData().contains(key);
        }

        @Override
        public void foreach(BiConsumer<String, IData<?>> consumer) {
            if (this.isEmpty()) {
                return;
            }
            this.getData().getAllKeys().forEach(k -> consumer.accept(k, this.getData(k, this.nullData())));
        }

        @Override
        public MapData newMap() {
            return new MapData(null);
        }

        @Override
        public ListData newList() {
            return new ListData(null);
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
            Tag element = this.getData().get(key);
            if (this.containsKey(key)) {
                if (element instanceof CompoundTag) {
                    IData.IMapData<?> map = map((CompoundTag) element);
                    this.cache.put(key, map);
                    return map;
                }else if (element instanceof CollectionTag) {
                    IData.IListData<?> list = list((CollectionTag<? extends Tag>) element);
                    this.cache.put(key, list);
                    return list;
                }else if(!(element instanceof EndTag)) {
                    IData.IPrimitiveData<?> primitive = primitive(element);
                    this.cache.put(key, primitive);
                    return primitive;
                }else {
                    return NULL;
                }
            }
            return failback;
        }

        @Override
        public IData.IListData<?> getList(String key, Class<?> typeClass, IData.IListData<?> failback) {
            return this.getList(key, NBTData.getType(typeClass), failback);
        }
        public IData.IListData<?> getList(String key, int type, IData.IListData<?> failback) {
            if (this.isEmpty()) {
                return failback;
            }
            if (this.cache.containsKey(key)) {
                return this.cache.get(key).getAsList();
            }
            if (this.containsKey(key)) {
                ListTag element = type == Tag.TAG_END ? (ListTag) this.getData().get(key) : this.getData().getList(key, type);
                if (!this.cache.containsKey(key)) {
                    ListData data = new ListData(element);
                    this.cache.put(key, data);
                    return data;
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
                CompoundTag element = this.getData().getCompound(key);
                if (!this.cache.containsKey(key)) {
                    MapData data = new MapData(element);
                    this.cache.put(key, data);
                    return data;
                }
            }
            return failback;
        }

        @Override
        public byte[] getByteArray(String key, byte[] failback) {
            if (this.isEmpty()) {
                return failback;
            }
            if (this.containsKey(key)) {
                return this.getData().getByteArray(key);
            }
            return failback;
        }

        @Override
        public int[] getIntArray(String key, int[] failback) {
            if (this.isEmpty()) {
                return failback;
            }
            if (this.containsKey(key)) {
                return this.getData().getIntArray(key);
            }
            return failback;
        }

        @Override
        public long[] getLongArray(String key, long[] failback) {
            if (this.isEmpty()) {
                return failback;
            }
            if (this.containsKey(key)) {
                return this.getData().getLongArray(key);
            }
            return failback;
        }

        @Override
        public BigInteger getBigInteger(String key, BigInteger failback) {
            if (this.isEmpty()) {
                return failback;
            }
            if (this.containsKey(key)) {
                Tag tag = this.getData().get(key);
                if (tag instanceof StringTag) {
                    return new BigInteger(tag.getAsString());
                }else if (tag instanceof NumericTag) {
                    return BigInteger.valueOf(((NumericTag) tag).getAsLong());
                }
            }
            return failback;
        }

        @Override
        public BigDecimal getBigDecimal(String key, BigDecimal failback) {
            if (this.isEmpty()) {
                return failback;
            }
            if (this.containsKey(key)) {
                Tag tag = this.getData().get(key);
                if (tag instanceof StringTag) {
                    return new  BigDecimal(tag.getAsString());
                }else if (tag instanceof NumericTag) {
                    return BigDecimal.valueOf(((NumericTag) tag).getAsDouble());
                }
            }
            return failback;
        }

        @Override
        public Number getNumber(String key, Number failback) {
            if (this.isEmpty()) {
                return failback;
            }
            if (this.containsKey(key)) {
                Tag tag = this.getData().get(key);
                if (tag instanceof NumericTag) {
                    return ((NumericTag) tag).getAsNumber();
                }
            }
            return failback;
        }

        @Override
        public boolean getBoolean(String key, boolean failback) {
            if (this.isEmpty()) {
                return failback;
            }
            if (this.containsKey(key)) {
                return this.getData().getBoolean(key);
            }
            return failback;
        }

        @Override
        public String getString(String key, String failback) {
            if (this.isEmpty()) {
                return failback;
            }
            if (this.containsKey(key)) {
                return this.getData().getString(key);
            }
            return failback;
        }



        protected void putData(String key, Tag value) {
            if (this != EMPTY_MAP) {
                this.getData().put(key, value);
            }
        }
        @Override
        public MapData putNull(String key) {
            this.getData().put(key, EndTag.INSTANCE);
            return this;
        }

        @Override
        public MapData putData(String key, IData.IMapData<?> data) {
            if (data.getData() instanceof Tag) {
                this.getData().put(key, ((Tag) data.getData()));
                this.cache.put(key, data);
            } else if (data.getData() instanceof JsonObject) {
                this.getData().put(key, NBTUtils.toNBT((JsonObject) data.getData()));
                this.cache.put(key, data);
            }
            return this;
        }

        @Override
        public MapData putData(String key, IData.IListData<?> data) {
            if (data.getData() instanceof Tag) {
                this.getData().put(key, ((Tag) data.getData()));
                this.cache.put(key, data);
            } else if (data.getData() instanceof JsonArray) {
                this.getData().put(key, NBTUtils.toNBT((JsonArray) data.getData()));
                this.cache.put(key, data);
            }
            return this;
        }

        @Override
        public MapData putData(String key, byte[] data) {
            this.getData().putByteArray(key, data);
            return this;
        }

        @Override
        public MapData putData(String key, int[] data) {
            this.getData().putIntArray(key, data);
            return this;
        }

        @Override
        public MapData putData(String key, long[] data) {
            this.getData().putLongArray(key, data);
            return this;
        }

        @Override
        public MapData putData(String key, Number data) {
            if (data instanceof Byte) {
                this.getData().put(key, ByteTag.valueOf(data.byteValue()));
            }else if (data instanceof Short) {
                this.getData().put(key, ShortTag.valueOf(data.shortValue()));
            }else if (data instanceof Integer) {
                this.getData().put(key, IntTag.valueOf(data.intValue()));
            }else if (data instanceof Long) {
                this.getData().put(key, LongTag.valueOf(data.longValue()));
            }else if (data instanceof Float) {
                this.getData().put(key, FloatTag.valueOf(data.floatValue()));
            }else if (data instanceof Double) {
                this.getData().put(key, DoubleTag.valueOf(data.doubleValue()));
            }else {
                String n = String.valueOf(data);
                if (n.contains(".")) {
                    this.getData().put(key, FloatTag.valueOf(data.floatValue()));
                }else {
                    this.getData().put(key, IntTag.valueOf(data.intValue()));
                }
            }
            return this;
        }

        @Override
        public MapData putData(String key, boolean data) {
            this.getData().putBoolean(key, data);
            return this;
        }

        @Override
        public MapData putData(String key, String data) {
            this.getData().putString(key, data);
            return this;
        }
    }
    public static class ListData implements IData.IListData<CollectionTag<? extends Tag>> {
        public final CollectionTag<? extends Tag> data;
        public final Map<Integer, IData<?>> cache = new HashMap<>();
        public ListData(CollectionTag<? extends Tag> data) {
            this.data = data == null ? new ListTag() : data;
        }

        @Override
        public CollectionTag<? extends Tag> getData() {
            return this == EMPTY_LIST ? new ListTag() : this.data;
        }
        @Override
        public int size() {
            return this.getData().size();
        }

        @Override
        public ListData copy() {
            return list((CollectionTag<?>) this.getData().copy());
        }

        @Override
        public boolean writeToFile(File file, boolean compressed, String charset) throws Exception {
            file.getParentFile().mkdirs();
            file.createNewFile();
            OutputStream output = Files.newOutputStream(file.toPath());
            if (compressed) {
                output = new FastBufferedOutputStream(new GZIPOutputStream(output));
            }
            NbtIo.writeUnnamedTag(this.getData(), new DataOutputStream(output));
            return true;
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
            return new MapData(null);
        }

        @Override
        public ListData newList() {
            return new ListData(null);
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
            Tag element = this.getData().get(index);
            if (this.containsKey(index)) {
                if (element instanceof CompoundTag) {
                    IData.IMapData<?> map = map((CompoundTag) element);
                    this.cache.put(index, map);
                    return map;
                }else if (element instanceof CollectionTag) {
                    IData.IListData<?> list = list((CollectionTag<? extends Tag>) element);
                    this.cache.put(index, list);
                    return list;
                }else if(!(element instanceof EndTag)) {
                    IData.IPrimitiveData<?> primitive = primitive(element);
                    this.cache.put(index, primitive);
                    return primitive;
                }else {
                    return NULL;
                }
            }
            return failback;
        }

        @Override
        public IData.IMapData<?> getMap(int index, IData.IMapData<?> failback) {
            if (this.isEmpty()) {
                return failback;
            }
            if (this.cache.containsKey(index)) {
                return this.cache.get(index).getAsMap();
            }
            if (this.getData() instanceof ListTag && this.containsKey(index)) {
                CompoundTag element = ((ListTag) this.getData()).getCompound(index);
                if (!this.cache.containsKey(index)) {
                    MapData data = new MapData(element);
                    this.cache.put(index, data);
                    return data;
                }
            }
            return failback;
        }

        @Override
        public IData.IListData<?> getList(int index, Class<?> typeClass, IData.IListData<?> failback) {
            if (this.isEmpty()) {
                return failback;
            }
            if (this.cache.containsKey(index)) {
                return this.cache.get(index).getAsList();
            }
            if (this.getData() instanceof ListTag && this.containsKey(index)) {
                ListTag element = ((ListTag) this.getData()).getList(index);
                if (!this.cache.containsKey(index)) {
                    ListData data = new ListData(element);
                    this.cache.put(index, data);
                    return data;
                }
            }
            return failback;
        }

        @Override
        public byte[] getByteArray(int index, byte[] failback) {
            if (this.isEmpty()) {
                return failback;
            }
            if (this.containsKey(index)) {
                Tag tag = this.getData().get(index);
                if (tag instanceof ByteArrayTag) {
                    return ((ByteArrayTag) tag).getAsByteArray();
                }
            }
            return failback;
        }
        @Override
        public int[] getIntArray(int index, int[] failback) {
            if (this.isEmpty()) {
                return failback;
            }
            if (this.containsKey(index)) {
                Tag tag = this.getData().get(index);
                if (tag instanceof IntArrayTag) {
                    return ((IntArrayTag) tag).getAsIntArray();
                }
            }
            return failback;
        }
        @Override
        public long[] getLongArray(int index, long[] failback) {
            if (this.isEmpty()) {
                return failback;
            }
            if (this.containsKey(index)) {
                Tag tag = this.getData().get(index);
                if (tag instanceof LongArrayTag) {
                    return ((LongArrayTag) tag).getAsLongArray();
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
                Tag tag = this.getData().get(index);
                if (tag instanceof NumericTag) {
                    return BigInteger.valueOf(((NumericTag) tag).getAsLong());
                }else if (tag instanceof StringTag) {
                    return new BigInteger(tag.getAsString());
                }
            }
            return failback;
        }

        @Override
        public BigDecimal getBigDecimal(int index, BigDecimal failback) {
            if (this.isEmpty()) {
                return failback;
            }
            if (this.containsKey(index)) {
                Tag tag = this.getData().get(index);
                if (tag instanceof NumericTag) {
                    return BigDecimal.valueOf(((NumericTag) tag).getAsDouble());
                }else if (tag instanceof StringTag) {
                    return new BigDecimal(tag.getAsString());
                }
            }
            return failback;
        }

        @Override
        public Number getNumber(int index, Number failback) {
            if (this.isEmpty()) {
                return failback;
            }
            if (this.containsKey(index)) {
                Tag tag = this.getData().get(index);
                if (tag instanceof NumericTag) {
                    return ((NumericTag) tag).getAsNumber();
                }else if (tag instanceof StringTag) {
                    return new LazilyParsedNumber(tag.getAsString());
                }
            }
            return failback;
        }

        @Override
        public boolean getBoolean(int index, boolean failback) {
            if (this.isEmpty()) {
                return failback;
            }
            Tag tag = this.getData().get(index);
            if (tag instanceof ByteTag) {
                return ((ByteTag) tag).getAsByte() != 0;
            }
            return failback;
        }

        @Override
        public String getString(int index, String failback) {
            if (this.isEmpty()) {
                return failback;
            }
            Tag tag = this.getData().get(index);
            if (tag instanceof StringTag) {
                return tag.getAsString();
            }
            return failback;
        }


        protected void add(Tag tag) {
            if (this != EMPTY_LIST && this.getData() instanceof ListTag) {
                ((ListTag)this.getData()).add(tag);
            }
        }

        @Override
        public ListData putNull() {
            this.add(EndTag.INSTANCE);
            return this;
        }

        @Override
        public ListData putData(IData.IMapData<?> data) {
            if (data.getData() instanceof Tag) {
                int index = this.size();
                this.add((Tag)data.getData());
                this.cache.put(index, data);
            } else if (data.getData() instanceof JsonObject) {
                int index = this.size();
                this.add(NBTUtils.toNBT((JsonObject) data.getData()));
                this.cache.put(index, data);
            }
            return this;
        }

        @Override
        public ListData putData(IData.IListData<?> data) {
            if (data.getData() instanceof Tag) {
                int index = this.size();
                this.add((Tag)data.getData());
                this.cache.put(index, data);
            } else if (data.getData() instanceof JsonArray) {
                int index = this.size();
                this.add(NBTUtils.toNBT((JsonArray) data.getData()));
                this.cache.put(index, data);
            }
            return this;
        }

        @Override
        public ListData putData(byte[] data) {
            this.add(new ByteArrayTag(data));
            return this;
        }
        @Override
        public ListData putData(int[] data) {
            this.add(new IntArrayTag(data));
            return this;
        }
        @Override
        public ListData putData(long[] data) {
            this.add(new LongArrayTag(data));
            return this;
        }

        @Override
        public ListData putData(Number data) {
            if (data instanceof Byte) {
                this.add(ByteTag.valueOf(data.byteValue()));
            }else if (data instanceof Short) {
                this.add(ShortTag.valueOf(data.shortValue()));
            }else if (data instanceof Integer) {
                this.add(IntTag.valueOf(data.intValue()));
            }else if (data instanceof Long) {
                this.add(LongTag.valueOf(data.longValue()));
            }else if (data instanceof Float) {
                this.add(FloatTag.valueOf(data.floatValue()));
            }else if (data instanceof Double) {
                this.add(DoubleTag.valueOf(data.doubleValue()));
            }else {
                String n = String.valueOf(data);
                if (n.contains(".")) {
                    this.add(FloatTag.valueOf(data.floatValue()));
                }else {
                    this.add(IntTag.valueOf(data.intValue()));
                }
            }
            return this;
        }

        @Override
        public ListData putData(boolean data) {
            this.add(ByteTag.valueOf(data));
            return this;
        }

        @Override
        public ListData putData(String data) {
            this.add(StringTag.valueOf(data));
            return this;
        }
    }
    public static class PrimitiveData implements IData.IPrimitiveData<Tag> {
        public Tag data;

        public PrimitiveData() {
            this(StringTag.valueOf("0"));
        }
        public PrimitiveData(Tag data) {
            this.data = data;
        }

        @Override
        public Tag getData() {
            return this.data;
        }

        @Override
        public PrimitiveData setData(IListData<?> data) {
            this.data = new ListTag();
            data.transfer(list((CollectionTag<?>) this.getData()));
            return this;
        }

        @Override
        public PrimitiveData setData(String data) {
            this.data = StringTag.valueOf(data);
            return this;
        }

        @Override
        public PrimitiveData setData(boolean data) {
            this.data = ByteTag.valueOf(data);
            return this;
        }

        @Override
        public PrimitiveData setData(Number data) {
            if (data instanceof Byte) {
                this.data = ByteTag.valueOf(data.byteValue());
            }else if (data instanceof Short) {
                this.data = ShortTag.valueOf(data.shortValue());
            }else if (data instanceof Integer) {
                this.data = IntTag.valueOf(data.intValue());
            }else if (data instanceof Long) {
                this.data = LongTag.valueOf(data.longValue());
            }else if (data instanceof Float) {
                this.data = FloatTag.valueOf(data.floatValue());
            }else if (data instanceof Double) {
                this.data = DoubleTag.valueOf(data.doubleValue());
            }else {
                String n = String.valueOf(data);
                if (n.contains(".")) {
                    this.data = FloatTag.valueOf(data.floatValue());
                }else {
                    this.data = IntTag.valueOf(data.intValue());
                }
            }
            return this;
        }
        @Override
        public PrimitiveData setData(Object[] data) {
            this.data = new ListTag();
            if (data.length > 0) {
                if (data[0] instanceof Tag) {
                    for (Object t : data) {
                        ((ListTag)this.getData()).add((Tag) t);
                    }
                } else if (data[0] instanceof String) {
                    for (Object t : data) {
                        ((ListTag)this.getData()).add(StringTag.valueOf(String.valueOf(t)));
                    }
                }else if (data[0] instanceof Boolean) {
                    for (Object t : data) {
                        ((ListTag)this.getData()).add(ByteTag.valueOf((Boolean) t));
                    }
                } else if (data[0] instanceof Number) {
                    Number n = (Number)data[0];
                    if (n instanceof Byte) {
                        for (Object t : data) {
                            ((ListTag)this.getData()).add(ByteTag.valueOf((Byte) t));
                        }
                    }else if (n instanceof Short) {
                        for (Object t : data) {
                            ((ListTag)this.getData()).add(ShortTag.valueOf((Short) t));
                        }
                    }else if (n instanceof Integer) {
                        for (Object t : data) {
                            ((ListTag)this.getData()).add(IntTag.valueOf((Integer) t));
                        }
                    }else if (n instanceof Long) {
                        for (Object t : data) {
                            ((ListTag)this.getData()).add(LongTag.valueOf((Long) t));
                        }
                    }else if (n instanceof Float) {
                        for (Object t : data) {
                            ((ListTag)this.getData()).add(FloatTag.valueOf((Float) t));
                        }
                    }else if (n instanceof Double) {
                        for (Object t : data) {
                            ((ListTag)this.getData()).add(DoubleTag.valueOf((Double) t));
                        }
                    }
                }else {
                    for (Object t : data) {
                        ((ListTag)this.getData()).add(StringTag.valueOf(String.valueOf(t)));
                    }
                }
            }
            return this;
        }

        @Override
        public IData<?> copy() {
            if (this.getData() instanceof CompoundTag) {
                return map((CompoundTag)this.getData().copy());
            }
            if (this.getData() instanceof ListTag) {
                return list((ListTag)this.getData().copy());
            }
            return this.getData() instanceof EndTag ? NULL : primitive(this.getData().copy());
        }

        @Override
        public byte[] getAsByteArray() {
            if (this.getData() instanceof ByteArrayTag) {
                return ((ByteArrayTag) this.getData()).getAsByteArray();
            }
            return ArrayUtils.EMPTY_BYTE_ARRAY;
        }

        @Override
        public short[] getAsShortArray() {
            if (this.getData() instanceof ListTag) {
                return ArrayUtils.toArray(ArrayUtils.asArray((ListTag) this.getData(), Short[]::new, tag -> tag instanceof ShortTag ? ((ShortTag) tag).getAsShort() : 0, ArrayUtils.EMPTY_SHORT_ARRAY_));
            }
            return ArrayUtils.EMPTY_SHORT_ARRAY;
        }

        @Override
        public int[] getAsIntArray() {
            if (this.getData() instanceof IntArrayTag) {
                return ((IntArrayTag) this.getData()).getAsIntArray();
            }
            return ArrayUtils.EMPTY_INT_ARRAY;
        }

        @Override
        public long[] getAsLongArray() {
            if (this.getData() instanceof LongArrayTag) {
                return ((LongArrayTag) this.getData()).getAsLongArray();
            }
            return ArrayUtils.EMPTY_LONG_ARRAY;
        }

        @Override
        public float[] getAsFloatArray() {
            if (this.getData() instanceof ListTag) {
                return ArrayUtils.toArray(ArrayUtils.asArray((ListTag) this.getData(), Float[]::new, tag -> tag instanceof FloatTag ? ((FloatTag) tag).getAsFloat() : 0, ArrayUtils.EMPTY_FLOAT_ARRAY_));
            }
            return ArrayUtils.EMPTY_FLOAT_ARRAY;
        }

        @Override
        public double[] getAsDoubleArray() {
            if (this.getData() instanceof ListTag) {
                return ArrayUtils.toArray(ArrayUtils.asArray((ListTag) this.getData(), Double[]::new, tag -> tag instanceof DoubleTag ? ((DoubleTag) tag).getAsDouble() : 0, ArrayUtils.EMPTY_DOUBLE_ARRAY_));
            }
            return ArrayUtils.EMPTY_DOUBLE_ARRAY;
        }

        @Override
        public BigInteger[] getAsBigIntegerArray() {
            if (this.getData() instanceof ListTag) {
                return ArrayUtils.asArray((ListTag) this.getData(), BigInteger[]::new, tag -> {
                    if (tag instanceof NumericTag) {
                        return BigInteger.valueOf(((NumericTag) tag).getAsLong());
                    }else if (tag instanceof StringTag) {
                        return new BigInteger(tag.getAsString());
                    }
                    return BigInteger.ZERO;
                }, ArrayUtils.EMPTY_BIG_INTEGER_ARRAY);
            }
            return ArrayUtils.EMPTY_BIG_INTEGER_ARRAY;
        }

        @Override
        public BigDecimal[] getAsBigDecimalArray() {
            if (this.getData() instanceof ListTag) {
                return ArrayUtils.asArray((ListTag) this.getData(), BigDecimal[]::new, tag -> {
                    if (tag instanceof NumericTag) {
                        return BigDecimal.valueOf(((NumericTag) tag).getAsDouble());
                    }else if (tag instanceof StringTag) {
                        return new BigDecimal(tag.getAsString());
                    }
                    return BigDecimal.ZERO;
                }, ArrayUtils.EMPTY_BIG_DECIMAL_ARRAY);
            }
            return ArrayUtils.EMPTY_BIG_DECIMAL_ARRAY;
        }

        @Override
        public Number getAsNumber() {
            if (this.getData() instanceof NumericTag) {
                return ((NumericTag) this.getData()).getAsNumber();
            }
            if (this.isString()) {
                return new LazilyParsedNumber(this.getAsString());
            }
            return 0;
        }

        @Override
        public Number[] getAsNumberArray() {
            if (this.getData() instanceof ListTag) {
                return ArrayUtils.asArray((ListTag) this.getData(), Number[]::new, tag -> tag instanceof NumericTag ? ((NumericTag) tag).getAsNumber() : 0, ArrayUtils.EMPTY_NUMBER_ARRAY);
            }
            return ArrayUtils.EMPTY_NUMBER_ARRAY;
        }

        @Override
        public boolean isNumber() {
            boolean isStringNumber = false;
            if (this.isString()) {
                try {
                    new BigDecimal(this.getAsString());
                    isStringNumber = true;
                } catch (Exception ignored) {

                }
            }
            return this.getData() instanceof NumericTag || isStringNumber;
        }

        @Override
        public boolean isBoolean() {
            if (this.getData() instanceof ByteTag) {
                byte value = ((ByteTag) this.getData()).getAsByte();
                return value == 0 || value == 1;
            }
            return false;
        }

        @Override
        public boolean getAsBoolean() {
            if (this.getData() instanceof ByteTag) {
                return ((ByteTag) this.getData()).getAsByte() != 0;
            }else if (this.getData() instanceof StringTag) {
                return this.getAsNumber().byteValue() != 0;
            }
            return Boolean.parseBoolean(this.getAsString());
        }

        @Override
        public boolean[] getAsBooleanArray() {
            if (this.getData() instanceof ListTag) {
                return ArrayUtils.toArray(ArrayUtils.asArray((ListTag) this.getData(), Boolean[]::new, tag -> tag instanceof ByteTag && ((ByteTag) tag).getAsByte() == 1, ArrayUtils.EMPTY_BOOLEAN_ARRAY_));
            }
            return ArrayUtils.EMPTY_BOOLEAN_ARRAY;
        }

        @Override
        public boolean isString() {
            return this.getData() instanceof StringTag;
        }

        @Override
        public String getAsString() {
            if (this.isString()) {
                return this.getData().getAsString();
            }else if (this.isNumber()) {
                return this.getAsNumber().toString();
            } else if (this.isBoolean()) {
                return ((Boolean)this.getAsBoolean()).toString();
            }else if (this.getData() instanceof ListTag) {
                StringJoiner joiner = new StringJoiner(", ", "[", "]");
                ((ListTag) this.getData()).forEach(tag -> joiner.add(tag.getAsString()));
                return joiner.toString();
            }
            return String.valueOf(this.getData());
        }

        @Override
        public String[] getAsStringArray() {
            if (this.getData() instanceof ListTag) {
                return ArrayUtils.asArray((ListTag) this.getData(), String[]::new, Tag::getAsString, ArrayUtils.EMPTY_STRING_ARRAY);
            }
            return ArrayUtils.EMPTY_STRING_ARRAY;
        }

        @Override
        public IListData<?> getAsArray() {
            if (this.isArray()) {
                return list((CollectionTag<?>) this.data);
            }
            return EMPTY_LIST;
        }

        @Override
        public boolean isArray() {
            return this.getData() instanceof CollectionTag;
        }
    }
}
