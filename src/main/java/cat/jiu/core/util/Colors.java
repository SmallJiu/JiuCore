package cat.jiu.core.util;

import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.client.event.ColorHandlerEvent;

//@EventBusSubscriber
public class Colors {
//	@SubscribeEvent
	public static void blockColors(ColorHandlerEvent.Block event) {
	    event.getBlockColors().registerBlockColorHandler((IBlockState state, @Nullable IBlockAccess worldIn, @Nullable BlockPos pos, int tintIndex) -> {
			return 0;
		}, (Block)null);
	}
	
//	@SubscribeEvent
	public static void itemColors(ColorHandlerEvent.Item event) {
	    event.getItemColors().registerItemColorHandler((ItemStack stack, int tintIndex) ->{
			return 0;
		}, (Item)null);
	}
}
