package cat.jiu.core.api.events.player;

import cat.jiu.core.api.IJiuEvent;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface IPlayerCraftedItemEvent extends IJiuEvent{
	/**
	 * 
	 * @param player Entity Player
	 * @param gui The craft gui
	 * @param stack Player get ItemStack
	 * @param world Player World
	 * @param pos Player Pos
	 * 
	 * @author small_jiu
	 * @param gui 
	 */
	void onPlayerCraftedItemInGui(EntityPlayer player, IInventory gui, ItemStack stack, World world, BlockPos pos);
}
