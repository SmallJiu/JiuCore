package cat.jiu.core.util;

import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

public class NetworkHandlerBase {
	protected final SimpleNetworkWrapper channel;
	public NetworkHandlerBase(String modid) {
		this.channel = NetworkRegistry.INSTANCE.newSimpleChannel(modid);
	}
	public final <REQ extends IMessage, REPLY extends IMessage> void registerMessage(IMessageHandler<? super REQ, ? extends REPLY> handler, Class<REQ> type, int msgID, Side targetSide) {
		this.channel.registerMessage(handler, type, msgID, targetSide);
    }
}
