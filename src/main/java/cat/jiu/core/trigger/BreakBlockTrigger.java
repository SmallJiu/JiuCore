package cat.jiu.core.trigger;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;

import cat.jiu.core.JiuCore;
import cat.jiu.core.util.JiuUtils;

import cat.jiu.core.util.base.BaseAdvancement;
import cat.jiu.core.util.base.BaseAdvancement.BaseCriterionTrigger;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;

public class BreakBlockTrigger extends BaseAdvancement.BaseCriterionTrigger<BreakBlockTrigger> {
	protected BreakBlockTrigger(IBlockState state) {
		super(new ResourceLocation("jc:break_block"), new BreakBlockFactory<BreakBlockTrigger>(state) {
			@Override
			public BreakBlockTrigger deserializeInstance(JsonObject json, JsonDeserializationContext context) {
				Item item = JsonUtils.getItem(json, "block");
				int meta = json.has("meta") ? json.get("meta").getAsInt() : 0;
				IBlockState state = JiuUtils.item.getStateFromItemStack(new ItemStack(item, 1, meta));
				if(state == null) {
					JiuCore.instance.log.error(json.get("block").getAsString() + " is NOT Block!");
					return new BreakBlockTrigger(null);
				}
				return new BreakBlockTrigger(state);
			}
		});
	}
	public static abstract class BreakBlockFactory<I extends BaseCriterionTrigger<I>> implements ICriterionTriggerFactory<I> {
		protected final IBlockState state;
		public BreakBlockFactory(IBlockState state) {
			this.state = state;
		}
		@Override
		public boolean check(Object... args) {
			if(args.length >= 1) {
				if(args[0] instanceof IBlockState) {
					if(this.state != null) {
						IBlockState state = (IBlockState) args[0];
						if(state.getBlock() == this.state.getBlock()) {
							return true;
						}
					}
				}
			}
			return false;
		}
	}
}
