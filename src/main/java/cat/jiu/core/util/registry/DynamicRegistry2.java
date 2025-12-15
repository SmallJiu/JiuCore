package cat.jiu.core.util.registry;

import cat.jiu.core.api.IData;
import cat.jiu.core.api.Lambdas;
import cat.jiu.core.api.serializable.IDataSerializable;
import cat.jiu.core.util.Utils;
import cat.jiu.core.util.element.data.JsonData;
import cat.jiu.core.util.element.data.NBTData;
import com.google.gson.JsonObject;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import org.apache.logging.log4j.LogManager;

import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class DynamicRegistry2<K, V extends IDataSerializable<IData.IMapData<?>> & Supplier<K>> {
    public static <K, V> Lambdas.Function2<K, IData.IMapData<?>, V> failback(ResourceLocation typeID) {
        return (id, data)->{
            LogManager.getLogger("Registry").fatal("{} is not register to {}.", id, typeID);
            return null;
        };
    }

    protected final ConcurrentHashMap<K, Function<IData.IMapData<?>, V>> registry = new ConcurrentHashMap<>();
    protected Lambdas.Function2<K, IData.IMapData<?>, V> failback;
    protected Function<IData.IMapData<?>, K> keyGetter;

    public DynamicRegistry2(){
    }
    public DynamicRegistry2(String modid, String typeName) {
        this(Utils.location(modid, typeName));
    }
    public DynamicRegistry2(ResourceLocation id) {
        this(failback(id));
    }

    public DynamicRegistry2(Lambdas.Function2<K, IData.IMapData<?>, V> failback) {
        this.failback = failback;
    }

    public void init(){}

    public DynamicRegistry2<K, V> register(Consumer<DynamicRegistry2<K, V>> register) {
        register.accept(this);
        return this;
    }

    public DynamicRegistry2<K, V> setStringKeyGetter(Function<String, K> keyInstance){
        return this.setKeyGetter(StaticRegistry.DEFAULT_ID_TAG_NAME, keyInstance);
    }
    public DynamicRegistry2<K, V> setKeyGetter(String keyName, Function<String, K> keyInstance){
        return this.setKeyGetter(
                data -> keyInstance.apply(data.getString(keyName))
        );
    }
    public DynamicRegistry2<K, V> setKeyGetter(Function<IData.IMapData<?>, K> getter) {
        this.keyGetter = getter;
        return this;
    }

    public DynamicRegistry2<K, V> setFailBack(
            Lambdas.Function2<K, IData.IMapData<?>, V> failback
    ) {
        this.failback = failback;
        return this;
    }

    /**
     *
     * @param clazz need class has an empty public constructor.
     */
    public K register(K id, Class<? extends V> clazz) {
        return this.register(id, data->{
            try {
                V instance = clazz.getDeclaredConstructor().newInstance();
                instance.read(data);
                return instance;
            }catch (Exception e){
                try {
                    return clazz.getDeclaredConstructor(IData.IMapData.class).newInstance(data);
                }catch (Exception e2){
                    e2.printStackTrace();
                    return this.failback.apply(id, data);
                }
            }
        });
    }

    public K register(K id, Function<IData.IMapData<?>, V> getter) {
        if (!this.registry.containsKey(id)) {
            this.registry.put(id, getter);
        }
        return id;
    }

    public boolean unregister(K id) {
        return this.registry.remove(id) != null;
    }
    public boolean registered(K id) {
        return this.registry.containsKey(id);
    }
    public boolean hasEntry(){
        return !this.registry.isEmpty();
    }

    public V get(K id, CompoundTag tag) {
        return this.get(id, NBTData.map(tag));
    }
    public V get(K id, JsonObject json) {
        return this.get(id, JsonData.map(json));
    }
    public V get(K id, IData.IMapData<?> data) {
        if (this.registry.containsKey(id)) {
            return this.registry.get(id).apply(data);
        }
        return this.failback.apply(id, data);
    }

    public V get(CompoundTag data) {
        return this.get(NBTData.map(data));
    }
    public V get(JsonObject data) {
        return this.get(JsonData.map(data));
    }
    public V get(IData.IMapData<?> data) {
        if (this.keyGetter != null) {
            return this.get(this.keyGetter.apply(data), data);
        }
        return null;
    }

    public V get(K id) {
        return this.get(id, new JsonObject());
    }

    public Set<K> getIDs() {
        return Collections.unmodifiableSet(this.registry.keySet());
    }
}
