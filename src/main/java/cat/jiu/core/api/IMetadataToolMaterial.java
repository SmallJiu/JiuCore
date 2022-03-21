package cat.jiu.core.api;

import java.util.Set;

import com.google.common.collect.Sets;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;

/**
 * Use for Enum
 * @author small_jiu
 */
public interface IMetadataToolMaterial {
	int getMaxDamage();
	float getAttackDamage();
	float getAttackSpeed();
	ItemStack getRepairItemStack();
	
	default float getDestroySpeed(IBlockState state) {return 0;}
	default int getHarvestLevel(String toolClass, IBlockState blockState) {return 0;}
	default float getEfficiency() {return 0;}
	default int getEnchantability() {return 0;}
	default Set<Block> getEffectiveBlocks() {return Sets.newHashSet();}
}
