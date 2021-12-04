package cat.jiu.core.util.helpers;

import java.util.ArrayList;
import java.util.List;

import cat.jiu.core.JiuCore;
import cat.jiu.core.util.JiuUtils;
import cat.jiu.core.util.ModSubtypes;
import cat.jiu.core.util.base.BaseBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fluids.BlockFluidBase;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.oredict.OreDictionary;

public final class ItemUtils {

	public void itemInit(Item item, String modid, String name, CreativeTabs tab, boolean hasSubtypes, List<Item> ITEMS) {
		item.setHasSubtypes(hasSubtypes);
		item.setUnlocalizedName(modid + "." + name);
		item.setCreativeTab(tab);
		ForgeRegistries.ITEMS.register(item.setRegistryName(modid, name));
		ITEMS.add(item);
	}
	
	public void blockInit(Block block, String modid, String name, CreativeTabs tab, float hardness, boolean hasSubType, List<Block> BLOCKS) {
		block.setUnlocalizedName(modid + "." + name);
		block.setCreativeTab(tab);
		if(hardness < 0) {
			block.setHardness(Float.MAX_VALUE);
		}else {
			block.setHardness(hardness);
		}
		BLOCKS.add(block);
		ForgeRegistries.BLOCKS.register(block.setRegistryName(modid, name));
		ForgeRegistries.ITEMS.register(new BaseBlock.BaseBlockItem(block, hasSubType).setRegistryName(modid, name));
	}
	
	public void removeItem(ItemStack stack) {
		stack.setCount(0);
	}
	
	public void removeItem(EntityItem stack) {
		stack.getItem().setCount(0);
		stack.setDead();
	}
	
	/**
	 * {@link Block#spawnAsEntity(World, BlockPos, ItemStack)}
	 */
	public void spawnAsEntity(World worldIn, BlockPos pos, ItemStack stack) {
		/*
		if (!worldIn.isRemote && !stack.isEmpty() && worldIn.getGameRules().getBoolean("doTileDrops")&& !worldIn.restoringBlockSnapshots) {
			double x = (double)(worldIn.rand.nextFloat() * 0.5F) + 0.25D;
            double y = (double)(worldIn.rand.nextFloat() * 0.5F) + 0.25D;
            double z = (double)(worldIn.rand.nextFloat() * 0.5F) + 0.25D;
			EntityItem eitem = new EntityItem(worldIn, (pos.getX() + x), (pos.getY() + y), (pos.getZ() + z), stack.copy());
			eitem.setDefaultPickupDelay();
			worldIn.spawnEntity(eitem);
		}
		*/
		Block.spawnAsEntity(worldIn, pos, stack.copy());
	}
	
	public void spawnAsEntity(World worldIn, BlockPos pos, ItemStack[] stack) {
		spawnAsEntity(worldIn, pos, stack, false);
	}
	
	public void spawnAsEntity(World worldIn, BlockPos pos, ItemStack[] stacks, boolean clearList) {
		for(ItemStack stack : stacks) {
			this.spawnAsEntity(worldIn, pos, stack);
		}
		if(clearList) {
			stacks = new ItemStack[0];
		}
	}
	
	public void spawnAsEntity(World worldIn, BlockPos pos, List<ItemStack> stack) {
		spawnAsEntity(worldIn, pos, stack, false);
	}
	
	public void spawnAsEntity(World worldIn, BlockPos pos, List<ItemStack> stacks, boolean clearList) {
		for(ItemStack stack : stacks) {
			this.spawnAsEntity(worldIn, pos, stack);
		}
		if(clearList) {
			stacks.clear();
		}
	}
	
	public void spawnAsEntity(World worldIn, BlockPos pos, IItemHandler handler) {
		for(int slot = 0; slot < handler.getSlots(); slot++) {
			this.spawnAsEntity(worldIn, pos, handler.getStackInSlot(slot));
		}
	}
	
	public void addItemToSlot(IItemHandlerModifiable slots, ItemStack stack) {
		for (int slot = 0; slot < slots.getSlots(); ++slot) {
			ItemStack putStack = slots.getStackInSlot(slot);
			if (JiuUtils.item.equalsStack(putStack, stack)) {
				putStack.grow(stack.getCount());
				slots.setStackInSlot(slot, putStack);
				break;
			}else if(putStack == null || putStack.isEmpty()){
				slots.setStackInSlot(slot, stack);
				break;
			}
		}
	}
	
