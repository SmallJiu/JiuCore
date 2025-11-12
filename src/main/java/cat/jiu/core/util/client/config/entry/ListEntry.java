package cat.jiu.core.util.client.config.entry;

import cat.jiu.core.util.client.config.ConfigEntry;
import cat.jiu.core.util.client.config.GuiButton;
import cat.jiu.core.util.client.config.GuiConfig;
import com.mojang.blaze3d.platform.Window;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.ObjectSelectionList;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraftforge.common.ForgeConfigSpec;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

public class ListEntry<T> extends ConfigEntry<List<T>> {
    protected static final HashMap<Class<?>, Function<TypeContext, ?>> GETTER = new HashMap<>();
    public static <T> void registerTypeGetter(Class<T> type, Function<TypeContext, T> getter) {
        GETTER.put(type, getter);
    }
    protected static final HashMap<Class<?>, Function<Class<?>, List<String>>> CUSTOM_SELECT = new HashMap<>();
    public static void registerTypeValueGetter(Class<?> type, Function<Class<?>, List<String>> getter) {
        CUSTOM_SELECT.put(type, getter);
    }
    protected static final HashMap<Class<?>, Function<String, String>> NAME_GETTER = new HashMap<>();
    public static void registerTypeNameGetter(Class<?> type, Function<String, String> getter) {
        NAME_GETTER.put(type, getter);
    }
    public static class TypeContext {
        public final String str;
        public final Class<?> type;
        public TypeContext(String str, Class<?> type) {
            this.str = str;
            this.type = type;
        }
    }

    static {
        registerTypeGetter(Integer.class, ctx -> Integer.valueOf(ctx.str));
        registerTypeGetter(Long.class, ctx -> Long.valueOf(ctx.str));
        registerTypeGetter(Float.class, ctx -> Float.valueOf(ctx.str));
        registerTypeGetter(Double.class, ctx -> Double.valueOf(ctx.str));
        registerTypeGetter(Boolean.class, ctx -> Boolean.valueOf(ctx.str));
        registerTypeGetter(String.class, ctx -> ctx.str);
        registerTypeGetter(Enum.class, ctx->{
            for (Object type : ctx.type.getEnumConstants()) {
                if (type.toString().equalsIgnoreCase(ctx.str)) {
                    return (Enum<?>)type;
                }
            }
            return null;
        });
        registerTypeValueGetter(Enum.class, clazz -> {
            List<String> values = new ArrayList<>();
            for (Object type : clazz.getEnumConstants()) {
                values.add(String.valueOf(type).toLowerCase());
            }
            return values;
        });
    }

    protected final String path;
    protected final Button button;
    protected final Class<?> type;
    public ListEntry(GuiConfig parent, ForgeConfigSpec.ConfigValue<List<T>> value, ForgeConfigSpec.ValueSpec spec) {
        super(value, spec);
        this.path = parent.getPath() + " > " + value.getPath().get(value.getPath().size()-1);
        this.type = this.defaultValue.get(0).getClass();
        this.button = this.addWidget(new GuiButton(0, 9999, 304, 20, Component.translatable(this.configName), btn-> parent.getMinecraft().setScreen(new ListScreen<>(parent, this.path, this.getCacheValue(), this.type))));
        this.button.setX(Minecraft.getInstance().getWindow().getGuiScaledWidth()/2 - this.button.getWidth()/2+2);
        this.addUndoAndReset();
    }

    @Override
    public void render(GuiConfig gui, GuiGraphics graphics, int x, int y, int mouseX, int mouseY) {
        this.renderWidget(gui, graphics, x, y, mouseX, mouseY);
    }

    @Override
    protected void relocation(int x, int y) {
        this.getConfigWidget().setX(x - this.getConfigWidget().getWidth()/2);
        this.getConfigWidget().setY(y);
    }

