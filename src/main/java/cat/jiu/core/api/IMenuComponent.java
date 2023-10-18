package cat.jiu.core.api;

import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public interface IMenuComponent {
    void draw(GuiScreen gui, int mouseX, int mouseY);
    boolean handleInput();
    boolean handMouseInput();
    boolean handleKeyboardInput();
    boolean mouseClicked(int mouseX, int mouseY, int mouseButton);
    boolean keyTyped(char typedChar, int keyCode);

    int getX();
    int getY();
    IMenuComponent setX(int x);
    IMenuComponent setY(int y);

    int getWidth();
    int getHeight();
    IMenuComponent  setWidth(int width);
    IMenuComponent  setHeight(int height);

    boolean isVisible();
    IMenuComponent setVisible(boolean visible);

    public static interface IDrawEvent {
        void draw(IMenuComponent button);
    }
}
