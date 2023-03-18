package cat.jiu.core.events.entity.player;

import javax.annotation.Nonnull;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.eventhandler.Cancelable;

public class PlayerUseItemEvent extends LivingEvent {
	public final ItemStack item;
	private int duration;

	private PlayerUseItemEvent(EntityPlayer player, @Nonnull ItemStack item, int duration) {
		super(player);
		this.item = item;
		this.setDuration(duration);
	}

	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}

	@Cancelable
	public static class Start extends PlayerUseItemEvent {
		public Start(EntityPlayer entity, @Nonnull ItemStack item, int duration) {
			super(entity, item, duration);
		}
	}
	@Cancelable
	public static class Tick extends PlayerUseItemEvent {
		public Tick(EntityPlayer entity, @Nonnull ItemStack item, int duration) {
			super(entity, item, duration);
		}
	}
	@Cancelable
	public static class Stop extends PlayerUseItemEvent {
		public Stop(EntityPlayer entity, @Nonnull ItemStack item, int duration) {
			super(entity, item, duration);
		}
	}
	public static class Finish extends PlayerUseItemEvent {
		private ItemStack result;

		public Finish(EntityPlayer entity, @Nonnull ItemStack item, int duration, @Nonnull ItemStack result) {
			super(entity, item, duration);
			this.setResultStack(result);
		}

		@Nonnull
		public ItemStack getResultStack() {
			return result;
		}

		public void setResultStack(@Nonnull ItemStack result) {
			this.result = result;
		}
	}
}
