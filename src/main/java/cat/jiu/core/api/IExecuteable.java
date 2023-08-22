package cat.jiu.core.api;

public interface IExecuteable {
	boolean execute(int tryCount, Throwable lastExecuteException);
}
