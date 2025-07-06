package cat.jiu.core.util.client.config;

import cat.jiu.core.util.client.config.entry.*;
import com.electronwill.nightconfig.core.Config;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.platform.Window;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.ObjectSelectionList;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.client.gui.components.Button;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.ModContainer;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.forgespi.language.IModInfo;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.lang.reflect.Field;
import java.nio.file.Paths;
import java.util.*;
import java.util.List;

/**
 * @author small_jiu
 */
@SuppressWarnings({"unused", "unchecked"})
@OnlyIn(Dist.CLIENT)
public class GuiConfig extends Screen {
    public final String configFile;
    protected final Screen parent;
    protected List<ConfigEntry<?>> entries;
    protected final ForgeConfigSpec spec;
    protected ConfigList configList;
    protected final String path;
    protected final List<String> paths = new ArrayList<>();
    protected Button done, undo, reset;
    protected String modid;
    protected ModConfig.Type type;
    protected boolean canEdit = true;

    public GuiConfig(Screen parent) {
        super(CommonComponents.EMPTY);
        this.spec = null;
        this.modid = "";
        this.configFile = "";
        this.parent = parent;
        this.path = "";
        this.setConfigEntries(new ArrayList<>());
        for (IModInfo mod : ModList.get().getMods()) {
            ModList.get().getModContainerById(mod.getModId()).ifPresent(mc -> {
                SubEntry entry = new SubEntry(mc, this);
                if (!entry.isEmpty()) this.addConfigEntry(entry);
            });
        }
    }

