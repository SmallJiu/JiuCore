package cat.jiu.core.api.element;

import com.mojang.blaze3d.platform.Window;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.gui.overlay.ForgeGui;

import java.util.function.Supplier;

public interface IOverlay extends Supplier<Class<?>> {
    /**
     * @return true if canceled all overlay render.
     */
    @OnlyIn(Dist.CLIENT)
    boolean render(ForgeGui hud, Window window, GuiGraphics graphics, float partialTicks, ResourceLocation overlay);

    /**
     * @return true if canceled all overlay response keyTyped.
     */
    default boolean keyTyped(int key, int scanCode, int action, int modifiers) {
        return false;
    }

    /**
     * @param overlay overlay name
     * @return true if this overlay instance is enable on window render.
     */
    boolean isEnable(ResourceLocation overlay, EventAction event);

    default boolean canRemove(boolean preEvent) {
        return false;
    }
    default boolean canRemove(ResourceLocation overlayName, EventAction event) {
        return this.canRemove(event.isPreRenderEvent());
    }

    @Override
    default Class<?> get() {
        return this.getClass();
    }

    enum EventAction {
        KEY_TYPED, RENDER_PRE, RENDER_POST;
        public boolean isPreRenderEvent() {
            return this == RENDER_PRE;
        }
        public boolean isPostRenderEvent() {
            return this == RENDER_POST;
        }
        public boolean isKeyTypedEvent() {
            return this == KEY_TYPED;
        }
    }
}