	/**
	 * {@link Item#getByNameOrId(String)}
	 */
	public Item getItemByNameOrId(String name) {
		Item item = Item.getByNameOrId(name);
		
		return item;
	}
	
	/**
	 * {@link Block#getBlockFromName(String)}
	 */
	public Block getBlockFromName(String name) {
		Block block = Block.getBlockFromName(name);
		
		return block;
	}
	
	public boolean isBlock(ItemStack stack) {
		if(stack == null) {
			return false;
		}
		return stack.getItem() instanceof ItemBlock;
	}
	
	public Block getBlockFromItemStack(ItemStack stack) {
		return isBlock(stack) ? ((ItemBlock)stack.getItem()).getBlock() : null;
	}
	
	@SuppressWarnings("deprecation")
	public IBlockState getStateFromItemStack(ItemStack stack) {
		return isBlock(stack) ? getBlockFromItemStack(stack).getStateFromMeta(stack.getMetadata()) : null;
	}
	
	public ItemStack getStackFormBlockState(IBlockState state) {
		return this.getStackFormBlockState(state, 1);
	}
	
	public ItemStack getStackFormBlockState(IBlockState state, int amout) {
		return new ItemStack(state.getBlock(), amout, state.getBlock().getMetaFromState(state));
	}
	
	public int getMetaFormBlockState(IBlockState state) {
		return state.getBlock().getMetaFromState(state);
	}
	
	public void fixedItem(ItemStack stack) {
		this.fixedItem(stack, 1);
	}
	
	public void fixedItem(ItemStack stack, int damage) {
		stack.setItemDamage(stack.getItemDamage() - damage);
	}
	
	/**
	 * default Keep 'copy stack' as 'original stack' is
	 * 
	 * 
	 * @param stack original stack
	 * @return stack copy
	 * 
	 * @author small_jiu
	 */
	public ItemStack copyStack(ItemStack stack) {
		return this.copyStack(stack, stack.getCount(), false);
	}
	
	/**
	 * default change meta
	 * 
	 * @param stack original stack
	 * @param meta Copied the meta
	 * @return stack copy
	 * 
	 * @author small_jiu
	 */
	public ItemStack copyStack(ItemStack stack, int meta) {
		return this.copyStack(stack, meta, true);
	}
	
	/**
	 * 
	 * @param stack original stack
	 * @param i meta or amout
	 * @param changeMeta 'i' is meta?
	 * @return stack copy
	 * 
	 * @author small_jiu
	 */
	public ItemStack copyStack(ItemStack stack, int i, boolean changeMeta) {
		if(changeMeta) {
			return new ItemStack(stack.getItem(), stack.getCount(), i);
		}else {
			return new ItemStack(stack.getItem(), i, stack.getMetadata());
		}
	}
	
	/**
	 * {@link #getStackFromString(String, String)}
	 */
	public ItemStack getStackFromString(String name) {
		return this.getStackFromString(name, "1");
	}
	
	/**
	 * {@link #getStackFromString(String, String, String)}
	 */
	public ItemStack getStackFromString(String name, String amout) {
		return this.getStackFromString(name, amout, "0");
	}
	
	public ItemStack getStackFromString(String name, String amout, String meta) {
		try {
			Item jItem = this.getItemByNameOrId(name);
			int jAmout = new Integer(amout);
			int jMeta = new Integer(meta);
			boolean isBlock = jItem instanceof ItemBlock;
			
			if(isBlock) {
				if(!(jMeta > 15)) {
					return new ItemStack(jItem, jAmout, jMeta);
				}else {
					JiuCore.instance.log.fatal("\"" + name +  "\": "+ "\"" + jMeta + "\"" + " It's too large! It must be >=15");
					return new ItemStack(jItem, jAmout, 15);
				}
			}else {
				return new ItemStack(jItem, jAmout, jMeta);
			}
		} catch (Exception e) {
			JiuCore.instance.log.fatal(e.getMessage() + " is not Number!");
			return null;
		}
	}
	
