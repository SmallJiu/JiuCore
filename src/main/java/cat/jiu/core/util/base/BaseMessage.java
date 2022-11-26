package cat.jiu.core.util.base;

import java.io.IOException;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufInputStream;

import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTSizeTracker;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;

import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public abstract class BaseMessage implements IMessage {
	public abstract IMessage handler(MessageContext ctx);
	protected abstract NBTTagCompound writeToNBT(NBTTagCompound nbt);
	protected abstract void readFromNBT(NBTTagCompound nbt);

	protected long getNBTMaxSize() {
		return 2097152L;
	}

	public final void fromBytes(ByteBuf buf) {
		try {
			this.readFromNBT(this.readCompundTag(buf));
		}catch(IOException e) {
			e.printStackTrace();
		}
	}

	public final void toBytes(ByteBuf buf) {
		new PacketBuffer(buf).writeCompoundTag(this.writeToNBT(new NBTTagCompound()));
	}

	private NBTTagCompound readCompundTag(ByteBuf buf) throws IOException {
		int i = buf.readerIndex();
		if(buf.readByte() == 0) {
			return null;
		}else {
			buf.readerIndex(i);
			try {
				return CompressedStreamTools.read(new ByteBufInputStream(buf), new NBTSizeTracker(this.getNBTMaxSize()));
			}catch(IOException e) {
				throw e;
			}
		}
	}
}
