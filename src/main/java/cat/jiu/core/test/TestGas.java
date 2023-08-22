package cat.jiu.core.test;

import cat.jiu.core.JiuCore;
import cat.jiu.core.util.base.BaseBlock;
import net.minecraft.block.material.Material;
import net.minecraft.item.EnumRarity;
import net.minecraftforge.fluids.BlockFluidClassic;

public class TestGas extends BaseBlock.BaseFluid {
	private final BlockFluidClassic block;
	public TestGas() {
		super("test_gas", JiuCore.MODID);
		super.setLuminosity(15) // 发光等级
			 .setDensity(-500) // 密度，负密度表示比空气轻，也就是会向上飘
			 .setViscosity(2000) // 粘度，表示扩散速度，不可为负
			 .setGaseous(true) // 是否是空气
			 .setRarity(EnumRarity.UNCOMMON); // 稀有度
		Init.FLUIDS.add(this);
		this.block = new BaseBlock.FluidBlock(this, this.modid, this.name, JiuCore.CORE, Material.WATER, Init.BLOCKS);
	}
	
	public BlockFluidClassic getFluidBlock() {
		return this.block;
	}
}
