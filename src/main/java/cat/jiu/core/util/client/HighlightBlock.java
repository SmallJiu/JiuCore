package cat.jiu.core.util.client;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.joml.Matrix4f;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

@OnlyIn(Dist.CLIENT)
@Mod.EventBusSubscriber(Dist.CLIENT)
public class HighlightBlock extends RenderType {
    private static HighlightBlock INSTANCE = new HighlightBlock();
    public static HighlightBlock getInstance() {
        return INSTANCE;
    }

    protected static void setInstance(HighlightBlock instance) {
        HighlightBlock.INSTANCE = instance;
    }

    public static final ColorData
            CUBE_COLOR_DATA = new ColorData(255, 0, 0),
            LINK_COLOR_DATA = new ColorData(255, 5, 0)
    ;
    protected static final HashMap<BlockPos, Highlight> HEIGHT_LIGHTS = new HashMap<>();
    protected static final ArrayList<BlockPos> HEIGHT_LIGHTS_KEYS = new ArrayList<>();
    public static Highlight highlight(BlockPos pos, int m, int s, int tick, BlockPos... connect) {
        Highlight highlight = highlight(pos, m, s, tick);
        Collections.addAll(highlight.connectPos, connect);
        return highlight;
    }
    public static Highlight highlight(BlockPos pos, int m, int s, int tick, Direction... connect) {
        Highlight highlight = highlight(pos, m, s, tick);
        for (Direction side : connect) {
            highlight.connectPos.add(highlight.pos.relative(side));
        }
        return highlight;
    }
    public static Highlight highlight(BlockPos pos, int m, int s, int tick, BlockPos[] connectPos, Direction[] connectSide) {
        Highlight highlight = highlight(pos, m, s, tick);
        Collections.addAll(highlight.connectPos, connectPos);
        for (Direction side : connectSide) {
            highlight.connectPos.add(highlight.pos.relative(side));
        }
        return highlight;
    }
    public static Highlight highlight(BlockPos pos, int m, int s, int tick) {
        if (!HEIGHT_LIGHTS.containsKey(pos)) {
            HEIGHT_LIGHTS_KEYS.add(pos);
            HEIGHT_LIGHTS.put(pos, new Highlight((((m * 60L) + s) * 20) + tick, pos));
        }
        return HEIGHT_LIGHTS.get(pos);
    }

    protected static final RenderType CUBE_RENDER = create(
            "color_cube",
            DefaultVertexFormat.POSITION_COLOR,
            VertexFormat.Mode.QUADS,
            256,
            false, false,
            CompositeState.builder()
                    .setTransparencyState(
                            new TransparencyStateShard(
                                    "sto",
                                    () -> {
                                        RenderSystem.enableBlend();
                                        RenderSystem.blendFunc(
                                                GlStateManager.SourceFactor.SRC_ALPHA,
                                                GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA
                                        );
                                    },
                                    () -> {
                                        RenderSystem.disableBlend();
                                        RenderSystem.defaultBlendFunc();
                                    }
                            )
                    )
                    .setDepthTestState(NO_DEPTH_TEST)
                    .setCullState(NO_CULL)
                    .setShaderState(POSITION_COLOR_SHADER)
                    .setLightmapState(NO_LIGHTMAP)
                    .setWriteMaskState(COLOR_DEPTH_WRITE)
                    .setWriteMaskState(RenderStateShard.COLOR_WRITE)
                    .setTextureState(NO_TEXTURE)
                    .createCompositeState(true)
    );

    protected VertexBuffer vertex;
    public HighlightBlock() {
        super("", DefaultVertexFormat.POSITION_COLOR_NORMAL, VertexFormat.Mode.LINES, 0, false, false, () -> {}, () -> {});
    }

    public ColorData getCubeColor() {
        return CUBE_COLOR_DATA;
    }
    public ColorData getLinkColor() {
        return LINK_COLOR_DATA;
    }

