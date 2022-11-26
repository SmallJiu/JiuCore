package cat.jiu.core.api;

import net.minecraftforge.fml.common.event.*;

public interface IMod {
	default void onConstruction(FMLConstructionEvent event) {}
	default void onPreInit(FMLPreInitializationEvent event) {}
	default void onInit(FMLInitializationEvent event) {}
	default void onPostInit(FMLPostInitializationEvent event) {}
	default void onLoadComplete(FMLLoadCompleteEvent event) {}
	
	default void onServerAboutToStart(FMLServerAboutToStartEvent event) {}
	default void onServerStarting(FMLServerStartingEvent event) {}
	default void onServerCloseing(FMLServerStoppingEvent event) {}
	default void onServerStarted(FMLServerStartedEvent event) {}
	default void onServerClosed(FMLServerStoppedEvent event) {}

	default void onIMC(FMLInterModComms.IMCEvent event) {}
}
