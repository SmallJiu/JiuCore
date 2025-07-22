package cat.jiu.core.util;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.capabilities.BlockCapability;
import net.neoforged.neoforge.capabilities.ItemCapability;

import java.util.Optional;

public class CapabilityUtils {
    public static <T, C> Optional<T> block(BlockCapability<T, C> capability, Level level, BlockPos pos, C ctx){
        return Optional.ofNullable(capability.getCapability(
                level, pos, level.getBlockState(pos), level.getBlockEntity(pos), ctx
        ));
    }

    public static <T> Optional<T> item(ItemCapability<T, Void> capability, ItemStack stack) {
        return Optional.ofNullable(stack.getCapability(capability));
    }

    public static <T, C> Optional<T> item(ItemCapability<T, C> capability, ItemStack stack, C ctx) {
        return Optional.ofNullable(stack.getCapability(capability, ctx));
    }
}
