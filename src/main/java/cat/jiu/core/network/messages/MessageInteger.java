package cat.jiu.core.network.messages;

import cat.jiu.core.network.IJiuMessage;
import io.netty.buffer.ByteBuf;

public abstract class MessageInteger implements IJiuMessage {
	protected int msg;
	
	public MessageInteger() {}
	public MessageInteger(int msg) {
		this.msg = msg;
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		this.msg = buf.readInt();
	}
	
	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(this.msg);
	}
}
