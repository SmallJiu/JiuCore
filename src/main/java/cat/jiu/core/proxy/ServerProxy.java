package cat.jiu.core.proxy;

import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;;

public class ServerProxy {
	
	public void preInit(FMLPreInitializationEvent event) {
		
	}

	public void init(FMLInitializationEvent event) {
		
	}
	
	public World getClientWorld() {
		return null;
	}
	
	public boolean isClient() {
		return FMLCommonHandler.instance().getSide() != Side.CLIENT;
	}
}
