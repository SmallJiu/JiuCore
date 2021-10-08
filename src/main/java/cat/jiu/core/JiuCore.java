package cat.jiu.core;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import cat.jiu.core.proxy.CommonProxy;
import cat.jiu.core.util.JiuCoreEvents;
import cat.jiu.core.util.crafting.InFluidCrafting;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(
	modid = JiuCore.MODID,
	name = JiuCore.NAME,
	version = JiuCore.VERSION,
	useMetadata = true,
	dependencies = "required:forge@[14.23.5.2847,)",
	acceptedMinecraftVersions = "[1.12.2]"
)
public class JiuCore {
	protected static final Logger logger = LogManager.getLogger(JiuCore.MODID);
	public final LogOS log = new LogOS();
	public static final String MODID = "jiucore";
	public static final String NAME = "JiuCore";
	public static final String OWNER = "small_jiu";
	public static final String VERSION = "1.0.0";
	
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
		JiuCoreEvents.addEvent(new InFluidCrafting());
		
		/*
		Don't add like this the recipe! it has a BUG!
		 
		new Recipes(MODID).addInFluidCrafting(new ItemStack(Blocks.COAL_BLOCK), 1, new ItemStack(Items.COAL, 9));
		new Recipes(MODID).addInFluidCrafting(new ItemStack(Items.COAL), 9, new ItemStack(Blocks.COAL_BLOCK));
		*/
	}
	
	public static Logger getLogger() {
		return logger;
	}
	
	// 原木操作系统(雾)
	public static class LogOS{
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
			this.log(Level.FATAL, msg);
		}
	}
}
