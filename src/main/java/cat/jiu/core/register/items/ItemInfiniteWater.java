package cat.jiu.core.register.items;

import cat.jiu.core.util.base.BaseItem;
import net.minecraft.core.BlockPos;
import net.minecraft.core.cauldron.CauldronInteraction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LiquidBlockContainer;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.Fluids;
import org.jetbrains.annotations.NotNull;

public class ItemInfiniteWater extends BaseItem {
    public ItemInfiniteWater(Properties properties) {
        super(properties);
        CauldronInteraction.EMPTY.map().put(this, ((pBlockState, pLevel, pBlockPos, pPlayer, pHand, pStack) -> {
            pLevel.setBlockAndUpdate(pBlockPos, Blocks.WATER_CAULDRON.defaultBlockState().setValue(BlockStateProperties.LEVEL_CAULDRON, 3));
            pLevel.playSound(pPlayer, pBlockPos, SoundEvents.BUCKET_FILL, SoundSource.BLOCKS);
            pLevel.gameEvent(pPlayer, GameEvent.FLUID_PLACE, pBlockPos);
            return InteractionResult.sidedSuccess(pLevel.isClientSide());
        }));
    }

    @NotNull
    @Override
    public InteractionResult useOn(UseOnContext ctx) {
        boolean placed = false;
        BlockPos pos = ctx.getClickedPos();
        BlockState state = ctx.getLevel().getBlockState(ctx.getClickedPos());
        if (state.getBlock() instanceof LiquidBlockContainer && ((LiquidBlockContainer) state.getBlock()).canPlaceLiquid(ctx.getLevel(), pos, state, Fluids.WATER)) {
            ((LiquidBlockContainer) state.getBlock()).placeLiquid(ctx.getLevel(), pos, state, Fluids.WATER.defaultFluidState());
            placed = true;
        }else {
            BlockPos pos_relative = ctx.getClickedPos().relative(ctx.getClickedFace());
            BlockState state_relative = ctx.getLevel().getBlockState(pos_relative);
            if (state_relative.getBlock() instanceof LiquidBlockContainer && ((LiquidBlockContainer) state_relative.getBlock()).canPlaceLiquid(ctx.getLevel(), pos, state_relative, Fluids.WATER)) {
                ((LiquidBlockContainer) state_relative.getBlock()).placeLiquid(ctx.getLevel(), pos_relative, state_relative, Fluids.WATER.defaultFluidState());
                placed = true;
            }else if (state_relative.canBeReplaced(Fluids.WATER)) {
                ctx.getLevel().destroyBlock(pos_relative, true);
                ctx.getLevel().setBlock(pos_relative, Fluids.WATER.defaultFluidState().createLegacyBlock(), 3);
                ctx.getLevel().scheduleTick(pos_relative, Fluids.WATER.defaultFluidState().getType(), Fluids.WATER.defaultFluidState().getType().getTickDelay(ctx.getLevel()));
                placed = true;
            }
        }

        if (placed) {
            ctx.getLevel().playSound(ctx.getPlayer(), pos, SoundEvents.BUCKET_FILL, SoundSource.BLOCKS);
            ctx.getLevel().gameEvent(ctx.getPlayer(), GameEvent.FLUID_PLACE, pos);
        }
        return placed ? InteractionResult.SUCCESS : InteractionResult.PASS;
    }
}
