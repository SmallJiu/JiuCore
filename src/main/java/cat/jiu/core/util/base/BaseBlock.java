package cat.jiu.core.util.base;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import java.util.List;

public class BaseBlock extends Block {
    private ResourceKey<CreativeModeTab> creativeTab = CreativeModeTabs.BUILDING_BLOCKS;
    public BaseBlock(Properties properties) {
        super(properties);
        FMLJavaModLoadingContext.get().getModEventBus().register(this);
    }

    @Override
    public void appendHoverText(ItemStack pStack, BlockGetter pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        this.addTooltip(pLevel, pStack, pIsAdvanced, pTooltipComponents);
    }
    protected void addTooltip(BlockGetter world, ItemStack stack, TooltipFlag isAdvanced, List<Component> tooltips){

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
}
