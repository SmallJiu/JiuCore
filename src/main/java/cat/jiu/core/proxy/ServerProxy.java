package cat.jiu.core.proxy;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import com.google.common.collect.Maps;

import cat.jiu.core.api.IProxy;
import cat.jiu.core.api.IResourceStream;
import net.minecraft.util.ResourceLocation;

public class ServerProxy implements IProxy<ServerProxy, ClientProxy> {
	protected Map<ResourceLocation, IResourceStream> resources;
	
	public void addCustomResource(ResourceLocation loc, IResourceStream stream) {
		if(this.resources==null) this.resources = Maps.newHashMap();
		this.resources.put(loc, stream);
	}
	public boolean hasCustomResource(ResourceLocation loc) {
		if(this.resources==null) return false;
		return this.resources.containsKey(loc);
	}
	public InputStream getCustomResource(ResourceLocation loc) throws IOException {
		if(this.resources==null || !this.hasCustomResource(loc)) return null;
		return this.resources.get(loc).get();
	}
}
