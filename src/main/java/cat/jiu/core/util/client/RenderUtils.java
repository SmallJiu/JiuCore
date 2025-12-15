package cat.jiu.core.util.client;

import cat.jiu.core.api.element.IText;
import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.blaze3d.platform.NativeImage;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.inventory.tooltip.TooltipRenderUtil;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.network.chat.*;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FastColor;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import org.joml.Matrix4f;
import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFW;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

public class RenderUtils {

    // image
    public static void draw(GuiGraphics graphics, ResourceLocation texture, int x, int y, int width, int height, float u, float v, int uWidth, int vHeight, int textureWidth, int textureHeight) {
        graphics.blit(texture, x, y, width, height, u, v, uWidth, vHeight, textureWidth, textureHeight);
    }
    public static void draw(GuiGraphics graphics, ResourceLocation texture, int x, int y, int width, int height, float u, float v, int textureWidth, int textureHeight) {
        draw(graphics, texture, x, y, width, height, u, v, width, height, textureWidth, textureHeight);
    }
    public static void draw(GuiGraphics graphics, ResourceLocation texture, int x, int y, int width, int height, float u, float v, int uWidth, int vHeight, Object nothing) {
        draw(graphics, texture, x, y, width, height, u, v, uWidth, vHeight, 256, 256);
    }
    public static void draw(GuiGraphics graphics, ResourceLocation texture, int x, int y, int width, int height, int u, int v) {
        draw(graphics, texture, x, y, width, height, u, v, 256, 256);
    }

    public static void draw(GuiGraphics graphics, int texture, int x, int y, int width, int height, float u, float v, int uWidth, int vHeight, int textureWidth, int textureHeight) {
        int
                x2 = x + width,
                y2 = y + height;

        float
                minU = (u + 0.0F) / textureWidth,
                maxU = (u + (float)uWidth) / textureWidth,
                minV = (v + 0.0F) / textureHeight,
                maxV = (v + (float)vHeight) / textureHeight;

        int blitOffset = 0;

        RenderSystem.setShaderTexture(0, texture);
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        Matrix4f matrix4f = graphics.pose().last().pose();
        BufferBuilder bufferbuilder = Tesselator.getInstance().getBuilder();
        bufferbuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
        bufferbuilder.vertex(matrix4f, (float)x, (float)y, (float)blitOffset).uv(minU, minV).endVertex();
        bufferbuilder.vertex(matrix4f, (float)x, (float)y2, (float)blitOffset).uv(minU, maxV).endVertex();
        bufferbuilder.vertex(matrix4f, (float)x2, (float)y2, (float)blitOffset).uv(maxU, maxV).endVertex();
        bufferbuilder.vertex(matrix4f, (float)x2, (float)y, (float)blitOffset).uv(maxU, minV).endVertex();
        BufferUploader.drawWithShader(bufferbuilder.end());
    }
    public static void draw(GuiGraphics graphics, int texture, int x, int y, int width, int height, float u, float v, int textureWidth, int textureHeight) {
        draw(graphics, texture, x, y, width, height, u, v, width, height, textureWidth, textureHeight);
    }
    public static void draw(GuiGraphics graphics, int texture, int x, int y, int width, int height, float u, float v, int uWidth, int vHeight, Object nothing) {
        draw(graphics, texture, x, y, width, height, u, v, uWidth, vHeight, 256, 256);
    }
    public static void draw(GuiGraphics graphics, int texture, int x, int y, int width, int height, int u, int v) {
        draw(graphics, texture, x, y, width, height, u, v, 256, 256);
    }

    public static void bindTexture(int texture) {
        RenderSystem.setShaderTexture(0, texture);
    }
    public static void bindTexture(ResourceLocation texture) {
        RenderSystem.setShaderTexture(0, texture);
    }

    public static int uploadGLTexture(BufferedImage image) {
        int glID;
        try(BufferedTexture texture = new BufferedTexture(image, true)) {
            glID = texture.getId();
        }
        return glID;
    }

    public static void uploadGLTexture(int glID, BufferedImage image) {
        new BufferedTexture(glID, image, true).close();
    }

