package cat.jiu.core.trigger;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;

import cat.jiu.core.JiuCore;
import cat.jiu.core.util.JiuUtils;
import cat.jiu.core.util.base.BaseAdvancement;

import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;

public class BreakBlockTrigger extends BaseAdvancement.BaseCriterionTrigger<BreakBlockTrigger> {
	protected final IBlockState state;
	protected BreakBlockTrigger(IBlockState state) {
		super(new ResourceLocation("jc:break_block"));
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

	@Override
	public BreakBlockTrigger getInstance(JsonObject json, JsonDeserializationContext context) {
		Item item = JsonUtils.getItem(json, "block");
		int meta = json.has("meta") ? json.get("meta").getAsInt() : 0;
		IBlockState state = JiuUtils.item.getStateFromItemStack(new ItemStack(item, 1, meta));
		if(state == null) {
			JiuCore.getLogOS().error(json.get("block").getAsString() + " is NOT Block!");
			return new BreakBlockTrigger(null);
		}
		return new BreakBlockTrigger(state);
	}
}
