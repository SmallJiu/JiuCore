package cat.jiu.core.api.element;

import cat.jiu.core.api.IData;
import cat.jiu.core.api.serializable.IDataSerializable;
import cat.jiu.core.util.JsonUtils;
import cat.jiu.core.util.NBTUtils;
import cat.jiu.core.util.Utils;
import cat.jiu.core.util.element.data.JsonData;
import cat.jiu.core.util.element.data.NBTData;
import cat.jiu.core.util.element.image.*;
import cat.jiu.core.util.element.image.gif.*;
import cat.jiu.core.util.registry.DynamicRegistry2;
import com.google.gson.JsonObject;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.function.Supplier;

public interface IImage extends IDataSerializable<IData.IMapData<?>>,  Supplier<ResourceLocation> {
    String ID_NAME = "type";
    DynamicRegistry2<ResourceLocation, IImage> REGISTRY = new DynamicRegistry2<ResourceLocation, IImage>("jiucore", "element/image")
            .register(registry -> {
                registry.register(ImageGL.ID,                  ImageGL.class);
                registry.register(ImageBuffered.ID,            ImageBuffered.class);
                registry.register(ImageSingletonGif.ID,        ImageSingletonGif.class);
                registry.register(ImageGif.ID,                 ImageGif.class);
                registry.register(ImageGifs.ID,                ImageGifs.class);
                registry.register(ImageFilePath.ID,            ImageFilePath.class);
                registry.register(ImageJarPath.ID,             ImageJarPath.class);
                registry.register(ImageMC.ID,                  ImageMC.class);
            })
            .setKeyGetter(
                    data-> Utils.location(data.getString(ID_NAME, ""))
            );

    @OnlyIn(Dist.CLIENT)
    void render(GuiGraphics graphics, int renderToX, int renderToY, int renderWidth, int renderHeight, float partialTick);

    boolean isInitImage();
    void initImage();
    ResourceLocation getImageType();
    @Override
    default ResourceLocation get(){
        return this.getImageType();
    }

    default void setRenderInfo(int u, int v, int uWidth, int vHeight) {

    }
    default void setImageSize(int width, int height) {

    }

    default int getWidth() {
        return 50;
    }
    default int getHeight() {
        return 50;
    }

    @Deprecated(since = "1.20.1-0.0.1-2025.8.10")
    default JsonObject write(JsonObject data) {
        this.write(JsonData.map(data));
        return data;
    }
    @Deprecated(since = "1.20.1-0.0.1-2025.8.10")
    default void read(JsonObject data) {
        this.write(JsonData.map(data));
    }
    @Deprecated(since = "1.20.1-0.0.1-2025.8.10")
    default CompoundTag write(CompoundTag data) {
        this.write(NBTData.map(data));
        return data;
    }
    @Deprecated(since = "1.20.1-0.0.1-2025.8.10")
    default void read(CompoundTag data) {
        this.read(NBTData.map(data));
    }

    static abstract class BaseImage implements IImage {
        protected int width = 50, height = 50, u, v, uWidth, vHeight;
        public void setImageSize(int width, int height) {
            this.width = width;
            this.height = height;
        }
        public void setRenderInfo(int u, int v, int uWidth, int vHeight) {
            this.u = u;
            this.v = v;
            this.uWidth = uWidth;
            this.vHeight = vHeight;
        }

        public int getU() {
            return u;
        }

        public int getV() {
            return v;
        }

        public int getUWidth() {
            return uWidth;
        }

        public int getVHeight() {
            return vHeight;
        }

        public int getWidth() {
            return width;
        }

        public int getHeight() {
            return height;
        }

        protected boolean init;
        @OnlyIn(Dist.CLIENT)
        @Override
        public void initImage() {
            if (!this.isInitImage()) {
                this.init();
                this.init = true;
            }
        }
        @OnlyIn(Dist.CLIENT)
        protected abstract void init();
        @OnlyIn(Dist.CLIENT)
        @Override
        public boolean isInitImage() {
            return this.init;
        }

        @Deprecated(since = "1.20.1-0.0.1-2025.8.10")
        protected void writeBaseInfo(JsonObject data) {
            data.addProperty(ID_NAME, String.valueOf(this.getImageType()));
            data.addProperty("width", this.width);
            data.addProperty("height", this.height);
            data.addProperty("u", this.u);
            data.addProperty("v", this.v);
            data.addProperty("uWidth", this.uWidth);
            data.addProperty("vHeight", this.vHeight);
        }
        @Deprecated(since = "1.20.1-0.0.1-2025.8.10")
        protected void readBaseInfo(JsonObject data) {
            this.width = JsonUtils.get(data, "width", 50);
            this.height = JsonUtils.get(data, "height", 50);
            this.u = JsonUtils.get(data, "u", 0);
            this.v = JsonUtils.get(data, "v", 0);
            this.uWidth = JsonUtils.get(data, "uWidth", 0);
            this.vHeight = JsonUtils.get(data, "vHeight", 0);
        }
        @Deprecated(since = "1.20.1-0.0.1-2025.8.10")
        protected void writeBaseInfo(CompoundTag data) {
            data.putString(ID_NAME, String.valueOf(this.getImageType()));
            data.putInt("width", this.width);
            data.putInt("height", this.height);
            data.putInt("u", this.u);
            data.putInt("v", this.v);
            data.putInt("uWidth", this.uWidth);
            data.putInt("vHeight", this.vHeight);
        }
        @Deprecated(since = "1.20.1-0.0.1-2025.8.10")
        protected void readBaseInfo(CompoundTag data) {
            this.width = NBTUtils.get(data, "width", 50);
            this.height = NBTUtils.get(data, "height", 50);
            this.u = NBTUtils.get(data, "u", 0);
            this.v = NBTUtils.get(data, "v", 0);
            this.uWidth = NBTUtils.get(data, "uWidth", 0);
            this.vHeight = NBTUtils.get(data, "vHeight", 0);
        }

        @Override
        public void read(IData.IMapData<?> data) {
            this.readBaseInfo(data);
        }
        public void readBaseInfo(IData.IMapData<?> data) {
            this.width = data.getInt("width", 50);
            this.height = data.getInt("height", 50);
            this.u = data.getInt("u", 0);
            this.v = data.getInt("v", 0);
            this.uWidth = data.getInt("uWidth", 0);
            this.vHeight = data.getInt("vHeight", 0);
        }

        @Override
        public IData.IMapData<?> write(IData.IMapData<?> data) {
            this.writeBaseInfo(data);
            return data;
        }
        public void writeBaseInfo(IData.IMapData<?> data) {
            data.putData(ID_NAME, String.valueOf(this.getImageType()));
            data.putData("width", this.width);
            data.putData("height", this.height);
            data.putData("u", this.u);
            data.putData("v", this.v);
            data.putData("uWidth", this.uWidth);
            data.putData("vHeight", this.vHeight);
        }
    }
}
