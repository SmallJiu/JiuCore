package cat.jiu.core.util.helpers;

import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

import javax.annotation.Nullable;

import java.util.Map.Entry;

import com.google.common.collect.Lists;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;

import cat.jiu.core.test.BlockTest.TestModSubtypes;
import cat.jiu.core.util.JiuUtils;
import cat.jiu.core.util.timer.Timer;

import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.*;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import net.minecraftforge.fluids.BlockFluidBase;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.oredict.OreDictionary;

public final class ItemUtils {
	public ItemStack getStackFromBlockPos(World world, BlockPos pos) {
		ItemStack stack = this.getStackFromBlockState(world.getBlockState(pos));
		TileEntity tile = world.getTileEntity(pos);
		NBTTagCompound nbt = null;
		if(tile != null) {
			nbt = new NBTTagCompound();
			nbt.setTag("BlockEntityTag", tile.writeToNBT(new NBTTagCompound()));
		}
		return JiuUtils.nbt.setNBT(stack, nbt);
	}
	
	public List<PotionEffect> toEffects(JsonElement arg) {
		if(arg.isJsonObject()) {
			return this.toEffects(arg.getAsJsonObject());
		}else if(arg.isJsonArray()){
			return this.toEffects(arg.getAsJsonArray());
		}
		return null;
	}
	
	public List<PotionEffect> toEffects(JsonObject arg) {
		List<PotionEffect> effects = Lists.newArrayList();
		arg.entrySet().stream().forEach(effect -> {
			effects.add(this.toEffect(effect.getValue()));
		});
		return effects;
	}
	
	public List<PotionEffect> toEffects(JsonArray arg) {
		List<PotionEffect> effects = Lists.newArrayList();
		arg.forEach(effect -> {
			effects.add(this.toEffect(effect));
		});
		return effects;
	}
	
	public PotionEffect toEffect(JsonElement arg) {
		if(arg.isJsonObject()) {
			return this.toEffect(arg.getAsJsonObject());
		}else if(arg.isJsonPrimitive()) {
			return this.toEffect(arg.getAsString());
		}
		return null;
	}
	
	public PotionEffect toEffect(String arg) {
		if(arg.contains(":")) {
			String[] effects = JiuUtils.other.custemSplitString("@", arg);
			Potion potion = null;
			int level = 0;
			long ticks = 200;
			boolean ambient = false;
			boolean showParticles = true;
			
			switch(effects.length) {
				case 5: showParticles = Boolean.parseBoolean(effects[4]);
				case 4: ambient = Boolean.parseBoolean(effects[3]);
				case 3:
					String[] times = JiuUtils.other.custemSplitString(":", effects[2]);
					ticks = Timer.parseTick(Integer.parseInt(times[0]), Integer.parseInt(times[1]), Integer.parseInt(times[2]));
				case 2: level = Integer.parseInt(effects[1]);
				default: potion = Potion.getPotionFromResourceLocation(effects[0]);
					break;
			}
			if(potion == null) return null;
			return new PotionEffect(potion, level, (int) ticks, ambient, showParticles);
		}else {
			return new PotionEffect(Potion.getPotionFromResourceLocation(arg), 0, 200);
		}
	}
	
	public PotionEffect toEffect(JsonObject obj) {
		Potion potion = Potion.getPotionFromResourceLocation(obj.get("name").getAsString());;
		int level = 0;
		long ticks = 200;
		boolean ambient = false;
		boolean showParticles = true;
		
		if(obj.has("ambient")) {
			ambient = obj.get("ambient").getAsBoolean();
		}
		if(obj.has("showParticles")) {
			showParticles = obj.get("showParticles").getAsBoolean();
		}
		if(obj.has("level")) {
			level = obj.get("level").getAsInt();
		}
		if(obj.has("lastMillis")) {
			Timer t = Timer.getTime(obj.get("lastMillis"));
			ticks = t != null ? t.getTicks() : 0;
		}
		if(potion == null) return null;
		return new PotionEffect(potion, level, (int) ticks, ambient, showParticles);
	}
	
