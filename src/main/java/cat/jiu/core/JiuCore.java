package cat.jiu.core;

import java.io.File;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import cat.jiu.core.api.IMod;
import cat.jiu.core.events.game.GameShutdownEvent;
import cat.jiu.core.events.game.InFluidCraftingEvent;
import cat.jiu.core.api.values.Values;
import cat.jiu.core.capability.CapabilityJiuEnergy;
import cat.jiu.core.commands.CommandJiuCore;
import cat.jiu.core.test.Init;
import cat.jiu.core.trigger.JiuCoreTriggers;
import cat.jiu.core.util.EntityDeathDrops;
import cat.jiu.core.util.JiuUtils;
import cat.jiu.core.util.base.BaseCreativeTab;
import cat.jiu.core.util.base.BaseNBT;
import cat.jiu.core.util.crafting.AnvilRecipe;
import cat.jiu.core.util.crafting.Recipes;
import cat.jiu.core.util.mc.NBTTagNull;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.event.FMLLoadCompleteEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod(
	modid = JiuCore.MODID,
	name = JiuCore.NAME,
	version = JiuCore.VERSION,
	dependencies = "required-after:mixinbooter",
	useMetadata = true
)
@EventBusSubscriber
public class JiuCore implements IMod {
	public static final String MODID = "jiucore";
	public static final String NAME = "JiuCore";
	public static final String OWNER = "small_jiu";
	public static final String VERSION = "1.1.6-a0";

	private static Boolean isDev; // if is IDE, you can set to 'true' to enable some test stuff
	public static final boolean dev() {
		if(isDev == null) {
			isDev = new File("./config/jiu/core_debug.jiu").exists();
		}
		return isDev;
	}

	public static final CreativeTabs CORE = dev() ? new BaseCreativeTab("core_test_tab", new ItemStack(Items.DIAMOND), false) : null;

	@Mod.Instance(
		value = JiuCore.MODID,
		owner = JiuCore.OWNER)
	public static JiuCore instance;
	public JiuCore() {
		CoreLoggers.getLogOS().info("Constructors start.");
		
		Runtime.getRuntime().addShutdownHook(new Thread(() -> {
			try {
				MinecraftForge.EVENT_BUS.post(new GameShutdownEvent(proxy.getSide()));
				Values.saveValue(true);
			}catch(Exception e) {
				e.printStackTrace();
			}
		},"Shutdown Thread"));
		
		FluidRegistry.enableUniversalBucket();
		try {
			Block.class.getDeclaredField("lightValue");
			Item.class.getDeclaredField("maxDamage");
			CoreLoggers.getLogOS().info("Is Dev.");
		}catch(Throwable e) {CoreLoggers.getLogOS().info("Is not Dev.");}
		CoreLoggers.getLogOS().info("Constructors end.");
	}
	
	@SidedProxy(
		clientSide = "cat.jiu.core.proxy.ClientProxy",
		serverSide = "cat.jiu.core.proxy.ServerProxy")
	public static cat.jiu.core.proxy.ServerProxy proxy;
	@Override
	public cat.jiu.core.proxy.ServerProxy getProxy() {
		return proxy;
	}
	
	@Mod.EventHandler
	public void onPreInit(FMLPreInitializationEvent event) {
		IMod.super.onPreInit(event);
		CoreLoggers.getLogOS().info("PreInit start.");
		System.out.println(proxy);
		CapabilityJiuEnergy.register();
		JiuUtils.entity.initNameAndUUID(null);
		
		Values.loadFromFile();
		Values.addValue(Values.Coin);
		Values.addValue(Values.Death);
		
		BaseNBT.register(-1, NBTTagNull.class, "TAG_Null", "NULL");
		
		JiuCoreTriggers.register();

		if(dev()) {
			new Init();
			AnvilRecipe.addAnvilRecipe(new ItemStack(Items.APPLE, 5), new ItemStack(Blocks.GOLD_BLOCK), new ItemStack(Blocks.DIAMOND_BLOCK, 5), 5);
		}
		CoreLoggers.getLogOS().info("PreInit end.");
	}

	@Mod.EventHandler
	public void onLoadComplete(FMLLoadCompleteEvent event) {
		IMod.super.onLoadComplete(event);
		MinecraftForge.EVENT_BUS.post(new InFluidCraftingEvent(new Recipes(MODID)));
	}
	
	@SubscribeEvent
	public static void onAddInFluidCrafting(InFluidCraftingEvent event) {
		if(dev()) {
			event.util.addInFluidCrafting(new ItemStack(Init.TestBlock), new ItemStack[]{new ItemStack(Items.PAPER, 1)});
			event.util.addInFluidCrafting(new ItemStack(Items.PAPER), new ItemStack[]{new ItemStack(Items.AIR, 1)});

			// InFluidCrafting.removeCrafting(new ItemStack(Blocks.CLAY));
		}
	}
	
	@Mod.EventHandler
	public void onServerStarting(FMLServerStartingEvent event) {
		IMod.super.onServerStarting(event);
		JiuUtils.entity.initNameAndUUID(event.getServer());
		event.registerServerCommand(new CommandJiuCore("jc", false, 0));
		EntityDeathDrops.initJsonDrops(event.getServer().getEntityWorld());
	}

	/**
	 * Simple Logger<p>
	 * 原木操作系统(雾)
	 * @author small_jiu
	 */
	public static final class LogOS {
		private final Logger logger;
		private final String tag;
		
		public LogOS() {
			String name = new Throwable().getStackTrace()[1].getClassName();
	    	name = name.substring(name.lastIndexOf(".")+1);
	    	this.tag = name;
	    	this.logger = LogManager.getLogger(this.tag);
	    	setHandler(logger);
		}
		public LogOS(Logger logger) {
			this.logger = logger;
	    	this.tag = logger.getName();
	    	setHandler(logger);
		}
		
		private static void setHandler(Logger logger) {
			
		}
		
		public Logger getLogger() {
			return logger;
		}
		public String getTag() {
			return tag;
		}

		public void log(Level level, String msg, Object... params) {
			this.logger.log(level, msg, params);
		}

		public void debug(String msg, Object... params) {
			this.log(Level.DEBUG, msg, params);
		}

		public void info(String msg, Object... params) {
			this.log(Level.INFO, msg, params);
		}

		public void warning(String msg, Object... params) {
			this.log(Level.WARN, msg, params);
		}

		public void error(String msg, Object... params) {
			this.log(Level.ERROR, msg, params);
		}

		public void fatal(String msg, Object... params) {
			this.log(Level.FATAL, msg, params);
		}
	}
}
