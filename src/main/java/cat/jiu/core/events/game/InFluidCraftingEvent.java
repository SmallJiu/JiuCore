package cat.jiu.core.events.game;

import cat.jiu.core.util.crafting.Recipes;
import net.minecraftforge.fml.common.eventhandler.Event;

public class InFluidCraftingEvent extends Event {
	public final Recipes util;
	public InFluidCraftingEvent(Recipes recipes) {
		this.util = recipes;
	}
}
