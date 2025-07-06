package cat.jiu.core.util.client.config;

import cat.jiu.core.util.client.config.entry.SubEntry;
import com.google.common.collect.Lists;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraft.client.gui.components.Button;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * @author small_jiu
 */
public abstract class ConfigEntry<T> {
    protected final ForgeConfigSpec.ConfigValue<T> value;
    protected final ForgeConfigSpec.ValueSpec spec;
    protected final T defaultValue;
    protected final String configName;
    protected final List<AbstractWidget> widgets = new ArrayList<>();
    protected Button undo, reset;
    protected T cache;
    public boolean canEdit;

    protected ConfigEntry(ForgeConfigSpec.ConfigValue<T> value, ForgeConfigSpec.ValueSpec spec) {
        this.value = value;
        this.spec = spec;
        if(value!=null) this.setCacheValue(value.get());
        String name;
        if(this.spec==null){
            name = "Sub Entry";
        }else if(this.spec.getTranslationKey()!=null){
            name = I18n.get(this.spec.getTranslationKey());
            if(Objects.equals(name, this.spec.getTranslationKey())){
                name = this.value.getPath().get(this.value.getPath().size()-1);
            }
        }else {
            name = this.value.getPath().get(this.value.getPath().size()-1);
        }
        this.configName = name;

        T defaultValue;
        try {
            defaultValue = this.value.getDefault();
        } catch (Exception e) {
            defaultValue = null;
        }
        this.defaultValue = defaultValue;
    }

    public void drawAlignRightString(GuiGraphics graphics, String text, int x, int y, int color, boolean drawShadow, Font font) {
        graphics.drawString(font, text, x - font.width(text), y, color);
    }

    protected final void addUndoAndReset(){
        if(this.getConfigWidget()!=null){
            this.undo = this.addWidget(new GuiButton(this.getConfigWidget().getX() +this.getConfigWidget().getWidth()+2, 0, 20, 20, Component.nullToEmpty("U"), btn->this.undo()));
            this.reset = this.addWidget(new GuiButton(this.undo.getX() +this.undo.getWidth()+2, 0, 20, 20, Component.nullToEmpty("R"), btn->this.reset()));
        }
        if (this.value != null) {
            this.setCacheValue(this.value.get());
        }
    }

    public abstract void render(GuiConfig gui, GuiGraphics graphics, int x, int y, int mouseX, int mouseY);
    protected T getCacheValue() {
        return this.cache;
    }
    protected void setCacheValue(T newValue) {
        this.cache = newValue;
    }
    protected abstract AbstractWidget getConfigWidget();

    protected void renderWidget(GuiConfig gui, GuiGraphics graphics, int x, int y, int mouseX, int mouseY){
        this.canEdit = gui.isCanEdit();
        this.widgets.forEach(widget -> {
            if (!(this instanceof SubEntry) && widget != this.undo && widget != this.reset) {
                widget.active = this.canEdit;
            }
            widget.setY(y);
            widget.render(graphics, mouseX, mouseY, 0);
        });
        if(this.undo!=null){
            this.undo.active = this.isChanged() && this.canEdit;
            if(this.reset.active && this.undo.isMouseOver(mouseX, mouseY)){
                graphics.renderTooltip(gui.getMinecraft().font, Component.translatable("info.config.undo"), mouseX, mouseY);
            }
        }
        if(this.reset!=null){
            this.reset.active = this.isDefault() && this.canEdit;
            if(this.reset.active && this.reset.isMouseOver(mouseX, mouseY)){
                graphics.renderTooltip(gui.getMinecraft().font, Component.translatable("info.config.reset"), mouseX, mouseY);
            }
        }
    }

    protected <T2 extends AbstractWidget> T2 addWidget(T2 w){
        this.widgets.add(w);
        return w;
    }

    public void undo(){
        this.setCacheValue(this.value.get());
    }
    public void reset(){
        this.setCacheValue(this.defaultValue);
    }
    public boolean isChanged() {
        return !Objects.equals(this.getCacheValue(), this.value.get());
    }
    public boolean isDefault() {
        return !Objects.equals(this.getCacheValue(), this.defaultValue);
    }
    public void save() {
        this.value.set(this.getCacheValue());
    }

    public int getWeight(){
        return 1;
    }

    public void setUnFocused() {
        for (AbstractWidget widget : this.widgets) {
            widget.setFocused(false);
        }
    }

