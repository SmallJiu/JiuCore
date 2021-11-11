package cat.jiu.core.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import cat.jiu.core.JiuCore;
import cat.jiu.core.api.IJiuEvent;
import cat.jiu.core.api.events.client.player.IPlayerMoveMouseInGameEvent;
import cat.jiu.core.api.events.client.player.IPlayerMoveMouseInGuiEvent;
import cat.jiu.core.api.events.client.player.IPlayerUseKeyboardInGameEvent;
import cat.jiu.core.api.events.client.player.IPlayerUseKeyboardInGuiEvent;
import cat.jiu.core.api.events.client.player.IPlayerUseMouseInGameEvent;
import cat.jiu.core.api.events.client.player.IPlayerUseMouseInGuiEvent;
import cat.jiu.core.api.events.entity.IEntityDeathDropItems;
import cat.jiu.core.api.events.entity.IEntityDeathEvent;
import cat.jiu.core.api.events.entity.IEntityInFluidEvent;
import cat.jiu.core.api.events.entity.IEntityJoinWorldEvent;
import cat.jiu.core.api.events.entity.IEntityJump;
import cat.jiu.core.api.events.entity.IEntityPlaceBlock;
import cat.jiu.core.api.events.entity.IEntityTickEvent;
import cat.jiu.core.api.events.entity.IEntityUseItemFinish;
import cat.jiu.core.api.events.entity.IEntityUseItemStart;
import cat.jiu.core.api.events.entity.IEntityUseItemStop;
import cat.jiu.core.api.events.entity.IEntityUseItemTick;
import cat.jiu.core.api.events.game.IFluidPlaceBlock;
import cat.jiu.core.api.events.game.IOreGen;
import cat.jiu.core.api.events.game.IPlaceFluid;
import cat.jiu.core.api.events.item.IItemInFluidTickEvent;
import cat.jiu.core.api.events.item.IItemInPlayerArmorTick;
import cat.jiu.core.api.events.item.IItemInPlayerInventoryTick;
import cat.jiu.core.api.events.item.IItemInWorldTickEvent;
import cat.jiu.core.api.events.player.IPlayerBreakBlock;
import cat.jiu.core.api.events.player.IPlayerBreakBlockDropItems;
import cat.jiu.core.api.events.player.IPlayerCraftedItemEvent;
import cat.jiu.core.api.events.player.IPlayerDeathEvent;
import cat.jiu.core.api.events.player.IPlayerEatFoodFinish;
import cat.jiu.core.api.events.player.IPlayerEatFoodStart;
import cat.jiu.core.api.events.player.IPlayerEatFoodStop;
import cat.jiu.core.api.events.player.IPlayerEatFoodTick;
import cat.jiu.core.api.events.player.IPlayerInFluidEvent;
import cat.jiu.core.api.events.player.IPlayerJoinWorldEvent;
import cat.jiu.core.api.events.player.IPlayerJump;
import cat.jiu.core.api.events.player.IPlayerLoggedInEvent;
import cat.jiu.core.api.events.player.IPlayerLoggedOutEvent;
import cat.jiu.core.api.events.player.IPlayerPickupEntityItemEvent;
import cat.jiu.core.api.events.player.IPlayerPickupXPEvent;
import cat.jiu.core.api.events.player.IPlayerPlaceBlock;
import cat.jiu.core.api.events.player.IPlayerPlaceFluid;
import cat.jiu.core.api.events.player.IPlayerRespawnEvent;
import cat.jiu.core.api.events.player.IPlayerSmeltedItemEvent;
import cat.jiu.core.api.events.player.IPlayerTickEvent;
import cat.jiu.core.api.events.player.IPlayerUseItemFinish;
import cat.jiu.core.api.events.player.IPlayerUseItemStart;
import cat.jiu.core.api.events.player.IPlayerUseItemStop;
import cat.jiu.core.api.events.player.IPlayerUseItemTick;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.client.event.MouseEvent;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingJumpEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.event.entity.player.PlayerPickupXpEvent;
import net.minecraftforge.event.terraingen.OreGenEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SuppressWarnings("unused")
@EventBusSubscriber
public final class JiuCoreEvents {
	private static final List<IJiuEvent> events = new ArrayList<IJiuEvent>();
	
