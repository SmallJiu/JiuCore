package cat.jiu.core.proxy;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import com.google.common.collect.Maps;

import cat.jiu.core.api.IProxy;
import cat.jiu.core.api.IResourceStream;

import net.minecraft.util.ResourceLocation;

public class ServerProxy implements IProxy<ServerProxy, ClientProxy>{
	private static Map<ResourceLocation, IResourceStream> custom_resource;
	public void addCustomResource(ResourceLocation loc, IResourceStream stream) {
		if(custom_resource==null) custom_resource = Maps.newHashMap();
		custom_resource.put(loc, stream);
	}
	public boolean hasCustomResource(ResourceLocation loc) {
		if(custom_resource==null) return false;
		return custom_resource.containsKey(loc);
	}
	public InputStream getCustomResource(ResourceLocation loc) throws IOException {
		if(custom_resource==null || !custom_resource.containsKey(loc)) return null;
		return custom_resource.get(loc).get(loc);
	}
}
