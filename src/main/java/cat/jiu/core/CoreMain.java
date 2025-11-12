package cat.jiu.core;

import cat.jiu.core.api.element.IImage;
import cat.jiu.core.api.element.ISound;
import cat.jiu.core.command.CommandJiuCore;
import cat.jiu.core.config.CoreConfig;
import cat.jiu.core.register.CoreItems;
import cat.jiu.core.util.SideProxy;
import cat.jiu.core.util.base.BaseCommand;
import cat.jiu.core.util.client.config.GuiConfig;
import cat.jiu.core.util.registry.CoreRegistrate;
import cat.jiu.core.util.registry.DynamicLanguageProvider;
import com.mojang.brigadier.Command;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.client.event.ClientPlayerNetworkEvent;
import net.minecraftforge.client.event.RegisterClientCommandsEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Mod(CoreMain.MODID)
public class CoreMain {
    public static final String
            MODID = "jiucore",
            NAME = "JiuCore",
            VERSION = "1.20.1-0.0.3-a2";
    public static final boolean DEV = false;
    public static final Logger LOGGER = LoggerFactory.getLogger(NAME);
    public static final StackWalker STACK_WALKER = StackWalker.getInstance(StackWalker.Option.RETAIN_CLASS_REFERENCE);
    private static CoreRegistrate registrate;
    public static CoreRegistrate registrate() {
        return registrate;
    }

    public CoreMain() {
        registrate = CoreRegistrate.create(MODID)
                .defaultCreativeTab((ResourceKey<CreativeModeTab>) null);
        registrate().addDataGenerator(DynamicLanguageProvider.TYPE, Languages::bootstrap);
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        bus.addListener(this::setup);
        if (SideProxy.isClient()) {
            bus.addListener(this::onClientSetup);
        }
        CoreItems.boostrap();
        CoreConfig.registerConfig();
        IImage.REGISTRY.init();
        ISound.REGISTRY.init();

        MinecraftForge.EVENT_BUS.register(this);
    }

    @OnlyIn(Dist.CLIENT)
    private void onClientSetup(FMLClientSetupEvent event) {
        CoreConfig.registerConfigScreen();

//        RenderSystem.recordRenderCall(()->{
//            JsonUtils.toJsonFile("C:/image.gif.json", new ImageGif(GifDecoder.getTexture("D:\\Users\\small_jiu\\Desktop\\Desktop\\表情包\\}LIKV%`AVKXF8ZZ}5SF1{8U.gif", -1)).write(JsonData.map()), true);
//            try {
//                ImageIO.write(
//                        GifDecoder.getTexture("D:\\Users\\small_jiu\\Desktop\\Desktop\\表情包\\}LIKV%`AVKXF8ZZ}5SF1{8U.gif", -1).getFullImage(),
//                        "png", new File("C:/image.png")
//                );
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        });
//        JsonUtils.toJsonFile("C:/test.json", Test.test(), false);
    }

    private void setup(final FMLCommonSetupEvent event){
    }

    @SubscribeEvent
    public void onCommandRegister(RegisterCommandsEvent event) {
        new CommandJiuCore().register(event);
    }

    @SubscribeEvent
    public void onClientCommandRegister(RegisterClientCommandsEvent event) {
        new BaseCommand.Builder("configUI")
                .level(0)
                .run(ctx->{
                    Minecraft.getInstance().setScreen(new GuiConfig(Minecraft.getInstance(), Minecraft.getInstance().screen));
                    return Command.SINGLE_SUCCESS;
                })
                .build().registerForClient(event);
    }
}
