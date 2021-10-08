package cat.jiu.core.util.crafting;

import javax.annotation.Nonnull;

import cat.jiu.core.util.JiuUtils;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.ShapedRecipes;
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
	
	public void addInFluidCrafting(IBlockState state, ItemStack dropItem, ItemStack giveItem, int consumeItemAmount,  boolean consumeItem, boolean consumeFuid) {
		InFluidCrafting.addInFluidCrafting(state, dropItem, giveItem, consumeItemAmount, consumeItem, consumeFuid);
	}
	
	public void addInFluidCrafting(IBlockState state, ItemStack dropItem, ItemStack giveItem, int consumeItemAmount, boolean consumeItem) {
		this.addInFluidCrafting(state, dropItem, giveItem, consumeItemAmount, consumeItem, false);
	}
	
	public void addInFluidCrafting(IBlockState state, ItemStack dropItem, ItemStack giveItem, int consumeItemAmount) {
		this.addInFluidCrafting(state, dropItem, giveItem, consumeItemAmount, true);
	}
	
	public void addInFluidCrafting(IBlockState state, ItemStack dropItem, ItemStack giveItem) {
		this.addInFluidCrafting(state, dropItem, giveItem, 1);
	}
	
	public void addInFluidCrafting(ItemStack dropItem, int consumeItemAmount, ItemStack giveItem) {
		this.addInFluidCrafting(Blocks.WATER.getDefaultState(), dropItem, giveItem, consumeItemAmount);
	}
	
	public void addInFluidCrafting(ItemStack dropItem, ItemStack giveItem) {
		this.addInFluidCrafting(Blocks.WATER.getDefaultState(), dropItem, giveItem, 1);
	}
	
	public void addSmelting(@Nonnull Item in, int inMeta,  @Nonnull ItemStack out) {
		addSmelting(new ItemStack(in, 1, inMeta), out, 9);
	}
	
	public void addSmelting(@Nonnull Block in, int inMeta,  @Nonnull ItemStack out) {
		addSmelting(new ItemStack(in, 1, inMeta), out, 9);
	}

	public void addSmelting(@Nonnull Item in, int inMeta,  @Nonnull ItemStack out, float xp) {
		addSmelting(new ItemStack(in, 1, inMeta), out, xp);
	}
	
	public void addSmelting(@Nonnull Block in, int inMeta,  @Nonnull ItemStack out, float xp) {
		addSmelting(new ItemStack(in, 1, inMeta), out, xp);
	}
	
	public void addSmelting(@Nonnull ItemStack in,  @Nonnull ItemStack out, float xp) {
		GameRegistry.addSmelting(in, out, xp);
	}
	
	public void add1x1RecipesWithOreDictionary(ItemStack out, ItemStack in){
		if(JiuUtils.item.getOreDict(in).isEmpty()) {
			addShapedRecipe(out, "X", 'X', in);
		}else {
			for(String ore : JiuUtils.item.getOreDict(in)) {
				addShapedRecipe(out, "X", 'X', ore);
			}
		}
	}
	
	public void add2x2AllRecipesWithOreDictionary(ItemStack output, ItemStack input) {
		if(JiuUtils.item.getOreDict(input).isEmpty()) {
			addShapedRecipe(output, "XX", "XX", 'X', input);
		}else {
			for(String ore : JiuUtils.item.getOreDict(input)) {
				addShapedRecipe(output, "XX", "XX", 'X', ore);
			}
		}
    }
	
	public void add3x3AllRecipesWithOreDictionary(ItemStack output, ItemStack input) {
		if(JiuUtils.item.getOreDict(input).isEmpty()) {
			addShapedRecipe(output, "XXX", "XXX", "XXX", 'X', input);
		}else {
			for(String ore : JiuUtils.item.getOreDict(input)) {
				addShapedRecipe(output, "XXX", "XXX", "XXX", 'X', ore);
			}
		}
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
	
	public void addShapedRecipe(ItemStack output, Object... input) {
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
