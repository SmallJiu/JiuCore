package cat.jiu.core;

import cat.jiu.core.util.JiuRandom;

public class Main {
	public static void main(String[] args) {
		int i = 1;
		int t = 0;
		int f = 0;
		
		for(int k = 1; k <= 100; ++k) {
			boolean j = new JiuRandom().nextBoolean(100, 20);
			if(j) {
				System.out.println(j + "  | " + "True: " + t + ", False: " + f + ", Time: " + i);
			}else {
				System.out.println(j + " | " + "True: " + t + ", False: " + f + ", Time: " + i);
			}
			
			i++;
			if(j) {
				t++;
			}else {
				f++;
			}
			try {
				Thread.sleep(150);
			} catch (InterruptedException e) {e.printStackTrace();}
		}
	}
}
