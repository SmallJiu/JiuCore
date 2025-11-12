package cat.jiu.core.util.registry;

import cat.jiu.core.CoreMain;
import com.tterrag.registrate.AbstractRegistrate;
import com.tterrag.registrate.providers.ProviderType;
import com.tterrag.registrate.providers.RegistrateLangProvider;
import com.tterrag.registrate.providers.RegistrateProvider;
import com.tterrag.registrate.util.nullness.NonNullSupplier;
import com.tterrag.registrate.util.nullness.NonnullType;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.PackOutput;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.data.LanguageProvider;
import net.minecraftforge.fml.LogicalSide;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiFunction;

public class DynamicLanguageProvider extends RegistrateLangProvider implements RegistrateProvider {
    public static final ProviderType<DynamicLanguageProvider> TYPE = ProviderType.register("lang_provider", ((p, e) -> new DynamicLanguageProvider(p, e.getGenerator().getPackOutput())));
    public static final BiFunction<RegistrateLangProvider, String, String> TO_EN_UD = (provider, s) -> {
        try {
            Method method = RegistrateLangProvider.class.getDeclaredMethod("toUpsideDown", String.class);
            method.setAccessible(true);
            return (String) method.invoke(provider, s);
        } catch (Exception ignored) {
        }
        return s;
    };

    private final HashMap<String, LanguageProvider> languageProviders = new HashMap<>();
    private final AbstractRegistrate<?> owner;
    private final PackOutput output;
    public DynamicLanguageProvider(AbstractRegistrate<?> owner, PackOutput packOutput) {
        super(owner, packOutput);
        this.owner = owner;
        this.output = packOutput;
    }

    @Override
    public String getName() {
        return "Languages for mod: " + this.owner.getModid();
    }

    @Override
    public void add(String key, String value) {
        this.add(this.languageCode, key, value);
    }

    public void add(String lang, String key, String value) {
        this.setLanguageCode(lang);
        if (!this.languageProviders.containsKey(lang)) {
            this.languageProviders.put(lang, new LanguageProvider(this.output, this.owner.getModid(), lang) {
                @Override
                protected void addTranslations() {

                }
            });
        }
        try {
            if ("en_us".equals(lang)) {
                this.add("en_ud", key, TO_EN_UD.apply(this, value));
            }
            this.languageProviders.get(lang).add(key, value);
        } catch (Exception e) {
            CoreMain.LOGGER.error("Duplicate: language {}, translation key {}, value {}", languageCode, key, value);
            e.getStackTrace();
        }
    }

    private String languageCode = "en_us";

    public String getLanguageCode() {
        return languageCode;
    }

    public DynamicLanguageProvider setLanguageCode(String languageCode) {
        this.languageCode = languageCode;
        return this;
    }

    @Override
    protected void addTranslations() {
        this.owner.genData(TYPE, this);
    }

    @Override
    public CompletableFuture<?> run(CachedOutput cache) {
        this.addTranslations();
        return CompletableFuture.allOf(this.languageProviders.values().stream().map(provider ->  provider.run(cache)).toArray(CompletableFuture<?>[]::new));
    }
}
