package cat.jiu.core.util.client;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import cat.jiu.core.JiuCore;
import cat.jiu.core.util.JiuUtils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.IResourcePack;
import net.minecraft.client.resources.data.IMetadataSection;
import net.minecraft.client.resources.data.MetadataSerializer;
import net.minecraft.util.ResourceLocation;

import net.minecraftforge.fml.client.FMLFileResourcePack;
import net.minecraftforge.fml.client.FMLFolderResourcePack;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.ModContainer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class CustomResource {
	private static Map<String, Map<ResourceLocation, File>> resources;
	
	/**
	 * this method must use before {@link net.minecraftforge.fml.common.event.FMLPreInitializationEvent}
	 */
	public static void register(ResourceLocation loc) {
		if(resources==null)resources = Maps.newHashMap();
		String domain = loc.getResourceDomain();
		if(!resources.containsKey(domain)) resources.put(domain, Maps.newHashMap());
		resources.get(domain).put(loc, new File(JiuUtils.other.toStringPath(loc)));
	}
	
	public static void registerResource() {
		if(resources!=null&&!resources.isEmpty()) {
			for(String domain : resources.keySet()) {
				JiuCore.proxy.getAsClientProxy().addCustomResourcePack(new CustomFilePack(domain));
			}
		}
	}
	
	public static class CustomFilePack implements IResourcePack {
		protected final String domain;
		protected final Map<ResourceLocation, File> resources;
		protected final IResourcePack original;
		public CustomFilePack(String domain) {
			this.domain = domain;
			this.resources = CustomResource.resources.get(domain);
			if(Loader.isModLoaded(domain)) {
				ModContainer mod = Loader.instance().getIndexedModList().get(domain);
				this.original = mod.getSource().isDirectory() ? new FMLFolderResourcePack(mod) : new FMLFileResourcePack(mod); // for dev
			}else {
				this.original = Minecraft.getMinecraft().mcDefaultResourcePack;
			}
		}

		@Override
		public InputStream getInputStream(ResourceLocation location) throws IOException {
			return new FileInputStream(this.resources.get(location));
		}

		@Override
		public boolean resourceExists(ResourceLocation location) {
			return this.resources.containsKey(location) && this.resources.get(location).exists();
		}

		public Set<String> getResourceDomains() {return Sets.newHashSet(this.domain);}
		public <T extends IMetadataSection> T getPackMetadata(MetadataSerializer metadataSerializer, String metadataSectionName) throws IOException {return this.original.getPackMetadata(metadataSerializer, metadataSectionName);}
		public BufferedImage getPackImage() throws IOException {return this.original.getPackImage();}
		public String getPackName() {return "JiuCoreCustomResource: " + this.domain;}
	}
}
