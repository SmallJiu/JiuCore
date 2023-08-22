package cat.jiu.core.util.helpers;

import java.math.BigDecimal;
import java.math.BigInteger;

public class BigIntegerUtils {
	public static final BigIntegerUtils INSTANCE = new BigIntegerUtils();
	
	public BigInteger create(String value) {
		if(value == null || value.isEmpty()) {
			return BigInteger.ZERO;
		}
		StringBuilder sb = new StringBuilder("0");
		for(int i = 0; i < value.length(); i++) {
			try {
				sb.append(Long.parseLong(String.valueOf(value.charAt(i))));
			}catch(Exception e) {
				sb.append(value.charAt(i));
			}
		}
		return new BigInteger(sb.toString());
	}
	
	public BigInteger copy(BigInteger other) {
		return new BigInteger(other.toString());
	}
	
	/**
	 * @return true if '{@code less}' < '{@code to}'
	 * @author small_jiu
	 */
	public boolean less(BigInteger less, BigInteger equ) {
		if(less == null || equ == null) return false;
		return less.compareTo(equ) == -1;
	}
	
	/**
	 * @return true if '{@code less}' <= '{@code equ}'
	 * @author small_jiu
	 */
	public boolean lessOrEqual(BigInteger less, BigInteger equ) {
		if(less == null || equ == null) return false;
		return less.compareTo(equ) <= 0;
	}
	
	/**
	 * @return true if '{@code greater}' > '{@code equ}'
	 * @author small_jiu
	 */
	public boolean greater(BigInteger greater, BigInteger equ) {
		if(greater == null || equ == null) return false;
		return greater.compareTo(equ) == 1;
	}
	
	/**
	 * @return true if '{@code greater}' >= '{@code equ}'
	 * @author small_jiu
	 */
	public boolean greaterOrEqual(BigInteger greater, BigInteger equ) {
		if(greater == null || equ == null) return false;
		return greater.compareTo(equ) >= 0;
	}
	
	public String format(long value) {
		return this.format(BigInteger.valueOf(value));
	}
	public String format(BigInteger value) {
		StringBuilder s = new StringBuilder(value.toString());
		
		int l = 0;
		for(int i = s.length()-1; i > 0; i--) {
			l+=1;
			if(l >= 3) {
				s.insert(i,",");
				l=0;
			}
		}
		
		return s.toString();
	}
	
