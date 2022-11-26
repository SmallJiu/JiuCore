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

import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber
public class Init {
	public static final List<Block> BLOCKS = new ArrayList<Block>();
	public static final List<Item> ITEMS = new ArrayList<Item>();
	public static final List<Fluid> FLUIDS = new ArrayList<Fluid>();

	public static final BlockTest TestBlock = JiuCore.dev() ? new BlockTest() : null;
	public static final BlockTestNormal TestBlockNormal = JiuCore.dev() ? new BlockTestNormal() : null;
	public static final ItemFoodTest TestFood = JiuCore.dev() ? new ItemFoodTest() : null;
	public static final ItemSubTest TestItem = JiuCore.dev() ? new ItemSubTest() : null;
	public static final Bubble BUBBLE = JiuCore.dev() ? new Bubble() : null;
	public static final ItemDebug DEBUG = JiuCore.dev() ? new ItemDebug() : null;
	public static final TestGas GAS = JiuCore.dev() ? new TestGas() : null;
	public static final ItemMetadataTool NBT_TOOL = JiuCore.dev() ? new ItemMetadataTool() : null;
	public static final ItemMetadataSword NBT_SWORD = JiuCore.dev() ? new ItemMetadataSword() : null;
	public static final ItemMetadataPickaxe NBT_PICKAXE = JiuCore.dev() ? new ItemMetadataPickaxe() : null;
	public static final ItemMetadataAxe NBT_AXE = JiuCore.dev() ? new ItemMetadataAxe() : null;
	public static final ItemMetadataShovel NBT_SHOVEL = JiuCore.dev() ? new ItemMetadataShovel() : null;
	public static final ItemMetadataHoe NBT_HOE = JiuCore.dev() ? new ItemMetadataHoe() : null;
}
