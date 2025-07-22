package cat.jiu.core.util.base;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.*;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;

import java.util.List;
import java.util.function.Consumer;

public class BaseBlock extends Block {
    private ResourceKey<CreativeModeTab> creativeTab = CreativeModeTabs.BUILDING_BLOCKS;
    private Consumer<RegisterCapabilitiesEvent> onCapabilityRegister;
    public BaseBlock(Properties properties) {
        super(properties);
        ModLoadingContext.get().getActiveContainer().getEventBus().register(this);
    }

    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        this.addTooltip(stack, context, tooltipComponents, tooltipFlag);
    }
    protected void addTooltip(ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag){

    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {

    }

    public ResourceKey<CreativeModeTab> getCreativeTab() {
        return creativeTab;
    }

    public BaseBlock setCreativeTab(ResourceKey<CreativeModeTab> tab) {
        this.creativeTab = tab;
        return this;
    }
    @SubscribeEvent
    public void addCreativeTab(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == this.getCreativeTab()) {
            event.accept(this);
        }
    }

    public BaseBlock setOnCapabilityRegister(Consumer<RegisterCapabilitiesEvent> onCapabilityRegister) {
        this.onCapabilityRegister = onCapabilityRegister;
        return this;
    }
    @SubscribeEvent
    public void registerCapabilities(RegisterCapabilitiesEvent event) {
        if (this.onCapabilityRegister!=null){
            this.onCapabilityRegister.accept(event);
        }
    }
}
