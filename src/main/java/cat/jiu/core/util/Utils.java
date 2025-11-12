package cat.jiu.core.util;

import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.function.Function;

public class Utils {
    @SuppressWarnings("unchecked")
	public static <T> T cast(Object other){
		return (T)other;
	}

	@Deprecated
	@SafeVarargs
	public static <T, R> R[] castArray(Function<Integer, R[]> arrayCreate, Function<T, R> objCast, T... other) {
		return ArrayUtils.cast(arrayCreate, objCast, other);
	}

	public static ResourceLocation location(String key) {
		return ResourceLocation.tryParse(key);
	}
	public static ResourceLocation location(String name, String path) {
		return ResourceLocation.tryBuild(name, path);
	}

	public static void spawnItem(Player player, Iterable<ItemStack> stacks) {
		for (ItemStack stack : stacks) {
			spawnItem(player, stack);
		}
	}
	public static void spawnItem(Player player, ItemStack stack) {
		spawnItem(player.level(), player.position(), stack);
	}
	public static void spawnItem(Level world, Vec3 pos, Iterable<ItemStack> stacks) {
		for (ItemStack stack : stacks) {
			spawnItem(world, pos, stack);
		}
	}
	public static void spawnItem(Level world, Vec3 pos, ItemStack stack) {
		if(!stack.isEmpty()){
			ItemEntity item = new ItemEntity(world, pos.x, pos.y, pos.z, stack.copy());
			item.setPickUpDelay(1);
			world.addFreshEntity(item);
		}
	}

	@OnlyIn(Dist.CLIENT)
	public static boolean isOp() {
		Minecraft mc = Minecraft.getInstance();
		return mc.player != null && ((mc.hasSingleplayerServer() && mc.player.hasPermissions(2)) || mc.player.hasPermissions(2));
	}
	public static boolean isOp(Player player) {
		return player.hasPermissions(2);
	}
}
