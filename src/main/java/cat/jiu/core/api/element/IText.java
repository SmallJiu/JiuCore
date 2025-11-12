package cat.jiu.core.api.element;

import cat.jiu.core.api.IData;
import cat.jiu.core.api.serializable.IDataSerializable;
import cat.jiu.core.util.client.RenderUtils;
import cat.jiu.core.util.element.Text;
import cat.jiu.core.util.element.data.JsonData;
import cat.jiu.core.util.element.data.NBTData;
import cat.jiu.sql.SQLValues;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TextColor;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.codehaus.plexus.util.dag.DAG;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

public interface IText extends IDataSerializable<IData.IMapData<?>> {
	static IText create(String k, Object... args) {
		return new Text(k, args);
	}
	static IText create(Player player, String k, Object... args) {
		return new Text(k, args).send(player);
	}
	static IText create(Player player, ChatFormatting color, String k, Object... args) {
		return new Text(k, args).send(player, color);
	}
	static IText create(IData<?> data) {
		return IText.create(new Text(""), data);
	}
	static IText create(IText instance, IData<?> data) {
		instance.dynamicRead(data);
		return instance;
	}

	String getText();
	IText setText(String text);

	Object[] getParameters();
	IText setParameters(Object... parameters);
	
	default boolean isCenter() {
		return false;
	}
	default IText setCenter(boolean isCenter) {
		return this;
	}

	default boolean isVanillaWrap() {
		return true;
	}
	default IText setUseVanillaWrap(boolean isVanillaWrap){
		return this;
	}

	default boolean isScrollText() {
		return false;
	}
	default IText setScrollText(boolean scroll) {
		return this;
	}

	default boolean isAlignRightRender(){
		return false;
	}
	default IText setAlignRightRender(boolean alignRight) {
		return this;
	}

	default boolean isEmpty(){
		return this == Text.empty || this.getText().isEmpty();
	}

	IText copy();

	@OnlyIn(Dist.CLIENT)
	default String format() {
		Object[] parameters = IText.format(this.getParameters());
		String result = I18n.get(this.getText(), parameters);
		if (Objects.equals(result, this.getText())) {
			result = String.format(this.getText(), parameters);
		}
		return result;
	}

	@OnlyIn(Dist.CLIENT)
	default int render(GuiGraphics graphics, int x, int y, int color, boolean drawShadow) {
		return this.render(graphics, x, y, this.getStringWidth(Minecraft.getInstance().font), color, drawShadow);
	}
	@OnlyIn(Dist.CLIENT)
	default int render(GuiGraphics graphics, int x, int y, int width, int color, boolean drawShadow) {
		int height = 0;
		if (this.isScrollText()) {
			RenderUtils.renderScrollingComponent(graphics, toTextComponent(), x, y, width, color, drawShadow);
			height = RenderUtils.fontHeight();
		}else {
			List<FormattedCharSequence> texts = RenderUtils.split(this.toTextComponent(), width, this.isVanillaWrap());
			for (FormattedCharSequence text : texts) {
				if (this.isCenter()) {
					RenderUtils.drawCenteredSequence(graphics, text, x + width/2, y, color, drawShadow);
				}else {
					if (this.isAlignRightRender()) {
						RenderUtils.drawRightSequence(graphics, text, x + width, y, color, drawShadow);
					}else {
						graphics.drawString(RenderUtils.getFontRenderer(), text, x, y, color, drawShadow);
					}
				}
				y += RenderUtils.getFontHeight() + 1;
				height += RenderUtils.fontHeight() + 1;
			}
		}
		return height;
	}

	default IText send(Player player) {
		return this.send(player, false);
	}
	default IText send(Player player, boolean actionBar) {
		player.displayClientMessage(this.toTextComponent(), actionBar);
		return this;
	}
	default IText send(Player player, ChatFormatting color) {
		return this.send(player, color, false);
	}
	default IText send(Player player, ChatFormatting color, boolean actionBar) {
		player.displayClientMessage(this.toTextComponent(color), actionBar);
		return this;
	}

	@OnlyIn(Dist.CLIENT)
	default List<FormattedCharSequence> split(int width) {
		return RenderUtils.split(this.toTextComponent(), width, this.isVanillaWrap());
	}

