package cat.jiu.core.network.messages;

import cat.jiu.core.network.IJiuMessage;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.PacketBuffer;

public abstract class MessageString implements IJiuMessage {
	protected String msg;
	
	public MessageString() {}
	public MessageString(String msg) {
		this.msg = msg;
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		PacketBuffer pb = new PacketBuffer(buf);
		this.msg = pb.readString(536870911);
	}
	
	@Override
	public void toBytes(ByteBuf buf) {
		PacketBuffer pb = new PacketBuffer(buf);
		pb.writeString(this.msg);
	}
}
