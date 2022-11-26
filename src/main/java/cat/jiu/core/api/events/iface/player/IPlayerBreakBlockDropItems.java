package cat.jiu.core.api.events.iface.player;

import java.util.List;

import cat.jiu.core.api.IJiuEvent;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface IPlayerBreakBlockDropItems extends IJiuEvent{
	/**
	 * 
	 * @param player
	 * @param world
	 * @param pos
	 * @param state
	 * @param drops the drop list, you can add or remove drop items
	 * @param dropChance the item drop chance, you can set to change the chance
	 * 
	 * @author small_jiu
	 * @return dropChance
	 */
	float onPlayerBreakBlockDropItems(EntityPlayer player, World world, BlockPos pos, IBlockState state, List<ItemStack> drops, float dropChance);
}