	public String toString(PotionEffect arg) {
		if(arg == null) return null;
		return JiuUtils.other.addJoins("@", arg.getEffectName(), arg.getAmplifier(), new Timer(arg.getDuration()).toString(), arg.getIsAmbient(), arg.doesShowParticles());
	}
	
	public JsonObject toJson(PotionEffect arg) {
		if(arg == null) return null;
		JsonObject obj = new JsonObject();
		
		obj.addProperty("name", arg.getEffectName());
		obj.addProperty("level", arg.getAmplifier());
		obj.add("lastMillis", new Timer(arg.getDuration()).writeTo(JsonObject.class));
		
		obj.addProperty("ambient", arg.getIsAmbient());
		obj.addProperty("showParticles", arg.doesShowParticles());
		
		return obj;
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
				}else if(e.isJsonArray()) {
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
			JsonArray num_array = this.getNumberArray((NBTTagString) base);
			if(num_array != null) return num_array;
			
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
		}else if(base instanceof NBTTagLongArray) {
			JsonArray array = new JsonArray();
			for(long i : ((NBTTagLongArray)base).data) {
				array.add(i);
			}
			return array;
		}
		return null;
	}
	
	private JsonArray getNumberArray(NBTTagString str) {
		JsonArray num_array = null;
		String s = str.getString();
		if(s.contains("short_array") && s.contains("@") && s.contains(",")) {
			num_array = new JsonArray();
			for (Short num : JiuUtils.other.toNumberArray(Short.class, JiuUtils.other.custemSplitString(",", JiuUtils.other.custemSplitString("@", s)[1]))) {
				num_array.add(num);
			}
		}else if(s.contains("double_array") && s.contains("@") && s.contains(",")) {
			num_array = new JsonArray();
			for (Double num : JiuUtils.other.toNumberArray(Double.class, JiuUtils.other.custemSplitString(",", JiuUtils.other.custemSplitString("@", s)[1]))) {
				num_array.add(num);
			}
		}else if(s.contains("float_array") && s.contains("@") && s.contains(",")) {
			num_array = new JsonArray();
			for (Float num : JiuUtils.other.toNumberArray(Float.class, JiuUtils.other.custemSplitString(",", JiuUtils.other.custemSplitString("@", s)[1]))) {
				num_array.add(num);
			}
		}
		
		if(num_array != null) return num_array;
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
		for(Entry<String, JsonElement> objTags : obj.entrySet()) {
			String key = objTags.getKey();
			JsonElement value = objTags.getValue();
			if("string".equals(key)) {
				for(Entry<String, JsonElement> nbts : value.getAsJsonObject().entrySet()) {
					nbt.setString(nbts.getKey(), nbts.getValue().getAsString());
				}
				continue;
			}
			if("boolean".equals(key)) {
				for(Entry<String, JsonElement> nbts : value.getAsJsonObject().entrySet()) {
					nbt.setBoolean(nbts.getKey(), nbts.getValue().getAsBoolean());
				}
				continue;
			}
			if("int".equals(key)) {
				for(Entry<String, JsonElement> nbts : value.getAsJsonObject().entrySet()) {
					nbt.setInteger(nbts.getKey(), nbts.getValue().getAsInt());
				}
				continue;
			}
			if("long".equals(key)) {
				for(Entry<String, JsonElement> nbts : value.getAsJsonObject().entrySet()) {
					nbt.setLong(nbts.getKey(), nbts.getValue().getAsLong());
				}
				continue;
			}
			if("float".equals(key)) {
				for(Entry<String, JsonElement> nbts : value.getAsJsonObject().entrySet()) {
					nbt.setFloat(nbts.getKey(), nbts.getValue().getAsFloat());
				}
				continue;
			}
			if("double".equals(key)) {
				for(Entry<String, JsonElement> nbts : value.getAsJsonObject().entrySet()) {
					nbt.setDouble(nbts.getKey(), nbts.getValue().getAsDouble());
				}
				continue;
			}
			if("byte".equals(key)) {
				for(Entry<String, JsonElement> nbts : value.getAsJsonObject().entrySet()) {
					nbt.setByte(nbts.getKey(), nbts.getValue().getAsByte());
				}
				continue;
			}
			if("short".equals(key)) {
				for(Entry<String, JsonElement> nbts : value.getAsJsonObject().entrySet()) {
					nbt.setShort(nbts.getKey(), nbts.getValue().getAsShort());
				}
				continue;
			}
			if("int_array".equals(key)) {
				for(Entry<String, JsonElement> nbts : value.getAsJsonObject().entrySet()) {
					JsonArray array = nbts.getValue().getAsJsonArray();
					int[] int_array = new int[array.size()];
					for(int i = 0; i < array.size(); i++) {
						int_array[i] = array.get(i).getAsInt();
					}
					nbt.setIntArray(nbts.getKey(), int_array);
				}
				continue;
			}
			if("short_array".equals(key)) {
				for(Entry<String, JsonElement> nbts : value.getAsJsonObject().entrySet()) {
					JsonArray array = nbts.getValue().getAsJsonArray();
					short[] num_array = new short[array.size()];
					for(int i = 0; i < array.size(); i++) {
						num_array[i] = array.get(i).getAsShort();
					}
					JiuUtils.nbt.setNBT(nbt, nbts.getKey(), num_array);
				}
				continue;
			}
			if("byte_array".equals(key)) {
				for(Entry<String, JsonElement> nbts : value.getAsJsonObject().entrySet()) {
					JsonArray array = nbts.getValue().getAsJsonArray();
					byte[] num_array = new byte[array.size()];
					for(int i = 0; i < array.size(); i++) {
						num_array[i] = array.get(i).getAsByte();
					}
					JiuUtils.nbt.setNBT(nbt, nbts.getKey(), num_array);
				}
				continue;
			}
			if("double_array".equals(key)) {
				for(Entry<String, JsonElement> nbts : value.getAsJsonObject().entrySet()) {
					JsonArray array = nbts.getValue().getAsJsonArray();
					double[] num_array = new double[array.size()];
					for(int i = 0; i < array.size(); i++) {
						num_array[i] = array.get(i).getAsDouble();
					}
					JiuUtils.nbt.setNBT(nbt, nbts.getKey(), num_array);
				}
				continue;
			}
			if("float_array".equals(key)) {
				for(Entry<String, JsonElement> nbts : value.getAsJsonObject().entrySet()) {
					JsonArray array = nbts.getValue().getAsJsonArray();
					float[] num_array = new float[array.size()];
					for(int i = 0; i < array.size(); i++) {
						num_array[i] = array.get(i).getAsFloat();
					}
					JiuUtils.nbt.setNBT(nbt, nbts.getKey(), num_array);
				}
				continue;
			}
			if("tags".equals(key)) {
				for(Entry<String, JsonElement> tags : value.getAsJsonObject().entrySet()) {
					nbt.setTag(tags.getKey(), this.getNBT(tags.getValue().getAsJsonObject()));
				}
				continue;
			}
			if(value.isJsonObject()) {
				nbt.setTag(key, this.getNBT(value.getAsJsonObject()));
				continue;
			}
			if(value.isJsonArray()) {
				nbt.setTag(key, this.getNBT(value.getAsJsonArray()));
				continue;
			}
		}
		
		return nbt;
	}
	
	public NBTTagList getNBT(JsonArray array) {
		NBTTagList list = new NBTTagList();
		for(int i = 0; i < array.size(); i++) {
			JsonElement array_element = array.get(i);
			if(array_element.isJsonObject()) {
				list.appendTag(this.getNBT(array_element.getAsJsonObject()));
				continue;
			}
			if(array_element.isJsonArray()) {
				list.appendTag(this.getNBT(array_element.getAsJsonArray()));
				continue;
			}
			if(array_element.isJsonPrimitive()) {
				JsonPrimitive pri = array_element.getAsJsonPrimitive();
				if(pri.isString()) {
					list.appendTag(new NBTTagString(pri.getAsString()));
					continue;
				}
				if(pri.isBoolean()) {
					list.appendTag(new NBTTagByte(pri.getAsBoolean() ? (byte)1 : (byte)0));
					continue;
				}
				if(pri.isNumber()) {
					Number num = pri.getAsNumber();
					if(num instanceof Integer) {
						list.appendTag(new NBTTagInt((Integer) num));
						continue;
					}
					if(num instanceof Double) {
						list.appendTag(new NBTTagDouble((Double) num));
						continue;
					}
					if(num instanceof Byte) {
						list.appendTag(new NBTTagByte((Byte) num));
						continue;
					}
					if(num instanceof Long) {
						list.appendTag(new NBTTagLong((Long) num));
						continue;
					}
					if(num instanceof Float) {
						list.appendTag(new NBTTagFloat((Float) num));
						continue;
					}
					if(num instanceof Short) {
						list.appendTag(new NBTTagShort((Short) num));
						continue;
					}
				}
			}
		}
		return list;
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
		array.forEach(e -> {
			if(e.isJsonArray()) {
				List<ItemStack> stackss = this.toStacks(e);
				if(stackss != null) stacks.addAll(stackss);
			}else {
				ItemStack s = this.toStack(e);
				if(!JiuUtils.other.isEmpty(s)) {
					stacks.add(s);
				}
			}
		});
		if(stacks.isEmpty()) return null;
		return stacks;
	}
	
	public List<ItemStack> toStacks(JsonObject obj) {
		List<ItemStack> stacks = Lists.newArrayList();
		obj.entrySet().stream().forEach(es -> {
			JsonElement e = es.getValue();
			if(e.isJsonArray()) {
				List<ItemStack> stackss = this.toStacks(e);
				if(stackss != null) stacks.addAll(stackss);
			}else {
				ItemStack s = this.toStack(e);
				if(!JiuUtils.other.isEmpty(s)) {
					stacks.add(s);
				}
			}
		});
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

	@Nullable
	public Block getBlockFromItemStack(ItemStack stack) {
		return isBlock(stack) ? ((ItemBlock)stack.getItem()).getBlock() : null;
	}
	
	@Nullable
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
	 * Not check nbt, amount<p>
	 * check meta<p>
	 * {@link #equalsStack(ItemStack, ItemStack, boolean)}
	 */
	public boolean equalsStack(ItemStack stackA, ItemStack stackB) {
		return this.equalsStack(stackA, stackB, false);
	}
	
	/**
	 * Not check nbt<p>
	 * check meta<p>
	 * {@link #equalsStack(ItemStack, ItemStack, boolean, boolean)}
	 */
	public boolean equalsStack(ItemStack stackA, ItemStack stackB, boolean checkAmout) {
		return this.equalsStack(stackA, stackB, true, checkAmout);
	}
	
	/**
	 * 
	 * Not check nbt<p>
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
	
	@SideOnly(Side.CLIENT)
	public ResourceLocation getTexture(IBlockState state, EnumFacing face) {
		Minecraft mc = Minecraft.getMinecraft();
		IBakedModel model = mc.getRenderItem().getItemModelMesher().getItemModel(getStackFromBlockState(state));
		List<BakedQuad> l = model.getQuads(getStateFromItemStack(getStackFromBlockState(state)), face, 100);
		String name = "";
		if(!l.isEmpty()) {
			name = l.get(0).getSprite().getIconName();
		}else {
			name = model.getParticleTexture().getIconName();
		}
		
		ResourceLocation texture = new ResourceLocation(name);
		return texture;
	}
	
	@SideOnly(Side.CLIENT)
	public ResourceLocation getTexture(ItemStack stack) {
		Minecraft mc = Minecraft.getMinecraft();
		String name = mc.getRenderItem().getItemModelMesher().getItemModel(stack).getParticleTexture().getIconName();
		ResourceLocation texture = new ResourceLocation(name);
		return texture;
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
