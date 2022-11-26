package cat.jiu.core.util;

@Deprecated
public class Time extends cat.jiu.core.util.timer.Timer {
	public Time() {
		this(0);
	}
	public Time(long sec, long tick) {
		this(0, sec, tick);
	}
	public Time(long min, long sec, long tick) {
		this(0, 0, min, sec, tick);
	}
	public Time(long hour, long min, long sec, long tick) {
		this(0, hour, min, sec, tick);
	}
	public Time(long day, long hour, long min, long sec, long tick) {
		this(parseTick(day, hour, min, sec, tick));
	}
	public Time(long ticks) {
		this.setTicks(ticks);
		this.setAllTicks(ticks);
	}
}