    @Override
    public void drawHoverText(Screen gui, GuiGraphics graphics, int mouseX, int mouseY) {
        try {
            if (this.button.isMouseOver(mouseX, mouseY)) {
                this.drawComment(gui, graphics, mouseX, mouseY);
            }
        } catch (Exception ignored) {}
    }

    @Override
    protected List<Component> extraComment(Screen gui, GuiGraphics graphics) {
        StringJoiner values = new StringJoiner(", ", "values: [ ", " ]");
        for (int i = 0; i < this.getCacheValue().size(); i++) {
            values.add(String.valueOf(this.getCacheValue().get(i)));
        }
        return Collections.singletonList(Component.literal(values.toString()));
    }

    @Override
    protected void setCacheValue(List<T> newValue) {
        if (this.getCacheValue()==null) super.setCacheValue(new ArrayList<>());
        this.getCacheValue().clear();
        this.getCacheValue().addAll(newValue);
    }

    @Override
    protected AbstractWidget getConfigWidget() {
        return this.button;
    }

    public static class ListScreen<T> extends Screen {
        protected final GuiConfig parent;
        protected final String path;
        protected final List<T> list;
        protected final List<String> lazyAdded = new ArrayList<>();
        protected final List<EditBox> edits = new ArrayList<>();
        protected final Class<?> type;
        protected ValuePanel<T> panel;
        protected Button confirm, cancel, clear;
        protected ListScreen(GuiConfig parent, String path, List<T> list, Class<?> type) {
            super(CommonComponents.EMPTY);
            this.parent = parent;
            this.path = path;
            this.list = list;
            this.type = type;
        }

        public T get(int index) {
            String s = this.panel.getEditbox(index).getValue();
            return this.get(s);
        }
        @SuppressWarnings("unchecked")
        public T get(String s) {
            if (s==null || s.isEmpty()) return null;
            for (Map.Entry<Class<?>, Function<TypeContext, ?>> entry : ListEntry.GETTER.entrySet()) {
                if (entry.getKey().isAssignableFrom(this.type)) {
                    return (T) entry.getValue().apply(new TypeContext(s, this.type));
                }
            }
            return null;
        }

        public void remove(EditBox boxIndex, ValuePanel.ValueEntry<T> entry) {
            for (int i = 0; i < this.edits.size(); i++) {
                if(this.edits.get(i).getValue().equals(boxIndex.getValue())) {
                    this.edits.remove(i);
                    if (i > this.list.size()) {
                        this.lazyAdded.remove(i - this.list.size());
                    }
                    break;
                }
            }
            this.panel.removeEntry(entry);
        }

        @Override
        protected void init() {
            Window window = this.getMinecraft().getWindow();
            this.cancel = this.addRenderableWidget(new GuiButton(window.getGuiScaledWidth()/2 - 50, window.getGuiScaledHeight() - 25, 100, 20, CommonComponents.GUI_BACK,btn->
                    this.getMinecraft().setScreen(this.parent)
            ));
            this.confirm = this.addRenderableWidget(new GuiButton(this.cancel.getX() - 102, this.cancel.getY(), 100, 20, CommonComponents.GUI_DONE, btn->{
                this.list.clear();
                for (int i = 0; i < this.edits.size(); i++) {
                    try {
                        T t = this.get(i);
                        if (t != null) {
                            this.list.add(t);
                        }
                    }catch (Exception ignored){}
                }
                this.getMinecraft().setScreen(this.parent);
            }));
            this.clear = this.addRenderableWidget(new GuiButton(this.cancel.getX() + 102, this.cancel.getY(), 100, 20, Component.translatable("info.config.clear"), btn-> {
                this.edits.clear();
                this.panel.clearEntries();
            }));
            this.clear.active = false;

            this.panel = this.addRenderableWidget(new ValuePanel<>(this, this.type, this.list, this.lazyAdded));
        }

