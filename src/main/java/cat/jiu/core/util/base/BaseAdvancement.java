package cat.jiu.core.util.base;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;

import cat.jiu.core.util.base.exmp.TestTrigger;

import net.minecraft.advancements.ICriterionTrigger;
import net.minecraft.advancements.PlayerAdvancements;
import net.minecraft.advancements.critereon.AbstractCriterionInstance;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ResourceLocation;

public class BaseAdvancement {
	/**
	 * example see {@link TestTrigger}
	 * 
	 * @author small_jiu
	 */
	public static abstract class BaseCriterionTrigger<T extends BaseCriterionTrigger<T>> implements ICriterionTrigger<BaseCriterionTrigger.Instance<T>> {
		protected final ResourceLocation ID;
		protected final Map<PlayerAdvancements, BaseCriterionTrigger.Listeners<T>> listeners = Maps.newHashMap();

		protected BaseCriterionTrigger(ResourceLocation id) {
			this.ID = id;
		}
		
		@Override
		public ResourceLocation getId() {
			return this.ID;
		}
		
		@Override
		public void addListener(PlayerAdvancements playerAdvancementsIn, Listener<Instance<T>> listener) {
			BaseCriterionTrigger.Listeners<T> listeners = this.listeners.get(playerAdvancementsIn);
			if(listeners == null) {
				listeners = new BaseCriterionTrigger.Listeners<T>(playerAdvancementsIn);
				this.listeners.put(playerAdvancementsIn, listeners);
			}
			listeners.add(listener);
		}

		@Override
		public void removeListener(PlayerAdvancements playerAdvancementsIn, Listener<Instance<T>> listener) {
			BaseCriterionTrigger.Listeners<T> listeners = this.listeners.get(playerAdvancementsIn);
			if(listeners != null) {
				listeners.remove(listener);
				if(listeners.isEmpty()) {
					this.listeners.remove(playerAdvancementsIn);
				}
			}
		}

		@Override
		public void removeAllListeners(PlayerAdvancements playerAdvancementsIn) {
			this.listeners.remove(playerAdvancementsIn);
		}

		/**
		 * use this to check and give player Advancement
		 * 
		 * @param player
		 *            the player, only active on {@code player} instanceof EntityPlayerMP
		 * @param args
		 *            the Factory args
		 * @author small_jiu
		 */
		public void trigger(EntityPlayer player, Object... args) {
			if(player instanceof EntityPlayerMP) {
				BaseCriterionTrigger.Listeners<T> listeners = this.listeners.get(((EntityPlayerMP) player).getAdvancements());
				if(listeners != null) {
					listeners.trigger(args);
				}
			}
		}
		public void trigger(ICommandSender sender, Object... args) {
			if(sender instanceof EntityPlayer) this.trigger((EntityPlayer)sender, args);
		}

		/**
		 * deserialize form json
		 * 
		 * @param json
		 *            Advancement 'conditions' JsonObject
		 * @return Trigger instance form Advancement
		 * @author small_jiu
		 */
		@Override
		public Instance<T> deserializeInstance(JsonObject json, JsonDeserializationContext context) {
			return new Instance<T>(this.getInstance(json, context));
		};
		
		/**
		 * Use this to check player can be get Advancement<p>
		 * For compatibility, check {@code args.length} can be '>=', 
		 * 
		 * @param args
		 *            the value of {@link BaseCriterionTrigger#trigger(EntityPlayerMP, Object...)}
		 * @return if player can get Advancement, return true
		 * @author small_jiu
		 */
		protected abstract boolean check(Object... args);
		
		/**
		 * deserialize form json
		 * 
		 * @param json
		 *            Advancement 'conditions' JsonObject
		 * @return Trigger instance form Advancement
		 * @author small_jiu
		 */
		protected abstract T getInstance(JsonObject json, JsonDeserializationContext context);
		
		protected static class Instance<T extends BaseCriterionTrigger<T>> extends AbstractCriterionInstance {
			private final BaseCriterionTrigger<T> instance;
			public Instance(BaseCriterionTrigger<T> instance) {
				super(instance.getId());
				this.instance = instance;
			}
			private final boolean check(Object... args) {
				return this.instance.check(args);
			}
		}

		protected static class Listeners<T extends BaseCriterionTrigger<T>> {
			private final PlayerAdvancements playerAdvancements;
			private final Set<ICriterionTrigger.Listener<Instance<T>>> listeners = Sets.newHashSet();

			public Listeners(PlayerAdvancements playerAdvancementsIn) {
				this.playerAdvancements = playerAdvancementsIn;
			}

			public boolean isEmpty() {
				return this.listeners.isEmpty();
			}

			public void add(ICriterionTrigger.Listener<Instance<T>> listener) {
				this.listeners.add(listener);
			}

			public void remove(ICriterionTrigger.Listener<Instance<T>> listener) {
				this.listeners.remove(listener);
			}

			public void trigger(Object... args) {
				List<ICriterionTrigger.Listener<Instance<T>>> list = null;
				for(ICriterionTrigger.Listener<Instance<T>> listener : this.listeners) {
					if(((Instance<T>) listener.getCriterionInstance()).check(args)) {
						if(list == null) {
							list = Lists.newArrayList();
						}
						list.add(listener);
					}
				}
				if(list != null) {
					for(ICriterionTrigger.Listener<Instance<T>> listener1 : list) {
						listener1.grantCriterion(this.playerAdvancements);
					}
				}
			}
		}
	}
}
