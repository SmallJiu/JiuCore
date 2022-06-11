package cat.jiu.core.test;

import java.util.ArrayList;

import cat.jiu.core.JiuCore;
import cat.jiu.core.api.events.item.IItemInFluidTickEvent;
import cat.jiu.core.util.JiuUtils;
import cat.jiu.core.util.base.BaseItem;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class Bubble extends BaseItem.Normal implements IItemInFluidTickEvent{
	public Bubble() {
		super(JiuCore.MODID, "bubble", JiuCore.CORE);
		Init.ITEMS.add(this);
	}
	
	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		if(!worldIn.isRemote) {
			if(player.getHeldItemMainhand().getItem() == this) {
				this.tick(pos, worldIn);
			}
		}
		return super.onItemUse(player, worldIn, pos, hand, facing, hitX, hitY, hitZ);
	}
	
	@Override
	public void onItemInFluidTick(EntityItem item, World world, BlockPos pos, IBlockState state) {
		if(item.getItem().getItem() == this) {
			this.tick(pos, world);
		}
	}
	
	private void tick(BlockPos pos, World world) {
		world.getMinecraftServer().addScheduledTask(() ->{
			ArrayList<BlockPos> poss = new ArrayList<>();
			if(JiuUtils.item.isFluid(world.getBlockState(pos))) {
				world.setBlockToAir(pos);
			}
			for(EnumFacing side : EnumFacing.values()) {
				BlockPos sidePos = pos.offset(side);
				if(JiuUtils.item.isFluid(world.getBlockState(sidePos))) {
					world.setBlockToAir(sidePos);
					poss.add(sidePos);
				}
			}
			if(!poss.isEmpty() && !world.isRemote) {
				for(int i = 0; i < poss.size(); ++i) {
					if(i == 1000000) {
						break;
					}
					BlockPos possPos = poss.get(i);
					if(possPos != null) {
						if(JiuUtils.item.isFluid(world.getBlockState(possPos))) {
							world.setBlockToAir(possPos);
						}
						for(EnumFacing side : EnumFacing.values()) {
							BlockPos sidePos = possPos.offset(side);
							if(JiuUtils.item.isFluid(world.getBlockState(sidePos))) {
								world.setBlockToAir(sidePos);
								poss.add(sidePos);
							}
						}
						JiuCore.getLogOS().info("Fluid: " + (i+1) + "@" + poss.size() + "#" + (poss.size()-i));
					}
				}
			}
		});
	}
}
