package cat.jiu.core.util;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import cat.jiu.core.api.IJiuEvent;
import cat.jiu.core.api.events.entity.IEntityDeathEvent;
import cat.jiu.core.api.events.entity.IEntityInFluidEvent;
import cat.jiu.core.api.events.entity.IEntityJoinWorldEvent;
import cat.jiu.core.api.events.entity.IEntityTickEvent;
import cat.jiu.core.api.events.item.IItemInFluidTickEvent;
import cat.jiu.core.api.events.item.IItemInWorldTickEvent;
import cat.jiu.core.api.events.player.IPlayerCraftedItemEvent;
import cat.jiu.core.api.events.player.IPlayerDeathEvent;
import cat.jiu.core.api.events.player.IPlayerInFluidEvent;
import cat.jiu.core.api.events.player.IPlayerJoinWorldEvent;
import cat.jiu.core.api.events.player.IPlayerLoggedInEvent;
import cat.jiu.core.api.events.player.IPlayerLoggedOutEvent;
import cat.jiu.core.api.events.player.IPlayerPickupEntityItemEvent;
import cat.jiu.core.api.events.player.IPlayerPickupXPEvent;
import cat.jiu.core.api.events.player.IPlayerRespawnEvent;
import cat.jiu.core.api.events.player.IPlayerSmeltedItemEvent;
import cat.jiu.core.api.events.player.IPlayerTickEvent;
import cat.jiu.core.api.events.player.IPlayerUseKeyboardInGameEvent;
import cat.jiu.core.api.events.player.IPlayerUseKeyboardInGuiEvent;
import cat.jiu.core.api.events.player.IPlayerUseMouseInGameEvent;
import cat.jiu.core.api.events.player.IPlayerUseMouseInGuiEvent;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.client.event.MouseEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.event.entity.player.PlayerPickupXpEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

@EventBusSubscriber
public final class JiuCoreEvents {
	private static final List<IJiuEvent> events = new ArrayList<IJiuEvent>();
	
	public static void addEvents(IJiuEvent... envs) {
		for(IJiuEvent event : envs) {
			addEvent(event);
		}
	}
	
	public static void addEvent(IJiuEvent[] envs) {
		for(IJiuEvent event : envs) {
			addEvent(event);
		}
	}
	
	public static void addEvent(List<IJiuEvent> envs) {
		for(IJiuEvent event : envs) {
			addEvent(event);
		}
	}
	
	public static void addEvent(IJiuEvent event) {
		events.add(event);
	}
	
	/*
	public static final void removeEvent(IJiuEvent event) {
		events.remove(event);
	}
	*/
	
	public static List<IJiuEvent> getEvents(){
		return JiuUtils.other.copyList(events);
	}
	
	@SubscribeEvent
	public static void onKeyDownInGame(InputEvent.KeyInputEvent event) {
		int key = Keyboard.getEventKey();
		
		for(IJiuEvent event0 : events) {
			if(event0 instanceof IPlayerUseKeyboardInGameEvent) {
				((IPlayerUseKeyboardInGameEvent) event0).onPlayerUseKeyboardInGame(key);
			}
		}
	}
	
	@SubscribeEvent
	public static void onKeyDownInGui(GuiScreenEvent.KeyboardInputEvent event) {
		GuiScreen gui = event.getGui();
		int key = Keyboard.getEventKey();
		
		for(IJiuEvent event0 : events) {
			if(event0 instanceof IPlayerUseKeyboardInGuiEvent) {
				((IPlayerUseKeyboardInGuiEvent) event0).onPlayerUseKeyboardInGui(gui, key);
			}
		}
	}
	
	@SubscribeEvent
	public static void onMouseDownInGame(InputEvent.MouseInputEvent event) {
		int key = Mouse.getEventButton();
		int x = Mouse.getX();
		int y = Mouse.getY();
		
		if(Mouse.isButtonDown(key)) {
			for(IJiuEvent event0 : events) {
				if(event0 instanceof IPlayerUseMouseInGameEvent) {
					((IPlayerUseMouseInGameEvent) event0).onPlayerUseMouseInGame(key, x, y);
				}
			}
		}
	}
	
	@SubscribeEvent
	public static void onMouseKeepDownInGui(MouseEvent event) {
		/*
		int key = Mouse.getEventButton();
		
		if(Mouse.isClipMouseCoordinatesToWindow()) {
			if(key == 0) {
				JiuCore.instance.log.info("gui mouse Key is 'Left': " + key);
			}else {
				JiuCore.instance.log.info("gui mouse Key is '" + Keyboard.getKeyName(key) + "': " + key);
			}
		}
		*/
	}
	