    /**
     * use this Constructor to register config gui factory
     */
    public GuiConfig(Screen parent, String modid) {
        super(CommonComponents.EMPTY);
        this.spec = null;
        this.modid = modid;
        this.configFile = modid;
        this.parent = parent;
        this.path = "";
        this.setConfigEntries(new ArrayList<>());
        Optional<? extends ModContainer> optional = ModList.get().getModContainerById(modid);
        if (optional.isPresent()) {
            try {
                Field field = ModContainer.class.getDeclaredField("configs");
                field.setAccessible(true);
                EnumMap<ModConfig.Type, ModConfig> configs = (EnumMap<ModConfig.Type, ModConfig>) field.get(optional.get());
                for (Map.Entry<ModConfig.Type, ModConfig> entry : configs.entrySet()) {
                    SubEntry entry1 = new SubEntry(entry.getValue(), this);
                    if (!entry1.isEmpty()) this.addConfigEntry(entry1);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * use this Constructor to register config gui factory
     */
    public GuiConfig(String file, Screen parent, ForgeConfigSpec spec) {
        this(file, parent, spec, ModConfig.Type.COMMON);
    }
    public GuiConfig(String file, Screen parent, ForgeConfigSpec spec, ModConfig.Type type) {
        this(file, parent, spec, null, new ArrayList<>());
        spec.afterReload();
        this.setType(type);
        this.setConfigEntries(this.createEntries(null, spec, spec.getValues().valueMap()));
    }

    /**
     * sub config entry use to create sub config gui, can not use to register config gui factory
      */
    public GuiConfig(String file, Screen parent, ForgeConfigSpec spec, String path, List<String> paths) {
        super(Component.nullToEmpty(file));
        this.spec = spec;
        this.configFile = file;
        this.parent = parent;
        this.path = path;
        this.paths.addAll(paths);
    }

    public static boolean isOp() {
        Minecraft mc = Minecraft.getInstance();
        return mc.player != null && ((mc.hasSingleplayerServer() && mc.player.hasPermissions(2)) || mc.player.hasPermissions(2));
    }

    public boolean isCanEdit() {
        return canEdit;
    }

    public void setCanEdit(boolean canEdit) {
        this.canEdit = canEdit;
    }

    public void setType(ModConfig.Type type) {
        this.type = type;
        if (this.getType() == ModConfig.Type.SERVER) {
            this.setCanEdit(isOp());
        }
    }

    public ModConfig.Type getType() {
        return type;
    }

    public String getPath() {
        return path;
    }

    public String getConfigFile() {
        return configFile;
    }

    public void setConfigEntries(List<ConfigEntry<?>> entries) {
        this.entries = entries;
    }
    public void addConfigEntry(ConfigEntry<?> value){
        if(this.entries==null) {
            this.entries = new ArrayList<>();
        }
        this.entries.add(value);
        this.entries.sort(Comparator.comparingInt(ConfigEntry::getWeight));
    }

    public List<ConfigEntry<?>> getConfigEntries() {
        return entries;
    }

    public ArrayList<ConfigEntry<?>> createEntries(String paths){
        return this.createEntries(paths, this.spec, this.spec.getValues().valueMap());
    }

    public ArrayList<ConfigEntry<?>> createEntries(String paths, ForgeConfigSpec spec, Map<String, Object> configs){
        ArrayList<ConfigEntry<?>> entries = new ArrayList<>();
        configs.forEach((k,v)->{
            if(v instanceof Config){
                ConfigEntry<?> entry = this.createConfigEntry(spec, (Config) v, k, paths);
                if (entry!=null) {
                    entries.add(entry);
                }else {
                    List<String> paths1 = new ArrayList<>(this.paths);
                    paths1.add(k);
                    String key = spec.getLevelTranslationKey(paths1);
                    entries.add(new SubEntry(k, spec, ((Config) v), (paths!=null? paths : "") + (paths!=null ? " > " : "") + (key != null ? I18n.get(key) : k), paths1, this));
                }
            }else if(v instanceof ForgeConfigSpec.ConfigValue<?> value){
                if(value.getDefault() instanceof Boolean){
                    entries.add(new BooleanEntry((ForgeConfigSpec.BooleanValue) v, spec.get(value.getPath())));
                }else if(value.getDefault() instanceof Enum){
                    entries.add(new EnumEntry<>((ForgeConfigSpec.EnumValue<? extends Enum<?>>) v, spec.get(value.getPath())));
                } else if (value.getDefault() instanceof List) {
                    entries.add(new ListEntry<>(this, (ForgeConfigSpec.ConfigValue<List<Object>>) v, spec.get(value.getPath())));
                } else if(value.getDefault() instanceof Number num){
                    if(num instanceof Integer){
                        entries.add(new IntEntry((ForgeConfigSpec.ConfigValue<Integer>) v, spec.get(value.getPath())));
                    }else if(num instanceof Long){
                        entries.add(new LongEntry((ForgeConfigSpec.ConfigValue<Long>) v, spec.get(value.getPath())));
                    }else if(num instanceof Float){
                        entries.add(new FloatEntry((ForgeConfigSpec.ConfigValue<Float>) v, spec.get(value.getPath())));
                    }else if(num instanceof Double){
                        entries.add(new DoubleEntry((ForgeConfigSpec.ConfigValue<Double>) v, spec.get(value.getPath())));
                    }
                }else if (value.getDefault() instanceof String) {
                    entries.add(new StringEntry((ForgeConfigSpec.ConfigValue<String>) v, spec.get(value.getPath())));
                }else {
                    ConfigEntry<?> entry = this.createConfigEntry(spec, value);
                    if (entry!=null) {
                        entries.add(entry);
                    }
                }
            }
        });
        entries.sort(Comparator.comparingInt(ConfigEntry::getWeight));
        return entries;
    }

    protected ConfigEntry<?> createConfigEntry(ForgeConfigSpec spec, Config config, String path, String paths) {
        return null;
    }
    protected ConfigEntry<?> createConfigEntry(ForgeConfigSpec spec, ForgeConfigSpec.ConfigValue<?> value) {
        return null;
    }

    @Override
    protected void init() {
        Window window = this.getMinecraft().getWindow();

        this.undo = this.addRenderableWidget(new GuiButton(window.getGuiScaledWidth()/2 - 50, window.getGuiScaledHeight() - 25, 100, 20, Component.translatable("info.config.undo"), btn->
                this.entries.forEach(ConfigEntry::undo)
        ));
        this.undo.active = false;

        this.done = this.addRenderableWidget(new GuiButton(this.undo.getX() - 102, this.undo.getY(), 100, 20, this.path==null? Component.translatable("info.config.save") : CommonComponents.GUI_DONE,btn->{
            if(this.path==null && this.canEdit){
                this.entries.forEach(ConfigEntry::save);
                this.spec.save();
                MinecraftForge.EVENT_BUS.post(new ConfigWriteEvent(this.configFile, this.spec));
            }
            this.getMinecraft().setScreen(this.parent);
        }));
        if (this.canEdit) {
            this.done.setTooltip(Tooltip.create(Component.translatable("info.config.save.0")));
        }else {
            this.done.setMessage(CommonComponents.GUI_BACK);
            this.done.setTooltip(Tooltip.create(Component.translatable("info.config.not_enough_permissions")));
        }

        this.reset = this.addRenderableWidget(new GuiButton(this.undo.getX() + 102, this.undo.getY(), 100, 20, Component.translatable("info.config.reset"), btn->
            this.entries.forEach(ConfigEntry::reset)
        ));
        this.reset.active = false;

        if(this.modid == null && this.canEdit) {
            this.addRenderableWidget(new GuiButton(this.reset.getX() + this.reset.getWidth()+2, this.reset.getY(), Minecraft.getInstance().font.width("Folder") + 5, 20, Component.literal("Folder"), btn->
                    Util.getPlatform().openFile(Paths.get(this.configFile).toFile().getParentFile())
            ));
        }
        if (this.spec != null && !this.entries.isEmpty() && this.canEdit) {
            this.addRenderableWidget(new GuiButton(this.done.getX() - 20 - 5, this.done.getY(), Minecraft.getInstance().font.width(Component.translatable("info.config.save.1")) + 5, 20, Component.translatable("info.config.save.1"), btn->{
                try {
                    this.entries.forEach(ConfigEntry::save);
                    this.spec.save();
                    MinecraftForge.EVENT_BUS.post(new ConfigWriteEvent(this.configFile, this.spec));
                    btn.setTooltip(null);
                }catch (Exception e) {
                    btn.setTooltip(Tooltip.create(Component.literal(e.getMessage())));
                }
            }));
        }
        this.configList = this.addRenderableWidget(new ConfigList(Minecraft.getInstance(), this, this.entries));
    }

    public Screen findTrueParent(Screen parent) {
        if (parent instanceof GuiConfig) {
            return this.findTrueParent(((GuiConfig) parent).parent);
        }
        return parent;
    }

    @Override
    public void render(@NotNull GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        super.renderDirtBackground(graphics);
        super.render(graphics, mouseX, mouseY, partialTicks);

        boolean changed = false;
        for (ConfigEntry<?> entry : this.entries) {
            if(entry.isChanged()){
                changed = true;
                break;
            }
        }
        boolean isDefault = false;
        for (ConfigEntry<?> entry : this.entries) {
            if(entry.isDefault()){
                isDefault = true;
                break;
            }
        }
        this.undo.active = changed && this.canEdit;
        this.reset.active = (changed || isDefault) && this.canEdit;

        graphics.drawString(this.font, this.configFile, (this.getMinecraft().getWindow().getGuiScaledWidth() / 2f) - (this.font.width(this.configFile)/2f), 5, Color.WHITE.getRGB(), true);
        if (this.path != null) {
            if(this.font.width(this.path) > this.getMinecraft().getWindow().getGuiScaledWidth()/2){
                List<String> texts = splitString(this.path, this.getMinecraft().getWindow().getGuiScaledWidth()/2);
                for (int i = 0; i < texts.size(); i++) {
                    graphics.drawString(this.font, texts.get(i), (this.getMinecraft().getWindow().getGuiScaledWidth() / 2f) - (this.font.width(texts.get(i))/2f), 5 + this.font.lineHeight+this.font.lineHeight*i, Color.WHITE.getRGB(), true);
                }
            }else {
                graphics.drawString(this.font, this.path, (this.getMinecraft().getWindow().getGuiScaledWidth() / 2f) - (this.font.width(this.path)/2f), 5 + this.font.lineHeight, Color.WHITE.getRGB(), true);
            }
        }

        this.entries.forEach(entry-> entry.drawHoverText(this, graphics, mouseX, mouseY));
    }

    @Override
    public void onClose() {
        this.getMinecraft().setScreen(this.parent);
    }

    public static class ConfigList extends ObjectSelectionList<ConfigList.ConfigListEntry> {
        public ConfigList(Minecraft client, GuiConfig parent, List<ConfigEntry<?>> entries) {
            super(client, client.getWindow().getGuiScaledWidth(), client.getWindow().getGuiScaledHeight() - 60, 35, 30+client.getWindow().getGuiScaledHeight() - 60, 23);
            this.setRenderSelection(false);
            this.setRenderHeader(false, 0);
            this.setRenderTopAndBottom(false);
            for (ConfigEntry<?> entry : entries) {
                this.addEntry(new ConfigListEntry(parent, entry));
            }
        }

        @Override
        protected int getScrollbarPosition() {
            return this.getWidth() - 15;
        }

        @Override
        public boolean mouseClicked(double pMouseX, double pMouseY, int pButton) {
            boolean result = false;
            for (ConfigListEntry entry : this.children()) {
                entry.entry.setUnFocused();
                if (entry.entry.mouseClick(pMouseX, pMouseY, pButton)) {
                    result = true;
                }
            }
            return result || super.mouseClicked(pMouseX, pMouseY, pButton);
        }

        @Override
        public boolean keyPressed(int pKeyCode, int pScanCode, int pModifiers) {
            boolean result = false;
            for (ConfigListEntry entry : this.children()) {
                if (entry.entry.keyPressed(pKeyCode, pScanCode, pModifiers)) {
                    result = true;
                }
            }
            return result || super.keyPressed(pKeyCode, pScanCode, pModifiers);
        }

        @Override
        public boolean charTyped(char pCodePoint, int pModifiers) {
            boolean result = false;
            for (ConfigListEntry entry : this.children()) {
                if (entry.entry.charTyped(pCodePoint, pModifiers)) {
                    result = true;
                }
            }
            return result || super.charTyped(pCodePoint, pModifiers);
        }

        public static class ConfigListEntry extends ObjectSelectionList.Entry<ConfigListEntry> {
            public final GuiConfig parent;
            public final ConfigEntry<?> entry;

            public ConfigListEntry(GuiConfig parent, ConfigEntry<?> entry) {
                this.parent = parent;
                this.entry = entry;
            }

            @NotNull
            @Override
            public Component getNarration() {
                return Component.nullToEmpty(null);
            }

            @Override
            public void render(@NotNull GuiGraphics pGuiGraphics, int pIndex, int pTop, int pLeft, int pWidth, int pHeight, int pMouseX, int pMouseY, boolean pHovering, float pPartialTick) {
                this.entry.render(this.parent, pGuiGraphics, pLeft-pWidth, pTop, pMouseX, pMouseY);
            }
        }
    }

    public static List<String> splitString(String text, int textMaxLength) {
        Font fr = Minecraft.getInstance().font;
        List<String> texts = Lists.newArrayList();
        if(fr.width(text) >= textMaxLength) {
            StringBuilder s = new StringBuilder();
            for(int i = 0; i < text.length(); i++) {
                String str = s.toString();
                if(fr.width(str) >= textMaxLength) {
                    texts.add(str);
                    s.setLength(0);
                }
                s.append(text.charAt(i));
            }
            if(s.length() > 0) {
                texts.add(s.toString());
            }
        }else {
            texts.add(text);
        }
        return texts;
    }
}
