package cat.jiu.core;

import java.io.File;
import java.util.List;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.common.collect.Lists;

import cat.jiu.core.api.IMod;
import cat.jiu.core.api.events.clazz.game.GameShutdownEvent;
import cat.jiu.core.api.events.iface.game.IGameShutdown;
import cat.jiu.core.api.events.iface.game.IInFluidCraftingEvent;
import cat.jiu.core.api.values.Values;
import cat.jiu.core.capability.CapabilityJiuEnergy;
import cat.jiu.core.commands.CommandJiuCore;
import cat.jiu.core.test.Init;
import cat.jiu.core.trigger.JiuCoreTriggers;
import cat.jiu.core.util.EntityDeathDrops;
import cat.jiu.core.util.JiuCoreEvents;
import cat.jiu.core.util.JiuUtils;
import cat.jiu.core.util.base.BaseCreativeTab;
import cat.jiu.core.util.base.BaseNBT;
import cat.jiu.core.util.crafting.AnvilRecipe;
import cat.jiu.core.util.crafting.InFluidCrafting;
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

@Mod(
	modid = JiuCore.MODID,
	name = JiuCore.NAME,
	version = JiuCore.VERSION,
	dependencies = "required-after:mixinbooter",
	useMetadata = true
)
@EventBusSubscriber
public class JiuCore implements IInFluidCraftingEvent, IMod {
	public static final String MODID = "jiucore";
	public static final String NAME = "JiuCore";
	public static final String OWNER = "small_jiu";
	public static final String VERSION = "1.1.5-a0";

	private static Boolean isDev; // if is IDE, you can set to 'true' to enable some test stuff
	public static final boolean dev() {
		if(isDev == null) {
			isDev = new File("./config/jiu/core_debug.jiu").exists();
		}
		return isDev;
	}

	public static final CreativeTabs CORE = dev() ? new BaseCreativeTab("core_test_tab", new ItemStack(Items.DIAMOND), false) : null;

	/** 'A' - 'Z' */private static final List<Character> CHAR_LETTERS = Lists.newArrayList('A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z');
	/** "A" - "Z" */private static final List<String> STRING_LETTERS = Lists.newArrayList("A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z");
	public static boolean containsLetter(char c) {
		return CHAR_LETTERS.contains(c);
	}
	public static boolean containsLetter(String c) {
		return STRING_LETTERS.contains(c);
	}
	
	/** '0' - '9' */private static final List<Character> CHAR_NUMBERS = Lists.newArrayList('0', '1', '2', '3', '4', '5', '6', '7', '8', '9');
	/** "0" - "9" */private static final List<String> STRING_NUMBERS = Lists.newArrayList("0", "1", "2", "3", "4", "5", "6", "7", "8", "9");
	public static boolean containsNumber(char num) {
		return CHAR_NUMBERS.contains(num);
	}
	public static boolean containsNumber(String num) {
		return STRING_NUMBERS.contains(num);
	}
	
