package cat.jiu.core.util;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import cat.jiu.core.api.ITime;

public class Time implements ITime {
	protected long day;
	protected long hour;
	protected long minute;
	protected long second;
	protected long tick;
	protected long ticks;
	protected long allTicks;
	
	public Time() {
		this(0);
	}
	public Time(long min, long sec, long tick) {
		this(0, 0, min, sec, tick);
	}
	public Time(long day, long hour, long min, long sec, long tick) {
		this(parseTick(day, hour, min, sec, tick));
	}
	public Time(long ticks) {
		this.setTicks(ticks);
	}
	
	public Time subtractDay(long day) {
		this.day -= day;
		this.replace();
		return this;
	}
	public Time subtractHour(long hour) {
		this.hour -= hour;
		this.replace();
		return this;
	}
	public Time subtractMinute(long minute) {
		this.minute -= minute;
		this.replace();
		return this;
	}
	public Time subtractSecond(long second) {
		this.second -= second;
		this.replace();
		return this;
	}
	public Time subtractTick(long tick) {
		this.tick -= tick;
		this.replace();
		return this;
	}
	
	public Time addDay(long day) {
		this.day += day;
		this.replace();
		return this;
	}
	public Time addHour(long hour) {
		this.hour += hour;
		this.replace();
		return this;
	}
	public Time addMinute(long minute) {
		this.minute += minute;
		this.replace();
		return this;
	}
	public Time addSecond(long second) {
		this.second += second;
		this.replace();
		return this;
	}
	public Time addTick(long tick) {
		this.tick += tick;
		this.replace();
		return this;
	}
	
	public Time setDay(long day) {
		this.day = day;
		this.replace();
		return this;
	}
	public Time setHour(long hour) {
		this.hour = hour;
		this.replace();
		return this;
	}
	public Time setMinute(long minute) {
		this.minute = minute;
		this.replace();
		return this;
	}
	public Time setSecond(long second) {
		this.second = second;
		this.replace();
		return this;
	}
	public Time setTick(long tick) {
		this.tick = tick;
		this.replace();
		return this;
	}
	public Time setAllTicks(long allTicks) {
		this.allTicks = allTicks;
		return this;
	}
	
	public void replace() {
		if(tick <= 0) tick = 0;
		if(second <= 0) second = 0;
		if(minute <= 0) minute = 0;
		if(hour <= 0) hour = 0;
		if(day <= 0) day = 0;
		
		this.check(parseTick(day, hour, minute, second, tick));
	}
	
	public void check(long ticks) {
		if(ticks <= 0) {
			this.day = 0;
			this.hour = 0;
			this.minute = 0;
			this.second = 0;
			this.tick = 0;
			this.ticks = 0;
			return;
		}
		
		long day = 0;
		long hour = 0;
		long min = 0;
		long sec = 0;
		long tick = 0;
		
		if(ticks >= 20) {
			tick = ticks % 20;
			sec += ticks / 20;
		}
		if(sec >= 60) {
			min = sec / 60;
			sec %= 60;
		}
		if(min >= 60) {
			hour += min / 60;
			min %= 60;
		}
		if(hour >= 24) {
			day += hour / 24;
			hour %= 24;
		}
		this.day = day;
		this.hour = hour;
		this.minute = min;
		this.second = sec;
		this.tick = tick;
		this.ticks = ticks;
	}
	
	public long getDay() {return day;}
	public long getHour() {return hour;}
	public long getMinute() {return minute;}
	public long getSecond() {return second;}
	public long getTick() {return tick;}
	public long getTicks() {return ticks;}
	public long getAllTicks() {return allTicks;}
	
	@Override
	public int hashCode() {
		return (int) ((ticks+1) ^ 31);
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj == this) return true;
		
		if(obj instanceof Time) {
			Time time = (Time) obj;
			this.replace();
			time.replace();
			return time.ticks == this.ticks;
		}
		
		return false;
	}
	
	@Override
	public Time copy() {
		return new Time(ticks);
	}
	
// static
	
	public static Time getTime(JsonElement e) {
		if(e.isJsonObject()) {
			return getTime(e.getAsJsonObject());
		}else if(e.isJsonPrimitive()) {
			return getTime(e.getAsJsonPrimitive());
		}
		return new Time();
	}
	
	public static Time getTime(JsonPrimitive json) {
		long day = 0;
		long hour = 0;
		long min = 0;
		long sec = 0;
		long tick = 0;
		if(json.isString()) {
			return getTime(json.getAsString());
		}else {
			tick = json.getAsLong();
		}
		
		return new Time(day, hour, min, sec, tick);
	}
	
	public static Time getTime(String time) {
		long day = 0;
		long hour = 0;
		long min = 0;
		long sec = 0;
		long tick = 0;
		if(time.contains(":")) {
			String[] times = JiuUtils.other.custemSplitString(":", time);
			switch(times.length) {
				case 5: day = Long.parseLong(times[4]);
				case 4: hour = Long.parseLong(times[3]);
				case 3: min = Long.parseLong(times[2]);
				case 2: sec = Long.parseLong(times[1]);
				default:
					tick = Long.parseLong(times[0]);
					break;
			}
		}else {
			tick = Long.parseLong(time);
		}
		return new Time(day, hour, min, sec, tick);
	}
	
	public static Time getTime(JsonObject obj) {
		return new Time(
						time(obj, "d", "ds", "day",   "days"),
						time(obj, "h", "hs", "hour",   "hours"),
						time(obj, "m", "ms", "minute", "minutes"),
						time(obj, "s", "ss", "sec",    "secs", "second", "seconds"),
						time(obj, "t", "ts", "tick")
					);
	}
	
	private static long time(JsonObject obj, String... keys) {
		JsonPrimitive pri = JiuUtils.json.getElement(JsonPrimitive.class, obj, keys);
		if(pri != null && pri.isNumber()) {
			return pri.getAsLong();
		}
		return 0;
	}
	
	public static long parseTick(long s, long tick) {
		return parseTick(0, s, tick);
	}
	
	public static long parseTick(long m, long s, long tick) {
		return parseTick(0, m, s, tick);
	}
	
	public static long parseTick(long h, long m, long s, long tick) {
		return parseTick(0, h, m, s, tick);
	}
	
	public static long parseTick(long day, long h, long m, long s, long tick) {
		return (((((((day*24)+h)*60)+m)*60)+s)*20)+tick;
	}
}
