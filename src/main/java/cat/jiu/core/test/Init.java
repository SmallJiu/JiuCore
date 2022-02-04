package cat.jiu.core.test;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import cat.jiu.core.JiuCore;
import cat.jiu.core.util.JiuUtils;
import cat.jiu.core.util.system.file.JsonUtil;

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
	
	static {
		if(JiuCore.TEST_MODEL) {
			JsonUtil.Write config0 = new JsonUtil.Write("TestMain");
			config0.add("test", "test0", 1);
			config0.add("test", "test1", 1.001F);
			config0.add("test", "test2", 1.001555D);
			config0.add("test", "test3", true);
			config0.add("test", "test4", "This is Test Json0");
			config0.add("test", "test4", "This is Test Json1");
			
			JsonObject o = new JsonObject();
			o.addProperty("test0", "test info");
			o.addProperty("test1", false);
			o.addProperty("test2", 1.001F);
			o.addProperty("test3", 1.00155D);
			config0.add("TestObj", o);
			config0.add("TestObj", "test4", "TestInfo");
			
			JsonArray a = new JsonArray();
			a.add("test info");
			a.add(false);
			a.add(1.001F);
			a.add(1.001555D);
			config0.add("TestArray", a);
			config0.add("TestArray", "", "TestInfo");
			
			
			
			JsonObject o1 = new JsonObject();
			o1.addProperty("test0", "test info");
			o1.addProperty("test1", false);
			o1.addProperty("test2", 1.001F);
			o1.addProperty("test3", 1.00155D);
			
			JsonUtil.Write config1 = new JsonUtil.Write("main", o1);
			
			config1.add("test", "test0", 1);
			config1.add("test", "test1", 1.001F);
			config1.add("test", "test2", 1.001555D);
			config1.add("test", "test3", true);
			config1.add("test", "test4", "This is Test Json0");
			config1.add("test", "test4", "This is Test Json1");
			
			JsonObject o0 = new JsonObject();
			o0.addProperty("test0", "test info");
			o0.addProperty("test1", false);
			o0.addProperty("test2", 1.001F);
			o0.addProperty("test3", 1.00155D);
			config1.add("TestObj", o0);
			config1.add("TestObj", "test4", "TestInfo");
			
			JsonArray a0 = new JsonArray();
			a0.add("test info");
			a0.add(false);
			a0.add(1.001F);
			a0.add(1.001555D);
			config1.add("TestArray", a0);
			config1.add("TestArray", "", "TestInfo");
			
			config0.toJsonFile("./config/jiu/core/test/config0.json");
			config1.toJsonFile("./config/jiu/core/test/config1.json");
			JsonUtil.Write.toJsonFile("./config/jiu/core/test/config2.json", new TestType("small_jiu",16,false,new Integer[] {10104,99800,9980,998}));
		}
	}
	
	private static class TestType {
		private final String name;
		private final int age;
		private final boolean man;
		private final Integer[] count;
		
		public TestType(String name, int age, boolean man, Integer[] count) {
			this.name = name;
			this.age = age;
			this.man = man;
			this.count = count;
		}
		
		@Override
		public String toString() {
			StringBuilder sb = new StringBuilder();
			sb.append("TestType{name=");
			sb.append(this.name);
			sb.append(", age=");
			sb.append(this.age);
			sb.append(", man=");
			sb.append(this.man);
			sb.append(", count=");
			sb.append(JiuUtils.other.toString(this.count));
			sb.append('}');
			return sb.toString();
		}
	}
}
