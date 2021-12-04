package cat.jiu.core;

import java.util.ArrayList;
import java.util.List;

import cat.jiu.core.util.JiuRandom;
import cat.jiu.core.util.JiuUtils;

public class CoreMain extends Thread implements Runnable {
	int i = 10;
	static JiuRandom rand = new JiuRandom();
	public static void main(String[] args) {
		int x = 999;
		System.out.println(x + " -> " + JiuUtils.other.numberToHexadecimal(x));
		
		while(true) {
			try { Thread.sleep(250); } catch (InterruptedException e) { e.printStackTrace(); }
			
			String hex0 = rand.nextHexadecima(0, rand.nextBoolean(10, 4), false);
			System.out.println(hex0 + " -> " + JiuUtils.other.hexadecimalToNumber(hex0, false) + " | " + hex0.indexOf("0x"));
		}
		
		/*
		String[] v = new String[] {"S", "t", "r", "i", "n", "g"};
		
		System.out.println(arrayToString(v));
		for(String s : custemSplitString(arrayToString(v), ",")) {
			System.out.println(s);
		}
		System.out.println("=======");
		
		int[] k = new int[] {1, 2, 3, 4, 5, 6};
		List<String> l = new ArrayList<>();
		for(int i : k) {
			l.add(i+"");
		}
		
		System.out.println(toString(l));
		System.out.println(arrayToString(l.toArray(new String[0])));
		System.out.println(l.toString());
		for(String s : custemSplitString(toString(l), ",")) {
			System.out.println(s);
		}
		/*
		System.out.println(new DayUtils().getDate());
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		main(args);
		/*
		CoreMain main = new CoreMain();
		Thread t1 = new Thread(main, "T1");
		Thread t2 = new Thread(main, "T2");
		Thread t3 = new Thread(main, "T3");
		Thread t4 = new Thread(main, "T4");
		
		t1.start();
		t2.start();
		t3.start();
		t4.start();
		*/
	}
	
	public static String arrayToString(String[] args) {
		String v = "";
		for(int i = 0; i < args.length; ++i) {
			if(i == args.length - 1) {
				v += args[i];
			}else {
				v += args[i] + ",";
			}
		}
		return v;
	}
	
	public static String[] custemSplitString(String arg, String separator) {
		if(arg.equals("")) {
			return new String[] {"null"};
		}
		return arg.split("\\" + separator);
	}
	
	public static <T> String toString(List<T> args) {
		List<String> v = new ArrayList<>();
		for(T t : args) {
			v.add(t+"");
		}
		return arrayToString(v.toArray(new String[0]));
	}

	@Override
	public void run() {
		while(true) {
			System.gc();
			synchronized ("Time") {
				try {
					Thread.sleep(50);
				} catch (InterruptedException e) { e.printStackTrace(); }
				if(i >= 0) {
					System.out.println("剩余票数为: " + i--);
				}
				if(i < 0) {
					System.exit(0);
				}
			}
		}
	}
}
