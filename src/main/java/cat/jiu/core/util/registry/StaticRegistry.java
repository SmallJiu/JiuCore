package cat.jiu.core.util.registry;

import cat.jiu.core.api.IData;
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

public class StaticRegistry<K, V extends Supplier<K>> {
    public static final String DEFAULT_ID_TAG_NAME = "id";
    public static <K, T> Function<K, T>  failBack(ResourceLocation typeID) {
        return id -> {
            LogManager.getLogger("Registry").fatal("{} is not register to {}. ", id, typeID);
            return null;
        };
    }

    protected final ConcurrentHashMap<K, V> registry = new ConcurrentHashMap<>();
    protected Function<K, V> failBack;
    protected Function<IData.IMapData<?>, K> keyGetter;

    public StaticRegistry() {
    }
    public StaticRegistry(ResourceLocation id) {
        this(failBack(id));
    }
    public StaticRegistry(String modid, String typeName) {
        this(Utils.location(modid, typeName));
    }
    public StaticRegistry(Function<K, V> failBack) {
        this.failBack = failBack;
    }

    public void init(){}

    public StaticRegistry<K, V> setStringKeyGetter(Function<String, K> keyInstance){
        return this.setKeyGetter(DEFAULT_ID_TAG_NAME, keyInstance);
    }
    public StaticRegistry<K, V> setKeyGetter(String keyName, Function<String, K> keyInstance){
        return this.setKeyGetter(
                data -> keyInstance.apply(data.getString(keyName))
        );
    }
    public StaticRegistry<K, V> setKeyGetter(Function<IData.IMapData<?>, K> dataKeyGetter) {
        this.keyGetter = dataKeyGetter;
        return this;
    }

    public StaticRegistry<K, V> setFailBack(Function<K, V> failBack) {
        this.failBack = failBack;
        return this;
    }

    public StaticRegistry<K, V> register(Consumer<StaticRegistry<K, V>> register) {
        register.accept(this);
        return this;
    }
    public K register(V instance) {
        return this.register(instance.get(), instance);
    }
    public K register(K id, V instance) {
        if (!this.registry.containsKey(id)) {
            this.registry.put(id, instance);
        }
        return id;
    }

    public boolean registered(K id) {
        return this.registry.containsKey(id);
    }
    public V unregister(K id) {
        return this.registry.remove(id);
    }
    public void unregisterAll() {
        this.registry.clear();
    }
    public boolean hasEntry(){
        return !this.registry.isEmpty();
    }

    public V get(K id) {
        if (this.registry.containsKey(id)) {
            return this.registry.get(id);
        }
        return this.failBack.apply(id);
    }
    public V get(CompoundTag data) {
        return this.get(NBTData.map(data));
    }
    public V get(JsonObject data) {
        return this.get(JsonData.map(data));
    }
    public V get(IData.IMapData<?> data) {
        if (this.keyGetter != null) {
            return this.get(this.keyGetter.apply(data));
        }
        return null;
    }

    public Set<K> getIDs() {
        return Collections.unmodifiableSet(this.registry.keySet());
    }
}
