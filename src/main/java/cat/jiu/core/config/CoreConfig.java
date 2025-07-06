package cat.jiu.core.config;

import cat.jiu.core.CoreMain;
import cat.jiu.core.util.client.config.entry.EnumEntry;
import cat.jiu.core.util.client.config.entry.ListEntry;
import net.minecraft.sounds.SoundSource;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.ConfigScreenHandler;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;

public class CoreConfig {
    public static final ForgeConfigSpec CONFIG_MAIN;
    public static final ConfigExample CONFIG_EXAMPLE;

    static {
        ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();

        CONFIG_EXAMPLE = new ConfigExample(builder);

        CONFIG_MAIN = builder.build();
    }

    public static void registerConfig() {
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, CoreConfig.CONFIG_MAIN, "jiu/core/configs.toml");
    }

    @OnlyIn(Dist.CLIENT)
    public static void registerConfigScreen() {
        ModLoadingContext.get().registerExtensionPoint(ConfigScreenHandler.ConfigScreenFactory.class, ()->new ConfigScreenHandler.ConfigScreenFactory((mc, parent)->
//                new cat.jiu.core.util.client.config.GuiConfig("/config/jiu/core/configs.toml", parent, CoreConfig.CONFIG_MAIN)
//                new cat.jiu.core.util.client.config.GuiConfig(parent, CoreMain.MODID)
                new cat.jiu.core.util.client.config.GuiConfig(parent)
        ));
        EnumEntry.registerNameGetter(SoundSource.class, source -> "soundCategory." + source.getName());
    }
}
