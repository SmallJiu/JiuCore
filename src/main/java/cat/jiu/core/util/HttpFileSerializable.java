package cat.jiu.core.util;

import cat.jiu.core.api.IData;
import cat.jiu.core.api.serializable.IDataSerializable;

import java.io.File;
import java.util.List;

public class HttpFileSerializable extends HttpFile implements IDataSerializable<IData.IMapData<?>> {
    public static HttpFileSerializable create(IData.IMapData<?> data) {
        HttpFileSerializable source = new HttpFileSerializable();
        if (data!=null) {
            source.read(data);
        }
        return source;
    }

    public HttpFileSerializable() {
    }

    public HttpFileSerializable(File saveFile, String... urls) {
        super(saveFile, urls);
    }

    @Override
    public void read(IData.IMapData<?> data) {
        this.setSaveFile(new File(data.getString("file")));
        data.getList("urls", String.class).foreach((index, value) ->
                this.addUrls(value.getAsPrimitive().getAsString())
        );
    }

    @Override
    public IData.IMapData<?> write(IData.IMapData<?> data) {
        data.putData("file", this.getSaveFile().getPath());
        List<String> u = this.getUrls();
        if (u != null) {
            IData.IListData<?> urls = data.newList();
            for (String url : u) {
                urls.putData(url);
            }
            data.putData("urls", urls);
        }
        return data;
    }
}
