package cat.jiu.core.network.messages;

import cat.jiu.core.network.IJiuMessage;
import io.netty.buffer.ByteBuf;

public abstract class MessageBoolean implements IJiuMessage {
	protected boolean msg;
	
	public MessageBoolean() {}
	public MessageBoolean(boolean msg) {
		this.msg = msg;
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		this.msg = buf.readBoolean();
	}
	
	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeBoolean(this.msg);
	}
}
