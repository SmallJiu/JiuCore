package cat.jiu.core.test;

import cat.jiu.core.JiuCore;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

public class TestMessageHandler {
	private static SimpleNetworkWrapper channel;
	private static int ID = 0;
	private static int nextID() {return ID+=1;}
	
	public static void register() {
		channel = NetworkRegistry.INSTANCE.newSimpleChannel(JiuCore.MODID);
		channel.registerMessage(TestMessageEnergy::handler, TestMessageEnergy.class, nextID(), Side.CLIENT);
	}
	
	public static void sendMessageToDim(IMessage msg, int dim) {
		channel.sendToDimension(msg, dim);
	}
	public static void sendMessageAroundPos(IMessage msg, int dim, BlockPos pos, double range) {
		channel.sendToAllAround(msg, new NetworkRegistry.TargetPoint(dim, pos.getX(), pos.getY(), pos.getZ(), range));
	}
	public static void sendMessageToPlayer(IMessage msg, EntityPlayerMP player) {
		channel.sendTo(msg, player);
	}
	public static void sendMessageToAll(IMessage msg) {
		channel.sendToAll(msg);
	}
	public static void sendMessageToServer(IMessage msg) {
		channel.sendToServer(msg);
	}
}
