package cat.jiu.core.exception;

public class Log4jBugException extends RuntimeException {
	private static final long serialVersionUID = -1178408565163194912L;
	public Log4jBugException() { }
	public Log4jBugException(final String s) { super(s); }
	public Log4jBugException(final String s, final Throwable t) { super(s, t); }
	public Log4jBugException(final Throwable t) {super(t); }
}
