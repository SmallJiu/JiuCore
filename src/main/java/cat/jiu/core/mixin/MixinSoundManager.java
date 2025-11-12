package cat.jiu.core.mixin;

import cat.jiu.core.util.client.AudioSystem;
import net.minecraft.client.sounds.SoundManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = SoundManager.class, remap = true)
public class MixinSoundManager {

    @Inject(
            at = @At("RETURN"),
            method = "pause()V"
    )
    private void onPauseGame(CallbackInfo ci) {
        AudioSystem.pauseAll();
    }

    @Inject(
            at = @At("RETURN"),
            method = "resume()V"
    )
    private void onResumeSound(CallbackInfo ci){
        AudioSystem.startAll();
    }
}
