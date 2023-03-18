package cat.jiu.core.api;

import java.io.IOException;
import java.io.InputStream;

import net.minecraft.util.ResourceLocation;

public interface IResourceStream {
	InputStream get(ResourceLocation location) throws IOException;
}
