package cat.jiu.core.util.base.exmp;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;

import cat.jiu.core.util.base.BaseAdvancement;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;

public class TestTrigger extends BaseAdvancement.BaseCriterionTrigger<TestTrigger> {
	public static final TestTrigger instance = new TestTrigger(null,0);
	protected final Item item;
	protected final int meta;
	
	/**
	 * Params use for <p>
	 * {@link Factory#deserializeInstance(JsonObject, JsonDeserializationContext)}, <p>
	 * instance trigger can be null
	 * @author small_jiu
	 */
	protected TestTrigger(Item item, int meta) {
		super(new ResourceLocation("mcs:test"));
		this.item = item;
		this.meta = meta;
	}
	
	@Override
	public TestTrigger getInstance(JsonObject json, JsonDeserializationContext context) {
		Item item = JsonUtils.getItem(json, "item");
		int meta = json.get("meta").getAsInt();
		return new TestTrigger(item, meta);
	}
	
	@Override
	public boolean check(Object... args) {
		if(args.length == 1 && args[0] instanceof ItemStack) {
			if(((ItemStack)args[0]).getMetadata() == this.meta
			&& ((ItemStack)args[0]).getItem() == this.item) {
				return true;
			}
		}
		return false;
	}
}