	@Mod.Instance(
		value = JiuCore.MODID,
		owner = JiuCore.OWNER)
	public static JiuCore instance;
	public JiuCore() {
		CoreLoggers.getLogOS().info("Constructors start.");
		
		Runtime.getRuntime().addShutdownHook(new Thread(() -> {
			try {
				MinecraftForge.EVENT_BUS.post(new GameShutdownEvent(proxy.getSide()));
				List<IGameShutdown> l = JiuCoreEvents.getEvents(IGameShutdown.class);
				if(l != null) l.stream().forEach(e -> e.shutdown(proxy.getSide()));
				Values.saveValue(true);
			}catch(Exception e) {
				CoreLoggers.getLogOS().exception(e);
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
	
	@Mod.EventHandler
	public void onPreInit(FMLPreInitializationEvent event) {
		CoreLoggers.getLogOS().info("PreInit start.");
		proxy.preInit(event);
		System.out.println(proxy);
		JiuCoreEvents.addEvent(this, new InFluidCrafting(), new EntityDeathDrops());
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
		proxy.loadComplete(event);
		List<IInFluidCraftingEvent> list = JiuCoreEvents.getEvents(IInFluidCraftingEvent.class);
		if(list != null) {
			Recipes recipe = new Recipes(MODID);
			list.stream().forEach(e -> e.onAddInFluidCrafting(recipe));
		}
	}

	@Override
	public void onAddInFluidCrafting(Recipes rec) {
		if(dev()) {
			rec.addInFluidCrafting(new ItemStack(Init.TestBlock), new ItemStack[]{new ItemStack(Items.PAPER, 1)});
			rec.addInFluidCrafting(new ItemStack(Items.PAPER), new ItemStack[]{new ItemStack(Items.AIR, 1)});

			// InFluidCrafting.removeCrafting(new ItemStack(Blocks.CLAY));
		}
	}
	
	@Mod.EventHandler
	public void onServerStarting(FMLServerStartingEvent event) {
		proxy.onServerStarting(event);
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
		private Logger logger;
		private cat.jiu.core.Logger log;
		private boolean useLog4j;
		private final String tag;
		
		public LogOS(boolean useLog4j) {
			String name = new Throwable().getStackTrace()[1].getClassName();
	    	name = name.substring(name.lastIndexOf(".")+1);
	    	this.useLog4j = useLog4j;
	    	if(useLog4j) {
	    		this.logger = LogManager.getLogger(name);
	    	}else {
	    		this.log = new cat.jiu.core.Logger(name);
	    	}
	    	this.setFilterClass(LogOS.class);
	    	this.tag = name;
		}
		public LogOS(Logger logger) {
			this(logger, true);
		}
		public LogOS(Logger logger, boolean useLog4j) {
			this.logger = logger;
			this.log = !useLog4j ? new cat.jiu.core.Logger(logger.getName()) : null;
			this.setFilterClass(LogOS.class);
			this.useLog4j = useLog4j;
	    	this.tag = logger.getName();
		}

		public Logger logger() {
			return this.logger;
		}
		public boolean isUseLog4j() {
			return useLog4j;
		}
		public void setUseLog4j(boolean useLog4j) {
			this.useLog4j = useLog4j;
			if(useLog4j && this.logger==null) {
				this.logger = LogManager.getLogger(this.tag);
			}
			if(!useLog4j && this.log==null) {
				this.log = new cat.jiu.core.Logger(this.tag);
			}
		}
		public void setLogFile(String string) {
			if(this.log!=null) {
				this.log.setLogFile(string);
			}
		}
		public void setFilterClass(Class<?>... clazzs) {
			if(this.log!=null) {
				this.log.setFilterClass(clazzs);
			}
		}
		
		public cat.jiu.core.Logger getLog() {
			return log;
		}
		
		public Logger getLogger() {
			return logger;
		}

		public void log(Level level, String msg, Object... params) {
			if(this.useLog4j) {
				this.logger.log(level, msg, params);
			}else {
				this.log.log(getLevel(level), msg, params);
			}
		}

		public void log(cat.jiu.core.Logger.Level level, String msg, Object... params) {
			if(this.useLog4j) {
				this.logger.log(getLevel(level), msg, params);
			}else {
				this.log.log(level, msg, params);
			}
		}

		public cat.jiu.core.Logger.Level getLevel(Level level) {
			if(level == Level.INFO) {
				return cat.jiu.core.Logger.Level.INFO;
			}else if(level == Level.WARN) {
				return cat.jiu.core.Logger.Level.WARN;
			}else if(level == Level.ERROR) {
				return cat.jiu.core.Logger.Level.ERROR;
			}else if(level == Level.FATAL) {
				return cat.jiu.core.Logger.Level.FATAL;
			}
			return cat.jiu.core.Logger.Level.DEBUG;
		}

		public Level getLevel(cat.jiu.core.Logger.Level level) {
			switch(level) {
				case DEBUG: return Level.DEBUG;
				case INFO: return Level.INFO;
				case WARN: return Level.WARN;
				case ERROR: return Level.ERROR;
				case FATAL: return Level.FATAL;
				default: return Level.ALL;
			}
		}

		public void debug(String msg, Object... params) {
			if(useLog4j) {
				this.log(Level.DEBUG, msg, params);
			}else {
				this.log.debug(msg, params);
			}
		}

		public void info(String msg, Object... params) {
			if(useLog4j) {
				this.log(Level.INFO, msg, params);
			}else {
				this.log.info(msg, params);
			}
		}

		public void warning(String msg, Object... params) {
			if(useLog4j) {
				this.log(Level.WARN, msg, params);
			}else {
				this.log.warning(msg, params);
			}
		}

		public void error(String msg, Object... params) {
			if(useLog4j) {
				this.log(Level.ERROR, msg, params);
			}else {
				this.log.error(msg, params);
			}
		}

		public void fatal(String msg, Object... params) {
			if(useLog4j) {
				this.log(Level.FATAL, msg, params);
			}else {
				this.log.fatal(msg, params);
			}
		}
		
		public void exception(Throwable exception) {
			this.error("{}", exception);
			StackTraceElement[] stacks = exception.getStackTrace();
			for(int i = 0; i < stacks.length; i++) {
				this.error("    {}", stacks[i]);
			}
			exception.printStackTrace();
		}
	}
}
