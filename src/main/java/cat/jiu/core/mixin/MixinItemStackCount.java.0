package cat.jiu.core.mixin;

import cat.jiu.core.CoreMain;
import cat.jiu.core.util.DevMessageEvent;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = ItemStack.class, remap = !CoreMain.DEV)
public class MixinItemStackCount {

    @Inject(
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/nbt/CompoundTag;getByte(Ljava/lang/String;)B",
                    shift = At.Shift.AFTER
            ),
            method = "<init>(Lnet/minecraft/nbt/CompoundTag;)V",
            cancellable = true
    )
    private void setCount_init(CallbackInfo ci) {
        ItemStack stack = ((ItemStack)(Object)this);
//        stack.setCount(nbt.getInt("count"));
        CoreMain.LOGGER.debug(DevMessageEvent.DEV, "stack: {}", stack);
    }

    @Inject(
            at = @At("RETURN"),
            method = "save(Lnet/minecraft/nbt/CompoundTag;)Lnet/minecraft/nbt/CompoundTag;"
    )
    private void saveCount(CallbackInfo ci) {

    }
}
