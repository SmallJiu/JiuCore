package cat.jiu.core.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;

import cat.jiu.core.events.game.WorldTimeChangeEvent;
import cat.jiu.core.util.JiuUtils;

import net.minecraft.world.World;
import net.minecraft.world.WorldProvider;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = World.class)
public abstract class MixinWorldInfo extends World {
	private MixinWorldInfo() {
		super(null, null, null, null, false);
		throw new RuntimeException();
	}
	WorldProvider provider;
	@Inject(
			at = {@At("HEAD")},
			method = "setWorldTime(J)V",
			cancellable = true
	)
	private void mixin_setWorldTime(long newTime, CallbackInfoReturnable<Long> cir) {
		WorldTimeChangeEvent event = new WorldTimeChangeEvent(this, JiuUtils.other.getCallingStack()[5], this.provider.getWorldTime(), newTime);
		if(!MinecraftForge.EVENT_BUS.post(event)) {
			cir.setReturnValue(event.getNewWorldTime());
		}
	}
}
