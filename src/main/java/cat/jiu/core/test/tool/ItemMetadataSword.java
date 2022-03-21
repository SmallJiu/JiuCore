package cat.jiu.core.test.tool;

import cat.jiu.core.JiuCore;
import cat.jiu.core.api.IMetadataToolMaterial;
import cat.jiu.core.test.Init;
import cat.jiu.core.util.base.BaseItemTool;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

public class ItemMetadataSword extends BaseItemTool.MetaSword {
	public ItemMetadataSword() {
		super(JiuCore.MODID, "nbt_sword", JiuCore.CORE, true, ToolMaterial.DIAMOND, Type.values());
		super.setMaxMetadata(3);
		Init.ITEMS.add(this);
	}
	public enum Type implements IMetadataToolMaterial {
		A(32, 10, 4),
		B(16, 5, 2),
		C(64, 15, 16);
		
		private final int maxDamage;
		private final float attackDamage;
		private final float attackSpeed;
		
		private Type(int maxDamage, float attackDamage, float attackSpeed) {
			this.maxDamage = maxDamage;
			this.attackDamage = attackDamage;
			this.attackSpeed = attackSpeed;
		}
		public int getMaxDamage() {return this.maxDamage;}
		public float getAttackDamage() {return this.attackDamage;}
		public float getAttackSpeed() {return this.attackSpeed;}
		public ItemStack getRepairItemStack() {return new ItemStack(Items.DIAMOND);}
	}
}
