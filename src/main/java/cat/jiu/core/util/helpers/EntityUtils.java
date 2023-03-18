package cat.jiu.core.util.helpers;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.UUID;
import java.util.Map.Entry;

import javax.annotation.Nullable;

import com.google.common.collect.Maps;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import cat.jiu.core.CoreLoggers;
import cat.jiu.core.api.ITimer;
import net.minecraft.block.BlockLiquid;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.server.MinecraftServer;
import net.minecraft.stats.StatBase;
import net.minecraft.stats.StatList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

import net.minecraftforge.fluids.BlockFluidBase;

public final class EntityUtils {
	public int getPlayerDeathCount(EntityPlayer player) {
		return this.getPlayerStatisticsCount(player, StatList.DEATHS);
	}
	public int getPlayerStatisticsCount(EntityPlayer player, StatBase stat) {
		return player.world.getMinecraftServer().getPlayerList().getPlayerStatsFile(player).readStat(stat);
	}
	
	// 是否是指定玩家名
	// 是则返回true，不是则返回false
	/**
	 * 
	 * @param name the player name
	 * @param player player
	 * @return 'player' name is 'name'
	 * 
	 * @author small_jiu
	 */
	public boolean isPlayer(String name, EntityPlayer player) {
		return player.getName().equals(name);
	}
	
	// 模糊匹配玩家名
	// 含有则返回true，不含有则返回false
	/**
	 * 
	 * @param name the player name
	 * @param player player
	 * @return 'player' name has 'name'
	 * 
	 * @author small_jiu
	 */
	public boolean isVaguePlayer(String name, EntityPlayer player) {
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
	
	public void sendMessage(ICommandSender sender, String key, Object... obj) {
		sender.sendMessage(new TextComponentTranslation(key, obj)); 
	}
	
	public void sendMessage(ICommandSender sender, String key, TextFormatting color, Object... obj) {
		TextComponentTranslation text = new TextComponentTranslation(key, obj);
		sender.sendMessage(text.setStyle(text.getStyle().setColor(color))); 
	}
	
	public void sendMessageToAllPlayer(World world, String key, Object... obj) {
		for(EntityPlayer player : world.getMinecraftServer().getPlayerList().getPlayers()) {
			player.sendMessage(new TextComponentTranslation(key, obj));
		}
	}
	
	public void sendMessageToAllPlayer(World world, String key, TextFormatting color, Object... obj) {
		for(EntityPlayer player : world.getMinecraftServer().getPlayerList().getPlayers()) {
			TextComponentTranslation text = new TextComponentTranslation(key, obj);
			player.sendMessage(text.setStyle(text.getStyle().setColor(color)));
		}
	}
	
	/**
	 * @param entity the entity
	 * @param potion {@link MobEffects}
	 * @param potionTime measure potionTime by the format_second
	 * @param potionLevel potion level, muse be >= 255
	 * 
	 * @author small_jiu
	 */
	public void addPotionEffect(EntityLivingBase entity, Potion potion, ITimer time, int potionLevel) {
		entity.addPotionEffect(new PotionEffect(potion, (int)time.getAllTicks(), potionLevel));
	}
	
	/**
	 * {@link Potion#getPotionFromResourceLocation(String)}
	 */
	@Nullable
    public Potion getRegisteredMobEffect(String id) {
        Potion potion = Potion.getPotionFromResourceLocation(id);
        
        if(potion == null) {
        	CoreLoggers.getLogOS().fatal("Effect not found: " + id);
        	return null;
        }else {
        	return potion;
        }
    }

	private static final HashMap<String, UUID> NameToUUID = Maps.newHashMap();
	private static final HashMap<UUID, String> UUIDToName = Maps.newHashMap();

	public void initNameAndUUID(@Nullable MinecraftServer server) {
		if(server != null) {
			server.getPlayerProfileCache().save();
			server.getPlayerProfileCache().load();
		}
		File file = MinecraftServer.USER_CACHE_FILE;
		if(file.exists()) {
			NameToUUID.clear();
			UUIDToName.clear();
			
			UUID init = new UUID(0,0);
			NameToUUID.put("Initialization", init);
			UUIDToName.put(init, "Initialization");
			
			try(FileInputStream in = new FileInputStream(file)) {
				JsonArray array = JsonUtil.parser.parse(new InputStreamReader(in, StandardCharsets.UTF_8)).getAsJsonArray();
				for(int i = 0; i < array.size(); i++) {
					JsonObject player = array.get(i).getAsJsonObject();
					
					String name = player.get("name").getAsString();
					UUID uid = UUID.fromString(player.get("uuid").getAsString());
					NameToUUID.put(name, uid);
					UUIDToName.put(uid, name);
				}
			}catch(IOException e) {
				e.printStackTrace();
			}
		}
	}

	public boolean hasNameOrUUID(String name) {
		return NameToUUID.containsKey(name) || UUIDToName.containsValue(name);
	}

	public boolean hasNameOrUUID(UUID uid) {
		return UUIDToName.containsKey(uid) || NameToUUID.containsValue(uid);
	}

	public UUID getUUID(String name) {
		if(this.hasNameOrUUID(name)) {
			UUID uid = NameToUUID.get(name);
			if(uid==null) {
				for(Entry<UUID, String> uuid : UUIDToName.entrySet()) {
					if(uuid.getValue().equals(name)) {
						uid = uuid.getKey();
						NameToUUID.put(name, uid);
						break;
					}
				}
			}
			return uid;
		}
		return null;
	}

	public String getName(UUID uid) {
		if(this.hasNameOrUUID(uid)) {
			String name = UUIDToName.get(uid);
			if(name==null) {
				for(Entry<String, UUID> names : NameToUUID.entrySet()) {
					if(names.getValue().equals(uid)) {
						name = names.getKey();
						UUIDToName.put(uid, name);
						break;
					}
				}
			}
			return name;
		}
		return null;
	}
}
