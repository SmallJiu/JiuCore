package cat.jiu.core.api.events.mixin.clazz.game;

import cat.jiu.core.api.annotation.Unfinish;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event;

@Unfinish
@Cancelable
public class WorldTimeChangeEvent extends Event {
	public final long oldTime;
	public final Class<?> wantChangeTimeCallClass;
	private long newTime;
	
	public WorldTimeChangeEvent(Class<?> wantChangeTimeCallClass, long oldTime, long newTime) {
		this.wantChangeTimeCallClass = wantChangeTimeCallClass;
		this.oldTime = oldTime;
		this.newTime = newTime;
	}

	public long getNewWorldTime() {
		return newTime;
	}

	public void setNewWorldTime(long newTime) {
		this.newTime = newTime;
	}
}
