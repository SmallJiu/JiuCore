package cat.jiu.core.api.events.mixin.iface.game;

import cat.jiu.core.api.IJiuEvent;
import net.minecraft.tileentity.TileEntity;

public interface ITileEntityUpdataPost extends IJiuEvent {
	void onPost(TileEntity tile);
}
