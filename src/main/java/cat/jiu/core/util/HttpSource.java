package cat.jiu.core.util;

import cat.jiu.core.api.IData;
import cat.jiu.core.api.serializable.IDataSerializable;

import javax.annotation.Nullable;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HttpSource implements IDataSerializable<IData.IMapData<?>> {
    public static HttpSource create(IData.IMapData<?> data) {
        HttpSource source = new HttpSource();
        if (data!=null) {
            source.read(data);
        }
        return source;
    }

    private File saveFile;
    private List<String> urls;

    public HttpSource() {
    }

    public HttpSource(File saveFile, String... urls) {
        this.setSaveFile(saveFile);
        this.addUrls(urls);
    }

    public File getSaveFile() {
        return saveFile;
    }

    public HttpSource setSaveFile(File saveFile) {
        this.saveFile = saveFile;
        return this;
    }

    public List<String> getUrls() {
        return urls;
    }

    public HttpSource addUrls(String... urls) {
        if (this.urls == null) {
            this.urls = new ArrayList<>();
        }
        this.urls.addAll(Arrays.asList(urls));
        return this;
    }
    public HttpSource setUrls(String... urls) {
        this.urls = new ArrayList<>(Arrays.asList(urls));
        return this;
    }
    public HttpSource setUrls(List<String> urls) {
        this.urls = urls;
        return this;
    }

    public void tryDownload(FileDownload.IProgress progress, @Nullable Runnable done, @Nullable Runnable fail) {
        new Thread(()->{
            List<String> urls = this.getUrls();
            boolean downloaded = false;
            if (urls != null) {
                for (String url : urls) {
                    if (this.download(url, progress)) {
                        if(done != null) done.run();
                        downloaded = true;
                        break;
                    }
                }
            }
            if (!downloaded && fail != null) {
                fail.run();
            }
        }).start();
    }

    protected boolean download(String url, FileDownload.IProgress progress) {
        return FileDownload.download(url, this.getSaveFile(), progress);
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
