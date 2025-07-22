package cat.jiu.core.util;

import net.minecraft.server.MinecraftServer;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.loading.FMLLoader;
import net.neoforged.neoforge.event.server.ServerStartedEvent;
import net.neoforged.neoforge.event.server.ServerStoppedEvent;

@EventBusSubscriber
public final class SideProxy {
    static boolean isServerClosed = true;
    static MinecraftServer server;

    public static Dist getSide() {
        return FMLLoader.getDist();
    }
    public static boolean isClient() {
        return getSide().isClient();
    }

    public static boolean isServerClosed() {
        return isServerClosed;
    }
    public static MinecraftServer getServer() {
        return server;
    }

    @SubscribeEvent
    public static void onServerStarting(ServerStartedEvent event) {
        isServerClosed = false;
        server = event.getServer();
    }
    @SubscribeEvent
    public static void onServerStopped(ServerStoppedEvent event) {
        isServerClosed = true;
        server = null;
    }
}
