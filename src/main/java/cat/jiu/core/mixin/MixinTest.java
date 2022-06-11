package cat.jiu.core.mixin;

//@Mixin(value = Minecraft.class )
public class MixinTest {
	
	private MixinTest() {
		throw new RuntimeException();
	}
	
//	@Inject(at = @At(value = "HEAD"), method = "shutdownMinecraftApplet()V")
	public void shutdownMinecraftApplet() {
		
	}
}
