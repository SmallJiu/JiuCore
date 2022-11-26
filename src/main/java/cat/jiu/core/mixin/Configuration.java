package cat.jiu.core.mixin;

import cat.jiu.core.CoreLoggers;
import cat.jiu.core.util.base.MixinConfiguration;

import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;

@IFMLLoadingPlugin.MCVersion(value = "1.12.2")
public class Configuration extends MixinConfiguration {
	public Configuration() {
		super("jiucore.mixin.json");
		CoreLoggers.getLogOS().info("Hello Mixin!");
	}
}
