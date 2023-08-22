package cat.jiu.core.util;

import cat.jiu.core.api.ITimer;
import cat.jiu.core.util.element.Sound;

import net.minecraft.client.audio.MovingSound;
import net.minecraft.entity.player.EntityPlayer;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class FollowPlayerSound extends MovingSound {
	protected final ITimer time;
	protected final EntityPlayer player;
	public FollowPlayerSound(EntityPlayer player, Sound element) {
		super(element.getSound(), element.getSoundCategory());
		this.volume = element.getSoundVolume();
		this.pitch = element.getSoundPitch();
		this.time = element.getTime();
		this.player = player;
	}

	@Override
	public void update() {
		this.time.update();
		this.xPosF = (float)this.player.posX;
	    this.yPosF = (float)this.player.posY;
	    this.zPosF = (float)this.player.posZ;
	}
	
	@Override
	public boolean isDonePlaying() {
		return this.time.isDone();
	}
}
