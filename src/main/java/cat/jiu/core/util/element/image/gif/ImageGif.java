package cat.jiu.core.util.element.image.gif;

import cat.jiu.core.api.IData;
import cat.jiu.core.util.Utils;
import cat.jiu.core.util.client.GifDecoder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.resources.ResourceLocation;

public class ImageGif extends BaseGifImage {
    public static final ResourceLocation ID = Utils.location("jiucore", "element/image/gif/normal");

    public ImageGif() {
    }

    public ImageGif(GifDecoder.GifTexture gif) {
        super(gif);
    }

    @Override
    public ResourceLocation getImageType() {
        return ID;
    }

    @Override
    public IData.IMapData<?> write(IData.IMapData<?> data) {
        this.writeBaseInfo(data);
        IData.IListData<?> array = data.newList();
        for (int i = 0; i < this.getGif().getAllTextureCount(); i++) {
            array.putData(this.writeTo(data.newMap(), this.getGif().getImage(i)));
        }
        data.putData("frames", array);
        return data;
    }

    @Override
    public void read(IData.IMapData<?> data) {
        this.readBaseInfo(data);
        this.setGif(new GifDecoder.GifTextures());
        data.getList("frames", IData.IMapData.class, data.emptyList()).foreach((i, element) ->
            ((GifDecoder.GifTextures)this.getGif()).addTexture(new GifDecoder.SingletonGifTexture(this.readFrom(element.getAsMap())))
        );
    }
}
