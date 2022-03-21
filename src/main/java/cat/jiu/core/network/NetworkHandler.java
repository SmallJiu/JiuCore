package cat.jiu.core.network;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;

public class NetworkHandler {
	private SimpleNetworkWrapper channel;
	
	public NetworkHandler(String modid) {
		this.channel = NetworkRegistry.INSTANCE.newSimpleChannel(modid);
	}
	
	public SimpleNetworkWrapper getChannel() {
		return this.channel;
	}
	
	// 向某个维度发包（服务器到客户端）
	// server to client
	public void sendMessageToDim(IMessage msg, int dim) {
		channel.sendToDimension(msg, dim);
	}
	
	// 向某个维度的某个点发包（服务器到客户端）
	// server to client
	public void sendMessageAroundPos(IMessage msg, int dim, BlockPos pos, double range) {
		// TargetPoint的构造器为：
		// 维度id x坐标 y坐标 z坐标 覆盖范围
		// 其中，覆盖范围指接受此更新数据包的坐标的范围
		channel.sendToAllAround(msg, new NetworkRegistry.TargetPoint(dim, pos.getX(), pos.getY(), pos.getZ(), range));
	}
	
	// 向某个玩家发包（服务器到客户端）
	// server to client
	public void sendMessageToPlayer(IMessage msg, EntityPlayerMP player) {
		channel.sendTo(msg, player);
	}
	
	// 向所有人发包（服务器到客户端）
	// server to client
	public void sendMessageToAll(IMessage msg) {
		channel.sendToAll(msg);
	}
	
	// 向服务器发包（客户端到服务器）
	// client to server
	public void sendMessageToServer(IMessage msg) {
		channel.sendToServer(msg);
	}
}
