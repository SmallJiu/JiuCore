package cat.jiu.core.api;

import cat.jiu.core.util.ArrayUtils;
import cat.jiu.core.util.Utils;
import cat.jiu.core.util.element.data.*;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import net.minecraft.nbt.CollectionTag;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.EndTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;

import java.io.File;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Collection;
import java.util.function.BiConsumer;
import java.util.function.Function;

@SuppressWarnings("unused")
public interface IData<T> {
    char NULL_CHAR = '\0';
    static IData<? extends Tag> create(Tag data) {
        if (data instanceof CompoundTag) {
            return NBTData.map((CompoundTag) data);
        }else if (data instanceof CollectionTag) {
            return NBTData.list((CollectionTag<? extends Tag>) data);
        }else if (!(data instanceof EndTag)){
            return NBTData.primitive(data);
        }
        return NBTData.NULL;
    }
    static IData<? extends JsonElement> create(JsonElement data) {
        if (data instanceof JsonObject) {
            return JsonData.map(data.getAsJsonObject());
        }else if (data instanceof JsonArray) {
            return JsonData.list(data.getAsJsonArray());
        }else if (data instanceof JsonPrimitive){
            return JsonData.primitive(data);
        }
        return JsonData.NULL;
    }

    T getData();
    default String asString() {
        return String.valueOf(this.getData());
    }

    default boolean isMap(){
        return this instanceof IMapData;
    }
    default IMapData<T> getAsMap() {
        if (isMap()){
            return (IMapData<T>) this;
        }else {
            throw new IllegalStateException("Not a map:" + this.asString());
        }
    }

    default boolean isList(){
        return this instanceof IListData;
    }
    default IListData<T> getAsList() {
        if (isList()){
            return (IListData<T>) this;
        }else  {
            throw new IllegalStateException("Not a list:" + this.asString());
        }
    }

    default boolean isPrimitive() {
        return this instanceof IPrimitiveData;
    }
    default IPrimitiveData<T> getAsPrimitive() {
        if (isPrimitive()){
            return (IPrimitiveData<T>) this;
        }else  {
            throw new IllegalStateException("Not a primitive:" + this.asString());
        }
    }

    default boolean isNull() {
        return this instanceof IData.INullData;
    }
    default IData.INullData<T> getAsNull() {
        if (isNull()){
            return (IData.INullData<T>) this;
        }else  {
            throw new IllegalStateException("Not a null:" + this.asString());
        }
    }

    default byte getAsByte(){
        if (this.isPrimitive()) {
            return this.getAsPrimitive().getAsByte();
        }
        throw new IllegalStateException("Not a primitive:" + this.asString());
    }
    default short getAsShort(){
        if (this.isPrimitive()) {
            return this.getAsPrimitive().getAsShort();
        }
        throw new IllegalStateException("Not a primitive:" + this.asString());
    }
    default int getAsInt(){
        if (this.isPrimitive()) {
            return this.getAsPrimitive().getAsInt();
        }
        throw new IllegalStateException("Not a primitive:" + this.asString());
    }
    default long getAsLong(){
        if (this.isPrimitive()) {
            return this.getAsPrimitive().getAsLong();
        }
        throw new IllegalStateException("Not a primitive:" + this.asString());
    }
    default float getAsFloat(){
        if (this.isPrimitive()) {
            return this.getAsPrimitive().getAsFloat();
        }
        throw new IllegalStateException("Not a primitive:" + this.asString());
    }
    default double getAsDouble(){
        if (this.isPrimitive()) {
            return this.getAsPrimitive().getAsDouble();
        }
        throw new IllegalStateException("Not a primitive:" + this.asString());
    }
    default Number getAsNumber() {
        if (this.isPrimitive()) {
            return this.getAsPrimitive().getAsNumber();
        }
        throw new IllegalStateException("Not a primitive:" + this.asString());
    }
    default char getAsChar(){
        if (this.isPrimitive()) {
            return this.getAsPrimitive().getAsChar();
        }
        throw new IllegalStateException("Not a primitive:" + this.asString());
    }
    default String getAsString(){
        if (this.isPrimitive()) {
            return this.getAsPrimitive().getAsString();
        }
        throw new IllegalStateException("Not a primitive:" + this.asString());
    }
    default boolean getAsBoolean() {
        if (this.isPrimitive()) {
            return this.getAsPrimitive().getAsBoolean();
        }
        throw new IllegalStateException("Not a primitive:" + this.asString());
    }
    default ResourceLocation getAsLocation() {
        if (this.isPrimitive()) {
            return this.getAsPrimitive().getAsLocation();
        }
        throw new IllegalStateException("Not a primitive:" + this.asString());
    }

    
    static <T> T[] getArray(IMapData<?> mapData, String key, Class<T> typeClass, Function<Integer, T[]> newArray, Lambdas.Function2<IListData<?>, Integer, T> elementGetter, T[] failback) {
        if (mapData.isEmpty()) {
            return failback;
        }
        if (mapData.containsKey(key)) {
            IData.IListData<?> list = mapData.getList(key, typeClass, mapData.emptyList());
            if (list.isEmpty()) {
                return failback;
            }
            T[] result = newArray.apply(list.size());
            for (int i = 0; i < list.size(); i++) {
                result[i] = elementGetter.apply(list, i);
            }
            return result;
        }
        return failback;
    }
    static <T> T[] getArray(IListData<?> mapData, int index, Class<T> typeClass, Function<Integer, T[]> newArray, Lambdas.Function2<IListData<?>, Integer, T> elementGetter, T[] failback) {
        if (mapData.isEmpty()) {
            return failback;
        }
        if (mapData.containsKey(index)) {
            IData.IListData<?> list = mapData.getList(index, typeClass, mapData.emptyList());
            if (list.isEmpty()) {
                return failback;
            }
            T[] result = newArray.apply(list.size());
            for (int i = 0; i < list.size(); i++) {
                result[i] = elementGetter.apply(list, i);
            }
            return result;
        }
        return failback;
    }

