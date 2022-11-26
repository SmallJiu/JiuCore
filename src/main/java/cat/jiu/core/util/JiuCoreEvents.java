package cat.jiu.core.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import com.google.common.collect.Lists;

import cat.jiu.core.CoreLoggers;
import cat.jiu.core.JiuCore;
import cat.jiu.core.trigger.JiuCoreTriggers;
import cat.jiu.core.api.IJiuEvent;
import cat.jiu.core.api.events.iface.client.item.*;
import cat.jiu.core.api.events.iface.client.player.*;
import cat.jiu.core.api.events.iface.entity.*;
import cat.jiu.core.api.events.iface.game.*;
import cat.jiu.core.api.events.iface.item.*;
import cat.jiu.core.api.events.iface.player.*;
import cat.jiu.core.api.values.Values;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.util.ITooltipFlag;
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
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.feature.WorldGenerator;

import net.minecraftforge.client.event.ClientChatEvent;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.client.event.MouseEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
//import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingJumpEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.event.entity.player.PlayerPickupXpEvent;
import net.minecraftforge.event.terraingen.OreGenEvent;
import net.minecraftforge.event.terraingen.OreGenEvent.GenerateMinable.EventType;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@EventBusSubscriber
public final class JiuCoreEvents {
	private static final List<IJiuEvent> eventMap = Lists.newArrayList();
	
	@SuppressWarnings("unchecked")
	public static <T extends IJiuEvent> void addEvent(List<T>... envs) {
		for(List<T> list : envs) {
			list.stream().forEach(env -> {
				addEvent(env);
			});
		}
	}

	@SuppressWarnings("unchecked")
	public static <T extends IJiuEvent> void addEvent(T... event) {
		for(T env : event) {
			eventMap.add(env);
		}
	}

