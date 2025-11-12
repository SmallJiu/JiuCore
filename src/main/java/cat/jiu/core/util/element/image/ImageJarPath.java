package cat.jiu.core.util.element.image;

import cat.jiu.core.api.IData;
import cat.jiu.core.util.Utils;
import cat.jiu.core.util.element.image.gif.BaseGifImage;
import cat.jiu.core.util.client.GifDecoder;
import net.minecraft.resources.ResourceLocation;

import javax.imageio.ImageIO;
import java.util.*;

public class ImageJarPath extends BaseGifImage {
    public static final ResourceLocation ID = Utils.location("jiucore", "element/image/path/jar");
    protected static final List<Function2<Class<?>, String, GifDecoder.IGifTexture>> DECODERS = new ArrayList<>();
    public static void register(Function2<Class<?>, String, GifDecoder.IGifTexture> decoder) {
        DECODERS.add(decoder);
    }
    public static interface Function2<T1, T2, R> {
        R apply(T1 t1, T2 t2);
    }

    protected final Map<Class<?>, Set<String>> paths = new HashMap<>();
    public ImageJarPath() {
    }

    public Map<Class<?>, Set<String>> getPaths() {
        return paths;
    }
    public ImageJarPath addPaths(Class<?> clazz, String... paths) {
        if (!this.paths.containsKey(clazz)) {
            this.paths.put(clazz, new HashSet<>());
        }
        for (String path : paths) {
            this.paths.get(clazz).add(path);
        }
        return this;
    }

    @Override
    public void init() {
        this.loadImages();
    }

    public ImageJarPath loadImages() {
        this.setGif(read(this.paths));
        return this;
    }

    public static GifDecoder.IGifTexture read(Map<Class<?>, Set<String>> paths) {
        if (paths.size() > 1) {
            GifDecoder.GifTextures image = new GifDecoder.GifTextures();
            paths.forEach((k, v)->
                image.addTexture(read(k, v))
            );
            return image;
        }
        Class<?> clazz = paths.keySet().toArray(new Class<?>[1])[0];
        return read(clazz, paths.get(clazz));
    }

    public static GifDecoder.IGifTexture read(Class<?> clazz, Set<String> paths) {
        if (paths.size() > 1) {
            GifDecoder.GifTextures image = new GifDecoder.GifTextures();
            for (String file : paths) {
                try {
                    if (file.endsWith(".gif")) {
                        image.addTexture(GifDecoder.getTexture(clazz.getResourceAsStream(file), -1));
                    }else if (file.endsWith(".png")) {
                        image.addTexture(GifDecoder.singleton(ImageIO.read(clazz.getResourceAsStream(file))));

                    }else {
                        for (Function2<Class<?>, String, GifDecoder.IGifTexture> decoder : DECODERS) {
                            GifDecoder.IGifTexture texture = decoder.apply(clazz, file);
                            if (texture!=null) {
                                image.addTexture(texture);
                                break;
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return image;
        }else {
            String path = paths.toArray(new String[1])[0];
            if (path.endsWith(".gif")) {
                return GifDecoder.getTexture(clazz.getResourceAsStream(path), -1);
            }else if (path.endsWith(".png")) {
                try {
                    return GifDecoder.singleton(ImageIO.read(clazz.getResourceAsStream(path)));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }else {
                for (Function2<Class<?>, String, GifDecoder.IGifTexture> decoder : DECODERS) {
                    GifDecoder.IGifTexture texture = decoder.apply(clazz, path);
                    if (texture!=null) {
                        return texture;
                    }
                }
            }
        }
        return new GifDecoder.GifTextures();
    }

    @Override
    public ResourceLocation getImageType() {
        return ID;
    }

    @Override
    public IData.IMapData<?> write(IData.IMapData<?> data) {
        this.writeBaseInfo(data);
        IData.IListData<?> array = data.newList();
        this.paths.forEach((clazz, list)->{
            IData.IMapData<?> object = data.newMap();
            object.putData("clazz", String.valueOf(clazz));

            IData.IListData<?> array1 = data.newList();
            list.forEach(array1::putData);
            object.putData("path", array1);

            array.putData(object);
        });
        data.putData("paths", array);
        return data;
    }

    @Override
    public void read(IData.IMapData<?> data) {
        this.readBaseInfo(data);
        data.getMap("paths", data.emptyMap()).foreach((k,v) -> {
            IData.IMapData<?> object = v.getAsMap();
            try {
                Class<?> clazz = Class.forName(object.getString("clazz", ""));
                object.getList("path", String.class, data.emptyList()).foreach((k1,v1)->
                    this.addPaths(clazz, v1.getAsPrimitive().getAsString())
                );
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
