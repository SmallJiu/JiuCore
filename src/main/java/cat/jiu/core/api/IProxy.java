package cat.jiu.core.api;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.event.*;
import net.minecraftforge.fml.relauncher.Side;

public interface IProxy<SP, CP extends SP> {
	
	default World getClientWorld() {return null;}
	default Side getSide() {return Side.SERVER;}
	default EntityPlayer getClientPlayer() {return null;}
	default String getLanguageCode() {return "en_us";}
	
	default void construction(FMLConstructionEvent event) {}
	default void preInit(FMLPreInitializationEvent event) {}
	default void init(FMLInitializationEvent event) {}
	default void postInit(FMLPostInitializationEvent event) {}
	default void loadComplete(FMLLoadCompleteEvent event) {}
	
	default void onServerAboutToStart(FMLServerAboutToStartEvent event) {}
	default void onServerStarting(FMLServerStartingEvent event) {}
	default void onServerCloseing(FMLServerStoppingEvent event) {}
	default void onServerStarted(FMLServerStartedEvent event) {}
	default void onServerClosed(FMLServerStoppedEvent event) {}
	
	default void onIMC(FMLInterModComms.IMCEvent event) {}
	
	@SuppressWarnings("unchecked")
	default CP getAsClientProxy() {return (CP) this;}
	@SuppressWarnings("unchecked")
	default SP getAsServerProxy() {return (SP) this;}
}
