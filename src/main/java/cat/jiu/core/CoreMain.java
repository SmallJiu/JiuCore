package cat.jiu.core;

import cat.jiu.core.api.element.IImage;
import cat.jiu.core.api.element.ISound;
import cat.jiu.core.command.CommandJiuCore;
import cat.jiu.core.config.CoreConfig;
import cat.jiu.core.register.CoreItems;
import cat.jiu.core.register.CoreRecipes;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Mod(CoreMain.MODID)
public class CoreMain {
    public static final String
            MODID = "jiucore",
            NAME = "JiuCore",
            VERSION = "1.20.1-0.0.1";
    public static final boolean DEV = false;
    public static final Logger LOGGER = LoggerFactory.getLogger(NAME);
    public static final StackWalker STACK_WALKER = StackWalker.getInstance(StackWalker.Option.RETAIN_CLASS_REFERENCE);

    public CoreMain() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::genData);
        if (FMLLoader.getDist().isClient()) {
            FMLJavaModLoadingContext.get().getModEventBus().addListener(this::onClientSetup);
        }
        CoreItems.REGISTER.register(FMLJavaModLoadingContext.get().getModEventBus());
        CoreConfig.registerConfig();
        IImage.REGISTRY.init();
        ISound.REGISTRY.init();
        MinecraftForge.EVENT_BUS.register(this);
    }

    @OnlyIn(Dist.CLIENT)
    private void onClientSetup(FMLClientSetupEvent event) {
        CoreConfig.registerConfigScreen();
//        RenderSystem.recordRenderCall(()->{
//            JsonUtils.toJsonFile("C:/image.gif.json", new ImageGif(GifDecoder.getTexture("D:\\Users\\small_jiu\\Desktop\\Desktop\\表情包\\}LIKV%`AVKXF8ZZ}5SF1{8U.gif", -1)).write(new JsonObject()), true);
//        });
    }

    private void setup(final FMLCommonSetupEvent event){
    }
    private void genData(GatherDataEvent event) {
        event.getGenerator().addProvider(event.includeServer(), new CoreRecipes(event.getGenerator().getPackOutput()));
    }

    @SubscribeEvent
    public void onCommandRegister(RegisterCommandsEvent event) {
        new CommandJiuCore().register(event.getDispatcher());
    }
}
