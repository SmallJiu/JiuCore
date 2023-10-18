package cat.jiu.core.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import cat.jiu.core.events.client.TextFormatEvent;

import net.minecraft.client.resources.Locale;

import net.minecraftforge.common.MinecraftForge;

@Mixin(value = Locale.class)
public class MixinLocale {
	private MixinLocale() {
		throw new RuntimeException();
	}
	@Inject(
		at = {@At("HEAD")},
		method = "formatMessage(Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;",
		cancellable = true
	)
	private void mixin_formatMessage(String translateKey, Object[] parameters, CallbackInfoReturnable<String> cir) {
		TextFormatEvent event = new TextFormatEvent(translateKey, parameters);
		if(MinecraftForge.EVENT_BUS.post(event) && event.getFormatResult() != null) {
			cir.setReturnValue(event.getFormatResult());
		}
	}
}
