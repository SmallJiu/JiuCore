package cat.jiu.core.util;

import java.math.BigInteger;

@Deprecated
public class BigTime extends cat.jiu.core.util.timer.BigTimer {
	public BigTime() {
		this(0);
	}
	public BigTime(long ticks) {
		this(BigInteger.valueOf(ticks));
	}
	public BigTime(long sec, long tick) {
		this(0, sec, tick);
	}
	public BigTime(long min, long sec, long tick) {
		this(0, 0, min, sec, tick);
	}
	public BigTime(long hour, long min, long sec, long tick) {
		this(0, hour, min, sec, tick);
	}
	public BigTime(long day, long hour, long min, long sec, long tick) {
		this(parseTick(day, hour, min, sec, tick));
	}
	
	public BigTime(BigInteger ticks) {
		this.format(ticks);
		this.setAllTicks(ticks);
	}
	public BigTime(BigInteger s, BigInteger tick) {
		this(BigInteger.ZERO, s, tick);
	}
	
	public BigTime(BigInteger m, BigInteger s, BigInteger tick) {
		this(BigInteger.ZERO, m, s, tick);
	}
	
	public BigTime(BigInteger h, BigInteger m, BigInteger s, BigInteger tick) {
		this(BigInteger.ZERO, h, m, s, tick);
	}
	
	public BigTime(BigInteger day, BigInteger h, BigInteger m, BigInteger s, BigInteger tick) {
		this(parseTick(day, h, m, s, tick));
	}
}
