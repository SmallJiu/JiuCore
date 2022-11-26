package cat.jiu.core.util.helpers;

import java.time.LocalDateTime;

public final class DayUtils {
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
}
