package cat.jiu.core.util.crafting;

import java.util.List;

import javax.annotation.Nonnull;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.item.crafting.ShapelessRecipes;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.common.crafting.CraftingHelper.ShapedPrimer;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.ModContainer;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.registries.GameData;

public class Recipes {
	
	private final String modid;
	public final ItemStack EMPTY = new ItemStack(Items.AIR);
	
	public Recipes(String modid){
		this.modid = modid;
	}
	
	public String getModid() {
		return this.modid;
	}
	
// =========================================================================================//
	public void addInFluidCrafting(IBlockState fluid, ItemStack input, int inputAmout, List<ItemStack> output, boolean consumeFluid) {
		InFluidCrafting.addInFluidCrafting(fluid, input, inputAmout, output, consumeFluid);
	}
	
	public void addInFluidCrafting(IBlockState fluid, ItemStack input, int inputAmout, List<ItemStack> output) {
		this.addInFluidCrafting(fluid, input, inputAmout, output, false);
	}
	
	public void addInFluidCrafting(IBlockState fluid, ItemStack input, List<ItemStack> output) {
		this.addInFluidCrafting(fluid, input, input.getCount(), output);
	}
	
	public void addInFluidCrafting(ItemStack input, int inputAmout, List<ItemStack> output) {
		this.addInFluidCrafting(Blocks.WATER.getDefaultState(), input, inputAmout, output);
	}
	
	public void addInFluidCrafting(ItemStack input, List<ItemStack> output) {
		this.addInFluidCrafting(Blocks.WATER.getDefaultState(), input, input.getCount(), output);
	}
	
// =========================================================================================//
	public void addInFluidCrafting(IBlockState fluid, ItemStack input, int inputAmout, ItemStack[] output, boolean consumeFuid) {
		InFluidCrafting.addInFluidCrafting(fluid, input, inputAmout, output, consumeFuid);
	}
	
	public void addInFluidCrafting(IBlockState fluid, ItemStack input, int inputAmout, ItemStack[] output) {
		this.addInFluidCrafting(fluid, input, inputAmout, output, false);
	}
	
	public void addInFluidCrafting(IBlockState fluid, ItemStack input, ItemStack[] output) {
		this.addInFluidCrafting(fluid, input, input.getCount(), output);
	}
	
	public void addInFluidCrafting(ItemStack input, int inputAmout, ItemStack[] output) {
		this.addInFluidCrafting(Blocks.WATER.getDefaultState(), input, inputAmout, output);
	}
	
	public void addInFluidCrafting(ItemStack input, ItemStack[] output) {
		this.addInFluidCrafting(Blocks.WATER.getDefaultState(), input, input.getCount(), output);
	}
	
	public void addInFluidCrafting(ItemStack input, ItemStack output) {
		this.addInFluidCrafting(input, new ItemStack[] {output});
	}
	
// =========================================================================================//
	public void addSmelting(@Nonnull Item in, @Nonnull ItemStack out) {
		addSmelting(new ItemStack(in, 1, 0), out, 9);
	}
	
	public void addSmelting(@Nonnull Item in, int inMeta,  @Nonnull ItemStack out) {
		addSmelting(new ItemStack(in, 1, inMeta), out, 9);
	}
	
	public void addSmelting(@Nonnull Block in, @Nonnull ItemStack out) {
		addSmelting(new ItemStack(in, 1, 0), out, 9);
	}
	
	public void addSmelting(@Nonnull Block in, int inMeta,  @Nonnull ItemStack out) {
		addSmelting(new ItemStack(in, 1, inMeta), out, 9);
	}

	public void addSmelting(@Nonnull Item in, @Nonnull ItemStack out, float xp) {
		addSmelting(new ItemStack(in, 1, 0), out, xp);
	}
	
	public void addSmelting(@Nonnull Item in, int inMeta,  @Nonnull ItemStack out, float xp) {
		addSmelting(new ItemStack(in, 1, inMeta), out, xp);
	}
	
