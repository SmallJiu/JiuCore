package cat.jiu.core.proxy;

import java.util.List;

import com.google.common.collect.Lists;

import cat.jiu.core.util.client.CustomResource;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.resources.IResourcePack;
import net.minecraft.client.resources.SimpleReloadableResourceManager;
import net.minecraft.entity.player.EntityPlayer;

import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

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
	}
	
	@Override
	public WorldClient getClientWorld() {
		return Minecraft.getMinecraft().world;
	}
	
	public boolean addCustomResourcePack(IResourcePack pack) {
		if(pack==null) return false;
		if(packs==null)packs = Lists.newArrayList();
		return packs.add(pack);
	}
	
	public EntityPlayer getClientPlayer() {
		return Minecraft.getMinecraft().player;
	}
	
	public String getLanguageCode() {
        return FMLClientHandler.instance().getCurrentLanguage();
    }
	
	@Override
	public boolean isClient() {
		return true;
	}
}