	public String format(long value, int length) {
		return this.format(BigInteger.valueOf(value), length);
	}
	public String format(BigInteger value, int length) {
		String bi = BigInteger.ZERO.toString();
		if(this.greaterOrEqual(value, BigIntegerUtils.LONG_MAX)) {
			if(this.greaterOrEqual(value, BigIntegerUtils.PDD)) {
				bi = value.divide(BigIntegerUtils.PDD).toString();
				return bi + "." + subString(value.toString().substring(bi.length()), length) + "PDD";
			}
			if(this.greaterOrEqual(value, BigIntegerUtils.TDD)) {
				bi = value.divide(BigIntegerUtils.TDD).toString();
				return bi + "." + subString(value.toString().substring(bi.length()), length) + "TDD";
			}
			if(this.greaterOrEqual(value, BigIntegerUtils.GDD)) {
				bi = value.divide(BigIntegerUtils.GDD).toString();
				return bi + "." + subString(value.toString().substring(bi.length()), length) + "GDD";
			}
			if(this.greaterOrEqual(value, BigIntegerUtils.MDD)) {
				bi = value.divide(BigIntegerUtils.MDD).toString();
				return bi + "." + subString(value.toString().substring(bi.length()), length) + "MDD";
			}
			if(this.greaterOrEqual(value, BigIntegerUtils.KDD)) {
				bi = value.divide(BigIntegerUtils.KDD).toString();
				return bi + "." + subString(value.toString().substring(bi.length()), length) + "KDD";
			}
			if(this.greaterOrEqual(value, BigIntegerUtils.DD)) {
				bi = value.divide(BigIntegerUtils.DD).toString();
				return bi + "." + subString(value.toString().substring(bi.length()), length) + "DD";
			}
			if(this.greaterOrEqual(value, BigIntegerUtils.ND)) {
				bi = value.divide(BigIntegerUtils.ND).toString();
				return bi + "." + subString(value.toString().substring(bi.length()), length) + "ND";
			}
			if(this.greaterOrEqual(value, BigIntegerUtils.BD)) {
				bi = value.divide(BigIntegerUtils.BD).toString();
				return bi + "." + subString(value.toString().substring(bi.length()), length) + "BD";
			}
			if(this.greaterOrEqual(value, BigIntegerUtils.YD)) {
				bi = value.divide(BigIntegerUtils.YD).toString();
				return bi + "." + subString(value.toString().substring(bi.length()), length) + "YD";
			}
			if(this.greaterOrEqual(value, BigIntegerUtils.ZD)) {
				bi = value.divide(BigIntegerUtils.ZD).toString();
				return bi + "." + subString(value.toString().substring(bi.length()), length) + "ZD";
			}
			if(this.greaterOrEqual(value, BigIntegerUtils.ED)) {
				bi = value.divide(BigIntegerUtils.ED).toString();
				return bi + "." + subString(value.toString().substring(bi.length()), length) + "ED";
			}
			if(this.greaterOrEqual(value, BigIntegerUtils.PD)) {
				bi = value.divide(BigIntegerUtils.PD).toString();
				return bi + "." + subString(value.toString().substring(bi.length()), length) + "PD";
			}
			if(this.greaterOrEqual(value, BigIntegerUtils.TD)) {
				bi = value.divide(BigIntegerUtils.TD).toString();
				return bi + "." + subString(value.toString().substring(bi.length()), length) + "TD";
			}
			if(this.greaterOrEqual(value, BigIntegerUtils.GD)) {
				bi = value.divide(BigIntegerUtils.GD).toString();
				return bi + "." + subString(value.toString().substring(bi.length()), length) + "GD";
			}
			if(this.greaterOrEqual(value, BigIntegerUtils.MD)) {
				bi = value.divide(BigIntegerUtils.MD).toString();
				return bi + "." + subString(value.toString().substring(bi.length()), length) + "MD";
			}
			if(this.greaterOrEqual(value, BigIntegerUtils.KD)) {
				bi = value.divide(BigIntegerUtils.KD).toString();
				return bi + "." + subString(value.toString().substring(bi.length()), length) + "KD";
			}
			if(this.greaterOrEqual(value, BigIntegerUtils.D)) {
				bi = value.divide(BigIntegerUtils.D).toString();
				return bi + "." + subString(value.toString().substring(bi.length()), length) + "D";
			}
			if(this.greaterOrEqual(value, BigIntegerUtils.N)) {
				bi = value.divide(BigIntegerUtils.N).toString();
				return bi + "." + subString(value.toString().substring(bi.length()), length) + "N";
			}
			if(this.greaterOrEqual(value, BigIntegerUtils.B)) {
				bi = value.divide(BigIntegerUtils.B).toString();
				return bi + "." + subString(value.toString().substring(bi.length()), length) + "B";
			}
			if(this.greaterOrEqual(value, BigIntegerUtils.Y)) {
				bi = value.divide(BigIntegerUtils.Y).toString();
				return bi + "." + subString(value.toString().substring(bi.length()), length) + "Y";
			}
			if(this.greaterOrEqual(value, BigIntegerUtils.Z)) {
				bi = value.divide(BigIntegerUtils.Z).toString();
				return bi + "." + subString(value.toString().substring(bi.length()), length) + "Z";
			}
		}
		
		if(this.greaterOrEqual(value, BigIntegerUtils.E)) {
			bi = value.divide(BigIntegerUtils.E).toString();
			return bi + "." + subString(value.toString().substring(bi.length()), length) + "E";
		}
		if(this.greaterOrEqual(value, BigIntegerUtils.P)) {
			bi = value.divide(BigIntegerUtils.P).toString();
			return bi + "." + subString(value.toString().substring(bi.length()), length) + "P";
		}
		if(this.greaterOrEqual(value, BigIntegerUtils.T)) {
			bi = value.divide(BigIntegerUtils.T).toString();
			return bi + "." + subString(value.toString().substring(bi.length()), length) + "T";
		}
		if(this.greaterOrEqual(value, BigIntegerUtils.G)) {
			bi = value.divide(BigIntegerUtils.G).toString();
			return bi + "." + subString(value.toString().substring(bi.length()), length) + "G";
		}
		if(this.greaterOrEqual(value, BigIntegerUtils.M)) {
			bi = value.divide(BigIntegerUtils.M).toString();
			return bi + "." + subString(value.toString().substring(bi.length()), length) + "M";
		}
		if(this.greaterOrEqual(value, BigIntegerUtils.K)) {
			bi = value.divide(BigIntegerUtils.K).toString();
			return bi + "." + subString(value.toString().substring(bi.length()), length) + "K";
		}
		if(this.less(value, BigIntegerUtils.K)) {
			return value.toString();
		}
		return bi;
	}
	
