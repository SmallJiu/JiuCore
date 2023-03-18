package cat.jiu.core.util;

import java.util.ArrayList;
import java.util.List;

import cat.jiu.core.trigger.JiuCoreTriggers;
import cat.jiu.core.events.client.LanguageReloadEvent;
import cat.jiu.core.events.entity.EntityInWorldEvent;
import cat.jiu.core.events.entity.player.PlayerDeathDropsEvent;
import cat.jiu.core.events.entity.player.PlayerDeathEvent;
import cat.jiu.core.events.entity.player.PlayerInWorldEvent;
import cat.jiu.core.events.entity.player.PlayerJoinWorldEvent;
import cat.jiu.core.events.entity.player.PlayerJumpEvent;
import cat.jiu.core.events.entity.player.PlayerPlaceEvent;
import cat.jiu.core.events.entity.player.PlayerUseItemEvent;
import cat.jiu.core.events.item.ItemInPlayerEvent;
import cat.jiu.core.events.item.ItemInWorldEvent;
import cat.jiu.core.api.values.Values;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiLanguage;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingJumpEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@EventBusSubscriber
public final class JiuCoreEvents {

	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public static void onLanguageReload(GuiScreenEvent.ActionPerformedEvent.Pre event) {
		if(event.getGui() instanceof GuiLanguage
		&& event.getButton().id == 6) {
			MinecraftForge.EVENT_BUS.post(new LanguageReloadEvent(Minecraft.getMinecraft().getLanguageManager().getCurrentLanguage().getLanguageCode()));
		}
	}
	
