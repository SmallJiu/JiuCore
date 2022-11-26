package cat.jiu.core.proxy;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import cat.jiu.core.util.client.CustomResource;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.resources.IResourcePack;
import net.minecraft.client.resources.SimpleReloadableResourceManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;

public class ClientProxy extends ServerProxy {
	private static List<IResourcePack> packs;
	@Override
	public void preInit(FMLPreInitializationEvent event) {
		super.preInit(event);
		CustomResource.registerResource();
		if(packs!=null&&!packs.isEmpty()) {
			List<IResourcePack> resourcePackList = Minecraft.getMinecraft().defaultResourcePacks;
			
			if(resourcePackList != null && Minecraft.getMinecraft().getResourceManager() instanceof SimpleReloadableResourceManager) {
				SimpleReloadableResourceManager manger = ((SimpleReloadableResourceManager)Minecraft.getMinecraft().getResourceManager());
				
				for(IResourcePack pack : packs) {
					resourcePackList.add(pack);
					manger.reloadResourcePack(pack);
				}
			}
		}
		/*
		DownloadResource.register(new ResourceLocation("jc:textures/gui/dev0.png"), "https://img2.baidu.com/it/u=3328680008,2657549212&fm=253&fmt=auto&app=120&f=JPEG?w=889&h=500");
		DownloadResource.register(new ResourceLocation("jc:textures/gui/dev1.png"), "https://img0.baidu.com/it/u=2518378277,1696634197&fm=253&fmt=auto&app=138&f=JPEG?w=500&h=773");
		DownloadResource.register(new ResourceLocation("jc:textures/gui/dev2.png"), "https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fimg.jj20.com%2Fup%2Fallimg%2F4k%2Fs%2F02%2F2109242332225H9-0-lp.jpg&refer=http%3A%2F%2Fimg.jj20.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=auto?sec=1664937083&t=82c3e33f0584b3d5e045baee5f88c741");
		DownloadResource.register(new ResourceLocation("jc:textures/gui/dev3.png"), "http://t13.baidu.com/it/u=1118984865,1801461819&fm=224&app=112&f=JPEG?w=500&h=500");
		DownloadResource.register(new ResourceLocation("jc:textures/gui/dev4.png"), "https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fimg.jj20.com%2Fup%2Fallimg%2F1113%2F052420110515%2F200524110515-1-1200.jpg&refer=http%3A%2F%2Fimg.jj20.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=auto?sec=1664937083&t=26a1af5f5adfc6ae86f9f67936065528");
		new Thread(()->{
			DownloadResource.Info info = DownloadResource.download("jc");
			System.out.println(info);
			for(Entry<String, String> md5 : info.getFileMD5s().entrySet()) {
				System.out.println("File:" + md5.getKey() + ", MD5: " + md5.getValue());
			}
		}).start();
		*/
	}
	
	@Override
	public WorldClient getClientWorld() {
		return Minecraft.getMinecraft().world;
	}
	
	@Override
	public Side getSide() {
		return Side.CLIENT;
	}
	
	public boolean addCustomResourcePack(IResourcePack pack) {
		if(pack==null) return false;
		if(packs==null)packs = Lists.newArrayList();
		return packs.add(pack);
	}
	
	private static Map<ResourceLocation, InputStream> custom_resource;
	public void addCustomResource(ResourceLocation loc, InputStream stream) {
		if(custom_resource==null) custom_resource = Maps.newHashMap();
		custom_resource.put(loc, stream);
	}
	public boolean hasCustomResource(ResourceLocation loc) {
		if(custom_resource==null) return false;
		return custom_resource.containsKey(loc);
	}
	public InputStream getCustomResource(ResourceLocation loc) {
		if(custom_resource==null) return null;
		return custom_resource.get(loc);
	}
	
	public EntityPlayer getClientPlayer() {
		return Minecraft.getMinecraft().player;
	}
	
	public String getLanguageCode() {
        return FMLClientHandler.instance().getCurrentLanguage();
    }
	@Override
	public ClientProxy getAsClientProxy() {
		return this;
	}
}
