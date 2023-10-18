package cat.jiu.core.util.base;

import java.util.Set;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;

import cat.jiu.core.api.IHasModel;
import cat.jiu.core.api.IMetadataToolMaterial;
import cat.jiu.core.util.JiuUtils;
import cat.jiu.core.util.RegisterModel;

import net.minecraft.block.Block;
import net.minecraft.block.BlockDirt;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemHoe;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemSpade;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemTool;
import net.minecraft.stats.StatList;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreDictionary;

public class BaseItemTool {
	public static class Tool extends ItemTool implements IHasModel {
		protected final String name;
		protected final CreativeTabs tab;
		protected final String modid;
		@Deprecated
		protected final RegisterModel model;

		protected Tool(String modid, String name, CreativeTabs tab, boolean hasSubtypes, float attackDamageIn, float attackSpeedIn, ToolMaterial materialIn, Set<Block> effectiveBlocksIn) {
			super(attackDamageIn, attackSpeedIn, materialIn, effectiveBlocksIn);
			this.name = name;
			this.tab = tab;
			this.modid = modid;
			this.model = new RegisterModel(this.modid);
			this.setHasSubtypes(hasSubtypes);
			this.setTranslationKey(this.modid + "." + this.name);
			this.setCreativeTab(this.tab);
			this.setRegistryName(this.modid, this.name);
			RegisterModel.addNeedRegistryModel(modid, this);
			ForgeRegistries.ITEMS.register(this);
		}

		protected Tool(String modid, String name, CreativeTabs tab, boolean hasSubtypes, ToolMaterial materialIn, Set<Block> effectiveBlocksIn) {
			this(modid, name, tab, hasSubtypes, 0.0F, 0.0F, materialIn, effectiveBlocksIn);
		}

		protected Tool(String modid, String name, CreativeTabs tab, ToolMaterial materialIn, Set<Block> effectiveBlocksIn) {
			this(modid, name, tab, false, materialIn, effectiveBlocksIn);
		}

		protected Tool(String modid, String name, ToolMaterial materialIn, Set<Block> effectiveBlocksIn) {
			this(modid, name, CreativeTabs.TOOLS, materialIn, effectiveBlocksIn);
		}

		protected int maxMeta = 1;

		public Tool setMaxMetadata(int maxMeta) {
			if(this.getHasSubtypes()) {
				this.maxMeta = maxMeta > 1 ? maxMeta : 1;
			}
			return this;
		}

		@Override
		public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items) {
			if(this.maxMeta > 1) {
				if(this.isInCreativeTab(tab)) {
					for(int i = 0; i < this.maxMeta; ++i) {
						items.add(new ItemStack(this, 1, i));
					}
				}
			}else {
				super.getSubItems(tab, items);
			}
		}

		@Override
		@SideOnly(Side.CLIENT)
		public void getItemModel(RegisterModel util) {
			if(this.maxMeta > 1) {
				for(int i = 0; i < this.maxMeta; ++i) {
					util.registerItemModel(this, i, "tools/" + this.name, this.name + "." + i);
				}
			}else {
				util.registerItemModel(this, "tools", this.name);
			}
		}

		@SideOnly(Side.CLIENT)
		@Override
		public String getItemStackDisplayName(ItemStack stack) {
			if(this.maxMeta > 1) {
				return I18n.format("item." + this.modid + "." + this.name + "." + stack.getMetadata() + ".name");
			}
			return super.getItemStackDisplayName(stack);
		}

