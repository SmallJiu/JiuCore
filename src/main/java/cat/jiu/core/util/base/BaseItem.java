package cat.jiu.core.util.base;

import java.util.Set;

import com.google.common.collect.Sets;

import cat.jiu.core.api.IHasModel;
import cat.jiu.core.util.RegisterModel;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemTool;
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
	
	public static class Tool extends ItemTool implements IHasModel{
		protected final String name;
		protected final CreativeTabs tab;
		protected final String modid;
		protected final RegisterModel model;
		
		protected Tool(String modid, String name, CreativeTabs tab, boolean hasSubtypes, float attackDamageIn, float attackSpeedIn, ToolMaterial materialIn, Set<Block> effectiveBlocksIn) {
			super(attackDamageIn, attackSpeedIn, materialIn, effectiveBlocksIn);
			this.name = name;
			this.tab = tab;
			this.modid = modid;
			this.model = new RegisterModel(this.modid);
			this.setHasSubtypes(hasSubtypes);
			this.setUnlocalizedName(this.modid + "." + this.name);
			this.setCreativeTab(this.tab);
			this.setRegistryName(this.modid, this.name);
		}
		
		protected Tool(String modid, String name, CreativeTabs tab, boolean hasSubtypes, ToolMaterial materialIn, Set<Block> effectiveBlocksIn) {
			this(name, name, tab, hasSubtypes, 0.0F, 0.0F, materialIn, effectiveBlocksIn);
		}
		
		protected Tool(String modid, String name, CreativeTabs tab, ToolMaterial materialIn, Set<Block> effectiveBlocksIn) {
			this(name, name, tab, false, materialIn, effectiveBlocksIn);
		}
		
		protected Tool(String modid, String name, ToolMaterial materialIn, Set<Block> effectiveBlocksIn) {
			this(name, name, CreativeTabs.TOOLS, materialIn, effectiveBlocksIn);
		}
		
		private int meta = 1;
		
		public Tool setMaxMetadata(int maxMeta) {
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
					this.model.registerItemModel(this, i, "tools/" + this.name, this.name + "." + i);
				}
			}else {
				this.model.registerItemModel(this, "tools", this.name);
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
		
		@Override
		public boolean canHarvestBlock(IBlockState block) {
			return block != Blocks.BEDROCK.getDefaultState()
				&& block != Blocks.PORTAL.getDefaultState()
				&& block != Blocks.END_PORTAL.getDefaultState()
				&& block != Blocks.END_PORTAL_FRAME.getDefaultState();
		}
		
		@Override
		public float getDestroySpeed(ItemStack stack, IBlockState state) {
			return 10;
		}
	}
	
	public static class Sword extends ItemSword implements IHasModel {
		protected final String name;
		protected final CreativeTabs tab;
		protected final String modid;
		protected final RegisterModel model;
		
		protected Sword(String modid, String name, CreativeTabs tab, boolean hasSubtypes, ToolMaterial materialIn) {
			super(materialIn);
			this.name = name;
			this.tab = tab;
			this.modid = modid;
			this.model = new RegisterModel(this.modid);
			
			this.setHasSubtypes(hasSubtypes);
			this.setUnlocalizedName(this.modid + "." + this.name);
			this.setCreativeTab(this.tab);
			this.setRegistryName(this.modid, this.name);
		}
		
		protected Sword(String modid, String name, CreativeTabs tab, ToolMaterial materialIn) {
			this(name, name, tab, false, materialIn);
		}
		
		protected Sword(String modid, String name, ToolMaterial materialIn) {
			this(name, name, CreativeTabs.TOOLS, materialIn);
		}
		
		private int meta = 1;
		
		public Sword setMaxMetadata(int maxMeta) {
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
					this.model.registerItemModel(this, i, "tools/sword/" + this.name, this.name + "." + i);
				}
			}else {
				this.model.registerItemModel(this, "tools/sword", this.name);
			}
		}
	}
	
	public static abstract class Pickaxe extends Tool implements IHasModel {
		protected final String name;
		protected final CreativeTabs tab;
		protected final String modid;
		protected final RegisterModel model;
		
		protected Pickaxe(String modid, String name, CreativeTabs tab, boolean hasSubtypes, ToolMaterial materialIn, float attackSpeed, Set<Block> harvests) {
			super(name, name, tab, hasSubtypes, materialIn.getAttackDamage(), attackSpeed, materialIn, harvests(harvests));
			this.name = name;
			this.tab = tab;
			this.modid = modid;
			this.model = new RegisterModel(this.modid);
			this.setHasSubtypes(hasSubtypes);
			this.setUnlocalizedName(this.modid + "." + this.name);
			this.setCreativeTab(this.tab);
			this.setRegistryName(this.modid, this.name);
		}
		
		private static Set<Block> harvests(Set<Block> harvests) {
			Set<Block> EFFECTIVE_ON = Sets.newHashSet(Blocks.ACTIVATOR_RAIL, Blocks.COAL_ORE, Blocks.COBBLESTONE, Blocks.DETECTOR_RAIL, Blocks.DIAMOND_BLOCK, Blocks.DIAMOND_ORE, Blocks.DOUBLE_STONE_SLAB, Blocks.GOLDEN_RAIL, Blocks.GOLD_BLOCK, Blocks.GOLD_ORE, Blocks.ICE, Blocks.IRON_BLOCK, Blocks.IRON_ORE, Blocks.LAPIS_BLOCK, Blocks.LAPIS_ORE, Blocks.LIT_REDSTONE_ORE, Blocks.MOSSY_COBBLESTONE, Blocks.NETHERRACK, Blocks.PACKED_ICE, Blocks.RAIL, Blocks.REDSTONE_ORE, Blocks.SANDSTONE, Blocks.RED_SANDSTONE, Blocks.STONE, Blocks.STONE_SLAB, Blocks.STONE_BUTTON, Blocks.STONE_PRESSURE_PLATE);
			
			for(Block b : harvests) {
				EFFECTIVE_ON.add(b);
			}
			
			return EFFECTIVE_ON;
		}
		
		protected Pickaxe(String modid, String name, CreativeTabs tab, ToolMaterial materialIn, float speed, Set<Block> breakBlock) {
			this(name, name, tab, false, materialIn, speed, breakBlock);
		}
		
		protected Pickaxe(String modid, String name, ToolMaterial materialIn, Set<Block> breakBlock) {
			this(name, name, CreativeTabs.TOOLS, materialIn, -2.8F, breakBlock);
		}
		
		private int meta = 1;
		
		public Pickaxe setMaxMetadata(int maxMeta) {
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
					this.model.registerItemModel(this, i, "tools/pickaxe/" + this.name, this.name + "." + i);
				}
			}else {
				this.model.registerItemModel(this, "tools/pickaxe", this.name);
			}
		}
		
		@Override
		public abstract boolean canHarvestBlock(IBlockState state, ItemStack stack);
		
		@Override
		public abstract float getDestroySpeed(ItemStack stack, IBlockState state);
	}
	
	public static abstract class Axe extends Tool implements IHasModel {
		protected final String name;
		protected final CreativeTabs tab;
		protected final String modid;
		protected final RegisterModel model;
		
		protected Axe(String modid, String name, CreativeTabs tab, boolean hasSubtypes, ToolMaterial materialIn, float attackSpeed, Set<Block> harvests) {
			super(name, name, tab, hasSubtypes, materialIn.getAttackDamage(), attackSpeed, materialIn, harvests(harvests));
			this.name = name;
			this.tab = tab;
			this.modid = modid;
			this.model = new RegisterModel(this.modid);
			this.setHasSubtypes(hasSubtypes);
			this.setUnlocalizedName(this.modid + "." + this.name);
			this.setCreativeTab(this.tab);
			this.setRegistryName(this.modid, this.name);
		}
		
		private static Set<Block> harvests(Set<Block> harvests) {
			Set<Block> EFFECTIVE_ON = Sets.newHashSet(Blocks.PLANKS, Blocks.BOOKSHELF, Blocks.LOG, Blocks.LOG2, Blocks.CHEST, Blocks.PUMPKIN, Blocks.LIT_PUMPKIN, Blocks.MELON_BLOCK, Blocks.LADDER, Blocks.WOODEN_BUTTON, Blocks.WOODEN_PRESSURE_PLATE);
		    
			for(Block b : harvests) {
				EFFECTIVE_ON.add(b);
			}
			
			return EFFECTIVE_ON;
		}
		
		protected Axe(String modid, String name, CreativeTabs tab, ToolMaterial materialIn, float speed, Set<Block> breakBlock) {
			this(name, name, tab, false, materialIn, speed, breakBlock);
		}
		
		protected Axe(String modid, String name, ToolMaterial materialIn, Set<Block> breakBlock) {
			this(name, name, CreativeTabs.TOOLS, materialIn, -2.8F, breakBlock);
		}
		
		private int meta = 1;
		
		public Axe setMaxMetadata(int maxMeta) {
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
					this.model.registerItemModel(this, i, "tools/axe/" + this.name + "/", this.name + "." + i);
				}
			}else {
				this.model.registerItemModel(this, "tools/axe/", this.name);
			}
		}
		
		@Override
		public abstract boolean canHarvestBlock(IBlockState state, ItemStack stack);
		
		@Override
		public abstract float getDestroySpeed(ItemStack stack, IBlockState state);
	}

	public static abstract class Shovel extends Tool implements IHasModel {
		protected final String name;
		protected final CreativeTabs tab;
		protected final String modid;
		protected final RegisterModel model;
		
		protected Shovel(String modid, String name, CreativeTabs tab, boolean hasSubtypes, ToolMaterial materialIn, float attackSpeed, Set<Block> harvests) {
			super(name, name, tab, hasSubtypes, materialIn.getAttackDamage(), attackSpeed, materialIn, harvests(harvests));
			this.name = name;
			this.tab = tab;
			this.modid = modid;
			this.model = new RegisterModel(this.modid);
			this.setHasSubtypes(hasSubtypes);
			this.setUnlocalizedName(this.modid + "." + this.name);
			this.setCreativeTab(this.tab);
			this.setRegistryName(this.modid, this.name);
		}
		
		private static Set<Block> harvests(Set<Block> harvests) {
			Set<Block> EFFECTIVE_ON = Sets.newHashSet(Blocks.CLAY, Blocks.DIRT, Blocks.FARMLAND, Blocks.GRASS, Blocks.GRAVEL, Blocks.MYCELIUM, Blocks.SAND, Blocks.SNOW, Blocks.SNOW_LAYER, Blocks.SOUL_SAND, Blocks.GRASS_PATH, Blocks.CONCRETE_POWDER);

			for(Block b : harvests) {
				EFFECTIVE_ON.add(b);
			}
			
			return EFFECTIVE_ON;
		}
		
		protected Shovel(String modid, String name, CreativeTabs tab, ToolMaterial materialIn, float speed, Set<Block> breakBlock) {
			this(name, name, tab, false, materialIn, speed, breakBlock);
		}
		
		protected Shovel(String modid, String name, ToolMaterial materialIn, Set<Block> breakBlock) {
			this(name, name, CreativeTabs.TOOLS, materialIn, -2.8F, breakBlock);
		}
		
		private int meta = 1;
		
		public Shovel setMaxMetadata(int maxMeta) {
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
					this.model.registerItemModel(this, i, "tools/shovel/" + this.name + "/", this.name + "." + i);
				}
			}else {
				this.model.registerItemModel(this, "tools/shovel/", this.name);
			}
		}
		
		@Override
		public abstract boolean canHarvestBlock(IBlockState state, ItemStack stack);
		
		@Override
		public abstract float getDestroySpeed(ItemStack stack, IBlockState state);
	}

}
