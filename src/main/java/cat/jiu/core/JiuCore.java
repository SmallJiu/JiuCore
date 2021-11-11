package cat.jiu.core;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import cat.jiu.core.api.IJiuEvent;
import cat.jiu.core.api.events.game.IInFluidCraftingEvent;
import cat.jiu.core.commands.CommandJiuCore;
import cat.jiu.core.proxy.CommonProxy;
import cat.jiu.core.test.Init;
import cat.jiu.core.util.JiuCoreEvents;
import cat.jiu.core.util.base.BaseCreativeTab;
import cat.jiu.core.util.crafting.InFluidCrafting;
import cat.jiu.core.util.crafting.Recipes;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;

@Mod(
	modid = JiuCore.MODID,
	name = JiuCore.NAME,
	version = JiuCore.VERSION,
	useMetadata = true,
	dependencies = "required:forge@[14.23.5.2847,)",
	acceptedMinecraftVersions = "[1.12.2]"
)
public class JiuCore implements IInFluidCraftingEvent {
	protected static final Logger logger = LogManager.getLogger(JiuCore.MODID);
	public final LogOS log = new LogOS();
	public static final String MODID = "jiucore";
	public static final String NAME = "JiuCore";
	public static final String OWNER = "small_jiu";
	public static final String VERSION = "1.0.2";
	public static final boolean TEST_MODEL = false;
	public static final CreativeTabs CORE = TEST_MODEL ? new BaseCreativeTab("core_test_tab", new ItemStack(Items.DIAMOND), false) : null;
	
	@Mod.Instance(value = JiuCore.MODID, owner = JiuCore.OWNER)
	public static JiuCore instance = new JiuCore();
	
	@SidedProxy(
		clientSide = "cat.jiu.core.proxy.ClientProxy",
		serverSide = "cat.jiu.core.proxy.CommonProxy",
		modId = JiuCore.MODID
	)
	public static CommonProxy proxy;
	
	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		JiuCoreEvents.addEvent(new IJiuEvent[] {this, new InFluidCrafting()});
		if(TEST_MODEL) {
			new Init();
		}
	}
	
	@Mod.EventHandler
	public void init(FMLInitializationEvent event) {
		for(IJiuEvent events : JiuCoreEvents.getEvents()) {
			if(events instanceof IInFluidCraftingEvent) {
				((IInFluidCraftingEvent) events).onAddInFluidCrafting(new Recipes(JiuCore.MODID));
			}
		}
	}
	
	@Override
	public void onAddInFluidCrafting(Recipes rec) {
		if(TEST_MODEL) {
			rec.addInFluidCrafting(new ItemStack(Init.TestBlock), new ItemStack[] {new ItemStack(Items.PAPER, 4), new ItemStack(Items.GHAST_TEAR, 4), new ItemStack(Items.DIAMOND_AXE, 4), new ItemStack(Items.BED, 4)});
			
//			InFluidCrafting.removeCrafting(new ItemStack(Blocks.CLAY));
		}
	}
	
	@Mod.EventHandler
	public void onServerstartting(FMLServerStartingEvent event) {
		event.registerServerCommand(new CommandJiuCore("jc", false, true, 0));
	}
	
	public static Logger getLogger() {
		return logger;
	}
	
	// 原木操作系统(雾)
	public static final class LogOS{
		private final Logger logger;
		
		protected LogOS() {
			this.logger = getLogger();
		}
		
		public LogOS(Logger logger) {
			this.logger = logger;
		}
		
		public void log(Level level, String msg) {
			this.logger.log(level, msg);
		}
		
		public void log(Level level, String msg, Object... params) {
			this.logger.log(level, msg, params);
		}
		
		public void debug(String msg) {
			this.log(Level.DEBUG, msg);
		}
		
		public void debug(String msg, Object... params) {
			this.log(Level.DEBUG, msg, params);
		}
		
		public void info(String msg) {
			this.log(Level.INFO, msg);
		}
		
		public void info(String msg, Object... params) {
			this.log(Level.INFO, msg, params);
		}
		
		public void warning(String msg) {
			this.log(Level.WARN, msg);
		}
		
		public void warning(String msg, Object... params) {
			this.log(Level.WARN, msg, params);
		}
		
		public void error(String msg) {
			this.log(Level.ERROR, msg);
		}
		
		public void error(String msg, Object... params) {
			this.log(Level.ERROR, msg, params);
		}
		
		public void fatal(String msg) {
			this.log(Level.FATAL, msg);
		}
		
		public void fatal(String msg, Object... params) {
			this.log(Level.FATAL, msg, params);
		}
	}
}