	@SuppressWarnings("unused")
	public boolean checkConfigStack(String[] configs, String cname, boolean isBlock, int cmeta, int metaIndex) {
		boolean lag = false;
		for(int i = 1; i < configs.length; ++i) {
			String[] config = JiuUtils.other.custemSplitString(configs[i], "|");
			
			String oname = config[0];
			try {
				int ometa = new Integer(config[metaIndex]);
				
				if(cname.equals(oname)) {
					if(isBlock) {
						if(!(ometa > 15)) {
							if(cmeta == ometa) {
								return true;
							}else {
								return false;
							}
						}else {
							JiuCore.instance.log.fatal("\"" + oname +  "\": "+ "\"" + ometa + "\"" + " It's too large! It must be >=15");
							return false;
						}
					}else {
						if(cmeta == ometa) {
							return true;
						}else {
							return false;
						}
					}
				}else {
					JiuCore.instance.log.fatal("\"" + oname +  "\": "+ "\"" + oname + "\"" + " is not belong to MCS's Block!");
					return false;
				}
			} catch (Exception e) {
				JiuCore.instance.log.fatal(oname + ": " + config[metaIndex] +  " is not Number!");
				return false;
			}
		}
		
		return lag;
	}
	
	/**
	 * {@link #equalsStack(ItemStack, ItemStack, boolean)}
	 */
	public boolean equalsStack(ItemStack stackA, ItemStack stackB) {
		return this.equalsStack(stackA, stackB, false);
	}
	
	/**
	 * {@link #equalsStack(ItemStack, ItemStack, boolean, boolean)}
	 */
	public boolean equalsStack(ItemStack stackA, ItemStack stackB, boolean checkAmout) {
		return this.equalsStack(stackA, stackB, true, checkAmout);
	}
	
	/**
	 * 
	 * @param stackA original stack
	 * @param stackB need to equals stack
	 * @param checkDamage check damage and meta
	 * @param checkAmout check amout
	 * @return if stackA = stackB, return 'true', else return 'false'.
	 * 
	 * @author small_jiu
	 */
	public boolean equalsStack(ItemStack stackA, ItemStack stackB, boolean checkDamage, boolean checkAmout) {
		if(stackA == null || stackB == null) {
			return false;
		}
		if(stackA.isEmpty() || stackB.isEmpty()) {
			return false;
		}
		if(stackA == stackB) {
			return true;
		}else {
			if(stackA.getItem().equals(stackB.getItem())) {
				if(checkAmout) {
					if(stackA.getCount() == stackB.getCount()) {
						if(checkDamage) {
							if(stackA.getItemDamage() == stackB.getItemDamage()) {
								return true;
							}else {
								return false;
							}
						}else {
							return true;
						}
					}else {
						return false;
					}
				}else {
					if(checkDamage) {
						if(stackA.getItemDamage() == stackB.getItemDamage()) {
							return true;
						}else {
							return false;
						}
					}else {
						return true;
					}
				}
			}else {
				return false;
			}
		}
	}
	
	public EntityEquipmentSlot getArmorSlotForID(int id) {
		if(id > 4) {
			return null;
		}
		EntityEquipmentSlot[] armorSlots = new EntityEquipmentSlot[] {EntityEquipmentSlot.FEET, EntityEquipmentSlot.LEGS, EntityEquipmentSlot.CHEST, EntityEquipmentSlot.HEAD};
		
		for(EntityEquipmentSlot slot : armorSlots) {
			if(slot.getIndex() == id && slot.getSlotIndex() == (id + 1)) {
				return slot;
			}
		}
		return null;
	}
	
	/**
	 * 
	 * @param stateA original state
	 * @param stateB need to equals state
	 * @return if stateA = stateB, return 'true', else return 'false'.
	 * 
	 * @author small_jiu
	 */
	public boolean equalsState(IBlockState stateA, IBlockState stateB) {
		if(stateA == null || stateB == null) {
			return false;
		}
		if(stateA == stateB) {
			return true;
		}else {
			if(stateA.getBlock().equals(stateB.getBlock())) {
				if(stateA.getBlock().getMetaFromState(stateA) == stateB.getBlock().getMetaFromState(stateB)) {
					return true;
				}else {
					return false;
				}
			}else {
				return false;
			}
		}
	}
	
