package cat.jiu.core.util.client;

import net.minecraft.client.gui.GuiGraphics;

public class TooltipUtils {
    public static final int MOUSE_OFFSET = 12;
    private static final int PADDING = 3;
    public static final int PADDING_LEFT = 3;
    public static final int PADDING_RIGHT = 3;
    public static final int PADDING_TOP = 3;
    public static final int PADDING_BOTTOM = 3;
    private static final int BACKGROUND_COLOR = -267386864;
    private static final int BORDER_COLOR_TOP = 1347420415;
    private static final int BORDER_COLOR_BOTTOM = 1344798847;

    public static void renderTooltipBackground(GuiGraphics guiGraphics, int x, int y, int width, int height, int z) {
        renderTooltipBackground(guiGraphics, x, y, width, height, z, -BACKGROUND_COLOR, -BACKGROUND_COLOR, BORDER_COLOR_TOP, BORDER_COLOR_BOTTOM);
    }

    public static void renderTooltipBackground(GuiGraphics guiGraphics, int x, int y, int width, int height, int z, int backgroundTop, int backgroundBottom, int borderTop, int borderBottom) {
        int i = x - 3;
        int j = y - 3;
        int k = width + 3 + 3;
        int l = height + 3 + 3;
        renderHorizontalLine(guiGraphics, i, j - 1, k, z, backgroundTop);
        renderHorizontalLine(guiGraphics, i, j + l, k, z, backgroundBottom);
        renderRectangle(guiGraphics, i, j, k, l, z, backgroundTop, backgroundBottom);
        renderVerticalLineGradient(guiGraphics, i - 1, j, l, z, backgroundTop, backgroundBottom);
        renderVerticalLineGradient(guiGraphics, i + k, j, l, z, backgroundTop, backgroundBottom);
        renderFrameGradient(guiGraphics, i, j + 1, k, l, z, borderTop, borderBottom);
    }

    private static void renderFrameGradient(GuiGraphics guiGraphics, int x, int y, int width, int height, int z, int topColor, int bottomColor) {
        renderVerticalLineGradient(guiGraphics, x, y, height - 2, z, topColor, bottomColor);
        renderVerticalLineGradient(guiGraphics, x + width - 1, y, height - 2, z, topColor, bottomColor);
        renderHorizontalLine(guiGraphics, x, y - 1, width, z, topColor);
        renderHorizontalLine(guiGraphics, x, y - 1 + height - 1, width, z, bottomColor);
    }

    private static void renderVerticalLine(GuiGraphics guiGraphics, int x, int y, int length, int z, int color) {
        guiGraphics.fill(x, y, x + 1, y + length, z, color);
    }

    private static void renderVerticalLineGradient(GuiGraphics guiGraphics, int x, int y, int length, int z, int topColor, int bottomColor) {
        guiGraphics.fillGradient(x, y, x + 1, y + length, z, topColor, bottomColor);
    }

    private static void renderHorizontalLine(GuiGraphics guiGraphics, int x, int y, int length, int z, int color) {
        guiGraphics.fill(x, y, x + length, y + 1, z, color);
    }

    /** @deprecated */
    @Deprecated
    private static void renderRectangle(GuiGraphics guiGraphics, int x, int y, int width, int height, int z, int color) {
        renderRectangle(guiGraphics, x, y, width, height, z, color, color);
    }

    private static void renderRectangle(GuiGraphics guiGraphics, int x, int y, int width, int height, int z, int color, int colorTo) {
        guiGraphics.fillGradient(x, y, x + width, y + height, z, color, colorTo);
    }
}
