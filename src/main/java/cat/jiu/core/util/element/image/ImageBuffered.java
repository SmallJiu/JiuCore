package cat.jiu.core.util.element.image;

import cat.jiu.core.api.IData;
import cat.jiu.core.util.Utils;
import cat.jiu.core.util.client.RenderUtils;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ImageBuffered extends ImageGL {
    public static final ResourceLocation ID = Utils.location("jiucore", "element/image/buffered");
    protected BufferedImage image;

    public ImageBuffered() {
    }

    public ImageBuffered(BufferedImage image) {
        this.setImage(image);
    }

    public ImageBuffered(BufferedImage image, int glID) {
        this.image = image;
        super.setImage(glID);
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void render(GuiGraphics graphics, int renderToX, int renderToY, int renderWidth, int renderHeight, float partialTick) {
        RenderUtils.draw(graphics, this.glID, renderToX, renderToY, renderWidth, renderHeight, this.u, this.v, this.uWidth, this.vHeight, this.image.getWidth(), this.image.getHeight());
    }

    @Override
    public void init() {
        super.setImage(this.image);
    }

    public BufferedImage getImage() {
        return image;
    }

    public void setImage(BufferedImage image) {
        this.image = image;
        if (this.getGlID() != -1) {
            RenderUtils.uploadGLTexture(this.getGlID(), image);
        }
    }

    @Override
    public ResourceLocation getImageType() {
        return ID;
    }

    @Override
    public IData.IMapData<?> write(IData.IMapData<?> data) {
        this.writeBaseInfo(data);
        return this.writeTo(data, this.getImage());
    }
    @Override
    public void read(IData.IMapData<?> data) {
        this.readBaseInfo(data);
        this.setImage(this.readFrom(data));
    }
    protected IData.IMapData<?> writeTo(IData.IMapData<?> data, BufferedImage image) {
        data.putData("width", image.getWidth());
        data.putData("height", image.getHeight());
        data.putData("colorType", image.getType());

        IData.IListData<?> pixels = data.newList();

        for (int u = 0; u < image.getWidth(); u++) {
            for (int v = 0; v < image.getHeight(); v++) {
                IData.IMapData<?> pixel = data.newMap();
                pixel.putData("u", u);
                pixel.putData("v", v);
                pixel.putData("color", image.getRGB(u, v));
                pixels.putData(pixel);
            }
        }
        data.putData("pixels", pixels);
        return data;
    }

    protected BufferedImage readFrom(IData.IMapData<?> data) {
        BufferedImage image = new BufferedImage(data.getInt("width", 0), data.getInt("height", 0), data.getInt("colorType", BufferedImage.TYPE_INT_ARGB));
        data.getList("pixels", IData.IListData.class, data.emptyList()).foreach((i, pixel) -> {
            IData.IMapData<?> pixelData = pixel.getAsMap();
            image.setRGB(pixelData.getInt("u", 0), pixelData.getInt("v", 0), pixelData.getInt("color", BufferedImage.TYPE_INT_ARGB));
        });
        return image;
    }

    public void writeToFile(File file) throws IOException {
        ImageIO.write(this.getImage(), "png", file);
    }






    @Deprecated(since = "1.20.1-0.0.1-2025.8.10")
    protected JsonObject writeTo(JsonObject data, BufferedImage image) {
        data.addProperty("width", image.getWidth());
        data.addProperty("height", image.getHeight());
        data.addProperty("colorType", image.getType());

        JsonArray pixels = new JsonArray();

        for (int u = 0; u < image.getWidth(); u++) {
            for (int v = 0; v < image.getHeight(); v++) {
                JsonObject pixel = new JsonObject();
                pixel.addProperty("u", u);
                pixel.addProperty("v", v);
                pixel.addProperty("color", image.getRGB(u, v));
                pixels.add(pixel);
            }
        }

        data.add("pixels", pixels);

        return data;
    }
    @Deprecated(since = "1.20.1-0.0.1-2025.8.10")
    protected BufferedImage readFrom(JsonObject data) {
        BufferedImage image = new BufferedImage(data.get("width").getAsInt(), data.get("height").getAsInt(), data.get("colorType").getAsInt());
        for (JsonElement pixelElement : data.getAsJsonArray("pixels")) {
            JsonObject pixel = pixelElement.getAsJsonObject();
            image.setRGB(pixel.get("u").getAsInt(), pixel.get("v").getAsInt(), pixel.get("color").getAsInt());
        }
        return image;
    }
    @Deprecated(since = "1.20.1-0.0.1-2025.8.10")
    protected CompoundTag writeTo(CompoundTag data, BufferedImage image) {
        data.putInt("width", image.getWidth());
        data.putInt("height", image.getHeight());
        data.putInt("colorType", image.getType());

        ListTag pixels = new ListTag();

        CompoundTag pixel = new CompoundTag();
        for (int u = 0; u < image.getWidth(); u++) {
            for (int v = 0; v < image.getHeight(); v++) {
                pixel.putInt("u", u);
                pixel.putInt("v", v);
                pixel.putInt("color", image.getRGB(u, v));
                pixels.add(pixel);
            }
        }

        data.put("pixels", pixels);
        return data;
    }
    @Deprecated(since = "1.20.1-0.0.1-2025.8.10")
    protected BufferedImage readFrom(CompoundTag data) {
        BufferedImage image = new BufferedImage(data.getInt("width"), data.getInt("height"), data.getInt("colorType"));
        ListTag pixels = data.getList("pixels", 10);
        for (int i = 0; i < pixels.size(); i++) {
            CompoundTag pixel = pixels.getCompound(i);
            image.setRGB(pixel.getInt("u"), pixel.getInt("v"), pixel.getInt("color"));
        }
        return image;
    }
}
