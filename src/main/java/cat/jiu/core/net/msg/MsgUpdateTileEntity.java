package cat.jiu.core.net.msg;

import cat.jiu.core.api.ITileEntityUpdate;
import cat.jiu.core.events.game.TileEntityUpdateEvent;
import cat.jiu.core.util.base.BaseMessage;

import net.minecraft.client.Minecraft;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class MsgUpdateTileEntity extends BaseMessage {
	protected BlockPos pos;
	protected NBTTagCompound nbt;
	public MsgUpdateTileEntity() {}
	public MsgUpdateTileEntity(TileEntity te) {
		if(te instanceof ITileEntityUpdate) {
			this.nbt = ((ITileEntityUpdate) te).getTileEntityUpdatePacket(new NBTTagCompound());
		}else {
			TileEntityUpdateEvent.Packet.Getter event = new TileEntityUpdateEvent.Packet.Getter(te);
			if (!MinecraftForge.EVENT_BUS.post(event) && event.getPacket()!=null){
				this.nbt = event.getPacket();
			}else {
				this.nbt = te.serializeNBT();
			}

		}
		this.pos = te.getPos();
	}
	public MsgUpdateTileEntity(BlockPos pos, NBTTagCompound nbt) {
		this.pos = pos;
		this.nbt = nbt;
	}

	@Override
	public NBTTagCompound write(NBTTagCompound nbt) {
		nbt.setInteger("x", this.pos.getX());
		nbt.setInteger("y", this.pos.getY());
		nbt.setInteger("z", this.pos.getZ());
		nbt.setTag("nbt", this.nbt);
		return nbt;
	}

	@Override
	public void read(NBTTagCompound nbt) {
		this.pos = new BlockPos(nbt.getInteger("x"), nbt.getInteger("y"), nbt.getInteger("z"));
		this.nbt = nbt.getCompoundTag("nbt");
	}

	@Override
	public IMessage handler(MessageContext ctx) {
		if(ctx.side.isClient()) {
			if(Minecraft.getMinecraft().world != null) {
				TileEntity te = Minecraft.getMinecraft().world.getTileEntity(this.pos);
				if(te!=null) {
					if(te instanceof ITileEntityUpdate) {
						((ITileEntityUpdate)te).onTileEntityUpdatePacket(this.nbt);
					}else {
						if(!MinecraftForge.EVENT_BUS.post(new TileEntityUpdateEvent.Packet.Handler(te, this.nbt))) {
							te.handleUpdateTag(this.nbt);
						}
					}
				}
			}
		}
		return null;
	}
}
