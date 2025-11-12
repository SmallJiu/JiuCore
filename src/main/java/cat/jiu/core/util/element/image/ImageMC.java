package cat.jiu.core.util.element.image;

import cat.jiu.core.api.IData;
import cat.jiu.core.api.element.IImage;
import cat.jiu.core.util.Utils;
import cat.jiu.core.util.client.RenderUtils;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class ImageMC extends IImage.BaseImage {
    public static final ResourceLocation ID = Utils.location("jiucore", "element/image/mc");
    protected ResourceLocation texture;

    public ImageMC() {
    }

    public ImageMC(ResourceLocation texture) {
        this.texture = texture;
    }

    public ResourceLocation getTexture() {
        return texture;
    }

    public void setTexture(String texture) {
        this.setTexture(Utils.location(texture));
    }
    public void setTexture(ResourceLocation texture) {
        this.texture = texture;
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void render(GuiGraphics graphics, int renderToX, int renderToY, int renderWidth, int renderHeight, float partialTick) {
        RenderUtils.draw(graphics, this.texture, renderToX, renderToY, renderWidth, renderHeight, this.getU(), this.getV(), this.getUWidth(), this.getVHeight(), null);
    }

    @Override
    public ResourceLocation getImageType() {
        return ID;
    }

    @Override
    public void init() {

    }

    @Override
    public IData.IMapData<?> write(IData.IMapData<?> data) {
        this.writeBaseInfo(data);
        data.putData("texture", String.valueOf(this.texture));
        return data;
    }

    @Override
    public void read(IData.IMapData<?> data) {
        this.readBaseInfo(data);
        this.setTexture(data.getString("texture", ""));
    }
}