	@SubscribeEvent
	public static void onMouseDownInGui(GuiScreenEvent.MouseInputEvent event) {
		GuiScreen gui = event.getGui();
		int key = Mouse.getEventButton();
		int x = Mouse.getX();
		int y = Mouse.getY();
		
		if(Mouse.isButtonDown(key)) {
			for(IJiuEvent event0 : events) {
				if(event0 instanceof IPlayerUseMouseInGuiEvent) {
					((IPlayerUseMouseInGuiEvent) event0).onPlayerUseMouseInGui(gui, x, y, key);
				}
			}
			/*
			while(Mouse.getEventButtonState()) {
				if(key == 0) {
					JiuCore.instance.log.info("gui mouse Key is 'Left': " + key);
				}else {
					JiuCore.instance.log.info("gui mouse Key is '" + Keyboard.getKeyName(key) + "': " + key);
				}
			}
			
			/*
			if(Mouse.getEventButtonState()) {
				for(IJiuEvent event0 : events) {
					if(event0 instanceof IPlayerKeepUseMouseInGui) {
						((IPlayerKeepUseMouseInGui) event0).onPlayerKeepUseMouseInGui(gui, x, y, key);
					}
				}
			}
			*/
		}
	}
	
	@SubscribeEvent
	public static void onEntityTick(LivingUpdateEvent event) {
		Entity entity = event.getEntity();
		BlockPos pos = entity.getPosition();
		World world = entity.getEntityWorld();
		
		for(IJiuEvent event0 : events) {
			if(event0 instanceof IEntityTickEvent) {
				((IEntityTickEvent) event0).onEntityTick(entity, world, pos);
			}
			
			if(event0 instanceof IPlayerTickEvent) {
				if(entity instanceof EntityPlayer) {
					EntityPlayer player = (EntityPlayer) entity;
					((IPlayerTickEvent) event0).onPlayerTick(player, world, pos);
				}
			}
			
			if(event0 instanceof IEntityInFluidEvent) {
				if(JiuUtils.entity.isEntityInFluid(world, pos)) {
					((IEntityInFluidEvent) event0).onEntityInFluidTick(entity, world, pos, world.getBlockState(pos));
				}
			}
			
			if(event0 instanceof IPlayerInFluidEvent) {
				if(JiuUtils.entity.isEntityInFluid(world, pos)) {
					if(entity instanceof EntityPlayer) {
						EntityPlayer player = (EntityPlayer) entity;
						((IPlayerInFluidEvent) event0).onPlayerInFluidTick(player, world, pos, world.getBlockState(pos));
					}
				}
			}
		}
	}
	
	@SubscribeEvent
	public static void onEntityDeath(LivingDeathEvent event) {
		Entity entity = event.getEntity();
		BlockPos pos = entity.getPosition();
		World world = entity.getEntityWorld();
		
		for(IJiuEvent event0 : events) {
			if(event0 instanceof IEntityDeathEvent) {
				((IEntityDeathEvent) event0).onEntityDeath(entity, world, pos);
			}
			
			if(event0 instanceof IPlayerDeathEvent) {
				if(entity instanceof EntityPlayer) {
					EntityPlayer player = (EntityPlayer) entity;
					((IPlayerDeathEvent) event0).onPlayerDeath(player, world, pos);
				}
			}
		}
	}
	
	@SubscribeEvent
	public static void onEntityJoinWorld(EntityJoinWorldEvent event) {
		Entity entity = event.getEntity();
		BlockPos pos = entity.getPosition();
		World world = entity.getEntityWorld();
		
		for(IJiuEvent event0 : events) {
			if(event0 instanceof IEntityJoinWorldEvent) {
				((IEntityJoinWorldEvent) event0).onEntityJoinWorld(entity, world, pos);
			}
			if(event0 instanceof IPlayerJoinWorldEvent) {
				if(entity instanceof EntityPlayer) {
					EntityPlayer player = (EntityPlayer) entity;
					((IPlayerJoinWorldEvent) event0).onPlayerJoinWorld(player, world, pos);
				}
			}
		}
	}
	
	@SubscribeEvent
	public static void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
		EntityPlayer player = event.player;
		World world = player.getEntityWorld();
		BlockPos pos = player.getPosition();
		
