package cat.jiu.core.util.base;

import java.util.List;

import javax.annotation.Nonnull;

import org.lwjgl.input.Mouse;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.SlotItemHandler;

public class BaseUI {
	@SideOnly(Side.CLIENT)
	public static abstract class BaseGui extends GuiContainer{
		public final ResourceLocation background;
		protected final EntityPlayer player;
		protected final World world;
		protected final BlockPos pos;
		protected TileEntity te = null;
		
		public BaseGui(Container container, EntityPlayer player, TileEntity te, ResourceLocation background, int xSize, int ySize) {
			this(container, player, te.getWorld(), te.getPos(), background, ySize, ySize);
		}
		
		public BaseGui(Container container, EntityPlayer player, World world, BlockPos pos, ResourceLocation background, int xSize, int ySize) {
			super(container);
			this.background = background;
			this.xSize = xSize;
			this.ySize = ySize;
			this.player = player;
			this.world = world;
			this.pos = pos;
			this.te = this.world.getTileEntity(pos);
		}
		
		@Override
		public void initGui() {
			super.initGui();
			this.init();
		}
		protected abstract void init();
		
		@Override
		protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
			int x = (this.width - this.xSize) / 2;
			int y = (this.height - this.ySize) / 2;
			
			GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
			this.mc.getTextureManager().bindTexture(this.background);
			this.drawTexturedModalRect(x, y, 0, 0, this.xSize, this.ySize);
		}
	}
	
	@Deprecated
	public static class ScrollBar {
		private final GuiContainer gui;
		private final int barX;
		private final int barY;
		private final int barWidth;
		private final int barHeight;
		private boolean wasClicking;
		private final ResourceLocation img;
		private final int barButtonTextureX;
		private final int barButtonTextureY;
		private final int barButtonTextureWidth;
		private final int barButtonTextureHeight;
		
		public ScrollBar(GuiContainer gui, ResourceLocation img, int barX, int barY, int barWidth, int barHeight, int barButtonTextureX, int barButtonTextureY, int barButtonTextureWidth, int barButtonTextureHeight) {
			this.gui = gui;
			this.img = img;
			this.barX = barX;
			this.barY = barY;
			this.barWidth = barWidth;
			this.barHeight = barHeight;
			this.barButtonTextureX = barButtonTextureX;
			this.barButtonTextureY = barButtonTextureY;
			this.barButtonTextureWidth = barButtonTextureWidth;
			this.barButtonTextureHeight = barButtonTextureHeight;
		}
		
		public boolean isScrolling(boolean canScroll, int mouseX, int mouseY) {
			boolean flag = Mouse.isButtonDown(0);
			int guiLeft = this.gui.getGuiLeft() + this.barX;
			int guiTop = this.gui.getGuiTop() + this.barY;
			int barWidth = guiLeft + this.barWidth;
			int barHeight = guiTop + this.barHeight;
			boolean f = false;

			if(!this.wasClicking && flag && mouseX >= guiLeft && mouseY >= guiTop && mouseX < barWidth && mouseY < barHeight) {
				f = canScroll;
			}
			if(!flag) {
				f = false;
			}
			this.wasClicking = flag;
			return f;
		}
		
		public float currentScroll(int mouseX, int mouseY) {
			float currentScroll = ((float) (mouseY - barY) - 7.5F) / ((float) (barHeight - barY) - 15.0F);
			currentScroll = MathHelper.clamp(currentScroll, 0.0F, 1.0F);
			return currentScroll;
		}
		
		public void draw(float currentScroll, boolean canScroll, int yMove, int yMaxDown) {
			this.gui.mc.getTextureManager().bindTexture(this.img);
			int guiLeft = this.gui.getGuiLeft() + this.barX;
			int guiTop = this.gui.getGuiTop() + this.barY;
			int guiMaxDown = guiTop + yMove;
			this.gui.drawTexturedModalRect(
					guiLeft, // x
					guiTop + (int) ((float) (guiMaxDown - guiTop - yMaxDown) * currentScroll),// y
					this.barButtonTextureX + (canScroll ? 0 : 12),// textureX
					this.barButtonTextureY,// textureY
					this.barButtonTextureHeight,// H
					this.barButtonTextureWidth);// W
		}
		
		public static int selectRows(int slots, float currentScroll, int slotWidth, int slotHeight) {
			int outRows = (slots + slotWidth - 1) / slotWidth - slotHeight;
			int selectRows = MathHelper.clamp((int) ((double) (currentScroll * (float) outRows) + 0.5D), 0, outRows);
			return selectRows;
		}
		
		public static void scrollTo(Container con, int stackListSize, List<ItemStack> stackList, float currentScroll, int slotWidth, int slotHeight) {
			int selectRows = selectRows(stackListSize, currentScroll, slotWidth, slotHeight);
			for(int slotY = 0; slotY < slotHeight; ++slotY) {
				for(int slotX = 0; slotX < slotWidth; ++slotX) {
					int stackIndex = slotX + (slotY + selectRows) * slotWidth;
					int slotIndex = slotX + slotY * slotWidth;

					Slot selectSlot = con.getSlot(slotIndex);
					if(stackIndex >= 0 && stackIndex < stackListSize) {
						selectSlot.putStack(stackList.get(stackIndex));
					}else {
						selectSlot.putStack(ItemStack.EMPTY);
					}
				}
			}
		}
	}
	
	public static class UndefinedIndexSlot extends SlotItemHandler {
		private int index;
		
		public UndefinedIndexSlot(IItemHandler itemHandler, int index, int xPosition, int yPosition) {
			super(itemHandler, index, xPosition, yPosition);
			this.index = index;
		}
		
		@Override
		public boolean isItemValid(@Nonnull ItemStack stack) {
			if(stack.isEmpty()
			|| !getItemHandler().isItemValid(index, stack)
			|| index >= this.getItemHandler().getSlots()) {
				return false;
			}

			IItemHandler handler = this.getItemHandler();
			ItemStack remainder;
			if(handler instanceof IItemHandlerModifiable) {
				IItemHandlerModifiable handlerModifiable = (IItemHandlerModifiable) handler;
				ItemStack currentStack = handlerModifiable.getStackInSlot(index);

				handlerModifiable.setStackInSlot(index, ItemStack.EMPTY);

				remainder = handlerModifiable.insertItem(index, stack, true);

				handlerModifiable.setStackInSlot(index, currentStack);
			}else {
				remainder = handler.insertItem(index, stack, true);
			}
			return remainder.getCount() < stack.getCount();
		}

		@Override
		public int getItemStackLimit(@Nonnull ItemStack stack) {
			if(index >= this.getItemHandler().getSlots()) {
				return 0;
			}
			ItemStack maxAdd = stack.copy();
			int maxInput = stack.getMaxStackSize();
			maxAdd.setCount(maxInput);

			IItemHandler handler = this.getItemHandler();
			ItemStack currentStack = handler.getStackInSlot(index);
			if(handler instanceof IItemHandlerModifiable) {
				IItemHandlerModifiable handlerModifiable = (IItemHandlerModifiable) handler;

				handlerModifiable.setStackInSlot(index, ItemStack.EMPTY);

				ItemStack remainder = handlerModifiable.insertItem(index, maxAdd, true);

				handlerModifiable.setStackInSlot(index, currentStack);

				return maxInput - remainder.getCount();
			}else {
				ItemStack remainder = handler.insertItem(index, maxAdd, true);

				int current = currentStack.getCount();
				int added = maxInput - remainder.getCount();
				return current + added;
			}
		}

		@Override
		@Nonnull
		public ItemStack decrStackSize(int amount) {
			if(index >= this.getItemHandler().getSlots()) {
				return ItemStack.EMPTY;
			}
			return this.getItemHandler().extractItem(index, amount, false);
		}

		@Override
		public boolean canTakeStack(EntityPlayer playerIn) {
			if(index >= this.getItemHandler().getSlots()) {
				return false;
			}
			return !this.getItemHandler().extractItem(index, 1, true).isEmpty();
		}

		@Override
		public int getSlotStackLimit() {
			if(index >= this.getItemHandler().getSlots()) {
				return 0;
			}
			return this.getItemHandler().getSlotLimit(this.index);
		}

		@Override
		public void putStack(@Nonnull ItemStack stack) {
			if(index < this.getItemHandler().getSlots()) {
				((IItemHandlerModifiable) this.getItemHandler()).setStackInSlot(index, stack);
				this.onSlotChanged();
			}
		}

		@Override
		@Nonnull
		public ItemStack getStack() {
			if(index >= this.getItemHandler().getSlots()) {
				return ItemStack.EMPTY;
			}
			return this.getItemHandler().getStackInSlot(index);
		}
		
		@Override
		public int getSlotIndex() {
			return index;
		}

		public void setIndex(int index) {
			this.index = index;
		}
	}
	
	public static abstract class BaseContainer extends Container {
		protected final BlockPos blockPos;
		protected final EntityPlayer player;
		protected final InventoryPlayer inventory;
		protected final World world;
		
		public BaseContainer(EntityPlayer player, World world, BlockPos pos) {
			this.player = player;
			this.inventory = player.inventory;
			this.world = world;
			this.blockPos = pos;
		}
		
		public abstract void sendChanges();
		
		@Override
		public final void detectAndSendChanges() {
			super.detectAndSendChanges();
			this.sendChanges();
		}
		
		@SideOnly(Side.CLIENT)
		protected abstract void updateChanges(int id, int data);
		
		@SideOnly(Side.CLIENT)
		@Override
		public final void updateProgressBar(int id, int data) {
			this.updateChanges(id, data);
		}
		
		@Override
		public final boolean canInteractWith(EntityPlayer player) {
			boolean haveBlock = player.world.getBlockState(this.blockPos).getBlock() != Blocks.AIR;
			return player.world.equals(player.getEntityWorld()) && player.getDistanceSq(this.blockPos) <= 32.0 && haveBlock;
		}
		
		protected void addPlayerInventorySlot(int x, int y) {
			int slotIndex = 0;
			for(int slotX = 0; slotX < 9; slotX++) {
				this.addSlotToContainer(new Slot(this.inventory, slotIndex, x + 18 * slotX, y + (18 * 2) + 22));
				slotIndex += 1;
			}
			
			for(int slotY = 0; slotY < 3; slotY++) {
				for(int slotX = 0; slotX < 9; slotX++) {
					this.addSlotToContainer(new Slot(this.inventory, slotIndex, x + 18 * slotX, y + (18 * slotY)));
					slotIndex += 1;
				}
			}
		}
		
		protected boolean mergeItemStack(IItemHandlerModifiable handler, ItemStack stack, int startIndex, int endIndex, boolean reverseDirection) {
			boolean flag = false;
			int i = startIndex;

			if(reverseDirection) {
				i = endIndex - 1;
			}

			if(stack.isStackable()) {
				while(!stack.isEmpty()) {
					if(reverseDirection) {
						if(i < startIndex) {
							break;
						}
					}else if(i >= endIndex) {
						break;
					}

					ItemStack itemstack = handler.getStackInSlot(i);

					if(!itemstack.isEmpty() && itemstack.getItem() == stack.getItem() && (!stack.getHasSubtypes() || stack.getMetadata() == itemstack.getMetadata()) && ItemStack.areItemStackTagsEqual(stack, itemstack)) {
						int j = itemstack.getCount() + stack.getCount();
						int maxSize = Math.min(handler.getSlotLimit(i), stack.getMaxStackSize());

						if(j <= maxSize) {
							stack.setCount(0);
							itemstack.setCount(j);
							flag = true;
						}else if(itemstack.getCount() < maxSize) {
							stack.shrink(maxSize - itemstack.getCount());
							itemstack.setCount(maxSize);
							flag = true;
						}
					}

					if(reverseDirection) {
						--i;
					}else {
						++i;
					}
				}
			}

			if(!stack.isEmpty()) {
				if(reverseDirection) {
					i = endIndex - 1;
				}else {
					i = startIndex;
				}

				while(true) {
					if(reverseDirection) {
						if(i < startIndex) {
							break;
						}
					}else if(i >= endIndex) {
						break;
					}

					ItemStack itemstack1 = handler.getStackInSlot(i);

					if(itemstack1.isEmpty() && handler.isItemValid(i, stack)) {
						if(stack.getCount() > handler.getSlotLimit(i)) {
							handler.setStackInSlot(i, stack.splitStack(handler.getSlotLimit(i)));
						}else {
							handler.setStackInSlot(i, stack.splitStack(stack.getCount()));
						}

						flag = true;
						break;
					}

					if(reverseDirection) {
						--i;
					}else {
						++i;
					}
				}
			}

			return flag;
		}
	}
}
