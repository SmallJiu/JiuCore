package cat.jiu.core.util.element.image.gif;

import cat.jiu.core.api.IData;
import cat.jiu.core.util.Utils;
import cat.jiu.core.util.client.GifDecoder;
import com.google.gson.JsonObject;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;

public class ImageGifs extends BaseGifImage {
    public static final ResourceLocation ID = Utils.location("jiucore", "element/image/gif/more");

    protected GifDecoder.GifTextures gif;
    public ImageGifs() {
    }

    public ImageGifs(GifDecoder.GifTextures gif) {
        super(gif);
        this.gif = gif;
    }

    public void setGif(GifDecoder.GifTextures gif) {
        this.gif = gif;
    }

    @Override
    public GifDecoder.GifTextures getGif() {
        return gif;
    }

    @Override
    public ResourceLocation getImageType() {
        return ID;
    }

    @Override
    public IData.IMapData<?> write(IData.IMapData<?> data) {
        this.writeBaseInfo(data);
        return data;
    }

    @Override
    public void read(IData.IMapData<?> data) {
        this.readBaseInfo(data);

    }
}
