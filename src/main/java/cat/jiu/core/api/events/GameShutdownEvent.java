package cat.jiu.core.api.events;

import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.relauncher.Side;

public class GameShutdownEvent extends Event {
	public final Side side;
	public GameShutdownEvent(Side side) {this.side = side;}
}
