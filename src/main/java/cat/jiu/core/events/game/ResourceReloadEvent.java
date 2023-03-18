package cat.jiu.core.events.game;

import net.minecraftforge.fml.common.eventhandler.Event;

public class ResourceReloadEvent extends Event {
	public static class Pre extends ResourceReloadEvent {}
	public static class Post extends ResourceReloadEvent {}
}