	/*
	public static <T extends IJiuEvent> void addEvents(T... envs) {
		for(IJiuEvent event : envs) {
			addEvent(event);
		}
	}
	*/
	
	@SuppressWarnings("unchecked")
	public static <T extends IJiuEvent> void addEvent(T[]... envs) {
		for(IJiuEvent[] env : envs) {
			for(IJiuEvent event : env) {
				addEvent(event);
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	public static <T extends IJiuEvent> void addEvent(List<T>... envs) {
		for(List<T> env : envs) {
			for(IJiuEvent event : env) {
				addEvent(event);
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	public static <K, T extends IJiuEvent> void addEventWithMapValues(Map<K, T>... envs) {
		for(Map<K, T> env : envs) {
			for(IJiuEvent event : env.values()) {
				addEvent(event);
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	public static <V, T extends IJiuEvent> void addEventWhitMapKeys(Map<T, V>... envs) {
		for(Map<T, V> env : envs) {
			for(IJiuEvent event : env.keySet()) {
				addEvent(event);
			}
		}
	}
	
	public static <T extends IJiuEvent> void addEvent(T event) {
		events.add(event);
	}
	
	/*
	public static final void removeEvent(IJiuEvent event) {
		events.remove(event);
	}
	*/
	
	public static List<IJiuEvent> getEvents() {
		return JiuUtils.other.copyList(events);
	}
	
	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public static void onKeyUseInGame(InputEvent.KeyInputEvent event) {
		int key = Keyboard.getEventKey();
		
		for(IJiuEvent event0 : events) {
			if(event0 instanceof IPlayerUseKeyboardInGameEvent) {
				((IPlayerUseKeyboardInGameEvent) event0).onPlayerUseKeyboardInGame(key);
			}
		}
	}
	
	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public static void onKeyUseInGui(GuiScreenEvent.KeyboardInputEvent event) {
		GuiScreen gui = event.getGui();
		int key = Keyboard.getEventKey();
		
		for(IJiuEvent event0 : events) {
			if(event0 instanceof IPlayerUseKeyboardInGuiEvent) {
				((IPlayerUseKeyboardInGuiEvent) event0).onPlayerUseKeyboardInGui(gui, key);
			}
		}
	}
	
	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public static void onMouseUseInGame(InputEvent.MouseInputEvent event) {
		int key = Mouse.getEventButton();
		int x = Mouse.getX();
		int y = Mouse.getY();
		
		if(Mouse.getEventButtonState()) {
//			JiuCore.instance.log.info("Game Key:" + key + ", X:" + x + ", Y:" + y);
		}
		if(Mouse.isButtonDown(key)) {
			for(IJiuEvent event0 : events) {
				if(event0 instanceof IPlayerUseMouseInGameEvent) {
					((IPlayerUseMouseInGameEvent) event0).onPlayerUseMouseInGame(key, x, y);
				}
			}
		}else {
			for(IJiuEvent event0 : events) {
				if(event0 instanceof IPlayerMoveMouseInGameEvent) {
					((IPlayerMoveMouseInGameEvent) event0).onPlayerMoveMouseInGame(x, y);
				}
			}
		}
	}
	
	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public static void onMouseKeepUseInGui(MouseEvent event) {
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
	
	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public static void onMouseUseInGui(GuiScreenEvent.MouseInputEvent event) {
		GuiScreen gui = event.getGui();
		int key = Mouse.getEventButton();
		int x = Mouse.getX();
		int y = Mouse.getY();
		
		if(Mouse.getEventButtonState()) {
//			JiuCore.instance.log.info("Gui Key:" + key + ", X:" + x + ", Y:" + y);
		}
		
		if(Mouse.isButtonDown(key)) {
			for(IJiuEvent event0 : events) {
				if(event0 instanceof IPlayerUseMouseInGuiEvent) {
					((IPlayerUseMouseInGuiEvent) event0).onPlayerUseMouseInGui(gui, key, x, y);
				}
				
				if(event0 instanceof IPlayerMoveMouseInGuiEvent) {
					((IPlayerMoveMouseInGuiEvent) event0).onPlayerMoveMouseInGui(gui, x, y);
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
			if(entity instanceof EntityPlayer) {
				EntityPlayer player = (EntityPlayer) entity;
				
				if(event0 instanceof IPlayerTickEvent) {
					((IPlayerTickEvent) event0).onPlayerTick(player, world, pos);
				}
				
				if(event0 instanceof IPlayerInFluidEvent) {
					if(JiuUtils.entity.isEntityInFluid(world, pos)) {
						((IPlayerInFluidEvent) event0).onPlayerInFluidTick(player, world, pos, world.getBlockState(pos));
					}
				}
				
				if(event0 instanceof IItemInPlayerInventoryTick) {
					NonNullList<ItemStack> mainInventory = player.inventory.mainInventory;
					ItemStack mainHand = player.getHeldItemMainhand();
					ItemStack offHand = player.getHeldItemOffhand();
					
					if(!mainInventory.isEmpty()) {
						for(ItemStack invStack : mainInventory) {
							if(!invStack.isEmpty()) {
								int slotId = getSlotFor(mainInventory, invStack);
								((IItemInPlayerInventoryTick) event0).onItemInPlayerInventoryTick(player, invStack, slotId, mainHand, offHand);
							}
						}
					}
				}
				
				if(event0 instanceof IItemInPlayerArmorTick) {
					NonNullList<ItemStack> armorInventory = player.inventory.armorInventory;
					if(!armorInventory.isEmpty()) {
						for(ItemStack invStack : armorInventory) {
							if(!invStack.isEmpty()) {
								int slotId = getSlotFor(armorInventory, invStack);
								EntityEquipmentSlot slot = JiuUtils.item.getArmorSlotForID(slotId);
								
								((IItemInPlayerArmorTick) event0).onItemInPlayerArmorTick(player, invStack, slot);
							}
						}
					}
				}
			}else {
				if(event0 instanceof IEntityInFluidEvent) {
					if(JiuUtils.entity.isEntityInFluid(world, pos)) {
						((IEntityInFluidEvent) event0).onEntityInFluidTick(entity, world, pos, world.getBlockState(pos));
					}
				}
				if(event0 instanceof IEntityTickEvent) {
					((IEntityTickEvent) event0).onEntityTick(entity, world, pos);
				}
				
			}
		}
	}
	
	/**
	 * {@link InventoryPlayer#getSlotFor(ItemStack)}
	 */
    private static int getSlotFor(NonNullList<ItemStack> inv, ItemStack stack) {
        for (int i = 0; i < inv.size(); ++i) {
            if (!((ItemStack)inv.get(i)).isEmpty() && JiuUtils.item.equalsStack(stack, inv.get(i), true, true)){
                return i;
            }
        }
        return -1;
    }
    
    @SubscribeEvent
	public static void onEntityJump(LivingJumpEvent event) {
		EntityLivingBase entity = event.getEntityLiving();
		BlockPos ePos = entity.getPosition();
		World eWorld = entity.getEntityWorld();
		
		for(IJiuEvent event0 : events) {
			if(entity instanceof EntityPlayer) {
				EntityPlayer player = (EntityPlayer) entity;
				if(event0 instanceof IPlayerJump) {
					((IPlayerJump) event0).onPlayerJump(player, ePos, eWorld);
				}
			}else {
				if(event0 instanceof IEntityJump) {
					((IEntityJump) event0).onEntityJump(entity, ePos, eWorld);
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
			if(entity instanceof EntityPlayer) {
				EntityPlayer player = (EntityPlayer) entity;
				
				if(event0 instanceof IPlayerDeathEvent) {
					((IPlayerDeathEvent) event0).onPlayerDeath(player, world, pos);
				}
			}else {
				if(event0 instanceof IEntityDeathEvent) {
					((IEntityDeathEvent) event0).onEntityDeath(entity, world, pos);
				}
			}
		}
	}
	
	@SubscribeEvent
	public static void onEntityDeathDropItems(LivingDropsEvent event) {
		EntityLivingBase entity = event.getEntityLiving();
		DamageSource source = event.getSource();
		List<EntityItem> drops = event.getDrops();
		List<ItemStack> items = new ArrayList<>();;
		int lootingLevel = event.getLootingLevel();
		boolean recentlyHit = event.isRecentlyHit();
		
		for(EntityItem eitem : drops) {
			items.add(eitem.getItem());
		}
		
		for(IJiuEvent event0 : events) {
			if(event0 instanceof IEntityDeathDropItems) {
				((IEntityDeathDropItems) event0).onEntityDeathDropItems(entity, source, drops, items, lootingLevel, recentlyHit);
			}
		}
		items.clear();
	}
	
	@SubscribeEvent
	public static void onEntityUseItemStart(LivingEntityUseItemEvent.Start event) {
		ItemStack stack = event.getItem();
		Entity entity = event.getEntity();
		World world = entity.getEntityWorld();
		BlockPos ePos = entity.getPosition();
		
		for(IJiuEvent event0 : events) {
			if(event.getEntityLiving() instanceof EntityPlayer) {
				EntityPlayer player = (EntityPlayer) entity;
				
				if(stack.getItem() instanceof ItemFood) {
					if(event0 instanceof IPlayerEatFoodStart) {
						((IPlayerEatFoodStart) event0).onPlayerEatFoodStart(stack, player, world, ePos);
					}
				}else {
					if(event0 instanceof IPlayerUseItemStart) {
						((IPlayerUseItemStart) event0).onPlayerUseItemStart(stack, player, world, ePos);
					}
				}
			}else {
				if(event0 instanceof IEntityUseItemStart) {
					((IEntityUseItemStart) event0).onEntityUseItemStart(stack, entity, world, ePos);
				}
			}
		}
	}
	
	@SubscribeEvent
	public static void onEntityUseItemTick(LivingEntityUseItemEvent.Tick event) {
		ItemStack stack = event.getItem();
		Entity entity = event.getEntity();
		World world = entity.getEntityWorld();
		BlockPos ePos = entity.getPosition();
		
		for(IJiuEvent event0 : events) {
			if(event.getEntityLiving() instanceof EntityPlayer) {
				EntityPlayer player = (EntityPlayer) entity;
				
				if(stack.getItem() instanceof ItemFood) {
					if(event0 instanceof IPlayerEatFoodTick) {
						((IPlayerEatFoodTick) event0).onPlayerEatFoodTick(stack, player, world, ePos);
					}
				}else {
					if(event0 instanceof IPlayerUseItemTick) {
						((IPlayerUseItemTick) event0).onPlayerUseItemTick(stack, player, world, ePos);
					}
				}
			}else {
				if(event0 instanceof IEntityUseItemTick) {
					((IEntityUseItemTick) event0).onEntityUseItemTick(stack, entity, world, ePos);
				}
			}
		}
	}
	
	@SubscribeEvent
	public static void onEntityUseItemStop(LivingEntityUseItemEvent.Stop event) {
		ItemStack stack = event.getItem();
		Entity entity = event.getEntity();
		World world = entity.getEntityWorld();
		BlockPos ePos = entity.getPosition();
		
		for(IJiuEvent event0 : events) {
			if(event.getEntityLiving() instanceof EntityPlayer) {
				EntityPlayer player = (EntityPlayer) entity;
				
				if(stack.getItem() instanceof ItemFood) {
					if(event0 instanceof IPlayerEatFoodStop) {
						((IPlayerEatFoodStop) event0).onPlayerEatFoodStop(stack, player, world, ePos);
					}
				}else {
					if(event0 instanceof IPlayerUseItemStop) {
						((IPlayerUseItemStop) event0).onPlayerUseItemStop(stack, player, world, ePos);
					}
				}
			}else {
				if(event0 instanceof IEntityUseItemStop) {
					((IEntityUseItemStop) event0).onEntityUseItemStop(stack, entity, world, ePos);
				}
			}
		}
	}
	
	@SubscribeEvent
	public static void onEntityUseItemFinish(LivingEntityUseItemEvent.Finish event) {
		ItemStack stack = event.getItem();
		Entity entity = event.getEntity();
		World world = entity.getEntityWorld();
		BlockPos ePos = entity.getPosition();
		
		for(IJiuEvent event0 : events) {
			if(event.getEntityLiving() instanceof EntityPlayer) {
				EntityPlayer player = (EntityPlayer) entity;
				
				if(stack.getItem() instanceof ItemFood) {
					if(event0 instanceof IPlayerEatFoodFinish) {
						((IPlayerEatFoodFinish) event0).onPlayerEatFoodFinish(stack, player, world, ePos);
					}
				}else {
					if(event0 instanceof IPlayerUseItemFinish) {
						((IPlayerUseItemFinish) event0).onPlayerUseItemFinish(stack, player, world, ePos);
					}
				}
			}else {
				if(event0 instanceof IEntityUseItemFinish) {
					((IEntityUseItemFinish) event0).onEntityUseItemFinish(stack, entity, world, ePos);
				}
			}
		}
	}
	
	@SubscribeEvent
	public static void onEntityJoinWorld(EntityJoinWorldEvent event) {
		Entity entity = event.getEntity();
		BlockPos pos = entity.getPosition();
		World world = entity.getEntityWorld();
		int dim = entity.dimension;
		
		for(IJiuEvent event0 : events) {
			if(entity instanceof EntityPlayer) {
				if(event0 instanceof IPlayerJoinWorldEvent) {
					EntityPlayer player = (EntityPlayer) entity;
					((IPlayerJoinWorldEvent) event0).onPlayerJoinWorld(player, world, pos, dim);
				}
			}else {
				if(event0 instanceof IEntityJoinWorldEvent) {
					((IEntityJoinWorldEvent) event0).onEntityJoinWorld(entity, world, pos, dim);
				}
			}
		}
	}
	
	@SubscribeEvent
	public static void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
		EntityPlayer player = event.player;
		World world = player.getEntityWorld();
		BlockPos pos = player.getPosition();
		int dim = player.dimension;
		
		for(IJiuEvent event0 : events) {
			if(event0 instanceof IPlayerLoggedInEvent) {
				((IPlayerLoggedInEvent) event0).onPlayerLoggedIn(player, world, pos, dim);
			}
		}
	}
	
	@SubscribeEvent
	public static void onPlayerLoggedOut(PlayerEvent.PlayerLoggedOutEvent event) {
		EntityPlayer player = event.player;
		World world = player.getEntityWorld();
		BlockPos pos = player.getPosition();
		int dim = player.dimension;
		
		for(IJiuEvent event0 : events) {
			if(event0 instanceof IPlayerLoggedOutEvent) {
				((IPlayerLoggedOutEvent) event0).onPlayerLoggedOut(player, world, pos, dim);
			}
		}
	}
	
	@SubscribeEvent
	public static void onPlayerSmeltedItem(PlayerEvent.ItemSmeltedEvent event) {
		EntityPlayer player = event.player;
		World world = player.getEntityWorld();
		BlockPos ePos = player.getPosition();
		ItemStack stack = event.smelting;
		
		for(IJiuEvent event0 : events) {
			if(event0 instanceof IPlayerSmeltedItemEvent) {
				((IPlayerSmeltedItemEvent) event0).onPlayerSmeltedItem(player, stack, world, ePos);
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
		int xps = event.getOrb().getXpValue();
		
		for(IJiuEvent event0 : events) {
			if(event0 instanceof IPlayerPickupXPEvent) {
				((IPlayerPickupXPEvent) event0).onPlayerPickupXP(player, xps, world, pos);
			}
		}
		event.getOrb().xpValue = xps;
    }
    
    @SubscribeEvent
    public static void onPlayerPickupEntityItem(EntityItemPickupEvent event) {
    	EntityPlayer player = event.getEntityPlayer();
		BlockPos pos = player.getPosition();
		World world = player.getEntityWorld();
		EntityItem eitem = event.getItem();
		
		for(IJiuEvent event0 : events) {
			if(event0 instanceof IPlayerPickupEntityItemEvent) {
				((IPlayerPickupEntityItemEvent) event0).onPlayerPickupEntityItem(player, eitem, world, pos);
			}
		}
    }
    
    @SubscribeEvent
    public static void onOreGenerate(OreGenEvent event) {
    	World world = event.getWorld();
    	BlockPos pos = event.getPos();
    	Random rand = event.getRand();
    	IBlockState state = world.getBlockState(pos);
    	
    	for(IJiuEvent event0 : events) {
			if(event0 instanceof IOreGen) {
				((IOreGen) event0).onOreGenerate(world, pos, state, rand);
				world.setBlockState(pos, state);
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
    
    @SubscribeEvent
    public static void onEntityPlaceBlock(BlockEvent.EntityPlaceEvent event) {
    	Entity entity = event.getEntity();
    	IBlockState placedBlock = event.getPlacedBlock();
    	IBlockState placeedAgainst = event.getPlacedAgainst();
    	BlockPos pos = event.getPos();
    	World world = event.getWorld();
    	
    	for(IJiuEvent event0 : events) {
    		if(entity instanceof EntityPlayer) {
    			EntityPlayer player = (EntityPlayer) entity;
        		if(event0 instanceof IPlayerPlaceBlock) {
        			((IPlayerPlaceBlock) event0).onPlayerPlaceBlock(player, pos, world, placedBlock, placeedAgainst);
        		}
        		if(JiuUtils.item.isFluid(placedBlock)) {
        			if(event0 instanceof IPlayerPlaceFluid) {
        				((IPlayerPlaceFluid) event0).onPlayerPlaceFluid(player, pos, world, placedBlock, placeedAgainst);
        			}
        		}
        	}else {
        		if(event0 instanceof IEntityPlaceBlock) {
        			((IEntityPlaceBlock) event0).onEntityPlaceBlock(entity, pos, world, placedBlock, placeedAgainst);
        		}
        	}
    	}
    }
    
    @SubscribeEvent
    public static void onPlayerBreakBlock(BlockEvent.BreakEvent event) {
    	EntityPlayer player = event.getPlayer();
    	IBlockState state = event.getState();
    	BlockPos pos = event.getPos();
    	World world = event.getWorld();
    	int exps = event.getExpToDrop();
    	
    	for(IJiuEvent event0 : events) {
    		if(event0 instanceof IPlayerBreakBlock) {
    			((IPlayerBreakBlock) event0).onPlayerBreakBlock(player, world, pos, state, exps);
    		}
    	}
    	
    	event.setExpToDrop(exps);
    }
    
    @SubscribeEvent
    public static void onPlayerBreakBlockDropItems(BlockEvent.HarvestDropsEvent event) {
    	EntityPlayer player = event.getHarvester();
    	IBlockState state = event.getState();
    	BlockPos pos = event.getPos();
    	World world = event.getWorld();
    	List<ItemStack> drops = event.getDrops();
    	float dropChance = event.getDropChance();
    	
    	for(IJiuEvent event0 : events) {
    		if(event0 instanceof IPlayerBreakBlockDropItems) {
    			((IPlayerBreakBlockDropItems) event0).onPlayerBreakBlockDropItems(player, world, pos, state, drops, dropChance);
    		}
    	}
    	
    	event.setDropChance(dropChance);
    }
    
    @SubscribeEvent
    public static void onPlaceFluid(BlockEvent.CreateFluidSourceEvent event) {
    	IBlockState state = event.getState();
    	BlockPos pos = event.getPos();
    	World world = event.getWorld();
    	
    	for(IJiuEvent event0 : events) {
    		if(event0 instanceof IPlaceFluid) {
    			((IPlaceFluid) event0).onPlaceFluid(pos, world, state);
    		}
    	}
    }
    
    @SubscribeEvent
    public static void onFluidPlaceBlock(BlockEvent.FluidPlaceBlockEvent event) {
    	BlockPos pos = event.getPos();
    	World world = event.getWorld();
    	IBlockState newState = event.getNewState();
    	IBlockState oldState = event.getOriginalState();
    	
    	for(IJiuEvent event0 : events) {
    		if(event0 instanceof IFluidPlaceBlock) {
    			((IFluidPlaceBlock) event0).onFluidPlaceBlock(world, pos, newState, oldState);
    		}
    	}
    	
    	event.setNewState(newState);
    }
}
