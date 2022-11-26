package cat.jiu.core.test;

import cat.jiu.core.CoreLoggers;
import cat.jiu.core.api.events.mixin.clazz.game.WorldTimeChangeEvent;
import net.minecraft.command.CommandTime;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber
public class Test {
	@SubscribeEvent
	public static void onWorldTimeChange(WorldTimeChangeEvent event) {
		if(event.wantChangeTimeCallClass != CommandTime.class) {
			if(!event.isCanceled()) {
				event.setCanceled(true);
				return;
			}
		}else {
			CoreLoggers.getLogOS().info("OldWorldTime: " + event.oldTime + ", NewWorldTime: " + event.getNewWorldTime());
		}
	}
}
