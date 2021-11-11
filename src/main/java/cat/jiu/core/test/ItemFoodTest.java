package cat.jiu.core.test;

import cat.jiu.core.JiuCore;
import cat.jiu.core.util.base.BaseItem;

public class ItemFoodTest extends BaseItem.Food{
	public ItemFoodTest() {
		super(JiuCore.MODID, "test_food", 100, 100, false, JiuCore.CORE, true);
		this.setMaxMetadata(5);
		Init.ITEMS.add(this);
	}
}
