package cat.jiu.core.network;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public interface IJiuMessage extends IMessage {
	default IMessage handler(MessageContext ctx) {
		this.handlerServer(ctx.getServerHandler().player.getServer());
		if(this.isClientMessage()) {
			this.handlerWorld(Minecraft.getMinecraft().world);
			this.handlerPlayer(Minecraft.getMinecraft().player);
		}else {
			this.handlerWorld(ctx.getServerHandler().player.getServerWorld());
			this.handlerPlayer(ctx.getServerHandler().player);
		}
		return this.handle(ctx);
	};
	IMessage handle(MessageContext ctx);
	
	/** @return if in server execute, return false, else return true */
	public boolean isClientMessage();
	
	default void handlerServer(MinecraftServer server) {};
	default void handlerWorld(World world) {};
	default void handlerPlayer(EntityPlayer player) {};
}