		@Override
		public boolean canHarvestBlock(IBlockState block) {
			return block != Blocks.BEDROCK.getDefaultState() && block != Blocks.PORTAL.getDefaultState() && block != Blocks.END_PORTAL.getDefaultState() && block != Blocks.END_PORTAL_FRAME.getDefaultState();
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
		@Deprecated
		protected final RegisterModel model;

		protected Sword(String modid, String name, CreativeTabs tab, boolean hasSubtypes, ToolMaterial materialIn) {
			super(materialIn);
			this.name = name;
			this.tab = tab;
			this.modid = modid;
			this.model = new RegisterModel(this.modid);

			this.setHasSubtypes(hasSubtypes);
			this.setTranslationKey(this.modid + "." + this.name);
			this.setCreativeTab(this.tab);
			this.setRegistryName(this.modid, this.name);
			RegisterModel.addNeedRegistryModel(modid, this);
			ForgeRegistries.ITEMS.register(this);
		}

		protected Sword(String modid, String name, CreativeTabs tab, ToolMaterial materialIn) {
			this(modid, name, tab, false, materialIn);
		}

		protected Sword(String modid, String name, ToolMaterial materialIn) {
			this(modid, name, CreativeTabs.TOOLS, materialIn);
		}

		protected int maxMeta = 1;

		public Sword setMaxMetadata(int maxMeta) {
			if(this.getHasSubtypes()) {
				this.maxMeta = maxMeta > 1 ? maxMeta : 1;
			}
			return this;
		}

		@Override
		public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items) {
			if(this.maxMeta > 1) {
				if(this.isInCreativeTab(tab)) {
					for(int i = 0; i < this.maxMeta; ++i) {
						items.add(new ItemStack(this, 1, i));
					}
				}
			}else {
				super.getSubItems(tab, items);
			}
		}

