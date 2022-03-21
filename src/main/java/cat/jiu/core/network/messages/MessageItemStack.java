package cat.jiu.core.network.messages;

import java.io.IOException;

import cat.jiu.core.network.IJiuMessage;
import io.netty.buffer.ByteBuf;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;

public abstract class MessageItemStack implements IJiuMessage {
	protected ItemStack msg;
	
	public MessageItemStack() {}
	public MessageItemStack(ItemStack msg) {
		this.msg = msg;
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		PacketBuffer pb = new PacketBuffer(buf);
		try {
			this.msg = pb.readItemStack();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void toBytes(ByteBuf buf) {
		PacketBuffer pb = new PacketBuffer(buf);
		pb.writeItemStack(this.msg);
	}
}
