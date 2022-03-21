package cat.jiu.core.util.helpers;

import java.math.BigDecimal;
import java.math.BigInteger;

import cat.jiu.core.JiuCore;

public class BigIntegerUtils {
	public BigInteger create(String value) {
		if(value.length() == 0) {
			return BigInteger.ZERO;
		}
		for(int i = 0; i < value.length(); i++) {
			if(!JiuCore.CHAR_NUMBERS.contains(value.charAt(i))) {
				return BigInteger.ZERO;
			}
		}
		return new BigInteger(value);
	}
	
	public String format(long value, int length) {
		return this.format(BigInteger.valueOf(value), length);
	}
	public String format(BigInteger value, int length) {
		String bi = BigInteger.ZERO.toString();
		if(value.max(this.LONG_MAX).equals(value)) {
			if(value.max(this.PDD).equals(value)) {
				bi = value.divide(this.PDD).toString();
				return bi + "." + subString(value.remainder(this.PDD).toString(), length) + "PDD";
			}
			if(value.max(this.TDD).equals(value)) {
				bi = value.divide(this.TDD).toString();
				return bi + "." + subString(value.remainder(this.TDD).toString(), length) + "TDD";
			}
			if(value.max(this.GDD).equals(value)) {
				bi = value.divide(this.GDD).toString();
				return bi + "." + subString(value.remainder(this.GDD).toString(), length) + "GDD";
			}
			if(value.max(this.MDD).equals(value)) {
				bi = value.divide(this.MDD).toString();
				return bi + "." + subString(value.remainder(this.MDD).toString(), length) + "MDD";
			}
			if(value.max(this.KDD).equals(value)) {
				bi = value.divide(this.KDD).toString();
				return bi + "." + subString(value.remainder(this.KDD).toString(), length) + "KDD";
			}
			if(value.max(this.DD).equals(value)) {
				bi = value.divide(this.DD).toString();
				return bi + "." + subString(value.remainder(this.DD).toString(), length) + "DD";
			}
			if(value.max(this.ND).equals(value)) {
				bi = value.divide(this.ND).toString();
				return bi + "." + subString(value.remainder(this.ND).toString(), length) + "ND";
			}
			if(value.max(this.BD).equals(value)) {
				bi = value.divide(this.BD).toString();
				return bi + "." + subString(value.remainder(this.BD).toString(), length) + "BD";
			}
			if(value.max(this.YD).equals(value)) {
				bi = value.divide(this.YD).toString();
				return bi + "." + subString(value.remainder(this.YD).toString(), length) + "YD";
			}
			if(value.max(this.ZD).equals(value)) {
				bi = value.divide(this.ZD).toString();
				return bi + "." + subString(value.remainder(this.ZD).toString(), length) + "ZD";
			}
			if(value.max(this.ED).equals(value)) {
				bi = value.divide(this.ED).toString();
				return bi + "." + subString(value.remainder(this.ED).toString(), length) + "ED";
			}
			if(value.max(this.PD).equals(value)) {
				bi = value.divide(this.PD).toString();
				return bi + "." + subString(value.remainder(this.PD).toString(), length) + "PD";
			}
			if(value.max(this.TD).equals(value)) {
				bi = value.divide(this.TD).toString();
				return bi + "." + subString(value.remainder(this.TD).toString(), length) + "TD";
			}
			if(value.max(this.GD).equals(value)) {
				bi = value.divide(this.GD).toString();
				return bi + "." + subString(value.remainder(this.GD).toString(), length) + "GD";
			}
			if(value.max(this.MD).equals(value)) {
				bi = value.divide(this.MD).toString();
				return bi + "." + subString(value.remainder(this.MD).toString(), length) + "MD";
			}
			if(value.max(this.KD).equals(value)) {
				bi = value.divide(this.KD).toString();
				return bi + "." + subString(value.remainder(this.KD).toString(), length) + "KD";
			}
			if(value.max(this.D).equals(value)) {
				bi = value.divide(this.D).toString();
				return bi + "." + subString(value.remainder(this.D).toString(), length) + "D";
			}
			if(value.max(this.N).equals(value)) {
				bi = value.divide(this.N).toString();
				return bi + "." + subString(value.remainder(this.N).toString(), length) + "N";
			}
			if(value.max(this.B).equals(value)) {
				bi = value.divide(this.B).toString();
				return bi + "." + subString(value.remainder(this.B).toString(), length) + "B";
			}
			if(value.max(this.Y).equals(value)) {
				bi = value.divide(this.Y).toString();
				return bi + "." + subString(value.remainder(this.Y).toString(), length) + "Y";
			}
			if(value.max(this.Z).equals(value)) {
				bi = value.divide(this.Z).toString();
				return bi + "." + subString(value.remainder(this.Z).toString(), length) + "Z";
			}
		}
		
		if(value.max(this.E).equals(value)) {
			bi = value.divide(this.E).toString();
			return bi + "." + subString(value.remainder(this.E).toString(), length) + "E";
		}
		if(value.max(this.P).equals(value)) {
			bi = value.divide(this.P).toString();
			return bi + "." + subString(value.remainder(this.P).toString(), length) + "P";
		}
		if(value.max(this.T).equals(value)) {
			bi = value.divide(this.T).toString();
			return bi + "." + subString(value.remainder(this.T).toString(), length) + "T";
		}
		if(value.max(this.G).equals(value)) {
			bi = value.divide(this.G).toString();
			return bi + "." + subString(value.remainder(this.G).toString(), length) + "G";
		}
		if(value.max(this.M).equals(value)) {
			bi = value.divide(this.M).toString();
			return bi + "." + subString(value.remainder(this.M).toString(), length) + "M";
		}
		if(value.max(this.K).equals(value)) {
			bi = value.divide(this.K).toString();
			return bi + "." + subString(value.remainder(this.K).toString(), length) + "K";
		}
		if(value.min(this.K).equals(value)) {
			return value.toString();
		}
		return bi;
	}
	
