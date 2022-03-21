package cat.jiu.core.test.tool;

import java.util.Set;

import com.google.common.collect.Sets;

import cat.jiu.core.JiuCore;
import cat.jiu.core.api.IMetadataToolMaterial;
import cat.jiu.core.test.Init;
import cat.jiu.core.util.base.BaseItemTool;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

public class ItemMetadataShovel extends BaseItemTool.MetaShovel {
	public ItemMetadataShovel() {
		super(JiuCore.MODID, "nbt_shovel", JiuCore.CORE, true, ToolMaterial.DIAMOND, Type.values());
		super.setMaxMetadata(3);
		Init.ITEMS.add(this);
	}

	@Override
	public boolean canHarvestBlock(IBlockState state, ItemStack stack) {
		return super.canHarvestBlock(state, stack);
	}

	public enum Type implements IMetadataToolMaterial {
		A(32, 10, 4, 7, 4), B(16, 5, 2, 3.5F, 3), C(64, 15, 16, 14, 5);

		private final int maxDamage;
		private final float attackDamage;
		private final float attackSpeed;
		private final float destroySpeed;
		private final int level;

		private Type(int maxDamage, float attackDamage, float attackSpeed, float destroySpeed, int level) {
			this.maxDamage = maxDamage;
			this.attackDamage = attackDamage;
			this.attackSpeed = attackSpeed;
			this.destroySpeed = destroySpeed;
			this.level = level;
		}

		public int getMaxDamage() {
			return this.maxDamage;
		}

		public float getAttackDamage() {
			return this.attackDamage;
		}

		public float getAttackSpeed() {
			return this.attackSpeed;
		}

		public float getDestroySpeed(IBlockState state) {
			return this.destroySpeed;
		}

		public Set<Block> getEffectiveBlocks() {
			return Sets.newHashSet(Blocks.BEDROCK);
		}

		public int getHarvestLevel(String toolClass, IBlockState blockState) {
			return this.level;
		}
		public ItemStack getRepairItemStack() {return new ItemStack(Items.DIAMOND);}
	}
}
