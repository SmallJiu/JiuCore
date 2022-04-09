package cat.jiu.core.trigger;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;

import cat.jiu.core.util.base.BaseAdvancement;

import net.minecraft.item.Item;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;

public class CraftAndSmeltedTrigger extends BaseAdvancement.BaseCriterionTrigger<CraftAndSmeltedTrigger> {
	protected CraftAndSmeltedTrigger(Item item, int meta) {
		super(new ResourceLocation("jc:craft_smelted_items"), new JiuCoreTriggers.ItemCriterionTriggerFactory<CraftAndSmeltedTrigger>(item, meta) {
			public CraftAndSmeltedTrigger deserializeInstance(JsonObject json, JsonDeserializationContext context) {
				Item item = JsonUtils.getItem(json, "item");
				int meta = json.has("meta") ? json.get("meta").getAsInt() : 0;
				return new CraftAndSmeltedTrigger(item, meta);
			}
		});
	}
}
