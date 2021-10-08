package cat.jiu.core.util;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import cat.jiu.core.JiuCore;
import cat.jiu.core.util.base.BaseBlockItem;
import cat.jiu.core.util.crafting.Recipes;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.state.IBlockState;
import net.minecraft.command.ICommandSender;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.MobEffects;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.fluids.BlockFluidBase;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.oredict.OreDictionary;

public final class JiuUtils {
	
	public static final ItemUtils item = new ItemUtils();
	public static final EntityUtils entity = new EntityUtils();
	public static final OtherUtils other = new OtherUtils();
	public static final Recipes recipe = new Recipes(JiuCore.MODID);

	public static class ItemUtils{
		
		public void itemInit(Item item, String name, CreativeTabs tab, boolean hasSubtypes, List<Item> ITEMS) {
			item.setHasSubtypes(hasSubtypes);
			item.setUnlocalizedName("mcs." + name);
			item.setCreativeTab(tab);
			ForgeRegistries.ITEMS.register(item.setRegistryName(name));
			ITEMS.add(item);
		}
		
		public void blockInit(Block block, String name, CreativeTabs tab, float hardness, boolean hasSubType, List<Block> BLOCKS) {
			block.setUnlocalizedName("mcs." + name);
			block.setCreativeTab(tab);
			if(hardness < 0) {
				block.setHardness(Float.MAX_VALUE);
			}else {
				block.setHardness(hardness);
			}
			BLOCKS.add(block);
			ForgeRegistries.BLOCKS.register(block.setRegistryName(name));
			ForgeRegistries.ITEMS.register(new BaseBlockItem(block, hasSubType).setRegistryName(name));
		}
		
		public void removeItem(ItemStack stack) {
			stack.setCount(0);
		}
		
		public void removeItem(EntityItem stack) {
			stack.setDead();
		}
		
		/**
		 * {@link Block#spawnAsEntity(World, BlockPos, ItemStack)}
		 */
		public void spawnAsEntity(World worldIn, BlockPos pos, ItemStack stack) {
			if (!worldIn.isRemote && !stack.isEmpty() && worldIn.getGameRules().getBoolean("doTileDrops")&& !worldIn.restoringBlockSnapshots) {
				double x = (double)(worldIn.rand.nextFloat() * 0.5F) + 0.25D;
	            double y = (double)(worldIn.rand.nextFloat() * 0.5F) + 0.25D;
	            double z = (double)(worldIn.rand.nextFloat() * 0.5F) + 0.25D;
				EntityItem eitem = new EntityItem(worldIn, (pos.getX() + x), (pos.getY() + y), (pos.getZ() + z), stack);
				eitem.setDefaultPickupDelay();
				worldIn.spawnEntity(eitem);
			}
			
//			Block.spawnAsEntity(worldIn, pos, stack);
		}
		
		public void spawnAsEntity(World worldIn, BlockPos pos, ItemStack[] stack, boolean clearList) {
			for(int i = 0; i < stack.length; ++i) {
				this.spawnAsEntity(worldIn, pos, stack[i]);
				if(i == stack.length) {
					if(clearList) {
						for(int j = 0; j < stack.length; ++j) {
							stack[j] = null;
						}
						break;
					}
				}
			}
		}
		
