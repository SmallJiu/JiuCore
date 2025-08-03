package cat.jiu.core.util.registry;

import com.google.gson.JsonObject;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryObject;
import org.apache.logging.log4j.LogManager;

import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;
import java.util.function.Function;

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
    protected Function<CompoundTag, String> nbtKeyGetter;
    protected Function<JsonObject, String> jsonKeyGetter;

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
        return this.setKeyGetter(
                data-> data.getString(StaticRegistry.DEFAULT_ID_TAG_NAME),
                data-> data.get(StaticRegistry.DEFAULT_ID_TAG_NAME).getAsString()
        );
    }
    public DeferredRegistry<V> setKeyGetter(Function<CompoundTag, String> nbtKeyGetter, Function<JsonObject, String> jsonKeyGetter) {
        this.nbtKeyGetter = nbtKeyGetter;
        this.jsonKeyGetter = jsonKeyGetter;
        return this;
    }

    public DeferredRegistry<V> setFailBack(Function<String, RegistryObject<V>> failBack) {
        this.failBack = failBack;
        return this;
    }

    public DeferredRegistry<V> register(Consumer<DeferredRegistry<V>> register) {
        register.accept(this);
        return this;
    }
    public RegistryObject<V> register(String name, V instance) {
        if (this.entries.containsKey(name)) {
            return this.entries.get(name);
        }
        RegistryObject<V> object = this.registry.register(name, ()->instance);
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
        if (this.nbtKeyGetter != null) {
            return this.get(this.nbtKeyGetter.apply(data));
        }
        return null;
    }
    public RegistryObject<V> get(JsonObject data) {
        if (this.jsonKeyGetter != null) {
            return this.get(this.jsonKeyGetter.apply(data));
        }
        return null;
    }

    public Set<String> getNames() {
        return Collections.unmodifiableSet(this.entries.keySet());
    }
}