    interface IMapData<T> extends IData<T>, IMaker {
        IMapData<T> copy();

        boolean writeToFile(File file, boolean compressed, String charset) throws Exception;
        default <E> IMapData<E> transfer(IMapData<E> other) {
            this.foreach(other::putData);
            return other;
        }
        
        int size();
        default boolean isEmpty() {
            return this.size() == 0;
        }
        boolean containsKey(String key);
        void foreach(BiConsumer<String, IData<?>> consumer);

        Object get(String key, Object failback);
        default Object get(String key) {
            return this.get(key, null);
        }
        IData<?> getData(String key, IData<?> failback);
        
        IMapData<?> getMap(String key, IMapData<?> failback);
        default IMapData<?> getMap(String key) {
            return this.getMap(key, this.emptyMap());
        }
        default IMapData<?>[] getMapArray(String key, IMapData<?>[] failback) {
            return IData.getArray(this, key, IMapData.class, IMapData[]::new, (list, i)->list.getMap(i, this.emptyMap()), failback);
        }
        default IMapData<?>[] getMapArray(String key) {
            return this.getMapArray(key, new IMapData[0]);
        }

        IListData<?> getList(String key, Class<?> typeClass, IListData<?> failback);
        default IListData<?> getList(String key, Class<?> typeClass){
            return this.getList(key, typeClass, this.emptyList());
        }
        default IListData<?>[] getListArray(String key, Class<?> typeClass, IListData<?>[] failback) {
            return IData.getArray(this, key, IListData.class, IListData[]::new, (list, i)->list.getList(i, typeClass, this.emptyList()), failback);
        }
        default IListData<?>[] getListArray(String key, Class<?> typeClass) {
            return this.getListArray(key, typeClass, new IListData[0]);
        }

        default byte getByte(String key, byte failback) {
            return this.getNumber(key, failback).byteValue();
        }
        default byte getByte(String key) {
            return this.getByte(key, (byte)0);
        }
        default byte[] getByteArray(String key, byte[] failback) {
            return ArrayUtils.toArray(IData.getArray(this, key, Byte.class, Byte[]::new, (list, i)->list.getByte(i, (byte)0), ArrayUtils.toArray(failback)));
        }
        default byte[] getByteArray(String key) {
            return this.getByteArray(key, ArrayUtils.EMPTY_BYTE_ARRAY);
        }
        default short getShort(String key, short failback) {
            return this.getNumber(key, failback).shortValue();
        }
        default short getShort(String key) {
            return this.getShort(key, (short)0);
        }
        default short[] getShortArray(String key, short[] failback) {
            return ArrayUtils.toArray(IData.getArray(this, key, Short.class, Short[]::new, (list, i)->list.getShort(i, (short)0), ArrayUtils.toArray(failback)));
        }
        default short[] getShortArray(String key) {
            return this.getShortArray(key, ArrayUtils.EMPTY_SHORT_ARRAY);
        }
        default int getInt(String key, int failback) {
            return this.getNumber(key, failback).intValue();
        }
        default int getInt(String key) {
            return this.getInt(key, 0);
        }
        default int[] getIntArray(String key, int[] failback) {
            return ArrayUtils.toArray(IData.getArray(this, key, Integer.class, Integer[]::new, (list, i)->list.getInt(i, 0), ArrayUtils.toArray(failback)));
        }
        default int[] getIntArray(String key) {
            return this.getIntArray(key, ArrayUtils.EMPTY_INT_ARRAY);
        }
        default long getLong(String key, long failback) {
            return this.getNumber(key, failback).longValue();
        }
        default long getLong(String key) {
            return this.getLong(key, 0L);
        }
        default long[] getLongArray(String key, long[] failback) {
            return ArrayUtils.toArray(IData.getArray(this, key, Long.class, Long[]::new, (list, i)->list.getLong(i, 0L), ArrayUtils.toArray(failback)));
        }
        default long[] getLongArray(String key) {
            return this.getLongArray(key, ArrayUtils.EMPTY_LONG_ARRAY);
        }

