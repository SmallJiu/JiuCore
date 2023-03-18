package cat.jiu.core.events.entity.player;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event;

@Cancelable
public class PlayerJoinWorldEvent extends Event {
	public final EntityPlayer entity;
	public final World world;
	public final BlockPos pos;
	public final int dim;
	public PlayerJoinWorldEvent(EntityPlayer entity, World world, BlockPos pos, int dim) {
		this.entity = entity;
		this.world = world;
		this.pos = pos;
		this.dim = dim;
	}
}
