package cat.jiu.core.api;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public interface IHasModel {
	@SideOnly(Side.CLIENT)
	void getItemModel();
}
