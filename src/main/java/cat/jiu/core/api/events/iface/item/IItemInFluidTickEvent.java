package cat.jiu.core.api.events.iface.item;

import cat.jiu.core.api.IJiuEvent;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;

public interface IItemInFluidTickEvent extends IJiuEvent {
	/**
	 * 
	 * @param item Entity Item
	 * @param state Fluid state
	 * @author small_jiu
	 */
	void onItemInFluidTick(EntityItem item, IBlockState state);
}
