package cat.jiu.core.net.msg;

import cat.jiu.core.types.MovingSoundElement;
import cat.jiu.core.util.FollowPlayerSound;
import cat.jiu.core.util.base.BaseMessage;

import net.minecraft.client.Minecraft;
import net.minecraft.nbt.NBTTagCompound;

import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class MsgMovingSound extends BaseMessage {
	protected MovingSoundElement element;
	public MsgMovingSound() {}
	public MsgMovingSound(MovingSoundElement element) {
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
	protected NBTTagCompound writeToNBT(NBTTagCompound nbt) {
		nbt.setTag("element", this.element.writeToNBT(null));
		return nbt;
	}

	@Override
	protected void readFromNBT(NBTTagCompound nbt) {
		this.element = MovingSoundElement.readFromeNBT(nbt.getCompoundTag("element"));
	}
}
