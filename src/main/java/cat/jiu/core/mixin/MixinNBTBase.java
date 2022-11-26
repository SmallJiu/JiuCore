package cat.jiu.core.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.CancellationException;

import cat.jiu.core.util.base.BaseNBT;
import net.minecraft.nbt.NBTBase;

@Mixin(value = NBTBase.class)
public class MixinNBTBase {
	private MixinNBTBase() {
		throw new RuntimeException();
	}
	@Inject(
		at = {@At(value = "HEAD")},
		method = {"createNewByType(B)Lnet/minecraft/nbt/NBTBase;"},
		cancellable = true
	)
	private static void mixin_createNewByType(byte id, CallbackInfoReturnable<NBTBase> cir) {
		if(BaseNBT.hasNBT(id)) {
			try {
				cir.setReturnValue(BaseNBT.getNBTInfo(id).clazz.newInstance());
			}catch(CancellationException | IllegalAccessException | InstantiationException e) {
				e.printStackTrace();
			}
		}
	}
	
	@Inject(
		at = {@At(value = "HEAD")},
		method = {"getTagTypeName(I)Ljava/lang/String;"},
		cancellable = true
	)
	private static void mixin_getTagTypeName(int id, CallbackInfoReturnable<String> cir) {
		if(BaseNBT.hasNBT(id)) {
			cir.setReturnValue(BaseNBT.getNBTInfo(id).type);
		}
	}
}