        default float getFloat(String key, float failback) {
            return this.getNumber(key, failback).floatValue();
        }
        default float getFloat(String key) {
            return this.getFloat(key, 0f);
        }
        default float[] getFloatArray(String key, float[] failback) {
            return ArrayUtils.toArray(IData.getArray(this, key, Float.class, Float[]::new, (list, i)->list.getFloat(i, 0.0F), ArrayUtils.toArray(failback)));
        }
        default float[] getFloatArray(String key) {
            return this.getFloatArray(key, ArrayUtils.EMPTY_FLOAT_ARRAY);
        }
        default double getDouble(String key, double failback) {
            return this.getNumber(key, failback).doubleValue();
        }
        default double getDouble(String key) {
            return this.getDouble(key, 0D);
        }
        default double[] getDoubleArray(String key, double[] failback) {
            return ArrayUtils.toArray(IData.getArray(this, key, Double.class, Double[]::new, (list, i)->list.getDouble(i, 0.0f), ArrayUtils.toArray(failback)));
        }
        default double[] getDoubleArray(String key) {
            return this.getDoubleArray(key, ArrayUtils.EMPTY_DOUBLE_ARRAY);
        }
        
        BigInteger getBigInteger(String key, BigInteger failback);
        default BigInteger getBigInteger(String key) {
            return this.getBigInteger(key, BigInteger.ZERO);
        }
        default BigInteger[] getBigIntegerArray(String key, BigInteger[] failback) {
            return IData.getArray(this, key, BigInteger.class, BigInteger[]::new, (list, i)->list.getBigInteger(i, BigInteger.ZERO), failback);
        }
        default BigInteger[] getBigIntegerArray(String key) {
            return this.getBigIntegerArray(key, ArrayUtils.EMPTY_BIG_INTEGER_ARRAY);
        }
        BigDecimal getBigDecimal(String key, BigDecimal failback);
        default BigDecimal getBigDecimal(String key) {
            return this.getBigDecimal(key, BigDecimal.ZERO);
        }
        default BigDecimal[] getBigDecimalArray(String key, BigDecimal[] failback) {
            return IData.getArray(this, key, BigDecimal.class, BigDecimal[]::new, (list, i)->list.getBigDecimal(i, BigDecimal.ZERO), failback);
        }
        default BigDecimal[] getBigDecimalArray(String key) {
            return this.getBigDecimalArray(key, ArrayUtils.EMPTY_BIG_DECIMAL_ARRAY);
        }

        boolean getBoolean(String key, boolean failback);
        default boolean getBoolean(String key) {
            return this.getBoolean(key, false);
        }
        default boolean[] getBooleanArray(String key, boolean[] failback) {
            return ArrayUtils.toArray(IData.getArray(this, key, Boolean.class, Boolean[]::new, (list, i)->list.getBoolean(i, false), ArrayUtils.toArray(failback)));
        }
        default boolean[] getBooleanArray(String key) {
            return this.getBooleanArray(key, ArrayUtils.EMPTY_BOOLEAN_ARRAY);
        }
        Number getNumber(String key, Number failback);
        default Number getNumber(String key) {
            return this.getNumber(key, 0);
        }
        default Number[] getNumberArray(String key, Number[] failback) {
            return IData.getArray(this, key, Number.class, Number[]::new, (list, i)->list.getNumber(i, 0), failback);
        }
        default Number[] getNumberArray(String key) {
            return this.getNumberArray(key, ArrayUtils.EMPTY_NUMBER_ARRAY);
        }

        default char getChar(String key) {
            return this.getChar(key, NULL_CHAR);
        }
        default char getChar(String key, char failback){
            String s = this.getString(key, null);
            if(s == null || s.isEmpty()){
                return failback;
            }
            return s.charAt(0);
        }
        default char[] getCharArray(String key) {
            return this.getCharArray(key, ArrayUtils.EMPTY_CHAR_ARRAY);
        }
        default char[] getCharArray(String key, char[] failback) {
            IListData<?> list = this.getList(key, String.class, null);
            if (list == null) {
                return failback;
            }
            char[] result = new char[list.size()];
            list.foreach((i,data)->
                result[i] = data.getAsPrimitive().getAsChar()
            );
            return result;
        }

        String getString(String key, String failback);
        default String getString(String key) {
            return this.getString(key, "");
        }
        default String[] getStringArray(String key, String[] failback) {
            return IData.getArray(this, key, String.class, String[]::new, (list, i)->list.getString(i, ""), failback);
        }
        default String[] getStringArray(String key) {
            return this.getStringArray(key, ArrayUtils.EMPTY_STRING_ARRAY);
        }

