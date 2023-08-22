package cat.jiu.core.api;

import java.io.File;

import cat.jiu.core.util.client.DownloadResource;

public interface IBatchProgress {
	void call(DownloadResource.AllTasksInfo info, File file, long fileLength, long fileTotalLength, double progress, long speedPerSecond, long speedPerTick);
}
