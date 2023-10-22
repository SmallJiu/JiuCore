package cat.jiu.core.events.game;

import net.minecraft.world.World;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event;

@Cancelable
public class WorldTimeChangeEvent extends Event {
	public final World world;
	public final long oldTime;
	public final Class<?> wantChangeTimeCallClass;
	private long newTime;
	
	public WorldTimeChangeEvent(World world, Class<?> wantChangeTimeCallClass, long oldTime, long newTime) {
		this.world = world;
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
