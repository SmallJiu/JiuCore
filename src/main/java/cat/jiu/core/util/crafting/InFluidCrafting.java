package cat.jiu.core.util.crafting;

import java.util.HashMap;
import java.util.List;

import com.google.common.collect.Lists;

import cat.jiu.core.api.events.item.IItemInFluidTickEvent;
import cat.jiu.core.util.JiuUtils;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

@SuppressWarnings("unchecked")
public class InFluidCrafting implements IItemInFluidTickEvent{
	
	// recipe id, input
	private static HashMap<Integer, ItemStack> recipe_map = new HashMap<Integer, ItemStack>();
	// input, recipe type
	private static HashMap<ItemStack, InFluidCraftingRecipeType> map = new HashMap<ItemStack, InFluidCraftingRecipeType>();
	
	public static void removeAllRecipes() {
		for(int i = 0; i < recipe_map.size(); ++i) {
			recipe_map.clear();
		}
		for(int i = 0; i < map.size(); ++i) {
			map.clear();
		}
	}
	
	public static InFluidCraftingRecipeType getRecipeType(int id) {
		return map.get(recipe_map.get(id));
	}
	
	/**
	 * 
	 * Please never add the recipe, it has a bug<p>
	 * 
	 * new Recipes(MODID).addInFluidCrafting(new ItemStack(Blocks.COAL_BLOCK, 1), new ItemStack(Items.COAL, 9));<p>
	 * new Recipes(MODID).addInFluidCrafting(new ItemStack(Items.COAL, 9), new ItemStack(Blocks.COAL_BLOCK));
	 * 
	 * @param fluidState fluid state
	 * @param input recipe input
	 * @param output recipe output
	 * @param inputAmout input amout use ' input.getCount() ' amout
	 * @param consumeItem item will remove
	 * @param consumeFluid fluid will remove
	 * 
	 * @author small_jiu
	 */
	public static void addInFluidCrafting(IBlockState fluid, ItemStack input, List<ItemStack> output, boolean consumeFluid) {
		addInFluidCrafting(fluid, input, input.getCount(), output, consumeFluid);
	}
	
	public static void addInFluidCrafting(IBlockState fluid, ItemStack input, ItemStack[] output, boolean consumeFluid) {
		addInFluidCrafting(fluid, input, input.getCount(), output, consumeFluid);
	}
	
	/**
	 * 
	 * Please never add the recipe, it has a bug<p>
	 * 
	 * new Recipes(MODID).addInFluidCrafting(new ItemStack(Blocks.COAL_BLOCK), 1, new ItemStack(Items.COAL, 9));<p>
	 * new Recipes(MODID).addInFluidCrafting(new ItemStack(Items.COAL), 9, new ItemStack(Blocks.COAL_BLOCK));
	 * @param input recipe input
	 * @param inputAmout input amout
	 * @param output recipe output
	 * @param consumeFluid fluid will remove
	 * @param fluidState fluid state
	 * 
	 * @author small_jiu
	 */
	public static void addInFluidCrafting(IBlockState fluid, ItemStack input, int inputAmout, List<ItemStack> output, boolean consumeFluid) {
		addInFluidCrafting(new InFluidCraftingRecipeType(fluid, input, inputAmout, output, consumeFluid));
	}
	
	public static void addInFluidCrafting(IBlockState fluid, ItemStack input, int inputAmout, ItemStack[] output, boolean consumeFluid) {
		addInFluidCrafting(new InFluidCraftingRecipeType(fluid, input, inputAmout, output, consumeFluid));
	}
	
	public static void addInFluidCrafting(InFluidCraftingRecipeType type) {
		map.put(type.getInput(), type);
		recipe_map.put(map.size(), type.getInput());
	}
	
