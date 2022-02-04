package cat.jiu.core;

import java.math.BigInteger;
import java.util.Date;

public class CoreMain {
	public static void main(String[] args) {
		System.out.println(getDateWithVersion());
		
		BigInteger meta = BigInteger.valueOf(1);
		meta = meta.add(BigInteger.valueOf(3));
		System.out.println("Amount: " + meta.toString());
		
		for(int i = 0; i < 16; i++) {
			meta = meta.add(BigInteger.valueOf((long)i));
			System.out.println("Meta: " + i + " | Amount: " + meta);
		}
	}
	
	@SuppressWarnings("deprecation")
	public static String getDateWithVersion() {
		Date date = new Date();
		int year = date.getYear() + 1900;
		int month = date.getMonth() + 1;
		int day = date.getDay();
		int hours = date.getHours();
		int min = date.getMinutes();
		int sec = date.getSeconds();
		
		return (year+"")+
			   (month < 10 ? "0" + month : month+"")+
			   (day < 10 ? "0" + day : day+"")+
			   (hours < 10 ? "0" + hours : hours+"")+
			   (min < 10 ? "0" + min : min+"")+
			   (sec < 10 ? "0" + sec : sec+"");
	}
}
