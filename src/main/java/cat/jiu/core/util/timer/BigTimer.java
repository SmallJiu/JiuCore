package cat.jiu.core.util.timer;

import java.math.BigInteger;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import cat.jiu.core.api.ITime;
import cat.jiu.core.api.ITimer;
import cat.jiu.core.util.JiuUtils;
import cat.jiu.core.util.helpers.BigIntegerUtils;

import net.minecraft.nbt.NBTTagCompound;

@SuppressWarnings("deprecation")
public class BigTimer implements ITimer, ITime {
	protected static final BigIntegerUtils util = JiuUtils.big_integer;
	protected BigInteger day;
	protected BigInteger hour;
	protected BigInteger minute;
	protected BigInteger second;
	protected BigInteger tick;
	protected BigInteger ticks;
	protected BigInteger allTicks;
	
	public BigTimer() {
		this(0);
	}
	public BigTimer(long ticks) {
		this(BigInteger.valueOf(ticks));
	}
	public BigTimer(long sec, long tick) {
		this(0, sec, tick);
	}
	public BigTimer(long min, long sec, long tick) {
		this(0, 0, min, sec, tick);
	}
	public BigTimer(long hour, long min, long sec, long tick) {
		this(0, hour, min, sec, tick);
	}
	public BigTimer(long day, long hour, long min, long sec, long tick) {
		this(parseTick(day, hour, min, sec, tick));
	}
	
	public BigTimer(BigInteger ticks) {
		this.format(ticks);
		this.setAllTicks(ticks);
	}
	public BigTimer(BigInteger s, BigInteger tick) {
		this(BigInteger.ZERO, s, tick);
	}
	
	public BigTimer(BigInteger m, BigInteger s, BigInteger tick) {
		this(BigInteger.ZERO, m, s, tick);
	}
	
	public BigTimer(BigInteger h, BigInteger m, BigInteger s, BigInteger tick) {
		this(BigInteger.ZERO, h, m, s, tick);
	}
	
	public BigTimer(BigInteger day, BigInteger h, BigInteger m, BigInteger s, BigInteger tick) {
		this(parseTick(day, h, m, s, tick));
	}
	
	protected static final BigInteger twenty = BigInteger.valueOf(20);
	protected static final BigInteger sixty = BigInteger.valueOf(60);
	protected static final BigInteger twenty_four = BigInteger.valueOf(24);
	
	public void format(BigInteger ticks) {
		if(util.lessOrEqual(ticks, BigInteger.ZERO)) {
			this.day = BigInteger.ZERO;
			this.hour = BigInteger.ZERO;
			this.minute = BigInteger.ZERO;
			this.second = BigInteger.ZERO;
			this.tick = BigInteger.ZERO;
			this.ticks = BigInteger.ZERO;
			return;
		}
		BigInteger day = BigInteger.ZERO;
		BigInteger hour = BigInteger.ZERO;
		BigInteger min = BigInteger.ZERO;
		BigInteger sec = BigInteger.ZERO;
		BigInteger tick = BigInteger.ZERO;

		if(util.greaterOrEqual(ticks, twenty)) {
			tick = ticks.remainder(twenty);
			sec = ticks.divide(twenty);
		}else {
			tick = ticks;
		}
		if(util.greaterOrEqual(sec, sixty)) {
			min = sec.divide(sixty);
			sec = sec.remainder(sixty);
		}
		if(util.greaterOrEqual(min, sixty)) {
			hour = min.divide(sixty);
			min = min.remainder(sixty);
		}
		if(util.greaterOrEqual(hour, twenty_four)) {
			day = hour.divide(twenty_four);
			hour = hour.remainder(twenty_four);
		}
		
		this.day = day;
		this.hour = hour;
		this.minute = min;
		this.second = sec;
		this.tick = tick;
		this.ticks = ticks;
	}
	
	public BigTimer setDay(BigInteger day) {
		this.day = day;
		this.replace();
		return this;
	}
	public BigTimer setHour(BigInteger hour) {
		this.hour = hour;
		this.replace();
		return this;
	}
	public BigTimer setMinute(BigInteger minute) {
		this.minute = minute;
		this.replace();
		return this;
	}
	public BigTimer setSecond(BigInteger second) {
		this.second = second;
		this.replace();
		return this;
	}
	public BigTimer setTick(BigInteger tick) {
		this.tick = tick;
		this.replace();
		return this;
	}
	public BigTimer setAllTicks(BigInteger allTicks) {
		this.allTicks = allTicks;
		return this;
	}
	
	@Override
	public BigTimer add(ITimer time) {
		BigInteger ticks = BigInteger.ZERO;
		if(time instanceof BigTimer) {
			ticks = this.ticks.add(((BigTimer)time).ticks);
		}else {
			ticks = this.ticks.add(BigInteger.valueOf(time.getTicks()));
		}
		this.format(ticks);
		return this;
	}
	
