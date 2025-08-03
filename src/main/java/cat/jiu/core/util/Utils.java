package cat.jiu.core.util;

import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.function.Function;

public class Utils {
    @SuppressWarnings("unchecked")
	public static <T> T cast(Object other){
		return (T)other;
	}

	@SafeVarargs
	public static <T, R> R[] castArray(Function<Integer, R[]> arrayCreate, Function<T, R> objCast, T... other) {
		R[] array = arrayCreate.apply(other.length);
		for (int i = 0; i < array.length; i++) {
			array[i] = objCast.apply(other[i]);
		}
		return array;
	}

	public static ResourceLocation location(String key) {
		return ResourceLocation.tryParse(key);
	}
	public static ResourceLocation location(String name, String path) {
		return ResourceLocation.tryBuild(name, path);
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
