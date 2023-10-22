package cat.jiu.core.util.base;

import java.awt.*;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import javax.annotation.Nonnull;

import cat.jiu.core.api.IMenuComponent;
import cat.jiu.core.events.client.MenuComponentEvent;
import net.minecraft.client.gui.*;
import org.lwjgl.input.Keyboard;

import com.google.common.collect.Lists;

import cat.jiu.core.api.handler.IBuilder;

import net.minecraft.client.Minecraft;
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
import net.minecraft.world.World;

import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.SlotItemHandler;

public class BaseUI {
	@Deprecated
	@SideOnly(Side.CLIENT)
	public static class BaseGui<CONTAINER extends Container, TILEENTITY extends TileEntity> extends BaseTitleEntityGui<CONTAINER, TILEENTITY> {
		public BaseGui(CONTAINER container, EntityPlayer player, TILEENTITY te, ResourceLocation background, int xSize, int ySize) {
			super(container, player, te, background, xSize, ySize);
		}

		public BaseGui(CONTAINER container, EntityPlayer player, World world, BlockPos pos, ResourceLocation background, int xSize, int ySize) {
			super(container, player, world, pos, background, xSize, ySize);
		}
	}

	@SideOnly(Side.CLIENT)
	public static class BaseTitleEntityGui<CONTAINER extends Container, TILEENTITY extends TileEntity> extends BaseGuiNormal<CONTAINER> {
		public BaseTitleEntityGui(CONTAINER container, EntityPlayer player, TILEENTITY te, ResourceLocation background, int xSize, int ySize) {
			super(container, player, te.getWorld(), te.getPos(), background, xSize, ySize);
		}

		public BaseTitleEntityGui(CONTAINER container, EntityPlayer player, World world, BlockPos pos, ResourceLocation background, int xSize, int ySize) {
			super(container, player, world, pos, background, xSize, ySize);
		}

		@SuppressWarnings("unchecked")
		protected TILEENTITY getTileEntity() {
			if(this.getContainer() instanceof BaseTileEntityContainer) {
				return ((BaseTileEntityContainer<TILEENTITY>) this.getContainer()).getTileEntity();
			}
			return (TILEENTITY) this.world.getTileEntity(this.pos);
		}
	}

	@SideOnly(Side.CLIENT)
	public static class BaseGuiNormal<CONTAINER extends Container> extends GuiContainer {
		public final ResourceLocation background;
		protected final EntityPlayer player;
		protected final World world;
		protected final BlockPos pos;
		protected final CONTAINER container;

		public BaseGuiNormal(CONTAINER container, EntityPlayer player, ResourceLocation background, int xSize, int ySize) {
			this(container, player, player.world, player.getPosition(), background, xSize, ySize);
		}

		public BaseGuiNormal(CONTAINER container, EntityPlayer player, World world, BlockPos pos, ResourceLocation background, int xSize, int ySize) {
			super(container);
			this.background = background;
			this.xSize = xSize;
			this.ySize = ySize;
			this.player = player;
			this.world = world;
			this.pos = pos;
			this.container = container;
		}

		protected void init() {
		}

		public final void initGui() {
			super.initGui();
			this.init();
		}

		@Override
		protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
			int x = (this.width - this.xSize) / 2;
			int y = (this.height - this.ySize) / 2;

			GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
			this.mc.getTextureManager().bindTexture(this.background);
			this.drawTexturedModalRect(x, y, 0, 0, this.xSize, this.ySize);
		}

		protected void drawGuiScreen(int mouseX, int mouseY, float partialTicks) {
		}

		public final void drawScreen(int mouseX, int mouseY, float partialTicks) {
			super.drawDefaultBackground();
			super.drawScreen(mouseX, mouseY, partialTicks);
			super.renderHoveredToolTip(mouseX, mouseY);
			this.drawGuiScreen(mouseX, mouseY, partialTicks);
		}

		protected CONTAINER getContainer() {
			return this.container;
		}

