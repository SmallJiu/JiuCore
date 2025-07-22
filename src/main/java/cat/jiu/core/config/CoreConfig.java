package cat.jiu.core.config;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.neoforged.neoforge.common.ModConfigSpec;

public class CoreConfig {
    public static final ModConfigSpec CONFIG_COMMON;
    public static final ConfigExample CONFIG_EXAMPLE;

    public static final ModConfigSpec CONFIG_SERVER;
    public static final ModConfigSpec.IntValue max_charing_energy;

    static {
        {
            ModConfigSpec.Builder builder = new ModConfigSpec.Builder();

            CONFIG_EXAMPLE = new ConfigExample(builder);

            CONFIG_COMMON = builder.build();
        }

        {
            ModConfigSpec.Builder builder = new ModConfigSpec.Builder();

            max_charing_energy = builder
                    .comment("item infinite_energy max charing energy")
                    .defineInRange("max_charing_energy", 10000000, 1, Integer.MAX_VALUE);

            CONFIG_SERVER = builder.build();
        }

    }

    public static void registerConfig(ModContainer container) {
        container.registerConfig(ModConfig.Type.COMMON, CoreConfig.CONFIG_COMMON, "jiu/core/configs.toml");
        container.registerConfig(ModConfig.Type.SERVER, CoreConfig.CONFIG_SERVER, "jiu/core/configs-server.toml");
    }

    @OnlyIn(Dist.CLIENT)
    public static void registerConfigScreen(ModContainer container) {
        container.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
    }
}
