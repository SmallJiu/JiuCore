package cat.jiu.core.api.events.player;

import cat.jiu.core.api.IJiuEvent;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface IPlayerJump extends IJiuEvent{
	void onPlayerJump(EntityPlayer player, BlockPos ePos, World eWorld);
}