    @SubscribeEvent
    public static void onRenderLevelStageEvent(RenderLevelStageEvent event) {
        if (event.getStage() == RenderLevelStageEvent.Stage.AFTER_PARTICLES && !HEIGHT_LIGHTS.isEmpty()) {
            for (int i = 0; i < HEIGHT_LIGHTS_KEYS.size(); i++) {
                Highlight light = HEIGHT_LIGHTS.get(HEIGHT_LIGHTS_KEYS.get(i));
                long gt = Minecraft.getInstance().level.getGameTime();
                if (!light.init) {
                    light.init = true;
                    light.endTime = gt + light.lightTime;
                }
                if (gt >= light.endTime) {
                    HEIGHT_LIGHTS_KEYS.remove(i);
                    HEIGHT_LIGHTS.remove(light.pos);
                }else {
                    if (light.isFlicker()) {
                        light.canRender = !light.canRender;
                    }
                    if (light.canRender) {
                        getInstance().render(light, event.getPoseStack(), event.getProjectionMatrix(), event.getCamera());
                    }
                }
            }
        }
    }

    public void render(Highlight light, PoseStack stack, Matrix4f matrix4f, Camera camera) {
        if (GameRenderer.getPositionColorShader() == null) {
            return;
        }
        if (camera.isInitialized()) {
            if (this.vertex == null) {
                this.vertex = new VertexBuffer(VertexBuffer.Usage.DYNAMIC);
            }
            BufferBuilder buffer = new BufferBuilder(CUBE_RENDER.bufferSize() * 8);
            stack.pushPose();
            Vec3 offset = camera.getPosition().reverse();
            stack.translate(offset.x, offset.y, offset.z);
            buffer.begin(CUBE_RENDER.mode(), CUBE_RENDER.format());

            light.render(buffer, stack, matrix4f, Minecraft.getInstance().renderBuffers().bufferSource(), offset);

            this.vertex.bind();
            this.vertex.upload(buffer.end());
            VertexBuffer.unbind();

            RenderSystem.setShader(GameRenderer::getPositionColorShader);
            RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
            RenderSystem.disableDepthTest();
            RenderSystem.disableCull();
            this.vertex.bind();
            this.vertex.drawWithShader(RenderSystem.getModelViewMatrix(), matrix4f, GameRenderer.getPositionColorShader());
            VertexBuffer.unbind();
            stack.popPose();
            RenderSystem.enableCull();
        }
    }

    public void drawCube(float cubeSize, BlockPos pos, ColorData color, PoseStack stack, BufferBuilder buf) {
        float half = cubeSize / 2f;
        Vec3 c = pos.getCenter();
        AABB box = new AABB(c.x - half, c.y - half, c.z - half, c.x + half, c.y + half, c.z + half);

        Vec3 topRight = new Vec3(box.maxX, box.maxY, box.maxZ);
        Vec3 bottomRight = new Vec3(box.maxX, box.minY, box.maxZ);
        Vec3 bottomLeft = new Vec3(box.minX, box.minY, box.maxZ);
        Vec3 topLeft = new Vec3(box.minX, box.maxY, box.maxZ);
        Vec3 topRight2 = new Vec3(box.maxX, box.maxY, box.minZ);
        Vec3 bottomRight2 = new Vec3(box.maxX, box.minY, box.minZ);
        Vec3 bottomLeft2 = new Vec3(box.minX, box.minY, box.minZ);
        Vec3 topLeft2 = new Vec3(box.minX, box.maxY, box.minZ);
        drawSize(color, stack, buf, topRight, bottomRight, bottomLeft, topLeft, topRight2, bottomRight2, bottomLeft2, topLeft2);
    }

