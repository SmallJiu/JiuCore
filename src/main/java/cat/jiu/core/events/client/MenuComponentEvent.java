package cat.jiu.core.events.client;

import cat.jiu.core.api.IMenuComponent;
import net.minecraftforge.fml.common.eventhandler.Event;

public class MenuComponentEvent extends Event {
    public final IMenuComponent component;
    public MenuComponentEvent(IMenuComponent component) {
        this.component = component;
    }

    public static class ComponentClicked extends MenuComponentEvent {
        public final int mouseX, mouseY, mouseButton;

        public ComponentClicked(IMenuComponent component, int mouseX, int mouseY, int mouseButton) {
            super(component);
            this.mouseX = mouseX;
            this.mouseY = mouseY;
            this.mouseButton = mouseButton;
        }
    }
    public static class ComponentKeyTyped extends MenuComponentEvent {
        public final char typedChar;
        public final int keyCode;
        public ComponentKeyTyped(IMenuComponent component, char typedChar, int keyCode) {
            super(component);
            this.typedChar = typedChar;
            this.keyCode = keyCode;
        }
    }
    public static class ComponentInput extends MenuComponentEvent {
        protected ComponentInput(IMenuComponent component) {
            super(component);
        }
        public static class All extends ComponentInput {
            public All(IMenuComponent component) {
                super(component);
            }
        }
        public static class Mouse extends ComponentInput{
            public Mouse(IMenuComponent component) {
                super(component);
            }
        }
        public static class Keyboard extends ComponentInput {
            public Keyboard(IMenuComponent component) {
                super(component);
            }
        }
    }
}