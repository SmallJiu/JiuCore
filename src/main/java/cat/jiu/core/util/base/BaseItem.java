package cat.jiu.core.util.base;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class BaseItem extends Item {
    private ResourceKey<CreativeModeTab> creativeTab = CreativeModeTabs.TOOLS_AND_UTILITIES;
    public BaseItem(Properties properties) {
        super(properties);
        ModLoadingContext.get().getActiveContainer().getEventBus().register(this);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        this.addTooltip(stack, context, tooltipComponents, tooltipFlag);
    }
    protected void addTooltip(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag){

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
