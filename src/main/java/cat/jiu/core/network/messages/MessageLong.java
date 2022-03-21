package cat.jiu.core.network.messages;

import cat.jiu.core.network.IJiuMessage;
import io.netty.buffer.ByteBuf;

public abstract class MessageLong implements IJiuMessage {
	protected long msg;
	
	public MessageLong() {}
	public MessageLong(long msg) {
		this.msg = msg;
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		this.msg = buf.readLong();
	}
	
	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeLong(this.msg);
	}
}