	public String formatByte(long value, int length) {
		return this.formatByte(BigInteger.valueOf(value), length);
	}
	public String formatByte(BigInteger value, int length) {
		String bi = BigInteger.ZERO.toString();
		
		if(value.max(this.DoggaByte).equals(value)) {
			bi = value.divide(this.DoggaByte).toString();
			return bi + "." + subString(value.remainder(this.DoggaByte).toString(), length) + "DB";
		}
		if(value.max(this.NonaByte).equals(value)) {
			bi = value.divide(this.NonaByte).toString();
			return bi + "." + subString(value.remainder(this.NonaByte).toString(), length) + "NB";
		}
		if(value.max(this.BrontoByte).equals(value)) {
			bi = value.divide(this.BrontoByte).toString();
			return bi + "." + subString(value.remainder(this.BrontoByte).toString(), length) + "BB";
		}
		if(value.max(this.YottaByte).equals(value)) {
			bi = value.divide(this.YottaByte).toString();
			return bi + "." + subString(value.remainder(this.YottaByte).toString(), length) + "YB";
		}
		if(value.max(this.ZettaByte).equals(value)) {
			bi = value.divide(this.ZettaByte).toString();
			return bi + "." + subString(value.remainder(this.ZettaByte).toString(), length) + "ZB";
		}
		if(value.max(this.ExaByte).equals(value)) {
			bi = value.divide(this.ExaByte).toString();
			return bi + "." + subString(value.remainder(this.ExaByte).toString(), length) + "EB";
		}
		if(value.max(this.PetaByte).equals(value)) {
			bi = value.divide(this.PetaByte).toString();
			return bi + "." + subString(value.remainder(this.PetaByte).toString(), length) + "PB";
		}
		if(value.max(this.TrillionByte).equals(value)) {
			bi = value.divide(this.TrillionByte).toString();
			return bi + "." + subString(value.remainder(this.TrillionByte).toString(), length) + "TB";
		}
		if(value.max(this.GigaByte).equals(value)) {
			bi = value.divide(this.GigaByte).toString();
			return bi + "." + subString(value.remainder(this.GigaByte).toString(), length) + "GB";
		}
		if(value.max(this.MegaByte).equals(value)) {
			bi = value.divide(this.MegaByte).toString();
			return bi + "." + subString(value.remainder(this.MegaByte).toString(), length) + "MB";
		}
		if(value.max(this.KiloByte).equals(value)) {
			bi = value.divide(this.KiloByte).toString();
			return bi + "." + subString(value.remainder(this.KiloByte).toString(), length) + "KB";
		}
		if(value.min(this.KiloByte).equals(value)) {
			bi = value.toString();
			return bi + "B";
		}
		return bi;
	}
	
