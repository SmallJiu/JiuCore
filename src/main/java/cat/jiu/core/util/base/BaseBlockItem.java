package cat.jiu.core.util.base;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BaseBlockItem extends ItemBlock {
	
	public BaseBlockItem(Block block, boolean hasSubtypes) {
		super(block);
		this.setHasSubtypes(hasSubtypes);
		this.setMaxDamage(0);
	}
	
	@Override
	public int getMetadata(int damage) {
		return damage;
	}
	
	@SideOnly(Side.CLIENT)
	@Override
	public String getItemStackDisplayName(ItemStack stack) {
		return super.getItemStackDisplayName(stack);
	}
}
