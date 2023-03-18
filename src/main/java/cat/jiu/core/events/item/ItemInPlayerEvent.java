package cat.jiu.core.events.item;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.player.PlayerEvent;

public class ItemInPlayerEvent extends PlayerEvent {
	protected ItemInPlayerEvent(EntityPlayer player) {
		super(player);
	}
	public static class InArmor extends ItemInPlayerEvent {
		public final ItemStack invStack;
		public final EntityEquipmentSlot slot;
		public InArmor(EntityPlayer player, ItemStack invStack, EntityEquipmentSlot slot) {
			super(player);
			this.invStack = invStack;
			this.slot = slot;
		}
	}
	public static class InHand extends ItemInPlayerEvent {
		public final boolean isMainHand;
		public final ItemStack stack;
		public InHand(EntityPlayer player, boolean mainHand, ItemStack stack) {
			super(player);
			this.isMainHand = mainHand;
			this.stack = stack;
		}
	}
	public static class InInventory extends ItemInPlayerEvent {
		public final ItemStack stack;
		public final int slot;
		public InInventory(EntityPlayer player, ItemStack invStack, int slotId) {
			super(player);
			this.stack = invStack;
			this.slot = slotId;
		}
	}
}