	private String subString(String str, int endlength) {
		if(str.length() > endlength) {
			return str.substring(0, endlength);
		}else {
			return str;
		}
	}
	
//	/** 2^5000000000 : BigInteger max*/ public final BigInteger BIGINTEGER_MAX = this.TWO.pow(1666666667).add(this.TWO.pow(1666666666)).add(this.TWO.pow(1666666667));
	
	/** 2^63-1 : Long max*/ public static final BigInteger LONG_MAX = BigInteger.valueOf(Long.MAX_VALUE);
	/** -2^63 : Long min*/ public static final BigInteger LONG_MIN = BigInteger.valueOf(Long.MIN_VALUE);
	
	/** 2^31-1 : Integer max*/ public static final BigInteger INTEGER_MAX = BigInteger.valueOf(Integer.MAX_VALUE);
	/** -2^31 : Integer min*/ public static final BigInteger INTEGER_MIN = BigInteger.valueOf(Integer.MIN_VALUE);
	
	/** 2^15-1 : Double max*/ public static final BigInteger SHORT_MAX = BigInteger.valueOf(Short.MAX_VALUE);
	/** -2^15 : Double min*/ public static final BigInteger SHORT_MIN = BigInteger.valueOf(Short.MIN_VALUE);
	
	/** 2^7-1 : Byte max*/ public static final BigInteger BYTE_MAX = BigInteger.valueOf(Byte.MAX_VALUE);
	/** -2^7 : Byte min*/ public static final BigInteger BYTE_MIN = BigInteger.valueOf(Byte.MIN_VALUE);
	
	/** (2-2^52).2^1023 : Double max*/ public static final BigDecimal DOUBLE_MAX = BigDecimal.valueOf(Double.MAX_VALUE);
	/** 2^-1074 : Double min*/ public static final BigDecimal DOUBLE_MIN = BigDecimal.valueOf(Double.MIN_VALUE);
	
	/** (2-2^23).2^127 : Float max*/ public static final BigDecimal FLOAT_MAX = BigDecimal.valueOf(Float.MAX_VALUE);
	/** 2^-149 : Float min*/ public static final BigDecimal FLOAT_MIN = BigDecimal.valueOf(Float.MIN_VALUE);
	
	/** 10^81 */public static final BigInteger PDD = BigInteger.TEN.pow(3*27);
	/** 10^78 */public static final BigInteger TDD = BigInteger.TEN.pow(3*26);
	/** 10^75 */public static final BigInteger GDD = BigInteger.TEN.pow(3*25);
	/** 10^72 */public static final BigInteger MDD = BigInteger.TEN.pow(3*24);
	/** 10^69 */public static final BigInteger KDD = BigInteger.TEN.pow(3*23);
	/** 10^66 */public static final BigInteger DD = BigInteger.TEN.pow(3*22);
	/** 10^63 */public static final BigInteger ND = BigInteger.TEN.pow(3*21);
	/** 10^60 */public static final BigInteger BD = BigInteger.TEN.pow(3*20);
	/** 10^57 */public static final BigInteger YD = BigInteger.TEN.pow(3*19);
	/** 10^54 */public static final BigInteger ZD = BigInteger.TEN.pow(3*18);
	/** 10^51 */public static final BigInteger ED = BigInteger.TEN.pow(3*17);
	/** 10^48 */public static final BigInteger PD = BigInteger.TEN.pow(3*16);
	/** 10^45 */public static final BigInteger TD = BigInteger.TEN.pow(3*15);
	/** 10^42 */public static final BigInteger GD = BigInteger.TEN.pow(3*14);
	/** 10^39 */public static final BigInteger MD = BigInteger.TEN.pow(3*13);
	/** 10^36 */public static final BigInteger KD = BigInteger.TEN.pow(3*12);
	/** 10^33 */public static final BigInteger D = BigInteger.TEN.pow(3*11);
	/** 10^30 */public static final BigInteger N = BigInteger.TEN.pow(3*10);
	/** 10^27 */public static final BigInteger B = BigInteger.TEN.pow(3*9);
	/** 10^24 */public static final BigInteger Y = BigInteger.TEN.pow(3*8);
	/** 10^21 */public static final BigInteger Z = BigInteger.TEN.pow(3*7);
	/** 10^18 */public static final BigInteger E = BigInteger.TEN.pow(3*6);
	/** 10^15 */public static final BigInteger P = BigInteger.TEN.pow(3*5);
	/** 10^12 */public static final BigInteger T = BigInteger.TEN.pow(3*4);
	/** 10^9 */public static final BigInteger G = BigInteger.TEN.pow(3*3);
	/** 10^6 */public static final BigInteger M = BigInteger.TEN.pow(3*2);
	/** 10^3 */public static final BigInteger K = BigInteger.TEN.pow(3*1);
}
