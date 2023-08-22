package cat.jiu.core.net.msg;

import cat.jiu.core.util.FollowPlayerSound;
import cat.jiu.core.util.base.BaseMessage;
import cat.jiu.core.util.element.Sound;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.NBTTagCompound;

import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class MsgMovingSound extends BaseMessage {
	protected Sound element;
	public MsgMovingSound() {}
	public MsgMovingSound(Sound element) {
		this.element = element;
	}
	
	@Override
	public IMessage handler(MessageContext ctx) {
		if(ctx.side.isClient() && this.element != null) {
			Minecraft.getMinecraft().getSoundHandler().playSound(new FollowPlayerSound(Minecraft.getMinecraft().player, this.element));
		}
		return this;
	}

	@Override
	public NBTTagCompound write(NBTTagCompound nbt) {
		nbt.setTag("element", this.element.writeTo(NBTTagCompound.class));
		return nbt;
	}

	@Override
	public void read(NBTTagCompound nbt) {
		this.element = new Sound(nbt.getCompoundTag("element"));
	}
}