	static Object[] format(Object... args) {
		Object[] arg = Arrays.copyOf(args, args.length);
		for(int i = 0; i < arg.length; i++) {
			Object object = arg[i];
			if(object instanceof IText) {
				arg[i] = ((IText) object).toTextComponent().getString();
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
	@OnlyIn(Dist.CLIENT)
	default int getStringHeight(int width) {
		if (this.isScrollText()) {
			return RenderUtils.fontHeight() + 1;
		}else {
			return (RenderUtils.fontHeight() + 1) * RenderUtils.split(this.toTextComponent(), width, this.isVanillaWrap()).size();
		}
	}

	default Component toTextComponent() {
		return Component.translatable(this.getText(), IText.format(this.getParameters()));
	}
	default Component toTextComponent(ChatFormatting color) {
		Component text = this.toTextComponent();
		return ((MutableComponent)text).setStyle(text.getStyle().withColor(color));
	}
	default Component toTextComponent(TextColor color) {
		Component text = this.toTextComponent();
		return ((MutableComponent)text).setStyle(text.getStyle().withColor(color));
	}

	default IData<?> dynamicWrite(IData.IMaker maker) {
		if (this.getParameters()!=null && this.getParameters().length > 0) {
			return this.write(maker.newMap());
		}else  {
			return maker.newPrimitive().setData(this.getText());
		}
	}

	default void dynamicRead(IData<?> data) {
		if (data.isMap()) {
			this.read(data.getAsMap());
		}else if (data.isPrimitive()) {
			this.setText(data.getAsPrimitive().getAsString());
		}
	}

	@Override
	default IData.IMapData<?> write(IData.IMapData<?> data) {
		data.putData("text", this.getText());
		if(this.getParameters()!=null && this.getParameters().length > 0) {
			data.putData("parameters", this.writeArgs(data.newList()));
		}
		if(this.isCenter()) {
			data.putData("isCenter", this.isCenter());
		}
		if(this.isVanillaWrap()) {
			data.putData("isVanillaWrap", this.isVanillaWrap());
		}
		if(this.isScrollText()) {
			data.putData("isScrollText", this.isScrollText());
		}
		if(this.isAlignRightRender()) {
			data.putData("isAlignRight", this.isAlignRightRender());
		}
		return data;
	}

	default IData.IListData<?> writeArgs(IData.IListData<?> data) {
		for(int i = 0; i < this.getParameters().length; i++) {
			data.putData(argToData(data.newMap(), this.getParameters()[i]));
		}
		return data;
	}
	static IData.IMapData<?> argToData(IData.IMapData<?> mapData, Object parameter) {
		if (parameter instanceof IText) {
			((IText) parameter).write(mapData);
			mapData.putData("type", (byte) 0);
		} else if (parameter instanceof Component) {
			mapData.putData("text", JsonData.map(JsonParser.parseString(Component.Serializer.toJson((Component) parameter)).getAsJsonObject()).transfer(mapData.newMap()));
			mapData.putData("type", (byte) 1);
		}else {
			mapData.putData("parameter", String.valueOf(parameter));
			mapData.putData("type", (byte) 2);
		}
		return mapData;
	}

	@Override
	default void read(IData.IMapData<?> data) {
		if(data.containsKey("text")) {
			this.setText(data.getString("text", ""));
		}else if(data.containsKey("key")) {
			this.setText(data.getString("key", ""));
		}
		if(data.containsKey("parameters")) {
			this.setParameters(readArgs(data.getList("parameters", IData.IMapData.class, data.emptyList())));
		}else if (data.containsKey("args")) {
			this.setParameters(readArgs(data.getList("args", IData.IMapData.class, data.emptyList())));
		}

		if(data.containsKey("isVanillaWrap")) {
			this.setUseVanillaWrap(data.getBoolean("isVanillaWrap", false));
		}
		if(data.containsKey("isCenter")) {
			this.setCenter(data.getBoolean("isCenter", false));
		}
		if(data.containsKey("isScrollText")) {
			this.setScrollText(data.getBoolean("isScrollText", false));
		}
		if(data.containsKey("isAlignRight")) {
			this.setAlignRightRender(data.getBoolean("isAlignRight", false));
		}
	}

	static Object[] readArgs(IData.IListData<?> data) {
		Object[] parameters = new Object[data.size()];
		for(int i = 0; i < parameters.length; i++) {
			parameters[i] = dataToArg(data.getMap(i, data.emptyMap()));
		}
		return parameters;
	}
	static Object dataToArg(IData.IMapData<?> data) {
		switch (data.getByte("type", (byte)2)) {
			case 0: {
				return new Text(data);
			}
			case 1: {
				IData<?> mapData = data.getData("text", data.newMap().putData("text", "notfound"));
				return Component.Serializer.fromJson((JsonElement) JsonData.EMPTY_MAP.castData(mapData).getData());
			}
			case 2: {
				return data.getString("parameter", "notfound");
			}
		}
		return null;
	}

	@Deprecated(since = "1.20.1-0.0.1-2025.8.10")
	default void read(JsonObject data) {
		this.read(JsonData.map(data));
	}

	@Deprecated(since = "1.20.1-0.0.1-2025.8.10")
	default JsonObject write(JsonObject data) {
		this.write(JsonData.map(data));
		return data;
	}

	@Deprecated(since = "1.20.1-0.0.1-2025.8.10")
	default void read(CompoundTag nbt) {
		this.read(NBTData.map(nbt));
	}

	@Deprecated(since = "1.20.1-0.0.1-2025.8.10")
	default CompoundTag write(CompoundTag nbt) {
		this.write(NBTData.map(nbt));
		return nbt;
	}

	@Deprecated(since = "1.20.1-0.0.1-2025.8.10")
	default SQLValues write(SQLValues value) {
		return value;
	}
	@Deprecated(since = "1.20.1-0.0.1-2025.8.10")
	default void read(ResultSet result) throws SQLException {

	}

	@Deprecated(since = "1.20.1-0.0.1-2025.8.10")
	default JsonArray writeArgs(JsonArray parametersArray) {
		if(parametersArray==null) parametersArray = new JsonArray();
		for(int i = 0; i < this.getParameters().length; i++) {
			parametersArray.add(argToJson(this.getParameters()[i]));
		}
		return parametersArray;
	}
	@Deprecated(since = "1.20.1-0.0.1-2025.8.10")
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

	@Deprecated(since = "1.20.1-0.0.1-2025.8.10")
	static Object[] readArgs(JsonArray parametersArray) {
		Object[] parameters = new Object[parametersArray.size()];
		for(int i = 0; i < parameters.length; i++) {
			parameters[i] = jsonToArg(parametersArray.get(i).getAsJsonObject());
		}
		return parameters;
	}
	@Deprecated(since = "1.20.1-0.0.1-2025.8.10")
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

	@Deprecated(since = "1.20.1-0.0.1-2025.8.10")
	default CompoundTag writeArgs(CompoundTag parametersTag) {
		if(parametersTag==null) parametersTag = new CompoundTag();
		for(int i = 0; i < this.getParameters().length; i++) {
			parametersTag.put(String.valueOf(i), argToNBT(this.getParameters()[i]));
		}
		return parametersTag;
	}
	@Deprecated(since = "1.20.1-0.0.1-2025.8.10")
	default ListTag writeArgs(ListTag parametersTag) {
		if(parametersTag==null) parametersTag = new ListTag();
		for(int i = 0; i < this.getParameters().length; i++) {
			parametersTag.add(argToNBT(this.getParameters()[i]));
		}
		return parametersTag;
	}

	@Deprecated(since = "1.20.1-0.0.1-2025.8.10")
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

	@Deprecated(since = "1.20.1-0.0.1-2025.8.10")
	static Object[] readArgs(CompoundTag parametersArray){
		Object[] parameters = new Object[parametersArray.size()];
		List<String> keys = parametersArray.getAllKeys().stream().sorted(Comparator.comparingLong(Long::valueOf)).toList();

		for(int i = 0; i < keys.size(); i++) {
			parameters[i] = nbtToArg(parametersArray.getCompound(keys.get(i)));
		}
		return parameters;
	}
	@Deprecated(since = "1.20.1-0.0.1-2025.8.10")
	static Object[] readArgs(ListTag parametersArray){
		Object[] parameters = new Object[parametersArray.size()];
		for (int i = 0; i < parametersArray.size(); i++) {
			parameters[i] = nbtToArg(parametersArray.getCompound(i));
		}
		return parameters;
	}
	@Deprecated(since = "1.20.1-0.0.1-2025.8.10")
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
}
