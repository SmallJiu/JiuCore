package cat.jiu.core.util.registry;

import com.tterrag.registrate.AbstractRegistrate;
import com.tterrag.registrate.builders.BlockBuilder;
import com.tterrag.registrate.builders.BuilderCallback;
import com.tterrag.registrate.builders.ItemBuilder;
import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.ProviderType;
import com.tterrag.registrate.providers.RegistrateLangProvider;
import com.tterrag.registrate.util.nullness.NonNullBiFunction;
import com.tterrag.registrate.util.nullness.NonNullFunction;
import com.tterrag.registrate.util.nullness.NonNullSupplier;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;

public class CoreRegistrate extends AbstractRegistrate<CoreRegistrate> {
    protected CoreRegistrate(String modid) {
        super(modid);
    }
    public static CoreRegistrate create(String modid) {
        var ret = new CoreRegistrate(modid);
        ret.registerEventListeners(ret.getModEventBus());
        return ret;
    }

//    @Override
//    public <T extends Item, P> ItemBuilder<T, P> item(P parent, String name, NonNullFunction<Item.Properties, T> factory) {
//        return entry(name, callback -> CoreItemBuilder.create(this, parent, name, callback, factory).transform(builder -> builder));
//    }
//
//    @Override
//    public <T extends Block, P> BlockBuilder<T, P> block(P parent, String name, NonNullFunction<BlockBehaviour.Properties, T> factory) {
//        return entry(name, callback -> CoreBlockBuilder.create(this, parent, name, callback, factory));
//    }

    public static class CoreItemBuilder<T extends Item, P> extends ItemBuilder<T, P> {
        public static <T extends Item, P> ItemBuilder<T, P> create(AbstractRegistrate<?> owner, P parent, String name, BuilderCallback callback, NonNullFunction<Item.Properties, T> factory) {
            return new CoreItemBuilder<>(owner, parent, name, callback, factory)
                    .defaultModel().defaultLang();
        }
        protected CoreItemBuilder(AbstractRegistrate<?> owner, P parent, String name, BuilderCallback callback, NonNullFunction<Item.Properties, T> factory) {
            super(owner, parent, name, callback, factory);
        }

        @Override
        public ItemBuilder<T, P> lang(net.minecraftforge.common.util.NonNullFunction<T, String> langKeyProvider) {
            return lang(langKeyProvider, (p, t) -> p.getAutomaticName(t, getRegistryKey()));
        }

        @Override
        public ItemBuilder<T, P> lang(net.minecraftforge.common.util.NonNullFunction<T, String> langKeyProvider, String name) {
            return lang(langKeyProvider, (p, s) -> name);
        }

        public ItemBuilder<T, P> lang(net.minecraftforge.common.util.NonNullFunction<T, String> langKeyProvider, NonNullBiFunction<DynamicLanguageProvider, NonNullSupplier<? extends T>, String> localizedNameProvider) {
            this.getOwner().addDataGenerator(DynamicLanguageProvider.TYPE, prov -> {
                DataGenContext<Item, T> ctx = DataGenContext.from(this, getRegistryKey());
                prov.setLanguageCode("en_us");
                prov.add(langKeyProvider.apply(ctx.getEntry()), localizedNameProvider.apply(prov, ctx::getEntry));
            });
            return this;
        }
    }

    public static class CoreBlockBuilder<T extends Block, P> extends BlockBuilder<T, P> {
        public static <T extends Block, P> BlockBuilder<T, P> create(AbstractRegistrate<?> owner, P parent, String name, BuilderCallback callback, NonNullFunction<BlockBehaviour.Properties, T> factory) {
            return new CoreBlockBuilder<>(owner, parent, name, callback, factory, BlockBehaviour.Properties::of)
                    .defaultBlockstate().defaultLoot().defaultLang();
        }
        protected CoreBlockBuilder(AbstractRegistrate<?> owner, P parent, String name, BuilderCallback callback, NonNullFunction<BlockBehaviour.Properties, T> factory, NonNullSupplier<BlockBehaviour.Properties> initialProperties) {
            super(owner, parent, name, callback, factory, initialProperties);
        }
        @Override
        public CoreBlockBuilder<T, P> lang(net.minecraftforge.common.util.NonNullFunction<T, String> langKeyProvider) {
            return lang(langKeyProvider, (p, t) -> p.getAutomaticName(t, getRegistryKey()));
        }

        @Override
        public CoreBlockBuilder<T, P> lang(net.minecraftforge.common.util.NonNullFunction<T, String> langKeyProvider, String name) {
            return lang(langKeyProvider, (p, s) -> name);
        }

        public CoreBlockBuilder<T, P> lang(net.minecraftforge.common.util.NonNullFunction<T, String> langKeyProvider, NonNullBiFunction<DynamicLanguageProvider, NonNullSupplier<? extends T>, String> localizedNameProvider) {
            this.getOwner().addDataGenerator(DynamicLanguageProvider.TYPE, prov -> {
                DataGenContext<Block, T> ctx = DataGenContext.from(this, getRegistryKey());
                prov.setLanguageCode("en_us");
                prov.add(langKeyProvider.apply(ctx.getEntry()), localizedNameProvider.apply(prov, ctx::getEntry));
            });
            return this;
        }
    }
}
