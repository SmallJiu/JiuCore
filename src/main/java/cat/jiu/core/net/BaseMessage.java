package cat.jiu.core.net;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public abstract class BaseMessage {
    public abstract void toBytes(FriendlyByteBuf buf);
    public abstract void fromBytes(FriendlyByteBuf buf);
    public abstract boolean handler(Supplier<NetworkEvent.Context> context);

    public static abstract class CallbackMessage<T extends BaseMessage> extends BaseMessage {
        protected final BiConsumer<Supplier<NetworkEvent.Context>, T> send;

        protected CallbackMessage(BiConsumer<Supplier<NetworkEvent.Context>, T> send) {
            this.send = send;
        }

        @Override
        public boolean handler(Supplier<NetworkEvent.Context> context) {
            T callback = this.callback(context);
            if (callback!=null) {
                this.send.accept(context, callback);
            }
            return true;
        }

        protected abstract T callback(Supplier<NetworkEvent.Context> context);

        public static class Callback<T extends BaseMessage> extends CallbackMessage<T> {
            protected final Function<Supplier<NetworkEvent.Context>, T> packet;
            public Callback(BiConsumer<Supplier<NetworkEvent.Context>, T> send, Function<Supplier<NetworkEvent.Context>, T> packet) {
                super(send);
                this.packet = packet;
            }
            @Override
            protected T callback(Supplier<NetworkEvent.Context> context) {
                return this.packet.apply(context);
            }

            @Override
            public void toBytes(FriendlyByteBuf buf) {}
            @Override
            public void fromBytes(FriendlyByteBuf buf) {}
        }
    }
}
