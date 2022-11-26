package cat.jiu.core.util.base;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Map;
import java.util.Map.Entry;

import com.google.common.collect.Maps;

import net.minecraft.nbt.*;

public abstract class BaseNBT extends NBTBase {
	public abstract void write(DataOutput output) throws IOException;
	public abstract void read(DataInput input, int depth, NBTSizeTracker sizeTracker) throws IOException;
	
	static Map<Integer, NBTInfo> registrys;
	
	/**
	 * the new nbt must has a public empty arguments constructor
	 * @param id nbt id, use for {@link NBTBase#getTagTypeName(int)} and {@link NBTBase#createNewByType(byte)}
	 * @param clazz nbt class, use for {@link NBTBase#createNewByType(byte)} crete new Instance
	 * @param type nbt type, like 'TAG_Null', use for {@link NBTBase#getTagTypeName(int)}
	 * @param name nbt name, like 'NULL', use for {@link NBTBase#NBT_TYPES}
	 */
	public static void register(int id, Class<? extends BaseNBT> clazz, String type, String name) {
		if(registrys==null) registrys = Maps.newHashMap();
		if(getTypeID(clazz) == null && "UNKNOWN".equals(getTypeName(clazz))) {
			registrys.put(id, new NBTInfo(clazz, type, name));
			
			String[] old = NBTBase.NBT_TYPES;
			NBTBase.NBT_TYPES = new String[NBTBase.NBT_TYPES.length+1];
			for(int i = 0; i < old.length; i++) {
				NBTBase.NBT_TYPES[i] = old[i];
			}
			NBT_TYPES[old.length] = name;
		}
	}
	
	public static NBTInfo getNBTInfo(int id) {
		if(registrys==null) return null;
		return registrys.get(id);
	}
	public static boolean hasNBT(int id) {
		if(registrys==null) return false;
		return registrys.containsKey(id);
	}
	public static boolean hasNBT(Class<? extends NBTBase> clazz) {
		return getTypeID(clazz) != null && !"UNKNOWN".equals(getTypeName(clazz));
	}
	
	public static class NBTInfo {
		public final String type;
		public final String name;
		public final Class<? extends BaseNBT> clazz;
		public NBTInfo(Class<? extends BaseNBT> clazz, String type, String name) {
			this.clazz = clazz;
			this.type = type;
			this.name = name;
		}
		@Override
		public String toString() {
			return "NBTInfo [type=" + type + ", name=" + name + ", clazz=" + clazz + "]";
		}
	}
	
	public static Integer getTypeID(Class<? extends NBTBase> tagClazz) {
		if(NBTTagEnd.class.isAssignableFrom(tagClazz)) return 0;
		if(NBTTagByte.class.isAssignableFrom(tagClazz)) return 1;
		if(NBTTagShort.class.isAssignableFrom(tagClazz)) return 2;
		if(NBTTagInt.class.isAssignableFrom(tagClazz)) return 3;
		if(NBTTagLong.class.isAssignableFrom(tagClazz)) return 4;
		if(NBTTagFloat.class.isAssignableFrom(tagClazz)) return 5;
		if(NBTTagDouble.class.isAssignableFrom(tagClazz)) return 6;
		if(NBTTagByteArray.class.isAssignableFrom(tagClazz)) return 7;
		if(NBTTagString.class.isAssignableFrom(tagClazz)) return 8;
		if(NBTTagList.class.isAssignableFrom(tagClazz)) return 9;
		if(NBTTagCompound.class.isAssignableFrom(tagClazz)) return 10;
		if(NBTTagIntArray.class.isAssignableFrom(tagClazz)) return 11;
		if(NBTTagLongArray.class.isAssignableFrom(tagClazz)) return 12;
		
		if(registrys!=null) for(Entry<Integer, NBTInfo> nbt : registrys.entrySet()) {
			NBTInfo info = nbt.getValue();
			if(info.clazz.isAssignableFrom(tagClazz)) {
				return nbt.getKey();
			}
		}
		return null;
	}
	
	public static String getTypeName(Class<? extends NBTBase> tagClazz) {
		if(NBTTagEnd.class.isAssignableFrom(tagClazz)) return "TAG_End";
		if(NBTTagByte.class.isAssignableFrom(tagClazz)) return "TAG_Byte";
		if(NBTTagShort.class.isAssignableFrom(tagClazz)) return "TAG_Short";
		if(NBTTagInt.class.isAssignableFrom(tagClazz)) return "TAG_Int";
		if(NBTTagLong.class.isAssignableFrom(tagClazz)) return "TAG_Long";
		if(NBTTagFloat.class.isAssignableFrom(tagClazz)) return "TAG_Float";
		if(NBTTagDouble.class.isAssignableFrom(tagClazz)) return "TAG_Double";
		if(NBTTagByteArray.class.isAssignableFrom(tagClazz)) return "TAG_Byte_Array";
		if(NBTTagString.class.isAssignableFrom(tagClazz)) return "TAG_String";
		if(NBTTagList.class.isAssignableFrom(tagClazz)) return "TAG_List";
		if(NBTTagCompound.class.isAssignableFrom(tagClazz)) return "TAG_Compound";
		if(NBTTagIntArray.class.isAssignableFrom(tagClazz)) return "TAG_Int_Array";
		if(NBTTagLongArray.class.isAssignableFrom(tagClazz)) return "TAG_Long_Array";
		if(NBTBase.class == tagClazz) return "Any Numeric Tag";
		
		if(registrys!=null) for(Entry<Integer, NBTInfo> nbt : registrys.entrySet()) {
			NBTInfo info = nbt.getValue();
			if(info.clazz.isAssignableFrom(tagClazz)) {
				return info.type;
			}
		}
		return "UNKNOWN";
	}
}