    public static void registerToMinecraft(ResourceLocation id, BufferedImage image, boolean enableAlpha) throws IOException {
        Minecraft.getInstance().getTextureManager().register(id, new DynamicTexture(NativeImage.read(toBuffer(image, enableAlpha))));
    }

    public static ByteBuffer toBuffer(BufferedImage image, boolean enableAlpha) {
        int[] pixels = new int[image.getWidth() * image.getHeight()];//创建像素列表
        image.getRGB(0, 0, image.getWidth(), image.getHeight(), pixels, 0, image.getWidth());//获取图片像素
        ByteBuffer buffer = BufferUtils.createByteBuffer(image.getWidth() * image.getHeight() * (enableAlpha ? 4 : 3));//创建字节缓冲区，*4是包含alpha *3不包含
        //遍历图片像素转换为RGBA
        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                int pixel = pixels[y * image.getWidth() + x];
                Color color = new Color(pixel);
                buffer.put((byte) color.getRed());//像素点的Red
                buffer.put((byte) color.getGreen());//像素点的Green
                buffer.put((byte) color.getBlue());//像素点的Blue
                if (enableAlpha) {
                    buffer.put((byte) color.getAlpha());//像素点的Alpha
                }
            }
        }
        buffer.flip(); //一定要翻转
        return buffer;
    }

    // text

    public static Font getFontRenderer(){
        return Minecraft.getInstance().font;
    }
    public static int fontHeight(){
        return getFontRenderer().lineHeight;
    }
    public static int getFontHeight() {
        return getFontRenderer().lineHeight;
    }
    public static int width(IText s) {
        return getFontRenderer().width(s.toTextComponent());
    }
    public static int width(String s) {
        return getFontRenderer().width(s);
    }
    public static int width(char s) {
        return getFontRenderer().width(String.valueOf(s));
    }
    public static int width(FormattedText s) {
        return getFontRenderer().width(s);
    }
    public static int width(FormattedCharSequence s) {
        return getFontRenderer().width(s);
    }

    @Deprecated
    public static MutableComponent copyOnClickedText(Component component) {
        return TextUtils.copyOnClickedText(component);
    }

    @Deprecated
    public static List<FormattedCharSequence> split(String text, int maxLength, boolean useMcWarp) {
        return TextUtils.split(text, maxLength, useMcWarp);
    }

    @Deprecated
    public static List<FormattedCharSequence> split(Component text, int maxLength, boolean useMcWarp) {
        return TextUtils.split(text, maxLength, useMcWarp);
    }

    // string

    public static void drawString(GuiGraphics graphics, String text, int x, int y, int color, boolean drawShadow) {
        graphics.drawString(getFontRenderer(), text, x, y, color, drawShadow);
    }
    public static void drawString(GuiGraphics graphics, List<String> text, int x, int y, int color, boolean drawShadow) {
        for (String s : text) {
            drawString(graphics, s, x, y, color, drawShadow);
            y += getFontHeight() + 1;
        }
    }

    public static void drawCenteredString(GuiGraphics graphics, String text, int x, int y, int color, boolean drawShadow) {
        drawString(graphics, text, x - width(text) / 2, y, color, drawShadow);
    }
    public static void drawCenteredString(GuiGraphics graphics, List<String> text, int x, int y, int color, boolean drawShadow) {
        for (String s : text) {
            drawCenteredString(graphics, s, x, y, color, drawShadow);
            y += getFontHeight() + 1;
        }
    }
    public static void drawScaledString(GuiGraphics graphics, String text, int x, int y, int color, boolean drawShadow, float scale, float z) {
        graphics.pose().pushPose();
        graphics.pose().translate(0, 0, z);
        graphics.pose().scale(scale, scale, 0);
        drawString(graphics, text, x, y, color, drawShadow);
        graphics.pose().popPose();
    }

    /**
     * like<p>
     *     't'<p>
     *     'h'<p>
     *     'i'<p>
     *     's'<p>
     *     ' '<p>
     *     'i'<p>
     *     's'<p>
     *     ' '<p>
     *     't'<p>
     *     'e'<p>
     *     'x'<p>
     *     't'
     */
    public static void drawVerticalString(GuiGraphics graphics, String text, int x, int y, int color, boolean drawShadow, int marinDown) {
        for (char c : text.toCharArray()) {
            String s = String.valueOf(c);
            drawString(graphics, s, x, y, color, drawShadow);
            y += getFontHeight() + marinDown;
        }
    }
    public static void drawVerticalString(GuiGraphics graphics, List<String> text, int x, int y, int color, boolean drawShadow, boolean alignRight, int marinSide, int marinDown) {
        for (String s : text) {
            drawVerticalString(graphics, s, x, y, color, drawShadow, marinDown);
            int firstWidth = width(s) + marinSide;
            if (alignRight) {
                x += firstWidth;
            }else {
                x -= firstWidth;
            }
        }
    }

    public static void drawRightString(GuiGraphics graphics, String text, int x, int y, int color, boolean drawShadow) {
        drawString(graphics, text, x - width(text), y, color, drawShadow);
    }

    /**
     * like<p>
     * '           this is text a'<p>
     * '        this is test text'<p>
     * 'hey! this is a test text!'<p>
     * '      this is a test text'<p>
     */
    public static void drawRightString(GuiGraphics graphics, List<String> text, int x, int y, int color, boolean drawShadow) {
        int maxWidth = 0;
        for (String s : text) {
            maxWidth = Math.max(maxWidth, width(s));
        }
        x -= maxWidth;
        for (String s : text) {
            drawString(graphics, s, x, y, color, drawShadow);
            y += getFontHeight() + 1;
        }
    }

    public static void drawString(GuiGraphics graphics, String text, int x, int y, int color, boolean drawShadow, boolean alignRight, int marinSide) {
        if (marinSide!=0) {
            for (char c : text.toCharArray()) {
                String s = String.valueOf(c);
                drawString(graphics, s, x, y, color, drawShadow);
                if (alignRight) {
                    x -= width(s) + marinSide;
                }else {
                    x += width(s) + marinSide;
                }
            }
        }else {
            if (alignRight) {
                drawRightString(graphics, text, x, y, color, drawShadow);
            }else {
                drawString(graphics, text, x, y, color, drawShadow);
            }
        }
    }
    public static void drawString(GuiGraphics graphics, List<String> text, int x, int y, int color, boolean drawShadow, boolean alignRight, int marinSide, int marinDown) {
        for (String s : text) {
            drawString(graphics, s, x, y, color, drawShadow, alignRight, marinSide);
            y += getFontHeight()+ marinDown;
        }
    }
    public static void renderScrollingString(GuiGraphics guiGraphics, String text, int x, int y, int width, int height, int color, boolean drawShadow) {
        renderScrollingComponent(guiGraphics, Component.literal(text), x, y, width, height, color, drawShadow);
    }
    public static void renderScrollingString(GuiGraphics guiGraphics, String text, int x, int y, int width, int color, boolean drawShadow) {
        renderScrollingComponent(guiGraphics, Component.literal(text), x, y, width, getFontHeight(), color, drawShadow);
    }

        // Component

    public static void drawComponent(GuiGraphics graphics, Component text, int x, int y, int color, boolean drawShadow) {
        graphics.drawString(getFontRenderer(), text, x, y, color, drawShadow);
    }
    public static void drawComponent(GuiGraphics graphics, List<Component> text, int x, int y, int color, boolean drawShadow) {
        for (Component s : text) {
            drawComponent(graphics, s, x, y, color, drawShadow);
            y += getFontHeight() + 1;
        }
    }

    public static void drawCenteredComponent(GuiGraphics graphics, Component text, int pX, int pY, int pColor, boolean drawShadow) {
        drawComponent(graphics, text, pX - width(text) / 2, pY, pColor, drawShadow);
    }
    public static void drawCenteredComponent(GuiGraphics graphics, List<Component> text, int x, int y, int color, boolean drawShadow) {
        for (Component s : text) {
            drawCenteredComponent(graphics, s, x, y, color, drawShadow);
            y += getFontHeight() + 1;
        }
    }
    public static void drawScaledComponent(GuiGraphics graphics, Component text, int x, int y, int color, boolean drawShadow, float scale, float z) {
        graphics.pose().pushPose();
        graphics.pose().translate(0, 0, z);
        graphics.pose().scale(scale, scale, 0);
        drawComponent(graphics, text, x, y, color, drawShadow);
        graphics.pose().popPose();
    }

    public static void drawRightComponent(GuiGraphics graphics, Component text, int x, int y, int color, boolean drawShadow) {
        drawComponent(graphics, text, x - width(text), y, color, drawShadow);
    }
    public static void drawRightComponent(GuiGraphics graphics, List<Component> text, int x, int y, int color, boolean drawShadow) {
        int maxWidth = 0;
        for (Component component : text) {
            maxWidth = Math.max(maxWidth, width(component));
        }
        x -= maxWidth;
        for (Component s : text) {
            drawComponent(graphics, s, x, y, color, drawShadow);
            y += getFontHeight() + 1;
        }
    }

    public static void renderScrollingComponent(GuiGraphics guiGraphics, Component text, int x, int y, int width, int color, boolean drawShadow) {
        renderScrollingComponent(guiGraphics, text, x, y, width, getFontHeight(), color, drawShadow);
    }
    public static void renderScrollingComponent(GuiGraphics guiGraphics, Component text, int x, int y, int width, int height, int color, boolean drawShadow) {
        int maxX = x + width;
        int maxY = y + height;
        int textWidth = width(text);
        int j = (y + maxY - 9) / 2 + 1;
        int k = maxX - x;
        if (textWidth > k) {
            int l = textWidth - k;
            double d0 = (double) Util.getMillis() / 1000.0D;
            double d1 = Math.max((double)l * 0.5D, 3.0D);
            double d2 = Math.sin((Math.PI / 2D) * Math.cos((Math.PI * 2D) * d0 / d1)) / 2.0D + 0.5D;
            double d3 = Mth.lerp(d2, 0.0D, l);
            guiGraphics.enableScissor(x, y, maxX, maxY);
            guiGraphics.drawString(getFontRenderer(), text, x - (int)d3, j, color, drawShadow);
            guiGraphics.disableScissor();
        } else {
            drawCenteredComponent(guiGraphics, text, (x + maxX) / 2, j, color, drawShadow);
        }
    }

    /*
    public static void drawVerticalComponent(GuiGraphics graphics, Component text, int x, int y, int color, boolean drawShadow, int marinDown) {
        for (char c : text.getString().toCharArray()) {
            String s = String.valueOf(c);
            drawString(graphics, s, x, y, color, drawShadow);
            y += getFontHeight() + marinDown;
        }
    }
    public static void drawVerticalComponent(GuiGraphics graphics, List<Component> text, int x, int y, int color, boolean drawShadow, boolean alignRight, int marinSide, int marinDown) {
        for (Component s : text) {
            drawVerticalComponent(graphics, s, x, y, color, drawShadow, marinDown);
            int firstWidth = width(s) + marinSide;
            if (alignRight) {
                x += firstWidth;
            }else {
                x -= firstWidth;
            }
        }
    }

    public static void drawComponent(GuiGraphics graphics, Component text, int x, int y, int color, boolean drawShadow, boolean alignRight, int marinSide) {
        if (marinSide!=0) {
            for (char c : text.getString().toCharArray()) {
                String s = String.valueOf(c);
                drawString(graphics, s, x, y, color, drawShadow);
                if (alignRight) {
                    x -= width(s) + marinSide;
                }else {
                    x += width(s) + marinSide;
                }
            }
        }else {
            if (alignRight) {
                drawRightComponent(graphics, text, x, y, color, drawShadow);
            }else {
                drawComponent(graphics, text, x, y, color, drawShadow);
            }
        }
    }
    public static void drawComponent(GuiGraphics graphics, List<Component> text, int x, int y, int color, boolean drawShadow, boolean alignRight, int marinSide, int marinDown) {
        for (Component s : text) {
            drawComponent(graphics, s, x, y, color, drawShadow, alignRight, marinSide);
            y += getFontHeight()+ marinDown;
        }
    }
     */

        // Sequence

    public static void drawSequence(GuiGraphics graphics, FormattedCharSequence s, int x, int y, int color, boolean drawShadow) {
        graphics.drawString(getFontRenderer(), s, x, y, color, drawShadow);
    }

    public static void drawCenteredSequence(GuiGraphics graphics, FormattedCharSequence text, int x, int y, int color, boolean drawShadow) {
        drawSequence(graphics, text, x - width(text) / 2, y, color, drawShadow);
    }
    public static void drawCenteredSequence(GuiGraphics graphics, List<FormattedCharSequence> text, int x, int y, int color, boolean drawShadow) {
        for (FormattedCharSequence s : text) {
            drawCenteredSequence(graphics, s, x, y, color, drawShadow);
            y += getFontHeight() + 1;
        }
    }
    public static void drawScaledComponent(GuiGraphics graphics, FormattedCharSequence text, int x, int y, int color, boolean drawShadow, float scale, float z) {
        graphics.pose().pushPose();
        graphics.pose().translate(0, 0, z);
        graphics.pose().scale(scale, scale, 0);
        drawSequence(graphics, text, x, y, color, drawShadow);
        graphics.pose().popPose();
    }

    public static void drawRightSequence(GuiGraphics graphics, FormattedCharSequence text, int x, int y, int color, boolean drawShadow) {
        drawSequence(graphics, text, x - width(text), y, color, drawShadow);
    }
    public static void drawRightSequence(GuiGraphics graphics, List<FormattedCharSequence> text, int x, int y, int color, boolean drawShadow) {
        int maxWidth = 0;
        for (FormattedCharSequence sequence : text) {
            maxWidth = Math.max(maxWidth, width(sequence));
        }
        x -= maxWidth;
        for (FormattedCharSequence s : text) {
            drawSequence(graphics, s, x, y, color, drawShadow);
            y += getFontHeight() + 1;
        }
    }

    public static void renderScrollingSequence(GuiGraphics guiGraphics, FormattedCharSequence text, int x, int y, int width, int color, boolean drawShadow) {
        renderScrollingSequence(guiGraphics, text, x, y, width, getFontHeight(), color, drawShadow);
    }
    public static void renderScrollingSequence(GuiGraphics guiGraphics, FormattedCharSequence text, int x, int y, int width, int height, int color, boolean drawShadow) {
        int maxX = x + width;
        int maxY = y + height;
        int textWidth = width(text);
        int j = (y + maxY - 9) / 2 + 1;
        int k = maxX - x;
        if (textWidth > k) {
            int l = textWidth - k;
            double d0 = (double) Util.getMillis() / 1000.0D;
            double d1 = Math.max((double)l * 0.5D, 3.0D);
            double d2 = Math.sin((Math.PI / 2D) * Math.cos((Math.PI * 2D) * d0 / d1)) / 2.0D + 0.5D;
            double d3 = Mth.lerp(d2, 0.0D, l);
            guiGraphics.enableScissor(x, y, maxX, maxY);
            guiGraphics.drawString(getFontRenderer(), text, x - (int)d3, j, color, drawShadow);
            guiGraphics.disableScissor();
        } else {
            drawCenteredSequence(guiGraphics, text, (x + maxX) / 2, j, color, drawShadow);
        }
    }

    /*
    public static void drawVerticalSequence(GuiGraphics graphics, FormattedCharSequence text, int x, int y, int color, boolean drawShadow, int marinDown) {
        for (char c : text.toCharArray()) {
            String s = String.valueOf(c);
            drawString(graphics, s, x, y, color, drawShadow);
            y += getFontHeight() + marinDown;
        }
    }
    public static void drawVerticalSequence(GuiGraphics graphics, List<FormattedCharSequence> text, int x, int y, int color, boolean drawShadow, boolean alignRight, int marinSide, int marinDown) {
        for (FormattedCharSequence s : text) {
            drawVerticalSequence(graphics, s, x, y, color, drawShadow, marinDown);
            int firstWidth = width(s) + marinSide;
            if (alignRight) {
                x += firstWidth;
            }else {
                x -= firstWidth;
            }
        }
    }

    public static void drawSequence(GuiGraphics graphics, FormattedCharSequence text, int x, int y, int color, boolean drawShadow, boolean alignRight, int marinSide) {
        if (marinSide!=0) {
            for (char c : text.toCharArray()) {
                String s = String.valueOf(c);
                drawSequence(graphics, s, x, y, color, drawShadow);
                if (alignRight) {
                    x -= width(s) + marinSide;
                }else {
                    x += width(s) + marinSide;
                }
            }
        }else {
            if (alignRight) {
                drawRightSequence(graphics, text, x, y, color, drawShadow);
            }else {
                drawSequence(graphics, text, x, y, color, drawShadow);
            }
        }
    }
    public static void drawSequence(GuiGraphics graphics, List<FormattedCharSequence> text, int x, int y, int color, boolean drawShadow, boolean alignRight, int marinSide, int marinDown) {
        for (FormattedCharSequence s : text) {
            drawSequence(graphics, s, x, y, color, drawShadow, alignRight, marinSide);
            y += getFontHeight()+ marinDown;
        }
    }
     */