        default ResourceLocation getLocation(String key, ResourceLocation failback) {
            return Utils.location(this.getString(key, String.valueOf(failback)));
        }
        default ResourceLocation getLocation(String key) {
            return Utils.location(this.getString(key, ""));
        }
        default ResourceLocation[] getLocationArray(String key, ResourceLocation[] failback) {
            return IData.getArray(this, key, ResourceLocation.class, ResourceLocation[]::new, (list, i)->list.getLocation(i, null), failback);
        }
        default ResourceLocation[] getLocationArray(String key) {
            return this.getLocationArray(key, new ResourceLocation[0]);
        }

        default IMapData<T> putData(String key, IData<?> data) {
            if (data instanceof IMapData) {
                this.putData(key, data.getAsMap());
            }else if (data instanceof IListData) {
                this.putData(key, data.getAsList());
            }else if (data instanceof IPrimitiveData) {
                IPrimitiveData<?> primitive = data.getAsPrimitive();
                if (primitive.isNumber()) {
                    this.putData(key, primitive.getAsNumber());
                }else if (primitive.isBoolean()) {
                    this.putData(key, primitive.getAsBoolean());
                }else if (primitive.isString()) {
                    this.putData(key, primitive.getAsString());
                }
            } else if (data == null || data instanceof INullData) {
                this.putNull(key);
            }
            return this;
        }
        IMapData<T> putNull(String key);
        IMapData<T> putData(String key, IMapData<?> data);
        default IMapData<T> putData(String key, Collection<IMapData<?>> data) {
            IListData<?> list = this.newList();
            for (IMapData<?> datum : data) {
                if (datum != null) {
                    list.putData(datum);
                }
            }
            return this.putData(key, list);
        }
        default IMapData<T> putData(String key, IMapData<?>[] data) {
            IListData<?> list = this.newList();
            for (IMapData<?> datum : data) {
                if (datum != null) {
                    list.putData(datum);
                }
            }
            return this.putData(key, list);
        }

        IMapData<T> putData(String key, IListData<?> data);
        default IMapData<T> putData(String key, Collection<IListData<?>> data, byte unuse) {
            IListData<?> list = this.newList();
            for (IListData<?> datum : data) {
                if (datum != null) {
                    list.putData(datum);
                }
            }
            return this.putData(key, list);
        }
        default IMapData<T> putData(String key, IListData<?>[] data){
            IListData<?> list = this.newList();
            for (IListData<?> datum : data) {
                if (datum != null) {
                    list.putData(datum);
                }
            }
            return this.putData(key, list);
        }

        default IMapData<T> putData(String key, byte data) {
            return this.putData(key, (Byte)data);
        }
        default IMapData<T> putData(String key, byte[] data) {
            IListData<?> list = this.newList();
            for (byte datum : data) {
                list.putData(datum);
            }
            return this.putData(key, list);
        }

        default IMapData<T> putData(String key, short data) {
            return this.putData(key, (Short)data);
        }
        default IMapData<T> putData(String key, short[] data) {
            IListData<?> list = this.newList();
            for (short datum : data) {
                list.putData(datum);
            }
            return this.putData(key, list);
        }

        default IMapData<T> putData(String key, int data) {
            return this.putData(key, (Integer)data);
        }
        default IMapData<T> putData(String key, int[] data) {
            IListData<?> list = this.newList();
            for (int datum : data) {
                list.putData(datum);
            }
            return this.putData(key, list);
        }

        default IMapData<T> putData(String key, long data) {
            return this.putData(key, (Long)data);
        }
        default IMapData<T> putData(String key, long[] data) {
            IListData<?> list = this.newList();
            for (long datum : data) {
                list.putData(datum);
            }
            return this.putData(key, list);
        }

        default IMapData<T> putData(String key, float data) {
            return this.putData(key, (Float)data);
        }
        default IMapData<T> putData(String key, float[] data) {
            IListData<?> list = this.newList();
            for (float datum : data) {
                list.putData(datum);
            }
            return this.putData(key, list);
        }

        default IMapData<T> putData(String key, double data) {
            return this.putData(key, (Double)data);
        }
        default IMapData<T> putData(String key, double[] data) {
            IListData<?> list = this.newList();
            for (double datum : data) {
                list.putData(datum);
            }
            return this.putData(key, list);
        }

        IMapData<T> putData(String key, Number data);
        default IMapData<T> putData(String key, Number[] data) {
            IListData<?> list = this.newList();
            for (Number datum : data) {
                list.putData(datum);
            }
            return this.putData(key, list);
        }

        IMapData<T> putData(String key, boolean data);
        default IMapData<T> putData(String key, boolean[] data) {
            IListData<?> list = this.newList();
            for (boolean datum : data) {
                list.putData(datum);
            }
            return this.putData(key, list);
        }

        default IMapData<T> putData(String key, char data){
            return this.putData(key, String.valueOf(data));
        }
        default IMapData<T> putData(String key, char[] data) {
            IListData<?> list = this.newList();
            for (char datum : data) {
                list.putData(datum);
            }
            return this.putData(key, list);
        }

