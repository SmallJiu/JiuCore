package cat.jiu.core.test;

import java.math.BigInteger;
import java.util.List;

import cat.jiu.core.JiuCore;
import cat.jiu.core.api.ISubBlockSerializable;
import cat.jiu.core.util.JiuUtils;
import cat.jiu.core.util.base.BaseBlock;
import cat.jiu.core.util.base.BaseBlock.BaseBlockItem;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockTest extends BaseBlock.Sub<BlockTest.TestModSubtypes> {
	public BlockTest() {
		super(JiuCore.MODID, "test_block", Material.ANVIL, SoundType.ANVIL, JiuCore.CORE, -1);
		Init.BLOCKS.add(this);
	}
	
	@Override
	protected IProperty<?>[] addBlockOthersProperty() {
//		return new IProperty[] {BlockTNT.EXPLODE, BlockLog.LOG_AXIS, BlockStone.VARIANT, BlockPlanks.VARIANT, BlockSlab.HALF, BlockLeaves.CHECK_DECAY, BlockLeaves.DECAYABLE};
		return null;
	}
	
	@Override
	protected PropertyEnum<BlockTest.TestModSubtypes> getPropertyEnum() {
		return PropertyEnum.create("level", BlockTest.TestModSubtypes.class);
	}
	
	@Override
	public ItemBlock getRegisterItemBlock() {
		return (ItemBlock) new BaseBlockItem(this, this.getHasSubtypes());
	}
	
	@Override
	public void addInformation(ItemStack stack, World world, List<String> tooltip, ITooltipFlag advanced) {
		tooltip.add(stack.getMetadata() + " : " + this.getMetaFromState(JiuUtils.item.getStateFromItemStack(stack)));
//		JiuUtils.nbt.setItemNBT(stack, "Test", BigInteger.valueOf(1));
//		JiuUtils.nbt.addItemNBT(stack, "Test", JiuUtils.nbt.getItemNBTBigInteger(stack, "Test"));
		BigInteger value = JiuUtils.nbt.getItemNBTBigInteger(stack, "Test");
//		BigInteger value = BigInteger.valueOf(Long.MAX_VALUE);
		tooltip.add(JiuUtils.big_integer.format(value, 3));
		tooltip.add("");
		tooltip.add(value.toString());
	}
	
	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		JiuCore.instance.log.info(JiuUtils.item.getTexture(JiuUtils.item.getStackFormBlockState(world.getBlockState(pos))).toString());
		return Blocks.TNT.onBlockActivated(world, pos, state, player, hand, facing, hitX, hitY, hitZ);
	}
	
	@Override
	public void getItemModel() {
		this.model.setBlockStateMapper(this, !this.getHasSubtypes(), "test/test_block");
		super.getItemModel();
	}
	
	public enum TestModSubtypes implements ISubBlockSerializable {
		LEVEL_1(0),
		LEVEL_2(1),
		LEVEL_3(2),
		LEVEL_4(3),
		LEVEL_5(4),
		LEVEL_6(5),
		LEVEL_7(6),
		LEVEL_8(7),
		LEVEL_9(8),
		LEVEL_10(9),
		LEVEL_11(10),
		LEVEL_12(11),
		LEVEL_13(12),
		LEVEL_14(13),
		LEVEL_15(14),
		LEVEL_16(15);
		
		private final int meta;
		TestModSubtypes(int meta) {
			this.meta = meta;
		}
		
		@Override
		public int getMeta() { return this.meta; }
		@Override
		public String getName() { return "state_" + this.meta; }
	}
}
