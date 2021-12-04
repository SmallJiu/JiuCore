package cat.jiu.core.util.helpers;

import java.util.Date;

@SuppressWarnings("deprecation")
public final class DayUtils {
	
	public static final GlobalFestivalsDay global = new GlobalFestivalsDay();
	public static final ChineseFestivalsDay chinese = new ChineseFestivalsDay();
	
	public Date addDate(Date date, int year, int month, int day, int hour, int m, int s) {
		date.setYear((this.getYear() - 1900) + year);
		date.setMonth((this.getMonth() - 1) + month);
		date.setDate(this.getDayOfMonth() + day);
		date.setHours(this.getHour() + hour);
		date.setMinutes(this.getMinutes() + m);
		date.setSeconds(this.getSecond() + s);
		return date;
	}
	
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
	
	private static final DayUtils day = new DayUtils();
	
	public static class ChineseFestivalsDay {
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
	
	public static class GlobalFestivalsDay {
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
