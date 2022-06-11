package cat.jiu.core;

import java.math.BigInteger;
import java.util.Date;

import cat.jiu.core.util.helpers.BigIntegerUtils;
import scala.util.Random;

public class CoreMain {
    public static final BigInteger BIGINTEGER_MAX = BigInteger.valueOf(2L).pow(Integer.MAX_VALUE-1);
    static BigIntegerUtils util = new BigIntegerUtils();
    
	public static void main(String[] args) throws InterruptedException {
		System.out.println(getDateWithVersion());
		BigInteger num = BigInteger.ZERO;
		Random rand = new Random();
		
		while(true) {
			num = num.add(BigInteger.valueOf(rand.nextInt(1000)));
			System.out.println(format(getInt()) + " | " + getInt());
			Thread.sleep(500);
		}
		
//		Time time = new Time(10);
//		while(time.getTicks() != 0) {
//			time.update();
//			System.out.println(time.toString() + " | Ticks: " + time.getTicks());
//			Thread.sleep(50);
//		}
	}
	
	static String format(long value) {
		StringBuilder s = new StringBuilder(String.valueOf(value));
		
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
	
	static long getInt() {
		return 1001001964864885891L;
	}
	
	public static String getDateWithVersion() {
		DayUtils day = new DayUtils();
		return day.getYear()+""+
				(day.getMonth() < 10 ? "0"+day.getMonth() : day.getMonth()+"")+
				(day.getDayOfMonth() < 10 ? "0"+day.getDayOfMonth() : day.getDayOfMonth()+"")+
				(day.getHour() < 10 ? "0"+day.getHour() : day.getHour()+"")+
				(day.getMinutes() < 10 ? "0"+day.getMinutes() : day.getMinutes()+"")+
				(day.getSecond() < 10 ? "0"+day.getSecond() : day.getSecond()+"");
	}
	
	@SuppressWarnings("deprecation")
	public static class DayUtils {
		public int getYear() {
			Date date = new Date();
			return date.getYear() + 1900;
		}
		
		public int getMonth() {
			Date date = new Date();
			return date.getMonth() + 1;
		}
		
		public int getDayOfWeek() {
			Date date = new Date();
			return date.getDay();
		}
		
		public int getDayOfMonth() {
			Date date = new Date();
			return date.getDate();
		}
		
		public int getHour() {
			Date date = new Date();
			return date.getHours();
		}
		
		public int getMinutes() {
			Date date = new Date();
			return date.getMinutes();
		}
		
		public int getSecond() {
			Date date = new Date();
			return date.getSeconds();
		}
		
		public boolean isTheDay(int month, int day) {
			return this.getMonth() == month && this.getDayOfMonth() == day;
		}
		
		public String getDate() {
			return "[" + this.getYear() + "/" + this.getMonth() + "/" + this.getDayOfMonth() + "/" + this.getDayOfWeek() +  " | " + this.getHour() + ":" + this.getMinutes() + ":" + this.getSecond() + "]";
		}
	}
}
