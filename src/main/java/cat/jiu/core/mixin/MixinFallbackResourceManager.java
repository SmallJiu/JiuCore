package cat.jiu.core.mixin;

import java.io.IOException;
import java.io.InputStream;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import cat.jiu.core.JiuCore;

import net.minecraft.client.resources.FallbackResourceManager;
import net.minecraft.client.resources.IResourcePack;
import net.minecraft.util.ResourceLocation;

@Mixin(FallbackResourceManager.class)
public class MixinFallbackResourceManager {
	private MixinFallbackResourceManager() {
		throw new RuntimeException();
	}
	
	@Redirect(
		method = "getInputStream(Lnet/minecraft/util/ResourceLocation;Lnet/minecraft/client/resources/IResourcePack;)Ljava/io/InputStream;",
		at = @At(
				value = "INVOKE",
				target = "Lnet/minecraft/client/resources/IResourcePack;getInputStream(Lnet/minecraft/util/ResourceLocation;)Ljava/io/InputStream;"
			)
	)
	private InputStream mixin_getInputStream(IResourcePack resourcePack, ResourceLocation location) throws IOException {
		if(JiuCore.proxy !=null && JiuCore.proxy.getAsClientProxy().hasCustomResource(location)) {
			return JiuCore.proxy.getAsClientProxy().getCustomResource(location);
		}
		return resourcePack.getInputStream(location);
	}
}