// graphical

   public static void fill(GuiGraphics graphics, int x, int y, int width, int height, int color1, int color2) {
       graphics.fillGradient(x, y, x + width, y + height, color1, color2);
   }
   public static void fill(GuiGraphics graphics, int x, int y, int width, int height, int color) {
        graphics.fill(x, y, x + width, y + height, color);
   }
   public static void hLine(GuiGraphics graphics, int x, int y, int width, int color) {
        graphics.hLine(x, x + width, y, color);
   }
   public static void vLine(GuiGraphics graphics, int x, int y, int height, int color) {
        graphics.vLine(x, y, y + height, color);
   }
    public static void hLine(GuiGraphics graphics, EditBox box, int color) {
        hLine(graphics, box.getX(), box.getY() + box.getHeight() - 2, box.getWidth(), color);
    }

   public static void fillCentered(GuiGraphics graphics, int x, int y, int width, int height, int color1, int color2) {
        fill(graphics, x - width/2, y, width, height, color1, color2);
   }
   public static void fillCentered(GuiGraphics graphics, int x, int y, int width, int height, int color) {
        fill(graphics, x - width/2, y, width, height, color);
   }
    public static void hLineCentered(GuiGraphics graphics, int x, int y, int width, int color) {
        hLine(graphics, x - width/2, y, width, color);
    }
    public static void vLineCentered(GuiGraphics graphics, int x, int y, int height, int color) {
        vLine(graphics, x, y - height/2, height, color);
    }

    public static void squareCentered(GuiGraphics graphics, int x, int y, int width, int height, int bgColor, int borderColor, boolean centerWidth, boolean centerHeight) {
        square(graphics, x - (centerWidth ? width / 2 : 0), y - (centerHeight ? height / 2 : 0), width, height, bgColor, borderColor);
    }

    public static void square(GuiGraphics graphics, int x, int y, int width, int height, int bgColor, int borderColor) {
        fill(graphics, x, y, width, height, bgColor); // 背景

        hLine(graphics, x, y, width, borderColor); // 上
        hLine(graphics, x, y - 1, width, bgColor);

        hLine(graphics, x, y + height, width, borderColor); // 下
        hLine(graphics, x, y + height + 1, width, bgColor);

        vLine(graphics, x, y, height, borderColor); // 左
        vLine(graphics, x - 1, y, height, bgColor);

        vLine(graphics, x + width, y, height, borderColor); // 右
        vLine(graphics, x + width + 1, y, height, bgColor);
    }

    public static void hLineGradient(GuiGraphics graphics, boolean anti, int x, int y, int width, int height, int colorFrom, int colorTo, Object nothing) {
        hLineGradient(graphics, anti, x, y, x+width, y+height, colorFrom, colorTo);
    }
    public static void hLineGradient(GuiGraphics graphics, boolean anti, int x, int y, int x2, int y2, int colorFrom, int colorTo) {
        VertexConsumer pConsumer = graphics.bufferSource().getBuffer(RenderType.gui());

        float fromAlpha = (float) FastColor.ARGB32.alpha(colorFrom) / 255.0F;
        float fromRed = (float)FastColor.ARGB32.red(colorFrom) / 255.0F;
        float fromGreen = (float)FastColor.ARGB32.green(colorFrom) / 255.0F;
        float fromBlue = (float)FastColor.ARGB32.blue(colorFrom) / 255.0F;
        float toAlpha = (float)FastColor.ARGB32.alpha(colorTo) / 255.0F;
        float toRed = (float)FastColor.ARGB32.red(colorTo) / 255.0F;
        float toGreen = (float)FastColor.ARGB32.green(colorTo) / 255.0F;
        float toBlue = (float)FastColor.ARGB32.blue(colorTo) / 255.0F;
        Matrix4f matrix4f = graphics.pose().last().pose();
        // toRed, toGreen, toBlue, toAlpha
        // fromRed, fromGreen, fromBlue, fromAlpha
        if (anti) {
            pConsumer.vertex(matrix4f, (float)x, (float)y, (float)0).color(toRed, toGreen, toBlue, toAlpha).endVertex();
            pConsumer.vertex(matrix4f, (float)x, (float)y2, (float)0).color(toRed, toGreen, toBlue, toAlpha).endVertex();
            pConsumer.vertex(matrix4f, (float)x2, (float)y2, (float)0).color(fromRed, fromGreen, fromBlue, fromAlpha).endVertex();
            pConsumer.vertex(matrix4f, (float)x2, (float)y, (float)0).color(fromRed, fromGreen, fromBlue, fromAlpha).endVertex();
        }else {
            pConsumer.vertex(matrix4f, (float)x, (float)y, (float)0).color(fromRed, fromGreen, fromBlue, fromAlpha).endVertex();
            pConsumer.vertex(matrix4f, (float)x, (float)y2, (float)0).color(fromRed, fromGreen, fromBlue, fromAlpha).endVertex();
            pConsumer.vertex(matrix4f, (float)x2, (float)y2, (float)0).color(toRed, toGreen, toBlue, toAlpha).endVertex();
            pConsumer.vertex(matrix4f, (float)x2, (float)y, (float)0).color(toRed, toGreen, toBlue, toAlpha).endVertex();
        }
    }

    public static void tooltipBackground(GuiGraphics graphics, int x, int y, int width, int height, boolean centerWidth, boolean centerHeight) {
        TooltipRenderUtil.renderTooltipBackground(graphics, x - (centerWidth ? (width / 2) : 0), y - (centerHeight ? (height / 2) : 0), width, height, 0);
    }

    public static void tooltipBackground(GuiGraphics graphics, int x, int y, int width, int height, int bgColor, int borderColor, boolean centerWidth, boolean centerHeight) {
        TooltipRenderUtil.renderTooltipBackground(graphics, x - (centerWidth ? (width / 2) : 0), y - (centerHeight ? (height / 2) : 0), width, height, 0, bgColor, bgColor, borderColor, borderColor);
    }

    public static void drawTextTooltip(GuiGraphics graphics, int mouseX, int mouseY, Collection<IText> tooltips) {
        graphics.renderComponentTooltip(getFontRenderer(), tooltips.stream().map(IText::toTextComponent).toList(), mouseX, mouseY);
    }
    public static void drawTextTooltip(GuiGraphics graphics, int mouseX, int mouseY, IText... tooltips) {
       graphics.renderComponentTooltip(getFontRenderer(), Arrays.stream(tooltips).map(IText::toTextComponent).toList(), mouseX, mouseY);
    }
    public static void drawStringTooltip(GuiGraphics graphics, int mouseX, int mouseY, Collection<String> tooltips) {
        graphics.renderComponentTooltip(getFontRenderer(), tooltips.stream().map(Component::translatable).collect(Collectors.toUnmodifiableList()), mouseX, mouseY);
    }
    public static void drawStringTooltip(GuiGraphics graphics, int mouseX, int mouseY, String... tooltips) {
       graphics.renderComponentTooltip(getFontRenderer(), Arrays.stream(tooltips).map(Component::translatable).collect(Collectors.toUnmodifiableList()), mouseX, mouseY);
    }
    public static void drawComponentTooltip(GuiGraphics graphics, int mouseX, int mouseY, List<Component> tooltips) {
        graphics.renderComponentTooltip(getFontRenderer(), tooltips, mouseX, mouseY);
    }
    public static void drawComponentTooltip(GuiGraphics graphics, int mouseX, int mouseY, Component... tooltips) {
       graphics.renderComponentTooltip(getFontRenderer(), Arrays.asList(tooltips), mouseX, mouseY);
    }
    public static void drawSequenceTooltip(GuiGraphics graphics, int mouseX, int mouseY, List<FormattedCharSequence> tooltips) {
        graphics.renderTooltip(getFontRenderer(), tooltips, mouseX, mouseY);
    }
    public static void drawSequenceTooltip(GuiGraphics graphics, int mouseX, int mouseY, FormattedCharSequence... tooltips) {
       graphics.renderTooltip(getFontRenderer(), Arrays.asList(tooltips), mouseX, mouseY);
    }
    public static void drawItemStackTooltip(GuiGraphics graphics, int mouseX, int mouseY, ItemStack stack, Collection<Component> otherTooltips) {
        List<Component> components = stack.getTooltipLines(Minecraft.getInstance().player, Minecraft.getInstance().options.advancedItemTooltips ? TooltipFlag.ADVANCED : TooltipFlag.NORMAL);
        components.addAll(otherTooltips);
        graphics.renderComponentTooltip(getFontRenderer(), components, mouseX, mouseY);
    }
    public static void drawItemStackTooltip(GuiGraphics graphics, int mouseX, int mouseY, ItemStack stack, Component... otherTooltips) {
       List<Component> components = stack.getTooltipLines(Minecraft.getInstance().player, Minecraft.getInstance().options.advancedItemTooltips ? TooltipFlag.ADVANCED : TooltipFlag.NORMAL);
       components.addAll(Arrays.asList(otherTooltips));
       graphics.renderComponentTooltip(getFontRenderer(), components, mouseX, mouseY);
    }


    public static String toTitleCase(String s) {
       return toTitleCase(s, Locale.ROOT);
    }
    public static String toTitleCase(String s, Locale locale) {
       return s.substring(0,1).toUpperCase(locale) + s.substring(1).toLowerCase(locale);
    }

    public static final Map<Integer, Collection<Component>> UNHOLD_MESSAGE = new HashMap<>();
    public static Collection<Component> addHoldMessage(int key, Component... msg){
       if (InputConstants.isKeyDown(Minecraft.getInstance().getWindow().getWindow(), key)) {
           return Arrays.asList(msg);
       }
       if (!UNHOLD_MESSAGE.containsKey(key)) {
           String name = GLFW.glfwGetKeyName(key, GLFW.glfwGetKeyScancode(key));
           if (name!=null) {
               name = toTitleCase(name);
           }else {
               name = "Unnamed";
           }
           UNHOLD_MESSAGE.put(key, List.of(Component.translatable("info.hold.un", name)));
       }
       return UNHOLD_MESSAGE.get(key);
    }

    // other

    public static boolean inRange(double mouseX, double mouseY, int x, int y, int width, int height) {
        int maxX = x + width;
        int maxY = y + height;
        return (mouseX >= x && mouseY >= y) && (mouseX <= maxX && mouseY <= maxY);
    }

    public static int rgb(float r, float g, float b, float a) {
        return rgb((int)(r * 255.0f + 0.5), (int)(g * 255.0f + 0.5), (int)(b * 255.0f + 0.5), (int)(a * 255.0f + 0.5));
    }

    public static int rgb(int r, int g, int b, int a) {
        return (a & 0xFF) << 24 | (r & 0xFF) << 16 | (g & 0xFF) << 8 | (b & 0xFF);
    }

    public static int rgb(float r, float g, float b, float a, boolean anti) {
        return rgb((int)(r*255+0.5), (int)(g*255+0.5), (int)(b*255+0.5), (int)(a*255+0.5), anti);
    }
    public static int rgb(int r, int g, int b, int a, boolean anti) {
       r = anti ? r - 255 : r;
       g = anti ? g - 255 : g;
       b = anti ? b - 255 : b;
        return ((a & 0xFF) << 24) |
                ((r & 0xFF) << 16) |
                ((g & 0xFF) << 8)  |
                 (b & 0xFF);
    }

    public static int red(int color) {
       return (color >> 16) & 0xFF;
    }
    public static int green(int color) {
       return (color >> 8) & 0xFF;
    }
    public static int blue(int color) {
       return color & 0xFF;
    }
    public static int alpha(int color) {
       return (color >> 24) & 0xFF;
    }
    public static int abgr2argb(int abgr){
       return (abgr & 0xFF00FF00) | ((abgr & 0xFF) << 16) | ((abgr >> 16) & 0xFF);
    }
}
