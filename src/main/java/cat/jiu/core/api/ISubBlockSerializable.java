package cat.jiu.core.api;

import net.minecraft.util.IStringSerializable;

public interface ISubBlockSerializable extends IStringSerializable{
	String getName();
	
	int getMeta();
}
