package cat.jiu.core.util.base;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.*;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;

import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

@EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD)
public class BaseItem extends Item {
    public static final Object VALUE = "";
    private static final ConcurrentHashMap<BaseItem, Object> MAP = new ConcurrentHashMap<>();
    public static Set<BaseItem> registerItems(){
        return MAP.keySet();
    }

    private Consumer<RegisterCapabilitiesEvent> onCapabilityRegister;
    public BaseItem(Properties properties) {
        super(properties);
        MAP.put(this, VALUE);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        this.addTooltip(stack, context, tooltipComponents, tooltipFlag);
    }
    protected void addTooltip(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag){

    }

    public BaseItem setOnCapabilityRegister(Consumer<RegisterCapabilitiesEvent> onCapabilityRegister) {
        this.onCapabilityRegister = onCapabilityRegister;
        return this;
    }

    @SubscribeEvent
    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        for (BaseItem item : registerItems()) {
            if (item.onCapabilityRegister != null) {
                item.onCapabilityRegister.accept(event);
            }
        }
    }
}
