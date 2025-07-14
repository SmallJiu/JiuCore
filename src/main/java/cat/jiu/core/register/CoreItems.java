package cat.jiu.core.register;

import cat.jiu.core.CoreMain;
import cat.jiu.core.register.items.ItemInfiniteBucket;
import cat.jiu.core.register.items.ItemInfiniteEnergy;
import cat.jiu.core.register.items.ItemInfiniteWater;
import com.tterrag.registrate.util.entry.ItemEntry;
import net.minecraft.world.item.CreativeModeTabs;

public class CoreItems {

    public static final ItemEntry<ItemInfiniteWater> INFINITE_WATER = CoreMain.registrate()
            .object("infinite_water")
            .item(ItemInfiniteWater::new)
            .tab(CreativeModeTabs.TOOLS_AND_UTILITIES)
            .properties(p->p.stacksTo(1))
            .register();

    public static final ItemEntry<ItemInfiniteBucket> INFINITE_BUCKET = CoreMain.registrate()
            .object("infinite_bucket")
            .item(ItemInfiniteBucket::new)
            .tab(CreativeModeTabs.TOOLS_AND_UTILITIES)
            .properties(p->p.stacksTo(1))
            .register();

    public static final ItemEntry<ItemInfiniteEnergy> INFINITE_ENERGY = CoreMain.registrate()
            .object("infinite_bucket")
            .item(ItemInfiniteEnergy::new)
            .tab(CreativeModeTabs.TOOLS_AND_UTILITIES)
            .properties(p->p.stacksTo(1))
            .register();
}
