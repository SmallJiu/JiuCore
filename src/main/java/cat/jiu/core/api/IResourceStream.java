package cat.jiu.core.api;

import java.io.IOException;
import java.io.InputStream;

public interface IResourceStream {
	InputStream get() throws IOException ;
}
