package cat.jiu.core.util;

import java.util.List;

import cat.jiu.core.JiuCore;
import cat.jiu.core.api.IHasModel;
import cat.jiu.core.test.Init;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

@EventBusSubscriber(value = Side.CLIENT)
public class RegisterModel {
	
	private final String modid;
	
	public RegisterModel(String modid) {
		this.modid = modid;
	}
	
	public String getModID() {
		return this.modid;
	}
	
	// 物品模型快捷注册,模型文件默认丢到items文件夹下
	public void registerItemModel(Item item) {
		register(item);
	}
	
	// 不含有meta的物品模型注册，模型文件路径可自定义
	public void registerItemModel(Item item, String name, String resname) {
		register(item, 0, this.modid + ":" + name + "/" + resname, "inventory");
	}
	
	// 含有meta的物品模型注册，模型文件路径可自定义
	public void registerItemModel(Item item, int meta, String name, String resname) {
		register(item, meta, this.modid + ":" + name + "/" + resname, "inventory");
	}
	
	// 方块模型快捷注册,模型文件默认丢到blocks文件夹下
	public void registerItemModel(Block block) {
		register(Item.getItemFromBlock(block), 0, "inventory");
	}
	
	// 不含有meta方块的模型注册，模型文件路径可自定义
	public void registerItemModel(Block block, String name, String resname) {
		register(Item.getItemFromBlock(block), 0, this.modid + ":" + name + "/" + resname, "inventory");
	}
	
	// 含有meta方块的模型注册，模型文件路径可自定义
	public void registerItemModel(Block block, int meta, String name, String resname) {
		register(Item.getItemFromBlock(block), meta, this.modid + ":" + name + "/" + resname, "inventory");
	}
	
	private void register(Item item) {
		ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(item.getRegistryName(), "inventory"));
	}
	
	private void register(Item item, int meta, String id) {
		ModelLoader.setCustomModelResourceLocation(item, meta, new ModelResourceLocation(item.getRegistryName(), id));
	}
	
	private void register(Item item, int meta, String pathName, String id) {
		ModelLoader.setCustomModelResourceLocation(item, meta, new ModelResourceLocation(new ResourceLocation(pathName), id));
	}
	
	@SubscribeEvent
	public static void onModelRegister(ModelRegistryEvent event) {
		if(JiuCore.TEST_MODEL) {
			modelRegisterHelper(Init.ITEMS);
			modelRegisterHelper(Init.BLOCKS);
			for (Item item : Init.ITEMS) {
				if (item instanceof IHasModel) {
					((IHasModel) item).getItemModel();;
				}
			}
			
			for (Block block : Init.BLOCKS) {
				if (block instanceof IHasModel) {
					((IHasModel) block).getItemModel();
				}
			}
		}
	}
	
	private static <T> void modelRegisterHelper(List<T> list) {
		for (T element : list) {
			if (element instanceof IHasModel) {
				((IHasModel) element).getItemModel();
			}
		}
	}
}