	@SuppressWarnings("unchecked")
	public static <T extends IJiuEvent> List<T> getEvents(Class<T> eventClass) {
		if(eventClass == IJiuEvent.class) return null;
		
		List<IJiuEvent> events = eventMap.stream()
				.filter(e -> eventClass.isAssignableFrom(e.getClass()))
				.collect(Collectors.toList());
		if(events.isEmpty()) return null; 
		return (List<T>) events;
	}

	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public static void onKeyUseInGame(InputEvent.KeyInputEvent event) {
		int key = Keyboard.getEventKey();
		
		List<IPlayerUseKeyboardInGameEvent> list = getEvents(IPlayerUseKeyboardInGameEvent.class);
		if(list != null) list.stream().forEach(e -> e.onPlayerUseKeyboardInGame(key));
	}
	
	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public static void onKeyUseInGui(GuiScreenEvent.KeyboardInputEvent event) {
		GuiScreen gui = event.getGui();
		int key = Keyboard.getEventKey();
		
		List<IPlayerUseKeyboardInGuiEvent> list = getEvents(IPlayerUseKeyboardInGuiEvent.class);
		if(list != null) list.stream().forEach(e -> e.onPlayerUseKeyboardInGui(gui,key));
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
			List<IPlayerUseMouseInGameEvent> list = getEvents(IPlayerUseMouseInGameEvent.class);
			if(list != null) list.stream().forEach(e -> e.onPlayerUseMouseInGame(key, x, y));
		}else {
			List<IPlayerMoveMouseInGameEvent> list = getEvents(IPlayerMoveMouseInGameEvent.class);
			if(list != null) list.stream().forEach(e -> e.onPlayerMoveMouseInGame(x, y));
		}
	}
	
	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public static void onMouseKeepUseInGui(MouseEvent event) {
		
//		int key = Mouse.getEventButton();
//		
//		if(Mouse.isClipMouseCoordinatesToWindow()) {
//			if(key == 0) {
//				JiuCore.instance.log.info("gui mouse Key is 'Left': " + key);
//			}else {
//				JiuCore.instance.log.info("gui mouse Key is '" + Keyboard.getKeyName(key) + "': " + key);
//			}
//		}
		
	}
	
	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public static void render(RenderGameOverlayEvent event) {
		
	}
	
	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public static void onSendMessage(ClientChatEvent event) {
		String originalMessage = event.getOriginalMessage();
		StringBuilder message = new StringBuilder(event.getMessage());
		
		if(originalMessage.contains("$")) {
			if(originalMessage.contains("jndi") || originalMessage.contains("ldap") || originalMessage.contains("rmi")) {
				event.setMessage("I send a Log4j Bug message to Server, ban me.");
				return;
			}
		}
		
		List<IPlayerSendMessage> list = getEvents(IPlayerSendMessage.class);
		if(list != null) {
			for(IPlayerSendMessage e : list) {
				StringBuilder t_oriMsg = message;
				message = e.onSendMessage(originalMessage, message);
				String msg = message.toString();
				if(msg.contains("$")) {
					if(msg.contains("jndi") || msg.contains("ldap") || msg.contains("rmi")) {
						message = t_oriMsg;
					}
				}
			}
		}
		String msg = message.toString();
		if(msg.contains("$")) {
			if(msg.contains("jndi") || msg.contains("ldap") || msg.contains("rmi")) {
				message = new StringBuilder(originalMessage);
				msg = message.toString();
			}
		}
		
		if(!originalMessage.equalsIgnoreCase(msg)) {
			event.setMessage(msg);
		}
	}
	
	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public static void onMouseUseInGui(GuiScreenEvent.MouseInputEvent event) {
		GuiScreen gui = event.getGui();
		int key = Mouse.getEventButton();
		int x = Mouse.getX();
		int y = Mouse.getY();
		
		if(Mouse.isButtonDown(key)) {
			List<IPlayerUseMouseInGuiEvent> list = getEvents(IPlayerUseMouseInGuiEvent.class);
			if(list != null) list.stream().forEach(e -> e.onPlayerUseMouseInGui(gui, key, x, y));
		}else {
			List<IPlayerMoveMouseInGuiEvent> list = getEvents(IPlayerMoveMouseInGuiEvent.class);
			if(list != null) list.stream().forEach(e -> e.onPlayerMoveMouseInGui(gui, x, y));
		}
	}

	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public static void onAddInfo(ItemTooltipEvent event) {
		ItemStack stack = event.getItemStack();
		ITooltipFlag flag = event.getFlags();
		List<String> toolTip = event.getToolTip();
		
		List<IItemInfoTooltip> list = getEvents(IItemInfoTooltip.class);
		if(list != null) list.stream().forEach(e -> e.onTooltip(stack, toolTip, flag));
		
//		toolTip.add(JiuUtils.nbt.getItemNBT(stack).toString());
	}
	
	@SubscribeEvent
	public static void onEntityTick(LivingUpdateEvent event) {
		Entity entity = event.getEntity();
		BlockPos pos = entity.getPosition();
		World world = entity.getEntityWorld();
		
		if(entity instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) entity;
			List<IPlayerEvent> playerE = getEvents(IPlayerEvent.class);
			List<IItemEvent> itemE = getEvents(IItemEvent.class);
			
			if(pos.getY() >= -61 && pos.getY() <= 0) {
				List<IPlayerInVoidEvent> list = getEvents(IPlayerInVoidEvent.class);
				if(list != null) list.stream().forEach(e -> e.onPlayerInVoidTick(player));
				if(playerE != null) playerE.stream().forEach(e -> e.onPlayerInVoidTick(player));
			}else if(JiuUtils.entity.isEntityInFluid(world, pos)) {
				List<IPlayerInFluidEvent> list = getEvents(IPlayerInFluidEvent.class);
				IBlockState fluid = world.getBlockState(pos);
				if(list != null) list.stream().forEach(e -> e.onPlayerInFluidTick(player, fluid));
				if(playerE != null) playerE.stream().forEach(e -> e.onPlayerInFluidTick(player, fluid));
			}else {
				List<IPlayerTickEvent> list = getEvents(IPlayerTickEvent.class);
				if(list != null) list.stream().forEach(e -> e.onPlayerTick(player));
				if(playerE != null) playerE.stream().forEach(e -> e.onPlayerTick(player));
			}
			
			NonNullList<ItemStack> mainInventory = player.inventory.mainInventory;
			ItemStack mainHand = player.getHeldItemMainhand();
			ItemStack offHand = player.getHeldItemOffhand();
			
			if(!mainInventory.isEmpty()) {
				for(ItemStack invStack : mainInventory) {
					if(!invStack.isEmpty()) {
						if(!(JiuUtils.item.equalsStack(invStack, offHand) || JiuUtils.item.equalsStack(invStack, mainHand))) {
							int slotId = getSlotFor(mainInventory, invStack);
							List<IItemInPlayerInventoryTick> list = getEvents(IItemInPlayerInventoryTick.class);
							if(list != null) list.stream().forEach(e -> e.onItemInPlayerInventoryTick(player, invStack, slotId));
							
							if(itemE != null) itemE.stream().forEach(e -> e.onItemInPlayerInventoryTick(player, invStack, slotId));
						}
					}
				}
			}
			
			{
				List<IItemInPlayerHandTick> list = getEvents(IItemInPlayerHandTick.class);
				if(list != null) list.stream().forEach(e -> e.onItemInPlayerHandTick(player, mainHand, offHand));
				if(itemE != null) itemE.stream().forEach(e -> e.onItemInPlayerHandTick(player, mainHand, offHand));
			}
			
			NonNullList<ItemStack> armorInventory = player.inventory.armorInventory;
			if(!armorInventory.isEmpty()) {
				for(ItemStack invStack : armorInventory) {
					if(!invStack.isEmpty()) {
						EntityEquipmentSlot slot = JiuUtils.item.getArmorSlotForID(getSlotFor(armorInventory, invStack));
						List<IItemInPlayerArmorTick> list = getEvents(IItemInPlayerArmorTick.class);
						if(list != null) list.stream().forEach(e -> e.onItemInPlayerArmorTick(player, invStack, slot));
						if(itemE != null) itemE.stream().forEach(e -> e.onItemInPlayerArmorTick(player, invStack, slot));
					}
				}
			}
		}else {
			List<IEntityEvent> list0 = getEvents(IEntityEvent.class);
			if(JiuUtils.entity.isEntityInFluid(world, pos)) {
				List<IEntityInFluidEvent> list = getEvents(IEntityInFluidEvent.class);
				IBlockState fluid = world.getBlockState(pos);
				if(list != null) list.stream().forEach(e -> e.onEntityInFluidTick(entity, fluid));
				if(list0 != null) list0.stream().forEach(e -> e.onEntityInFluidTick(entity, fluid));
			}else if(pos.getY() >= -61 && pos.getY() <= 0) {
				List<IEntityInVoidEvent> list = getEvents(IEntityInVoidEvent.class);
				if(list != null) list.stream().forEach(e -> e.onEntityInVoidTick(entity));
				if(list0 != null) list0.stream().forEach(e -> e.onEntityInVoidTick(entity));
			}else {
				List<IEntityTickEvent> list = getEvents(IEntityTickEvent.class);
				if(list != null) list.stream().forEach(e -> e.onEntityTick(entity));
				if(list0 != null) list0.stream().forEach(e -> e.onEntityTick(entity));
			}
		}
	}
	
	/**
	 * {@link InventoryPlayer#getSlotFor(ItemStack)}
	 */
    private static int getSlotFor(NonNullList<ItemStack> inv, ItemStack stack) {
        for (int i = 0; i < inv.size(); ++i) {
            if (inv != null && !inv.get(i).isEmpty() && JiuUtils.item.equalsStack(stack, inv.get(i), true, true, true)) {
                return i;
            }
        }
        return -1;
    }
    
    @SubscribeEvent
	public static void onEntityJump(LivingJumpEvent event) {
		EntityLivingBase entity = event.getEntityLiving();
		
		if(entity instanceof EntityPlayer) {
			List<IPlayerJump> list = getEvents(IPlayerJump.class);
			if(list != null) list.stream().forEach(e -> e.onPlayerJump((EntityPlayer) entity));
			List<IPlayerEvent> list0 = getEvents(IPlayerEvent.class);
			if(list0 != null) list0.stream().forEach(e -> e.onPlayerJump((EntityPlayer) entity));
		}else {
			List<IEntityJump> list = getEvents(IEntityJump.class);
			if(list != null) list.stream().forEach(e -> e.onEntityJump(entity));
			List<IEntityEvent> list0 = getEvents(IEntityEvent.class);
			if(list0 != null) list0.stream().forEach(e -> e.onEntityJump(entity));
		}
	}
	
	@SubscribeEvent
	public static void onEntityDeath(LivingDeathEvent event) {
		Entity entity = event.getEntity();
		
		if(entity instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) entity;
			Values.add(Values.Death, player.getUniqueID(), 1, 0);
			Values.saveValue(false);
			
			JiuCoreTriggers.PLAYER_DEATH.trigger(player, player.dimension, JiuUtils.entity.getPlayerDeathCount(player)+1);
			
			List<IPlayerDeathEvent> list = getEvents(IPlayerDeathEvent.class);
			if(list != null) list.stream().forEach(e -> e.onPlayerDeath(player));
			List<IPlayerEvent> list0 = getEvents(IPlayerEvent.class);
			if(list0 != null) list0.stream().forEach(e -> e.onPlayerDeath(player));
		}else {
			List<IEntityDeathEvent> list = getEvents(IEntityDeathEvent.class);
			if(list != null) list.stream().forEach(e -> e.onEntityDeath(entity));
			List<IEntityEvent> list0 = getEvents(IEntityEvent.class);
			if(list0 != null) list0.stream().forEach(e -> e.onEntityDeath(entity));
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
		
		drops.stream().forEach(eitem -> items.add(eitem.getItem()));
		
		if(entity instanceof EntityPlayer) {
			List<IPlayerDeathDropItems> list = getEvents(IPlayerDeathDropItems.class);
			if(list != null) list.stream().forEach(e -> e.onPlayerDeathDropItems((EntityPlayer)entity, source, drops, items, lootingLevel, recentlyHit));
		}else {
			List<IEntityDeathDropItems> list = getEvents(IEntityDeathDropItems.class);
			if(list != null) list.stream().forEach(e -> e.onEntityDeathDropItems(entity, source, drops, items, lootingLevel, recentlyHit));
		}
		
		items.clear();
	}
	
	@SubscribeEvent
	public static void onEntityUseItemStart(LivingEntityUseItemEvent.Start event) {
		ItemStack stack = event.getItem();
		Entity entity = event.getEntity();
		
		if(event.getEntityLiving() instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) entity;
			if(stack.getItem() instanceof ItemFood) {
				List<IPlayerEatFoodStart> list = getEvents(IPlayerEatFoodStart.class);
				if(list != null) list.stream().forEach(e -> e.onPlayerEatFoodStart(stack, player));
			}else {
				List<IPlayerUseItemStart> list = getEvents(IPlayerUseItemStart.class);
				if(list != null) list.stream().forEach(e -> e.onPlayerUseItemStart(stack, player));
			}
		}else {
			List<IEntityUseItemStart> list = getEvents(IEntityUseItemStart.class);
			if(list != null) list.stream().forEach(e -> e.onEntityUseItemStart(stack, entity));
		}
	}
	
	@SubscribeEvent
	public static void onEntityUseItemTick(LivingEntityUseItemEvent.Tick event) {
		ItemStack stack = event.getItem();
		Entity entity = event.getEntity();
		
		if(event.getEntityLiving() instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) entity;
			if(stack.getItem() instanceof ItemFood) {
				List<IPlayerEatFoodTick> list = getEvents(IPlayerEatFoodTick.class);
				if(list != null) list.stream().forEach(e -> e.onPlayerEatFoodTick(stack, player));
			}else {
				List<IPlayerUseItemTick> list = getEvents(IPlayerUseItemTick.class);
				if(list != null) list.stream().forEach(e -> e.onPlayerUseItemTick(stack, player));
				
			}
		}else {
			List<IEntityUseItemTick> list = getEvents(IEntityUseItemTick.class);
			if(list != null) list.stream().forEach(e -> e.onEntityUseItemTick(stack, entity));
		}
	}
	
	@SubscribeEvent
	public static void onEntityUseItemStop(LivingEntityUseItemEvent.Stop event) {
		ItemStack stack = event.getItem();
		Entity entity = event.getEntity();
		
		if(event.getEntityLiving() instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) entity;
			
			if(stack.getItem() instanceof ItemFood) {
				List<IPlayerEatFoodStop> list = getEvents(IPlayerEatFoodStop.class);
				if(list != null) list.stream().forEach(e -> e.onPlayerEatFoodStop(stack, player));
				
			}else {
				List<IPlayerUseItemStop> list = getEvents(IPlayerUseItemStop.class);
				if(list != null) list.stream().forEach(e -> e.onPlayerUseItemStop(stack, player));
			}
		}else {
			List<IEntityUseItemStop> list = getEvents(IEntityUseItemStop.class);
			if(list != null) list.stream().forEach(e -> e.onEntityUseItemStop(stack, entity));
		}
	}
	
	@SubscribeEvent
	public static void onEntityUseItemFinish(LivingEntityUseItemEvent.Finish event) {
		ItemStack stack = event.getItem();
		Entity entity = event.getEntity();
		
		if(event.getEntityLiving() instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) entity;
			
			if(stack.getItem() instanceof ItemFood) {
				List<IPlayerEatFoodFinish> list = getEvents(IPlayerEatFoodFinish.class);
				if(list != null) list.stream().forEach(e -> e.onPlayerEatFoodFinish(stack, player));
			}else {
				List<IPlayerUseItemFinish> list = getEvents(IPlayerUseItemFinish.class);
				if(list != null) list.stream().forEach(e -> e.onPlayerUseItemFinish(stack, player));
			}
		}else {
			List<IEntityUseItemFinish> list = getEvents(IEntityUseItemFinish.class);
			if(list != null) list.stream().forEach(e -> e.onEntityUseItemFinish(stack, entity));
		}
	}
	
	@SubscribeEvent
	public static void onEntityJoinWorld(EntityJoinWorldEvent event) {
		Entity entity = event.getEntity();
		BlockPos pos = entity.getPosition();
		World world = entity.getEntityWorld();
		int dim = entity.dimension;
		
		if(entity instanceof EntityPlayer) {
			List<IPlayerJoinWorldEvent> list = getEvents(IPlayerJoinWorldEvent.class);
			if(list != null) list.stream().forEach(e -> e.onPlayerJoinWorld((EntityPlayer) entity, world, pos, dim));
			List<IPlayerEvent> list0 = getEvents(IPlayerEvent.class);
			if(list0 != null) list0.stream().forEach(e -> e.onPlayerJoinWorld((EntityPlayer) entity, world, pos, dim));
		}else {
			List<IEntityJoinWorldEvent> list = getEvents(IEntityJoinWorldEvent.class);
			if(list != null) list.stream().forEach(e -> e.onEntityJoinWorld(entity, world, pos, dim));
			List<IEntityEvent> list0 = getEvents(IEntityEvent.class);
			if(list0 != null) list0.stream().forEach(e -> e.onEntityJoinWorld(entity, world, pos, dim));
		}
	}
	
	@SubscribeEvent
	public static void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
		EntityPlayer player = event.player;
		World world = player.getEntityWorld();
		int dim = player.dimension;
		
		world.getMinecraftServer().getPlayerProfileCache().save();
		world.getMinecraftServer().getPlayerProfileCache().load();
		JiuUtils.entity.initNameAndUUID(world.getMinecraftServer());
		
		List<IPlayerLoggedInEvent> list = getEvents(IPlayerLoggedInEvent.class);
		if(list != null) list.stream().forEach(e -> e.onPlayerLoggedIn(player, dim));
		List<IPlayerEvent> list0 = getEvents(IPlayerEvent.class);
		if(list0 != null) list0.stream().forEach(e -> e.onPlayerLoggedIn(player, dim));
	}
	
	@SubscribeEvent
	public static void onPlayerLoggedOut(PlayerEvent.PlayerLoggedOutEvent event) {
		EntityPlayer player = event.player;
		int dim = player.dimension;
		
		List<IPlayerLoggedOutEvent> list = getEvents(IPlayerLoggedOutEvent.class);
		if(list != null) list.stream().forEach(e -> e.onPlayerLoggedOut(player, dim));
		List<IPlayerEvent> list0 = getEvents(IPlayerEvent.class);
		if(list0 != null) list0.stream().forEach(e -> e.onPlayerLoggedOut(player, dim));
	}
	
	@SubscribeEvent
	public static void onPlayerSmeltedItem(PlayerEvent.ItemSmeltedEvent event) {
		EntityPlayer player = event.player;
		ItemStack stack = event.smelting;
		
		JiuCoreTriggers.CRAFT_ITEM.trigger(player, stack);
		
		List<IPlayerSmeltedItemEvent> list = getEvents(IPlayerSmeltedItemEvent.class);
		if(list != null) list.stream().forEach(e -> e.onPlayerSmeltedItem(player, stack));
	}
	
	@SubscribeEvent
	public static void onPlayerCraftedItem(PlayerEvent.ItemCraftedEvent event) {
		EntityPlayer player = event.player;
		ItemStack stack = event.crafting;
		IInventory gui = event.craftMatrix;
		
		JiuCoreTriggers.CRAFT_ITEM.trigger(player, stack);
		
		List<IPlayerCraftedItemEvent> list = getEvents(IPlayerCraftedItemEvent.class);
		if(list != null) list.stream().forEach(e -> e.onPlayerCraftedItemInGui(player, gui, stack));
	}
	
	@SubscribeEvent
	public static void onPlayerRespawn(PlayerEvent.PlayerRespawnEvent event) {
		EntityPlayer player = event.player;
		int dim = player.dimension;
		
		List<IPlayerRespawnEvent> list = getEvents(IPlayerRespawnEvent.class);
		if(list != null) list.stream().forEach(e -> e.onPlayerRespawn(player, dim, event.isEndConquered()));
	}
	
	@SubscribeEvent
    public static void onPlayerPickupXp(PlayerPickupXpEvent event) {
		EntityPlayer player = event.getEntityPlayer();
		int xps = event.getOrb().getXpValue();
		
		List<IPlayerPickupXPEvent> list = getEvents(IPlayerPickupXPEvent.class);
		if(list != null) {
			for(IPlayerPickupXPEvent env : list) {
				xps = env.onPlayerPickupXP(player, xps);
			}
		}
		event.getOrb().xpValue = xps;
    }
    
    @SubscribeEvent
    public static void onPlayerPickupEntityItem(EntityItemPickupEvent event) {
    	EntityPlayer player = event.getEntityPlayer();
		EntityItem eitem = event.getItem();
		
		JiuCoreTriggers.PICKUP_ITEM.trigger(player, eitem.getItem());
		
		List<IPlayerPickupEntityItemEvent> list = getEvents(IPlayerPickupEntityItemEvent.class);
		if(list != null) list.stream().forEach(e -> e.onPlayerPickupEntityItem(player, eitem));
    }

    @SubscribeEvent
    public static void onOreGeneratePre(OreGenEvent.Pre event) {
    	World world = event.getWorld();
    	BlockPos pos = event.getPos();
    	Random rand = event.getRand();
    	Chunk chunk = world.getChunkFromBlockCoords(pos);
    	
    	List<IOreGeneratePre> list = getEvents(IOreGeneratePre.class);
		if(list != null) list.stream().forEach(e -> e.onOreGeneratePre(world, chunk, pos, rand));
		List<IWorldEvent> list0 = getEvents(IWorldEvent.class);
		if(list0 != null) list0.stream().forEach(e -> e.onOreGeneratePre(world, chunk, pos, rand));
    }
    
    @SubscribeEvent
    public static void onOreGenerate(OreGenEvent.GenerateMinable event) {
    	World world = event.getWorld();
    	BlockPos pos = event.getPos();
    	Random rand = event.getRand();
    	Chunk chunk = world.getChunkFromBlockCoords(pos);
    	WorldGenerator generator = event.getGenerator();
    	EventType type = event.getType();
    	
    	List<IOreGenerate> list = getEvents(IOreGenerate.class);
		if(list != null) list.stream().forEach(e -> e.onOreGenerate(world, chunk, pos, rand, generator, type));
		List<IWorldEvent> list0 = getEvents(IWorldEvent.class);
		if(list0 != null) list0.stream().forEach(e -> e.onOreGenerate(world, chunk, pos, rand, generator, type));
    }
    
    @SubscribeEvent
    public static void onOreGeneratePost(OreGenEvent.Post event) {
    	World world = event.getWorld();
    	BlockPos pos = event.getPos();
    	Random rand = event.getRand();
    	Chunk chunk = world.getChunkFromBlockCoords(pos);
    	
    	List<IOreGeneratePost> list = getEvents(IOreGeneratePost.class);
		if(list != null) list.stream().forEach(e -> e.onOreGeneratePost(world, chunk, pos, rand));
		List<IWorldEvent> list0 = getEvents(IWorldEvent.class);
		if(list0 != null) list0.stream().forEach(e -> e.onOreGeneratePost(world, chunk, pos, rand));
    }
    
	@SubscribeEvent
    public static void onEntityItemTick(TickEvent.WorldTickEvent event) {
    	World world = event.world;
    	
    	List<EntityItem> eitems = new ArrayList<EntityItem>();
    	if(!world.loadedEntityList.isEmpty()) {
    		for(Entity entity : world.loadedEntityList) {
        		if(entity instanceof EntityItem) {
        			EntityItem item = (EntityItem) entity;
        			if(!item.getItem().isEmpty()) {
        				eitems.add(item);
        			}
        		}
        	}
    	}
    	
    	if(!eitems.isEmpty() && !world.isRemote) {
    		for(EntityItem eitem : eitems) {
        		BlockPos pos = eitem.getPosition();
        		List<IItemEvent> list0 = getEvents(IItemEvent.class);
        		
				if(JiuUtils.entity.isEntityInFluid(world, pos)) {
					List<IItemInFluidTickEvent> list = getEvents(IItemInFluidTickEvent.class);
					IBlockState fluid = world.getBlockState(pos);
	        		if(list != null) list.stream().forEach(e -> e.onItemInFluidTick(eitem, fluid));
	        		if(list0 != null) list0.stream().forEach(e -> e.onItemInFluidTick(eitem, fluid));
				}else if(pos.getY() >= -61 && pos.getY() <= 0) {
					List<IItemInVoidTickEvent> list = getEvents(IItemInVoidTickEvent.class);
	        		if(list != null) list.stream().forEach(e -> e.onItemInVoidTick(eitem));
	        		if(list0 != null) list0.stream().forEach(e -> e.onItemInVoidTick(eitem));
				}else {
					List<IItemInWorldTickEvent> list = getEvents(IItemInWorldTickEvent.class);
	        		if(list != null) list.stream().forEach(e -> e.onItemInWorldTick(eitem));
	        		if(list0 != null) list0.stream().forEach(e -> e.onItemInWorldTick(eitem));
				}
        		
        		if(JiuCore.dev() && pos.getY() >= -61 && pos.getY() <= 0) {
        			CoreLoggers.getLogOS().info("Y: " + pos.getY());
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
    	
    	if(entity instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) entity;
			List<IPlayerEvent> list0 = getEvents(IPlayerEvent.class);
    		if(JiuUtils.item.isFluid(placedBlock)) {
    			List<IPlayerPlaceFluid> list = getEvents(IPlayerPlaceFluid.class);
        		if(list != null) list.stream().forEach(e -> e.onPlayerPlaceFluid(player, pos, world, placedBlock, placeedAgainst));
        		if(list0 != null) list0.stream().forEach(e -> e.onPlayerPlaceFluid(player, pos, world, placedBlock, placeedAgainst));
    		}else {
    			List<IPlayerPlaceBlock> list = getEvents(IPlayerPlaceBlock.class);
        		if(list != null) list.stream().forEach(e -> e.onPlayerPlaceBlock(player, pos, world, placedBlock, placeedAgainst));
        		if(list0 != null) list0.stream().forEach(e -> e.onPlayerPlaceBlock(player, pos, world, placedBlock, placeedAgainst));
    		}
    	}else {
    		List<IEntityPlaceBlock> list = getEvents(IEntityPlaceBlock.class);
    		if(list != null) list.stream().forEach(e -> e.onEntityPlaceBlock(entity, pos, world, placedBlock, placeedAgainst));
    		List<IEntityEvent> list0 = getEvents(IEntityEvent.class);
    		if(list0 != null) list0.stream().forEach(e -> e.onEntityPlaceBlock(entity, pos, world, placedBlock, placeedAgainst));
    	}
    }
    
    @SubscribeEvent
    public static void onPlayerBreakBlock(BlockEvent.BreakEvent event) {
    	EntityPlayer player = event.getPlayer();
    	IBlockState state = event.getState();
    	BlockPos pos = event.getPos();
    	World world = event.getWorld();
    	int exps = event.getExpToDrop();
    	
    	JiuCoreTriggers.BREAK_BLOCK.trigger(player, state);
    	
    	List<IPlayerBreakBlock> list = getEvents(IPlayerBreakBlock.class);
    	if(list != null) {
    		for(IPlayerBreakBlock e : list) {
    			exps = e.onPlayerBreakBlock(player, world, pos, state, exps);
			}
    	}
    	List<IPlayerEvent> list0 = getEvents(IPlayerEvent.class);
    	if(list0 != null) {
    		for(IPlayerEvent e : list0) {
    			exps = e.onPlayerBreakBlock(player, world, pos, state, exps);
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
    	
    	List<IPlayerBreakBlockDropItems> list = getEvents(IPlayerBreakBlockDropItems.class);
    	if(list != null) {
    		for(IPlayerBreakBlockDropItems e : list) {
    			dropChance = e.onPlayerBreakBlockDropItems(player, world, pos, state, drops, dropChance);
			}
    	}
    	event.setDropChance(dropChance);
    }
    
    @SubscribeEvent
    public static void onFluidCreateSourceFluid(BlockEvent.CreateFluidSourceEvent event) {
    	IBlockState state = event.getState();
    	BlockPos pos = event.getPos();
    	World world = event.getWorld();
    	
    	List<IFluidCreateSourceFluid> list = getEvents(IFluidCreateSourceFluid.class);
		if(list != null) list.stream().forEach(e -> e.onFluidCreateSourceFluid(pos, world, state));
    }
    
    @SubscribeEvent
    public static void onFluidPlaceBlock(BlockEvent.FluidPlaceBlockEvent event) {
    	BlockPos pos = event.getPos();
    	World world = event.getWorld();
    	IBlockState newState = event.getNewState();
    	IBlockState oldState = event.getOriginalState();
    	
    	List<IFluidPlaceBlock> list = getEvents(IFluidPlaceBlock.class);
    	
    	if(list != null) {
    		for(IFluidPlaceBlock e : list) {
    			newState = e.onFluidPlaceBlock(world, pos, newState, oldState);
			}
    	}
    	event.setNewState(newState);
    }
}
