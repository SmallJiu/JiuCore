package cat.jiu.core.net;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

import java.util.HashMap;
import java.util.HashSet;
import java.util.function.Function;

public class BaseNetworkHandler {
	protected final SimpleChannel channel;
	private final HashMap<Class<? extends BaseMessage>, HashSet<NetworkDirection>> REGISTRY = new HashMap<>();
	
	private int ID;
	private int nextID() {
		return ID++;
	}
	
	protected BaseNetworkHandler(ResourceLocation id, String version) {
		this(id, version, version, version);
	}
	protected BaseNetworkHandler(ResourceLocation id, String networkVersion, String clientVersion, String serverVersion) {
		ID = 0;
		this.channel = this.channel(id, networkVersion, clientVersion, serverVersion);
	}
	protected SimpleChannel channel(ResourceLocation id, String networkVersion, String clientVersion, String serverVersion) {
		return NetworkRegistry.newSimpleChannel(
				id,
				networkVersion::toString,
				clientVersion::equals,
				serverVersion::equals
		);
	}

	/** server to client */
	public void sendMessageToPlayer(BaseMessage msg, ServerPlayer player) {
		if(msg!=null && player!=null)
			channel.send(PacketDistributor.PLAYER.with(()->player), msg);
	}

	/** client to server */
	public void sendMessageToServer(BaseMessage msg) {
		if(msg!=null) channel.sendToServer(msg);
	}

	protected <T extends BaseMessage> BaseNetworkHandler register(T msg, NetworkDirection... sendTo) {
		return this.register(msg.getClass(), sendTo);
	}

	protected <T extends BaseMessage> BaseNetworkHandler register(Class<T> msgClass, NetworkDirection... sendTo) {
		return this.register(msgClass, buf->{
			try {
				T instance = msgClass.getDeclaredConstructor().newInstance();
				instance.fromBytes(buf);
				return instance;
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		}, sendTo);
	}

	protected <T extends BaseMessage> BaseNetworkHandler register(Class<T> msgClass, Function<FriendlyByteBuf, T> decoder, NetworkDirection... sendTo) {
		for (NetworkDirection side : sendTo) {
			if (!REGISTRY.containsKey(msgClass) || !REGISTRY.get(msgClass).contains(side)) {
				this.channel.messageBuilder(msgClass, nextID(), side)
						.encoder(T::toBytes)
						.decoder(decoder)
						.consumerNetworkThread(T::handler)
						.add();
				if (!REGISTRY.containsKey(msgClass)) {
					REGISTRY.put(msgClass, new HashSet<>());
				}
				REGISTRY.get(msgClass).add(side);
			}
		}
		return this;
	}
}
