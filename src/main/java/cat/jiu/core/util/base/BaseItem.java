package cat.jiu.core.util.base;

import java.util.List;

import com.google.common.collect.Lists;

import cat.jiu.core.api.IHasModel;
import cat.jiu.core.types.StackCaches;
import cat.jiu.core.util.RegisterModel;

import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
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
			RegisterModel.addNeedRegistryModel(modid, this);
			ForgeRegistries.ITEMS.register(this);
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
		
		List<String> I18nInfo = Lists.newArrayList();

		public Normal addI18nInfo(String... keys) {
			for(String key : keys) {
				this.I18nInfo.add(key);
			}
			return this;
		}

		@SideOnly(Side.CLIENT)
		public void addInformation(ItemStack stack, World world, List<String> tooltip, ITooltipFlag flagIn) {
			if(!I18nInfo.isEmpty()) {
				for(String key : I18nInfo) {
					tooltip.add(I18n.format(key));
				}
			}
		}
		private StackCaches caches;
		public final StackCaches getStackCaches() {return this.caches;}
		protected int meta = 1;
		public Normal setMaxMetadata(int maxMeta) {
			if(this.getHasSubtypes()) {
				this.meta = maxMeta > 1 ? maxMeta : 1;
				this.caches = new StackCaches(this, meta);
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
		
		String[] modelRes = null;
		public Normal setModelResourceLocation(String dir, String fileName) {
			this.modelRes = new String[] {dir, fileName.equals("this.name") ? this.name : fileName};
			return this;
		}
		
		@Override
		@SideOnly(Side.CLIENT)
		public void getItemModel() {
			if(this.meta > 1) {
				for(int i = 0; i < this.meta; ++i) {
					if(this.modelRes != null) {
						this.model.registerItemModel(this, i, this.modelRes[0], this.modelRes[1]);
					}else {
						this.model.registerItemModel(this, i, "normal/" + this.name, this.name);
					}
				}
			}else {
				if(this.modelRes != null) {
					this.model.registerItemModel(this, this.modelRes[0], this.modelRes[1]);
				}else {
					this.model.registerItemModel(this, "normal", this.name);
				}
				
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
			RegisterModel.addNeedRegistryModel(modid, this);
			ForgeRegistries.ITEMS.register(this);
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
		
		List<String> I18nInfo = Lists.newArrayList();

		public Food addI18nInfo(String... keys) {
			for(String key : keys) {
				this.I18nInfo.add(key);
			}
			return this;
		}

		@SideOnly(Side.CLIENT)
		public void addInformation(ItemStack stack, World world, List<String> tooltip, ITooltipFlag flagIn) {
			if(!I18nInfo.isEmpty()) {
				for(String key : I18nInfo) {
					tooltip.add(I18n.format(key));
				}
			}
		}
		
		private StackCaches caches;
		public final StackCaches getStackCaches() {return this.caches;}
		protected int meta = 1;
		public Food setMaxMetadata(int maxMeta) {
			if(this.getHasSubtypes()) {
				this.meta = maxMeta > 1 ? maxMeta : 1;
				this.caches = new StackCaches(this, meta);
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
