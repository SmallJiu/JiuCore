package cat.jiu.core;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;

import cat.jiu.core.util.helpers.DayUtils;

public class CoreMain {
	public static void main(String[] args) {
		System.out.println("Value name must be ALL Letter");
		CoreMain m = new CoreMain();
		System.out.println(getDateWithVersion());
		System.out.println(m.DoggaBit.toString());
		System.out.println(m.DoggaByte.toString());
		BigInteger meta = BigInteger.valueOf(1);
		meta = meta.add(BigInteger.valueOf(3));
		System.out.println("Amount: " + meta.toString());
		System.out.println((long)Integer.MAX_VALUE * 4);
	}
	
	public boolean runSystemCommand(String cmd) {
		Runtime rt = Runtime.getRuntime();
		Process ps = null;
		
		try {
			ps = rt.exec(cmd);
			ps.waitFor();
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}
		
		boolean lag = ps.exitValue() == 0;
		ps.destroy();
		ps = null;
		
		return lag;
	}
	
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
	
	private final BigInteger TEN = BigInteger.valueOf(10);
	/** 10^81 */public final BigInteger PDD = this.TEN.pow(3*27);
	/** 10^78 */public final BigInteger TDD = this.TEN.pow(3*26);
	/** 10^75 */public final BigInteger GDD = this.TEN.pow(3*25);
	/** 10^72 */public final BigInteger MDD = this.TEN.pow(3*24);
	/** 10^69 */public final BigInteger KDD = this.TEN.pow(3*23);
	/** 10^66 */public final BigInteger DD = this.TEN.pow(3*22);
	/** 10^63 */public final BigInteger ND = this.TEN.pow(3*21);
	/** 10^60 */public final BigInteger BD = this.TEN.pow(3*20);
	/** 10^57 */public final BigInteger YD = this.TEN.pow(3*19);
	/** 10^54 */public final BigInteger ZD = this.TEN.pow(3*18);
	/** 10^51 */public final BigInteger ED = this.TEN.pow(3*17);
	/** 10^48 */public final BigInteger PD = this.TEN.pow(3*16);
	/** 10^45 */public final BigInteger TD = this.TEN.pow(3*15);
	/** 10^42 */public final BigInteger GD = this.TEN.pow(3*14);
	/** 10^39 */public final BigInteger MD = this.TEN.pow(3*13);
	/** 10^36 */public final BigInteger KD = this.TEN.pow(3*12);
	/** 10^33 */public final BigInteger D = this.TEN.pow(3*11);
	/** 10^30 */public final BigInteger N = this.TEN.pow(3*10);
	/** 10^27 */public final BigInteger B = this.TEN.pow(3*9);
	/** 10^24 */public final BigInteger Y = this.TEN.pow(3*8);
	/** 10^21 */public final BigInteger Z = this.TEN.pow(3*7);
	/** 10^18 */public final BigInteger E = this.TEN.pow(3*6);
	/** 10^15 */public final BigInteger P = this.TEN.pow(3*5);
	/** 10^12 */public final BigInteger T = this.TEN.pow(3*4);
	/** 10^9 */public final BigInteger G = this.TEN.pow(3*3);
	/** 10^6 */public final BigInteger M = this.TEN.pow(3*2);
	/** 10^3 */public final BigInteger K = this.TEN.pow(3*1);
	
	private final BigInteger TWO = BigInteger.valueOf(2);
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
	
	private final BigInteger BI8 = BigInteger.valueOf(8);
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
	
	public static String getDateWithVersion() {
		DayUtils day = new DayUtils();
		return day.getYear()+""+
				(day.getMonth() < 10 ? "0"+day.getMonth() : day.getMonth()+"")+
				(day.getDayOfMonth() < 10 ? "0"+day.getDayOfMonth() : day.getDayOfMonth()+"")+
				(day.getHour() < 10 ? "0"+day.getHour() : day.getHour()+"")+
				(day.getMinutes() < 10 ? "0"+day.getMinutes() : day.getMinutes()+"")+
				(day.getSecond() < 10 ? "0"+day.getSecond() : day.getSecond()+"");
	}
}
