package cat.jiu.core.util.crafting;

import java.util.List;
import java.util.Set;

import cat.jiu.core.util.base.BaseUI.ContainerNull;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraftforge.items.IItemHandler;

public final class InventoryCraftingNull extends InventoryCrafting {
	public InventoryCraftingNull(int width, int height) {
		super(new ContainerNull(), width, height);
	}
	public InventoryCraftingNull(int width, int height, ItemStack[] slots) {
		this(width, height);
		this.putStacks(slots);
	}
	public InventoryCraftingNull(int width, int height, Slot[] slots) {
		this(width, height);
		this.putStacks(slots);
	}
	public InventoryCraftingNull(int width, int height, List<ItemStack> slots) {
		this(width, height);
		this.putStacks(slots);
	}
	public InventoryCraftingNull(int width, int height, Set<ItemStack> slots) {
		this(width, height);
		this.putStacks(slots);
	}
	public InventoryCraftingNull(int width, int height, IItemHandler slots) {
		this(width, height);
		this.putStacks(slots);
	}
	public InventoryCraftingNull(int width, int height, ItemStack stack) {
		this(width, height);
		this.putStacks(NonNullList.withSize(width * height, stack));
	}
	
	public boolean putStacks(List<ItemStack> slots) {
		return this.putStacks(slots.toArray(new ItemStack[0]));
	}
	public boolean putStacks(Set<ItemStack> slots) {
		return this.putStacks(slots.toArray(new ItemStack[0]));
	}
	public boolean putStacks(ItemStack[] slots) {
		for(int i = 0; i < slots.length; i++) {
			if(i >= super.getSizeInventory()) break;
			super.setInventorySlotContents(i, slots[i]);
		}
		return true;
	}
	public boolean putStacks(IItemHandler slots) {
		for(int i = 0; i < slots.getSlots(); i++) {
			if(i >= super.getSizeInventory()) break;
			super.setInventorySlotContents(i, slots.getStackInSlot(i));
		}
		return true;
	}
	public boolean putStacks(Slot[] slots) {
		for(int i = 0; i < slots.length; i++) {
			if(i >= super.getSizeInventory()) break;
			super.setInventorySlotContents(i, slots[i].getStack());
		}
		return true;
	}
	
	public boolean hasRecipe(World world) {
		return this.findMatchingRecipe(world) != null;
	}
	public IRecipe findMatchingRecipe(World world) {
		if(super.isEmpty()) return null;
		return CraftingManager.findMatchingRecipe(this, world);
	}
}
