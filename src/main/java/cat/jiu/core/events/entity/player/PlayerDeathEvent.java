package cat.jiu.core.events.entity.player;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.eventhandler.Event;

public class PlayerDeathEvent extends Event {
	public final EntityPlayer player;
	public final int count;
	public final int dimension;
	public final BlockPos position;
	
	public PlayerDeathEvent(EntityPlayer player, int count, int dimension, BlockPos position) {
		this.player = player;
		this.count = count;
		this.dimension = dimension;
		this.position = position;
	}
}
