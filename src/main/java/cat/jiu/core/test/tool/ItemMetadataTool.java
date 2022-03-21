package cat.jiu.core.test.tool;

import java.util.List;
import java.util.Set;

import com.google.common.collect.Sets;

import cat.jiu.core.JiuCore;
import cat.jiu.core.api.IMetadataToolMaterial;
import cat.jiu.core.test.Init;
import cat.jiu.core.util.JiuUtils;
import cat.jiu.core.util.base.BaseItemTool;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.init.Items;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemSpade;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemMetadataTool extends BaseItemTool.MetaTool {
	public ItemMetadataTool() {
		super(JiuCore.MODID, "nbt_tool", JiuCore.CORE, true, ToolMaterial.DIAMOND, DamageType.values());
		super.setMaxMetadata(5);
		Init.ITEMS.add(this);
	}
	
	@Override
	public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		tooltip.add("Damage: " + this.getDamage(stack) + "/" + this.material[stack.getMetadata()].getMaxDamage());
		tooltip.add("NBT: " + JiuUtils.nbt.getItemNBT(stack).toString());
	}
	
	public enum DamageType implements IMetadataToolMaterial {
		A(1, 10, 12.1F),
		B(2, 5, 10.2F),
		C(63, 1, 1.5F),
		D(1024, 3, 4.9F),
		E(999, 999, 100.999F);
		
		private final int damage;
		private final float attackDamage;
		private final float attackSpeed;
		DamageType(int damage, float attackDamage, float attackSpeed) {
			this.damage = damage;
			this.attackDamage = attackDamage;
			this.attackSpeed = attackSpeed;
		}
		
		public int getMaxDamage() {return this.damage;}
		public float getAttackDamage() {return this.attackDamage;}
		public float getAttackSpeed() {return this.attackSpeed;}
		public ItemStack getRepairItemStack() {return new ItemStack(Items.DIAMOND);}
		public float getEfficiency() {return 2;}
		public int getEnchantability() {return 2;}
		public float getDestroySpeed(IBlockState state) {return 1;}
		public int getHarvestLevel(String toolClass, IBlockState blockState) {
			return 1;
		}
		public Set<Block> getEffectiveBlocks() {
			Set<Block> set = Sets.newHashSet();
			set.addAll(ItemPickaxe.EFFECTIVE_ON);
			set.addAll(ItemAxe.EFFECTIVE_ON);
			set.addAll(ItemSpade.EFFECTIVE_ON);
			return set;
		}
	}
}
