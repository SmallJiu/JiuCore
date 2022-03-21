package cat.jiu.core.test;

import java.util.ArrayList;
import java.util.List;

import cat.jiu.core.JiuCore;
import cat.jiu.core.test.tool.ItemMetadataAxe;
import cat.jiu.core.test.tool.ItemMetadataHoe;
import cat.jiu.core.test.tool.ItemMetadataPickaxe;
import cat.jiu.core.test.tool.ItemMetadataShovel;
import cat.jiu.core.test.tool.ItemMetadataSword;
import cat.jiu.core.test.tool.ItemMetadataTool;
import net.minecraft.block.Block;
import net.minecraft.item.Item;

import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@EventBusSubscriber
public class Init {
	public static final List<Block> BLOCKS = new ArrayList<Block>();
	public static final List<Item> ITEMS = new ArrayList<Item>();
	public static final List<Fluid> FLUIDS = new ArrayList<Fluid>();

	public static final BlockTest TestBlock = JiuCore.TEST_MODEL ? new BlockTest() : null;
	public static final BlockTestNormal TestBlockNormal = JiuCore.TEST_MODEL ? new BlockTestNormal() : null;
	public static final ItemFoodTest TestFood = JiuCore.TEST_MODEL ? new ItemFoodTest() : null;
	public static final ItemSubTest TestItem = JiuCore.TEST_MODEL ? new ItemSubTest() : null;
	public static final Bubble BUBBLE = JiuCore.TEST_MODEL ? new Bubble() : null;
	public static final ItemDebug DEBUG = JiuCore.TEST_MODEL ? new ItemDebug() : null;
	public static final TestGas GAS = JiuCore.TEST_MODEL ? new TestGas() : null;
	public static final ItemMetadataTool NBT_TOOL = JiuCore.TEST_MODEL ? new ItemMetadataTool() : null;
	public static final ItemMetadataSword NBT_SWORD = JiuCore.TEST_MODEL ? new ItemMetadataSword() : null;
	public static final ItemMetadataPickaxe NBT_PICKAXE = JiuCore.TEST_MODEL ? new ItemMetadataPickaxe() : null;
	public static final ItemMetadataAxe NBT_AXE = JiuCore.TEST_MODEL ? new ItemMetadataAxe() : null;
	public static final ItemMetadataShovel NBT_SHOVEL = JiuCore.TEST_MODEL ? new ItemMetadataShovel() : null;
	public static final ItemMetadataHoe NBT_HOE = JiuCore.TEST_MODEL ? new ItemMetadataHoe() : null;

	@SubscribeEvent(priority = EventPriority.LOWEST)
	public static void onBlockRegister(RegistryEvent.Register<Block> event) {
		if(JiuCore.TEST_MODEL) {
			event.getRegistry().registerAll(BLOCKS.toArray(new Block[0]));
		}
	}

	@SubscribeEvent(priority = EventPriority.LOWEST)
	public static void onItemRegister(RegistryEvent.Register<Item> event) {
		if(JiuCore.TEST_MODEL) {
			event.getRegistry().registerAll(ITEMS.toArray(new Item[0]));
		}
	}
}
