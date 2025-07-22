package cat.jiu.core.net;

import cat.jiu.core.CoreMain;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

import java.util.function.BiConsumer;
import java.util.function.Function;

public abstract class BaseMessage implements CustomPacketPayload {
    public static <T extends CustomPacketPayload> Type<T> type(String modid, String name) {
        return new Type<>(ResourceLocation.fromNamespaceAndPath(modid, name));
    }
    public static <T extends CustomPacketPayload> Type<T> type(String modid) {
        Class<?> caller = CoreMain.STACK_WALKER.getCallerClass();
        return new Type<>(ResourceLocation.fromNamespaceAndPath(modid, caller.getName().replace('$', '.')));
    }

    protected final Type<? extends BaseMessage> type;
    protected BaseMessage(Type<? extends BaseMessage> type) {
        this.type = type;
    }
    @Override
    public @NotNull Type<? extends BaseMessage> type() {
        return this.type;
    }

    public abstract void toBytes(FriendlyByteBuf buf);
    public abstract void fromBytes(FriendlyByteBuf buf);
    public abstract boolean handler(IPayloadContext context);

    public static abstract class CallbackMessage<T extends BaseMessage> extends BaseMessage {
        protected final BiConsumer<IPayloadContext, T> send;

        protected CallbackMessage(Type<? extends Callback<T>> type, BiConsumer<IPayloadContext, T> send) {
            super(type);
            this.send = send;
        }

        @Override
        public boolean handler(IPayloadContext context) {
            T callback = this.callback(context);
            if (callback!=null) {
                this.send.accept(context, callback);
            }
            return true;
        }

        protected abstract T callback(IPayloadContext context);

        public static class Callback<T extends BaseMessage> extends CallbackMessage<T> {
            protected final Function<IPayloadContext, T> packet;
            public Callback(Type<? extends Callback<T>> type, BiConsumer<IPayloadContext, T> send, Function<IPayloadContext, T> packet) {
                super(type, send);
                this.packet = packet;
            }
            @Override
            protected T callback(IPayloadContext context) {
                return this.packet.apply(context);
            }

            @Override
            public void toBytes(FriendlyByteBuf buf) {

            }

            @Override
            public void fromBytes(FriendlyByteBuf buf) {

            }
        }
    }
}