        @Override
        public void render(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
            super.renderDirtBackground(pGuiGraphics);
            super.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
            this.clear.active = !this.edits.isEmpty();

            int y = 5;
            pGuiGraphics.drawCenteredString(this.font, this.parent.configFile, this.getMinecraft().getWindow().getGuiScaledWidth() / 2, y, Color.WHITE.getRGB());
            y += this.font.lineHeight;
            if(this.font.width(this.path) > this.getMinecraft().getWindow().getGuiScaledWidth()/2){
                List<String> texts = GuiConfig.splitString(this.path, this.getMinecraft().getWindow().getGuiScaledWidth());
                for (String text : texts) {
                    pGuiGraphics.drawCenteredString(this.font, text, this.getMinecraft().getWindow().getGuiScaledWidth() / 2, y, Color.WHITE.getRGB());
                    y += this.font.lineHeight;
                }
            }else {
                pGuiGraphics.drawCenteredString(this.font, this.path, this.getMinecraft().getWindow().getGuiScaledWidth() / 2, 5 + this.font.lineHeight, Color.WHITE.getRGB());
                y += this.font.lineHeight;
            }
            pGuiGraphics.drawCenteredString(this.font, "Type: " + ChatFormatting.AQUA + this.type.getTypeName(), this.getMinecraft().getWindow().getGuiScaledWidth() / 2, y, Color.WHITE.getRGB());
        }

        @Override
        public void onClose() {
            this.parent.getMinecraft().setScreen(this.parent);
        }

        public static class ValuePanel<T> extends ObjectSelectionList<ValuePanel.ValueEntry<T>> {
            protected final ListScreen<T> gui;
            protected Button add;
            public ValuePanel(ListScreen<T> gui, Class<?> type, List<T> list, List<String> lazyAdded) {
                super(Minecraft.getInstance(), Minecraft.getInstance().getWindow().getGuiScaledWidth(), Minecraft.getInstance().getWindow().getGuiScaledHeight() - 60, 35, 30+Minecraft.getInstance().getWindow().getGuiScaledHeight() - 60, 23);
                this.gui = gui;
                this.add = new GuiButton(0, 0, 200, 20, Component.literal("+"), btn-> {
                    List<String> custom_values = null;
                    for (Map.Entry<Class<?>, Function<Class<?>, List<String>>> entry : CUSTOM_SELECT.entrySet()) {
                        if (entry.getKey().isAssignableFrom(type)) {
                            if (custom_values==null) {
                                custom_values = new ArrayList<>();
                            }
                            custom_values.addAll(entry.getValue().apply(type));
                        }
                    }
                    if (custom_values == null || custom_values.isEmpty()) {
                        this.addEntry((String) null);
                        lazyAdded.add("");
                    }else {
                        Minecraft.getInstance().setScreen(new SelectValueScreen<>(this.gui, custom_values, lazyAdded::add));
                    }
                });
                this.add.setX(Minecraft.getInstance().getWindow().getGuiScaledWidth()/2 - this.add.getWidth()/2);
                this.addEntry(new ValueEntry<>(this.gui, this.add));
                this.setRenderSelection(false);
                this.gui.edits.clear();
                for (T t : list) {
                    this.addEntry(String.valueOf(t));
                }
                for (String t : lazyAdded) {
                    this.addEntry(t);
                }
            }

            public void addEntry(String s) {
                EditBox box = new EditBox(Minecraft.getInstance().font, 0, 0, 200, 20, Component.nullToEmpty(null));
                if (s!=null) {
                    box.setValue(s);
                }
                box.setBordered(true);
                box.setMaxLength(Integer.MAX_VALUE);
                box.setVisible(true);
                box.setX(Minecraft.getInstance().getWindow().getGuiScaledWidth()/2 - box.getWidth()/2);
                this.gui.edits.add(box);
                this.remove(this.children().size()-1);
                this.addEntry(new ValueEntry<>(this.gui, box));
                this.addEntry(new ValueEntry<>(this.gui, this.add));
            }

