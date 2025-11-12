package cat.jiu.core.util.client.config.entry;

import cat.jiu.core.util.client.config.ConfigEntry;
import cat.jiu.core.util.client.config.GuiConfig;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.util.StringUtil;
import net.minecraftforge.common.ForgeConfigSpec;

import java.awt.*;
import java.util.Objects;

/**
 * @author small_jiu
 */
public abstract class NumberEntry<T extends Number> extends ConfigEntry<T> {
    protected final EditBox field;
    protected final boolean isDecimal;
    protected String fieldValue;
    protected NumberEntry(ForgeConfigSpec.ConfigValue<T> value, ForgeConfigSpec.ValueSpec spec, boolean isDecimal) {
        super(value, spec);
        this.isDecimal = isDecimal;
        this.field = this.addWidget(new EditBox(Minecraft.getInstance().font, 0,9999,152, 20, CommonComponents.EMPTY));
        this.field.setX(Minecraft.getInstance().getWindow().getGuiScaledWidth()/2 - this.field.getWidth()/2 + this.field.getWidth() - this.field.getWidth()/2 - 1);
        this.field.setMaxLength(Integer.MAX_VALUE);
        this.field.setValue(String.valueOf(this.getCacheValue()));
        this.addUndoAndReset();
        this.undo.setX(this.undo.getX() + 1);
        this.reset.setX(this.reset.getX() + 1);
        this.setCacheValue(this.getCacheValue());
    }

    @Override
    protected AbstractWidget getConfigWidget() {
        return this.field;
    }

    @Override
    public void render(GuiConfig gui, GuiGraphics graphics, int x, int y, int mouseX, int mouseY) {
        this.renderWidget(gui, graphics, x, y, mouseX, mouseY);
        this.drawAlignRightString(graphics, this.configName, this.field.getX() - 5, this.field.getY() + 5, Color.WHITE.getRGB(), true, gui.getMinecraft().font);

        if (this.isChanged()) {
            boolean canSave = false;
            try {
                canSave = this.parse(this.field.getValue()) != null;
            } catch (Exception ignored) {}
            drawHollowSquare(graphics,
                    this.getConfigWidget().getX(), this.getConfigWidget().getY(),
                    this.getConfigWidget().getWidth()-1, this.getConfigWidget().getHeight()-1,
                    (canSave ? Color.GREEN : Color.RED).getRGB()
            );
        }
    }

    @Override
    protected void relocation(int x, int y) {
        this.getConfigWidget().setX(x);
        this.getConfigWidget().setY(y);
    }

    @Override
    public void drawHoverText(Screen gui, GuiGraphics graphics, int mouseX, int mouseY) {
        try {
            this.drawCommentWithRange(gui, graphics, mouseX, mouseY,
                    this.field.getX() -5-gui.getMinecraft().font.width(this.configName), this.field.getY() +5,
                    gui.getMinecraft().font.width(this.configName), gui.getMinecraft().font.lineHeight);
        } catch (Exception ignored) {}
    }

    @Override
    protected void setCacheValue(T newValue) {
        super.setCacheValue(newValue);
        if (this.field!=null) {
            this.field.setValue(String.valueOf(newValue));
            this.fieldValue = this.field.getValue();
        }
    }

    protected abstract T parse(String value);

    @Override
    public void save() {
        try {
            this.setCacheValue(this.parse(StringUtil.isNullOrEmpty(this.field.getValue()) ? "0" : this.field.getValue()));
        }catch (Exception ignored){}
        super.save();
    }

    @Override
    public boolean isChanged() {
        return super.isChanged() || !Objects.equals(this.field.getValue(), this.fieldValue);
    }
}
