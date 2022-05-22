package cat.jiu.core.util;

import cat.jiu.core.api.ISubBlockSerializable;

public class SubtypesExample {
	public enum ModSubtypes implements ISubBlockSerializable {
		LEVEL_1;
		
		private static final ModSubtypes[] METADATA_LOOKUP = new ModSubtypes[values().length];
		
		public int getMeta() {return this.ordinal();}
		@Override
		public String getName() {return "state_" + this.ordinal();}
		
		public static ModSubtypes byMetadata(int meta) {
			return METADATA_LOOKUP[meta];
		}
		
		static {
			for (ModSubtypes type : values()) {
				METADATA_LOOKUP[type.getMeta()] = type;
			}
		}
	}
	public enum ModSubtypes00 implements ISubBlockSerializable {
		LEVEL_1;
		
		@Override
		public int getMeta() {return this.ordinal();}
		@Override
		public String getName() {return "state_" + this.ordinal();}
	}
}

