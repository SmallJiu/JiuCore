package cat.jiu.core.api.events.iface.game;

import cat.jiu.core.api.IJiuEvent;
import net.minecraftforge.fml.relauncher.Side;

public interface IGameShutdown extends IJiuEvent {
	void shutdown(Side side);
}
