package cat.jiu.core.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

import cat.jiu.core.JiuCore;
import cat.jiu.core.api.events.entity.IEntityDeathDropItems;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

public final class EntityDeathDrops implements IEntityDeathDropItems {
	static final Map<String, List<DropType>> Drop = Maps.newHashMap();
	
	public static void addDrops(ResourceLocation name, List<DropType> drops) {
		drops.stream().forEach(drop-> addDrops(name, drop));
	}
	public static void addDrops(EntityLivingBase entity, List<DropType> drops) {
		drops.stream().forEach(drop-> addDrops(entity, drop));
	}
	
	public static void addDrops(ResourceLocation name, DropType drop) {
		EntityEntry entity = ForgeRegistries.ENTITIES.getValue(name);
		if(entity == null) unknowEntity(name);
		
		String s = entity.getName();
		if(!Drop.containsKey(s)) {
			Drop.put(s, Lists.newArrayList());
		}
		Drop.get(s).add(drop);
	}
	
	public static void addDrops(EntityLivingBase entity, DropType drop) {
		if(entity instanceof EntityPlayer) {
			JiuCore.getLogOS().error("Can not add drops to Player!");
			return;
		}
		
		String s = EntityList.getEntityString(entity);
		if(s == null) unknowEntity(entity);
		
		if(!Drop.containsKey(s)) {
			Drop.put(s, Lists.newArrayList());
		}
		Drop.get(s).add(drop);
	}
	
	public static List<DropType> getDrops(EntityLivingBase entity) {
		String s = EntityList.getEntityString(entity);
		if(s == null) unknowEntity(entity);

		if(!Drop.containsKey(s)) return null;
		return Drop.get(s);
	}
	
	private static void unknowEntity(ResourceLocation name) {
		throw new RuntimeException("Can not find entity: " + name);
	}
	
	private static void unknowEntity(EntityLivingBase entity) {
		throw new RuntimeException("Can not find entity: EntityID: " + entity.getEntityId() + ", EntityName: " + entity.getName());
	}
	
	public static List<DropType> getDrops(ResourceLocation name) {
		EntityEntry entity = ForgeRegistries.ENTITIES.getValue(name);
		if(entity == null) unknowEntity(name);
		
		String s = entity.getName();
		if(!Drop.containsKey(s)) return null;
		return Drop.get(s);
	}
	
	public static boolean initJsonDrops(World world) {
		File json = new File("./config/jiu/core/entity_drops.json");
		if(!json.exists()) {
			return false;
		}
		Drop.clear();
		try {
			JsonObject file = new JsonParser().parse(new InputStreamReader(new FileInputStream(json), StandardCharsets.UTF_8)).getAsJsonObject();
			
			for (Entry<String, JsonElement> entitys : file.entrySet()) {
				String name = entitys.getKey();
				JsonArray array = entitys.getValue().getAsJsonArray();
				List<DropType> drops = Lists.newArrayList();
				
				for(int i = 0; i < array.size(); i++) {
					JsonElement e = array.get(i);
					if(e.isJsonPrimitive()) {
						ItemStack stack = JiuUtils.item.toStack(e);
						if(stack != null && !stack.isEmpty()) {
							drops.add(new DropType(stack));
						}
					}else if(e.isJsonObject()) {
						JsonObject obj = e.getAsJsonObject();
						if(!obj.has("item")) throw new RuntimeException("Not found \'item\' tag");
						
						ItemStack stack = JiuUtils.item.toStack(obj.get("item"));
						if(stack == null || stack.isEmpty()) throw new RuntimeException("Can not load Item -> " + name);
						
						int minCount = obj.has("minCount") ? obj.get("minCount").getAsInt() : 1;
						int maxCount = obj.has("maxCount") ? obj.get("maxCount").getAsInt() : stack.getCount();
						float chance = obj.has("dropChance") ? obj.get("dropChance").getAsFloat() : 1;

						drops.add(new DropType(minCount, maxCount, chance, stack));
					}
				}
				
				if(drops != null && !drops.isEmpty()) {
					addDrops(new ResourceLocation(name), drops);
				}
			}
			return true;
		} catch (JsonIOException | JsonSyntaxException | FileNotFoundException | NumberFormatException e) {
			throw new RuntimeException(e);
		}
	}
	
	public static final class DropType {
		private final int minCount;
		private final int maxCount;
		private final float chance;
		private final ItemStack stack;
		public DropType(ItemStack stack) {
			this(stack.getCount(), stack.getCount(), 1F, stack);
		}
		public DropType(int minCount, int maxCount, float chance, ItemStack stack) {
			this.minCount = minCount;
			this.maxCount = maxCount;
			this.chance = chance > 1F ? 1F : chance < 0F ? 0F : chance;
			this.stack = stack;
		}
		@Override
		public DropType clone() {
			return new DropType(minCount, maxCount, chance, stack);
		}
		@Override
		public String toString() {
			return "Stack: [" + JiuUtils.item.toString(this.stack) + "], MinCount: " + this.minCount + ", MaxCount: " + this.maxCount + ", Chance: " + this.chance;
		}
	}

	static final JiuRandom rand = new JiuRandom();

	@Override
	public void onEntityDeathDropItems(EntityLivingBase entity, DamageSource source, List<EntityItem> drops, List<ItemStack> items, int lootingLevel, boolean recentlyHit) {
		if(!entity.getEntityWorld().isRemote) {
			List<DropType> otherDrops = getDrops(entity);
			if(otherDrops != null) {
				for(DropType stackTypeT : otherDrops) {
					DropType stackType = stackTypeT.clone();
					ItemStack stack = stackType.stack;
					stack.setCount(rand.nextIntFromRange(stackType.minCount, stackType.maxCount));
					
					if(rand.nextInt(1000) <= stackType.chance*1000) {
						drops.add(this.createEntityItem(entity, stack));
					}
				}
			}
		}
	}

	EntityItem createEntityItem(Entity e, ItemStack stack) {
		BlockPos pos = e.getPosition();
		return new EntityItem(e.getEntityWorld(), pos.getX(), pos.getY(), pos.getZ(), stack);
	}
}