		public void spawnAsEntity(World worldIn, BlockPos pos, List<ItemStack> stack, boolean clearList) {
			for(int i = 0; i < stack.size(); ++i) {
				this.spawnAsEntity(worldIn, pos, stack.get(i));
				if(i == stack.size()) {
					if(clearList) {
						stack.clear();
						break;
					}
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
			return isBlock(stack) ? ((ItemBlock)stack.getItem()).getBlock() : Blocks.AIR;
		}
		
		@SuppressWarnings("deprecation")
		public IBlockState getStateFromItemStack(ItemStack stack) {
			return isBlock(stack) ? getBlockFromItemStack(stack).getStateFromMeta(stack.getMetadata()) : Blocks.AIR.getDefaultState();
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
			this.fixedItem(stack, 1);;
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
		
		public ItemStack getStackFromString(String name) {
			return this.getStackFromString(name, "1");
		}
		
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
				String[] config = other.custemSplitString(configs[i], "|");
				
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
		 * default don't check amout
		 * 
		 * @param stackA original stack
		 * @param stackB need to equals stack
		 * @return if stackA = stackB, return 'true', else return 'false'.
		 * 
		 * @author small_jiu
		 */
		public boolean equalsStack(ItemStack stackA, ItemStack stackB) {
			return this.equalsStack(stackA, stackB, false);
		}
		
		/**
		 * 
		 * @param stackA original stack
		 * @param stackB need to equals stack
		 * @param checkAmout equals amout?
		 * @return if stackA = stackB, return 'true', else return 'false'.
		 * 
		 * @author small_jiu
		 */
		public boolean equalsStack(ItemStack stackA, ItemStack stackB, boolean checkAmout) {
			if(stackA == null || stackB == null) {
				return false;
			}
			if(stackA == stackB) {
				return true;
			}else {
				if(stackA.getItem().equals(stackB.getItem())) {
					if(checkAmout) {
						if(stackA.getCount() == stackB.getCount()) {
							if(stackA.getMetadata() == stackB.getMetadata()) {
								return true;
							}else {
								return false;
							}
						}else {
							return false;
						}
					}else {
						if(stackA.getMetadata() == stackB.getMetadata()) {
							return true;
						}else {
							return false;
						}
					}
				}else {
					return false;
				}
			}
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
		 * Unfinsh!
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
	
	public static class EntityUtils{
		// 是否是指定玩家名
		// 是则返回true，不是则返回false
		/**
		 * 
		 * @param name the player name
		 * @param player player
		 * @return if 'player' name is 'name', return 'true', else return 'false'
		 * 
		 * @author small_jiu
		 */
		public static boolean isPlayer(String name, EntityPlayer player) {
			return player.getName().equals(name);
		}
		
		// 模糊匹配玩家名
		// 含有则返回true，不含有则返回false
		/**
		 * 
		 * @param name the player name
		 * @param player player
		 * @return if 'player' name has 'name', return 'true', else return 'false'
		 * 
		 * @author small_jiu
		 */
		public static boolean isVaguePlayer(String name, EntityPlayer player) {
			return player.getName().indexOf(name) != -1;
		}
		
		/**
		 * 
		 * @param world entity world
		 * @param pos entity pos
		 * @return if is in fluid, return 'true', else return 'false'.
		 * 
		 * @author small_jiu
		 */
		public boolean isEntityInFluid(World world, BlockPos pos) {
			return world.getBlockState(pos).getBlock() instanceof BlockFluidBase
				|| world.getBlockState(pos).getBlock() instanceof BlockLiquid;
		}
		
		/**
		 * 
		 * @param sender Player, or any Entity
		 * @param key Message
		 * 
		 * @author small_jiu
		 */
		public void sendMessage(ICommandSender sender, String key) {
			sender.sendMessage(new TextComponentTranslation(key, 4)); 
		}
		
		/*
		looks like the last one, but this use Generic<T>
		public <T extends ICommandSender> void sendMessage(T sender, String key) {
			sender.sendMessage(new TextComponentTranslation(key, 4));
		}
		*/
		
		/**
		 * 
		 * @param world The World!
		 * @param key Message
		 * 
		 * @author small_jiu
		 */
		public void sendMessageToAllPlayer(World world, String key) {
			for (int i = 0; i < world.playerEntities.size(); ++i) {
				EntityPlayer player = world.playerEntities.get(i);

				player.sendMessage(new TextComponentTranslation(key, 4));
			}
		}
		
		/**
		 * 
		 * @param entity the entity
		 * @param potion {@link MobEffects}
		 * @param potionTime measure potionTime by the second
		 * @param potionLevel potion level, muse be >= 255
		 * 
		 * @author small_jiu
		 */
		public void addPotionEffect(EntityLivingBase entity, Potion potion, int potionTime, int potionLevel){
			entity.addPotionEffect(new PotionEffect(potion, potionTime * 20, potionLevel));
		}
	}
	
	public static class OtherUtils{
		
		/**
		 * 
		 * @param date original array
		 * @param da original array
		 * @param start 
		 * @return original array copy
		 * @throws ArrayIndexOutOfBoundsException Use the length of the 0th array to build the array.If some array length less than this length, will throw.
		 *
		 * @author small_jiu
		 */
		public <T> T[][] one2two(T[][] date, T[] da, int start) throws ArrayIndexOutOfBoundsException {
			int k = start;
			
			T[][] date0 = date.clone();
			
			for(int i =0; i < date.length; ++i) {
				for(int j =0; j < date[0].length; ++j) {
					date0[i][j] = da[k];
					k++;
				}
			}
			
			return date0;
		}
		
		/**
		 * 
		 * @param strs original list
		 * @param str contain key
		 * @return if is the key, return 'true', else return 'false'.
		 * 
		 * @author small_jiu
		 */
		public <T> boolean containKey(T[] strs, T str) {
			for(T stri : strs) {
				if(stri.equals(str)) {
					return true;
				}
			}
			return false;
		}
		
		/**
		 * 
		 * @param strs original list
		 * @param str contain key
		 * @return if is the key, return 'true', else return 'false'.
		 * 
		 * @author small_jiu
		 */
		public boolean containKey(String[] strs, String str) {
			for(String stri : strs) {
				if(stri.equals(str)) {
					return true;
				}
			}
			return false;
		}
		
		/**
		 * 
		 * @param strs original list
		 * @param str contain key
		 * @return if is the key, return 'true', else return 'false'.
		 * 
		 * @author small_jiu
		 */
		public boolean containKey(List<String> strs, String str) {
			for(String stri : strs) {
				if(stri.equals(str)) {
					return true;
				}
			}
			return false;
		}
		
		/**
		 * 
		 * @param args original list
		 * @return copy
		 * @throws ArrayIndexOutOfBoundsException Use the length of the 0th array to build the array.If some array length less than this length, will throw.
		 * 
		 * @author small_jiu
		 */
		public String[][] more2two(String[]... args) throws ArrayIndexOutOfBoundsException {
			String[][] arg = new String[args.length][args[0].length];
			int hang = arg.length;
			
			for(int i = 0; i < hang; ++i) {
				for(int j = 0; j < arg[i].length; ++j) {
					arg[i][j] = args[i][j];
				}
			}
			return arg;
		}
		
		/**
		 * 
		 * @param args original list
		 * @return copy
		 * @throws ArrayIndexOutOfBoundsException Use the length of the 0th array to build the array.If some array length less than this length, will throw.
		 * 
		 * @author small_jiu
		 */
		public int[][] more2two(int[]... args) throws ArrayIndexOutOfBoundsException {
			int[][] arg = new int[args.length][args[0].length];
			int hang = arg.length;
			
			for(int i = 0; i < hang; ++i) {
				for(int j = 0; j < arg[i].length; ++j) {
					arg[i][j] = args[i][j];
				}
			}
			return arg;
		}
		
		/**
		 * 
		 * @param arg original arg
		 * @param separator split symbol
		 * @return use 'split symbol' to split back the list
		 * 
		 * @author small_jiu
		 */
		public String[] custemSplitString(String arg, String separator){
			return arg.split("\\" + separator);
		}
		
		/**
		 * 
		 * @param list0 original list
		 * @return copy
		 * 
		 * @author small_jiu
		 */
		public <T> List<T> copyList(List<T> list0){
			List<T> list = new ArrayList<T>();
			for(T o : list0) {
				list.add(o);
			}
			return list;
		}
		
		/**
		 * 
		 * @param list0 original list
		 * @return copy
		 * 
		 * @author small_jiu
		 */
		@SuppressWarnings("unchecked")
		public <T> T[][] copyArray(T[][] array){
			T[][] temp = (T[][]) new Object[array.length+1][array[0].length];
			
			for(int i = 0; i < array.length; ++i) {
				for(int j = 0; j < array[i].length; ++j) {
					temp[i][j] = array[i][j];
				}
			}
			return temp;
		}
		
		/**
		 * {@link Potion#getPotionFromResourceLocation(String)}
		 */
		@Nullable
	    public Potion getRegisteredMobEffect(String id) {
	        Potion potion = Potion.getPotionFromResourceLocation(id);
	        
	        if(potion == null) {
	        	JiuCore.instance.log.fatal("Effect not found: " + id);
	        	return null;
	        }else {
	        	return potion;
	        }
	    }
	}
}
