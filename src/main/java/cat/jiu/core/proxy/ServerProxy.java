package cat.jiu.core.proxy;

import net.minecraft.world.World;

import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class ServerProxy {
	
	public void preInit(FMLPreInitializationEvent event) {
		
	}

	public void init(FMLInitializationEvent event) {
		
	}
	
	public World getClientWorld() {
		return null;
	}
	
	public boolean isClient() {
		return false;
	}
}
