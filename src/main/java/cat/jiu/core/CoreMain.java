package cat.jiu.core;

import cat.jiu.core.api.element.IImage;
import cat.jiu.core.api.element.ISound;
import cat.jiu.core.command.CommandJiuCore;
import cat.jiu.core.config.CoreConfig;
import cat.jiu.core.register.CoreItems;
import cat.jiu.core.register.data.*;
import com.tterrag.registrate.Registrate;
import com.tterrag.registrate.providers.ProviderType;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Mod(CoreMain.MODID)
public class CoreMain {
    public static final String
            MODID = "jiucore",
            NAME = "JiuCore",
            VERSION = "1.21.1-1.0.0";
    public static final boolean DEV = false;
    public static final Logger LOGGER = LoggerFactory.getLogger(NAME);
    public static final StackWalker STACK_WALKER = StackWalker.getInstance(StackWalker.Option.RETAIN_CLASS_REFERENCE);
    private static final Registrate registrate = Registrate.create(MODID).defaultCreativeTab((ResourceKey<CreativeModeTab>) null);
    public static Registrate registrate() {
        return registrate;
    }

    private static ModContainer modContainer;
    public static ModContainer container() {
        return modContainer;
    }
    private static IEventBus modBus;
    public static IEventBus bus() {
        return modBus;
    }

    public CoreMain(IEventBus modBus, ModContainer container) {
        CoreMain.modBus = modBus;
        CoreMain.modContainer = container;
        bus().addListener(this::setup);
//        if (FMLLoader.getDist().isClient()) {
            bus().addListener(this::clientSetup);
//        }
        CoreItems.bootstrap();

        registrate().addDataGenerator(ProviderType.RECIPE, CoreRecipes::register);

        CoreConfig.registerConfig(container());
        IImage.REGISTRY.init();
        ISound.REGISTRY.init();
        NeoForge.EVENT_BUS.register(this);
    }

//    @OnlyIn(Dist.CLIENT)
    private void clientSetup(FMLClientSetupEvent event) {
        CoreConfig.registerConfigScreen(container());
//        RenderSystem.recordRenderCall(()->{
//            JsonUtils.toJsonFile("C:/image.gif.json", new ImageGif(GifDecoder.getTexture("D:\\Users\\small_jiu\\Desktop\\Desktop\\表情包\\}LIKV%`AVKXF8ZZ}5SF1{8U.gif", -1)).write(new JsonObject()), true);
//        });
    }

    private void setup(FMLCommonSetupEvent event){

    }
//    private void genData(GatherDataEvent event) {
//        event.getGenerator().addProvider(event.includeServer(), new CoreRecipes(event.getGenerator().getPackOutput()));
//    }

    @SubscribeEvent
    public void onCommandRegister(RegisterCommandsEvent event) {
        new CommandJiuCore().register(event.getDispatcher());
    }
}
