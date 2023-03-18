package cat.jiu.core.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import cat.jiu.core.events.game.ResourceReloadEvent;

import net.minecraft.client.resources.SimpleReloadableResourceManager;

import net.minecraftforge.common.MinecraftForge;

@Mixin(SimpleReloadableResourceManager.class)
public class MixinSimpleReloadableResourceManager {
	private MixinSimpleReloadableResourceManager() {
		throw new RuntimeException();
	}
	
	@Inject(
		at = {@At(value = "HEAD")},
		method = {"reloadResources(Ljava/util/List;)V"}
	)
	private void mixin_preReload(CallbackInfo ci) {
		MinecraftForge.EVENT_BUS.post(new ResourceReloadEvent.Pre());
	}
	
	@Inject(
		at = {@At(value = "RETURN")},
		method = {"reloadResources(Ljava/util/List;)V"}
	)
	private void mixin_postReload(CallbackInfo ci) {
		MinecraftForge.EVENT_BUS.post(new ResourceReloadEvent.Post());
	}
}
