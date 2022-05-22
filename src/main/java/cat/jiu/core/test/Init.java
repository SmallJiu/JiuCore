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

	public static final BlockTest TestBlock = JiuCore.test() ? new BlockTest() : null;
	public static final BlockTestNormal TestBlockNormal = JiuCore.test() ? new BlockTestNormal() : null;
	public static final ItemFoodTest TestFood = JiuCore.test() ? new ItemFoodTest() : null;
	public static final ItemSubTest TestItem = JiuCore.test() ? new ItemSubTest() : null;
	public static final Bubble BUBBLE = JiuCore.test() ? new Bubble() : null;
	public static final ItemDebug DEBUG = JiuCore.test() ? new ItemDebug() : null;
	public static final TestGas GAS = JiuCore.test() ? new TestGas() : null;
	public static final ItemMetadataTool NBT_TOOL = JiuCore.test() ? new ItemMetadataTool() : null;
	public static final ItemMetadataSword NBT_SWORD = JiuCore.test() ? new ItemMetadataSword() : null;
	public static final ItemMetadataPickaxe NBT_PICKAXE = JiuCore.test() ? new ItemMetadataPickaxe() : null;
	public static final ItemMetadataAxe NBT_AXE = JiuCore.test() ? new ItemMetadataAxe() : null;
	public static final ItemMetadataShovel NBT_SHOVEL = JiuCore.test() ? new ItemMetadataShovel() : null;
	public static final ItemMetadataHoe NBT_HOE = JiuCore.test() ? new ItemMetadataHoe() : null;

	@SubscribeEvent(priority = EventPriority.LOWEST)
	public static void onBlockRegister(RegistryEvent.Register<Block> event) {
		if(JiuCore.test()) {
			event.getRegistry().registerAll(BLOCKS.toArray(new Block[0]));
		}
	}

	@SubscribeEvent(priority = EventPriority.LOWEST)
	public static void onItemRegister(RegistryEvent.Register<Item> event) {
		if(JiuCore.test()) {
			event.getRegistry().registerAll(ITEMS.toArray(new Item[0]));
		}
	}
}
