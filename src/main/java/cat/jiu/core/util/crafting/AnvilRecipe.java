package cat.jiu.core.util.crafting;

import java.util.HashMap;
import java.util.Map;

import cat.jiu.core.util.JiuUtils;

import net.minecraft.item.ItemStack;

import net.minecraftforge.event.AnvilUpdateEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@EventBusSubscriber
public final class AnvilRecipe {
	private static final Map<Integer, AnvilRecipeType> recipeType = new HashMap<Integer, AnvilRecipeType>();
	
	public static void addAnvilRecipe(AnvilRecipeType type) {
		recipeType.put(recipeType.size(), type);
	}
	
	public static void addAnvilRecipe(ItemStack left, ItemStack right, ItemStack out, int expLevel) {
		addAnvilRecipe(new AnvilRecipeType(left, right, out, expLevel));
	}
	
	@SubscribeEvent
	public static void onAnvilUpdate(AnvilUpdateEvent event) {
		if(!recipeType.isEmpty()) {
			for(int i : recipeType.keySet()) {
				AnvilRecipeType type = recipeType.get(i);
				
				if((JiuUtils.item.equalsStack(type.getLeftInput(), event.getLeft()) && type.getLeftInput().getCount() <= event.getLeft().getCount())
				&& (JiuUtils.item.equalsStack(type.getRightInput(), event.getRight()) && type.getRightInput().getCount() <= event.getRight().getCount())) {
					event.setCost(type.getExpLevel());
					ItemStack out = type.getOutput();
					
					if(event.getLeft().getCount() / type.getLeftInput().getCount() <=  event.getRight().getCount() / type.getRightInput().getCount()) {
						out.setCount(out.getCount() * (event.getLeft().getCount() / type.getLeftInput().getCount()));
					}
					
					event.setOutput(out);
				}
			}
		}
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
			return this.left.copy();
		}
		
		public ItemStack getRightInput() {
			return this.right.copy();
		}
		
		public ItemStack getOutput() {
			return this.out.copy();
		}
		
		public int getExpLevel() {
			return this.expLevel;
		}
	}
}
