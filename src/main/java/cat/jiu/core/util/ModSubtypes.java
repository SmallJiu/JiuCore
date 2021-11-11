package cat.jiu.core.util;

import cat.jiu.core.api.ISubBlockSerializable;

public enum ModSubtypes implements ISubBlockSerializable {

	LEVEL_1(0);
	
	private final int meta;
	private static final ModSubtypes[] METADATA_LOOKUP = new ModSubtypes[values().length];
	
	ModSubtypes(int meta) {
		this.meta = meta;
	}
	
	public int getMeta() {
		return meta;
	}
	
	@Override
	public String getName() {
		return "state_" + meta;
	}
	
	public static ModSubtypes byMetadata(int meta) {
		return METADATA_LOOKUP[meta];
	}
	
	static {
		for (ModSubtypes type : values()) {
			METADATA_LOOKUP[type.getMeta()] = type;
		}
	}
}