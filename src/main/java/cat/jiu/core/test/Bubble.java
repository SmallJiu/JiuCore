package cat.jiu.core.test;

import cat.jiu.core.JiuCore;
import cat.jiu.core.util.base.BaseItem;

public class Bubble extends BaseItem.Normal{
	public Bubble() {
		super(JiuCore.MODID, "bubble", JiuCore.CORE);
		Init.ITEMS.add(this);
	}

}
