package cat.jiu.core.stuff;

import cat.jiu.core.CoreMain;
import cat.jiu.core.stuff.items.ItemInfiniteBucket;
import cat.jiu.core.stuff.items.ItemInfiniteEnergy;
import cat.jiu.core.stuff.items.ItemInfiniteWater;
import com.tterrag.registrate.util.entry.ItemEntry;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Items;

import static com.tterrag.registrate.providers.RegistrateRecipeProvider.has;

public class CoreItems {
    public static final ItemEntry<ItemInfiniteWater> INFINITE_WATER = CoreMain.registrate()
            .object("infinite_water")
            .item(ItemInfiniteWater::new)
            .tab(CreativeModeTabs.TOOLS_AND_UTILITIES)
            .properties(p->p.stacksTo(1))
            .recipe((ctx, provider)->
                    ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ctx.getEntry())
                            .pattern("aba")
                            .define('a', Items.WATER_BUCKET)
                            .define('b', Items.ENDER_PEARL)
                            .unlockedBy("has_water_bucket", has(Items.WATER_BUCKET))
                            .save(provider)
            )
            .register();

    public static final ItemEntry<ItemInfiniteBucket> INFINITE_BUCKET = CoreMain.registrate()
            .object("infinite_bucket")
            .item(ItemInfiniteBucket::new)
            .tab(CreativeModeTabs.TOOLS_AND_UTILITIES)
            .properties(p->p.stacksTo(1))
            .recipe((ctx, provider)->
                    ShapelessRecipeBuilder.shapeless(RecipeCategory.TOOLS, ctx.getEntry())
                            .requires(Items.ENDER_PEARL)
                            .requires(Items.BUCKET)
                            .unlockedBy("has_empty_bucket", has(Items.BUCKET))
                            .save(provider)
            )
            .register();

    public static final ItemEntry<ItemInfiniteEnergy> INFINITE_ENERGY = CoreMain.registrate()
            .object("infinite_energy")
            .item(ItemInfiniteEnergy::new)
            .tab(CreativeModeTabs.TOOLS_AND_UTILITIES)
            .properties(p->p.stacksTo(1))
            .register();

    public static void boostrap(){ }
}
