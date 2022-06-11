package cat.jiu.core.util.helpers;

import java.time.LocalDateTime;

public final class DayUtils {
	
	public final GlobalFestivalsDay global = new GlobalFestivalsDay();
	public final ChineseFestivalsDay chinese = new ChineseFestivalsDay();
	
	public int getYear() {
		LocalDateTime date = LocalDateTime.now();
		return date.getYear();
	}
	
	public int getMonth() {
		LocalDateTime date = LocalDateTime.now();
		return date.getMonth().getValue();
	}
	
	public int getDayOfWeek() {
		LocalDateTime date = LocalDateTime.now();
		return date.getDayOfWeek().getValue();
	}
	
	public int getDayOfMonth() {
		LocalDateTime date = LocalDateTime.now();
		return date.getDayOfMonth();
	}
	
	public int getHour() {
		LocalDateTime date = LocalDateTime.now();
		return date.getHour();
	}
	
	public int getMinutes() {
		LocalDateTime date = LocalDateTime.now();
		return date.getMinute();
	}
	
	public int getSecond() {
		LocalDateTime date = LocalDateTime.now();
		return date.getSecond();
	}
	
	public boolean isTheDay(int month, int day) {
		return this.getMonth() == month && this.getDayOfMonth() == day;
	}
	
	public String getDate() {
		return "[" + this.getYear() + "/" + this.getMonth() + "/" + this.getDayOfMonth() + "/" + this.getDayOfWeek() +  " | " + this.getHour() + ":" + this.getMinutes() + ":" + this.getSecond() + "]";
	}
	
	private static final DayUtils day = new DayUtils();
	
	public class ChineseFestivalsDay {
		public boolean isChineseNationalDay() {
			return day.isTheDay(10, 1);
		}
		
		public boolean is918() {
			return day.isTheDay(9, 18);
		}
		
		public boolean isMartyrMarkDay() {
			return day.isTheDay(9, 30);
		}
		
		public boolean isNanjingMassacre() {
			return day.isTheDay(12, 13);
		}
		
		public boolean isChineseLunarNewYear() {
			return false;
		}
	}
	
	public class GlobalFestivalsDay {
		
		public boolean isNewYear() {
			return day.isTheDay(1, 1);
		}
		
		public boolean isFoolsDay() {
			return day.isTheDay(4, 1);
		}
		
		public boolean isChristmas() {
			return day.isTheDay(12, 25);
		}
		
		public boolean isHalloween() {
			return (day.isTheDay(10, 31) || day.isTheDay(10, 30)) || day.isTheDay(11, 1);
		}
	}
}
