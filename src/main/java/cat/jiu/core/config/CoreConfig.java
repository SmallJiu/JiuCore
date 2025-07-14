package cat.jiu.core.config;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.neoforged.neoforge.common.ModConfigSpec;

public class CoreConfig {
    public static final ModConfigSpec CONFIG_MAIN;
    public static final ConfigExample CONFIG_EXAMPLE;

    static {
        ModConfigSpec.Builder builder = new ModConfigSpec.Builder();

        CONFIG_EXAMPLE = new ConfigExample(builder);

        CONFIG_MAIN = builder.build();
    }

    public static void registerConfig(ModContainer container) {
        container.registerConfig(ModConfig.Type.COMMON, CoreConfig.CONFIG_MAIN, "jiu/core/configs.toml");
    }

    @OnlyIn(Dist.CLIENT)
    public static void registerConfigScreen(ModContainer container) {
        container.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
    }
}
