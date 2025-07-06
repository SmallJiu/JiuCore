package cat.jiu.core.util.base;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class BaseItem extends Item {
    private ResourceKey<CreativeModeTab> creativeTab = CreativeModeTabs.TOOLS_AND_UTILITIES;
    public BaseItem(Properties properties) {
        super(properties);
        FMLJavaModLoadingContext.get().getModEventBus().register(this);
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        this.addTooltip(pLevel, pStack, pIsAdvanced, pTooltipComponents);
    }
    protected void addTooltip(@Nullable Level level, ItemStack stack, TooltipFlag isAdvanced, List<Component> tooltips){

    }

    public ResourceKey<CreativeModeTab> getCreativeTab() {
        return creativeTab;
    }

    public BaseItem setCreativeTab(ResourceKey<CreativeModeTab> tab) {
        this.creativeTab = tab;
        return this;
    }

    @SubscribeEvent
    public void addToCreativeTab(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == this.getCreativeTab()) {
            event.accept(this);
        }
    }
}
