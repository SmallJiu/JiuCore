package cat.jiu.core.test;

import java.util.List;

import cat.jiu.core.JiuCore;
import cat.jiu.core.util.JiuUtils;
import cat.jiu.core.util.base.BaseBlock;
import cat.jiu.core.util.base.BaseBlock.BaseBlockItem;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockTestNormal extends BaseBlock.Normal implements ITileEntityProvider {
	public BlockTestNormal() {
		super(JiuCore.MODID, "test_block_nomal", Material.ANVIL, SoundType.ANVIL, JiuCore.CORE, -1);
		Init.BLOCKS.add(this);
	}
	
	@SideOnly(Side.CLIENT)
	@Override
	public void addInformation(ItemStack stack, World world, List<String> tooltip, ITooltipFlag advanced) {
		tooltip.add(stack.getMetadata() + " : " + this.getMetaFromState(JiuUtils.item.getStateFromItemStack(stack)));
	}
	
	@Override
	public IProperty<?>[] addBlockOthersProperty() {
		return null;
//		return new IProperty[] {BlockTNT.EXPLODE, BlockLog.LOG_AXIS, BlockStone.VARIANT, BlockPlanks.VARIANT, BlockSlab.HALF, BlockLeaves.CHECK_DECAY, BlockLeaves.DECAYABLE};
	}
	
	@Override
	public ItemBlock getRegisterItemBlock() {
		return (ItemBlock) new BaseBlockItem(this, this.getHasSubtypes());
	}
	
	@SideOnly(Side.CLIENT)
	@Override
	public void getItemModel() {
		this.model.setBlockStateMapper(this, !this.getHasSubtypes(), "test/test_block");
		this.model.registerItemModel(this, "block/sub/" + "test_block", "test_block" + "." + 0);
	}
	
	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return null;
	}
}
