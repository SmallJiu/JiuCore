package cat.jiu.core.util;

import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class Utils {
    @SuppressWarnings("unchecked")
	public static <T> T cast(Object other){
		return (T)other;
	}
	@OnlyIn(Dist.CLIENT)
	public static boolean isOp() {
		Minecraft mc = Minecraft.getInstance();
		return mc.player != null && ((mc.hasSingleplayerServer() && mc.player.hasPermissions(2)) || mc.player.hasPermissions(2));
	}
}