            @Override
            public boolean mouseClicked(double pMouseX, double pMouseY, int pButton) {
                for (int i = 0; i < this.children().size(); i++) {
                    this.getEntry(i).setUnFocused();
                }
                return super.mouseClicked(pMouseX, pMouseY, pButton);
            }

            @Override
            public boolean removeEntry(ValueEntry<T> pEntry) {
                return super.removeEntry(pEntry);
            }

            public EditBox getEditbox(int index) {
                return this.gui.edits.get(index);
            }

            public int getX() {
                return this.x0;
            }
            public int getY() {
                return this.y0;
            }

            @Override
            public void clearEntries() {
                super.clearEntries();
                this.addEntry(new ValueEntry<>(this.gui, this.add));
            }

            @Override
            protected void renderBackground(GuiGraphics pGuiGraphics) {
//                pGuiGraphics.fill(this.getLeft(), this.getTop(), this.getRight(), this.getBottom(), 0xC0101010);
            }

            @Override
            public NarrationPriority narrationPriority() {return NarrationPriority.NONE;}
            @Override
            public void updateNarration(NarrationElementOutput pNarrationElementOutput) {}

            public static class ValueEntry<T> extends Entry<ValueEntry<T>> {
                public static final ResourceLocation BEACON_LOCATION = GuiConfig.location("textures/gui/container/beacon.png");
                protected final ListScreen<T> screen;
                protected final AbstractWidget widget;
                protected Button remove;
                protected boolean canRead;

                public ValueEntry(ListScreen<T> screen, AbstractWidget widget) {
                    this.screen = screen;
                    this.widget = widget;
                    if (widget instanceof EditBox) {
                        this.remove = new GuiButton(widget.getX() - widget.getHeight() - 3, 0, widget.getHeight(), widget.getHeight(), Component.literal("X"), btn->
                            screen.remove((EditBox) widget, this)
                        );
                        this.check();
                    }
                }

                public void setUnFocused() {
                    this.widget.setFocused(false);
                    if (this.remove != null) {
                        this.remove.setFocused(false);
                    }
                }

                @Override
                public boolean mouseClicked(double pMouseX, double pMouseY, int pButton) {
                    if (this.remove != null) {
                        this.remove.setFocused(false);
                        if (this.remove.mouseClicked(pMouseX, pMouseY, pButton)) {
                            this.remove.setFocused(true);
                            return true;
                        }
                    }
                    this.widget.setFocused(false);
                    if (this.widget.mouseClicked(pMouseX, pMouseY, pButton)) {
                        this.widget.setFocused(true);
                        return true;
                    }
                    return false;
                }

                @Override
                public boolean charTyped(char pCodePoint, int pModifiers) {
                    boolean result = this.widget.charTyped(pCodePoint, pModifiers);
                    this.check();
                    return  result;
                }

                @Override
                public boolean keyPressed(int pKeyCode, int pScanCode, int pModifiers) {
                    boolean result = this.widget.keyPressed(pKeyCode, pScanCode, pModifiers);
                    this.check();
                    return  result;
                }

                @Override
                public Component getNarration() {
                    return Component.nullToEmpty(null);
                }

                public void check() {
                    if (this.widget instanceof EditBox) {
                        try {
                            T t = this.screen.get(((EditBox) this.widget).getValue());
                            this.canRead = t != null;
                        }catch (Exception e) {
                            this.canRead = false;
                        }
                    }
                }

                @Override
                public void render(GuiGraphics pGuiGraphics, int pIndex, int pTop, int pLeft, int pWidth, int pHeight, int pMouseX, int pMouseY, boolean pHovering, float pPartialTick) {
                    this.widget.setY(pTop);
                    this.widget.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
                    if (this.widget instanceof EditBox) {
                        this.remove.setY(pTop);
                        this.remove.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
                        pGuiGraphics.blit(BEACON_LOCATION, this.widget.getX() + this.widget.getWidth() + 3, this.widget.getY() + 3, this.canRead ? 91 : 113, 222, 15, 15);
                    }
                }
            }
        }

