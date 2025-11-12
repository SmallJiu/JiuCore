package cat.jiu.core.util.element.image;

import cat.jiu.core.api.IData;
import cat.jiu.core.api.element.IImage;
import cat.jiu.core.util.Utils;
import cat.jiu.core.util.client.RenderUtils;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.awt.image.BufferedImage;

public class ImageGL extends IImage.BaseImage {
    public static final ResourceLocation ID = Utils.location("jiucore", "element/image/gl");
    protected int glID = -1;

    public ImageGL() {
    }

    public ImageGL(int glID) {
        this.glID = glID;
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void render(GuiGraphics graphics, int renderToX, int renderToY, int renderWidth, int renderHeight, float partialTick) {
        RenderUtils.draw(graphics, this.glID, renderToX, renderToY, renderWidth, renderHeight, this.getU(), this.getV(), this.getUWidth(), this.getVHeight(), null);
    }

    public int getGlID() {
        return glID;
    }

    public void setImage(BufferedImage image) {
        if (image != null) {
            this.setImage(RenderUtils.uploadGLTexture(image));
        }
    }
    public void setImage(int glID) {
        if (this.glID!=-1) {
            RenderSystem.deleteTexture(this.glID);
        }
        this.glID = glID;
    }

    @Override
    public void init() {

    }

    @Override
    public ResourceLocation getImageType() {
        return ID;
    }

    @Override
    public IData.IMapData<?> write(IData.IMapData<?> data) {
        super.write(data);
        data.putData("glID", this.glID);
        return data;
    }

    @Override
    public void read(IData.IMapData<?> data) {
        super.read(data);
        this.setImage(data.getInt("glID", -1));
    }
}
