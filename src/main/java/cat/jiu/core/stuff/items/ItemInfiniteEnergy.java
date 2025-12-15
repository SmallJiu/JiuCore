package cat.jiu.core.stuff.items;

import cat.jiu.core.CoreMain;
import cat.jiu.core.util.base.BaseItem;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.EnergyStorage;
import net.minecraftforge.energy.IEnergyStorage;

import javax.annotation.Nullable;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class ItemInfiniteEnergy extends BaseItem {
    public static final String CHARGING_TAG_NAME = "chargingEnergy";
    public static final ICapabilityProvider Infinite_Energy_Capability = new ICapabilityProvider() {
        final LazyOptional<IEnergyStorage> optional = LazyOptional.of(() -> new EnergyStorage(Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, 998){
            @Override
            public int extractEnergy(int maxExtract, boolean simulate) {
                return Integer.MAX_VALUE;
            }
            @Override
            public int receiveEnergy(int maxReceive, boolean simulate) {
                return Integer.MAX_VALUE;
            }
        });
        @Override
        public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
            return ForgeCapabilities.ENERGY.orEmpty(cap, optional);
        }
    };

    public ItemInfiniteEnergy(Properties properties) {
        super(properties, CoreMain.registrate());
        this.addLanguage("zh_cn", "无限能量");
        this.addLanguage("zh_cn", "item.jiucore.infinite_energy.full", "已充满能量.");
        this.addLanguage("zh_cn", "item.jiucore.infinite_energy.empty", "已抽空能量.");

//        this.addLanguage("en_us", "Infinite Energy");
        this.addLanguage("en_us", "item.jiucore.infinite_energy.full", "Energy has been fulled.");
        this.addLanguage("en_us", "item.jiucore.infinite_energy.empty", "Energy has been drained.");
    }

    @Override
    public boolean isFoil(ItemStack pStack) {
        return pStack.getOrCreateTag().getBoolean(CHARGING_TAG_NAME);
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slotId, boolean isSelected) {
        if (entity instanceof Player && stack.getOrCreateTag().getBoolean(CHARGING_TAG_NAME)) {
            this.chargingEnergy(((Player) entity).getInventory().items);
            this.chargingEnergy(((Player) entity).getInventory().armor);
            this.chargingEnergy(((Player) entity).getInventory().offhand);
        }
    }

    public void chargingEnergy(List<ItemStack> stacks){
        for (ItemStack stack : stacks) {
            if (stack.isEmpty() || stack.is(this)) continue;
            stack.getCapability(ForgeCapabilities.ENERGY).ifPresent(storage->
                storage.receiveEnergy(Integer.MAX_VALUE, false)
            );
        }
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        BlockHitResult result = getPlayerPOVHitResult(level, player, ClipContext.Fluid.SOURCE_ONLY);
        AtomicBoolean placed = new AtomicBoolean();
        BlockEntity te = level.getBlockEntity(result.getBlockPos());
        if (te != null) {
            te.getCapability(ForgeCapabilities.ENERGY).ifPresent(storage->{
                if (storage.getEnergyStored() == storage.getMaxEnergyStored()) {
                    storage.extractEnergy(Integer.MAX_VALUE, false);
                    player.displayClientMessage(Component.translatable("item.jiucore.infinite_energy.empty"), true);
                }else {
                    storage.receiveEnergy(Integer.MAX_VALUE, false);
                    player.displayClientMessage(Component.translatable("item.jiucore.infinite_energy.full"), true);
                }
                placed.set(true);
            });
        }
        if (!placed.get() && player.isShiftKeyDown()) {
            CompoundTag tag = player.getItemInHand(hand).getOrCreateTag();
            tag.putBoolean(CHARGING_TAG_NAME, !tag.getBoolean(CHARGING_TAG_NAME));
            player.playSound(SoundEvents.EXPERIENCE_ORB_PICKUP, 1.0f, tag.getBoolean(CHARGING_TAG_NAME) ? 0.75f : 0.7f);
            placed.set(true);
        }
        return placed.get() ? InteractionResultHolder.sidedSuccess(player.getItemInHand(hand), level.isClientSide()) : InteractionResultHolder.pass(player.getItemInHand(hand));
    }

    @Override
    public net.minecraftforge.common.capabilities.ICapabilityProvider initCapabilities(ItemStack stack, @Nullable net.minecraft.nbt.CompoundTag nbt) {
        return Infinite_Energy_Capability;
    }
}
