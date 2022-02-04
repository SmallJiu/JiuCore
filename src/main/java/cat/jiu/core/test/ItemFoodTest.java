package cat.jiu.core.test;

import cat.jiu.core.JiuCore;
import cat.jiu.core.util.base.BaseItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

public class ItemFoodTest extends BaseItem.Food{
	public ItemFoodTest() {
		super(JiuCore.MODID, "test_food", 100, 100, false, JiuCore.CORE, true);
		this.setMaxMetadata(5);
		Init.ITEMS.add(this);
	}
	
	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
		ItemStack stack = player.getHeldItem(hand);
		stack.damageItem(1, player);
//		stack.setItemDamage(stack.getItemDamage() - 1);
		return super.onItemRightClick(world, player, hand);
	}
}
