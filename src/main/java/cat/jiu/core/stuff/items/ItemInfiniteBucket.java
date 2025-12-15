package cat.jiu.core.stuff.items;

import cat.jiu.core.CoreMain;
import cat.jiu.core.util.base.BaseItem;
import net.minecraft.core.BlockPos;
import net.minecraft.core.cauldron.CauldronInteraction;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BucketPickup;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.fluids.FluidStack;

import javax.annotation.Nullable;
import java.util.Optional;

public class ItemInfiniteBucket extends BaseItem {
    public ItemInfiniteBucket(Properties properties) {
        super(properties, CoreMain.registrate());
        this.addLanguage("zh_cn", "无底桶");

        CauldronInteraction interaction = (BlockState pBlockState, Level pLevel, BlockPos pBlockPos, Player pPlayer, InteractionHand pHand, ItemStack pStack) -> {
            boolean flag = pBlockState.hasProperty(BlockStateProperties.LEVEL_CAULDRON) && pBlockState.getValue(BlockStateProperties.LEVEL_CAULDRON) == 3;
            SoundEvent sound = SoundEvents.BUCKET_FILL;
            if(pBlockState.is(Blocks.LAVA_CAULDRON)) {
                sound = SoundEvents.BUCKET_FILL_LAVA;
            } else if (pBlockState.is(Blocks.POWDER_SNOW_CAULDRON)) {
                sound = SoundEvents.BUCKET_FILL_POWDER_SNOW;
            }

            if (pBlockState.is(Blocks.LAVA_CAULDRON) || flag) {
                pLevel.setBlockAndUpdate(pBlockPos, Blocks.CAULDRON.defaultBlockState());
                pLevel.playSound(pPlayer, pBlockPos, sound, SoundSource.BLOCKS);
                pLevel.gameEvent(pPlayer, GameEvent.FLUID_PLACE, pBlockPos);
                return InteractionResult.sidedSuccess(pLevel.isClientSide());
            }
            return InteractionResult.CONSUME;
        };
        CauldronInteraction.WATER.put(this, interaction);
        CauldronInteraction.LAVA.put(this, interaction);
        CauldronInteraction.POWDER_SNOW.put(this, interaction);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        BlockHitResult result = getPlayerPOVHitResult(level, player, ClipContext.Fluid.SOURCE_ONLY);
        boolean placed = false;
        BlockPos pos = result.getBlockPos();
        BlockState state = level.getBlockState(pos);
        Optional<SoundEvent> optional = Optional.empty();
        if (state.getBlock() instanceof BucketPickup) {
            if (!((BucketPickup) state.getBlock()).pickupBlock(level, pos, state).isEmpty()){
                optional = ((BucketPickup) state.getBlock()).getPickupSound(state);
                placed = true;
            }
        }else {
            BlockPos pos_relative = pos.relative(result.getDirection());
            BlockState state_relative = level.getBlockState(pos_relative);
            if (state_relative.getBlock() instanceof BucketPickup) {
                if (!((BucketPickup) state_relative.getBlock()).pickupBlock(level, pos_relative, state_relative).isEmpty()){
                    optional = ((BucketPickup) state.getBlock()).getPickupSound(state);
                    placed = true;
                }
            }
        }
        if (placed) {
            optional.ifPresent((sound) ->
                level.playSound(player, pos, sound, SoundSource.BLOCKS)
            );
            level.gameEvent(player, GameEvent.FLUID_PLACE, pos);
        }
        return placed ? InteractionResultHolder.sidedSuccess(player.getItemInHand(hand), level.isClientSide()) : InteractionResultHolder.pass(player.getItemInHand(hand));
    }

    @Override
    public net.minecraftforge.common.capabilities.ICapabilityProvider initCapabilities(ItemStack stack, @Nullable net.minecraft.nbt.CompoundTag nbt) {
        return new net.minecraftforge.fluids.capability.templates.FluidHandlerItemStackSimple(stack, 1000){
            @Override
            public FluidStack getFluid() {
                return FluidStack.EMPTY;
            }
        };
    }
}
