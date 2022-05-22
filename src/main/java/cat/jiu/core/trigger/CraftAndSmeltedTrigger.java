package cat.jiu.core.trigger;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;

import net.minecraft.item.Item;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;

public class CraftAndSmeltedTrigger extends JiuCoreTriggers.ItemCriterionTrigger<CraftAndSmeltedTrigger> {
	protected CraftAndSmeltedTrigger(Item item, int meta) {
		super(new ResourceLocation("jc:craft_smelted_items"), item, meta);
	}
	@Override
	public CraftAndSmeltedTrigger getInstance(JsonObject json, JsonDeserializationContext context) {
		Item item = JsonUtils.getItem(json, "item");
		int meta = json.has("meta") ? json.get("meta").getAsInt() : 0;
		return new CraftAndSmeltedTrigger(item, meta);
	}
}
