package cat.jiu.core.exception;

public class UnsupportedException extends Throwable {
	private static final long serialVersionUID = -1178408565163194912L;
	public UnsupportedException() { }
	public UnsupportedException(final String s) { super(s); }
	public UnsupportedException(final String s, final Throwable t) { super(s, t); }
	public UnsupportedException(final Throwable t) {super(t); }
}
