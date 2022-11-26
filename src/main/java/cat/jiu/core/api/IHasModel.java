package cat.jiu.core.api;

import cat.jiu.core.util.RegisterModel;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public interface IHasModel {
	@SideOnly(Side.CLIENT)
	default void getItemModel(RegisterModel util) {this.getItemModel();}
	
	@Deprecated
	@SideOnly(Side.CLIENT)
	default void getItemModel() {};
}
