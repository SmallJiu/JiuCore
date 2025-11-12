package cat.jiu.core.util.client;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import org.apache.commons.lang3.ArrayUtils;

@OnlyIn(Dist.CLIENT)
public class KeyUtil {
    public static KeyUtil of(String name, int keyCode, String category) {
        return new KeyUtil(name, keyCode, category);
    }
    public static KeyUtil of(String name, InputConstants.Type type, int keyCode, String category) {
        return new KeyUtil(name, type, keyCode, category);
    }
    public static KeyUtil of(KeyMapping keyMapping) {
        return new KeyUtil(keyMapping);
    }

    public final KeyMapping keyMapping;
    public KeyUtil(KeyMapping keyMapping) {
        this.keyMapping = keyMapping;
    }
    public KeyUtil(String name, int keyCode, String category) {
        this(new KeyMapping(name, keyCode, category));
    }
    public KeyUtil(String name, InputConstants.Type type, int keyCode, String category) {
        this(new KeyMapping(name, type, keyCode, category));
    }

    /**
     * <pre> maybe can register ? </pre>
     */
    public void register() {
        Minecraft.getInstance().options.keyMappings = ArrayUtils.add(Minecraft.getInstance().options.keyMappings, this.keyMapping);
    }

    /**
     * <pre>
     *  public ModMain(){
     *      FMLJavaModLoadingContext.get().getModEventBus().addListener(this::onRegisterBindings);
     *  }
     *
     *  OnlyIn(Dist.CLIENT)
     *  public void onRegisterBindings(RegisterKeyMappingsEvent event) {
     *     KEY_INSTANCE.register(event);
     *  }
     * </pre>
     */
    public void register(RegisterKeyMappingsEvent event) {
        event.register(this.keyMapping);
    }
    public Component name() {
        return this.keyMapping.getKey().getDisplayName();
    }
    public boolean isClicked() {
        return this.keyMapping.consumeClick();
    }

    /**
     * {@link net.minecraft.client.gui.screens.Screen#mouseClicked(double, double, int)}
     */
    public boolean isClicked(int button) {
        return this.keyMapping.isActiveAndMatches(InputConstants.Type.MOUSE.getOrCreate(button));
    }

    /**
     * {@link net.minecraft.client.gui.screens.Screen#keyPressed(int, int, int)}
     */
    public boolean isClicked(int keyCode, int scancode) {
        return this.keyMapping.isActiveAndMatches(InputConstants.getKey(keyCode, scancode));
    }
}
