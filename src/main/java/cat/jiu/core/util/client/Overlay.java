package cat.jiu.core.util.client;

import cat.jiu.core.api.element.IOverlay;
import cat.jiu.core.util.registry.StaticRegistry;
import com.mojang.blaze3d.platform.Window;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.RenderGuiEvent;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@OnlyIn(Dist.CLIENT)
@Mod.EventBusSubscriber(Dist.CLIENT)
public class Overlay {
    public static final ResourceLocation
            ALL_OVERLAY = new ResourceLocation("all"),
            KEY_TYPED   = new ResourceLocation("key_typed");
    public static final StaticRegistry<Class<?>, IOverlay> REGISTRY = new StaticRegistry<>();
    public static <T extends IOverlay> T register(T overlayInstance) {
        REGISTRY.register(overlayInstance);
        return overlayInstance;
    }

    @SubscribeEvent
    public static void onPreOverlayRender(RenderGuiEvent.Pre event) {
        renderOverlay(event.getWindow(), event.getGuiGraphics(), event.getPartialTick(), ALL_OVERLAY, true);
    }

    @SubscribeEvent
    public static void onPostOverlayRender(RenderGuiEvent.Post  event) {
        renderOverlay(event.getWindow(), event.getGuiGraphics(), event.getPartialTick(), ALL_OVERLAY, false);
    }

    @SubscribeEvent
    public static void onPreNameOverlayRender(RenderGuiOverlayEvent.Pre event) {
        renderOverlay(event.getWindow(), event.getGuiGraphics(), event.getPartialTick(), event.getOverlay().id(), true);
    }

    @SubscribeEvent
    public static void onPostNameOverlayRender(RenderGuiOverlayEvent.Post event) {
        renderOverlay(event.getWindow(), event.getGuiGraphics(), event.getPartialTick(), event.getOverlay().id(), false);
    }

    public static void renderOverlay(Window window, GuiGraphics graphics, float partialTick, ResourceLocation overlayName, boolean pre) {
        if (REGISTRY.hasEntry()) {
            ForgeGui hud = (ForgeGui) Minecraft.getInstance().gui;
            IOverlay.EventAction event = pre ? IOverlay.EventAction.RENDER_PRE : IOverlay.EventAction.RENDER_POST;
            for (Class<?> id : REGISTRY.getIDs()) {
                IOverlay overlay = REGISTRY.get(id);
                if (overlay.isEnable(overlayName, event) && overlay.render(hud, window, graphics, partialTick, overlayName)) {
                    if (overlay.canRemove(overlayName, event)) {
                        REGISTRY.unregister(id);
                    }
                    break;
                }
            }
        }
    }

    @SubscribeEvent
    public static void onKeyTyped(InputEvent.Key event) {
        if (REGISTRY.hasEntry()) {
            for (Class<?> id : REGISTRY.getIDs()) {
                IOverlay overlay = REGISTRY.get(id);
                if (overlay.isEnable(KEY_TYPED, IOverlay.EventAction.KEY_TYPED) && overlay.keyTyped(event.getKey(), event.getScanCode(), event.getAction(), event.getModifiers())){
                    if (overlay.canRemove(KEY_TYPED, IOverlay.EventAction.KEY_TYPED)) {
                        REGISTRY.unregister(id);
                    }
                    break;
                }
            }
        }
    }
}
