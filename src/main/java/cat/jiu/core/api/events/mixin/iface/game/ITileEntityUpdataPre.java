package cat.jiu.core.api.events.mixin.iface.game;

import cat.jiu.core.api.IJiuEvent;
import net.minecraft.tileentity.TileEntity;

public interface ITileEntityUpdataPre extends IJiuEvent {
	void onPre(TileEntity tile);
}
