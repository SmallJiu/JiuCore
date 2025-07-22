package cat.jiu.core.register.data;

import cat.jiu.core.register.CoreItems;
import com.tterrag.registrate.providers.RegistrateRecipeProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.world.item.Items;

import java.util.concurrent.CompletableFuture;

public class CoreRecipes extends RecipeProvider {
    public CoreRecipes(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries);
    }

    public static void register(RegistrateRecipeProvider provider) {
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, CoreItems.INFINITE_WATER.get())
                .pattern("aba")
                .define('a', Items.WATER_BUCKET)
                .define('b', Items.ENDER_PEARL)
                .unlockedBy("has_water_bucket", has(Items.WATER_BUCKET))
                .save(provider);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.TOOLS, CoreItems.INFINITE_BUCKET.get())
                .requires(Items.ENDER_PEARL)
                .requires(Items.BUCKET)
                .unlockedBy("has_empty_bucket", has(Items.BUCKET))
                .save(provider);
    }
}