	/**
	 * 
	 * @param state the block state
	 * @return if state is Fluid, return 'true', else return 'false'.
	 * 
	 * @author small_jiu
	 */
	public boolean isFluid(IBlockState state) {
		if(state == null) {
			return false;
		}
		return state.getBlock() instanceof BlockFluidBase
			|| state.getBlock() instanceof BlockLiquid;
	}
	
	/**
	 * 
	 * @param stack the stack
	 * @return stack ore list
	 * 
	 * @author small_jiu
	 */
	public List<String> getOreDict(ItemStack stack) {
		List<String> names = new ArrayList<>();
		int[] ids = OreDictionary.getOreIDs(stack);

		for (int id : ids) {
			names.add(OreDictionary.getOreName(id));
		}
		
		return names;
	}
	
	/**
	 * Unfinsh!<p>
	 * getTexture form ItemStack
	 * 
	 * @author small_jiu
	 */
	@SuppressWarnings({ "deprecation", "unused" })
	public ResourceLocation getTexture(ItemStack stack) {
		if(isBlock(stack)) {
			Block block = getBlockFromItemStack(stack);
			IBlockState state = block.getStateFromMeta(stack.getMetadata());
		}else if(stack.getItem() instanceof Item) {
			Item item = stack.getItem();
		}
		return null;
	}
	
	/**
	 * Mod 'MultipleCompressedStuffs' use this method
	 * 
	 * @param oreDictName
	 * @param itemIn
	 * @param isHas
	 * 
	 * @author small_jiu
	 */
	public void registerCompressedOre(String oreDictName, Block itemIn, boolean isHas) {
		registerCompressedOre(oreDictName, Item.getItemFromBlock(itemIn), isHas);
	}
	
	/**
	 * Mod 'MultipleCompressedStuffs' use this method
	 * 
	 * @param oreDictName
	 * @param itemIn
	 * @param isHas
	 * 
	 * @author small_jiu
	 */
	public void registerCompressedOre(String oreDictName, Item itemIn, boolean isHas) {
		for(ModSubtypes type : ModSubtypes.values()){
			int meta = type.getMeta();
			
			if(isHas){
				if(meta == 0) {
					registerOre("block" + oreDictName, itemIn, 0);
				}else {
					registerOre("compressed" + meta + "x" + oreDictName, itemIn, meta);
				}
			}else {
				registerOre("compressed" + (meta + 1) + "x" + oreDictName, itemIn, meta);
			}
		}
	}
	
	public void registerCompressedOre(String oreDictName, Block blockIn, String materialName) {
		this.registerCompressedOre(oreDictName, Item.getItemFromBlock(blockIn), materialName);
	}
	
	/**
	 * Mod 'MultipleCompressedStuffs' use this method
	 * 
	 * @param oreDictName
	 * @param itemIn
	 * @param materialName
	 * 
	 * @author small_jiu
	 */
	public void registerCompressedOre(String oreDictName, Item itemIn, String materialName) {
		for(ModSubtypes type : ModSubtypes.values()){
			int meta = type.getMeta();
			
			registerOre("compressed" + (meta + 1) + "x" + materialName + oreDictName, itemIn, meta);
		}
	}
	
	/**
	 * meta default use 0
	 * 
	 * @param oreDict the ore
	 * @param blockIn the block
	 * 
	 * @author small_jiu
	 */
	public void registerOre(String oreDict, Block blockIn) {
		registerOre(oreDict, Item.getItemFromBlock(blockIn), 0);
	}
	
	/**
	 * meta default use 0
	 * 
	 * @param oreDict the ore
	 * @param itemIn the item
	 * 
	 * @author small_jiu
	 */
	public void registerOre(String oreDict, Item itemIn) {
		registerOre(oreDict, itemIn, 0);
	}
	
	/**
	 * 
	 * @param oreDict the ore name
	 * @param blockIn the block
	 * @param meta the block meta
	 * 
	 * @author small_jiu
	 */
	public void registerOre(String oreDict, Block blockIn, int meta) {
		registerOre(oreDict, Item.getItemFromBlock(blockIn), meta);
	}
	
	/**
	 * 
	 * @param oreDict the ore name
	 * @param itemIn the item
	 * @param meta the item meta
	 * 
	 * @author small_jiu
	 */
	public void registerOre(String oreDict, Item itemIn, int meta) {
		OreDictionary.registerOre(oreDict, new ItemStack(itemIn, 1, meta));
	}
}
