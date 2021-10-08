package cat.jiu.core.util;


import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;

import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.relauncher.Side;

@EventBusSubscriber(value = Side.CLIENT)
public class RegisterModel {
	
	private final String modID;
	
	public RegisterModel(String modid) {
		this.modID = modid;
	}
	
	public String getModID() {
		return this.modID;
	}
	
	// 物品模型快捷注册,模型文件默认丢到items文件夹下
	public void itemModelRegister(Item item) {
		register(item);
	}
	
	// 不含有meta的物品模型注册，模型文件路径可自定义
	public void registerItemModel(Item item, String name, String resname) {
		register(item, 0, this.modID + ":" + name + "/" + resname, "inventory");
	}
	
	// 含有meta的物品模型注册，模型文件路径可自定义
	public void registerItemModel(Item item, int meta, String name, String resname) {
		register(item, meta, this.modID + ":" + name + "/" + resname, "inventory");
	}
	
	// 方块模型快捷注册,模型文件默认丢到blocks文件夹下
	public void registerItemModel(Block block) {
		register(Item.getItemFromBlock(block), 0, "inventory");
	}
	
	// 不含有meta方块的模型注册，模型文件路径可自定义
	public void registerItemModel(Block block, String name, String resname) {
		register(Item.getItemFromBlock(block), 0, this.modID + ":" + name + "/" + resname, "inventory");
	}
	
	// 含有meta方块的模型注册，模型文件路径可自定义
	public void registerItemModel(Block block, int meta, String name, String resname) {
		register(Item.getItemFromBlock(block), meta, this.modID + ":" + name + "/" + resname, "inventory");
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
}
