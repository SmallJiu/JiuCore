package cat.jiu.core.api.events.iface.entity;

import cat.jiu.core.api.IJiuEvent;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;

public interface IEntityInFluidEvent extends IJiuEvent{
	/**
	 * 
	 * @param entity Entity
	 * @param state Fluid State
	 * @author small_jiu
	 */
	void onEntityInFluidTick(Entity entity, IBlockState state);
}
