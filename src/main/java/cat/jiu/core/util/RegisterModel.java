package cat.jiu.core.util;

import cat.jiu.core.JiuCore;
import cat.jiu.core.api.IHasModel;
import cat.jiu.core.test.Init;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;

import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fluids.BlockFluidBase;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@EventBusSubscriber
public class RegisterModel {
	
	private final String modid;
	
	public RegisterModel(String modid) {
		this.modid = modid;
	}
	
	public String getModID() {
		return this.modid;
	}
	
	/**
	 * @param item the item
	 */
	// 物品模型快捷注册,模型文件默认丢到items文件夹下
	@SideOnly(Side.CLIENT)
	public void registerItemModel(Item item) {
		this.register(item);
	}
	
	/**
	 * @param item the item
	 * @param dir the dir name
	 * @param file the model name
	 */
	// 不含有meta的物品模型注册，模型文件路径可自定义
	@SideOnly(Side.CLIENT)
	public void registerItemModel(Item item, String dir, String file) {
		this.register(item, 0, this.modid + ":" + dir + (dir.endsWith("/") ? "" : "/") + file, "inventory");
	}
	
	/**
	 * @param item the item
	 * @param meta the meta
	 * @param dir the dir name
	 * @param file the model name
	 */
	// 含有meta的物品模型注册，模型文件路径可自定义
	@SideOnly(Side.CLIENT)
	public void registerItemModel(Item item, int meta, String dir, String file) {
		this.register(item, meta, this.modid + ":" + dir + (dir.endsWith("/") ? "" : "/") + file, "inventory");
	}
	
	/**
	 * @param block the block
	 */
	// 方块模型快捷注册,模型文件默认丢到blocks文件夹下
	@SideOnly(Side.CLIENT)
	public void registerItemModel(Block block) {
		this.register(Item.getItemFromBlock(block), 0, "inventory");
	}
	
	/**
	 * @param block the block
	 * @param dir the dir name
	 * @param file the model name
	 */
	// 不含有meta方块的模型注册，模型文件路径可自定义
	@SideOnly(Side.CLIENT)
	public void registerItemModel(Block block, String dir, String file) {
		this.register(Item.getItemFromBlock(block), 0, this.modid + ":" + dir + (dir.endsWith("/") ? "" : "/") + file, "inventory");
	}
	
	/**
	 * @param block the block
	 * @param meta the meta
	 * @param dir the dir name
	 * @param file the model name
	 */
	// 含有meta方块的模型注册，模型文件路径可自定义
	@SideOnly(Side.CLIENT)
	public void registerItemModel(Block block, int meta, String dir, String file) {
		this.register(Item.getItemFromBlock(block), meta, this.modid + ":" + dir + (dir.endsWith("/") ? "" : "/") + file, "inventory");
	}
	
	/**
	 * @param block the fluid block
	 */
	@SideOnly(Side.CLIENT)
	public void registerFluidModel(BlockFluidBase block) {
		this.registerFluidModel(block, "");
	}
	
	/**
	 * @param block the fluid block
	 * @param statePath the state mapper path
	 */
	@SideOnly(Side.CLIENT)
	public void registerFluidModel(BlockFluidBase block, String statePath) {
		String location = statePath + block.getRegistryName().getResourcePath();
		ModelLoader.setCustomMeshDefinition(Item.getItemFromBlock(block), stack -> new ModelResourceLocation(this.modid + ":" + location, block.getRegistryName().getResourcePath()));
		ModelBakery.registerItemVariants(Item.getItemFromBlock(block));
		this.setBlockStateMapper(block, false, location, block.getRegistryName().getResourcePath());
	}
	
	/**
	 * @param block the block
	 * @param isNormalBlock if the block meta only has zero(0), set to true
	 * @param statePath the state mapper path
	 */
	@SideOnly(Side.CLIENT)
	public void setBlockStateMapper(Block block, boolean isNormalBlock, String statePath) {
		ModelLoader.setCustomStateMapper(block, new StateMapperBase() {
			protected ModelResourceLocation getModelResourceLocation(IBlockState state) {
				String variant = isNormalBlock ? "normal" : JiuUtils.other.custemSplitString(JiuUtils.other.custemSplitString(state.toString(), "[")[1], "]")[0];
				return new ModelResourceLocation(modid + ":" + statePath, variant);
			}
		});
	}
	
	/**
	 * @param block the block
	 * @param isNormalBlock if the block meta only has zero(0), set to true
	 * @param statePath the state mapper path
	 * @param variant the variant
	 */
	@SideOnly(Side.CLIENT)
	public void setBlockStateMapper(Block block, boolean isNormalBlock, String statePath, String variant) {
		ModelLoader.setCustomStateMapper(block, new StateMapperBase() {
			protected ModelResourceLocation getModelResourceLocation(IBlockState state) {
				return new ModelResourceLocation(modid + ":" + statePath, isNormalBlock ? "normal" : variant);
			}
		});
	}
	
	/**
	 * @param item the item
	 */
	@SideOnly(Side.CLIENT)
	public void register(Item item) {
		ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(item.getRegistryName(), "inventory"));
	}
	
	/**
	 * @param item the item
	 * @param meta the meta
	 * @param resname the model name
	 */
	@SideOnly(Side.CLIENT)
	public void register(Item item, int meta, String variant) {
		ModelLoader.setCustomModelResourceLocation(item, meta, new ModelResourceLocation(item.getRegistryName(), variant));
	}
	
	/**
	 * @param item the item
	 * @param meta the meta
	 * @param file the file name
	 * @param variant the variant name
	 */
	@SideOnly(Side.CLIENT)
	public void register(Item item, int meta, String file, String variant) {
		ModelLoader.setCustomModelResourceLocation(item, meta, new ModelResourceLocation(new ResourceLocation(file), variant));
	}
	
	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public static void onModelRegister(ModelRegistryEvent event) {
		if(JiuCore.TEST_MODEL) {
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
}
