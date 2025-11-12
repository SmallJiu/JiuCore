package cat.jiu.core.util.element;

import cat.jiu.core.api.IData;
import cat.jiu.core.api.element.IText;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TextColor;
import net.minecraft.network.chat.contents.LiteralContents;
import net.minecraft.network.chat.contents.TranslatableContents;

import java.util.Arrays;

public class Text implements IText {
	public static final Object[] EMPTY_ARGS = new Object[0];
	public static final Text empty = new Text("") {
		public Text setText(String key) {return this;}
	};
	
	protected String key = "";
	protected Object[] args = EMPTY_ARGS;
	protected boolean center;
	protected boolean vanillaWrap;
	protected boolean isScrollText;
	protected boolean isAlignRight;
	protected MutableComponent component;

	public Text(Component component){
		if (component instanceof MutableComponent mutable) {
			if (mutable.getContents() instanceof TranslatableContents contents) {
				this.setText(contents.getKey());
				this.setParameters(contents.getArgs());
			}else if (mutable.getContents() instanceof LiteralContents contents) {
				this.setText(contents.text());
			}else {
				this.setText(component.getString());
			}
		}else {
			this.setText(component.getString());
		}
	}

	public Text(String key, Object... args) {
		this.setText(key);
		if(args!=null&&args.length>0) {
			this.setParameters(args);
		}
	}
	public Text(JsonObject json) {
		this.read(json);
	}
	public Text(CompoundTag nbt) {
		this.read(nbt);
	}
	public Text(IData.IMapData<?> data) {
		this.read(data);
	}

	public String getText() {
		return key;
	}
	public Text setText(String key) {
		this.key = key;
		return this;
	}
	public Object[] getParameters() {
		return args;
	}
	@Override
	public Text setParameters(Object... parameters) {
		this.args = parameters;
		return this;
	}
	
	@Override
	public boolean isCenter() {
		return this.center;
	}
	@Override
	public Text setCenter(boolean isCenter) {
		this.center = isCenter;
		return this;
	}
	
	@Override
	public boolean isVanillaWrap() {
		return this.vanillaWrap;
	}
	@Override
	public Text setUseVanillaWrap(boolean isVanillaWrap) {
		this.vanillaWrap = isVanillaWrap;
		return this;
	}

	@Override
	public boolean isScrollText() {
		return this.isScrollText;
	}
	@Override
	public Text setScrollText(boolean scroll) {
		this.isScrollText = scroll;
		return this;
	}

	@Override
	public boolean isAlignRightRender() {
		return this.isAlignRight;
	}

	@Override
	public Text setAlignRightRender(boolean alignRight) {
		this.isAlignRight = alignRight;
		return this;
	}

	public Text copy() {
		return new Text(this.write(new CompoundTag()));
	}

	@Override
	public MutableComponent toTextComponent() {
		if (this.component == null) {
			this.component = Component.translatable(this.getText(), IText.format(this.getParameters()));
		}
		return this.component;
	}

	@Override
	public MutableComponent toTextComponent(ChatFormatting color) {
		this.toTextComponent();
		this.component.setStyle(this.component.getStyle().applyFormat(color));
		return this.component;
	}

	@Override
	public MutableComponent toTextComponent(TextColor color) {
		this.toTextComponent();
		this.component.setStyle(this.component.getStyle().withColor(color));
		return this.component;
	}

	@Override
	public String toString() {
		return String.valueOf(this.write(new JsonObject()));
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(args);
		result = prime * result + ((key == null) ? 0 : key.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if(this == obj)
			return true;
		if(obj == null)
			return false;
		if(getClass() != obj.getClass())
			return false;
		Text other = (Text) obj;
		if(!Arrays.equals(args, other.args))
			return false;
		if(key == null) {
			return other.key == null;
		}else return key.equals(other.key);
	}
}