    public void drawLink(float linkWidth, ColorData color, BlockPos from, BlockPos to, PoseStack stack, BufferBuilder buf) {
        Vec3
                a = from.getCenter(),
                b = to.getCenter(),
                law = getLawVec(a, b).scale(linkWidth),
                law2 = getLawVec2(a, b).scale(linkWidth);
        Vec3 topRight = a.add(law2);
        Vec3 bottomRight = a.subtract(law);
        Vec3 bottomLeft = a.subtract(law2);
        Vec3 topLeft = a.add(law);
        Vec3 topRight2 = b.add(law2);
        Vec3 bottomRight2 = b.subtract(law);
        Vec3 bottomLeft2 = b.subtract(law2);
        Vec3 topLeft2 = b.add(law);
        drawSize(color, stack, buf, topRight, bottomRight, bottomLeft, topLeft, topRight2, bottomRight2, bottomLeft2, topLeft2);
    }

    public void drawSize(ColorData color, PoseStack stack, BufferBuilder buf, Vec3 topRight, Vec3 bottomRight, Vec3 bottomLeft, Vec3 topLeft, Vec3 topRight2, Vec3 bottomRight2, Vec3 bottomLeft2, Vec3 topLeft2) {
        drawSide(topRight, topLeft, bottomRight, bottomLeft, color, buf, stack);
        drawSide(topRight2, topRight, bottomRight2, bottomRight, color, buf, stack);
        drawSide(topLeft2, topRight2, bottomLeft2, bottomRight2, color, buf, stack);
        drawSide(topLeft, topLeft2, bottomLeft, bottomLeft2, color, buf, stack);
        drawSide(topLeft2, topRight2, topLeft, topRight, color, buf, stack);
        drawSide(bottomLeft2, bottomRight2, bottomLeft, bottomRight, color, buf, stack);
    }
    public void drawSide(Vec3 tr, Vec3 tl, Vec3 br, Vec3 bl, ColorData color, VertexConsumer buf, PoseStack stack) {
        Matrix4f matrix4f = stack.last().pose();
        buf.vertex(matrix4f, (float) tr.x, (float) tr.y, (float) tr.z).color(color.getRf(), color.getGf(), color.getBf(), color.getAf()).endVertex();
        buf.vertex(matrix4f, (float) br.x, (float) br.y, (float) br.z).color(color.getRf(), color.getGf(), color.getBf(), color.getAf()).endVertex();
        buf.vertex(matrix4f, (float) bl.x, (float) bl.y, (float) bl.z).color(color.getRf(), color.getGf(), color.getBf(), color.getAf()).endVertex();
        buf.vertex(matrix4f, (float) tl.x, (float) tl.y, (float) tl.z).color(color.getRf(), color.getGf(), color.getBf(), color.getAf()).endVertex();
    }

    public void drawInWorldText(String text, ColorData color, Vec3 pos, Vec3 offset, Camera camera, PoseStack stack, MultiBufferSource multiBuf) {
        float scale = 0.027f;
        var fontRender = Minecraft.getInstance().font;
        var c = pos.add(offset);
        float stringMiddle = fontRender.width(text) / 2.0f;
        stack.pushPose();
        stack.translate(c.x, c.y, c.z);
        stack.mulPose(camera.rotation());
        stack.scale(-scale, -scale, scale);
        var mat = stack.last().pose();
        fontRender.drawInBatch(text, -stringMiddle, 0, color.toARGB(), false, mat, multiBuf, Font.DisplayMode.SEE_THROUGH, 0, 0xF000F0);
        stack.popPose();
    }

    public static class Highlight {
        public final long lightTime;
        protected long endTime;
        public final BlockPos pos;
        public float
                linkWidth = 0.025f,
                cubeSize = 0.8f;
        protected ColorData
                cubeColor = getInstance().getCubeColor().copy(),
                linkColor = getInstance().getLinkColor().copy()
        ;
        public final List<BlockPos> connectPos = new ArrayList<>();
        protected boolean
                init,
                isFlicker = false,
                canRender = true;

