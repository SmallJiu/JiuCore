package cat.jiu.core;

import java.io.File;
import java.util.List;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.common.collect.Lists;

import cat.jiu.core.api.events.game.IInFluidCraftingEvent;
import cat.jiu.core.api.values.Values;
import cat.jiu.core.capability.CapabilityJiuEnergy;
import cat.jiu.core.commands.CommandJiuCore;
import cat.jiu.core.proxy.ServerProxy;
import cat.jiu.core.test.Init;
import cat.jiu.core.util.EntityDeathDrops;
import cat.jiu.core.util.JiuCoreEvents;
import cat.jiu.core.trigger.JiuCoreTriggers;
import cat.jiu.core.util.JiuUtils;
import cat.jiu.core.util.base.BaseCreativeTab;
import cat.jiu.core.util.crafting.AnvilRecipe;
import cat.jiu.core.util.crafting.InFluidCrafting;
import cat.jiu.core.util.crafting.Recipes;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLLoadCompleteEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;

@Mod(
	modid = JiuCore.MODID,
	name = JiuCore.NAME,
	version = JiuCore.VERSION,
	useMetadata = true
)
public class JiuCore implements IInFluidCraftingEvent {
	protected static final Logger logger = LogManager.getLogger(JiuCore.MODID);
	public final LogOS log = new LogOS();
	public static final String MODID = "jiucore";
	public static final String NAME = "JiuCore";
	public static final String OWNER = "small_jiu";
	public static final String VERSION = "1.0.10-20220522225957";
	
	private static byte isTest = -1; // if is IDE, you can set to '1' to enable some test stuff
	public static final boolean test() {
		if(isTest == -1) {
			isTest = (byte) (new File("./config/jiu/core_debug.jiu").exists() ? 1 : 0);
		}
		return isTest == 1;
	}
	
	public static final CreativeTabs CORE = test() ? new BaseCreativeTab("core_test_tab", new ItemStack(Items.DIAMOND), false) : null;

	public static final List<Character> CHAR_LETTERS = Lists.newArrayList('A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z');
	public static final List<String> STRING_LETTERS = Lists.newArrayList("A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z");

	public static final List<Character> CHAR_NUMBERS = Lists.newArrayList('0', '1', '2', '3', '4', '5', '6', '7', '8', '9');
	public static final List<String> STRING_NUMBERS = Lists.newArrayList("0", "1", "2", "3", "4", "5", "6", "7", "8", "9");

	@Mod.Instance(
		value = JiuCore.MODID,
		owner = JiuCore.OWNER)
	public static JiuCore instance = new JiuCore();

	public JiuCore() {
		FluidRegistry.enableUniversalBucket();
	}

	@SidedProxy(
		clientSide = "cat.jiu.core.proxy.ClientProxy",
		serverSide = "cat.jiu.core.proxy.ServerProxy",
		modId = JiuCore.MODID)
	public static ServerProxy proxy;

	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		proxy.preInit(event);
		JiuCoreEvents.addEvent(this, new InFluidCrafting(), new EntityDeathDrops());
		CapabilityJiuEnergy.register();
		JiuUtils.entity.initNameAndUUID(null);
		
		Values.loadFromFile();
		Values.addValue("coin");
		Values.addValue("death");
		JiuCoreTriggers.register();

		if(test()) {
			new Init();
			AnvilRecipe.addAnvilRecipe(new ItemStack(Items.APPLE, 5), new ItemStack(Blocks.GOLD_BLOCK), new ItemStack(Blocks.DIAMOND_BLOCK, 5), 5);
			JiuCoreEvents.addEvent(Init.BUBBLE);
		}
	}

	@Mod.EventHandler
	public void loadComplete(FMLLoadCompleteEvent event) {
		List<IInFluidCraftingEvent> list = JiuCoreEvents.getEvents(IInFluidCraftingEvent.class);
		if(list != null) {
			list.stream().forEach(e -> e.onAddInFluidCrafting(new Recipes(MODID)));
		}
	}

	@Override
	public void onAddInFluidCrafting(Recipes rec) {
		if(test()) {
			rec.addInFluidCrafting(new ItemStack(Init.TestBlock), new ItemStack[]{new ItemStack(Items.PAPER, 1)});
			rec.addInFluidCrafting(new ItemStack(Items.PAPER), new ItemStack[]{new ItemStack(Items.AIR, 1)});

			// InFluidCrafting.removeCrafting(new ItemStack(Blocks.CLAY));
		}
	}

	@Mod.EventHandler
	public void onServerstartting(FMLServerStartingEvent event) {
		JiuUtils.entity.initNameAndUUID(event.getServer());
		event.registerServerCommand(new CommandJiuCore("jc", false, 0));
		EntityDeathDrops.initJsonDrops(event.getServer().getEntityWorld());
	}

	public static Logger getLogger() {
		return logger;
	}

	// 原木操作系统(雾)
	public static final class LogOS {
		private final Logger logger;
		private LogOS() {
			this.logger = getLogger();
		}

		public LogOS(Logger logger) {
			this.logger = logger;
		}

		public Logger logger() {
			return this.logger;
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
