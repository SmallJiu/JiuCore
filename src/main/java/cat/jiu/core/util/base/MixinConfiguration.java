package cat.jiu.core.util.base;

import java.util.Map;

import org.spongepowered.asm.launch.MixinBootstrap;
import org.spongepowered.asm.mixin.Mixins;

import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;

public class MixinConfiguration implements IFMLLoadingPlugin {
	public MixinConfiguration(String... file) {
		MixinBootstrap.init();
		Mixins.addConfigurations(file);
	}
	public String[] getASMTransformerClass() {return null;}
	public String getModContainerClass() {return null;}
	public String getSetupClass() {return null;}
	public void injectData(Map<String, Object> data) {}
	public String getAccessTransformerClass() {return null;}
}
