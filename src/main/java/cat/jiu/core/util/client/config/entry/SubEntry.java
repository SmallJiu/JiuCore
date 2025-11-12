package cat.jiu.core.util.client.config.entry;

import cat.jiu.core.util.client.config.ConfigEntry;
import cat.jiu.core.util.client.config.GuiConfig;
import cat.jiu.core.util.client.config.GuiButton;

import com.electronwill.nightconfig.core.Config;

import com.google.common.collect.Lists;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.util.StringUtil;
import net.minecraft.world.level.storage.LevelResource;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.ModContainer;
import net.minecraftforge.fml.config.ModConfig;

import java.util.ArrayList;
import java.util.List;

/**
 * @author small_jiu
 */
public class SubEntry extends ConfigEntry<Object> {
    private final Button button;
    private final List<ConfigEntry<?>> entries;
    private final String comment;
    public SubEntry(ModContainer mod, GuiConfig parent) {
        super(null, null);
        GuiConfig gui = new GuiConfig(parent, mod);
        gui.setCanEdit(parent.isCanEdit());
        List<ConfigEntry<?>> entries = gui.getConfigEntries();
        this.entries = entries == null ? new ArrayList<>() : entries;

        this.comment = null;

        this.button = this.addWidget(new GuiButton(0, 9999, 305, 20, Component.literal(mod.getModId()), btn-> parent.getMinecraft().setScreen(gui)));
        this.button.setX(Minecraft.getInstance().getWindow().getGuiScaledWidth()/2 - this.button.getWidth()/2+2);
        this.addUndoAndReset();
    }
    public SubEntry(ModConfig config, GuiConfig parent) {
        super(null, null);
        String configDir = "config/";

        if (config.getType() == ModConfig.Type.SERVER) {
            if (Minecraft.getInstance().hasSingleplayerServer()) {
                configDir = (Minecraft.getInstance().getSingleplayerServer().getWorldPath(LevelResource.LEVEL_DATA_FILE).getParent() + "/serverconfig/").replace('\\', '/');
            }else {
                configDir = ":server/world/serverconfig/";
            }
        }

        GuiConfig gui = new GuiConfig(
                configDir + config.getFileName(),
                parent,
                (ForgeConfigSpec) config.getSpec(),
                null,
                new ArrayList<>()
        );
        gui.setCanEdit(parent.isCanEdit());
        gui.setType(config.getType());
        gui.setConfigEntries(this.entries = gui.createEntries(String.valueOf(config.getType())));

        this.comment = null;

        this.button = this.addWidget(new GuiButton(0, 9999, 305, 20, Component.literal(String.valueOf(config.getType())), btn-> parent.getMinecraft().setScreen(gui)));
        this.button.setX(Minecraft.getInstance().getWindow().getGuiScaledWidth()/2 - this.button.getWidth()/2+2);
        this.addUndoAndReset();
    }

    public SubEntry(String name, ForgeConfigSpec spec, Config config, String path, List<String> paths, GuiConfig parent) {
        super(null, null);
        GuiConfig gui = new GuiConfig(parent.configFile, parent, spec, path, paths);
        gui.setCanEdit(parent.isCanEdit());
        gui.setType(parent.getType());
        gui.setConfigEntries(this.entries = gui.createEntries(path, spec, config.valueMap()));

        String lC = spec.getLevelComment(paths);
        String comment = I18n.get(StringUtil.isNullOrEmpty(lC) ? "" : lC);
        this.comment = StringUtil.isNullOrEmpty(comment) ? null : comment;

        String key = spec.getLevelTranslationKey(paths);
        this.button = this.addWidget(new GuiButton(0, 9999, 305, 20, Component.literal(key != null ? I18n.get(key) : name), btn-> parent.getMinecraft().setScreen(gui)));
        this.button.setX(Minecraft.getInstance().getWindow().getGuiScaledWidth()/2 - this.button.getWidth()/2+2);
        this.addUndoAndReset();
    }

    public boolean isEmpty(){
        return this.entries == null || this.entries.isEmpty();
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
    protected void drawComment(Screen gui, GuiGraphics graphics, int mouseX, int mouseY) throws Exception {
        if (this.comment !=null) {
            List<Component> comments = Lists.newArrayList();
            for (String s : this.comment.split("\n")) {
                if (!(s.isEmpty() || s.length() == 1)) {
                    comments.add(Component.nullToEmpty(s));
                }
            }
            graphics.renderComponentTooltip(gui.getMinecraft().font, comments, mouseX+5, mouseY);
        }
    }

    @Override
    public void save() {
        this.entries.forEach(ConfigEntry::save);
    }

    @Override
    public void undo() {
        this.entries.forEach(ConfigEntry::undo);
    }

    @Override
    public void reset() {
        this.entries.forEach(ConfigEntry::reset);
    }

    @Override
    public int getWeight() {
        return 0;
    }

    @Override
    public String getConfigName() {
        return this.button.getMessage().getString();
    }

    @Override
    public boolean isChanged() {
        for (ConfigEntry<?> entry : this.entries) {
            if (entry.isChanged()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean isDefault() {
        for (ConfigEntry<?> entry : this.entries) {
            if (entry.isDefault()) {
                return true;
            }
        }
        return false;
    }

    @Override
    protected Object getCacheValue() {
        return null;
    }

    @Override
    protected void setCacheValue(Object newValue) {

    }

    @Override
    protected AbstractWidget getConfigWidget() {
        return this.button;
    }
}
