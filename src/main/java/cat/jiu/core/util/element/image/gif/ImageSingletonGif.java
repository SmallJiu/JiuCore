package cat.jiu.core.util.element.image.gif;

import cat.jiu.core.util.Utils;
import cat.jiu.core.util.element.image.ImageBuffered;
import cat.jiu.core.util.client.GifDecoder;
import net.minecraft.resources.ResourceLocation;

public class ImageSingletonGif extends ImageBuffered {
    public static final ResourceLocation ID = Utils.location("jiucore", "element/image/gif/singleton");

    protected GifDecoder.SingletonGifTexture gif;
    public ImageSingletonGif() {
    }

    public ImageSingletonGif(GifDecoder.SingletonGifTexture gif) {
        super(gif.getImage(0), gif.getCurrentTextureID());
        this.gif = gif;
    }

    @Override
    public ResourceLocation getImageType() {
        return ID;
    }
}
