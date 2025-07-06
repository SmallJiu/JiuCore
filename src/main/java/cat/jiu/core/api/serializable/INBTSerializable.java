package cat.jiu.core.api.serializable;

import net.minecraft.nbt.CompoundTag;

public interface INBTSerializable {
	CompoundTag write(CompoundTag data);
	void read(CompoundTag data);
}
