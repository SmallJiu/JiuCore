package cat.jiu.core.proxy;

import java.lang.reflect.Field;
import java.util.List;

import com.google.common.collect.Lists;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.IResourcePack;
import net.minecraft.client.resources.SimpleReloadableResourceManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;

@SuppressWarnings("unchecked")
public class ClientProxy extends ServerProxy {
	private final List<IResourcePack> packs = Lists.newArrayList();
	@Override
	public void preInit(FMLPreInitializationEvent event) {
		super.preInit(event);
		if(!this.packs.isEmpty()) {
			try {
				Field resourceList = FMLClientHandler.class.getDeclaredField("resourcePackList");
				resourceList.setAccessible(true);
				List<IResourcePack> resourcePackList = (List<IResourcePack>) resourceList.get(FMLClientHandler.instance());
				
				if(resourcePackList != null && Minecraft.getMinecraft().getResourceManager() instanceof SimpleReloadableResourceManager) {
					SimpleReloadableResourceManager manger = ((SimpleReloadableResourceManager)Minecraft.getMinecraft().getResourceManager());
					for(IResourcePack pack : packs) {
						resourcePackList.add(pack);
						manger.reloadResourcePack(pack);
					}
				}
			}catch(NoSuchFieldException | IllegalAccessException | IllegalArgumentException | SecurityException e) {
				e.printStackTrace();
			}
		}
	}
	
	@Override
	public void init(FMLInitializationEvent event) {
		super.init(event);
	}
	
	@Override
	public World getClientWorld() {
		return Minecraft.getMinecraft().world;
	}
	
	@Override
	public Side getSide() {
		return Side.CLIENT;
	}
	
	public boolean addCustomResourcePack(IResourcePack pack) {
		if(pack == null) return false;
		return this.packs.add(pack);
	}
	
	public EntityPlayer getClientPlayer() {
		return Minecraft.getMinecraft().player;
	}
	
	public String getLanguage() {
        return FMLClientHandler.instance().getCurrentLanguage();
    }
}