		public static boolean isInRange(int mouseX, int mouseY, int x, int y, int width, int height) {
			return (mouseX >= x && mouseY >= y) && (mouseX <= x + width && mouseY <= y + height);
		}
	}

	@SideOnly(Side.CLIENT)
	public static class GuiDecimalNumberTextField extends GuiTextField {
		protected double min = Double.MIN_NORMAL, max = Double.MAX_VALUE;
		public GuiDecimalNumberTextField(int componentId, FontRenderer fontRenderer, int x, int y, int par5Width, int par6Height) {
			super(componentId, fontRenderer, x, y, par5Width, par6Height);
			this.setText("0");
		}
		public GuiDecimalNumberTextField setNumberRange(double min, double max) {
			this.min = min;
			this.max = max;
			return this;
		}
		
		@Override
		public boolean textboxKeyTyped(char typedChar, int keyCode) {
			boolean f = false;
			if("0123456789.".contains(String.valueOf(typedChar))) {
				f = true;
			}
			if(typedChar == '.' && !this.getText().contains(".")) {
				f = true;
			}
			switch(keyCode) {
				case Keyboard.KEY_UP:
				case Keyboard.KEY_DOWN:
				case Keyboard.KEY_LEFT:
				case Keyboard.KEY_RETURN:
				case Keyboard.KEY_BACK: f = true; break;
			}
			if(f) {
				boolean flag = super.textboxKeyTyped(typedChar, keyCode);
				double num = this.getNumber();
				if(num > this.max) {
					this.setText(String.valueOf(this.max));
				}
				if(num < this.min) {
					this.setText(String.valueOf(this.min));
				}
				return flag;
			}
			return false;
		}
		public double getNumber() {
			if(this.getText().isEmpty()) {
				this.setText("0");
			}
			if(this.getText().endsWith(".")) {
				this.setText(this.getText()+"0");
			}
			return Double.parseDouble(this.getText());
		}
	}

	@SideOnly(Side.CLIENT)
	public static class GuiNumberTextField extends GuiTextField {
		protected long min = Long.MIN_VALUE, max = Long.MAX_VALUE;
		public GuiNumberTextField(int componentId, FontRenderer fontRenderer, int x, int y, int par5Width, int par6Height) {
			super(componentId, fontRenderer, x, y, par5Width, par6Height);
		}
		public GuiNumberTextField setNumberRange(long min, long max) {
			this.min = min;
			this.max = max;
			return this;
		}
		
		@Override
		public boolean textboxKeyTyped(char typedChar, int keyCode) {
			boolean f = false;
			if("0123456789".contains(String.valueOf(typedChar))) {
				f = true;
			}
			switch(keyCode) {
				case Keyboard.KEY_UP:
				case Keyboard.KEY_DOWN:
				case Keyboard.KEY_LEFT:
				case Keyboard.KEY_RETURN:
				case Keyboard.KEY_BACK: f = true; break;
			}
			if(f) {
				boolean flag = super.textboxKeyTyped(typedChar, keyCode);
				long num = this.getNumber();
				if(num > this.max) {
					this.setText(String.valueOf(this.max));
				}
				if(num < this.min) {
					this.setText(String.valueOf(this.min));
				}
				return flag;
			}
			return false;
		}
		public long getNumber() {
			if(this.getText().isEmpty()) {
				this.setText("0");
			}
			return Long.parseLong(this.getText());
		}
	}

	/**
	 * player use Keyboard key_tab on text field, can fast input player name
	 * 
	 * @author small_jiu
	 */
	@SideOnly(Side.CLIENT)
	public static class GuiNameTextField extends GuiTextField {
		protected final boolean canSelectedSelf;

		public GuiNameTextField(int componentId, FontRenderer fontRenderer, int x, int y, int par5Width, int par6Height, boolean canSelectedSelf) {
			super(componentId, fontRenderer, x, y, par5Width, par6Height);
			this.canSelectedSelf = canSelectedSelf;
		}

		private int index = 0;

		@Override
		public boolean textboxKeyTyped(char typedChar, int keyCode) {
			boolean lag = super.textboxKeyTyped(typedChar, keyCode);
			if(!lag && keyCode == Keyboard.KEY_TAB) {
				List<EntityPlayer> playerList = Minecraft.getMinecraft().player.world.playerEntities;
				if(this.index >= playerList.size()) {
					this.index = 0;
				}
				String name = playerList.get(this.index).getName();
				if(this.canSelectedSelf) {
					super.setText(name);
					this.index++;
				}else if(playerList.size() > 1) {
					if(name.equals(Minecraft.getMinecraft().player.getName())) {
						this.index++;
						return this.textboxKeyTyped(typedChar, keyCode);
					}else {
						super.setText(name);
					}
					lag = true;
					this.index++;
				}
			}
			return lag;
		}
	}

	@Deprecated
	public static class BaseContainer<TILEENTITY extends TileEntity> extends BaseTileEntityContainer<TILEENTITY> {
		public BaseContainer(EntityPlayer player, World world, BlockPos pos) {
			super(player, world, pos);
		}

		public BaseContainer(TILEENTITY te, EntityPlayer player, World world, BlockPos pos) {
			super(player, te);
		}
	}

	public static class BaseTileEntityContainer<TILEENTITY extends TileEntity> extends BaseContainerNormal {
		public BaseTileEntityContainer(EntityPlayer player, TILEENTITY te) {
			super(player, te.getWorld(), te.getPos());
		}

		public BaseTileEntityContainer(EntityPlayer player, World world, BlockPos pos) {
			super(player, world, pos);
		}

		@SuppressWarnings("unchecked")
		public TILEENTITY getTileEntity() {
			return (TILEENTITY) this.world.getTileEntity(this.pos);
		}
	}

	public static class BaseContainerNormal extends Container {
		protected final BlockPos pos;
		protected final EntityPlayer player;
		protected final InventoryPlayer inventory;
		protected final World world;

		public BaseContainerNormal(EntityPlayer player, World world) {
			this(player, world, player.getPosition());
		}

		public BaseContainerNormal(EntityPlayer player, World world, BlockPos pos) {
			this.player = player;
			this.inventory = player.inventory;
			this.world = world;
			this.pos = pos;
		}

		protected void sendChanges() {
		}

		public final void detectAndSendChanges() {
			super.detectAndSendChanges();
			this.sendChanges();
		}

		@SideOnly(Side.CLIENT)
		protected void updateChanges(int id, int data) {
		}

		@SideOnly(Side.CLIENT)
		@Override
		public final void updateProgressBar(int id, int data) {
			this.updateChanges(id, data);
		}

		protected boolean canSeeUI(EntityPlayer player) {
			return true;
		}

		public boolean canInteractWith(EntityPlayer player) {
			boolean hasBlock = player.world.getBlockState(this.pos).getBlock() != Blocks.AIR;
			return player.dimension == this.player.dimension && player.getDistanceSq(this.pos) <= 32.0 && hasBlock && this.canSeeUI(player);
		}

		/**
		 * @param slot
		 *            custom slot,
		 *            args: itemHandler, slotIndex, slotX, slotY
		 * @author small_jiu
		 */
		protected void addHandlerSlot(ItemStackHandler handler, int x, int y, int slotWidth, int slotHeight, IBuilder<Slot> slot) {
			int slotIndex = 0;
			for(int slotY = 0; slotY < slotHeight; slotY++) {
				for(int slotX = 0; slotX < slotWidth; slotX++) {
					if(slotIndex >= handler.getSlots())
						return;
					this.addSlotToContainer(slot.builder(handler, slotIndex, x + (18 * slotX), y + (18 * slotY)));
					slotIndex += 1;
				}
			}
		}

		protected void addHandlerSlot(ItemStackHandler handler, int x, int y, int slotWidth, int slotHeight) {
			int slotIndex = 0;
			for(int slotY = 0; slotY < slotHeight; slotY++) {
				for(int slotX = 0; slotX < slotWidth; slotX++) {
					if(slotIndex >= handler.getSlots())
						return;
					this.addSlotToContainer(new SlotItemHandler(handler, slotIndex, x + (18 * slotX), y + (18 * slotY)));
					slotIndex += 1;
				}
			}
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

	public static final class ContainerNull extends Container {
		public boolean canInteractWith(EntityPlayer entityplayer) {
			return true;
		}
	}

	@SideOnly(Side.CLIENT)
	public static class GuiClickButton extends GuiButton {
		protected final IButtonClickEvent event;

		public GuiClickButton(int buttonId, int x, int y, int widthIn, int heightIn, String buttonText, IButtonClickEvent event) {
			super(buttonId, x, y, widthIn, heightIn, buttonText);
			this.event = event;
		}

		public GuiClickButton(int buttonId, int x, int y, String buttonText, IButtonClickEvent event) {
			super(buttonId, x, y, buttonText);
			this.event = event;
		}
		@Deprecated
		public GuiClickButton(int buttonId, int x, int y, int widthIn, int heightIn, String buttonText, IButtonClickEvent_Old event) {
			super(buttonId, x, y, widthIn, heightIn, buttonText);
			this.event = event;
		}

		@Deprecated
		public GuiClickButton(int buttonId, int x, int y, String buttonText, IButtonClickEvent_Old event) {
			super(buttonId, x, y, buttonText);
			this.event = event;
		}

		@Override
		public void mouseReleased(int mouseX, int mouseY) {
			if(this.event != null)
				this.event.click(this, mouseX, mouseY);
		}

		public static interface IButtonClickEvent_Old extends IButtonClickEvent {
			void click(int mouseX, int mouseY);

			@Override
			default void click(GuiClickButton button, int mouseX, int mouseY){
				this.click(mouseX, mouseY);
			}
		}
		public static interface IButtonClickEvent {
			void click(GuiClickButton button, int mouseX, int mouseY);
		}
	}

	public static class UndefinedIndexSlot extends SlotItemHandler {
		protected int index;

		public UndefinedIndexSlot(IItemHandler itemHandler, int index, int xPosition, int yPosition) {
			super(itemHandler, index, xPosition, yPosition);
			this.index = index;
		}

		@Override
		public boolean isItemValid(@Nonnull ItemStack stack) {
			if(stack.isEmpty() || !getItemHandler().isItemValid(this.index, stack) || this.index >= this.getItemHandler().getSlots()) {
				return false;
			}

			IItemHandler handler = this.getItemHandler();
			ItemStack remainder;
			if(handler instanceof IItemHandlerModifiable) {
				IItemHandlerModifiable handlerModifiable = (IItemHandlerModifiable) handler;
				ItemStack currentStack = handlerModifiable.getStackInSlot(this.index);

				handlerModifiable.setStackInSlot(this.index, ItemStack.EMPTY);

				remainder = handlerModifiable.insertItem(this.index, stack, true);

				handlerModifiable.setStackInSlot(this.index, currentStack);
			}else {
				remainder = handler.insertItem(this.index, stack, true);
			}
			return remainder.getCount() < stack.getCount();
		}

		@Override
		public int getItemStackLimit(@Nonnull ItemStack stack) {
			if(this.index >= this.getItemHandler().getSlots()) {
				return 0;
			}
			ItemStack maxAdd = stack.copy();
			int maxInput = stack.getMaxStackSize();
			maxAdd.setCount(maxInput);

			IItemHandler handler = this.getItemHandler();
			ItemStack currentStack = handler.getStackInSlot(this.index);
			if(handler instanceof IItemHandlerModifiable) {
				IItemHandlerModifiable handlerModifiable = (IItemHandlerModifiable) handler;

				handlerModifiable.setStackInSlot(this.index, ItemStack.EMPTY);

				ItemStack remainder = handlerModifiable.insertItem(this.index, maxAdd, true);

				handlerModifiable.setStackInSlot(this.index, currentStack);

				return maxInput - remainder.getCount();
			}else {
				ItemStack remainder = handler.insertItem(this.index, maxAdd, true);

				int current = currentStack.getCount();
				int added = maxInput - remainder.getCount();
				return current + added;
			}
		}

		@Override
		@Nonnull
		public ItemStack decrStackSize(int amount) {
			if(this.index >= this.getItemHandler().getSlots()) {
				return ItemStack.EMPTY;
			}
			return this.getItemHandler().extractItem(this.index, amount, false);
		}

		@Override
		public boolean canTakeStack(EntityPlayer playerIn) {
			if(this.index >= this.getItemHandler().getSlots()) {
				return false;
			}
			return !this.getItemHandler().extractItem(this.index, 1, true).isEmpty();
		}

		@Override
		public int getSlotStackLimit() {
			if(this.index >= this.getItemHandler().getSlots()) {
				return 0;
			}
			return this.getItemHandler().getSlotLimit(this.index);
		}

		@Override
		public void putStack(@Nonnull ItemStack stack) {
			if(this.index < this.getItemHandler().getSlots()) {
				((IItemHandlerModifiable) this.getItemHandler()).setStackInSlot(this.index, stack);
				this.onSlotChanged();
			}
		}

		@Override
		@Nonnull
		public ItemStack getStack() {
			if(this.index >= this.getItemHandler().getSlots()) {
				return ItemStack.EMPTY;
			}
			return this.getItemHandler().getStackInSlot(this.index);
		}

		@Override
		public int getSlotIndex() {
			return this.index;
		}

		public UndefinedIndexSlot setIndex(int index) {
			this.index = index;
			return this;
		}
	}

	@SideOnly(Side.CLIENT)
	public static class GuiImageButton extends GuiButton {
		protected static final Color HOVERED_COLOR = new Color(0, 255, 255, 85);
		protected final GuiScreen gui;
		protected final ResourceLocation background;
		protected int u, v = 0;
		protected final int uWidth, vHeight, tileWidth, tileHeight;
		protected int hoveredColor = HOVERED_COLOR.getRGB();

		public GuiImageButton(GuiScreen gui, int buttonId, int x, int y, int widthIn, int heightIn, String hoveringText, ResourceLocation background, int tileWidth, int tileHeight, int uWidth, int vHeight) {
			super(buttonId, x, y, widthIn, heightIn, hoveringText);
			this.gui = gui;
			this.background = background;
			this.tileWidth = tileWidth;
			this.tileHeight = tileHeight;
			this.uWidth = uWidth;
			this.vHeight = vHeight;
			this.width = this.uWidth;
			this.height = this.vHeight;
		}

		public GuiImageButton(GuiScreen gui, int buttonId, int x, int y, String hoveringText, ResourceLocation background, int tileWidth, int tileHeight, int uWidth, int vHeight) {
			super(buttonId, x, y, hoveringText);
			this.gui = gui;
			this.background = background;
			this.tileWidth = tileWidth;
			this.tileHeight = tileHeight;
			this.uWidth = uWidth;
			this.vHeight = vHeight;
			this.width = this.uWidth;
			this.height = this.vHeight;
		}

		public GuiImageButton(GuiScreen gui, int buttonId, int x, int y, int widthIn, int heightIn, String hoveringText, ResourceLocation background, int tileWidth, int tileHeight, int u, int v, int uWidth, int vHeight) {
			super(buttonId, x, y, widthIn, heightIn, hoveringText);
			this.gui = gui;
			this.background = background;
			this.tileWidth = tileWidth;
			this.tileHeight = tileHeight;
			this.u = u;
			this.v = v;
			this.uWidth = uWidth;
			this.vHeight = vHeight;
			this.width = this.uWidth;
			this.height = this.vHeight;
		}

		public GuiImageButton(GuiScreen gui, int buttonId, int x, int y, String hoveringText, ResourceLocation background, int tileWidth, int tileHeight, int u, int v, int uWidth, int vHeight) {
			super(buttonId, x, y, hoveringText);
			this.gui = gui;
			this.background = background;
			this.tileWidth = tileWidth;
			this.tileHeight = tileHeight;
			this.u = u;
			this.v = v;
			this.uWidth = uWidth;
			this.vHeight = vHeight;
			this.width = this.uWidth;
			this.height = this.vHeight;
		}

		public GuiImageButton setImageX(int u) {
			this.u = u;
			return this;
		}
		public GuiImageButton setImageY(int v) {
			this.v = v;
			return this;
		}
		public GuiImageButton setImageWidth(int drawWidth) {
			this.width = drawWidth;
			return this;
		}
		public GuiImageButton setImageHeight(int drawHeight) {
			this.height = drawHeight;
			return this;
		}

		public GuiImageButton setHoveredColor(int hoveredColor) {
			this.hoveredColor = hoveredColor;
			return this;
		}
		public GuiImageButton setHoveredColor(Color hoveredColor) {
			this.hoveredColor = hoveredColor.getRGB();
			return this;
		}

		@Override
		public void drawButton(Minecraft mc, int mouseX, int mouseY, float partialTicks) {
			if(this.visible) {
				this.hovered = mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height;

				GlStateManager.pushMatrix();
				GlStateManager.enableAlpha();
				GlStateManager.color(1.0F, 1.0F, 1.0F);
				mc.getTextureManager().bindTexture(this.background);
				Gui.drawScaledCustomSizeModalRect(this.x, this.y, this.u, this.v, this.uWidth, this.vHeight, this.width, this.height, this.tileWidth, this.tileHeight);
				GlStateManager.disableAlpha();
				GlStateManager.popMatrix();

				if(this.isMouseOver()) {
					this.drawGradientRect(this.x, this.y, this.x + this.width, this.y + this.height, this.hoveredColor, this.hoveredColor);
					if(this.displayString!=null) this.gui.drawHoveringText(this.displayString, mouseX, mouseY);
				}
			}
		}
	}

	@SideOnly(Side.CLIENT)
	public static class GuiButtonPopupMenu extends GuiScreen {
		public final List<GuiButton> buttons;
		protected boolean visible = false;
		public final Scroll<List<GuiButton>> scroll;
		private IDrawEvent event;

		public GuiButtonPopupMenu() {
			this(Lists.newArrayList());
		}

		public GuiButtonPopupMenu(List<GuiButton> buttons) {
			this.buttons = buttons;
			this.scroll = new Scroll<>(this.buttons);
		}

		public GuiButtonPopupMenu setDrawEvent(IDrawEvent event) {
			this.event = event;
			return this;
		}

		int createX = 0;
		int createY = 0;

		public void setCreatePoint(int createX, int createY) {
			this.createX = createX;
			this.createY = createY;
		}

		public void setVisible(boolean visible) {
			this.visible = visible;

			int btnY = this.createY;
			int width = getWidth();
			for (GuiButton btn : buttons) {
				btn.visible = visible;
				if (visible) {
					btn.width = width;
					btn.x = this.createX;
					btn.y = btnY;
					btnY += btn.height;
				}
			}
		}

		public boolean isVisible() {
			return visible;
		}

		public void drawPopupMenu(Minecraft mc, int x, int y, float partialTicks) {
			if (this.visible) {
				int btnY = this.createY;
				for (int i : this.scroll.getShows()) {
					GuiButton btn = this.buttons.get(i);
					btn.y = btnY;
					btn.drawButton(mc, x, y, partialTicks);
					if (this.event != null) {
						this.event.draw(btn);
					}
					btnY += btn.height;
				}
				GuiButton btn = this.buttons.get(this.scroll.getShows()[this.scroll.getShows().length - 1]);
				this.drawHorizontalLine(btn.x, btn.x + btn.width - 1, btn.y + btn.height, Color.BLACK.getRGB());
			}
		}

		public int getHeight() {
			int height = 0;
			for (int show : this.scroll.getShows()) {
				height += this.buttons.get(show).height;
			}
			return height;
		}

		public int getWidth() {
			int width = 0;
			for (GuiButton btn : this.buttons) {
				width = Math.max(width, btn.width);
			}
			return width;
		}

		public boolean mouseClicked(Minecraft mc, int mouseX, int mouseY, int mouseButton) {
			boolean flag = false;
			if (mouseButton == 0 && this.isVisible()) {
				for (GuiButton btn : this.buttons) {
					if (btn.mousePressed(mc, mouseX, mouseY)) {
						GuiScreenEvent.ActionPerformedEvent.Pre event = new GuiScreenEvent.ActionPerformedEvent.Pre(this, btn, this.buttons);
						if (MinecraftForge.EVENT_BUS.post(event))
							break;
						btn = event.getButton();
						btn.playPressSound(mc.getSoundHandler());
						btn.mouseReleased(mouseX, mouseY);
						flag = true;
						MinecraftForge.EVENT_BUS.post(new GuiScreenEvent.ActionPerformedEvent.Post(this, btn, this.buttons));
					}
				}
			}
//			this.setVisible(false);
			return flag;
		}

		@Override
		public <T extends GuiButton> T addButton(T buttonIn) {
			this.buttons.add(buttonIn);
			this.scroll.init();
			return buttonIn;
		}

		public boolean scroll(int mouseX, int mouseY, int key) {
			if (this.visible && isInRange(mouseX, mouseY, this.createX, this.createY, this.getWidth(), this.getHeight())) {
				int page = 0;

				if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT)) {
					page += 2;
				}
				if (Keyboard.isKeyDown(Keyboard.KEY_LCONTROL) || Keyboard.isKeyDown(Keyboard.KEY_RCONTROL)) {
					page += 1;
				}
				if (key == 120) {
					this.scroll.go(-1 - page);
					return true;
				} else if (key == -120) {
					this.scroll.go(1 + page);
					return true;
				}
			}
			return false;
		}

		static boolean isInRange(int mouseX, int mouseY, int x, int y, int width, int height) {
			return (mouseX >= x && mouseY >= y) && (mouseX <= x + width && mouseY <= y + height);
		}

		public static interface IDrawEvent {
			void draw(GuiButton button);
		}
	}

	@SideOnly(Side.CLIENT)
	public static abstract class MenuComponentBase implements IMenuComponent {
		protected int x, y, width, height;
		protected boolean visible;

		public MenuComponentBase(int width, int height) {
			this(0,0,width, height);
		}
		public MenuComponentBase(int x, int y, int width, int height) {
			this.x = x;
			this.y = y;
			this.width = width;
			this.height = height;
		}

		@Override
		public boolean handMouseInput() {
			return false;
		}

		@Override
		public boolean handleInput() {
			return false;
		}

		@Override
		public boolean handleKeyboardInput() {
			return false;
		}

		@Override
		public boolean mouseClicked(int mouseX, int mouseY, int mouseButton) {
			return false;
		}

		@Override
		public boolean keyTyped(char typedChar, int keyCode) {
			return false;
		}

		@Override
		public int getX() {
			return this.x;
		}

		@Override
		public int getY() {
			return this.y;
		}

		@Override
		public MenuComponentBase setX(int x) {
			this.x = x;
			return this;
		}

		@Override
		public MenuComponentBase setY(int y) {
			this.y = y;
			return this;
		}

		@Override
		public int getWidth() {
			return this.width;
		}

		@Override
		public int getHeight() {
			return this.height;
		}

		@Override
		public MenuComponentBase setWidth(int width) {
			this.width = width;
			return this;
		}

		@Override
		public MenuComponentBase setHeight(int height) {
			this.height = height;
			return this;
		}

		@Override
		public boolean isVisible() {
			return visible;
		}

		@Override
		public MenuComponentBase setVisible(boolean visible) {
			this.visible = visible;
			return this;
		}
	}

	@SideOnly(Side.CLIENT)
	public static class GuiVisibleButtonPopupMenu extends GuiButtonPopupMenu {

		public GuiVisibleButtonPopupMenu() {
			this(Lists.newArrayList());
		}

		public GuiVisibleButtonPopupMenu(List<GuiButton> components) {
			super(components);
			super.setVisible(true);
		}

		@Override
		public void setVisible(boolean visible) {
			super.setVisible(true);
		}
	}

	@SideOnly(Side.CLIENT)
	public static class GuiVisibleComponentPopupMenu extends GuiComponentPopupMenu {

		public GuiVisibleComponentPopupMenu(GuiScreen gui) {
			this(gui, Lists.newArrayList());
		}

		public GuiVisibleComponentPopupMenu(GuiScreen gui, List<IMenuComponent> components) {
			super(gui, components);
			super.setVisible(true);
		}

		@Override
		public void setVisible(boolean visible) {
			super.setVisible(true);
		}
	}

	@SideOnly(Side.CLIENT)
	public static class GuiComponentPopupMenu extends Gui {
		public final GuiScreen gui;
		public final List<IMenuComponent> components;
		protected boolean visible = false;
		public final Scroll<List<IMenuComponent>> scroll;
		private IMenuComponent.IDrawEvent event;

		public GuiComponentPopupMenu(GuiScreen gui) {
			this(gui, Lists.newArrayList());
		}

		public GuiComponentPopupMenu(GuiScreen gui, List<IMenuComponent> components) {
			this.gui = gui;
			this.components = components;
			this.scroll = new Scroll<>(this.components);
		}

		public GuiComponentPopupMenu setDrawEvent(IMenuComponent.IDrawEvent event) {
			this.event = event;
			return this;
		}

		int createX = 0;
		int createY = 0;

		public void setCreatePoint(int createX, int createY) {
			this.createX = createX;
			this.createY = createY;
		}

		public void setVisible(boolean visible) {
			this.visible = visible;

			int btnY = this.createY;
			int width = getWidth();
			for (IMenuComponent component : this.components) {
				component.setVisible(visible);
				if (visible) {
					component.setWidth(width)
							.setX(this.createX)
							.setY(btnY);
					btnY += component.getHeight();
				}
			}
		}

		public boolean isVisible() {
			return visible;
		}

		public void drawPopupMenu(Minecraft mc, int mouseX, int mouseY, float partialTicks) {
			if (this.visible) {
				int btnY = this.createY;
				for (int i : this.scroll.getShows()) {
					IMenuComponent component = this.components.get(i);
					if (component.isVisible()) {
						component.setY(btnY)
								.draw(this.gui, mouseX, mouseY);
						if (this.event != null) {
							this.event.draw(component);
						}
						btnY += component.getHeight();
					}
				}
			}
		}

		public int getHeight() {
			int height = 0;
			for (int show : this.scroll.getShows()) {
				height += this.components.get(show).getHeight();
			}
			return height;
		}

		public int getWidth() {
			int width = 0;
			for (IMenuComponent btn : this.components) {
				width = Math.max(width, btn.getWidth());
			}
			return width;
		}

		public boolean mouseClicked(Minecraft mc, int mouseX, int mouseY, int mouseButton) {
			if (this.visible) {
				for (int show : this.scroll.getShows()) {
					if (this.components.get(show).isVisible() && this.components.get(show).mouseClicked(mouseX, mouseY, mouseButton)) {
						MinecraftForge.EVENT_BUS.post(new MenuComponentEvent.ComponentClicked(this.components.get(show), mouseX, mouseY, mouseButton));
						return true;
					}
				}
			}
			return false;
		}

		public boolean keyTyped(char typedChar, int keyCode) {
			if (this.visible) {
				for (int show : this.scroll.getShows()) {
					if (this.components.get(show).isVisible() && this.components.get(show).keyTyped(typedChar, keyCode)) {
						MinecraftForge.EVENT_BUS.post(new MenuComponentEvent.ComponentKeyTyped(this.components.get(show), typedChar, keyCode));
						return true;
					}
				}
			}
			return false;
		}

		public boolean handleInput() {
			if (this.visible) {
				for (int show : this.scroll.getShows()) {
					if (this.components.get(show).isVisible() && this.components.get(show).handleInput()) {
						MinecraftForge.EVENT_BUS.post(new MenuComponentEvent.ComponentInput.All(this.components.get(show)));
						return true;
					}
				}
			}
			return false;
		}

		public boolean handMouseInput() {
			if (this.visible) {
				for (int show : this.scroll.getShows()) {
					if (this.components.get(show).isVisible() && this.components.get(show).handMouseInput()) {
						MinecraftForge.EVENT_BUS.post(new MenuComponentEvent.ComponentInput.Mouse(this.components.get(show)));
						return true;
					}
				}
			}
			return false;
		}

		public boolean handleKeyboardInput() {
			if (this.visible) {
				for (int show : this.scroll.getShows()) {
					if (this.components.get(show).isVisible() && this.components.get(show).handleKeyboardInput()) {
						MinecraftForge.EVENT_BUS.post(new MenuComponentEvent.ComponentInput.Keyboard(this.components.get(show)));
						return true;
					}
				}
			}
			return false;
		}

		public <T extends IMenuComponent> T addComponent(T buttonIn) {
			this.components.add(buttonIn);
			this.scroll.init();
			return buttonIn;
		}

		public boolean scroll(int mouseX, int mouseY, int key) {
			if (this.visible && isInRange(mouseX, mouseY, this.createX, this.createY, this.getWidth(), this.getHeight())) {
				int page = 0;

				if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT)) {
					page += 2;
				}
				if (Keyboard.isKeyDown(Keyboard.KEY_LCONTROL) || Keyboard.isKeyDown(Keyboard.KEY_RCONTROL)) {
					page += 1;
				}
				if (key == 120) {
					this.scroll.go(-1 - page);
					return true;
				} else if (key == -120) {
					this.scroll.go(1 + page);
					return true;
				}
			}
			return false;
		}

		static boolean isInRange(int mouseX, int mouseY, int x, int y, int width, int height) {
			return (mouseX >= x && mouseY >= y) && (mouseX <= x + width && mouseY <= y + height);
		}
	}

	public static class Scroll<T extends Collection<?>> {
		public final T collection;
		protected int size = -1;
		protected int[] shows = null;
		protected int[] ids = null;
		protected int page = 0;
		protected int showCount = 5;

		public Scroll(T collection) {
			this.collection = collection;
		}

		public int[] getShows() {
			return shows;
		}
		public int getPage() {
			return page;
		}

		public int getShowCount() {
			return showCount;
		}

		public Scroll<T> setShowCount(int showCount) {
			this.showCount = showCount;
			return this;
		}

		public boolean go(int page) {
			if(this.ids == null) return false;
			if(this.ids.length > this.getShowCount()) {
				this.page += page;

				if(this.page < 0) {
					this.page = 0;
				}
				if(this.page > this.ids.length - this.getShowCount()) {
					this.page = this.ids.length - this.getShowCount();
				}
				int[] shows = Arrays.copyOfRange(this.ids, this.page, this.page + this.getShowCount());
				if (!Arrays.equals(this.shows, shows)) {
					this.shows = shows;
					return true;
				}
				return false;
			}else {
				this.shows = this.ids;
			}
			return false;
		}

		public void init() {
			if(this.size == -1) this.size = collection.size();
			if(this.ids == null && this.size > 0) {
				this.ids = toArray(collection.size());
				this.page = 0;
				this.go(0);
			}
			if(this.size != collection.size()) {
				this.size = collection.size();
				this.ids = toArray(collection.size());
				this.go(0);
			}
		}

		public boolean scroll(int key){
			int page = 0;

			if(Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT)) {
				page += 2;
			}
			if(Keyboard.isKeyDown(Keyboard.KEY_LCONTROL) || Keyboard.isKeyDown(Keyboard.KEY_RCONTROL)) {
				page += 1;
			}

			if(key == 120) {
				this.go(-1 - page);
				return true;
			}else if (key == -120){
				this.go(1 + page);
				return true;
			}
			return false;
		}

		static int[] toArray(int size){
			int[] array = new int[size];
			for (int i = 0; i < size; i++) {
				array[i] = i;
			}
			return array;
		}
	}
}
