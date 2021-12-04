package cat.jiu.core.test;

import java.util.List;

import cat.jiu.core.JiuCore;
import cat.jiu.core.api.ISubBlockSerializable;
import cat.jiu.core.util.JiuUtils;
import cat.jiu.core.util.base.BaseBlock;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class BlockTest extends BaseBlock.Sub<BlockTest.TestModSubtypes> {
	public BlockTest() {
		super(JiuCore.MODID, "test_block", Material.ANVIL, SoundType.ANVIL, JiuCore.CORE, -1);
		Init.BLOCKS.add(this);
	}
	
	@Override
	public void addInformation(ItemStack stack, World world, List<String> tooltip, ITooltipFlag advanced) {
		tooltip.add(stack.getMetadata() + " : " + this.getMetaFromState(JiuUtils.item.getStateFromItemStack(stack)));
	}
	
	@Override
	protected PropertyEnum<TestModSubtypes> getPropertyEnum() {
		return PropertyEnum.create("level", TestModSubtypes.class);
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