		@Override
		@SideOnly(Side.CLIENT)
		public void getItemModel(RegisterModel util) {
			if(this.maxMeta > 1) {
				for(int i = 0; i < this.maxMeta; ++i) {
					util.registerItemModel(this, i, "tools/sword/" + this.name, this.name + "." + i);
				}
			}else {
				util.registerItemModel(this, "tools/sword", this.name);
			}
		}
	}

	public static abstract class Pickaxe extends Tool {
		protected Pickaxe(String modid, String name, CreativeTabs tab, boolean hasSubtypes, ToolMaterial materialIn, float attackSpeed, Set<Block> harvests) {
			super(modid, name, tab, hasSubtypes, materialIn.getAttackDamage(), attackSpeed, materialIn, harvests(harvests));
		}

		private static Set<Block> harvests(Set<Block> harvests) {
			Set<Block> EFFECTIVE_ON = Sets.newHashSet(Blocks.ACTIVATOR_RAIL, Blocks.COAL_ORE, Blocks.COBBLESTONE, Blocks.DETECTOR_RAIL, Blocks.DIAMOND_BLOCK, Blocks.DIAMOND_ORE, Blocks.DOUBLE_STONE_SLAB, Blocks.GOLDEN_RAIL, Blocks.GOLD_BLOCK, Blocks.GOLD_ORE, Blocks.ICE, Blocks.IRON_BLOCK, Blocks.IRON_ORE, Blocks.LAPIS_BLOCK, Blocks.LAPIS_ORE, Blocks.LIT_REDSTONE_ORE, Blocks.MOSSY_COBBLESTONE, Blocks.NETHERRACK, Blocks.PACKED_ICE, Blocks.RAIL, Blocks.REDSTONE_ORE, Blocks.SANDSTONE, Blocks.RED_SANDSTONE, Blocks.STONE, Blocks.STONE_SLAB, Blocks.STONE_BUTTON, Blocks.STONE_PRESSURE_PLATE);

			for(Block b : harvests) {
				EFFECTIVE_ON.add(b);
			}

			return EFFECTIVE_ON;
		}

		protected Pickaxe(String modid, String name, CreativeTabs tab, ToolMaterial materialIn, float speed, Set<Block> breakBlock) {
			this(modid, name, tab, false, materialIn, speed, breakBlock);
		}

		protected Pickaxe(String modid, String name, ToolMaterial materialIn, Set<Block> breakBlock) {
			this(modid, name, CreativeTabs.TOOLS, materialIn, -2.8F, breakBlock);
		}

		@Override
		@SideOnly(Side.CLIENT)
		public void getItemModel(RegisterModel util) {
			if(this.maxMeta > 1) {
				for(int i = 0; i < this.maxMeta; ++i) {
					util.registerItemModel(this, i, "tools/pickaxe/" + this.name, this.name + "." + i);
				}
			}else {
				util.registerItemModel(this, "tools/pickaxe", this.name);
			}
		}

		@Override
		public abstract boolean canHarvestBlock(IBlockState state, ItemStack stack);

		@Override
		public abstract float getDestroySpeed(ItemStack stack, IBlockState state);
	}

	public static abstract class Axe extends Tool {
		protected Axe(String modid, String name, CreativeTabs tab, boolean hasSubtypes, ToolMaterial materialIn, float attackSpeed, Set<Block> harvests) {
			super(modid, name, tab, hasSubtypes, materialIn.getAttackDamage(), attackSpeed, materialIn, harvests(harvests));
		}

		private static Set<Block> harvests(Set<Block> harvests) {
			Set<Block> EFFECTIVE_ON = Sets.newHashSet(Blocks.PLANKS, Blocks.BOOKSHELF, Blocks.LOG, Blocks.LOG2, Blocks.CHEST, Blocks.PUMPKIN, Blocks.LIT_PUMPKIN, Blocks.MELON_BLOCK, Blocks.LADDER, Blocks.WOODEN_BUTTON, Blocks.WOODEN_PRESSURE_PLATE);

			for(Block b : harvests) {
				EFFECTIVE_ON.add(b);
			}

			return EFFECTIVE_ON;
		}

		protected Axe(String modid, String name, CreativeTabs tab, ToolMaterial materialIn, float speed, Set<Block> breakBlock) {
			this(modid, name, tab, false, materialIn, speed, breakBlock);
		}

		protected Axe(String modid, String name, ToolMaterial materialIn, Set<Block> breakBlock) {
			this(modid, name, CreativeTabs.TOOLS, materialIn, -2.8F, breakBlock);
		}

		@Override
		@SideOnly(Side.CLIENT)
		public void getItemModel(RegisterModel util) {
			if(this.maxMeta > 1) {
				for(int i = 0; i < this.maxMeta; ++i) {
					util.registerItemModel(this, i, "tools/axe/" + this.name + "/", this.name + "." + i);
				}
			}else {
				util.registerItemModel(this, "tools/axe/", this.name);
			}
		}

		@Override
		public abstract boolean canHarvestBlock(IBlockState state, ItemStack stack);

		@Override
		public abstract float getDestroySpeed(ItemStack stack, IBlockState state);
	}

	public static abstract class Shovel extends Tool {
		protected Shovel(String modid, String name, CreativeTabs tab, boolean hasSubtypes, ToolMaterial materialIn, float attackSpeed, Set<Block> harvests) {
			super(modid, name, tab, hasSubtypes, materialIn.getAttackDamage(), attackSpeed, materialIn, harvests(harvests));
		}

		private static Set<Block> harvests(Set<Block> harvests) {
			Set<Block> EFFECTIVE_ON = Sets.newHashSet(Blocks.CLAY, Blocks.DIRT, Blocks.FARMLAND, Blocks.GRASS, Blocks.GRAVEL, Blocks.MYCELIUM, Blocks.SAND, Blocks.SNOW, Blocks.SNOW_LAYER, Blocks.SOUL_SAND, Blocks.GRASS_PATH, Blocks.CONCRETE_POWDER);

			for(Block b : harvests) {
				EFFECTIVE_ON.add(b);
			}

			return EFFECTIVE_ON;
		}

		protected Shovel(String modid, String name, CreativeTabs tab, ToolMaterial materialIn, float speed, Set<Block> breakBlock) {
			this(modid, name, tab, false, materialIn, speed, breakBlock);
		}

		protected Shovel(String modid, String name, ToolMaterial materialIn, Set<Block> breakBlock) {
			this(modid, name, CreativeTabs.TOOLS, materialIn, -2.8F, breakBlock);
		}

		@Override
		@SideOnly(Side.CLIENT)
		public void getItemModel(RegisterModel util) {
			if(this.maxMeta > 1) {
				for(int i = 0; i < this.maxMeta; ++i) {
					util.registerItemModel(this, i, "tools/shovel/" + this.name + "/", this.name + "." + i);
				}
			}else {
				util.registerItemModel(this, "tools/shovel/", this.name);
			}
		}

		@Override
		public abstract boolean canHarvestBlock(IBlockState state, ItemStack stack);

		@Override
		public abstract float getDestroySpeed(ItemStack stack, IBlockState state);
	}

	public static class MetaTool extends ItemTool implements IHasModel {
		protected final String name;
		protected final CreativeTabs tab;
		protected final String modid;
		@Deprecated
		protected final RegisterModel model;
		protected final IMetadataToolMaterial[] material;
		protected String toolClass;

		public MetaTool(String modid, String name, CreativeTabs tab, boolean hasSubtypes, ToolMaterial mainMaterial, IMetadataToolMaterial[] material) {
			super(material[0].getAttackDamage(), material[0].getAttackSpeed(), mainMaterial, material[0].getEffectiveBlocks());
			this.name = name;
			this.tab = tab;
			this.modid = modid;
			this.material = material;
			this.model = new RegisterModel(this.modid);
			this.setMaxMetadata(material.length);
			this.setHasSubtypes(hasSubtypes);
			this.setTranslationKey(this.modid + "." + this.name);
			this.setCreativeTab(this.tab);
			this.setRegistryName(this.modid, this.name);

			if(this instanceof MetaPickaxe) {
				this.toolClass = "pickaxe";
				this.effectiveBlocks.addAll(ItemPickaxe.EFFECTIVE_ON);
			}else if(this instanceof MetaAxe) {
				this.toolClass = "axe";
				this.effectiveBlocks.addAll(ItemAxe.EFFECTIVE_ON);
			}else if(this instanceof MetaShovel) {
				this.toolClass = "shovel";
				this.effectiveBlocks.addAll(ItemSpade.EFFECTIVE_ON);
			}
			RegisterModel.addNeedRegistryModel(modid, this);
			ForgeRegistries.ITEMS.register(this);
		}

		public MetaTool(String modid, String name, CreativeTabs tab, ToolMaterial mainMaterial, IMetadataToolMaterial[] material) {
			this(modid, name, tab, false, mainMaterial, material);
		}

		public MetaTool(String modid, String name, ToolMaterial mainMaterial, IMetadataToolMaterial[] material) {
			this(modid, name, CreativeTabs.TOOLS, mainMaterial, material);
		}

		protected int maxMeta = 1;

		public MetaTool setMaxMetadata(int maxMeta) {
			if(this.getHasSubtypes()) {
				this.maxMeta = maxMeta > 1 ? maxMeta : 1;
			}
			return this;
		}

		@Override
		public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items) {
			if(this.maxMeta > 1) {
				if(this.isInCreativeTab(tab)) {
					for(int i = 0; i < this.maxMeta; ++i) {
						items.add(new ItemStack(this, 1, i));
					}
				}
			}else {
				super.getSubItems(tab, items);
			}
		}

		@Override
		@SideOnly(Side.CLIENT)
		public void getItemModel(RegisterModel util) {
			if(this.maxMeta > 1) {
				for(int i = 0; i < this.maxMeta; ++i) {
					util.registerItemModel(this, i, "tools/" + this.name, this.name + "." + i);
				}
			}else {
				util.registerItemModel(this, "tools", this.name);
			}
		}

		@SideOnly(Side.CLIENT)
		@Override
		public String getItemStackDisplayName(ItemStack stack) {
			if(this.maxMeta > 1) {
				return I18n.format("item." + this.modid + "." + this.name + "." + stack.getMetadata() + ".name");
			}
			return super.getItemStackDisplayName(stack);
		}

		@Override
		public boolean canHarvestBlock(IBlockState state, ItemStack stack) {
			return this.effectiveBlocks.contains(state.getBlock());
		}

		@Override
		public float getDestroySpeed(ItemStack stack, IBlockState state) {
			return this.effectiveBlocks.contains(state.getBlock()) ? this.material[stack.getMetadata()].getDestroySpeed(state) : super.getDestroySpeed(stack, state);
		}

		@Override
		public int getHarvestLevel(ItemStack stack, String toolClass, EntityPlayer player, IBlockState blockState) {
			int level = this.getMaterial(stack).getHarvestLevel(toolClass, blockState);
			
			if(level < 0 && toolClass.equals(this.toolClass)) {
				level = super.getHarvestLevel(stack, toolClass, player, blockState);
			}
			if(!this.canHarvestBlock(blockState, stack)) {
				level = -1;
			}
			return level;
		}

		public int getHarvestLevel(ItemStack stack, String toolClass, IBlockState blockState) {
			int level = this.getMaterial(stack).getHarvestLevel(toolClass, blockState);
			if(!this.canHarvestBlock(blockState, stack)) {
				level = -1;
			}
			return level;
		}

		@Override
		public Multimap<String, AttributeModifier> getAttributeModifiers(EntityEquipmentSlot slot, ItemStack stack) {
			Multimap<String, AttributeModifier> multimap = HashMultimap.create();
			if(slot == EntityEquipmentSlot.MAINHAND) {
				multimap.put(SharedMonsterAttributes.ATTACK_DAMAGE.getName(), new AttributeModifier(ATTACK_DAMAGE_MODIFIER, "Tool modifier", (double) this.material[stack.getMetadata()].getAttackDamage(), 0));
				multimap.put(SharedMonsterAttributes.ATTACK_SPEED.getName(), new AttributeModifier(ATTACK_SPEED_MODIFIER, "Tool modifier", (double) this.material[stack.getMetadata()].getAttackSpeed(), 0));
			}
			return multimap;
		}

		@Override
		public boolean onBlockDestroyed(ItemStack stack, World worldIn, IBlockState state, BlockPos pos, EntityLivingBase entityLiving) {
			if(!worldIn.isRemote && (double) state.getBlockHardness(worldIn, pos) != 0.0D) {
				this.damageItem(stack, 1, entityLiving);
			}
			return true;
		}

		@Override
		public boolean hitEntity(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker) {
			if(attacker instanceof EntityPlayer) {
				if(!((EntityPlayer) attacker).isCreative()) {
					this.damageItem(stack, 2, attacker);
				}
			}
			return true;
		}

		@Override
		public Set<String> getToolClasses(ItemStack stack) {
			return this.toolClass != null ? ImmutableSet.of(this.toolClass) : super.getToolClasses(stack);
		}

		@Override
		public boolean getIsRepairable(ItemStack toRepair, ItemStack repair) {
			ItemStack mat = this.getMaterial(repair).getRepairItemStack();
			if(!mat.isEmpty() && OreDictionary.itemMatches(mat, repair, false))
				return true;
			return super.getIsRepairable(toRepair, repair);
		}
		
		@Override
		public int getDamage(ItemStack stack) {
			if(JiuUtils.nbt.hasNBT(stack, "Damage")) {
				return JiuUtils.nbt.getItemNBTInt(stack, "Damage");
			}
			return 0;
		}

		@Override
		public int getMaxDamage(ItemStack stack) {
			return this.getMaterial(stack).getMaxDamage();
		}

		@Override
		public boolean isDamaged(ItemStack stack) {
			return JiuUtils.nbt.getItemNBTInt(stack, "Damage") > 0;
		}

		@Override
		public void setDamage(ItemStack stack, int damage) {
			JiuUtils.nbt.setItemNBT(stack, "Damage", damage);

			if(JiuUtils.nbt.getItemNBTInt(stack, "Damage") < 0) {
				JiuUtils.nbt.setItemNBT(stack, "Damage", 0);
			}
		}
		
		public void damageItem(ItemStack stack, int amount, EntityLivingBase entity) {
			if(JiuUtils.nbt.hasNBT(stack, "Damage")) {
				JiuUtils.nbt.addItemNBT(stack, "Damage", amount);
			}else {
				JiuUtils.nbt.setItemNBT(stack, "Damage", amount);
			}

			if(JiuUtils.nbt.getItemNBTInt(stack, "Damage") > this.getMaxDamage(stack)) {
				stack.shrink(1);
				if(entity != null) {
					entity.renderBrokenItemStack(stack);
					if(entity instanceof EntityPlayer) {
						((EntityPlayer) entity).addStat(StatList.getObjectBreakStats(stack.getItem()));
					}
				}
				
			}
		}

		@Override
		public boolean canDestroyBlockInCreative(World world, BlockPos pos, ItemStack stack, EntityPlayer player) {
			return !(this instanceof MetaSword);
		}

		@Override
		public int getItemEnchantability(ItemStack stack) {
			return this.getMaterial(stack).getEnchantability();
		}
		
		public IMetadataToolMaterial getMaterial(ItemStack stack) {
			if(stack.getMetadata() >= this.material.length) {
				return this.material[this.material.length-1];
			}
			if(stack.getMetadata() < 0) {
				return this.material[0];
			}
			return this.material[stack.getMetadata()];
		}
	}

	public static class MetaSword extends MetaTool {
		public MetaSword(String modid, String name, CreativeTabs tab, boolean hasSubtypes, ToolMaterial mainMaterial, IMetadataToolMaterial[] materialIn) {
			super(modid, name, tab, hasSubtypes, mainMaterial, materialIn);
		}

		public MetaSword(String modid, String name, CreativeTabs tab, ToolMaterial mainMaterial, IMetadataToolMaterial[] materialIn) {
			this(modid, name, tab, false, mainMaterial, materialIn);
		}

		public MetaSword(String modid, String name, ToolMaterial mainMaterial, IMetadataToolMaterial[] materialIn) {
			this(modid, name, CreativeTabs.TOOLS, mainMaterial, materialIn);
		}

		@Override
		public boolean canHarvestBlock(IBlockState state, ItemStack stack) {
			return Items.DIAMOND_SWORD.canHarvestBlock(state);
		}

		@Override
		public float getDestroySpeed(ItemStack stack, IBlockState state) {
			return Items.DIAMOND_SWORD.getDestroySpeed(stack, state);
		}

		@Override
		public boolean onBlockDestroyed(ItemStack stack, World worldIn, IBlockState state, BlockPos pos, EntityLivingBase entityLiving) {
			if((double) state.getBlockHardness(worldIn, pos) != 0.0D) {
				super.damageItem(stack, 2, entityLiving);
			}
			return true;
		}

		@Override
		public boolean hitEntity(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker) {
			if(attacker instanceof EntityPlayer) {
				if(!((EntityPlayer) attacker).isCreative()) {
					super.damageItem(stack, 1, attacker);
				}
			}
			return true;
		}

		@Override
		public Multimap<String, AttributeModifier> getAttributeModifiers(EntityEquipmentSlot slot, ItemStack stack) {
			Multimap<String, AttributeModifier> multimap = HashMultimap.create();
			if(slot == EntityEquipmentSlot.MAINHAND) {
				multimap.put(SharedMonsterAttributes.ATTACK_DAMAGE.getName(), new AttributeModifier(ATTACK_DAMAGE_MODIFIER, "Weapon modifier", (double) this.material[stack.getMetadata()].getAttackDamage(), 0));
				multimap.put(SharedMonsterAttributes.ATTACK_SPEED.getName(), new AttributeModifier(ATTACK_SPEED_MODIFIER, "Weapon modifier", (double) this.material[stack.getMetadata()].getAttackSpeed(), 0));
			}
			return multimap;
		}

		@Override
		@SideOnly(Side.CLIENT)
		public void getItemModel(RegisterModel util) {
			if(this.maxMeta > 1) {
				for(int i = 0; i < this.maxMeta; ++i) {
					util.registerItemModel(this, i, "tools/sword/" + this.name, this.name + "." + i);
				}
			}else {
				util.registerItemModel(this, "tools/sword", this.name);
			}
		}
	}

	public static class MetaPickaxe extends MetaTool {
		public MetaPickaxe(String modid, String name, CreativeTabs tab, boolean hasSubtypes, ToolMaterial mainMaterial, IMetadataToolMaterial[] material) {
			super(modid, name, tab, hasSubtypes, mainMaterial, material);
		}

		public MetaPickaxe(String modid, String name, CreativeTabs tab, ToolMaterial mainMaterial, IMetadataToolMaterial[] material) {
			this(modid, name, tab, false, mainMaterial, material);
		}

		public MetaPickaxe(String modid, String name, ToolMaterial mainMaterial, IMetadataToolMaterial[] material) {
			this(modid, name, CreativeTabs.TOOLS, mainMaterial, material);
		}

		@Override
		public boolean canHarvestBlock(IBlockState state, ItemStack stack) {
			Block block = state.getBlock();
			int level = this.material[stack.getMetadata()].getHarvestLevel(toolClass, state);

			if(block == Blocks.OBSIDIAN) {
				return level >= 3;
			}else if(block != Blocks.DIAMOND_BLOCK && block != Blocks.DIAMOND_ORE) {
				if(block != Blocks.EMERALD_ORE && block != Blocks.EMERALD_BLOCK) {
					if(block != Blocks.GOLD_BLOCK && block != Blocks.GOLD_ORE) {
						if(block != Blocks.IRON_BLOCK && block != Blocks.IRON_ORE) {
							if(block != Blocks.LAPIS_BLOCK && block != Blocks.LAPIS_ORE) {
								if(block != Blocks.REDSTONE_ORE && block != Blocks.LIT_REDSTONE_ORE) {
									Material material = state.getMaterial();

									if(material == Material.ROCK) {
										return true;
									}else if(material == Material.IRON) {
										return true;
									}else {
										return material == Material.ANVIL;
									}
								}else {
									return level >= 2;
								}
							}else {
								return level >= 1;
							}
						}else {
							return level >= 1;
						}
					}else {
						return level >= 2;
					}
				}else {
					return level >= 2;
				}
			}else {
				return level >= 2;
			}
		}

		@Override
		@SideOnly(Side.CLIENT)
		public void getItemModel(RegisterModel util) {
			if(this.maxMeta > 1) {
				for(int meta = 0; meta < this.maxMeta; ++meta) {
					util.registerItemModel(this, meta, "tools/pickaxe/" + this.name, this.name + "." + meta);
				}
			}else {
				util.registerItemModel(this, "tools/pickaxe", this.name);
			}
		}
	}

	public static class MetaAxe extends MetaTool {
		public MetaAxe(String modid, String name, CreativeTabs tab, boolean hasSubtypes, ToolMaterial mainMaterial, IMetadataToolMaterial[] material) {
			super(modid, name, tab, hasSubtypes, mainMaterial, material);
		}

		public MetaAxe(String modid, String name, CreativeTabs tab, ToolMaterial mainMaterial, IMetadataToolMaterial[] material) {
			this(modid, name, tab, false, mainMaterial, material);
		}

		public MetaAxe(String modid, String name, ToolMaterial mainMaterial, IMetadataToolMaterial[] material) {
			this(modid, name, CreativeTabs.TOOLS, mainMaterial, material);
		}

		@Override
		public boolean canHarvestBlock(IBlockState state, ItemStack stack) {
			return Items.DIAMOND_AXE.canHarvestBlock(state, stack);
		}

		@Override
		@SideOnly(Side.CLIENT)
		public void getItemModel(RegisterModel util) {
			if(this.maxMeta > 1) {
				for(int i = 0; i < this.maxMeta; ++i) {
					util.registerItemModel(this, i, "tools/axe/" + this.name, this.name + "." + i);
				}
			}else {
				util.registerItemModel(this, "tools/axe/", this.name);
			}
		}
	}

	public static class MetaShovel extends MetaTool {
		public MetaShovel(String modid, String name, CreativeTabs tab, boolean hasSubtypes, ToolMaterial mainMaterial, IMetadataToolMaterial[] material) {
			super(modid, name, tab, hasSubtypes, mainMaterial, material);
		}

		public MetaShovel(String modid, String name, CreativeTabs tab, ToolMaterial mainMaterial, IMetadataToolMaterial[] material) {
			this(modid, name, tab, false, mainMaterial, material);
		}

		public MetaShovel(String modid, String name, ToolMaterial mainMaterial, IMetadataToolMaterial[] material) {
			this(modid, name, CreativeTabs.TOOLS, mainMaterial, material);
		}

		@Override
		@SideOnly(Side.CLIENT)
		public void getItemModel(RegisterModel util) {
			if(this.maxMeta > 1) {
				for(int i = 0; i < this.maxMeta; ++i) {
					util.registerItemModel(this, i, "tools/shovel/" + this.name + "/", this.name + "." + i);
				}
			}else {
				util.registerItemModel(this, "tools/shovel/", this.name);
			}
		}

		@Override
		public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
			return Items.DIAMOND_SHOVEL.onItemUse(player, worldIn, pos, hand, facing, hitX, hitY, hitZ);
		}

		@Override
		public boolean canHarvestBlock(IBlockState state, ItemStack stack) {
			return Items.DIAMOND_SHOVEL.canHarvestBlock(state, stack);
		}
	}

	public static class MetaHoe extends MetaTool {
		public MetaHoe(String modid, String name, CreativeTabs tab, boolean hasSubtypes, ToolMaterial mainMaterial, IMetadataToolMaterial[] material) {
			super(modid, name, tab, hasSubtypes, mainMaterial, material);
		}

		public MetaHoe(String modid, String name, CreativeTabs tab, ToolMaterial mainMaterial, IMetadataToolMaterial[] material) {
			this(modid, name, tab, false, mainMaterial, material);
		}

		public MetaHoe(String modid, String name, ToolMaterial mainMaterial, IMetadataToolMaterial[] material) {
			this(modid, name, CreativeTabs.TOOLS, mainMaterial, material);
		}
		
		/**
		 * change the damage item method<p>
		 * {@link ItemHoe#onItemUse(EntityPlayer, World, BlockPos, EnumHand, EnumFacing, float, float, float)}
		 */
		@SuppressWarnings("incomplete-switch")
		public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
			
			ItemStack itemstack = player.getHeldItem(hand);

			if(!player.canPlayerEdit(pos.offset(facing), facing, itemstack)) {
				return EnumActionResult.FAIL;
			}else {
				int hook = ForgeEventFactory.onHoeUse(itemstack, player, worldIn, pos);
				if(hook != 0)
					return hook > 0 ? EnumActionResult.SUCCESS : EnumActionResult.FAIL;

				IBlockState iblockstate = worldIn.getBlockState(pos);
				Block block = iblockstate.getBlock();

				if(facing != EnumFacing.DOWN && worldIn.isAirBlock(pos.up())) {
					if(block == Blocks.GRASS || block == Blocks.GRASS_PATH) {
						this.setBlock(itemstack, player, worldIn, pos, Blocks.FARMLAND.getDefaultState());
						return EnumActionResult.SUCCESS;
					}

					if(block == Blocks.DIRT) {
						switch((BlockDirt.DirtType) iblockstate.getValue(BlockDirt.VARIANT)) {
							case DIRT:
								this.setBlock(itemstack, player, worldIn, pos, Blocks.FARMLAND.getDefaultState());
								return EnumActionResult.SUCCESS;
							case COARSE_DIRT:
								this.setBlock(itemstack, player, worldIn, pos, Blocks.DIRT.getDefaultState().withProperty(BlockDirt.VARIANT, BlockDirt.DirtType.DIRT));
								return EnumActionResult.SUCCESS;
						}
					}
				}

				return EnumActionResult.PASS;
			}
		}

		protected void setBlock(ItemStack stack, EntityPlayer player, World worldIn, BlockPos pos, IBlockState state) {
			worldIn.playSound(player, pos, SoundEvents.ITEM_HOE_TILL, SoundCategory.BLOCKS, 1.0F, 1.0F);

			if(!worldIn.isRemote) {
				worldIn.setBlockState(pos, state, 11);
				super.damageItem(stack, 1, player);
			}
		}

		@Override
		public boolean hitEntity(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker) {
			if(attacker instanceof EntityPlayer) {
				if(!((EntityPlayer) attacker).isCreative()) {
					this.damageItem(stack, 1, attacker);
				}
			}
			return true;
		}

		@Override
		@SideOnly(Side.CLIENT)
		public void getItemModel(RegisterModel util) {
			if(this.maxMeta > 1) {
				for(int i = 0; i < this.maxMeta; ++i) {
					util.registerItemModel(this, i, "tools/hoe/" + this.name + "/", this.name + "." + i);
				}
			}else {
				util.registerItemModel(this, "tools/hoe/", this.name);
			}
		}
	}
}
