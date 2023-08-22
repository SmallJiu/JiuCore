package cat.jiu.core.net;

import java.util.Map;

import com.google.common.collect.Maps;

import cat.jiu.core.util.base.BaseMessage;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.math.BlockPos;

import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

public class SimpleMsgHandler {
	private static Map<String, SimpleNetworkWrapper> channels;
	private static Map<String, Integer> ids;
	public static SimpleNetworkWrapper register(String channelID) {
		if(channels==null) channels=Maps.newHashMap();
		channels.put(channelID, NetworkRegistry.INSTANCE.newSimpleChannel(channelID));
		return channels.get(channelID);
	}
	
	public static SimpleNetworkWrapper getChannel(String channelID) {return channels.get(channelID);}
	
	public static <T extends BaseMessage> void register(String channelID, Class<T> msgClass, Side sendTo) {
		if(ids==null) ids = Maps.newHashMap();
		int id = ids.getOrDefault(channelID, -1)+1;
		ids.put(channelID, id);
		getChannel(channelID).registerMessage(T::handler, msgClass, id, sendTo);
	}
	
	/** 向某个维度发包（服务器到客户端）
	 * server to client
	 */
	public static void sendMessageToDim(String channelID, IMessage msg, int dim) {
		channels.get(channelID).sendToDimension(msg, dim);
	}
	
	/** 向某个维度的某个点发包（服务器到客户端）
	 * server to client
	 */
	public static void sendMessageAroundPos(String channelID, IMessage msg, int dim, BlockPos pos, double range) {
		channels.get(channelID).sendToAllAround(msg, new NetworkRegistry.TargetPoint(dim, pos.getX(), pos.getY(), pos.getZ(), range));
	}
	
	/** 向某个玩家发包（服务器到客户端）
	 * server to client
	 */
	public static void sendMessageToPlayer(String channelID, IMessage msg, EntityPlayerMP player) {
		channels.get(channelID).sendTo(msg, player);
	}
	
	/** 向所有人发包（服务器到客户端）
	 * server to client
	 */
	public static void sendMessageToAll(String channelID, IMessage msg) {
		channels.get(channelID).sendToAll(msg);
	}
	
	/** 向服务器发包（客户端到服务器）
	 * client to server
	 */
	public static void sendMessageToServer(String channelID, IMessage msg) {
		channels.get(channelID).sendToServer(msg);
	}
	/**
	 * server to client
	 */
	public static void sendToAllTracking(String channelID, IMessage msg, Entity entity) {
		channels.get(channelID).sendToAllTracking(msg, entity);
	}
	/**
	 * server to client
	 */
	public static void sendToAllTracking(String channelID, IMessage msg, int dim, BlockPos pos, double range) {
		channels.get(channelID).sendToAllTracking(msg, new NetworkRegistry.TargetPoint(dim, pos.getX(), pos.getY(), pos.getZ(), range));
	}
}