        public static class SelectValueScreen<T> extends Screen {
            public final ListScreen<T> parent;
            public final List<String> values;
            public final Consumer<String> select;

            public SelectValueScreen(ListScreen<T> parent, List<String> values, Consumer<String> select) {
                super(CommonComponents.EMPTY);
                this.parent = parent;
                this.values = values;
                this.select = select;
            }

            @Override
            protected void init() {
                this.addRenderableWidget(new SelectValuePanel<>(this.parent, this.values, this.select));
                Window window = Minecraft.getInstance().getWindow();
                this.addRenderableWidget(Button.builder(CommonComponents.GUI_BACK, btn->
                        Minecraft.getInstance().setScreen(this.parent)
                ).pos(window.getGuiScaledWidth()/2-150/2, window.getGuiScaledHeight() - 25).build());
            }

            @Override
            public void render(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
                this.renderBackground(pGuiGraphics);
                super.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
                pGuiGraphics.drawCenteredString(this.font, "Type: " + ChatFormatting.AQUA + this.parent.type.getTypeName(), this.getMinecraft().getWindow().getGuiScaledWidth() / 2, 15, Color.WHITE.getRGB());
            }
            @Override
            public void onClose() {
                this.parent.getMinecraft().setScreen(this.parent);
            }
        }

        public static class SelectValuePanel<T> extends ObjectSelectionList<SelectValuePanel.SelectEntry<T>> {
            public SelectValuePanel(ListScreen<T> parent, List<String> values, Consumer<String> select) {
                super(Minecraft.getInstance(), Minecraft.getInstance().getWindow().getGuiScaledWidth(), Minecraft.getInstance().getWindow().getGuiScaledHeight() - 60, 35, 30+Minecraft.getInstance().getWindow().getGuiScaledHeight() - 60, 23);
                for (String value : values) {
                    this.addEntry(new SelectEntry<>(value, parent, select));
                }
            }

            public static class SelectEntry<T> extends Entry<SelectEntry<T>> {
                public final String value;
                public final ListScreen<T> parent;
                public final Consumer<String> select;

                public SelectEntry(String value, ListScreen<T> parent, Consumer<String> select) {
                    this.value = value;
                    this.parent = parent;
                    this.select = select;
                }

                @Override
                public Component getNarration() {
                    return CommonComponents.EMPTY;
                }

                @Override
                public void render(GuiGraphics graphics, int pIndex, int pTop, int pLeft, int pWidth, int pHeight, int pMouseX, int pMouseY, boolean pHovering, float pPartialTick) {

                    graphics.setColor(1.0F, 1.0F, 1.0F, 1.0F);
                    RenderSystem.enableBlend();
                    RenderSystem.enableDepthTest();
                    graphics.blitNineSliced(AbstractWidget.WIDGETS_LOCATION, pLeft, pTop, pWidth, pHeight, 20, 4, 200, 20, 0, 46 + (pHovering ? 2 : 1) * 20);
                    graphics.setColor(1.0F, 1.0F, 1.0F, 1.0F);

                    String name = this.value;
                    if (NAME_GETTER.containsKey(this.parent.type)) {
                        name = I18n.get((NAME_GETTER.get(this.parent.type).apply(this.value)));
                    }

                    graphics.drawCenteredString(
                            Minecraft.getInstance().font,
                            name, pLeft + pWidth/2, pTop+pHeight/2-Minecraft.getInstance().font.lineHeight/2,Color.WHITE.getRGB()
                    );
                }

                @Override
                public boolean mouseClicked(double pMouseX, double pMouseY, int pButton) {
                    this.select.accept(this.value);
                    Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F));
                    Minecraft.getInstance().setScreen(this.parent);
                    return true;
                }
            }
        }
    }
}
