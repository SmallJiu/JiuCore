package cat.jiu.core.util.client.config.entry;

import cat.jiu.core.util.client.config.ConfigEntry;
import cat.jiu.core.util.client.config.GuiButton;
import cat.jiu.core.util.client.config.GuiConfig;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraftforge.common.ForgeConfigSpec;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.function.Function;

/**
 * @author small_jiu
 */
public class EnumEntry<T extends Enum<T>> extends ConfigEntry<T> {
    protected static final HashMap<Class<? extends Enum<?>>, Function<Object, String>> NAME_GETTER = new HashMap<>();
    public static <T extends Enum<?>> void registerNameGetter(Class<T> type, Function<T, String> getter) {
        NAME_GETTER.put(type, o -> getter.apply((T) o));
    }

    private final Button button;
    private final T[] types;
    private int cacheIndex;
    private Component cacheName;
    public EnumEntry(ForgeConfigSpec.EnumValue<T> value, ForgeConfigSpec.ValueSpec spec) {
        super(value, spec);
        this.types = value.get().getDeclaringClass().getEnumConstants();
        this.setCacheValue(value.get());

        this.button = this.addWidget(new GuiButton(0,9999,154, 20, this.cacheName, btn->{
            this.cacheIndex++;
            if (this.cacheIndex >= this.types.length) {
                this.cacheIndex = 0;
            }
            this.setCacheValue(this.types[this.cacheIndex]);
        }){
            @Override
            public Component getMessage() {
                return cacheName;
            }
        });
        this.button.setX(Minecraft.getInstance().getWindow().getGuiScaledWidth()/2 - this.button.getWidth()/2 + this.button.getWidth() - this.button.getWidth()/2 - 2);
        this.addUndoAndReset();
    }

    @Override
    public void render(GuiConfig gui, GuiGraphics graphics, int x, int y, int mouseX, int mouseY) {
        this.renderWidget(gui, graphics, x, y, mouseX, mouseY);
        this.drawAlignRightString(graphics, this.configName, this.button.getX() - 5, this.button.getY() + 5, Color.WHITE.getRGB(), true, gui.getMinecraft().font);
    }

    @Override
    protected void relocation(int x, int y) {
        this.getConfigWidget().setX(x-1);
        this.getConfigWidget().setY(y);
    }

    @Override
    public void drawHoverText(Screen gui, GuiGraphics graphics, int mouseX, int mouseY) {
        try {
            this.drawCommentWithRange(gui, graphics, mouseX, mouseY, this.button.getX() -5-gui.getMinecraft().font.width(this.configName), this.button.getY() +5, gui.getMinecraft().font.width(this.configName), Minecraft.getInstance().font.lineHeight);
            if(this.button.isMouseOver(mouseX, mouseY)) {
                StringJoiner selected = new StringJoiner(ChatFormatting.RESET + ", ", ChatFormatting.RESET + "[ ", ChatFormatting.RESET + " ]");
                for (int i = 0; i < this.types.length; i++) {
                    T t = this.types[i];
                    boolean set = false;
                    String name = "";
                    for (Map.Entry<Class<? extends Enum<?>>, Function<Object, String>> entry : NAME_GETTER.entrySet()) {
                        if(entry.getKey().isAssignableFrom(t.getClass())) {
                            name = I18n.get(entry.getValue().apply(t));
                            set = true;
                        }
                    }
                    if (!set) {
                        name = I18n.get(t.toString());
                    }
                    selected.add((i == this.cacheIndex ? ChatFormatting.RED : ChatFormatting.GREEN) + name);
                }
                graphics.renderComponentTooltip(gui.getMinecraft().font, List.of(Component.literal(selected.toString())), mouseX+5, mouseY);
            }
        } catch (Exception ignored) {}
    }

    @Override
    protected void setCacheValue(T newValue) {
        super.setCacheValue(newValue);
        this.cacheIndex = newValue.ordinal();
        boolean set = false;
        for (Map.Entry<Class<? extends Enum<?>>, Function<Object, String>> entry : NAME_GETTER.entrySet()) {
            if(entry.getKey().isAssignableFrom(newValue.getClass())) {
                this.cacheName = Component.translatable(entry.getValue().apply(newValue));
                set = true;
            }
        }
        if (!set) {
            this.cacheName = Component.translatable(newValue.toString());
        }
    }

    @Override
    protected AbstractWidget getConfigWidget() {
        return this.button;
    }
}
