package cat.jiu.core.mixin;

import java.util.List;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import cat.jiu.core.api.events.mixin.clazz.game.TileEntityUpdataEvent;
import cat.jiu.core.api.events.mixin.iface.game.ITileEntityUpdataPost;
import cat.jiu.core.api.events.mixin.iface.game.ITileEntityUpdataPre;
import cat.jiu.core.util.JiuCoreEvents;

import net.minecraft.tileentity.TileEntity;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.server.timings.TimeTracker;

@Mixin(value = TimeTracker.class, remap = false)
public class MixinTileEntityUpdataEvent<T> {
	private MixinTileEntityUpdataEvent() {
		throw new RuntimeException();
	}

	@Inject(
		at = {@At(value = "HEAD")},
		method = {"trackStart(Ljava/lang/Object;)V"}
	)
	private void mixin_onPre(T t, CallbackInfo ci) {
		if(t instanceof TileEntity) {
			TileEntity tile = (TileEntity) t;
			MinecraftForge.EVENT_BUS.post(new TileEntityUpdataEvent.Pre(tile));
			List<ITileEntityUpdataPre> l = JiuCoreEvents.getEvents(ITileEntityUpdataPre.class);
			if(l != null) l.stream().forEach(e -> e.onPre(tile));
		}
	}
	
	@Inject(
		at = {@At(value = "RETURN")},
		method = {"trackEnd(Ljava/lang/Object;)V"}
	)
	private void mixin_onPost(T t, CallbackInfo ci) {
		if(t instanceof TileEntity) {
			TileEntity tile = (TileEntity) t;
			MinecraftForge.EVENT_BUS.post(new TileEntityUpdataEvent.Post(tile));
			List<ITileEntityUpdataPost> l = JiuCoreEvents.getEvents(ITileEntityUpdataPost.class);
			if(l != null) l.stream().forEach(e -> e.onPost(tile));
		}
	}
	
	/*
	@Inject(
		at = {@At(value = "INVOKE", target = "Lnet/minecraftforge/server/timings/TimeTracker;trackStart(Ljava/lang/Object;)V")},
		method = {"updateEntities()V"},
		slice = @Slice(
			from = @At(value = "INVOKE", target = "Lnet/minecraft/profiler/Profiler;func_194340_a()V"),
			to = @At(value = "INVOKE", target = "Lnet/minecraftforge/server/timings/TimeTracker;trackStart(Ljava/lang/Object;)V")
		)
	)
	private void onPre(CallbackInfo ci) {
		System.out.println("Updata TileEntity Pre");
	}
	
	@Inject(
		at = {@At(value = "INVOKE", target = "Lnet/minecraft/profiler/Profiler;endSection()V")},
		method = {"updateEntities()V"},
		slice = @Slice(
			from = @At(value = "INVOKE", target = "Lnet/minecraftforge/server/timings/TimeTracker;trackEnd(Ljava/lang/Object;)V"),
			to = @At(value = "INVOKE", target = "Lnet/minecraft/profiler/Profiler;endSection()V")
		)
	)
	private void onPost(CallbackInfo ci) {
		System.out.println("Updata TileEntity Post");
	}
	*/
}
