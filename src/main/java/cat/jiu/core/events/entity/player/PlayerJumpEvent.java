package cat.jiu.core.events.entity.player;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.eventhandler.Event;

public class PlayerJumpEvent extends Event {
	public final EntityPlayer player;
	public PlayerJumpEvent(EntityPlayer player) {
		this.player = player;
	}
}
