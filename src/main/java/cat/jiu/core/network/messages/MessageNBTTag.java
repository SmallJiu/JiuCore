package cat.jiu.core.network.messages;

import java.io.IOException;

import cat.jiu.core.network.IJiuMessage;
import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;

public abstract class MessageNBTTag implements IJiuMessage {
	protected NBTTagCompound msg;
	
	public MessageNBTTag() {}
	public MessageNBTTag(NBTTagCompound msg) {
		this.msg = msg;
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		PacketBuffer pb = new PacketBuffer(buf);
		try {
			this.msg = pb.readCompoundTag();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void toBytes(ByteBuf buf) {
		PacketBuffer pb = new PacketBuffer(buf);
		pb.writeCompoundTag(this.msg);
	}
}
