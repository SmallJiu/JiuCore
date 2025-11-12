package cat.jiu.core.mixin;

import cat.jiu.core.util.registry.DynamicLanguageProvider;
import com.tterrag.registrate.builders.AbstractBuilder;
import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateLangProvider;
import com.tterrag.registrate.util.nullness.NonNullBiFunction;
import com.tterrag.registrate.util.nullness.NonNullSupplier;
import net.minecraftforge.common.util.NonNullFunction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@SuppressWarnings({"unchecked"})
@Mixin(value = AbstractBuilder.class, remap = false)
public abstract class MixinRegistrateAbstractBuilder<R, T extends R, P, S extends AbstractBuilder<R, T, P, S>> {
    @Inject(
            at = @At("HEAD"),
            method = "lang(Lnet/minecraftforge/common/util/NonNullFunction;Lcom/tterrag/registrate/util/nullness/NonNullBiFunction;)Lcom/tterrag/registrate/builders/AbstractBuilder;",
            cancellable = true
    )
    private void lang(NonNullFunction<T, String> langKeyProvider, NonNullBiFunction<RegistrateLangProvider, NonNullSupplier<? extends T>, String> localizedNameProvider, CallbackInfoReturnable<S> cir) {
        S instance = (S)((Object)this);
        instance.getOwner().addDataGenerator(DynamicLanguageProvider.TYPE, prov -> {
            DataGenContext<R, T> ctx = DataGenContext.from(instance, instance.getRegistryKey());
            prov.setLanguageCode("en_us");
            prov.add(langKeyProvider.apply(ctx.getEntry()), localizedNameProvider.apply(prov, ctx::getEntry));
        });
        cir.setReturnValue(instance);
    }
}