	@Override
	public BigTimer subtract(ITimer time) {
		BigInteger ticks = BigInteger.ZERO;
		if(time instanceof BigTimer) {
			ticks = this.ticks.subtract(((BigTimer)time).ticks);
		}else {
			ticks = this.ticks.subtract(BigInteger.valueOf(time.getTicks()));
		}
		this.format(ticks);
		return this;
	}
	
	@Override
	public BigTimer reset() {
		this.format(this.allTicks);
		return this;
	}
	
	@Override
	public void replace() {
		if(util.lessOrEqual(this.tick, BigInteger.ZERO)) this.tick = BigInteger.ZERO;
		if(util.lessOrEqual(this.second, BigInteger.ZERO)) this.second = BigInteger.ZERO;
		if(util.lessOrEqual(this.minute, BigInteger.ZERO)) this.minute = BigInteger.ZERO;
		if(util.lessOrEqual(this.hour, BigInteger.ZERO)) this.hour = BigInteger.ZERO;
		if(util.lessOrEqual(this.day, BigInteger.ZERO)) this.day = BigInteger.ZERO;
		this.format(parseTick(day, hour, minute, second, tick));
	}

// implement
	public void update(int subtractTick) {this.format(this.ticks.subtract(BigInteger.valueOf(subtractTick)));}
	public void format(long ticks) {this.format(BigInteger.valueOf(ticks));}
	public boolean isDone() {return util.lessOrEqual(this.ticks, BigInteger.ZERO);}
	
	public long getDay() {return this.day.longValue();}
	public long getHour() {return this.hour.longValue();}
	public long getMinute() {return this.minute.longValue();}
	public long getSecond() {return this.second.longValue();}
	public long getTick() {return this.tick.longValue();}
	public long getTicks() {return this.ticks.longValue();}
	public long getAllTicks() {return this.allTicks.longValue();}
	
	public BigInteger getBigDay() {return this.day;}
	public BigInteger getBigHour() {return this.hour;}
	public BigInteger getBigMinute() {return this.minute;}
	public BigInteger getBigSecond() {return this.second;}
	public BigInteger getBigTick() {return this.tick;}
	public BigInteger getBigTicks() {return this.ticks;}
	public BigInteger getBigAllTicks() {return this.allTicks;}
	
	public BigTimer setDay(long day) {return this.setDay(BigInteger.valueOf(day));}
	public BigTimer setHour(long hour) {return this.setHour(BigInteger.valueOf(hour));}
	public BigTimer setMinute(long minute) {return this.setMinute(BigInteger.valueOf(minute));}
	public BigTimer setSecond(long second) {return this.setSecond(BigInteger.valueOf(second));}
	public BigTimer setTick(long tick) {return this.setTick(BigInteger.valueOf(tick));}
	public BigTimer setAllTicks(long allTicks) {return this.setAllTicks(BigInteger.valueOf(allTicks));}
	public BigTimer copy() {
		BigTimer time = new BigTimer(ticks);
		time.setAllTicks(allTicks);
		return time;
	}
	public long hash() {return this.allTicks.hashCode();}
	
	public boolean equals(Object obj) {return this.equalsTime(obj);}
	public String toString() {return this.toStringTime(false);}
	public BigTimer clone() {return this.copy();}
	public int hashCode() {return this.allTicks.hashCode();}
	
	public boolean isPart(int numerator, int denominator) {
		BigInteger parts = this.allTicks.divide(BigInteger.valueOf(denominator)).multiply(BigInteger.valueOf(numerator));
		return this.ticks.equals(parts);
	}
	/*
	public float getSurplusPart() {
		/* unsafe
		BigDecimal ticks = new BigDecimal(this.ticks);
		BigDecimal allTicks = new BigDecimal(this.allTicks);
		
		BigDecimal b3 = ticks.subtract(allTicks).multiply(BigDecimal.valueOf(1)).divide(allTicks).add(BigDecimal.valueOf(1));
		return b3.floatValue();
		
		return ITimer.super.getSurplusPart();
	}
	*/
	public int getPart(int denominator) {
		BigInteger part = this.allTicks.divide(BigInteger.valueOf(denominator));
		for(int i = 1; i < denominator+1; i++) {
			if(this.ticks.equals(part.multiply(BigInteger.valueOf(i)))) {
				return i;
			}
		}
		return -1;
	}
	
	@Override
	public String toStringTime(boolean reverse) {
		if(reverse) {
			return JiuUtils.other.addJoins(10, ":", this.tick, this.second, this.minute, this.hour, this.day);
		}
		return JiuUtils.other.addJoins(10, ":", this.day, this.hour, this.minute, this.second,this.tick);
	}

