package cat.jiu.core.util.base;

import cat.jiu.core.api.IHasModel;
import cat.jiu.core.util.RegisterModel;

import net.minecraft.client.resources.I18n;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BaseItem {
	public static class Normal extends Item implements IHasModel {
		protected final String name;
		protected final CreativeTabs tab;
		protected final String modid;
		protected final RegisterModel model;
		
		public Normal(String modid, String name, CreativeTabs tab, boolean hasSubtypes) {
			this.name = name;
			this.tab = tab;
			this.modid = modid;
			this.model = new RegisterModel(this.modid);
			this.setHasSubtypes(hasSubtypes);
			this.setUnlocalizedName(this.modid + "." + this.name);
			this.setCreativeTab(this.tab);
			this.setRegistryName(this.modid, this.name);
			this.setNoRepair();
			RegisterModel.NeedToRegistryModel.add(this);
		}
		
		public Normal(String modid, String name, boolean hasSubtypes) {
			this(modid, name, CreativeTabs.MISC, hasSubtypes);
		}
		
		public Normal(String modid, String name, CreativeTabs tab) {
			this(modid, name, tab, false);
		}
		
		public Normal(String modid, String name) {
			this(modid, name, false);
		}
		
		protected int meta = 1;
		
		public Normal setMaxMetadata(int maxMeta) {
			if(this.getHasSubtypes()) {
				this.meta = maxMeta > 1 ? maxMeta : 1;
			}
			return this;
		}
		
		@Override
		public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items) {
			if(this.meta > 1) {
				if(this.isInCreativeTab(tab)) {
					for(int i = 0; i < this.meta; ++i) {
						items.add(new ItemStack(this, 1, i));
					}
				}
			}else {
				super.getSubItems(tab, items);
			}
			
		}
		
		@Override
		@SideOnly(Side.CLIENT)
		public void getItemModel() {
			if(this.meta > 1) {
				for(int i = 0; i < this.meta; ++i) {
					this.model.registerItemModel(this, i, "normal/" + this.name, this.name + "." + i);
				}
			}else {
				this.model.registerItemModel(this, "normal", this.name);
			}
		}
		
		@SideOnly(Side.CLIENT)
		@Override
		public String getItemStackDisplayName(ItemStack stack) {
			if(this.meta > 1) {
				return I18n.format("item." + this.modid + "." + this.name + "." + stack.getMetadata() + ".name");
			}
			return super.getItemStackDisplayName(stack);
		}
	}
	
	public static class Food extends ItemFood implements IHasModel{
		protected final String name;
		protected final CreativeTabs tab;
		protected final String modid;
		protected final RegisterModel model;
		
		public Food(String modid, String name, int amount, float saturation, boolean isWolfFood, CreativeTabs tab, boolean hasSubtypes) {
			super(amount, saturation, isWolfFood);
			this.name = name;
			this.tab = tab;
			this.modid = modid;
			this.model = new RegisterModel(this.modid);
			this.setHasSubtypes(hasSubtypes);
			this.setUnlocalizedName(this.modid + "." + this.name);
			this.setCreativeTab(this.tab);
			this.setRegistryName(this.modid, this.name);
			this.setNoRepair();
			RegisterModel.NeedToRegistryModel.add(this);
		}
		
		public Food(String modid, String name, int amount, float saturation, boolean isWolfFood, boolean hasSubtypes) {
			this(name, name, amount, saturation, isWolfFood, CreativeTabs.FOOD, hasSubtypes);
		}
		
		public Food(String modid, String name, int amount, float saturation, boolean hasSubtypes) {
			this(name, name, amount, saturation, false, hasSubtypes);
		}
		
		public Food(String modid, String name, int amount, float saturation) {
			this(name, name, amount, saturation, false);
		}
		
		@Override
		protected void onFoodEaten(ItemStack stack, World worldIn, EntityPlayer player) {
			super.onFoodEaten(stack, worldIn, player);
		}
		
		private int meta = 1;
		
		public Food setMaxMetadata(int maxMeta) {
			if(this.getHasSubtypes()) {
				this.meta = maxMeta > 1 ? maxMeta : 1;
			}
			return this;
		}
		
		@Override
		public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items) {
			if(this.meta > 1) {
				if(this.isInCreativeTab(tab)) {
					for(int i = 0; i < this.meta; ++i) {
						items.add(new ItemStack(this, 1, i));
					}
				}
			}else {
				super.getSubItems(tab, items);
			}
			
		}
		
		@Override
		@SideOnly(Side.CLIENT)
		public void getItemModel() {
			if(this.meta > 1) {
				for(int i = 0; i < this.meta; ++i) {
					this.model.registerItemModel(this, i, "foods/" + this.name, this.name + "." + i);
				}
			}else {
				this.model.registerItemModel(this, "foods", this.name);
			}
		}
		
		@SideOnly(Side.CLIENT)
		@Override
		public String getItemStackDisplayName(ItemStack stack) {
			if(this.meta > 1) {
				return I18n.format("item." + this.modid + "." + this.name + "." + stack.getMetadata() + ".name");
			}
			return super.getItemStackDisplayName(stack);
		}
	}
}
