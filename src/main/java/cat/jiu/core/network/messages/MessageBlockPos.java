package cat.jiu.core.network.messages;

import cat.jiu.core.network.IJiuMessage;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;

public abstract class MessageBlockPos implements IJiuMessage {
	protected BlockPos msg;
	
	public MessageBlockPos() {}
	public MessageBlockPos(BlockPos msg) {
		this.msg = msg;
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		PacketBuffer pb = new PacketBuffer(buf);
		this.msg = pb.readBlockPos();
	}
	
	@Override
	public void toBytes(ByteBuf buf) {
		PacketBuffer pb = new PacketBuffer(buf);
		pb.writeBlockPos(this.msg);
	}
}
