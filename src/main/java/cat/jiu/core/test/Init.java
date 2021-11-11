package cat.jiu.core.test;

import java.util.ArrayList;
import java.util.List;

import cat.jiu.core.JiuCore;
import cat.jiu.core.util.base.BaseBlock;
import cat.jiu.core.util.base.BaseItem;
import net.minecraft.block.Block;
import net.minecraft.item.Item;

import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@EventBusSubscriber
public class Init {
	public static final List<Block> BLOCKS = new ArrayList<Block>();
	public static final List<Item> ITEMS = new ArrayList<Item>();
	
	public static final BaseBlock.Sub TestBlock = JiuCore.TEST_MODEL ? new BlockTest() : null;
	public static final BaseItem.Food TestFood = JiuCore.TEST_MODEL ? new ItemFoodTest() : null;
	
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
