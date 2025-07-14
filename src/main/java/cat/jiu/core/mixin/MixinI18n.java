package cat.jiu.core.mixin;

import cat.jiu.core.CoreMain;
import cat.jiu.core.event.client.TextFormatEvent;
import net.minecraft.client.resources.language.I18n;
import net.neoforged.neoforge.common.NeoForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = I18n.class, remap = !CoreMain.DEV)
public class MixinI18n {
    @Inject(
            at = @At("HEAD"),
            method = "get(Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;",
            cancellable = true
    )
    private static void mixin_get(String translateKey, Object[] parameters, CallbackInfoReturnable<String> cir) {
        TextFormatEvent event = new TextFormatEvent(translateKey, parameters);
        if(NeoForge.EVENT_BUS.post(event).isCanceled() && event.getFormatResult() != null) {
            cir.setReturnValue(event.getFormatResult());
        }
    }
}
