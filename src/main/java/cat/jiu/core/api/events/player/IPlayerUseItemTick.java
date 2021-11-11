package cat.jiu.core.api.events.player;

import cat.jiu.core.api.IJiuEvent;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface IPlayerUseItemTick extends IJiuEvent{
	void onPlayerUseItemTick(ItemStack stack, EntityPlayer player, World world, BlockPos pos);
}
