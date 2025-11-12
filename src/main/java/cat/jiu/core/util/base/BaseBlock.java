package cat.jiu.core.util.base;

import cat.jiu.core.api.IStuff;
import cat.jiu.core.util.registry.DynamicLanguageProvider;
import com.tterrag.registrate.AbstractRegistrate;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.*;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class BaseBlock extends Block implements IStuff {
    public static final Object VALUE = "";
    private static final ConcurrentHashMap<BaseBlock, Object> MAP = new ConcurrentHashMap<>();
    public static Set<BaseBlock> registerBlocks(){
        return MAP.keySet();
    }

    public BaseBlock(Properties properties, AbstractRegistrate<?> registrate) {
        super(properties);
        MAP.put(this, VALUE);
        this.registrate = registrate;
        registrate.addDataGenerator(DynamicLanguageProvider.TYPE, this::registerLanguage);
    }

    protected final AbstractRegistrate<?> registrate;
    @Override
    public AbstractRegistrate<?> getRegistrate() {
        return null;
    }

    private ArrayList<Language> languages;
    @Override
    public List<Language> getLanguages() {
        if (this.languages == null) {
            this.languages = new ArrayList<>();
        }
        return this.languages;
    }

    public <T extends IStuff> T  addLanguage(String languageCode, String name) {
        return IStuff.super.addLanguage(languageCode, this::getDescriptionId, name);
    }

    @Override
    public void appendHoverText(ItemStack stack, BlockGetter level, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        this.addTooltip(stack, level, tooltipComponents, tooltipFlag);
    }
    protected void addTooltip(ItemStack stack, BlockGetter level, List<Component> tooltipComponents, TooltipFlag tooltipFlag){

    }

    private ArrayList<Property<?>> properties;
    public <T extends IStuff> T  addProperty(Property<?> property) {
        if (this.properties == null) {
            this.properties = new ArrayList<>();
        }
        this.properties.add(property);
        return this.self();
    }
    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        if (this.properties != null && !this.properties.isEmpty()) {
            builder.add(this.properties.toArray(Property[]::new));
        }
    }

    private Consumer<RegisterCapabilitiesEvent> onCapabilityRegister;
    public <T extends IStuff> T  registerCapability(Consumer<RegisterCapabilitiesEvent> onCapabilityRegister) {
        this.onCapabilityRegister = onCapabilityRegister;
        return this.self();
    }
    @SubscribeEvent
    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        for (BaseBlock block : registerBlocks()) {
            if (block.onCapabilityRegister != null) {
                block.onCapabilityRegister.accept(event);
            }
        }
    }
}
