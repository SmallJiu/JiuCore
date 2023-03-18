package cat.jiu.core.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import cat.jiu.core.events.game.WorldTimeChangeEvent;
import cat.jiu.core.util.JiuUtils;

import net.minecraft.world.World;
import net.minecraft.world.WorldProvider;
import net.minecraftforge.common.MinecraftForge;

@Mixin(value = World.class)
public class MixinWorldInfo {
	private MixinWorldInfo() {
		throw new RuntimeException();
	}
	
	@Redirect(
		method = "setWorldTime(J)V",
		at = @At(
				value = "INVOKE",
				target = "Lnet/minecraft/world/WorldProvider;setWorldTime(J)V"
			)
	)
	private void mixin_setWorldTime(WorldProvider provider, long newTime) {
		WorldTimeChangeEvent event = new WorldTimeChangeEvent(JiuUtils.other.getCallingStack()[5], provider.getWorldTime(), newTime);
		if(!MinecraftForge.EVENT_BUS.post(event)) {
			provider.setWorldTime(event.getNewWorldTime());
		}
	}
}
