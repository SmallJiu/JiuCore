package cat.jiu.core.api;

public interface IProgress {
	void call(long fileLength, long fileTotalLength,  double progress, long speedPerSecond, long speedPerTick);
}
