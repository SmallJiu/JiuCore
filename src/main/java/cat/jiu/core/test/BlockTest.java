package cat.jiu.core.test;

import java.util.List;

import cat.jiu.core.JiuCore;
import cat.jiu.core.api.ISubBlockSerializable;
import cat.jiu.core.util.JiuUtils;
import cat.jiu.core.util.base.BaseBlock;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class BlockTest extends BaseBlock.Sub {
	private static final PropertyEnum<TestModSubtypes> VARIANT = PropertyEnum.create("level", TestModSubtypes.class);
	public BlockTest() {
		super(JiuCore.MODID, "test_block", Material.ANVIL, SoundType.ANVIL, JiuCore.CORE, -1);
		this.setDefaultState(this.blockState.getBaseState().withProperty(VARIANT, VARIANT.getValueClass().getEnumConstants()[0]));
		Init.BLOCKS.add(this);
	}
	
	@Override
	public void addInformation(ItemStack stack, World world, List<String> tooltip, ITooltipFlag advanced) {
		tooltip.add(stack.getMetadata() + " : " + this.getMetaFromState(JiuUtils.item.getStateFromItemStack(stack)));
	}
	
	@Override
	public int getMetaFromState(IBlockState state) {
		return state.getValue(VARIANT).getMeta();
	}
	
	@Override
	public IBlockState getStateFromMeta(int meta) {
		return this.getDefaultState().withProperty(VARIANT, VARIANT.getValueClass().getEnumConstants()[meta]);
	}
	
	@Override
	public String getName(ItemStack stack) {
		return VARIANT.getValueClass().getEnumConstants()[stack.getMetadata()].getName();
	}
	
	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, new IProperty[] { VARIANT });
	}
	
	@Override
	public void getItemModel() {
		for(int i = 0; i < VARIANT.getValueClass().getEnumConstants().length; ++i) {
			this.model.registerItemModel(this, i, "block/normal/" + this.name, this.name + "." + i);
		}
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
		public String getName() { return "state_" + meta; }
	}
}