	@Override
	public void read(NBTTagCompound nbt) {
		this.format(JiuUtils.big_integer.create(nbt.getString("ticks")));
		this.setAllTicks(JiuUtils.big_integer.create(nbt.getString("allTicks")));
	}

	@Override
	public NBTTagCompound write(NBTTagCompound nbt) {
		if(nbt == null) nbt = new NBTTagCompound();
		nbt.setString("ticks", this.getBigTicks().toString());
		nbt.setString("allTicks", this.getBigAllTicks().toString());
		nbt.setBoolean("isBig", true);
		return nbt;
	}

	@Override
	public void read(JsonObject obj) {
		this.format(obj.get("ticks").getAsBigInteger());
		this.setAllTicks(JiuUtils.big_integer.create(obj.get("allTicks").getAsString()));
	}

	@Override
	public JsonObject write(JsonObject json) {
		if(json==null) json = new JsonObject();
		json.addProperty("ticks", this.ticks);
		json.addProperty("allTicks", this.getBigAllTicks().toString());
		json.addProperty("isBig", true);
		return json;
	}
	
// static
	
	public static BigTimer getTime(JsonElement e) {
		if(e.isJsonObject()) {
			return getTime(e.getAsJsonObject());
		}else if(e.isJsonPrimitive()) {
			return getTime(e.getAsJsonPrimitive());
		}
		return new BigTimer();
	}
	
	public static BigTimer getTime(JsonPrimitive json) {
		BigInteger day = BigInteger.ZERO;
		BigInteger hour = BigInteger.ZERO;
		BigInteger min = BigInteger.ZERO;
		BigInteger sec = BigInteger.ZERO;
		BigInteger tick = BigInteger.ZERO;
		if(json.isString()) {
			return getTime(json.getAsString());
		}else {
			tick = util.create(json.getAsString());
		}
		
		return new BigTimer(day, hour, min, sec, tick);
	}
	
	public static BigTimer getTime(String time) {
		BigInteger day = BigInteger.ZERO;
		BigInteger hour = BigInteger.ZERO;
		BigInteger min = BigInteger.ZERO;
		BigInteger sec = BigInteger.ZERO;
		BigInteger tick = BigInteger.ZERO;
		if(time.contains(":")) {
			String[] times = JiuUtils.other.custemSplitString(":", time);
			switch(times.length) {
				case 5: day = util.create(times[4]);
				case 4: hour = util.create(times[3]);
				case 3: min = util.create(times[2]);
				case 2: sec = util.create(times[1]);
				default:
					tick = util.create(times[0]);
					break;
			}
		}else {
			tick = util.create(time);
		}
		return new BigTimer(day, hour, min, sec, tick);
	}
	
	public static BigTimer getTime(JsonObject obj) {
		return new BigTimer(
						time(obj, "d", "ds", "format_day",   "days"),
						time(obj, "h", "hs", "format_hour",   "hours"),
						time(obj, "m", "ms", "format_minute", "minutes"),
						time(obj, "s", "ss", "sec",    "secs", "format_second", "seconds"),
						time(obj, "t", "ts", "tick")
					);
	}
	
	private static BigInteger time(JsonObject obj, String... keys) {
		JsonPrimitive pri = JiuUtils.json.getElement(JsonPrimitive.class, obj, keys);
		if(pri != null && pri.isString()) {
			return util.create(pri.getAsString());
		}
		return BigInteger.ZERO;
	}
	
	public static BigInteger parseTick(long s, long tick) {
		return parseTick(0, s, tick);
	}
	
	public static BigInteger parseTick(long m, long s, long tick) {
		return parseTick(0, m, s, tick);
	}
	
	public static BigInteger parseTick(long h, long m, long s, long tick) {
		return parseTick(0, h, m, s, tick);
	}
	
	public static BigInteger parseTick(long day, long h, long m, long s, long tick) {
		return parseTick(BigInteger.valueOf(day), BigInteger.valueOf(h), BigInteger.valueOf(m), BigInteger.valueOf(s), BigInteger.valueOf(tick));
	}
	
	public static BigInteger parseTick(BigInteger s, BigInteger tick) {
		return parseTick(BigInteger.ZERO, s, tick);
	}
	
	public static BigInteger parseTick(BigInteger m, BigInteger s, BigInteger tick) {
		return parseTick(BigInteger.ZERO, m, s, tick);
	}
	
	public static BigInteger parseTick(BigInteger h, BigInteger m, BigInteger s, BigInteger tick) {
		return parseTick(BigInteger.ZERO, h, m, s, tick);
	}
	
	public static BigInteger parseTick(BigInteger day, BigInteger h, BigInteger m, BigInteger s, BigInteger tick) {
		return tick.add(
				s.add(
					m.add(
						h.add(day.multiply(twenty_four))
						.multiply(sixty))
					.multiply(sixty))
				.multiply(twenty));
	}
}
