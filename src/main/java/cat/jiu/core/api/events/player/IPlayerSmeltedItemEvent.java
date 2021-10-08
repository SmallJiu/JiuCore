package cat.jiu.core.api.events.player;

import cat.jiu.core.api.IJiuEvent;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface IPlayerSmeltedItemEvent extends IJiuEvent{
	/**
	 * 
	 * @param player Entity Player
	 * @param stack Player craft ItemStack
	 * @param world Player World
	 * @param pos Player Pos
	 * 
	 * @author small_jiu
	 */
	void onPlayerSmeltedItem(EntityPlayer player, ItemStack stack, World world, BlockPos pos);
}
