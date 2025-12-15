package cat.jiu.core.util.registry;

import cat.jiu.core.api.IData;
import cat.jiu.core.util.element.data.JsonData;
import cat.jiu.core.util.element.data.NBTData;
import com.google.gson.JsonObject;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryObject;
import org.apache.logging.log4j.LogManager;

import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class DeferredRegistry<V> {
    public static <K, V> Function<K, RegistryObject<V>> failBack(ResourceLocation typeID) {
        return name -> {
            LogManager.getLogger("Registry").fatal("{} is not register to {}. ", name, typeID);
            return null;
        };
    }

    protected final DeferredRegister<V> registry;
    protected final ConcurrentHashMap<String, RegistryObject<V>> entries = new ConcurrentHashMap<>();
    protected Function<String, RegistryObject<V>> failBack;
    protected Function<IData.IMapData<?>, String> keyGetter;

    public DeferredRegistry(DeferredRegister<V> register) {
        this.registry = register;
    }
    public DeferredRegistry(String modid, IForgeRegistry<V> type) {
        this(DeferredRegister.create(type, modid));
    }
    public DeferredRegistry(String modid, ResourceKey<? extends Registry<V>> type, boolean optionalRegistry) {
        this(optionalRegistry ? DeferredRegister.createOptional(type, modid) : DeferredRegister.create(type, modid));
    }
    public DeferredRegistry(String modid, ResourceLocation typeName, boolean optionalRegistry) {
        this(optionalRegistry ? DeferredRegister.createOptional(typeName, modid) : DeferredRegister.create(typeName, modid));
    }

    public void init(){}

    public DeferredRegistry<V> setKeyGetter() {
        return this.setKeyGetter(StaticRegistry.DEFAULT_ID_TAG_NAME);
    }
    public DeferredRegistry<V> setKeyGetter(String idName) {
        return setKeyGetter(idName, "");
    }
    public DeferredRegistry<V> setKeyGetter(String idName, String failback) {
        this.keyGetter = data -> data.getString(idName, failback);
        return this;
    }

    public DeferredRegistry<V> setFailBack(Function<String, RegistryObject<V>> failBack) {
        this.failBack = failBack;
        return this;
    }

    public void registerBus(IEventBus bus) {
        this.registry.register(bus);
    }
    public DeferredRegistry<V> register(Consumer<DeferredRegistry<V>> register) {
        register.accept(this);
        return this;
    }
    public RegistryObject<V> register(String name, Supplier<V> instance) {
        if (this.entries.containsKey(name)) {
            return this.entries.get(name);
        }
        RegistryObject<V> object = this.registry.register(name, instance);
        this.entries.put(name, object);
        return object;
    }

    public boolean registered(String name) {
        return this.entries.containsKey(name);
    }

    public RegistryObject<V> get(String name) {
        if (this.entries.containsKey(name)) {
            return this.entries.get(name);
        }
        return this.failBack.apply(name);
    }

    public RegistryObject<V> get(CompoundTag data) {
        return this.get(NBTData.map(data));
    }
    public RegistryObject<V> get(JsonObject data) {
        return this.get(JsonData.map(data));
    }
    public RegistryObject<V> get(IData.IMapData<?> data) {
        if (this.keyGetter != null) {
            return this.get(this.keyGetter.apply(data));
        }
        return null;
    }

    public Set<String> getNames() {
        return Collections.unmodifiableSet(this.entries.keySet());
    }
}
