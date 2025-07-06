package cat.jiu.core.register;

import cat.jiu.core.CoreMain;
import cat.jiu.core.register.items.ItemInfiniteBucket;
import cat.jiu.core.register.items.ItemInfiniteEnergy;
import cat.jiu.core.register.items.ItemInfiniteWater;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class CoreItems {
    public static final DeferredRegister<Item> REGISTER = DeferredRegister.create(ForgeRegistries.ITEMS, CoreMain.MODID);

    public static final RegistryObject<ItemInfiniteWater> INFINITE_WATER = REGISTER.register("infinite_water", ItemInfiniteWater::new);
    public static final RegistryObject<ItemInfiniteBucket> INFINITE_BUCKET = REGISTER.register("infinite_bucket", ItemInfiniteBucket::new);
    public static final RegistryObject<ItemInfiniteEnergy> INFINITE_ENERGY = REGISTER.register("infinite_energy", ItemInfiniteEnergy::new);
}