		for(IJiuEvent event0 : events) {
			if(event0 instanceof IPlayerLoggedInEvent) {
				((IPlayerLoggedInEvent) event0).onPlayerLoggedIn(player, world, pos);
			}
		}
	}
	
	@SubscribeEvent
	public static void onPlayerLoggedOut(PlayerEvent.PlayerLoggedOutEvent event) {
		EntityPlayer player = event.player;
		World world = player.getEntityWorld();
		BlockPos pos = player.getPosition();
		
		for(IJiuEvent event0 : events) {
			if(event0 instanceof IPlayerLoggedOutEvent) {
				((IPlayerLoggedOutEvent) event0).onPlayerLoggedOut(player, world, pos);
			}
		}
	}
	
	@SubscribeEvent
	public static void onPlayerSmeltedItem(PlayerEvent.ItemSmeltedEvent event) {
		EntityPlayer player = event.player;
		World world = player.getEntityWorld();
		BlockPos pos = player.getPosition();
		ItemStack stack = event.smelting;
		
		for(IJiuEvent event0 : events) {
			if(event0 instanceof IPlayerSmeltedItemEvent) {
				((IPlayerSmeltedItemEvent) event0).onPlayerSmeltedItem(player, stack, world, pos);
			}
		}
	}
	
	@SubscribeEvent
	public static void onPlayerCraftedItem(PlayerEvent.ItemCraftedEvent event) {
		EntityPlayer player = event.player;
		World world = player.getEntityWorld();
		BlockPos pos = player.getPosition();
		ItemStack stack = event.crafting;
		IInventory gui = event.craftMatrix;
		
		for(IJiuEvent event0 : events) {
			if(event0 instanceof IPlayerCraftedItemEvent) {
				((IPlayerCraftedItemEvent) event0).onPlayerCraftedItemInGui(player, gui, stack, world, pos);
			}
		}
	}
	
	@SubscribeEvent
	public static void onPlayerRespawn(PlayerEvent.PlayerRespawnEvent event) {
		EntityPlayer player = event.player;
		World world = player.getEntityWorld();
		BlockPos pos = player.getPosition();
		int dim = player.dimension;
		
		for(IJiuEvent event0 : events) {
			if(event0 instanceof IPlayerRespawnEvent) {
				((IPlayerRespawnEvent) event0).onPlayerRespawn(player, world, pos, dim);
			}
		}
	}
	
	@SubscribeEvent
    public static void onPlayerPickupXp(PlayerPickupXpEvent event) {
		EntityPlayer player = event.getEntityPlayer();
		BlockPos pos = player.getPosition();
		World world = player.getEntityWorld();
		
		for(IJiuEvent event0 : events) {
			if(event0 instanceof IPlayerPickupXPEvent) {
				int xps = event.getOrb().getXpValue();
				((IPlayerPickupXPEvent) event0).onPlayerPickupXP(player, xps, world, pos);
			}
		}
    }
    
    @SubscribeEvent
    public static void onPlayerPickupEntityItem(EntityItemPickupEvent event) {
    	EntityPlayer player = event.getEntityPlayer();
		BlockPos pos = player.getPosition();
		World world = player.getEntityWorld();
		
		for(IJiuEvent event0 : events) {
			if(event0 instanceof IPlayerPickupEntityItemEvent) {
				ItemStack stack = event.getItem().getItem();
				((IPlayerPickupEntityItemEvent) event0).onPlayerPickupEntityItem(player, stack, world, pos);
			}
		}
    }
    
    @SubscribeEvent
    public static void onEntityItemTick(TickEvent.WorldTickEvent event) {
    	World world = event.world;
    	List<EntityItem> eitems = new ArrayList<>();
    	for(Entity entity : world.loadedEntityList) {
    		if(entity instanceof EntityItem) {
    			eitems.add(((EntityItem) entity));
    		}
    	}
    	
    	for(EntityItem eitem : eitems) {
    		BlockPos pos = eitem.getPosition();
    		for(IJiuEvent event0 : events) {
    			if(event0 instanceof IItemInWorldTickEvent) {
    				((IItemInWorldTickEvent) event0).onItemInWorldTick(eitem, world, pos);
    			}
    			if(event0 instanceof IItemInFluidTickEvent) {
    				if(!world.isRemote) {
        				if(JiuUtils.entity.isEntityInFluid(world, pos)) {
        					((IItemInFluidTickEvent) event0).onItemInFluidTick(eitem, world, pos, world.getBlockState(pos));
        				}
    				}
    			}
    		}
    	}
    	eitems.clear();
    }
}
