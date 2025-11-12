package cat.jiu.core.util.client;

import cat.jiu.core.util.Randoms;
import cat.jiu.core.util.element.sound.SoundMC;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class FollowPosSoundInstance extends AbstractTickableSoundInstance {
    private boolean followEntity;
    private Entity entity = Minecraft.getInstance().player;
    private BlockPos pos;
    public FollowPosSoundInstance(SoundMC sound) {
        super(sound.getSoundEvent(), sound.getSoundChannel(), Randoms.getRandomSource());
        this.looping = sound.isSoundLooping();
        this.volume = AudioSystem.getMinecraftVolume(sound.getSoundChannel());
        this.pitch = sound.getSoundPitch();
        if (this.entity != null) {
            this.pos = this.entity.blockPosition();
        }
    }

    public void setFollowEntity(Entity entity) {
        this.entity = entity;
        this.followEntity = true;
    }

    public void setFollowPos(BlockPos pos) {
        this.pos = pos;
        this.followEntity = false;
    }

    public void setFollowing(boolean followEntity, Entity entity, BlockPos pos) {
        this.followEntity = followEntity;
        this.entity = entity;
        this.pos = pos;
    }

    @Override
    public void tick() {
        if (this.followEntity) {
            if (this.entity != null) {
                this.x = this.entity.getX();
                this.y = this.entity.getY();
                this.z = this.entity.getZ();
            }
        }else {
            if (this.pos != null) {
                this.x = this.pos.getX();
                this.y = this.pos.getY();
                this.z = this.pos.getZ();
            }
        }
    }
}
