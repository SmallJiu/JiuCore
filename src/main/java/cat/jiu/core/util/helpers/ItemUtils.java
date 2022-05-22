package cat.jiu.core.util.helpers;

import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;
import java.util.Map.Entry;

import com.google.common.collect.Lists;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;

import cat.jiu.core.JiuCore;
import cat.jiu.core.test.BlockTest.TestModSubtypes;
import cat.jiu.core.util.JiuUtils;
import cat.jiu.core.util.base.BaseBlock;

import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTPrimitive;
import net.minecraft.nbt.NBTTagByte;
import net.minecraft.nbt.NBTTagByteArray;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagDouble;
import net.minecraft.nbt.NBTTagFloat;
import net.minecraft.nbt.NBTTagInt;
import net.minecraft.nbt.NBTTagIntArray;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagLong;
import net.minecraft.nbt.NBTTagShort;
import net.minecraft.nbt.NBTTagString;
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
	
	public String toString(ItemStack args) {
		if(JiuUtils.other.isEmpty(args)) return null;
		StringJoiner j = new StringJoiner("@");
		j.add(args.getItem().getRegistryName().toString());
		j.add(Integer.toString(args.getCount()));
		j.add(Integer.toString(args.getMetadata()));
		if(args.hasTagCompound()) {
			JsonObject nbt = this.toJson(JiuUtils.nbt.getItemNBT(args));
			if(nbt != null) j.add(nbt.toString());
		}
		return j.toString();
	}
	
	public JsonObject toJson(ItemStack arg) {
		if(JiuUtils.other.isEmpty(arg)) return null;
		JsonObject obj = new JsonObject();
		
		obj.addProperty("name", arg.getItem().getRegistryName().toString());
		obj.addProperty("count", arg.getCount());
		obj.addProperty("meta", arg.getMetadata());
		if(arg.hasTagCompound()) {
			JsonObject nbt = this.toJson(JiuUtils.nbt.getItemNBT(arg));
			if(nbt != null) obj.add("nbt", nbt);
		}
		
		return obj;
	}
	
	public JsonObject toJson(NBTTagCompound nbt) {
		JsonObject obj = new JsonObject();
		nbt.getKeySet().stream().forEach(key -> {
			JsonElement e = this.toJson(nbt.getTag(key));
			if(e != null) {
				if(e.isJsonPrimitive()) {
					JsonPrimitive pri = (JsonPrimitive) e;
					if(pri.isNumber()) {
						Number num = pri.getAsNumber();
						if(num instanceof Integer) {
							if(!obj.has("int")) {
								obj.add("int", new JsonObject());
							}
							obj.get("int").getAsJsonObject().add(key, pri);
						}else if(num instanceof Float) {
							if(!obj.has("float")) {
								obj.add("float", new JsonObject());
							}
							obj.get("float").getAsJsonObject().add(key, pri);
						}else if(num instanceof Short) {
							if(!obj.has("short")) {
								obj.add("short", new JsonObject());
							}
							obj.get("short").getAsJsonObject().add(key, pri);
						}else if(num instanceof Byte) {
							if(!obj.has("byte")) {
								obj.add("byte", new JsonObject());
							}
							obj.get("byte").getAsJsonObject().add(key, pri);
						}else if(num instanceof Double) {
							if(!obj.has("double")) {
								obj.add("double", new JsonObject());
							}
							obj.get("double").getAsJsonObject().add(key, pri);
						}else if(num instanceof Long) {
							if(!obj.has("long")) {
								obj.add("long", new JsonObject());
							}
							obj.get("long").getAsJsonObject().add(key, pri);
						}
					}else {
						obj.add(key, pri);
					}
				}else if(e.isJsonObject()) {
					if(!obj.has("tags")) obj.add("tags", new JsonObject()); 
					obj.get("tags").getAsJsonObject().add(key, e);
				}else {
					obj.add(key, e);
				}
			}
		});
		return obj;
	}
	
	private JsonElement toJson(NBTBase base) {
		if(base instanceof NBTPrimitive) {
			NBTPrimitive pri = (NBTPrimitive) base;
			if(pri instanceof NBTTagByte) {
				return new JsonPrimitive(pri.getByte());
			}else if(pri instanceof NBTTagInt) {
				return new JsonPrimitive(pri.getInt());
			}else if(pri instanceof NBTTagLong) {
				return new JsonPrimitive(pri.getLong());
			}else if(pri instanceof NBTTagDouble) {
				return new JsonPrimitive(pri.getDouble());
			}else if(pri instanceof NBTTagFloat) {
				return new JsonPrimitive(pri.getFloat());
			}else if(pri instanceof NBTTagShort) {
				return new JsonPrimitive(pri.getShort());
			}
		}else if(base instanceof NBTTagString) {
			return new JsonPrimitive(((NBTTagString)base).getString());
		}else if(base instanceof NBTTagCompound) {
			return this.toJson((NBTTagCompound)base);
		}else if(base instanceof NBTTagList) {
			JsonArray array = new JsonArray();
			for(int i = 0; i < ((NBTTagList)base).tagCount(); i++) {
				JsonElement e = this.toJson(((NBTTagList)base).get(i));
				if(e != null) {
					return e;
				}
			}
			return array;
		}else if(base instanceof NBTTagIntArray) {
			JsonArray array = new JsonArray();
			for(int i : ((NBTTagIntArray)base).getIntArray()) {
				array.add(i);
			}
			return array;
		}else if(base instanceof NBTTagByteArray) {
			JsonArray array = new JsonArray();
			for(byte i : ((NBTTagByteArray)base).getByteArray()) {
				array.add(i);
			}
			return array;
		}
		return null;
	}
	
	public ItemStack toStack(JsonElement e) {
		if(e.isJsonObject()) {
			return this.toStack(e.getAsJsonObject());
		}else if(e.isJsonPrimitive()) {
			return this.toStack(e.getAsString());
		}
		return null;
	}
	
	public ItemStack toStack(JsonObject obj) {
		Item item = Item.getByNameOrId(obj.get("name").getAsString());
		if(item == null) return null;
		int count = obj.has("count") ? obj.get("count").getAsInt() : obj.has("amount") ? obj.get("amount").getAsInt() : 1;
		int meta = obj.has("meta") ? obj.get("meta").getAsInt() : obj.has("data") ? obj.get("data").getAsInt() : 0;
		NBTTagCompound nbt = obj.has("nbt") ? this.getNBT(obj.get("nbt").getAsJsonObject()) : null;

		return JiuUtils.nbt.setNBT(new ItemStack(item, count, meta), nbt);
	}
	
	public NBTTagCompound getNBT(JsonObject obj) {
		NBTTagCompound nbt = new NBTTagCompound();
		if(obj.has("string")) {
			for(Entry<String, JsonElement> nbts : obj.get("string").getAsJsonObject().entrySet()) {
				nbt.setString(nbts.getKey(), nbts.getValue().getAsString());
			}
		}
		if(obj.has("boolean")) {
			for(Entry<String, JsonElement> nbts : obj.get("boolean").getAsJsonObject().entrySet()) {
				nbt.setBoolean(nbts.getKey(), nbts.getValue().getAsBoolean());
			}
		}
		if(obj.has("int")) {
			for(Entry<String, JsonElement> nbts : obj.get("int").getAsJsonObject().entrySet()) {
				nbt.setInteger(nbts.getKey(), nbts.getValue().getAsInt());
			}
		}
		if(obj.has("long")) {
			for(Entry<String, JsonElement> nbts : obj.get("long").getAsJsonObject().entrySet()) {
				nbt.setLong(nbts.getKey(), nbts.getValue().getAsLong());
			}
		}
		if(obj.has("float")) {
			for(Entry<String, JsonElement> nbts : obj.get("float").getAsJsonObject().entrySet()) {
				nbt.setFloat(nbts.getKey(), nbts.getValue().getAsFloat());
			}
		}
		if(obj.has("double")) {
			for(Entry<String, JsonElement> nbts : obj.get("double").getAsJsonObject().entrySet()) {
				nbt.setDouble(nbts.getKey(), nbts.getValue().getAsDouble());
			}
		}
		if(obj.has("byte")) {
			for(Entry<String, JsonElement> nbts : obj.get("byte").getAsJsonObject().entrySet()) {
				nbt.setByte(nbts.getKey(), nbts.getValue().getAsByte());
			}
		}
		if(obj.has("short")) {
			for(Entry<String, JsonElement> nbts : obj.get("short").getAsJsonObject().entrySet()) {
				nbt.setShort(nbts.getKey(), nbts.getValue().getAsShort());
			}
		}
		if(obj.has("int_array")) {
			for(Entry<String, JsonElement> nbts : obj.get("int_array").getAsJsonObject().entrySet()) {
				JsonArray array = nbts.getValue().getAsJsonArray();
				int[] int_array = new int[array.size()];
				for(int i = 0; i < array.size(); i++) {
					int_array[i] = array.get(i).getAsInt();
				}
				nbt.setIntArray(nbts.getKey(), int_array);
			}
		}
		if(obj.has("byte_array")) {
			for(Entry<String, JsonElement> nbts : obj.get("byte_array").getAsJsonObject().entrySet()) {
				JsonArray array = nbts.getValue().getAsJsonArray();
				byte[] byte_array = new byte[array.size()];
				for(int i = 0; i < array.size(); i++) {
					byte_array[i] = array.get(i).getAsByte();
				}
				nbt.setByteArray(nbts.getKey(), byte_array);
			}
		}
		if(obj.has("tags")) {
			for(Entry<String, JsonElement> tags : obj.get("tags").getAsJsonObject().entrySet()) {
				nbt.setTag(tags.getKey(), this.getNBT(tags.getValue().getAsJsonObject()));
				
			}
		}
		
		return nbt;
	}
	
	public ItemStack toStack(String stack) {
		if(stack.contains("@")) {
			String[] name = JiuUtils.other.custemSplitString(stack, "@");
			Item item = Item.getByNameOrId(name[0]);
			int meta = 0;
			int amount = 1;
			NBTTagCompound nbt = null;
			
			switch(name.length) {
				case 4:
					nbt = this.getNBT(new JsonParser().parse(name[3]).getAsJsonObject());
				case 3:
					meta = Integer.parseInt(name[2]);
				case 2:
					amount = Integer.parseInt(name[1]);
					break;
			}
			if(item != null) {
				if(nbt != null) {
					return JiuUtils.nbt.setNBT(new ItemStack(item, amount, meta), nbt);
				}else {
					return new ItemStack(item, amount, meta);
				}
			}
		}else {
			return new ItemStack(Item.getByNameOrId(stack));
		}
		return null;
	}
	
	public List<ItemStack> toStacks(JsonElement e) {
		if(e.isJsonObject()) {
			return this.toStacks(e.getAsJsonObject());
		}else if(e.isJsonArray()) {
			return this.toStacks(e.getAsJsonArray());
		}
		return null;
	}
	
	public List<ItemStack> toStacks(JsonArray array) {
		List<ItemStack> stacks = Lists.newArrayList();
		
		for(int i = 0; i < array.size(); i++) {
			JsonElement e = array.get(i);
			if(e.isJsonArray()) {
				List<ItemStack> stackss = this.toStacks(e);
				if(stackss != null) stacks.addAll(stackss);
			}else {
				ItemStack s = this.toStack(e);
				if(!JiuUtils.other.isEmpty(s)) {
					stacks.add(s);
				}
			}
		}
		
		if(stacks.isEmpty()) return null;
		return stacks;
	}
	
	public List<ItemStack> toStacks(JsonObject obj) {
		List<ItemStack> stacks = Lists.newArrayList();
		
		for(Entry<String, JsonElement> stack : obj.entrySet()) {
			JsonElement e = stack.getValue();
			if(e.isJsonArray()) {
				List<ItemStack> stackss = this.toStacks(e);
				if(stackss != null) stacks.addAll(stackss);
			}else {
				ItemStack s = this.toStack(e);
				if(!JiuUtils.other.isEmpty(s)) {
					stacks.add(s);
				}
			}
		}
		
		if(stacks.isEmpty()) return null;
		return stacks;
	}
	
	public JsonArray toJsonArray(ItemStack[] stacks, boolean useStringStack) {
		return this.toJsonArray(Lists.newArrayList(stacks), useStringStack);
	}
	public JsonArray toJsonArray(List<ItemStack> stacks, boolean useStringStack) {
		JsonArray array = new JsonArray();
		stacks.stream().forEach(stack -> {
			if(useStringStack) {
				String s = this.toString(stack);
				if(s!=null)array.add(s);
			}else {
				JsonElement e = this.toJson(stack);
				if(e!=null)array.add(e);
			}
		});
		return array;
	}
	
	public JsonObject toJsonObject(ItemStack[] stacks, boolean useStringStack) {
		return this.toJsonObject(Lists.newArrayList(stacks), useStringStack);
	}
	public JsonObject toJsonObject(List<ItemStack> stacks, boolean useStringStack) {
		JsonObject obj = new JsonObject();
		for(int i = 0; i < stacks.size(); i++) {
			ItemStack stack = stacks.get(i);
			if(useStringStack) {
				String s = this.toString(stack);
				if(s!=null)obj.addProperty(Integer.toString(i), s);
			}else {
				JsonElement e = this.toJson(stack);
				if(e!=null)obj.add(Integer.toString(i), e);
			}
		}
		return obj;
	}
	
	/**
	 * {@link Block#spawnAsEntity(World, BlockPos, ItemStack)}
	 */
	public void spawnAsEntity(World worldIn, BlockPos pos, ItemStack stack) {
		Block.spawnAsEntity(worldIn, pos, stack.copy());
	}
	
	public void spawnAsEntity(World worldIn, BlockPos pos, ItemStack[] stack) {
		spawnAsEntity(worldIn, pos, stack, false);
	}
	
	public void spawnAsEntity(World worldIn, BlockPos pos, ItemStack[] stacks, boolean clearList) {
		if(JiuUtils.other.isEmpty(stacks)) return;
		for(ItemStack stack : stacks) {
			this.spawnAsEntity(worldIn, pos, stack);
		}
		if(clearList) {
			for(int i = 0; i < stacks.length; i++) {
				stacks[i] = ItemStack.EMPTY;
			}
		}
	}
	
	public void spawnAsEntity(World worldIn, BlockPos pos, List<ItemStack> stack) {
		spawnAsEntity(worldIn, pos, stack, false);
	}
	
	public void spawnAsEntity(World worldIn, BlockPos pos, List<ItemStack> stacks, boolean clearList) {
		if(JiuUtils.other.isEmpty(stacks)) return;
		for(ItemStack stack : stacks) {
			this.spawnAsEntity(worldIn, pos, stack);
		}
		if(clearList) {
			stacks.clear();
		}
	}
	
	public void spawnAsEntity(World worldIn, BlockPos pos, IItemHandler handler) {
		if(handler == null || handler.getSlots() == 0) return;
		for(int slot = 0; slot < handler.getSlots(); slot++) {
			this.spawnAsEntity(worldIn, pos, handler.getStackInSlot(slot));
		}
	}
	
	public boolean addItemToSlot(IItemHandlerModifiable slots, ItemStack stack, boolean simulate) {
		if(stack == null || stack.isEmpty()) return false;
		for (int slot = 0; slot < slots.getSlots(); ++slot) {
			ItemStack putStack = slots.getStackInSlot(slot);
			if (JiuUtils.item.equalsStack(putStack, stack)) {
				if(!simulate) {
					putStack.grow(stack.getCount());
					slots.setStackInSlot(slot, putStack);
				}
				return true;
			}else if(putStack == null || putStack.isEmpty()){
				if(!simulate) {
					slots.setStackInSlot(slot, stack);
				}
				return true;
			}
		}
		return false;
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
	
	public ItemStack getStackFromBlockState(IBlockState state) {
		return this.getStackFromBlockState(state, 1);
	}
	
	public ItemStack getStackFromBlockState(IBlockState state, int amout) {
		return new ItemStack(state.getBlock(), amout, this.getMetaFromBlockState(state));
	}
	
	public int getMetaFromBlockState(IBlockState state) {
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
			return JiuUtils.nbt.setNBT(new ItemStack(stack.getItem(), stack.getCount(), i), stack.getTagCompound());
		}else {
			return JiuUtils.nbt.setNBT(new ItemStack(stack.getItem(), i, stack.getMetadata()), stack.getTagCompound());
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
			Item jItem = Item.getByNameOrId(name);
			if(jItem == null) return ItemStack.EMPTY; 
			int jAmout = Integer.parseInt(amout);
			int jMeta = Integer.parseInt(meta);
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
			return ItemStack.EMPTY;
		}
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
	 * {@link #equalsStack(ItemStack, ItemStack, boolean, boolean, boolean)}
	 */
	public boolean equalsStack(ItemStack stackA, ItemStack stackB, boolean checkDamage, boolean checkAmout) {
		return this.equalsStack(stackA, stackB, checkDamage, checkAmout, false);
	}
	
	/**
	 * 
	 * @param stackA original stack
	 * @param stackB need to equals stack
	 * @param checkDamage check damage and meta
	 * @param checkAmout check amout
	 * @param checkNBT check nbt tags
	 * @return if stackA = stackB, return 'true', else return 'false'.
	 * 
	 * @author small_jiu
	 */
	public boolean equalsStack(ItemStack stackA, ItemStack stackB, boolean checkDamage, boolean checkAmout, boolean checkNBT) {
		if(stackA == null || stackB == null) {
			return false;
		}
		if(stackA.isEmpty() || stackB.isEmpty()) {
			return false;
		}
		if(stackA == stackB) {
			return true;
		}else {
			if(stackA.getItem() != stackB.getItem()) {
				return false;
			}
			if(checkDamage) {
				if(stackA.getMetadata() != stackB.getMetadata()) {
					return false;
				}
			}
			if(checkAmout) {
				if(stackA.getCount() != stackB.getCount()) {
					return false;
				}
			}
			if(checkNBT) {
				if(stackA.getTagCompound() != null && stackB.getTagCompound() != null) {
					if(!stackA.getTagCompound().equals(stackB.getTagCompound())) {
						return false;
					}
				}else {
					return false;
				}
			}
		}
		return true;
	}
	
	public EntityEquipmentSlot getArmorSlotForID(int id) {
		if(id > 4) {
			return null;
		}
		for(EntityEquipmentSlot slot : EntityEquipmentSlot.values()) {
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
				}
			}
		}
		return false;
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
	 * getTexture From ItemStack
	 * 
	 * @author small_jiu
	 */
	@SuppressWarnings({ "deprecation", "unused" })
	public ResourceLocation getTexture(ItemStack stack) {
		TextureMap map = Minecraft.getMinecraft().getTextureMapBlocks();
		
		if(isBlock(stack)) {
			Block block = getBlockFromItemStack(stack);
			IBlockState state = block.getStateFromMeta(stack.getMetadata());
		}else if(stack.getItem() instanceof Item) {
			Item item = stack.getItem();
		}
		return new ResourceLocation(map.getAtlasSprite(stack.getItem().getRegistryName().toString()).getIconName());
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
		for(TestModSubtypes type : TestModSubtypes.values()){
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
		for(TestModSubtypes type : TestModSubtypes.values()){
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
