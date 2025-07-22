package cat.jiu.core.util;

import cat.jiu.core.CoreMain;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ScreenEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;

@EventBusSubscriber
public class DevMessageEvent {
    public static final Marker DEV = MarkerFactory.getMarker("Dev");
    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public static void onScreenOpen(ScreenEvent.Opening event) {
        CoreMain.LOGGER.debug(DEV, "Screen: {}", event.getNewScreen());
    }

    @SubscribeEvent
    public static void onPlayerClone(PlayerEvent.Clone event) {
        CoreMain.LOGGER.debug(DEV, "Player clone. name: {}, wasDeath: {}, old uid: {}, new uid: {}", event.getEntity().getName().getString(), event.isWasDeath(), event.getOriginal().getStringUUID(), event.getEntity().getStringUUID());
    }

    @SubscribeEvent
    public static void onPlayerChangeDim(PlayerEvent.PlayerChangedDimensionEvent event) {
        CoreMain.LOGGER.debug(DEV, "Player change dimension. player: {}, from: {}, to: {}", event.getEntity().getName().getString(), event.getFrom(), event.getTo());
    }
}
