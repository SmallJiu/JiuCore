package com.example.examplemod;

import com.tterrag.registrate.Registrate;
import com.tterrag.registrate.util.entry.ItemEntry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import net.minecraft.client.Minecraft;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.server.ServerStartingEvent;

//@Mod(ExampleMod.MODID)
public class ExampleMod {
    public static final String MODID = "examplemod";
    private static final Logger LOGGER = LogUtils.getLogger();

    private static ModContainer modContainer;
    public static ModContainer container() {
        return modContainer;
    }
    private static IEventBus modBus;
    public static IEventBus bus() {
        return modBus;
    }

    private static final Registrate registrate = Registrate.create(MODID).defaultCreativeTab((ResourceKey<CreativeModeTab>) null);
    public static Registrate registrate() {
        return registrate;
    }

    public static final ItemEntry<Item> EXAMPLE_ITEM = registrate()
            .object("example_item")
            .item(Item::new)
            .tab(CreativeModeTabs.TOOLS_AND_UTILITIES)
            .properties(p->p.stacksTo(1))
            .register();

    public ExampleMod(IEventBus modBus, ModContainer container) {
        ExampleMod.modBus = modBus;
        ExampleMod.modContainer = container;

        bus().addListener(this::setup);
        bus().addListener(this::clientSetup);
        container().registerConfig(ModConfig.Type.COMMON, Config.SPEC);

        NeoForge.EVENT_BUS.register(this);
    }

    private void setup(final FMLCommonSetupEvent event) {
        LOGGER.info("HELLO FROM COMMON SETUP");
    }
    private void clientSetup(FMLClientSetupEvent event) {
        LOGGER.info("HELLO FROM CLIENT SETUP");
        LOGGER.info("MINECRAFT NAME >> {}", Minecraft.getInstance().getUser().getName());
    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        LOGGER.info("HELLO from server starting");
    }
}
