package cat.jiu.core.util.crafting;

import java.util.HashMap;
import java.util.Map;

import cat.jiu.core.JiuCore;
import cat.jiu.core.util.JiuUtils;

import net.minecraft.item.ItemStack;

import net.minecraftforge.event.AnvilUpdateEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@EventBusSubscriber
public final class AnvilRecipe {
	private static final Map<Integer, AnvilRecipeType> recipeType = new HashMap<Integer, AnvilRecipeType>();
	
	public static void addAnvilRecipe(ItemStack left, ItemStack right, ItemStack out, int expLevel) {
		recipeType.put(recipeType.size(), new AnvilRecipeType(left, right, out, expLevel));
	}
	
	@SubscribeEvent
	public static void onAnvilUpdate(AnvilUpdateEvent event) {
		if(!recipeType.isEmpty()) {
			for(int i : recipeType.keySet()) {
				AnvilRecipeType type = recipeType.get(i);
				
				if(JiuUtils.item.equalsStack(type.getLeftInput(), event.getLeft())
				&& JiuUtils.item.equalsStack(type.getRightInput(), event.getRight())) {
					ItemStack stack = type.getOutput();
//					stack.setCount(stack.getCount() * Math.min(event.getLeft().getCount(), event.getRight().getCount()));
					event.setCost(type.getExpLevel());
					event.setOutput(stack);
					JiuCore.instance.log.info("AnvilUpdate1");
				}
			}
		}
		JiuCore.instance.log.info("AnvilUpdate0");
	}
	
	public static final class AnvilRecipeType {
		private final ItemStack left;
		private final ItemStack right;
		private final ItemStack out;
		private final int expLevel;
		
		public AnvilRecipeType(ItemStack left, ItemStack right, ItemStack out, int expLevel) {
			this.left = left;
			this.right = right;
			this.out = out;
			this.expLevel = expLevel;
		}
		
		public ItemStack getLeftInput() {
			return this.left;
		}

		public ItemStack getRightInput() {
			return this.right;
		}
		
		public ItemStack getOutput() {
			return this.out;
		}
		
		public int getExpLevel() {
			return this.expLevel;
		}
	}
}
