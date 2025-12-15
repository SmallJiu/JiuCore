package cat.jiu.core.util.registry;

import com.tterrag.registrate.AbstractRegistrate;
import com.tterrag.registrate.builders.*;
import com.tterrag.registrate.util.nullness.NonNullBiFunction;
import com.tterrag.registrate.util.nullness.NonNullFunction;
import com.tterrag.registrate.util.nullness.NonNullSupplier;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.MenuAccess;
import net.minecraft.core.Registry;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.client.extensions.common.IClientFluidTypeExtensions;
import net.minecraftforge.data.loading.DatagenModLoader;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.fluids.ForgeFlowingFluid;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class CoreRegistrate extends AbstractRegistrate<CoreRegistrate> {
    private final NonNullSupplier<Boolean> canGen = NonNullSupplier.lazy(DatagenModLoader::isRunningDataGen);
    protected CoreRegistrate(String modid) {
        super(modid);
    }
    public static CoreRegistrate create(String modid) {
        var ret = new CoreRegistrate(modid);
        ret.registerEventListeners(ret.getModEventBus());
        return ret;
    }

    public CoreRegistrate nullDefaultTab() {
        return this.defaultCreativeTab((ResourceKey<CreativeModeTab>) null);
    }

    private final NonNullSupplier<Map<String, List<Pair<String, String>>>> extraLang = NonNullSupplier.lazy(() -> {
        Map<String, List<Pair<String, String>>> result = new HashMap<>();
        addDataGenerator(DynamicLanguageProvider.TYPE, prov -> {
            prov.setLanguageCode("en_us");
            result.forEach((lang, values) -> {
                prov.setLanguageCode(lang);
                values.forEach(p ->
                        prov.add(p.getKey(), p.getValue())
                );
            });
        });
        return result;
    });

    @Override
    public MutableComponent addRawLang(String key, String value) {
        return this.addRawLang("en_us", key, value);
    }
    public MutableComponent addRawLang(String langCode, String key, String value) {
        if (canGen.get()) {
            if (!extraLang.get().containsKey(langCode)) {
                extraLang.get().put(langCode, new ArrayList<>());
            }
            extraLang.get().get(langCode).add(Pair.of(key, value));
        }
        return Component.translatable(key);
    }

    public static <R, T extends R, P, B extends AbstractBuilder<R, T, P, B>> B lang(B builder, net.minecraftforge.common.util.NonNullFunction<T, String> langKeyProvider) {
        return lang(builder, langKeyProvider, (p, t) -> p.getAutomaticName(t, builder.getRegistryKey()));
    }
    public static <R, T extends R, P, B extends AbstractBuilder<R, T, P, B>> B lang(B builder, net.minecraftforge.common.util.NonNullFunction<T, String> langKeyProvider, String name) {
        return lang(builder, langKeyProvider, (p, s) -> name);
    }
    public static <R, T extends R, P, B extends AbstractBuilder<R, T, P, B>> B lang(B builder, net.minecraftforge.common.util.NonNullFunction<T, String> langKeyProvider, NonNullBiFunction<DynamicLanguageProvider, NonNullSupplier<? extends T>, String> localizedNameProvider) {
        builder.setData(DynamicLanguageProvider.TYPE, (ctx, provider)->
            provider.add("en_us", langKeyProvider.apply(ctx.getEntry()), localizedNameProvider.apply(provider, ctx::getEntry))
        );
        return builder;
    }



    @Override
    public <R, T extends R, P> NoConfigBuilder<R, T, P> generic(P parent, String name, ResourceKey<Registry<R>> registryType, NonNullSupplier<T> factory) {
        return entry(name, callback -> this.generic(callback, parent, name, registryType, factory));
    }
    protected <R, T extends R, P> NoConfigBuilder<R, T, P> generic(BuilderCallback callback, P parent, String name, ResourceKey<Registry<R>> registryType, NonNullSupplier<T> factory) {
        return new NoConfigBuilder<>(this, parent, name, callback, registryType, factory) {
            @Override
            public NoConfigBuilder<R, T, P> lang(net.minecraftforge.common.util.NonNullFunction<T, String> langKeyProvider) {
                return CoreRegistrate.lang(this, langKeyProvider);
            }

            @Override
            public NoConfigBuilder<R, T, P> lang(net.minecraftforge.common.util.NonNullFunction<T, String> langKeyProvider, String name) {
                return CoreRegistrate.lang(this, langKeyProvider, name);
            }
        };
    }



    @Override
    public <T extends Item, P> ItemBuilder<T, P> item(P parent, String name, NonNullFunction<Item.Properties, T> factory) {
        return entry(name, callback -> this.item(callback, parent, name, factory));
    }
    protected <T extends Item, P> ItemBuilder<T, P> item(BuilderCallback callback, P parent, String name, NonNullFunction<Item.Properties, T> factory) {
        return CoreItemBuilder.create(this, parent, name, callback, factory).transform(builder -> builder);
    }
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
            return CoreRegistrate.lang(this, langKeyProvider);
        }
        @Override
        public ItemBuilder<T, P> lang(net.minecraftforge.common.util.NonNullFunction<T, String> langKeyProvider, String name) {
            return CoreRegistrate.lang(this, langKeyProvider, name);
        }
    }



    @Override
    public <T extends Block, P> BlockBuilder<T, P> block(P parent, String name, NonNullFunction<BlockBehaviour.Properties, T> factory) {
        return entry(name, callback -> this.block(callback, parent, name, factory));
    }
    protected <T extends Block, P> BlockBuilder<T, P> block(BuilderCallback callback, P parent, String name, NonNullFunction<BlockBehaviour.Properties, T> factory) {
        return CoreBlockBuilder.create(this, parent, name, callback, factory);
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
        public BlockBuilder<T, P> lang(net.minecraftforge.common.util.NonNullFunction<T, String> langKeyProvider) {
            return CoreRegistrate.lang(this, langKeyProvider);
        }

        @Override
        public BlockBuilder<T, P> lang(net.minecraftforge.common.util.NonNullFunction<T, String> langKeyProvider, String name) {
            return CoreRegistrate.lang(this, langKeyProvider, name);
        }
    }



    @Override
    public <T extends Entity, P> EntityBuilder<T, P> entity(P parent, String name, EntityType.EntityFactory<T> factory, MobCategory classification) {
        return entry(name, callback -> this.entity(callback, parent, name, factory, classification));
    }
    protected  <T extends Entity, P> EntityBuilder<T, P> entity(BuilderCallback callback, P parent, String name, EntityType.EntityFactory<T> factory, MobCategory classification) {
        return CoreEntityBuilder.create(this, parent, name, callback, factory, classification);
    }
    public static class CoreEntityBuilder<T extends Entity, P> extends EntityBuilder<T, P> {
        public static <T extends Entity, P> EntityBuilder<T, P> create(AbstractRegistrate<?> owner, P parent, String name, BuilderCallback callback, EntityType.EntityFactory<T> factory, MobCategory classification) {
            return new CoreEntityBuilder<>(owner, parent, name, callback, factory, classification)
                    .defaultLang();
        }
        protected CoreEntityBuilder(AbstractRegistrate<?> owner, P parent, String name, BuilderCallback callback, EntityType.EntityFactory<T> factory, MobCategory classification) {
            super(owner, parent, name, callback, factory, classification);
        }
        @Override
        public EntityBuilder<T, P> lang(net.minecraftforge.common.util.NonNullFunction<EntityType<T>, String> langKeyProvider) {
            return CoreRegistrate.lang(this, langKeyProvider);
        }
        @Override
        public EntityBuilder<T, P> lang(net.minecraftforge.common.util.NonNullFunction<EntityType<T>, String> langKeyProvider, String name) {
            return CoreRegistrate.lang(this, langKeyProvider, name);
        }
    }



    @Override
    public <T extends BlockEntity, P> BlockEntityBuilder<T, P> blockEntity(P parent, String name, BlockEntityBuilder.BlockEntityFactory<T> factory) {
        return entry(name, callback -> this.blockEntity(callback, parent, name, factory));
    }
    protected  <T extends BlockEntity, P> BlockEntityBuilder<T, P> blockEntity(BuilderCallback callback, P parent, String name, BlockEntityBuilder.BlockEntityFactory<T> factory) {
        return CoreBlockEntityBuilder.create(this, parent, name, callback, factory);
    }
    public static class CoreBlockEntityBuilder<T extends BlockEntity, P> extends BlockEntityBuilder<T, P> {
        public static <T extends BlockEntity, P> BlockEntityBuilder<T, P> create(AbstractRegistrate<?> owner, P parent, String name, BuilderCallback callback, BlockEntityFactory<T> factory) {
            return new CoreBlockEntityBuilder<>(owner, parent, name, callback, factory);
        }
        protected CoreBlockEntityBuilder(AbstractRegistrate<?> owner, P parent, String name, BuilderCallback callback, BlockEntityFactory<T> factory) {
            super(owner, parent, name, callback, factory);
        }
        @Override
        public BlockEntityBuilder<T, P> lang(net.minecraftforge.common.util.NonNullFunction<BlockEntityType<T>, String> langKeyProvider) {
            return CoreRegistrate.lang(this, langKeyProvider);
        }

        @Override
        public BlockEntityBuilder<T, P> lang(net.minecraftforge.common.util.NonNullFunction<BlockEntityType<T>, String> langKeyProvider, String name) {
            return CoreRegistrate.lang(this, langKeyProvider, name);
        }
    }



    @Override
    public <P> FluidBuilder<ForgeFlowingFluid.Flowing, P> fluid(P parent, String name, ResourceLocation stillTexture, ResourceLocation flowingTexture) {
        return entry(name, callback -> this.fluid(callback, parent, name, stillTexture, flowingTexture));
    }
    protected <P> FluidBuilder<ForgeFlowingFluid.Flowing, P> fluid(BuilderCallback callback, P parent, String name, ResourceLocation stillTexture, ResourceLocation flowingTexture) {
        return CoreFluidBuilder.create(this, parent, name, callback, stillTexture, flowingTexture);
    }
    @Override
    public <P> FluidBuilder<ForgeFlowingFluid.Flowing, P> fluid(P parent, String name, ResourceLocation stillTexture, ResourceLocation flowingTexture, FluidBuilder.FluidTypeFactory typeFactory) {
        return entry(name, callback -> this.fluid(callback, parent, name, stillTexture, flowingTexture, typeFactory));
    }
    protected <P> FluidBuilder<ForgeFlowingFluid.Flowing, P> fluid(BuilderCallback callback, P parent, String name, ResourceLocation stillTexture, ResourceLocation flowingTexture, FluidBuilder.FluidTypeFactory typeFactory) {
        return CoreFluidBuilder.create(this, parent, name, callback, stillTexture, flowingTexture, typeFactory);
    }
    @Override
    public <P> FluidBuilder<ForgeFlowingFluid.Flowing, P> fluid(P parent, String name, ResourceLocation stillTexture, ResourceLocation flowingTexture, NonNullSupplier<FluidType> fluidType) {
        return entry(name, callback -> this.fluid(callback, parent, name, stillTexture, flowingTexture, fluidType));
    }
    protected <P> FluidBuilder<ForgeFlowingFluid.Flowing, P> fluid(BuilderCallback callback, P parent, String name, ResourceLocation stillTexture, ResourceLocation flowingTexture, NonNullSupplier<FluidType> fluidType) {
        return CoreFluidBuilder.create(this, parent, name, callback, stillTexture, flowingTexture, fluidType);
    }
    @Override
    public <T extends ForgeFlowingFluid, P> FluidBuilder<T, P> fluid(P parent, String name, ResourceLocation stillTexture, ResourceLocation flowingTexture, NonNullFunction<ForgeFlowingFluid.Properties, T> fluidFactory) {
        return entry(name, callback -> this.fluid(callback, parent, name, stillTexture, flowingTexture, fluidFactory));
    }
    protected <T extends ForgeFlowingFluid, P> FluidBuilder<T, P> fluid(BuilderCallback callback, P parent, String name, ResourceLocation stillTexture, ResourceLocation flowingTexture, NonNullFunction<ForgeFlowingFluid.Properties, T> fluidFactory) {
        return CoreFluidBuilder.create(this, parent, name, callback, stillTexture, flowingTexture, fluidFactory);
    }
    @Override
    public <T extends ForgeFlowingFluid, P> FluidBuilder<T, P> fluid(P parent, String name, ResourceLocation stillTexture, ResourceLocation flowingTexture, FluidBuilder.FluidTypeFactory typeFactory, NonNullFunction<ForgeFlowingFluid.Properties, T> fluidFactory) {
        return entry(name, callback -> this.fluid(callback, parent, name, stillTexture, flowingTexture, fluidFactory));
    }
    protected <T extends ForgeFlowingFluid, P> FluidBuilder<T, P> fluid(BuilderCallback callback, P parent, String name, ResourceLocation stillTexture, ResourceLocation flowingTexture, FluidBuilder.FluidTypeFactory typeFactory, NonNullFunction<ForgeFlowingFluid.Properties, T> fluidFactory) {
        return CoreFluidBuilder.create(this, parent, name, callback, stillTexture, flowingTexture, typeFactory, fluidFactory);
    }
    @Override
    public <T extends ForgeFlowingFluid, P> FluidBuilder<T, P> fluid(P parent, String name, ResourceLocation stillTexture, ResourceLocation flowingTexture, NonNullSupplier<FluidType> fluidType, NonNullFunction<ForgeFlowingFluid.Properties, T> fluidFactory) {
        return entry(name, callback -> this.fluid(callback, parent, name, stillTexture, flowingTexture, fluidType, fluidFactory));
    }
    protected <T extends ForgeFlowingFluid, P> FluidBuilder<T, P> fluid(BuilderCallback callback, P parent, String name, ResourceLocation stillTexture, ResourceLocation flowingTexture, NonNullSupplier<FluidType> fluidType, NonNullFunction<ForgeFlowingFluid.Properties, T> fluidFactory) {
        return CoreFluidBuilder.create(this, parent, name, callback, stillTexture, flowingTexture, fluidType, fluidFactory);
    }
    public static class CoreFluidBuilder<T extends ForgeFlowingFluid, P> extends FluidBuilder<T, P> {
        public static <P> FluidBuilder<ForgeFlowingFluid.Flowing, P> create(AbstractRegistrate<?> owner, P parent, String name, BuilderCallback callback, ResourceLocation stillTexture, ResourceLocation flowingTexture) {
            return create(owner, parent, name, callback, stillTexture, flowingTexture, CoreFluidBuilder::defaultFluidType, ForgeFlowingFluid.Flowing::new);
        }
        public static <P> FluidBuilder<ForgeFlowingFluid.Flowing, P> create(AbstractRegistrate<?> owner, P parent, String name, BuilderCallback callback, ResourceLocation stillTexture, ResourceLocation flowingTexture, FluidTypeFactory typeFactory) {
            return create(owner, parent, name, callback, stillTexture, flowingTexture, typeFactory, ForgeFlowingFluid.Flowing::new);
        }
        public static <P> FluidBuilder<ForgeFlowingFluid.Flowing, P> create(AbstractRegistrate<?> owner, P parent, String name, BuilderCallback callback, ResourceLocation stillTexture, ResourceLocation flowingTexture, NonNullSupplier<FluidType> fluidType) {
            return create(owner, parent, name, callback, stillTexture, flowingTexture, fluidType, ForgeFlowingFluid.Flowing::new);
        }
        public static <T extends ForgeFlowingFluid, P> FluidBuilder<T, P> create(AbstractRegistrate<?> owner, P parent, String name, BuilderCallback callback, ResourceLocation stillTexture, ResourceLocation flowingTexture, NonNullFunction<ForgeFlowingFluid.Properties, T> fluidFactory) {
            return create(owner, parent, name, callback, stillTexture, flowingTexture, CoreFluidBuilder::defaultFluidType, fluidFactory);
        }
        public static <T extends ForgeFlowingFluid, P> FluidBuilder<T, P> create(AbstractRegistrate<?> owner, P parent, String name, BuilderCallback callback, ResourceLocation stillTexture, ResourceLocation flowingTexture, FluidTypeFactory typeFactory, NonNullFunction<ForgeFlowingFluid.Properties, T> fluidFactory) {
            return new CoreFluidBuilder<>(owner, parent, name, callback, stillTexture, flowingTexture, typeFactory, fluidFactory)
                    .defaultLang().defaultSource().defaultBlock().defaultBucket();
        }
        public static FluidType defaultFluidType(FluidType.Properties properties, ResourceLocation stillTexture, ResourceLocation flowingTexture) {
            return new FluidType(properties) {
                @Override
                public void initializeClient(Consumer<IClientFluidTypeExtensions> consumer) {
                    consumer.accept(new IClientFluidTypeExtensions() {
                        @Override
                        public ResourceLocation getStillTexture() {
                            return stillTexture;
                        }

                        @Override
                        public ResourceLocation getFlowingTexture() {
                            return flowingTexture;
                        }
                    });
                }
            };
        }
        public CoreFluidBuilder(AbstractRegistrate<?> owner, P parent, String name, BuilderCallback callback, ResourceLocation stillTexture, ResourceLocation flowingTexture, NonNullSupplier<FluidType> fluidType, NonNullFunction<ForgeFlowingFluid.Properties, T> fluidFactory) {
            super(owner, parent, name, callback, stillTexture, flowingTexture, fluidType, fluidFactory);
        }
        public CoreFluidBuilder(AbstractRegistrate<?> owner, P parent, String name, BuilderCallback callback, ResourceLocation stillTexture, ResourceLocation flowingTexture, FluidTypeFactory typeFactory, NonNullFunction<ForgeFlowingFluid.Properties, T> fluidFactory) {
            super(owner, parent, name, callback, stillTexture, flowingTexture, typeFactory, fluidFactory);
        }
        @Override
        public FluidBuilder<T, P> lang(net.minecraftforge.common.util.NonNullFunction<T, String> langKeyProvider) {
            return CoreRegistrate.lang(this, langKeyProvider);
        }
        @Override
        public FluidBuilder<T, P> lang(net.minecraftforge.common.util.NonNullFunction<T, String> langKeyProvider, String name) {
            return CoreRegistrate.lang(this, langKeyProvider, name);
        }
    }



    @Override
    public <T extends AbstractContainerMenu, SC extends Screen & MenuAccess<T>, P> MenuBuilder<T, SC, P> menu(P parent, String name, MenuBuilder.MenuFactory<T> factory, NonNullSupplier<MenuBuilder.ScreenFactory<T, SC>> screenFactory) {
        return entry(name, callback -> this.menu(callback, parent, name, factory, screenFactory));
    }
    protected <T extends AbstractContainerMenu, SC extends Screen & MenuAccess<T>, P> MenuBuilder<T, SC, P> menu(BuilderCallback callback, P parent, String name, MenuBuilder.MenuFactory<T> factory, NonNullSupplier<MenuBuilder.ScreenFactory<T, SC>> screenFactory) {
        return new MenuBuilder<>(this, parent, name, callback, factory, screenFactory){
            @Override
            public MenuBuilder<T, SC, P> lang(net.minecraftforge.common.util.NonNullFunction<MenuType<T>, String> langKeyProvider) {
                return CoreRegistrate.lang(this, langKeyProvider);
            }
            @Override
            public MenuBuilder<T, SC, P> lang(net.minecraftforge.common.util.NonNullFunction<MenuType<T>, String> langKeyProvider, String name) {
                return CoreRegistrate.lang(this, langKeyProvider, name);
            }
        };
    }
    @Override
    public <T extends AbstractContainerMenu, SC extends Screen & MenuAccess<T>, P> MenuBuilder<T, SC, P> menu(P parent, String name, MenuBuilder.ForgeMenuFactory<T> factory, NonNullSupplier<MenuBuilder.ScreenFactory<T, SC>> screenFactory) {
        return entry(name, callback -> this.menu(callback, parent, name, factory, screenFactory));
    }
    protected <T extends AbstractContainerMenu, SC extends Screen & MenuAccess<T>, P> MenuBuilder<T, SC, P> menu(BuilderCallback callback, P parent, String name, MenuBuilder.ForgeMenuFactory<T> factory, NonNullSupplier<MenuBuilder.ScreenFactory<T, SC>> screenFactory) {
        return new MenuBuilder<>(this, parent, name, callback, factory, screenFactory){
            @Override
            public MenuBuilder<T, SC, P> lang(net.minecraftforge.common.util.NonNullFunction<MenuType<T>, String> langKeyProvider) {
                return CoreRegistrate.lang(this, langKeyProvider);
            }
            @Override
            public MenuBuilder<T, SC, P> lang(net.minecraftforge.common.util.NonNullFunction<MenuType<T>, String> langKeyProvider, String name) {
                return CoreRegistrate.lang(this, langKeyProvider, name);
            }
        };
    }



    @Override
    public <T extends Enchantment, P> EnchantmentBuilder<T, P> enchantment(P parent, String name, EnchantmentCategory type, EnchantmentBuilder.EnchantmentFactory<T> factory) {
        return entry(name, callback -> this.enchantment(callback, parent, name, type, factory));
    }
    protected <T extends Enchantment, P> EnchantmentBuilder<T, P> enchantment(BuilderCallback callback, P parent, String name, EnchantmentCategory type, EnchantmentBuilder.EnchantmentFactory<T> factory) {
        return CoreEnchantmentBuilder.create(this, parent, name, callback, type, factory);
    }
    public static class CoreEnchantmentBuilder<T extends Enchantment, P> extends EnchantmentBuilder<T, P> {
        public static <T extends Enchantment, P> EnchantmentBuilder<T, P> create(AbstractRegistrate<?> owner, P parent, String name, BuilderCallback callback, EnchantmentCategory type, EnchantmentFactory<T> factory) {
            return new CoreEnchantmentBuilder<>(owner, parent, name, callback, type, factory)
                    .defaultLang();
        }
        protected CoreEnchantmentBuilder(AbstractRegistrate<?> owner, P parent, String name, BuilderCallback callback, EnchantmentCategory type, EnchantmentFactory<T> factory) {
            super(owner, parent, name, callback, type, factory);
        }
        @Override
        public EnchantmentBuilder<T, P> lang(net.minecraftforge.common.util.NonNullFunction<T, String> langKeyProvider) {
            return CoreRegistrate.lang(this, langKeyProvider);
        }
        @Override
        public EnchantmentBuilder<T, P> lang(net.minecraftforge.common.util.NonNullFunction<T, String> langKeyProvider, String name) {
            return CoreRegistrate.lang(this, langKeyProvider, name);
        }
    }
}
