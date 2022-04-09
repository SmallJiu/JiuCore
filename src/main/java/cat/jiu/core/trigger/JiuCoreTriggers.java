package cat.jiu.core.trigger;

import cat.jiu.core.util.base.BaseAdvancement.BaseCriterionTrigger;
import cat.jiu.core.util.base.BaseAdvancement.BaseCriterionTrigger.ICriterionTriggerFactory;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class JiuCoreTriggers {
	public static final CraftAndSmeltedTrigger CRAFT_ITEM = new CraftAndSmeltedTrigger(null, -1);
	public static final PickupItemTrigger PICKUP_ITEM = new PickupItemTrigger(null, -1);
	public static final BreakBlockTrigger BREAK_BLOCK = new BreakBlockTrigger(null);
	public static final PlayerDeathTrigger PLAYER_DEATH = new PlayerDeathTrigger(-1,-1);
	public static void register() {
		CriteriaTriggers.register(CRAFT_ITEM);
		CriteriaTriggers.register(PICKUP_ITEM);
		CriteriaTriggers.register(BREAK_BLOCK);
		CriteriaTriggers.register(PLAYER_DEATH);
	}
	
	public static abstract class ItemCriterionTriggerFactory<T extends BaseCriterionTrigger<T>> implements ICriterionTriggerFactory<T> {
		protected final Item item;
		protected final int meta;
		public ItemCriterionTriggerFactory(Item item, int meta) {
			this.item = item;
			this.meta = meta;
		}

		@Override
		public boolean check(Object... args) {
			if(args.length >= 1) {
				if(args[0] instanceof ItemStack) {
					 ItemStack stack = (ItemStack) args[0];
					 if(stack.getItem() == this.item) {
						 if(this.meta != -1) {
							 if(stack.getMetadata() == this.meta) {
								 return true;
							 }
						 }else {
							 return true;
						 }
					 }
				}
			}
			return false;
		}
	}
}
