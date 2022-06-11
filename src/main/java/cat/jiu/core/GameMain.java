package cat.jiu.core;

import java.util.Random;

public class GameMain {
	public static void main(String[] args) throws InterruptedException {
		GameMain m = new GameMain();
		
		while(true) {
			System.out.println("===============================");
			System.out.println(m.nextIntFromRange(10, 9));
			Thread.sleep(500);
		}
	}
	Random rand = new Random();
	public int nextIntFromRange(int min, int max) {
		if(min == max) return max; 
		int i = rand.nextInt(max);
		System.out.println("Min: " + min + ", Max: " + max + ", RandInt: " + i);
		if(min > max) {
			int temp_0 = min;
			int temp_1 = max;
			min = temp_1;
			max = temp_0; 
		}
		if(i <= max && i >= min){
			return i;
		}else{
			return this.nextIntFromRange(min, max);
		}
	}
}
