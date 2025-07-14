package cat.jiu.core.util.client.widget;

import cat.jiu.core.CoreMain;
import cat.jiu.core.util.client.RenderUtils;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class ButtonReload extends Button {
    public static final Component RELOAD = Component.translatable("info.config.reload");
    public static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(CoreMain.MODID, "textures/gui/widgets.png");
    public ButtonReload(int x, int y, OnPress onPress) {
        super(x, y, 20, 20, CommonComponents.EMPTY, onPress, DEFAULT_NARRATION);
        this.setTooltip(Tooltip.create(RELOAD));
    }
    public ButtonReload(int pX, int pY, Component pMessage, OnPress pOnPress) {
        super(pX, pY, 20, 20, pMessage, pOnPress, DEFAULT_NARRATION);
        this.setTooltip(Tooltip.create(RELOAD));
    }
    public ButtonReload(int pX, int pY, Component pMessage, OnPress pOnPress, CreateNarration pCreateNarration) {
        super(pX, pY, 20, 20, pMessage, pOnPress, pCreateNarration);
        this.setTooltip(Tooltip.create(RELOAD));
    }
    public ButtonReload(Builder builder) {
        super(builder);
        this.setWidth(20);
        this.setHeight(20);
        this.setTooltip(Tooltip.create(RELOAD));
    }

    @Override
    protected void renderWidget(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        super.renderWidget(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
        int u = this.isHovered() ? 18 : 0;
        RenderUtils.draw(pGuiGraphics, TEXTURE, this.getX()+3, this.getY()+2, 15, 15, u, 0, 18, 19, null);
    }
}
