package cat.jiu.core.api.element;

import cat.jiu.core.api.serializable.ISerializable;
import cat.jiu.core.util.element.Text;
import cat.jiu.sql.SQLValues;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.Font;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextColor;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

public interface IText extends ISerializable {
	String getText();
	IText setText(String text);

	Object[] getParameters();
	IText setParameters(Object... parameters);
	
	boolean isCenter();
	IText setCenter(boolean isCenter);

	boolean isVanillaWrap();
	IText setUseVanillaWrap(boolean isVanillaWrap);
	
	IText copy();

	@OnlyIn(Dist.CLIENT)
	default String format() {
		Object[] parameters = IText.format(this.getParameters());
		String result = I18n.get(this.getText(), parameters);
		if (Objects.equals(result, this.getText())) {
			result = String.format(result, parameters);
		}
		return result;
	}

	@OnlyIn(Dist.CLIENT)
	static Object[] format(Object... args) {
		Object[] arg = Arrays.copyOf(args, args.length);
		for(int i = 0; i < arg.length; i++) {
			Object object = arg[i];
			if(object instanceof IText) {
				arg[i] = ((IText) object).format();
			}else if(object instanceof Component){
				arg[i] = ((Component) object).getString();
			}
		}
		return arg;
	}

	@OnlyIn(Dist.CLIENT)
	default int getStringWidth(Font fr) {
		return fr.width(this.format());
	}

	default Component toTextComponent() {
		return Component.translatable(this.getText(), this.getParameters());
	}
	default Component toTextComponent(ChatFormatting color) {
		Component text = Component.translatable(this.getText(), this.getParameters());
		return text.copy().setStyle(text.getStyle().withColor(color));
	}
	default Component toTextComponent(TextColor color) {
		Component text = Component.translatable(this.getText(), this.getParameters());
		return text.copy().setStyle(text.getStyle().withColor(color));
	}

	default JsonArray writeArgs(JsonArray parametersArray) {
		if(parametersArray==null) parametersArray = new JsonArray();
		for(int i = 0; i < this.getParameters().length; i++) {
			parametersArray.add(argToJson(this.getParameters()[i]));
		}
		return parametersArray;
	}
	static JsonObject argToJson(Object parameter) {
		JsonObject object;
		if (parameter instanceof IText) {
			object = ((IText) parameter).write(new JsonObject());
			object.addProperty("type", (byte) 0);
		} else if (parameter instanceof Component) {
			object = Component.Serializer.toJsonTree((Component) parameter).getAsJsonObject();
			object.addProperty("type", (byte) 1);
		}else {
			object = new JsonObject();
			object.addProperty("parameter", String.valueOf(parameter));
			object.addProperty("type", (byte) 2);
		}
		return object;
	}

	static Object[] readArgs(JsonArray parametersArray) {
		Object[] parameters = new Object[parametersArray.size()];
		for(int i = 0; i < parameters.length; i++) {
			parameters[i] = jsonToArg(parametersArray.get(i).getAsJsonObject());
		}
		return parameters;
	}
	static Object jsonToArg(JsonObject arg) {
		switch (arg.get("type").getAsByte()) {
			case 0: {
				return new Text(arg);
			}
			case 1: {
				return Component.Serializer.fromJson(arg);
			}
			case 2: {
				return arg.get("parameter").getAsString();
			}
		}
		return null;
	}

	default CompoundTag writeArgs(CompoundTag parametersTag) {
		if(parametersTag==null) parametersTag = new CompoundTag();
		for(int i = 0; i < this.getParameters().length; i++) {
			parametersTag.put(String.valueOf(i), argToNBT(this.getParameters()[i]));
		}
		return parametersTag;
	}
	default ListTag writeArgs(ListTag parametersTag) {
		if(parametersTag==null) parametersTag = new ListTag();
		for(int i = 0; i < this.getParameters().length; i++) {
			parametersTag.add(argToNBT(this.getParameters()[i]));
		}
		return parametersTag;
	}

