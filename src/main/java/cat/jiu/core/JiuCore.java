package cat.jiu.core;

import java.io.File;

import org.apache.logging.log4j.Logger;

import cat.jiu.core.api.IMod;
import cat.jiu.core.events.game.GameShutdownEvent;
import cat.jiu.core.net.SimpleMsgHandler;
import cat.jiu.core.net.msg.MsgUpdateTileEntity;
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
import net.minecraftforge.fml.common.event.FMLLoadCompleteEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.relauncher.Side;

@Mod(
	modid = JiuCore.MODID,
	name = JiuCore.NAME,
	version = JiuCore.VERSION,
	dependencies = "required-after:mixinbooter",
	useMetadata = true)
public class JiuCore implements IMod {
	public static final String MODID = "jiucore";
	public static final String NAME = "JiuCore";
	public static final String OWNER = "small_jiu";
	public static final String VERSION = "1.1.6-a2";

	private static Boolean isDev; // if is IDE, you can set to 'true' to enable some test stuff
	public static boolean dev() {
		if(isDev == null) {
			isDev = new File("./config/jiu/core_debug.jiu").exists();
		}
		return isDev;
	}
	private static Boolean DevelopmentEnvironment;
	public static boolean isDevelopmentEnvironment() {
		if(DevelopmentEnvironment==null) {
			try {
				Block.class.getDeclaredField("lightValue");
				Item.class.getDeclaredField("maxDamage");
				DevelopmentEnvironment = true;
				CoreLoggers.getLogOS().info("Is Dev.");
			}catch(Throwable e) {
				CoreLoggers.getLogOS().info("Is not Dev.");
			}
		}
		return DevelopmentEnvironment;
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
		}, "Shutdown Thread"));

		FluidRegistry.enableUniversalBucket();
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
		JiuUtils.entity.initNameAndUUID(null);

		Values.loadFromFile();
		Values.addValue(Values.Coin);
		Values.addValue(Values.Death);

		BaseNBT.register(-1, NBTTagNull.class, "TAG_Null", "NULL");
		
		SimpleMsgHandler.register(MODID)
			.registerMessage(MsgUpdateTileEntity::handler, MsgUpdateTileEntity.class, 0, Side.CLIENT);
		
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
	}

	@Mod.EventHandler
	public void onServerStarting(FMLServerStartingEvent event) {
		IMod.super.onServerStarting(event);
		JiuUtils.entity.initNameAndUUID(event.getServer());
		event.registerServerCommand(new CommandJiuCore("jc", false, 0));
		EntityDeathDrops.initJsonDrops(event.getServer().getEntityWorld());
	}
	
	/**
	 * Simple Logger
	 * <p>
	 * 原木操作系统(雾)
	 * 
	 * @author small_jiu
	 */
	@Deprecated
	public static final class LogOS extends cat.jiu.core.LogOS {

		public LogOS() {
			super();
		}

		public LogOS(Class<?> clazz) {
			super(clazz);
		}

		public LogOS(Logger logger) {
			super(logger);
		}

		public LogOS(String tag) {
			super(tag);
		}
		
	}
}