    public boolean isMouseOver(int x, int y) {
        boolean result = false;
        for (AbstractWidget widget : this.widgets) {
            if (widget.isMouseOver(x, y)) {
                result = true;
            }
        }
        return result;
    }

    public boolean mouseClick(double mouseX, double mouseY, int button){
        boolean flag = false;
        for (AbstractWidget widget : this.widgets) {
            widget.setFocused(false);
            if (!this.canEdit && !(this instanceof SubEntry)) {
                continue;
            }
            if(!flag && widget.mouseClicked(mouseX, mouseY, button)){
                widget.setFocused(true);
                flag =  true;
            }
        }
        return flag;
    }
    public boolean charTyped(char codePoint, int modifiers) {
        if (!this.canEdit) return false;
        for (AbstractWidget widget : this.widgets) {
            if(widget.charTyped(codePoint, modifiers)){
                return true;
            }
        }
        return false;
    }
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (!this.canEdit) return false;
        for (AbstractWidget widget : this.widgets) {
            if(widget.keyPressed(keyCode, scanCode, modifiers)){
                return true;
            }
        }
        return false;
    }

    public void drawHoverText(Screen gui, GuiGraphics graphics, int mouseX, int mouseY){

    }

    protected void drawCommentWithRange(Screen gui, GuiGraphics graphics, int mouseX, int mouseY, int x, int y, int width, int height) throws Exception {
        if(isInRange(mouseX, mouseY, x, y, width, height)){
            this.drawComment(gui, graphics, mouseX, mouseY);
        }
    }
    protected List<Component> extraComment(Screen gui, GuiGraphics graphics) {
        return Collections.emptyList();
    }

    protected void drawComment(Screen gui, GuiGraphics graphics, int mouseX, int mouseY) throws Exception {
        List<Component> comments = Lists.newArrayList();

        comments.add(Component.literal(ChatFormatting.GREEN + this.configName));
        comments.addAll(this.extraComment(gui, graphics));
        comments.add(CommonComponents.EMPTY);

        if(spec.getComment() != null){
            for (String s1 : this.spec.getComment().split("\n")) {
                if (!(s1.isEmpty() || s1.length() == 1)) {
                    comments.add(Component.literal(ChatFormatting.YELLOW + I18n.get(s1)));
                }
            }
            if(this.spec.getRange()!=null){
                comments.remove(comments.size()-1);
            }
            if(spec.getDefault() instanceof Enum<?>) {
                comments.remove(comments.size()-1);
            }
            comments.add(CommonComponents.EMPTY);
        }
        if(this.spec.getRange()!=null){
            Class<?> clazz = null;
            for (Class<?> c : ForgeConfigSpec.class.getDeclaredClasses()) {
                if("Range".equalsIgnoreCase(c.getSimpleName())){
                    clazz = c;
                    break;
                }
            }

            Method getMin = clazz.getDeclaredMethod("getMin");
            getMin.setAccessible(true);
            Object min = getMin.invoke(this.spec.getRange());

            Method getMax = clazz.getDeclaredMethod("getMax");
            getMax.setAccessible(true);
            Object max = getMax.invoke(this.spec.getRange());

            boolean show = true;
            if (
                    min instanceof Integer && ((Integer)min) == Integer.MIN_VALUE
                    && max instanceof Integer && ((Integer)max) == Integer.MAX_VALUE) {
                show = false;
            }else if (
                    min instanceof Long && ((Long)min) == Long.MIN_VALUE
                    && max instanceof Long && ((Long)max) == Long.MAX_VALUE) {
                show = false;
            }

            if (show) {
                comments.add(Component.literal(String.format(ChatFormatting.AQUA + "range: %s ~ %s", min, max)));
            }
        }
        comments.add(Component.literal(ChatFormatting.AQUA + String.format("default: %s", this.spec.getDefault())));
        if(this.spec.needsWorldRestart()) {
            comments.add(Component.literal(ChatFormatting.RED + I18n.get("info.config.world_restart")));
        }

        graphics.renderComponentTooltip(gui.getMinecraft().font, comments, mouseX+5, mouseY);
    }

    public static boolean isInRange(double mouseX, double mouseY, int x, int y, int width, int height) {
        int maxX = x + width;
        int maxY = y + height;
        return (mouseX >= x && mouseY >= y) && (mouseX <= maxX && mouseY <= maxY);
    }
}