	@SubscribeEvent
	public static void onEntityTick(LivingUpdateEvent event) {
		EntityLivingBase entity = event.getEntityLiving();
		BlockPos pos = entity.getPosition();
		World world = entity.getEntityWorld();
		
		if(entity instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) entity;
			
			if(pos.getY() >= -61 && pos.getY() <= 0) {
				MinecraftForge.EVENT_BUS.post(new PlayerInWorldEvent.InVoid(player));
			}else if(JiuUtils.entity.isEntityInFluid(world, pos)) {
				MinecraftForge.EVENT_BUS.post(new PlayerInWorldEvent.InFluid(player, world.getBlockState(pos)));
			}
			
			NonNullList<ItemStack> mainInventory = player.inventory.mainInventory;
			ItemStack mainHand = player.getHeldItemMainhand();
			ItemStack offHand = player.getHeldItemOffhand();
			
			if(!mainInventory.isEmpty()) {
				for(int i = 0; i < mainInventory.size(); i++) {
					ItemStack invStack = mainInventory.get(i);
					if(!invStack.isEmpty()) {
						if(!(JiuUtils.item.equalsStack(invStack, offHand)
						 || JiuUtils.item.equalsStack(invStack, mainHand))) {
							MinecraftForge.EVENT_BUS.post(new ItemInPlayerEvent.InInventory(player, invStack, i));
						}
					}
				}
			}
			
			if(!mainHand.isEmpty()) {
				MinecraftForge.EVENT_BUS.post(new ItemInPlayerEvent.InHand(player, true, mainHand));
			}
			if(!offHand.isEmpty()) {
				MinecraftForge.EVENT_BUS.post(new ItemInPlayerEvent.InHand(player, false, offHand));
			}
			
			NonNullList<ItemStack> armorInventory = player.inventory.armorInventory;
			if(!armorInventory.isEmpty()) {
				for(int i = 0; i < armorInventory.size(); i++) {
					ItemStack invStack = armorInventory.get(i);
					if(!invStack.isEmpty()) {
						EntityEquipmentSlot slot = JiuUtils.item.getArmorSlotForID(i);
						MinecraftForge.EVENT_BUS.post(new ItemInPlayerEvent.InArmor(player, invStack, slot));
					}
				}
			}
		}else {
			if(JiuUtils.entity.isEntityInFluid(world, pos)) {
				MinecraftForge.EVENT_BUS.post(new EntityInWorldEvent.InFluid(entity, world.getBlockState(pos)));
			}else if(pos.getY() >= -61 && pos.getY() <= 0) {
				MinecraftForge.EVENT_BUS.post(new EntityInWorldEvent.InVoid(entity));
			}
		}
	}
    
    @SubscribeEvent
	public static void onEntityJump(LivingJumpEvent event) {
		EntityLivingBase entity = event.getEntityLiving();
		if(entity instanceof EntityPlayer) {
			MinecraftForge.EVENT_BUS.post(new PlayerJumpEvent((EntityPlayer)entity));
		}
	}
	
	@SubscribeEvent
	public static void onEntityDeath(LivingDeathEvent event) {
		Entity entity = event.getEntity();
		
		if(entity instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) entity;
			int count = JiuUtils.entity.getPlayerDeathCount(player)+1;
			Values.set(Values.Death, player.getUniqueID(), count, 0);
			Values.saveValue(false);
			
			JiuCoreTriggers.PLAYER_DEATH.trigger(player, player.dimension, count);
			
			MinecraftForge.EVENT_BUS.post(new PlayerDeathEvent(player, count, player.dimension, player.getPosition()));
		}
	}
	
	@SubscribeEvent
	public static void onEntityDeathDropItems(LivingDropsEvent event) {
		EntityLivingBase entity = event.getEntityLiving();
		if(entity instanceof EntityPlayer) {
			MinecraftForge.EVENT_BUS.post(new PlayerDeathDropsEvent((EntityPlayer)entity, event.getSource(), event.getDrops(), event.getLootingLevel(), event.isRecentlyHit()));
		}
	}
	
	@SubscribeEvent
	public static void onEntityUseItemStart(LivingEntityUseItemEvent.Start event) {
		if(event.getEntityLiving() instanceof EntityPlayer) {
			PlayerUseItemEvent.Start e = new PlayerUseItemEvent.Start((EntityPlayer) event.getEntityLiving(), event.getItem(), event.getDuration());
			if(MinecraftForge.EVENT_BUS.post(e)) event.setCanceled(true);
			event.setDuration(e.getDuration());
		}
	}
	
	@SubscribeEvent
	public static void onEntityUseItemTick(LivingEntityUseItemEvent.Tick event) {
		if(event.getEntityLiving() instanceof EntityPlayer) {
			PlayerUseItemEvent.Tick e = new PlayerUseItemEvent.Tick((EntityPlayer) event.getEntityLiving(), event.getItem(), event.getDuration());
			if(MinecraftForge.EVENT_BUS.post(e)) event.setCanceled(true);
			event.setDuration(e.getDuration());
		}
	}
	
	@SubscribeEvent
	public static void onEntityUseItemStop(LivingEntityUseItemEvent.Stop event) {
		if(event.getEntityLiving() instanceof EntityPlayer) {
			PlayerUseItemEvent.Stop e = new PlayerUseItemEvent.Stop((EntityPlayer) event.getEntityLiving(), event.getItem(), event.getDuration());
			if(MinecraftForge.EVENT_BUS.post(e)) event.setCanceled(true);
			event.setDuration(e.getDuration());
		}
	}
	
	@SubscribeEvent
	public static void onEntityUseItemFinish(LivingEntityUseItemEvent.Finish event) {
		if(event.getEntityLiving() instanceof EntityPlayer) {
			PlayerUseItemEvent.Finish e = new PlayerUseItemEvent.Finish((EntityPlayer) event.getEntityLiving(), event.getItem(), event.getDuration(), event.getResultStack());
			MinecraftForge.EVENT_BUS.post(e);
			event.setDuration(e.getDuration());
			event.setResultStack(e.getResultStack());
		}
	}
	
	@SubscribeEvent
	public static void onEntityJoinWorld(EntityJoinWorldEvent event) {
		if(event.getEntity() instanceof EntityPlayer) {
			if(MinecraftForge.EVENT_BUS.post(new PlayerJoinWorldEvent((EntityPlayer) event.getEntity(), event.getEntity().getEntityWorld(), event.getEntity().getPosition(), event.getEntity().dimension))) event.setCanceled(true);
		}
	}
    
	@SubscribeEvent
    public static void onEntityItemTick(TickEvent.WorldTickEvent event) {
    	World world = event.world;
    	
    	List<EntityItem> eitems = null;
    	if(!world.loadedEntityList.isEmpty()) {
    		eitems = new ArrayList<EntityItem>();
    		for(Entity entity : world.loadedEntityList) {
        		if(entity instanceof EntityItem) {
        			EntityItem item = (EntityItem) entity;
        			if(!item.getItem().isEmpty()) {
        				eitems.add(item);
        			}
        		}
        	}
    	}
    	
    	if(eitems!=null && !world.isRemote) {
    		for(EntityItem eitem : eitems) {
        		BlockPos pos = eitem.getPosition();
				if(JiuUtils.entity.isEntityInFluid(world, pos)) {
					MinecraftForge.EVENT_BUS.post(new ItemInWorldEvent.InFluid(eitem, world.getBlockState(pos)));
				}else if(pos.getY() >= -61 && pos.getY() <= 0) {
					MinecraftForge.EVENT_BUS.post(new ItemInWorldEvent.InVoid(eitem));
				}else {
					MinecraftForge.EVENT_BUS.post(new ItemInWorldEvent.InWorld(eitem));
				}
        	}
    		eitems.clear();
    	}
    }
    
    @SubscribeEvent
    public static void onEntityPlaceBlock(BlockEvent.EntityPlaceEvent event) {
    	Entity entity = event.getEntity();
    	IBlockState placedBlock = event.getPlacedBlock();
    	IBlockState placeedAgainst = event.getPlacedAgainst();
    	BlockPos pos = event.getPos();
    	World world = event.getWorld();
    	
    	if(entity instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) entity;
    		if(JiuUtils.item.isFluid(placedBlock)) {
    			MinecraftForge.EVENT_BUS.post(new PlayerPlaceEvent.PlaceFluid(player, pos, world, placedBlock, placeedAgainst));
    		}else {
    			MinecraftForge.EVENT_BUS.post(new PlayerPlaceEvent.PlaceBlock(player, pos, world, placedBlock, placeedAgainst));
    		}
    	}
    }
}
