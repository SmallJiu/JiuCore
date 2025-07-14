package cat.jiu.core.net;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.codec.StreamDecoder;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.handling.MainThreadPayloadHandler;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import org.apache.http.ParseException;

public class BaseNetworkHandler {
	protected PayloadRegistrar channel;
	protected final ResourceLocation id;
	protected String version;
	
	private int ID;
	private int nextID() {
		return ID++;
	}

	protected BaseNetworkHandler(ResourceLocation id) {
		this(id, null);
	}
	protected BaseNetworkHandler(ResourceLocation id, String version) {
		ID = 0;
		this.id = id;
	}
	public void register(RegisterPayloadHandlersEvent event){
		this.channel = event.registrar(String.valueOf(this.id));
		if (this.version != null) {
			this.channel = this.channel.versioned(this.version);
		}
	}

	/** server to client */
	public void sendMessageToPlayer(ServerPlayer player, BaseMessage msg, BaseMessage... msgs) {
		if(msg!=null && player!=null)
			PacketDistributor.sendToPlayer(player, msg, msgs);
	}

	/** client to server */
	public void sendMessageToServer(BaseMessage msg, BaseMessage... msgs) {
		if(msg!=null)
			PacketDistributor.sendToServer(msg, msgs);
	}

	protected <T extends BaseMessage> BaseNetworkHandler register(Class<T> msgClass, CustomPacketPayload.Type<T> type) {
		return this.register(type, buf->{
			try {
				T instance = msgClass.getDeclaredConstructor().newInstance();
				instance.fromBytes(buf);
				return instance;
			} catch (Exception e) {
				try {
					return msgClass.getDeclaredConstructor(RegistryFriendlyByteBuf.class).newInstance(buf);
				} catch (Exception ignored) {}
			}
			throw new ParseException(String.format("Error parse network packet. class: %s, type: %s", msgClass, type.id()));
		});
	}

	protected <T extends BaseMessage> BaseNetworkHandler register(CustomPacketPayload.Type<T> type, StreamDecoder<RegistryFriendlyByteBuf, T> decoder) {
		this.channel.playBidirectional(type, StreamCodec.of(
				((buffer, value) -> value.fromBytes(buffer)), decoder
		), new MainThreadPayloadHandler<>(T::handler));
		return this;
	}

	public static enum NetworkDirection {
		CLIENT_TO_SERVER, SERVER_TO_CLIENT
	}
}