        IMapData<T> putData(String key, String data);
        default IMapData<T> putData(String key, String[] data) {
            IListData<?> list = this.newList();
            for (String datum : data) {
                list.putData(datum);
            }
            return this.putData(key, list);
        }
        default IMapData<T> putData(String key, ResourceLocation data) {
            this.putData(key, String.valueOf(data));
            return this;
        }
        default IMapData<T> putData(String key, ResourceLocation[] data) {
            IListData<?> list = this.newList();
            for (ResourceLocation datum : data) {
                list.putData(datum);
            }
            return this.putData(key, list);
        }
    }

    interface IListData<T> extends IData<T>, IMaker {
        IListData<T> copy();

        boolean writeToFile(File file, boolean compressed, String charset) throws Exception;
        default <E> IListData<E> transfer(IListData<E> other) {
            this.foreach((i, v)->other.putData(v));
            return other;
        }

        int size();
        default boolean isEmpty() {
            return this.size() == 0;
        }
        default boolean containsKey(int index){
            return index >= 0 && index < size();
        }
        default void foreach(BiConsumer<Integer, IData<?>> consumer) {
            if (this.isEmpty()) {
                return;
            }
            for (int i = 0; i < this.size(); i++) {
                consumer.accept(i, this.getData(i, this.nullData()));
            }
        }
        Object get(int index, Object failback);
        default Object get(int index) {
            return this.get(index, null);
        }
        IData<?> getData(int index, IData<?> failback);
        default IData<?> getData(int index) {
            return this.getData(index, this.nullData());
        }

        IMapData<?> getMap(int index, IMapData<?> failback);
        default IMapData<?> getMap(int index) {
            return this.getMap(index, this.emptyMap());
        }
        default IMapData<?>[] getMapArray(int index, IMapData<?>[] failback) {
            return IData.getArray(this, index, IMapData.class, IMapData[]::new, (list, i)->list.getMap(i, this.emptyMap()), failback);
        }
        default IMapData<?>[] getMapArray(int index) {
            return this.getMapArray(index, new IMapData[0]);
        }

        IListData<?> getList(int index, Class<?> typeClass, IListData<?> failback);
        default IListData<?> getList(int index, Class<?> typeClass){
            return this.getList(index, typeClass, this.emptyList());
        }
        default IListData<?>[] getListArray(int index, Class<?> typeClass, IListData<?>[] failback) {
            return IData.getArray(this, index, IListData.class, IListData[]::new, (list, i)->list.getList(i, typeClass, this.emptyList()), failback);
        }
        default IListData<?>[] getListArray(int index, Class<?> typeClass) {
            return this.getListArray(index, typeClass, new IListData[0]);
        }

        default byte getByte(int index, byte failback) {
            return this.getNumber(index, failback).byteValue();
        }
        default byte getByte(int index) {
            return this.getByte(index, (byte)0);
        }
        default byte[] getByteArray(int index, byte[] failback) {
            return ArrayUtils.toArray(IData.getArray(this, index, Byte.class, Byte[]::new, (list, i)->list.getByte(i, (byte)0), ArrayUtils.toArray(failback)));
        }
        default byte[] getByteArray(int index) {
            return this.getByteArray(index, ArrayUtils.EMPTY_BYTE_ARRAY);
        }
        default short getShort(int index, short failback) {
            return this.getNumber(index, failback).shortValue();
        }
        default short getShort(int index) {
            return this.getShort(index, (short)0);
        }
        default short[] getShortArray(int index, short[] failback) {
            return ArrayUtils.toArray(IData.getArray(this, index, Short.class, Short[]::new, (list, i)->list.getShort(i, (short)0), ArrayUtils.toArray(failback)));
        }
        default short[] getShortArray(int index) {
            return this.getShortArray(index, ArrayUtils.EMPTY_SHORT_ARRAY);
        }
        default int getInt(int index, int failback) {
            return this.getNumber(index, failback).intValue();
        }
        default int getInt(int index) {
            return this.getInt(index, 0);
        }
        default int[] getIntArray(int index, int[] failback) {
            return ArrayUtils.toArray(IData.getArray(this, index, Integer.class, Integer[]::new, (list, i)->list.getInt(i, 0), ArrayUtils.toArray(failback)));
        }
        default int[] getIntArray(int index) {
            return this.getIntArray(index, ArrayUtils.EMPTY_INT_ARRAY);
        }
        default long getLong(int index, long failback) {
            return this.getNumber(index, failback).longValue();
        }
        default long getLong(int index) {
            return this.getLong(index, 0L);
        }
        default long[] getLongArray(int index, long[] failback) {
            return ArrayUtils.toArray(IData.getArray(this, index, Long.class, Long[]::new, (list, i)->list.getLong(i, 0L), ArrayUtils.toArray(failback)));
        }
        default long[] getLongArray(int index) {
            return this.getLongArray(index, ArrayUtils.EMPTY_LONG_ARRAY);
        }