	static CompoundTag argToNBT(Object arg) {
		CompoundTag tag;
		if (arg instanceof IText) {
			tag = ((IText) arg).write(new CompoundTag());
			tag.putByte("type", (byte) 0);
		} else if (arg instanceof Component) {
			tag = new CompoundTag();
			tag.putString("text", Component.Serializer.toJson((Component) arg));
			tag.putByte("type", (byte) 1);
		}else {
			tag = new CompoundTag();
			tag.putString("parameter", String.valueOf(arg));
			tag.putByte("type", (byte) 2);
		}
		return tag;
	}

	static Object[] readArgs(CompoundTag parametersArray){
		Object[] parameters = new Object[parametersArray.size()];
		List<String> keys = parametersArray.getAllKeys().stream().sorted(Comparator.comparingLong(Long::valueOf)).toList();

		for(int i = 0; i < keys.size(); i++) {
			parameters[i] = nbtToArg(parametersArray.getCompound(keys.get(i)));
		}
		return parameters;
	}
	static Object[] readArgs(ListTag parametersArray){
		Object[] parameters = new Object[parametersArray.size()];
		for (int i = 0; i < parametersArray.size(); i++) {
			parameters[i] = nbtToArg(parametersArray.getCompound(i));
		}
		return parameters;
	}
	static Object nbtToArg(CompoundTag tag) {
		switch (tag.getByte("type")) {
			case 0: {
				return new Text(tag);
			}
			case 1: {
				return Component.Serializer.fromJson(tag.getString("text"));
			}
			case 2: {
				return tag.getString("parameter");
			}
		}
		return null;
	}

	@Override
	default void read(JsonObject json) {
		if(json.has("text")) {
			this.setText(json.get("text").getAsString());
		}else if(json.has("key")) {
			this.setText(json.get("key").getAsString());
		}
		
		if(json.has("isVanillaWrap")) this.setUseVanillaWrap(json.get("isVanillaWrap").getAsBoolean());
		if(json.has("isCenter")) this.setCenter(json.get("isCenter").getAsBoolean());
		if(json.has("parameters") || json.has("args")) {
			this.setParameters(readArgs(json.getAsJsonArray(json.has("parameters") ? "parameters" : "args")));
		}
	}

	@Override
	default JsonObject write(JsonObject json) {
		if(json == null)
			json = new JsonObject();
		
		json.addProperty("text", this.getText());
		if(this.isCenter()) json.addProperty("isCenter", this.isCenter());
		if(this.isVanillaWrap()) json.addProperty("isVanillaWrap", this.isVanillaWrap());
		if(this.getParameters()!=null && this.getParameters().length > 0) {
			json.add("parameters", this.writeArgs(new JsonArray()));
		}
		
		return json;
	}

	@Override
	default void read(CompoundTag nbt) {
		this.setText(nbt.getString("text"));
		if(nbt.contains("isVanillaWrap")) this.setUseVanillaWrap(nbt.getBoolean("isVanillaWrap"));
		if(nbt.contains("isCenter")) this.setCenter(nbt.getBoolean("isCenter"));
		if(nbt.contains("parameters")) {
			this.setParameters(readArgs(nbt.getCompound("parameters")));
		}
	}

	@Override
	default CompoundTag write(CompoundTag nbt) {
		if(nbt == null)
			nbt = new CompoundTag();
		
		nbt.putString("text", this.getText());
		if(this.isCenter()) nbt.putBoolean("isCenter", this.isCenter());
		if(this.isVanillaWrap()) nbt.putBoolean("isVanillaWrap", this.isVanillaWrap());
		if(this.getParameters()!=null && this.getParameters().length > 0) {
			nbt.put("parameters", this.writeArgs(new CompoundTag()));
		}
		
		return nbt;
	}
	
	@Override
	default SQLValues write(SQLValues value) {
		return value;
	}
	@Override
	default void read(ResultSet result) throws SQLException {
		
	}

	public static IText create(String k, Object... args) {
		return new Text(k, args);
	}
}
