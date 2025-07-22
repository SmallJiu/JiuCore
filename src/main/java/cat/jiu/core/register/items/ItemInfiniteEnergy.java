package cat.jiu.core.register.items;

import cat.jiu.core.config.CoreConfig;
import cat.jiu.core.util.CapabilityUtils;
import cat.jiu.core.util.NBTUtils;
import cat.jiu.core.util.base.BaseItem;
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
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.energy.EnergyStorage;
import net.neoforged.neoforge.energy.IEnergyStorage;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class ItemInfiniteEnergy extends BaseItem {
    public static final String CHARGING_TAG_NAME = "chargingEnergy";
    public static int getMaxChargingEnergy() {
        return CoreConfig.max_charing_energy.get();
    }

    public static final IEnergyStorage INFINITE_ENERGY = new EnergyStorage(10000000){
        @Override
        public int extractEnergy(int toExtract, boolean simulate) {
            return getMaxChargingEnergy();
        }
        @Override
        public int receiveEnergy(int toReceive, boolean simulate) {
            return getMaxChargingEnergy();
        }
    };

    public ItemInfiniteEnergy(Properties properties) {
        super(properties);
        this.setOnCapabilityRegister(event->
            event.registerItem(Capabilities.EnergyStorage.ITEM, (stack, ctx) -> INFINITE_ENERGY, this)
        );
    }

    @Override
    public boolean isFoil(ItemStack pStack) {
        AtomicBoolean b = new AtomicBoolean();
        NBTUtils.get(pStack, nbt->
                b.set(nbt.getBoolean(CHARGING_TAG_NAME))
        );
        return b.get();
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slotId, boolean isSelected) {
        if (entity instanceof Player && this.isFoil(stack)) {
            this.chargingEnergy(((Player) entity).getInventory().items);
            this.chargingEnergy(((Player) entity).getInventory().armor);
            this.chargingEnergy(((Player) entity).getInventory().offhand);
        }
    }

    public void chargingEnergy(List<ItemStack> stacks){
        for (ItemStack stack : stacks) {
            if (stack.isEmpty() || stack.is(this)) continue;
            try {
                CapabilityUtils.item(Capabilities.EnergyStorage.ITEM, stack).ifPresent(storage->
                    storage.receiveEnergy(getMaxChargingEnergy(), false)
                );
            } catch (Exception ignored) {

            }
        }
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        BlockHitResult result = getPlayerPOVHitResult(level, player, ClipContext.Fluid.SOURCE_ONLY);
        AtomicBoolean placed = new AtomicBoolean();
        BlockEntity te = level.getBlockEntity(result.getBlockPos());
        if (te != null) {
            CapabilityUtils.block(Capabilities.EnergyStorage.BLOCK, level, result.getBlockPos(), result.getDirection()).ifPresent(storage->{
                if (storage.getEnergyStored() == storage.getMaxEnergyStored()) {
                    storage.extractEnergy(getMaxChargingEnergy(), false);
                    player.displayClientMessage(Component.translatable("item.jiucore.infinite_energy.empty"), true);
                }else {
                    storage.receiveEnergy(getMaxChargingEnergy(), false);
                    player.displayClientMessage(Component.translatable("item.jiucore.infinite_energy.full"), true);
                }
                placed.set(true);
            });
        }
        if (!placed.get() && player.isShiftKeyDown()) {
            NBTUtils.get(player.getItemInHand(hand), nbt->
                nbt.putBoolean(CHARGING_TAG_NAME, !nbt.getBoolean(CHARGING_TAG_NAME))
            );
            player.playSound(SoundEvents.EXPERIENCE_ORB_PICKUP, 1.0f, this.isFoil(player.getItemInHand(hand)) ? 0.75f : 0.7f);
            placed.set(true);
        }
        return placed.get() ? InteractionResultHolder.sidedSuccess(player.getItemInHand(hand), level.isClientSide()) : InteractionResultHolder.pass(player.getItemInHand(hand));
    }
}