        default float getFloat(int index, float failback) {
            return this.getNumber(index, failback).floatValue();
        }
        default float getFloat(int index) {
            return this.getFloat(index, 0f);
        }
        default float[] getFloatArray(int index, float[] failback) {
            return ArrayUtils.toArray(IData.getArray(this, index, Float.class, Float[]::new, (list, i)->list.getFloat(i, 0.0F), ArrayUtils.toArray(failback)));
        }
        default float[] getFloatArray(int index) {
            return this.getFloatArray(index, ArrayUtils.EMPTY_FLOAT_ARRAY);
        }
        default double getDouble(int index, double failback) {
            return this.getNumber(index, failback).doubleValue();
        }
        default double getDouble(int index) {
            return this.getDouble(index, 0D);
        }
        default double[] getDoubleArray(int index, double[] failback) {
            return ArrayUtils.toArray(IData.getArray(this, index, Double.class, Double[]::new, (list, i)->list.getDouble(i, 0.0f), ArrayUtils.toArray(failback)));
        }
        default double[] getDoubleArray(int index) {
            return this.getDoubleArray(index, ArrayUtils.EMPTY_DOUBLE_ARRAY);
        }

        BigInteger getBigInteger(int index, BigInteger failback);
        default BigInteger getBigInteger(int index) {
            return this.getBigInteger(index, BigInteger.ZERO);
        }
        default BigInteger[] getBigIntegerArray(int index, BigInteger[] failback) {
            return IData.getArray(this, index, BigInteger.class, BigInteger[]::new, (list, i)->list.getBigInteger(i, BigInteger.ZERO), failback);
        }
        default BigInteger[] getBigIntegerArray(int index) {
            return this.getBigIntegerArray(index, ArrayUtils.EMPTY_BIG_INTEGER_ARRAY);
        }
        BigDecimal getBigDecimal(int index, BigDecimal failback);
        default BigDecimal getBigDecimal(int index) {
            return this.getBigDecimal(index, BigDecimal.ZERO);
        }
        default BigDecimal[] getBigDecimalArray(int index, BigDecimal[] failback) {
            return IData.getArray(this, index, BigDecimal.class, BigDecimal[]::new, (list, i)->list.getBigDecimal(i, BigDecimal.ZERO), failback);
        }
        default BigDecimal[] getBigDecimalArray(int index) {
            return this.getBigDecimalArray(index, ArrayUtils.EMPTY_BIG_DECIMAL_ARRAY);
        }

        boolean getBoolean(int index, boolean failback);
        default boolean getBoolean(int index) {
            return this.getBoolean(index, false);
        }
        default boolean[] getBooleanArray(int index, boolean[] failback) {
            return ArrayUtils.toArray(IData.getArray(this, index, Boolean.class, Boolean[]::new, (list, i)->list.getBoolean(i, false), ArrayUtils.toArray(failback)));
        }
        default boolean[] getBooleanArray(int index) {
            return this.getBooleanArray(index, ArrayUtils.EMPTY_BOOLEAN_ARRAY);
        }
        Number getNumber(int index, Number failback);
        default Number getNumber(int index) {
            return this.getNumber(index, 0);
        }
        default Number[] getNumberArray(int index, Number[] failback) {
            return IData.getArray(this, index, Number.class, Number[]::new, (list, i)->list.getNumber(i, 0), failback);
        }
        default Number[] getNumberArray(int index) {
            return this.getNumberArray(index, ArrayUtils.EMPTY_NUMBER_ARRAY);
        }

        default char getChar(int index) {
            return this.getChar(index, NULL_CHAR);
        }
        default char getChar(int index, char failback) {
            String s = this.getString(index, null);
            if (s == null || s.isEmpty()) {
                return failback;
            }
            return s.charAt(0);
        }
        default char[] getCharArray(int index) {
            return this.getCharArray(index, ArrayUtils.EMPTY_CHAR_ARRAY);
        }
        default char[] getCharArray(int index, char[] failback) {
            IListData<?> list = this.getList(index, String.class, null);
            if (list == null) {
                return failback;
            }
            char[] result = new char[list.size()];
            list.foreach((i,data)->
                    result[i] = data.getAsPrimitive().getAsChar()
            );
            return result;
        }

        String getString(int index, String failback);
        default String getString(int index) {
            return this.getString(index, "");
        }
        default String[] getStringArray(int index, String[] failback) {
            return IData.getArray(this, index, String.class, String[]::new, (list, i)->list.getString(i, ""), failback);
        }
        default String[] getStringArray(int index) {
            return this.getStringArray(index, ArrayUtils.EMPTY_STRING_ARRAY);
        }

        default ResourceLocation getLocation(int index, ResourceLocation failback) {
            return Utils.location(this.getString(index, String.valueOf(failback)));
        }
        default ResourceLocation getLocation(int index) {
            return Utils.location(this.getString(index, ""));
        }
        default ResourceLocation[] getLocationArray(int index, ResourceLocation[] failback) {
            return IData.getArray(this, index, ResourceLocation.class, ResourceLocation[]::new, (list, i)->list.getLocation(i, null), failback);
        }
        default ResourceLocation[] getLocationArray(int index) {
            return this.getLocationArray(index, new ResourceLocation[0]);
        }
        
