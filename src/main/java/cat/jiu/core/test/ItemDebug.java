package cat.jiu.core.test;

import cat.jiu.core.JiuCore;
import cat.jiu.core.util.JiuUtils;
import cat.jiu.core.util.base.BaseItem;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemDebug extends BaseItem.Normal {
	public ItemDebug() {
		super(JiuCore.MODID, "debug", CreativeTabs.TOOLS, false);
		Init.ITEMS.add(this);
	}
	
	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
		if(world.isRemote) return super.onItemRightClick(world, player, hand);
		
		BlockPos pPos = player.getPosition();
		if(world.setBlockState(new BlockPos(pPos.getX(), -1, pPos.getZ()), Blocks.STONE.getDefaultState())) {
			player.sendStatusMessage(JiuUtils.other.createTextComponent("True"), true);
		}else {
			player.sendStatusMessage(JiuUtils.other.createTextComponent("Flase"), true);
		}
		
		return super.onItemRightClick(world, player, hand);
	}
	
	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		if(world.isRemote) return super.onItemUse(player, world, pos, hand, facing, hitX, hitY, hitZ);
		
		BlockPos pPos = player.getPosition();
		if(world.setBlockState(new BlockPos(pPos.getX(), -1, pPos.getZ()), Blocks.STONE.getDefaultState(), 3)) {
			player.sendStatusMessage(JiuUtils.other.createTextComponent("True"), true);
		}else {
			player.sendStatusMessage(JiuUtils.other.createTextComponent("Flase"), true);
		}
		
		return super.onItemUse(player, world, pos, hand, facing, hitX, hitY, hitZ);
	}
}
