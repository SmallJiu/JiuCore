package cat.jiu.core;

import java.math.BigInteger;

import org.apache.logging.log4j.LogManager;

import cat.jiu.core.util.helpers.BigIntegerUtils;
import cat.jiu.core.util.helpers.DayUtils;
import cat.jiu.core.util.timer.MillisTimer;

public class CoreMain {
    public static final BigInteger BIGINTEGER_MAX = BigInteger.valueOf(2L).pow(Integer.MAX_VALUE-1);
	static final DayUtils day = new DayUtils();
    static BigIntegerUtils util = new BigIntegerUtils();
    static Logger log = new Logger("JiuJiu");
    static org.apache.logging.log4j.Logger logger = LogManager.getLogger("JiuJiu");
    
	public static void main(String[] args) throws InterruptedException {
		System.out.println(System.currentTimeMillis());
		System.out.println(getDateWithVersion());
		
		long bytes = 2097152;
		long bs = bytes % 1024;
		long kbs = bytes / 1024;
		long mbs = kbs / 1024;
		System.out.println("Byte: " + bs + ", KBs: " + (kbs % 1024) + ", MBs: " + mbs);
		
		MillisTimer time = new MillisTimer(9,0,9,10,10).start();
		System.out.println(time.getAllTicks() + " | " + time.isDone());
		while(!time.isDone()) {
			System.out.println(day.getSecond() + " | " + time.toString());
			Thread.sleep(50);
		}
		System.out.println(getDateWithVersion());
		System.out.println(System.currentTimeMillis());
	}
	
	public static String getDateWithVersion() {
		return day.getYear()+""+
				(day.getMonth() < 10 ? "0"+day.getMonth() : day.getMonth()+"")+
				(day.getDayOfMonth() < 10 ? "0"+day.getDayOfMonth() : day.getDayOfMonth()+"")+
				(day.getHour() < 10 ? "0"+day.getHour() : day.getHour()+"")+
				(day.getMinutes() < 10 ? "0"+day.getMinutes() : day.getMinutes()+"")+
				(day.getSecond() < 10 ? "0"+day.getSecond() : day.getSecond()+"");
	}
}