        default IListData<T> putData(IData<?> data) {
            if (data instanceof IMapData) {
                this.putData(data.getAsMap());
            }
            if (data instanceof IListData) {
                this.putData(data.getAsList());
            }
            if (data instanceof IPrimitiveData) {
                IPrimitiveData<?> primitive = data.getAsPrimitive();
                if (primitive.isNumber()) {
                    this.putData(primitive.getAsNumber());
                }else if (primitive.isBoolean()) {
                    this.putData(primitive.getAsBoolean());
                }else if (primitive.isString()) {
                    this.putData(primitive.getAsString());
                }
            }
            if (data == null || data instanceof INullData) {
                this.putNull();
            }
            return this;
        }
        IListData<T> putNull();
        IListData<T> putData(IMapData<?> data);
        default IListData<T> putData(Collection<IMapData<?>> data) {
            IListData<?> list = this.newList();
            for (IMapData<?> datum : data) {
                if (datum != null) {
                    list.putData(datum);
                }
            }
            return this.putData(list);
        }
        default IListData<T> putData(IMapData<?>[] data) {
            IListData<?> list = this.newList();
            for (IMapData<?> datum : data) {
                if (datum != null) {
                    list.putData(datum);
                }
            }
            return this.putData(list);
        }

        IListData<T> putData(IListData<?> data);
        default IListData<T> putData(Collection<IListData<?>> data, byte unuse) {
            IListData<?> list = this.newList();
            for (IListData<?> datum : data) {
                if (datum != null) {
                    list.putData(datum);
                }
            }
            return this.putData(list);
        }
        default IListData<T> putData(IListData<?>[] data) {
            IListData<?> list = this.newList();
            for (IListData<?> datum : data) {
                if (datum != null) {
                    list.putData(datum);
                }
            }
            return this.putData(list);
        }

        default IListData<T> putData(byte data) {
            this.putData((Byte) data);
            return this;
        }
        default IListData<T> putData(byte[] data) {
            IListData<?> list = this.newList();
            for (byte datum : data) {
                list.putData(datum);
            }
            return this.putData(list);
        }

        default IListData<T> putData(short data) {
            return this.putData((Short) data);
        }
        default IListData<T> putData(short[] data) {
            IListData<?> list = this.newList();
            for (short datum : data) {
                list.putData(datum);
            }
            return this.putData(list);
        }

        default IListData<T> putData(int data) {
            return this.putData((Integer) data);
        }
        default IListData<T> putData(int[] data) {
            IListData<?> list = this.newList();
            for (int datum : data) {
                list.putData(datum);
            }
            return this.putData(list);
        }

        default IListData<T> putData(long data) {
            return this.putData((Long) data);
        }
        default IListData<T> putData(long[] data) {
            IListData<?> list = this.newList();
            for (long datum : data) {
                list.putData(datum);
            }
            return this.putData(list);
        }

        default IListData<T> putData(float data) {
            return this.putData((Float) data);
        }
        default IListData<T> putData(float[] data) {
            IListData<?> list = this.newList();
            for (float datum : data) {
                list.putData(datum);
            }
            return this.putData(list);
        }

        default IListData<T> putData(double data) {
            return this.putData((Double)data);
        }
        default IListData<T> putData(double[] data) {
            IListData<?> list = this.newList();
            for (double datum : data) {
                list.putData(datum);
            }
            return this.putData(list);
        }
        
        default IListData<T> putData(BigInteger data) {
            return this.putData((Number) data);
        }
        default IListData<T> putData(BigInteger[] data) {
            IListData<?> list = this.newList();
            for (BigInteger datum : data) {
                list.putData(datum);
            }
            return this.putData(list);
        }
        default IListData<T> putData(BigDecimal data) {
            return this.putData((Number) data);
        }
        default IListData<T> putData(BigDecimal[] data) {
            IListData<?> list = this.newList();
            for (BigDecimal datum : data) {
                list.putData(datum);
            }
            return this.putData(list);
        }

        IListData<T> putData(Number data);
        default IListData<T> putData(Number[] data) {
            IListData<?> list = this.newList();
            for (Number datum : data) {
                list.putData(datum);
            }
            return this.putData(list);
        }

        IListData<T> putData(boolean data);
        default IListData<T> putData(boolean[] data) {
            IListData<?> list = this.newList();
            for (boolean datum : data) {
                list.putData(datum);
            }
            return this.putData(list);
        }

        default IListData<T> putData(char data) {
            return this.putData(String.valueOf(data));
        }
        default IListData<T> putData(char[] data) {
            IListData<?> list = this.newList();
            for (char datum : data) {
                list.putData(datum);
            }
            return this.putData(list);
        }

