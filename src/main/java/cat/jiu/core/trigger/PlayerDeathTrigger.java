package cat.jiu.core.trigger;

import java.math.BigInteger;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;

import cat.jiu.core.util.base.BaseAdvancement;

import net.minecraft.util.ResourceLocation;

public class PlayerDeathTrigger extends BaseAdvancement.BaseCriterionTrigger<PlayerDeathTrigger>{
	protected PlayerDeathTrigger(int dim, int deathCount) {
		super(new ResourceLocation("jc:player_death"), new PlayerDeathFactory<PlayerDeathTrigger>(dim, deathCount) {
			@Override
			public PlayerDeathTrigger deserializeInstance(JsonObject json, JsonDeserializationContext context) {
				int dim = json.has("dim") ? json.get("dim").getAsInt() : Integer.MAX_VALUE;
				int deathCount = json.has("count") ? json.get("count").getAsInt() : 1;
				return new PlayerDeathTrigger(dim, deathCount);
			}
		});
	}
	public static abstract class PlayerDeathFactory<I extends BaseAdvancement.BaseCriterionTrigger<I>>  implements ICriterionTriggerFactory<PlayerDeathTrigger>{
		protected final int dim;
		protected final BigInteger deathCount;
		public PlayerDeathFactory(int dim, int deathCount) {
			this.dim = dim;
			this.deathCount = BigInteger.valueOf(deathCount);
		}
		@Override
		public boolean check(Object... args) {
			if(args.length == 2) {
				if(args[0] instanceof Integer && args[1] instanceof BigInteger) {
					int dim = (int) args[0];
					BigInteger deathCount = (BigInteger) args[1];
					
					if(this.dim != Integer.MAX_VALUE) {
						if(!this.deathCount.max(BigInteger.valueOf(1)).equals(this.deathCount)) {
							if(this.dim == dim && deathCount.max(this.deathCount).equals(this.deathCount)) {
								return true;
							}
						}else if((int)args[0] == this.dim) {
							return true;
						}else {
							return false;
						}
					}else if(!this.deathCount.max(BigInteger.valueOf(1)).equals(this.deathCount)) {
						if(this.dim != Integer.MAX_VALUE) {
							if(this.dim == dim && deathCount.max(this.deathCount).equals(this.deathCount)) {
								return true;
							}
						}else if(this.deathCount.max(deathCount).equals(this.deathCount)) {
							return true;
						}else {
							return false;
						}
					}
				}
			}
			return true;
		}
	}
}
