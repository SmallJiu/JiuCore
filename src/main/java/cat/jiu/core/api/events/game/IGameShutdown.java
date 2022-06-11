package cat.jiu.core.api.events.game;

import cat.jiu.core.api.IJiuEvent;
import net.minecraftforge.fml.relauncher.Side;

public interface IGameShutdown extends IJiuEvent {
	void shutdown(Side side);
}
