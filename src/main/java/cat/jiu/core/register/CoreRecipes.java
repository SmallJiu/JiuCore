package cat.jiu.core.register;

import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.world.item.Items;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public class CoreRecipes extends RecipeProvider {
    public CoreRecipes(PackOutput pOutput) {
        super(pOutput);
    }

    @Override
    protected void buildRecipes(@NotNull Consumer<FinishedRecipe> writer) {
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, CoreItems.INFINITE_WATER.get())
                .pattern("aba")
                .define('a', Items.WATER_BUCKET)
                .define('b', Items.ENDER_PEARL)
                .unlockedBy("has_water_bucket", has(Items.WATER_BUCKET))
                .save(writer);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.TOOLS, CoreItems.INFINITE_BUCKET.get())
                .requires(Items.ENDER_PEARL)
                .requires(Items.BUCKET)
                .unlockedBy("has_empty_bucket", has(Items.BUCKET))
                .save(writer);
    }
}
