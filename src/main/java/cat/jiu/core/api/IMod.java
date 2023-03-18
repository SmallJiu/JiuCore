package cat.jiu.core.api;

import javax.annotation.Nonnull;

import net.minecraftforge.fml.common.event.*;

public interface IMod {
	default void onConstruction(FMLConstructionEvent event) {
		this.getProxy().construction(event);
	}
	default void onPreInit(FMLPreInitializationEvent event) {
		this.getProxy().preInit(event);
	}
	default void onInit(FMLInitializationEvent event) {
		this.getProxy().init(event);
	}
	default void onPostInit(FMLPostInitializationEvent event) {
		this.getProxy().postInit(event);
	}
	default void onLoadComplete(FMLLoadCompleteEvent event) {
		this.getProxy().loadComplete(event);
	}
	
	default void onServerAboutToStart(FMLServerAboutToStartEvent event) {
		this.getProxy().onServerAboutToStart(event);
	}
	default void onServerStarting(FMLServerStartingEvent event) {
		this.getProxy().onServerStarting(event);
	}
	default void onServerStarted(FMLServerStartedEvent event) {
		this.getProxy().onServerStarted(event);
	}
	default void onServerCloseing(FMLServerStoppingEvent event) {
		this.getProxy().onServerCloseing(event);
	}
	default void onServerClosed(FMLServerStoppedEvent event) {
		this.getProxy().onServerClosed(event);
	}
	
	default void onIMC(FMLInterModComms.IMCEvent event) {
		this.getProxy().onIMC(event);
	}
	
	@Nonnull IProxy<?, ?> getProxy();
}
