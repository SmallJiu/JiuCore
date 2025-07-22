package cat.jiu.core.util;

import com.google.gson.JsonObject;
import com.mojang.serialization.JsonOps;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.items.ItemStackHandler;

import java.util.List;
import java.util.function.Consumer;

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

	public static void writeStackToNBT(ItemStack stack, Consumer<CompoundTag> consumer) {
		ItemStack.CODEC.encodeStart(NbtOps.INSTANCE, stack).ifSuccess(data -> {
			if (data instanceof CompoundTag) {
				consumer.accept((CompoundTag) data);
			}
		});
	}
	public static void readStack(CompoundTag data, Consumer<ItemStack> consumer) {
		ItemStack.CODEC.parse(NbtOps.INSTANCE, data).ifSuccess(consumer);
	}

	public static void writeStackToJson(ItemStack stack, Consumer<JsonObject> consumer) {
		ItemStack.CODEC.encodeStart(JsonOps.INSTANCE, stack).ifSuccess(data -> {
			if (data instanceof JsonObject) {
				consumer.accept(data.getAsJsonObject());
			}
		});
	}
	public static void readStack(JsonObject data, Consumer<ItemStack> consumer) {
		ItemStack.CODEC.parse(JsonOps.INSTANCE, data).ifSuccess(consumer);
	}

	public static void spawnAsEntity(Player player, List<ItemStack> stacks) {
		if(stacks == null || stacks.isEmpty()) return;
		spawnAsEntity(player.level(), player.getEyePosition(), stacks.toArray(new ItemStack[0]));
	}
	public static void spawnAsEntity(Player player, ItemStackHandler handler) {
		if(handler==null || handler.getSlots()==0) return;
		for(int i = 0; i < handler.getSlots(); i++) {
			spawnAsEntity(player.level(), player.getEyePosition(), handler.getStackInSlot(i));
		}
	}
	public static void spawnAsEntity(Level world, Vec3 pos, ItemStack... stacks){
		if (!world.isClientSide()) {
			for (ItemStack stack : stacks) {
				if(!stack.isEmpty()){
					ItemEntity item = new ItemEntity(world, pos.x, pos.y, pos.z, stack.copy());
					item.setPickUpDelay(1);
					world.addFreshEntity(item);
				}
			}
		}
	}
}