        IListData<T> putData(String data);
        default IListData<T> putData(String[] data) {
            IListData<?> list = this.newList();
            for (String datum : data) {
                list.putData(datum);
            }
            return this.putData(list);
        }
        default IListData<T> putData(ResourceLocation data) {
            this.putData(String.valueOf(data));
            return this;
        }
        default IListData<T> putData(ResourceLocation[] data) {
            IListData<?> list = this.newList();
            for (ResourceLocation datum : data) {
                list.putData(datum);
            }
            return this.putData(list);
        }
    }

    interface IPrimitiveData<T> extends IData<T> {
        IData<?> copy();

        default<E> IPrimitiveData<E> transfer(IPrimitiveData<E> other) {
            if (this.isNumber()) {
                other.setData(this.getAsNumber());
            }else if (this.isBoolean()) {
                other.setData(this.getAsBoolean());
            } else if (this.isString()) {
                other.setData(this.getAsString());
            } else if (this.isArray()) {
                other.setData(this.getAsArray());
            }
            return other;
        }
        default IPrimitiveData<T> setData(char data) {
            return this.setData(String.valueOf(data));
        }
        IPrimitiveData<T> setData(String data);
        IPrimitiveData<T> setData(boolean data);
        IPrimitiveData<T> setData(Number data);
        IPrimitiveData<T> setData(Object[] data);
        IPrimitiveData<T> setData(IListData<?> data);


        default byte getAsByte() {
            if (this.isNumber()) {
                return this.getAsNumber().byteValue();
            }
            throw new IllegalStateException("Not a byte:" + this.asString());
        }
        byte[] getAsByteArray();

        default short getAsShort() {
            if (this.isNumber()) {
                return this.getAsNumber().shortValue();
            }
            throw new IllegalStateException("Not a short:" + this.asString());
        }
        short[] getAsShortArray();

        default int getAsInt() {
            if (this.isNumber()) {
                return this.getAsNumber().intValue();
            }
            throw new IllegalStateException("Not a int:" + this.asString());
        }
        int[] getAsIntArray();

        default long getAsLong() {
            if (this.isNumber()) {
                return this.getAsNumber().longValue();
            }
            throw new IllegalStateException("Not a long:" + this.asString());
        }
        long[] getAsLongArray();

        default float getAsFloat() {
            if (this.isNumber()) {
                return this.getAsNumber().floatValue();
            }
            throw new IllegalStateException("Not a float:" + this.asString());
        }
        float[] getAsFloatArray();

        default double getAsDouble() {
            if (this.isNumber()) {
                return this.getAsNumber().doubleValue();
            }
            throw new IllegalStateException("Not a double:" + this.asString());
        }
        double[] getAsDoubleArray();

        default BigInteger getAsBigInteger() {
            if (this.isNumber()) {
                return new BigInteger(String.valueOf(this.getAsNumber()));
            }
            if (this.isString()) {
                return new  BigInteger(this.getAsString());
            }
            throw new IllegalStateException("Not a big integer:" + this.asString());
        }
        BigInteger[] getAsBigIntegerArray();

        default BigDecimal getAsBigDecimal() {
            if (this.isNumber()) {
                return new BigDecimal(String.valueOf(this.getAsNumber()));
            }
            if (this.isString()) {
                return new BigDecimal(this.getAsString());
            }
            throw new IllegalStateException("Not a big decimal:" + this.asString());
        }
        BigDecimal[] getAsBigDecimalArray();

        boolean isNumber();
        Number getAsNumber();
        Number[] getAsNumberArray();

        boolean isBoolean();
        boolean getAsBoolean();
        boolean[] getAsBooleanArray();

		default boolean isChar() {
            return this.isString();
        }
		default char getAsChar() {
            if (this.isString()) {
                String s = this.getAsString();
                if (!s.isEmpty()) {
                    return s.charAt(0);
                }
            }
            return NULL_CHAR;
        }
		char[] getAsCharArray();

        boolean isString();
        String getAsString();
        String[] getAsStringArray();

        boolean isArray();
        IListData<?> getAsArray();

        default boolean isLocation(){
            return this.isString() && this.getAsString().contains(":");
        }
        default ResourceLocation getAsLocation() {
            if (this.isLocation()) {
                return Utils.location(this.getAsString());
            }
            throw new IllegalStateException("Not a location:" + this.asString());
        }
    }

    interface INullData<T> extends IData<T> { }

    interface IMaker {
        IMapData<?> emptyMap();
        IListData<?> emptyList();

        IMapData<?> newMap();
        IListData<?> newList();
        IPrimitiveData<?> newPrimitive();
        INullData<?> nullData();

        default IData<?> castData(IData<?> other) {
            if (other instanceof IMapData) {
                return other.getAsMap().transfer(this.newMap());
            }else if (other instanceof IListData) {
                return other.getAsList().transfer(this.newList());
            }else if (other instanceof IPrimitiveData) {
                return other.getAsPrimitive().transfer(this.newPrimitive());
            }
            return this.nullData();
        }
    }
}
