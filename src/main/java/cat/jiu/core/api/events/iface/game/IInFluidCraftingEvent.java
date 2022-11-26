package cat.jiu.core.api.events.iface.game;

import cat.jiu.core.api.IJiuEvent;
import cat.jiu.core.util.crafting.Recipes;

public interface IInFluidCraftingEvent extends IJiuEvent{
	/**
	 * For recipe reload
	 * 
	 * @param rec
	 */
	void onAddInFluidCrafting(Recipes rec);
}