        public Highlight(long lightTime, BlockPos pos) {
            this.lightTime = lightTime;
            this.pos = pos;
        }

        public boolean isFlicker() {
            return isFlicker;
        }

        public Highlight setFlicker(boolean canFlicker) {
            this.isFlicker = canFlicker;
            return this;
        }
        public Highlight addConnectPos(BlockPos pos) {
            this.connectPos.add(pos);
            return this;
        }

        public void render(BufferBuilder buffer, PoseStack pose, Matrix4f matrix4f, MultiBufferSource.BufferSource multiBuffer, Vec3 offset) {
            getInstance().drawCube(this.cubeSize, this.pos, this.cubeColor, pose, buffer);
            if(!this.connectPos.isEmpty()) {
                for (BlockPos pos : this.connectPos) {
                    getInstance().drawLink(this.linkWidth, this.linkColor, this.pos, pos, pose, buffer);
                    getInstance().drawCube(this.cubeSize, pos, this.linkColor, pose, buffer);
                }
            }
        }
    }

    public static Vec3 getLawVec(Vec3 a, Vec3 b) {
        var normal = a.subtract(b);
        normal = normal.normalize();
        if (normal.equals(Vec3.ZERO)) {
            return Vec3.ZERO;
        }
        return new Vec3(normal.y - normal.z, normal.z - normal.x, normal.x - normal.y).normalize();
    }

    public static Vec3 getLawVec2(Vec3 a, Vec3 b) {
        var normal = a.subtract(b);
        normal = normal.normalize();
        if (normal.equals(Vec3.ZERO)) {
            return Vec3.ZERO;
        }
        double x = normal.x;
        double y = normal.y;
        double z = normal.z;
        return new Vec3(
                z*z-x*z+y*y-x*y,
                z*z-y*z-x*y+x*x,
                y*y+x*x-y*z-x*z
        ).normalize();
    }

    public static class ColorData {
        public float a;
        public float r;
        public float g;
        public float b;

        public ColorData(float a, float r, float g, float b) {
            this.a = a;
            this.r = r;
            this.g = g;
            this.b = b;
        }

        public ColorData(float r, float g, float b) {
            this(1, r, g, b);
        }

        public ColorData(int a, int r, int g, int b) {
            this(a / 255f, r / 255f, g / 255f, b / 255f);
        }

        public ColorData(int r, int g, int b) {
            this(255, r, g, b);
        }

        public ColorData(int argb) {
            this((argb >>> 24) & 0xFF, (argb >>> 16) & 0xFF, (argb >>> 8) & 0xFF, argb & 0xFF);
        }

        public float getAf() {
            return this.a;
        }

        public float getRf() {
            return this.r;
        }

        public float getGf() {
            return this.g;
        }

        public float getBf() {
            return this.b;
        }

        public int getAi() {
            return (int) (this.a * 255);
        }

        public int getRi() {
            return (int) (this.r * 255);
        }

        public int getGi() {
            return (int) (this.g * 255);
        }

        public int getBi() {
            return (int) (this.b * 255);
        }

        public int toARGB() {
            return (getAi() << 24) | (getRi() << 16) | (getGi() << 8) | getBi();
        }

        public int toRGBA() {
            return (getRi() << 24) | (getGi() << 16) | (getBi() << 8) | getAi();
        }

        public int toRGB() {
            return (getRi() << 16) | (getGi() << 8) | getBi();
        }

        @Override
        public String toString() {
            return "[a=%s, r=%s, g=%s, b=%s]".formatted(this.a, this.r, this.g, this.b);
        }

        @Override
        public int hashCode() {
            return toARGB();
        }

        @Override
        public boolean equals(Object obj) {
            if (obj instanceof ColorData cd) {
                return cd.toARGB() == toARGB();
            }
            return false;
        }

        public ColorData copy() {
            return new ColorData(a, r, g, b);
        }
    }
}