	public void addSmelting(@Nonnull Block in, @Nonnull ItemStack out, float xp) {
		addSmelting(new ItemStack(in, 1, 0), out, xp);
	}
	
	public void addSmelting(@Nonnull Block in, int inMeta,  @Nonnull ItemStack out, float xp) {
		addSmelting(new ItemStack(in, 1, inMeta), out, xp);
	}
	
	public void addSmelting(@Nonnull ItemStack in,  @Nonnull ItemStack out, float xp) {
		GameRegistry.addSmelting(in, out, xp);
	}
	
	public void add1x1RecipesWithOreDictionary(ItemStack out, String ore) {
		addShapedRecipe(out, "X", 'X', ore);
	}
	
	public void add1x1Recipes(ItemStack out, ItemStack in) {
		addShapedRecipe(out, "X", 'X', in);
	}
	
	public void add2x2AllRecipesWithOreDictionary(ItemStack output, String ore) {
		addShapedRecipe(output, "XX", "XX", 'X', ore);
	}
	
	public void add2x2AllRecipes(ItemStack output, ItemStack input) {
		addShapedRecipe(output, "XX", "XX", 'X', input);
    }
	
	public void add3x3AllRecipesWithOreDictionary(ItemStack output, String ore) {
		addShapedRecipe(output, "XXX", "XXX", "XXX", 'X', ore);
	}
	
	public void add3x3AllRecipes(ItemStack output, ItemStack input) {
		addShapedRecipe(output, "XXX", "XXX", "XXX", 'X', input);
    }
	
	public void addShapedRecipes(ItemStack output, ItemStack input) {
		addShapedRecipe(output, "A", 'A', input);
	}
	
	public void addShapedRecipes(ItemStack output, ItemStack input1, ItemStack input2, ItemStack input3, ItemStack input4, ItemStack input5, ItemStack input6, ItemStack input7, ItemStack input8, ItemStack input9) {
		addShapedRecipe(output,
				"ABC",
				"DEF",
				"GHI",
				'A', input1,
				'B', input2,
				'C', input3,
				
				'D', input4,
				'E', input5,
				'F', input6,
				
				'G', input7,
				'H', input8,
				'I', input9
		);
	}
	
	public void addShapedlessRecipe(ItemStack output, Object... inputs) {
		for(Object input: inputs) 
			if(input == null) return;
		ResourceLocation recipeName = getNameForRecipe(output);
		NonNullList<Ingredient> inputIng = NonNullList.create();
		
		for(Object input: inputs) {
			inputIng.add(CraftingHelper.getIngredient(input));
		}
		ShapelessRecipes recipe = new ShapelessRecipes(output.getItem().getRegistryName().toString(), output, inputIng);
		GameData.register_impl(recipe.setRegistryName(recipeName));
	}
	
	public void addShapedRecipe(ItemStack output, Object... input) {
		for(Object object : input) 
			if(object == null) return;
		ResourceLocation recipeName = getNameForRecipe(output);
		ShapedPrimer primer = CraftingHelper.parseShaped(input);
		ShapedRecipes recipe = new ShapedRecipes(output.getItem().getRegistryName().toString(), primer.width, primer.height, primer.input, output);
		GameData.register_impl(recipe.setRegistryName(recipeName));
	}
	
	protected ResourceLocation getNameForRecipe(ItemStack output) {
		ModContainer con = Loader.instance().activeModContainer();
		ResourceLocation t = new ResourceLocation(con.getModId(), output.getItem().getRegistryName().getResourcePath());
		ResourceLocation recipe = t;
		int i = 0;
		while (CraftingManager.REGISTRY.containsKey(recipe)) {
			i++;
			recipe = new ResourceLocation(this.modid, t.getResourcePath() + "." + i);
		}
		return recipe;
	}
}
