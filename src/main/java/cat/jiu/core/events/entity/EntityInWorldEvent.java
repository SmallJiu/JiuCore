package cat.jiu.core.events.entity;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraftforge.event.entity.EntityEvent;

public class EntityInWorldEvent extends EntityEvent {
	protected EntityInWorldEvent(Entity entity) {
		super(entity);
	}
	public static class InFluid extends EntityInWorldEvent {
		public final IBlockState fluid;
		public InFluid(Entity entity, IBlockState fluid) {
			super(entity);
			this.fluid = fluid;
		}
	}
	public static class InVoid extends EntityInWorldEvent {
		public InVoid(Entity entity) {
			super(entity);
		}
	}
}
