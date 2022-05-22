package cat.jiu.core.util.helpers;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Map.Entry;
import java.util.HashMap;
import java.util.UUID;

import javax.annotation.Nullable;

import com.google.common.collect.Maps;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import cat.jiu.core.JiuCore;
import net.minecraft.block.BlockLiquid;
import net.minecraft.client.resources.I18n;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fluids.BlockFluidBase;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public final class EntityUtils {
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
	
	@SideOnly(Side.CLIENT)
	public void sendI18nMessage(ICommandSender sender, String key, Object... obj) {
		sender.sendMessage(new TextComponentTranslation(I18n.format(key, obj), 4)); 
	}
	
	@SideOnly(Side.CLIENT)
	public void sendI18nMessage(ICommandSender sender, String key, TextFormatting color, Object... obj) {
		TextComponentTranslation text = new TextComponentTranslation(I18n.format(key, obj));
		sender.sendMessage(text.setStyle(text.getStyle().setColor(color))); 
	}
	
	public void sendMessage(ICommandSender sender, String key, Object... obj) {
		sender.sendMessage(new TextComponentTranslation(key, obj)); 
	}
	
	public void sendMessage(ICommandSender sender, String key, TextFormatting color, Object... obj) {
		TextComponentTranslation text = new TextComponentTranslation(key, obj);
		sender.sendMessage(text.setStyle(text.getStyle().setColor(color))); 
	}
	
	@SideOnly(Side.CLIENT)
	public void sendI18nMessageToAllPlayer(World world, String key, Object... obj) {
		for (EntityPlayer player : world.playerEntities) {
			player.sendMessage(new TextComponentTranslation(I18n.format(key, obj), 4));
		}
	}
	
	@SideOnly(Side.CLIENT)
	public void sendI18nMessageToAllPlayer(World world, String key, TextFormatting color, Object... obj) {
		for (EntityPlayer player : world.playerEntities) {
			TextComponentTranslation text = new TextComponentTranslation(I18n.format(key, obj));
			player.sendMessage(text.setStyle(text.getStyle().setColor(color)));
		}
	}
	
	public void sendMessageToAllPlayer(World world, String key, Object... obj) {
		for(EntityPlayer player : world.playerEntities) {
			player.sendMessage(new TextComponentTranslation(key, obj));
		}
	}
	
	public void sendMessageToAllPlayer(World world, String key, TextFormatting color, Object... obj) {
		for(EntityPlayer player : world.playerEntities) {
			TextComponentTranslation text = new TextComponentTranslation(key, obj);
			player.sendMessage(text.setStyle(text.getStyle().setColor(color)));
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
	public void addPotionEffect(EntityLivingBase entity, Potion potion, int potionTime, int potionLevel) {
		entity.addPotionEffect(new PotionEffect(potion, potionTime * 20, potionLevel));
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

	private final HashMap<String, UUID> NameToUUID = Maps.newHashMap();
	private final HashMap<UUID, String> UUIDToName = Maps.newHashMap();

	public void initNameAndUUID(@Nullable MinecraftServer server) {
		if(server != null) {
			server.getPlayerProfileCache().save();
			server.getPlayerProfileCache().load();
		}
		File file = new File("./usernamecache.json");
		if(file.exists()) {
			this.NameToUUID.clear();
			this.UUIDToName.clear();
			try(FileInputStream in = new FileInputStream(file)) {
				JsonObject obj = new JsonParser().parse(new InputStreamReader(in, StandardCharsets.UTF_8)).getAsJsonObject();
				for(Entry<String, JsonElement> cache : obj.entrySet()) {
					String name = cache.getValue().getAsString();
					UUID uid = UUID.fromString(cache.getKey());
					this.NameToUUID.put(name, uid);
					this.UUIDToName.put(uid, name);
				}
				this.NameToUUID.put("Initialization", new UUID(0, 0));
				this.UUIDToName.put(new UUID(0, 0), "Initialization");
			}catch(IOException e) {
				e.printStackTrace();
			}
		}
	}

	public boolean hasNameOrUUID(String name) {
		return (!this.NameToUUID.isEmpty() && !this.UUIDToName.isEmpty()) ? this.NameToUUID.containsKey(name) && this.UUIDToName.containsValue(name) : false;
	}

	public boolean hasNameOrUUID(UUID uid) {
		return (!this.NameToUUID.isEmpty() && !this.UUIDToName.isEmpty()) ? this.UUIDToName.containsKey(uid) && this.NameToUUID.containsValue(uid) : false;
	}

	public UUID getUUID(String name) {
		if(this.hasNameOrUUID(name)) {
			return this.NameToUUID.get(name);
		}
		return null;
	}

	public String getName(UUID uid) {
		if(this.hasNameOrUUID(uid)) {
			return this.UUIDToName.get(uid);
		}
		return null;
	}
}
