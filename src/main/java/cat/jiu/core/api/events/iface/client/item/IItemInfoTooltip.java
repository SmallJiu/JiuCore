package cat.jiu.core.api.events.iface.client.item;

import java.util.List;

import cat.jiu.core.api.IJiuEvent;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;

public interface IItemInfoTooltip extends IJiuEvent {
	void onTooltip(ItemStack stack, List<String> toolTip, ITooltipFlag flag);
}
