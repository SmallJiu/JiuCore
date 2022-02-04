package cat.jiu.core.util.base;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.Container;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BaseUI {
	@SideOnly(Side.CLIENT)
	public class BaseGui extends GuiContainer{
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
		protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
			int x = (this.width - this.xSize) / 2;
			int y = (this.height - this.ySize) / 2;
			
			GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
			this.mc.getTextureManager().bindTexture(this.background);
			this.drawTexturedModalRect(x, y, 0, 0, this.xSize, this.ySize);
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
		
	}
}
