package cat.jiu.core.mixin;

import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import cat.jiu.core.events.game.ResourceReloadEvent;

import net.minecraft.client.resources.SimpleReloadableResourceManager;

import net.minecraftforge.common.MinecraftForge;

@Mixin(Minecraft.class)
public class MixinMinecraft {
	private MixinMinecraft() {
		throw new RuntimeException();
	}
	
	@Inject(
		at = {@At(value = "HEAD")},
		method = {"refreshResources()V"}
	)
	private void mixin_preReload(CallbackInfo ci) {
		MinecraftForge.EVENT_BUS.post(new ResourceReloadEvent.Pre());
	}
	
	@Inject(
		at = {@At(value = "RETURN")},
		method = {"refreshResources()V"}
	)
	private void mixin_postReload(CallbackInfo ci) {
		MinecraftForge.EVENT_BUS.post(new ResourceReloadEvent.Post());
	}
}
