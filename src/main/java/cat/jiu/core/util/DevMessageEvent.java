package cat.jiu.core.util;

import cat.jiu.core.CoreMain;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;

@Mod.EventBusSubscriber
public class DevMessageEvent {
    public static final Marker DEV = MarkerFactory.getMarker("Dev");
    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public static void onScreenOpen(ScreenEvent.Opening event) {
        CoreMain.LOGGER.debug(DEV, "Screen: {}", event.getNewScreen());
    }
}
