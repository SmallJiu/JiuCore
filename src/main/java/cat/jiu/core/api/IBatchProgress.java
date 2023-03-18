package cat.jiu.core.api;

import java.io.File;

import cat.jiu.core.util.client.DownloadResource;

public interface IBatchProgress {
	void call(DownloadResource.InfoE info, File file, long fileLength, long fileTotalLength, double progress);
}