	private String subString(String str, int endlength) {
		if(str.length() <= endlength) {
			return str.substring(0, str.length());
		}
		if(str.length() > endlength) {
			return str.substring(0, endlength);
		}else {
			return str;
		}
	}
	
//	/** 2^5000000000 : BigInteger max*/ public final BigInteger BIGINTEGER_MAX = BigInteger.valueOf(2).pow(Integer.MAX_VALUE-1);
	
	/** 2^63-1 : Long max*/ public final BigInteger LONG_MAX = BigInteger.valueOf(Long.MAX_VALUE);
	/** -2^63 : Long min*/ public final BigInteger LONG_MIN = BigInteger.valueOf(Long.MIN_VALUE);
	
	/** 2^31-1 : Integer max*/ public final BigInteger INTEGER_MAX = BigInteger.valueOf(Integer.MAX_VALUE);
	/** -2^31 : Integer min*/ public final BigInteger INTEGER_MIN = BigInteger.valueOf(Integer.MIN_VALUE);
	
	/** 2^15-1 : Double max*/ public final BigInteger SHORT_MAX = BigInteger.valueOf(Short.MAX_VALUE);
	/** -2^15 : Double min*/ public final BigInteger SHORT_MIN = BigInteger.valueOf(Short.MIN_VALUE);
	
	/** 2^7-1 : Byte max*/ public final BigInteger BYTE_MAX = BigInteger.valueOf(Byte.MAX_VALUE);
	/** -2^7 : Byte min*/ public final BigInteger BYTE_MIN = BigInteger.valueOf(Byte.MIN_VALUE);
	
	/** (2-2^52).2^1023 : Double max*/ public final BigDecimal DOUBLE_MAX = BigDecimal.valueOf(Double.MAX_VALUE);
	/** 2^-1074 : Double min*/ public final BigDecimal DOUBLE_MIN = BigDecimal.valueOf(Double.MIN_VALUE);
	
	/** (2-2^23).2^127 : Float max*/ public final BigDecimal FLOAT_MAX = BigDecimal.valueOf(Float.MAX_VALUE);
	/** 2^-149 : Float min*/ public final BigDecimal FLOAT_MIN = BigDecimal.valueOf(Float.MIN_VALUE);
	
	/** 10^81 */public final BigInteger PDD = BigInteger.TEN.pow(3*27);
	/** 10^78 */public final BigInteger TDD = BigInteger.TEN.pow(3*26);
	/** 10^75 */public final BigInteger GDD = BigInteger.TEN.pow(3*25);
	/** 10^72 */public final BigInteger MDD = BigInteger.TEN.pow(3*24);
	/** 10^69 */public final BigInteger KDD = BigInteger.TEN.pow(3*23);
	/** 10^66 */public final BigInteger DD = BigInteger.TEN.pow(3*22);
	/** 10^63 */public final BigInteger ND = BigInteger.TEN.pow(3*21);
	/** 10^60 */public final BigInteger BD = BigInteger.TEN.pow(3*20);
	/** 10^57 */public final BigInteger YD = BigInteger.TEN.pow(3*19);
	/** 10^54 */public final BigInteger ZD = BigInteger.TEN.pow(3*18);
	/** 10^51 */public final BigInteger ED = BigInteger.TEN.pow(3*17);
	/** 10^48 */public final BigInteger PD = BigInteger.TEN.pow(3*16);
	/** 10^45 */public final BigInteger TD = BigInteger.TEN.pow(3*15);
	/** 10^42 */public final BigInteger GD = BigInteger.TEN.pow(3*14);
	/** 10^39 */public final BigInteger MD = BigInteger.TEN.pow(3*13);
	/** 10^36 */public final BigInteger KD = BigInteger.TEN.pow(3*12);
	/** 10^33 */public final BigInteger D = BigInteger.TEN.pow(3*11);
	/** 10^30 */public final BigInteger N = BigInteger.TEN.pow(3*10);
	/** 10^27 */public final BigInteger B = BigInteger.TEN.pow(3*9);
	/** 10^24 */public final BigInteger Y = BigInteger.TEN.pow(3*8);
	/** 10^21 */public final BigInteger Z = BigInteger.TEN.pow(3*7);
	/** 10^18 */public final BigInteger E = BigInteger.TEN.pow(3*6);
	/** 10^15 */public final BigInteger P = BigInteger.TEN.pow(3*5);
	/** 10^12 */public final BigInteger T = BigInteger.TEN.pow(3*4);
	/** 10^9 */public final BigInteger G = BigInteger.TEN.pow(3*3);
	/** 10^6 */public final BigInteger M = BigInteger.TEN.pow(3*2);
	/** 10^3 */public final BigInteger K = BigInteger.TEN.pow(3*1);
	
