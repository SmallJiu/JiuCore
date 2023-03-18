package cat.jiu.core.events.item;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraftforge.fml.common.eventhandler.Event;

public class ItemInWorldEvent extends Event {
	public final EntityItem item;
	protected ItemInWorldEvent(EntityItem e) {
		this.item = e;
	}
	public static class InWorld extends ItemInWorldEvent {
		public InWorld(EntityItem e) {
			super(e);
		}
	}
	public static class InVoid extends ItemInWorldEvent {
		public InVoid(EntityItem e) {
			super(e);
		}
	}
	public static class InFluid extends ItemInWorldEvent {
		public final IBlockState fluid;
		public InFluid(EntityItem e, IBlockState fluid) {
			super(e);
			this.fluid = fluid;
		}
	}
}
