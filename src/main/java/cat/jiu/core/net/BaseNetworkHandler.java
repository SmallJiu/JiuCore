package cat.jiu.core.net;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.codec.StreamDecoder;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.HandlerThread;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

import java.io.Serial;

public abstract class BaseNetworkHandler {
	protected PayloadRegistrar channel_network, channel_main;
	protected final ResourceLocation id;
	protected String version;
	
	private int ID = 0;
	private int nextID() {
		return ID++;
	}

	protected  BaseNetworkHandler(ResourceLocation id) {
		this(id, null);
	}
	protected BaseNetworkHandler(ResourceLocation id, String version) {
		this.id = id;
		this.version = version;
	}
	public void registerChannel(RegisterPayloadHandlersEvent event){
		this.channel_main = event.registrar(String.valueOf(this.id));
		if (this.version != null) {
			this.channel_main = this.channel_main.versioned(this.version);
		}
		this.channel_network = this.channel_main.executesOn(HandlerThread.NETWORK);
		this.registerMessage();
	}
	protected abstract void registerMessage();

	/** server to client */
	public void sendMessageToPlayer(BaseMessage msg, ServerPlayer player, BaseMessage... msgs) {
		this.sendMessageToPlayer(player, msg, msgs);
	}
	public void sendMessageToPlayer(ServerPlayer player, BaseMessage msg, BaseMessage... msgs) {
		if(msg!=null && player!=null)
			PacketDistributor.sendToPlayer(player, msg, msgs);
	}

	/** client to server */
	public void sendMessageToServer(BaseMessage msg, BaseMessage... msgs) {
		if(msg!=null)
			PacketDistributor.sendToServer(msg, msgs);
	}

	@SuppressWarnings("unchecked")
	public <T extends BaseMessage> BaseNetworkHandler register(Class<T> msgClass, NetworkDirection side) {
		CustomPacketPayload.Type<T> type;
		try {
            type = (CustomPacketPayload.Type<T>) msgClass.getField("TYPE").get(null);
        } catch (Exception e) {
            throw new ParseException("Not found network packet type. class: %s, side: %s", msgClass, side);
        }
		return this.register(msgClass, type, side);
    }
	public <T extends BaseMessage> BaseNetworkHandler register(Class<T> msgClass, CustomPacketPayload.Type<T> type, NetworkDirection side) {
		return this.register(msgClass, type, side, true);
	}
	public <T extends BaseMessage> BaseNetworkHandler register(Class<T> msgClass, CustomPacketPayload.Type<T> type, NetworkDirection side, boolean onMainThread) {
		return this.register(type, buf->{
			try {
				T instance = msgClass.getDeclaredConstructor().newInstance();
				instance.fromBytes(buf);
				return instance;
			} catch (Exception e) {
				try {
					return msgClass.getDeclaredConstructor(FriendlyByteBuf.class).newInstance(buf);
				} catch (Exception ignored) {}
			}
			throw new ParseException("Error parse network packet. class: %s, type: %s", msgClass, type.id());
		}, side, onMainThread);
	}

	public  <T extends BaseMessage> BaseNetworkHandler register(CustomPacketPayload.Type<T> type, StreamDecoder<FriendlyByteBuf, T> decoder, NetworkDirection side, boolean onMainThread) {
		side.register(onMainThread ? this.channel_main : this.channel_network, type, StreamCodec.of(
				((buffer, value) -> value.fromBytes(buffer)), decoder
		), BaseMessage::handler);
		return this;
	}

	public enum NetworkDirection {
		PLAY_TO_SERVER(PayloadRegistrar::playToServer),
		PLAY_TO_CLIENT(PayloadRegistrar::playToClient),
		PLAY_BIDIRECTIONAL(PayloadRegistrar::playBidirectional),

		CONFIGURATION_TO_SERVER(PayloadRegistrar::configurationToServer),
		CONFIGURATION_TO_CLIENT(PayloadRegistrar::configurationToClient),
		CONFIGURATION_BIDIRECTIONAL(PayloadRegistrar::configurationBidirectional),

		COMMON_TO_SERVER(PayloadRegistrar::commonToServer),
		COMMON_TO_CLIENT(PayloadRegistrar::commonToClient),
		COMMON_BIDIRECTIONAL(PayloadRegistrar::commonBidirectional);

		private final Send send;
		NetworkDirection(Send send) {
			this.send = send;
		}
		public <T extends CustomPacketPayload> void register(PayloadRegistrar registrar, CustomPacketPayload.Type<T> type, StreamCodec<FriendlyByteBuf, T> reader, net.neoforged.neoforge.network.handling.IPayloadHandler<T> handler) {
			this.send.register(registrar, type, reader, handler);
		}
	}

	public interface Send {
		<T extends CustomPacketPayload> void register(PayloadRegistrar registrar, CustomPacketPayload.Type<T> type, StreamCodec<FriendlyByteBuf, T> reader, net.neoforged.neoforge.network.handling.IPayloadHandler<T> handler);
	}

	public static class ParseException extends RuntimeException {
		@Serial
		private static final long serialVersionUID = 1L;
		public ParseException(String message) {
			super(message);
		}
		public ParseException(String message, Object... args) {
			super(String.format(message, args));
		}
	}
}
