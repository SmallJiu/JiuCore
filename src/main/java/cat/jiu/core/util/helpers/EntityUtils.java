package cat.jiu.core.util.helpers;

import net.minecraft.block.BlockLiquid;
import net.minecraft.client.resources.I18n;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fluids.BlockFluidBase;

public final class EntityUtils {
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
	public boolean isPlayer(String name, EntityPlayer player) {
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
	
	/**
	 * 
	 * @param sender Player, or any Entity
	 * @param key Message
	 * 
	 * @author small_jiu
	 */
	public void sendMessage(ICommandSender sender, String key, Object... obj) {
		if(sender.getEntityWorld().isRemote) {
			sender.sendMessage(new TextComponentTranslation(I18n.format(key, obj), 4)); 
		}
	}
	
	public void sendMessage(ICommandSender sender, String key, TextFormatting color, Object... obj) {
		if(sender.getEntityWorld().isRemote) {
			TextComponentTranslation text = new TextComponentTranslation(I18n.format(key, obj));
			sender.sendMessage(text.setStyle(text.getStyle().setColor(color))); 
		}
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
	public void sendMessageToAllPlayer(World world, String key, Object... obj) {
		if(world.isRemote) {
			for (int i = 0; i < world.playerEntities.size(); ++i) {
				EntityPlayer player = world.playerEntities.get(i);

				player.sendMessage(new TextComponentTranslation(I18n.format(key, obj), 4));
			}
		}
	}
	
	public void sendMessageToAllPlayer(World world, String key, TextFormatting color, Object... obj) {
		if(world.isRemote) {
			for (int i = 0; i < world.playerEntities.size(); ++i) {
				EntityPlayer player = world.playerEntities.get(i);

				TextComponentTranslation text = new TextComponentTranslation(I18n.format(key, obj));
				player.sendMessage(text.setStyle(text.getStyle().setColor(color)));
			}
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