	public static void removeCrafting(ItemStack input) {
		HashMap<ItemStack, InFluidCraftingRecipeType> map_t = new HashMap<ItemStack, InFluidCraftingRecipeType>();
		HashMap<Integer, ItemStack> recipe_map_t = new HashMap<Integer, ItemStack>();
		
		for(ItemStack in : map.keySet()) {
			if(!JiuUtils.item.equalsStack(in, input, true)) {
				map_t.put(in, map.get(in));
			}
		}
		
		for(int i : recipe_map.keySet()) {
			ItemStack in = recipe_map.get(i);
			if(!JiuUtils.item.equalsStack(in, input, true)) {
				recipe_map_t.put(recipe_map_t.size(), in);
			}
		}
		
		recipe_map = recipe_map_t;
		map = map_t;
		
		recipe_map_t = null;
		map_t = null;
	}
	
	public static HashMap<ItemStack, InFluidCraftingRecipeType> getRecipes() {
		return (HashMap<ItemStack, InFluidCraftingRecipeType>) map.clone();
	}
	
	public static HashMap<Integer, ItemStack> getRecipesMap() {
		return (HashMap<Integer, ItemStack>) recipe_map.clone();
	}
	
	@Override
	public void onItemInFluidTick(EntityItem item, World world, BlockPos pos, IBlockState state) {
		ItemStack stack = item.getItem();
		
		if(!map.isEmpty()) {
			for(int i : recipe_map.keySet()) {
				InFluidCraftingRecipeType type = getRecipeType(i);
				
				IBlockState fluid = type.getFluid();// Fluid state
				ItemStack input = type.getInput();// input item
				List<ItemStack> output = type.getOutputsList();// output items
				int inputAmout = type.getInputAmout();// input item amout
				boolean consumeFuid = type.canConsumeFluid();// input fluid can be consume?
				
				// check fluid
				if(JiuUtils.item.equalsState(state, fluid)) {
					// check input stack
					if(JiuUtils.item.equalsStack(input, stack)) {
						// check EntityItemStack amout
						if(stack.getCount() >= inputAmout) {
							if(stack.getItem() == Items.PAPER) {
								stack.shrink(inputAmout);
							}
							
							stack.shrink(inputAmout);
							if(world.getBlockState(pos.up()) != Blocks.AIR.getDefaultState()) {
//								item.setPosition(pos.getX(), pos.getY() + 1.5, pos.getZ());
							}
							
							// spawn item with the pos
							JiuUtils.item.spawnAsEntity(world, item.getPosition(), output);
							
							// if fluid can be consume, set fluid to air
							if(consumeFuid) {
								world.setBlockToAir(pos);
							}
						}
					}
				}
			}
		}
	}
	
	public static final class InFluidCraftingRecipeType {
		private final IBlockState fluid;
		private final ItemStack input;
		private final int inputAmout;
		private final List<ItemStack> outputs;
		private final boolean consumeFluid;
		
		public InFluidCraftingRecipeType(IBlockState fluid, ItemStack input, int inputAmout, ItemStack[] outputs, boolean consumeFluid) {
			this(fluid, input, inputAmout, Lists.newArrayList(outputs), consumeFluid);
		}
		
		public InFluidCraftingRecipeType(IBlockState fluid, ItemStack input, int inputAmout, List<ItemStack> outputs, boolean consumeFluid) {
			this.fluid = fluid;
			this.input = input;
			this.inputAmout = inputAmout;
			this.outputs = outputs;
			this.consumeFluid = consumeFluid;
		}
		
		public IBlockState getFluid() {
			return this.fluid;
		}
		
		public ItemStack getInput() {
			return this.input;
		}
		
		public int getInputAmout() {
			return this.inputAmout;
		}
		
		public List<ItemStack> getOutputsList() {
			return this.outputs;
		}
		
		public ItemStack[] getOutputsArray() {
			return this.outputs.toArray(new ItemStack[this.outputs.size()]);
		}
		
		public boolean canConsumeFluid() {
			return this.consumeFluid;
		}
		
		@Override
		public String toString() {
			String name = "";
			for(ItemStack stack : this.outputs) {
				name += stack + ",";
			}
			return "[" + this.fluid + ",{" + this.inputAmout + "x" + this.input.getUnlocalizedName() + "}" + "[ " + name + " ]" + this.consumeFluid + "]";
		}
	}
}
