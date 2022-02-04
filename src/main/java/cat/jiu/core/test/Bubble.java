package cat.jiu.core.test;

import java.util.ArrayList;
import java.util.List;

import cat.jiu.core.JiuCore;
import cat.jiu.core.api.events.item.IItemInFluidTickEvent;
import cat.jiu.core.util.JiuUtils;
import cat.jiu.core.util.base.BaseItem;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class Bubble extends BaseItem.Normal implements IItemInFluidTickEvent{
	public Bubble() {
		super(JiuCore.MODID, "bubble", JiuCore.CORE);
		Init.ITEMS.add(this);
	}
	
	@Override
	public void onItemInFluidTick(EntityItem item, World world, BlockPos pos, IBlockState state) {
		if(item.getItem().getItem() == this) {
			ArrayList<BlockPos> poss = new ArrayList<>();
			world.setBlockToAir(pos);
			for(EnumFacing side : EnumFacing.values()) {
				BlockPos sidePos = pos.offset(side);
				if(JiuUtils.item.isFluid(world.getBlockState(sidePos))) {
					world.setBlockToAir(sidePos);
					poss.add(sidePos);
				}
			}
			if(!poss.isEmpty() && !world.isRemote) {
				this.tick(poss, world);
			}
		}
	}
	
	private void tick(List<BlockPos> pos, World world) {
//		try {
			final List<BlockPos> poss = JiuUtils.other.copyList(pos);
//			new Thread(() -> {
				for(int i = 0; i < poss.size(); ++i) {
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
						JiuCore.instance.log.info("Fluid: " + (i+1) + "@" + poss.size());
					}
//					try { Thread.sleep(1); } catch (InterruptedException e) { e.printStackTrace(); }
				}
//			}, "Fluid Thread").start();
//		} catch (Exception e) {
//			e.printStackTrace();
//			e.fillInStackTrace();
//		}
	}
}
