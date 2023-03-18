package cat.jiu.core.events.entity.player;

import java.util.List;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.eventhandler.Event;

public class PlayerDeathDropsEvent extends Event {
	public final EntityPlayer player;
	public final DamageSource source;
	public final List<EntityItem> drops;
	public final int lootingLevel;
	public final boolean recentlyHit;
	
	public PlayerDeathDropsEvent(EntityPlayer entity, DamageSource source, List<EntityItem> drops, int lootingLevel, boolean recentlyHit) {
		this.player = entity;
		this.source = source;
		this.drops = drops;
		this.lootingLevel = lootingLevel;
		this.recentlyHit = recentlyHit;
	}
	public void addItem(ItemStack stack, BlockPos pos) {
		this.drops.add(new EntityItem(this.player.world, pos.getX(), pos.getY(), pos.getZ(), stack));
	}
}