	public final BigInteger TWO = BigInteger.valueOf(2);
	/** 2^110 Byte : 1DB */public final BigInteger DoggaByte = this.TWO.pow(110);
	/** 2^100 Byte : 1NB */public final BigInteger NonaByte = this.TWO.pow(100);
	/** 2^90 Byte : 1BB */public final BigInteger BrontoByte = this.TWO.pow(90);
	/** 2^80 Byte : 1YB */public final BigInteger YottaByte = this.TWO.pow(80);
	/** 2^70 Byte : 1ZB */public final BigInteger ZettaByte = this.TWO.pow(70);
	/** 2^60 Byte : 1EB */public final BigInteger ExaByte = this.TWO.pow(60);
	/** 2^50 Byte : 1PB */public final BigInteger PetaByte = this.TWO.pow(50);
	/** 2^40 Byte : 1TB */public final BigInteger TrillionByte = this.TWO.pow(40);
	/** 2^30 Byte : 1GB */public final BigInteger GigaByte = this.TWO.pow(30);
	/** 2^20 Byte : 1MB */public final BigInteger MegaByte = this.TWO.pow(20);
	/** 2^10 Byte : 1KB */public final BigInteger KiloByte = this.TWO.pow(10);
	/** 1 Byte : 1B */public final BigInteger BYTE = BigInteger.valueOf(1);
	
	public final BigInteger BI8 = BigInteger.valueOf(8);
	/** (2^110)*8 Bit : 1Db */public final BigInteger DoggaBit = DoggaByte.multiply(BI8);
	/** (2^100)*8 Bit : 1Nb */public final BigInteger NonaBit = NonaByte.multiply(BI8);
	/** (2^90)*8 Bit : 1Bb */public final BigInteger BrontoBit = BrontoByte.multiply(BI8);
	/** (2^80)*8 Bit : 1Yb */public final BigInteger YottaBit = YottaByte.multiply(BI8);
	/** (2^70)*8 Bit : 1Zb */public final BigInteger ZettaBit = ZettaByte.multiply(BI8);
	/** (2^60)*8 Bit : 1Eb */public final BigInteger ExaBit = ExaByte.multiply(BI8);
	/** (2^50)*8 Bit : 1Pb */public final BigInteger PetaBit = PetaByte.multiply(BI8);
	/** (2^40)*8 Bit : 1Tb */public final BigInteger TrillionBit = TrillionByte.multiply(BI8);
	/** (2^30)*8 Bit : 1Gb */public final BigInteger GigaBit = GigaByte.multiply(BI8);
	/** (2^20)*8 Bit : 1Mb */public final BigInteger MegaBit = MegaByte.multiply(BI8);
	/** (2^10)*8 Bit : 1Kb */public final BigInteger KiloBit = KiloByte.multiply(BI8);
	/** 1 Bit : 1b */public final BigInteger BIT = BigInteger.valueOf(1);
}
