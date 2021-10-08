package cat.jiu.core.util.crafting;

import cat.jiu.core.api.events.item.IItemInFluidTickEvent;
import cat.jiu.core.util.JiuUtils;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class InFluidCrafting implements IItemInFluidTickEvent{
	static int i = 6;
	private static Object[][] drops = new Object[0][i];
	
	/**
	 * 
	 * Please never add the recipe, it has a bug
	 * 
	 * new Recipes(MODID).addInFluidCrafting(new ItemStack(Blocks.COAL_BLOCK, 1), new ItemStack(Items.COAL, 9));
	 * new Recipes(MODID).addInFluidCrafting(new ItemStack(Items.COAL, 9), new ItemStack(Blocks.COAL_BLOCK));
	 * 
	 * @param fluidState fluid state
	 * @param input recipe input
	 * @param output recipe output
	 * @param inputAmout input amout use ' input.getCount() ' amout
	 * @param consumeItem item will remove
	 * @param consumeFluid fluid will remove
	 * 
	 * @author small_jiu
	 */
	public static void addInFluidCrafting(IBlockState fluidState, ItemStack input, ItemStack output, boolean consumeItem, boolean consumeFluid) {
		addInFluidCrafting(fluidState, input, output, input.getCount(), consumeFluid, consumeFluid);
	}
	
	/**
	 * 
	 * Please never add the recipe, it has a bug
	 * 
	 * new Recipes(MODID).addInFluidCrafting(new ItemStack(Blocks.COAL_BLOCK), 1, new ItemStack(Items.COAL, 9));
	 * new Recipes(MODID).addInFluidCrafting(new ItemStack(Items.COAL), 9, new ItemStack(Blocks.COAL_BLOCK));
	 * 
	 * @param fluidState fluid state
	 * @param input recipe input
	 * @param output recipe output
	 * @param inputAmout input amout
	 * @param consumeItem item will remove
	 * @param consumeFluid fluid will remove
	 * 
	 * @author small_jiu
	 */
	public static void addInFluidCrafting(IBlockState fluidState, ItemStack input, ItemStack output, int inputAmout, boolean consumeItem, boolean consumeFluid) {
		int leg = drops.length;
		Object[][] temp = new Object[(leg + 1)][i];
		
		for(int i = 0; i < drops.length; ++i) {
			temp[i][0] = drops[i][0];
			temp[i][1] = drops[i][1];
			temp[i][2] = drops[i][2];
			temp[i][3] = drops[i][3];
			temp[i][4] = drops[i][4];
			temp[i][5] = drops[i][5];
		}
		
		drops = temp;
		
		drops[leg][0] = JiuUtils.item.isFluid(fluidState) ? fluidState : Blocks.WATER.getDefaultState();
		drops[leg][1] = input;
		drops[leg][2] = output;
		drops[leg][3] = inputAmout;
		drops[leg][4] = consumeItem;
		drops[leg][5] = consumeFluid;
	}
	
	public static void removeCrafting(ItemStack giveItem) {
		
	}
	
	public static void removeCrafting(ItemStack dropItem, ItemStack giveItem) {
		/*
		Object[][] drop = drops.clone();
		
		for(int i = 0; i < drops.length; ++i) {
			if(JiuUtils.item.equalsStack((ItemStack) drops[i][2], giveItem)) {
				if(JiuUtils.item.equalsStack((ItemStack) drops[i][1], dropItem)) {
					drop[i][0] = null;
					drop[i][1] = null;
					drop[i][2] = null;
					drop[i][3] = null;
					drop[i][4] = null;
					drop[i][5] = null;
					break;
				}
			}
		}
		
		Object[][] temp = new Object[drop.length][i];
		
		for(int i = 0; i < drop.length; ++i) {
			if(drop[i][0] != null) {
				temp[i][0] = drop[i][0];
				temp[i][1] = drop[i][1];
				temp[i][2] = drop[i][2];
				temp[i][3] = drop[i][3];
				temp[i][4] = drop[i][4];
				temp[i][5] = drop[i][5];
			}
		}
		 drops = temp;
		 */
	}
	
	@Override
	public void onItemInFluidTick(EntityItem item, World world, BlockPos pos, IBlockState state) {
		ItemStack stack = item.getItem();
		
		try {
			if(drops.length != 0) {
				for(int i = 0; i < drops.length; ++i) {
					
					final IBlockState cState = (IBlockState) drops[i][0];// Fluid state
					final ItemStack dropItem = (ItemStack) drops[i][1];// items consumed
					final ItemStack giveItem = ((ItemStack) drops[i][2]).copy();// Items given
					final int consumeItemAmout = (int) drops[i][3];// consume amout
					final boolean consumeItem = (boolean) drops[i][4];// item can be consume?
					final boolean consumeFuid = (boolean) drops[i][5];// fluid can be consume?
					
					// check fluid
					if(JiuUtils.item.equalsState(state, cState)) {
						// check consumed stack
						if(JiuUtils.item.equalsStack(dropItem, stack)) {
							// check amout
							if(stack.getCount() >= consumeItemAmout) {
								// if item can be consume, subtract the amout
								if(consumeItem) {
									stack.setCount(stack.getCount() - consumeItemAmout);
								}
								
								// spawn item with the pos
								JiuUtils.item.spawnAsEntity(world, pos, giveItem);
								
								// if fluid can can consume, set fluid to air
								if(consumeFuid) {
									world.setBlockToAir(pos);
								}
								break;
							}
						}
					}
				}
			}
		} catch (Exception e) {e.fillInStackTrace();}
	}
}
