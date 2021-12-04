package cat.jiu.core.util;

import java.util.Random;

public class JiuRandom extends Random {
	private static final long serialVersionUID = -8330324278894190587L;
	
	private Random rand;
	
	public JiuRandom() {
		this.rand = new Random();
	}
	
	public JiuRandom(Random rand) {
		this.rand = rand;
	}
	
	public JiuRandom(long seed) {
		this.rand = new Random(seed);
	}
	
	public void setSeed(long seed) {
		this.rand = new Random(seed);
	}
	
	public void setSeed(Random rand) {
		this.rand = rand;
	}
	
	public String nextIP() {
		int i0 = this.nextIntNoZero(254);
		int i1 = this.nextInt(254);
		int i2 = this.nextInt(254);
		int i3 = this.nextIntNoZero(254);
		
		return i0+"."+i1+"."+i2+"."+i3;
	}

	public String nextMAC(boolean toUpperCase) {
		String x = "";
		for(int i = 0; i < 12; ++i) {
			x += this.nextNumberOrLetter(toUpperCase, 25, 9);
		}
		return x;
	}
	
	public String nextHexadecima(int size, boolean has0x, boolean toUpperCase) {
		String x = has0x ? "0x" : "";
		String hex = "";
		
		if(size < 1 || size > 7) {
			return this.nextHexadecima(this.nextIntNoZero(7), has0x, toUpperCase);
		}
		for(int i = 0; i < size; ++i) {
			hex += this.nextNumberOrLetter(toUpperCase, 5, 9);
		}
		
		return x + hex;
	}

	public String nextNumberOrLetter(boolean toUpperCase) {
		boolean i = this.nextBoolean();

		if(i){
			return this.nextLetter(25, toUpperCase);
		}else {
			return Integer.toString(this.nextInt());
		}
	}

	public String nextNumberOrLetter(boolean toUpperCase, int letterSeed, int numberseed) {
		boolean i = this.nextBoolean();
		int letterSeed0 = letterSeed;
		if(letterSeed < 1) {
			letterSeed0 = 1;
		}
		if(letterSeed > 25) {
			letterSeed0 = 25;
		}

		if(i){
			return this.nextLetter(letterSeed0, toUpperCase);
		}else {
			return Integer.toString(this.nextInt(numberseed));
		}
	}
	
	public String nextLetter(boolean toUpperCase) {
		return this.nextLetter(25, toUpperCase);
	}

	public String nextLetter(int seed, boolean toUpperCase) {
		if(toUpperCase){
			return this.letter(seed).toUpperCase();
		}else {
			return this.letter(seed).toLowerCase();
		}
	}

	private String letter(int seed) {
		int i = this.nextInt(seed > 25 ? 25 : seed);

		switch(i){
			case 0:return "A";
			case 1:return "B";
			case 2:return "C";
			case 3:return "D";
			case 4:return "E";
			case 5:return "F";
			case 6:return "G";
			case 7:return "H";
			case 8:return "I";
			case 9:return "J";
			case 10:return "K";
			case 11:return "L";
			case 12:return "M";
			case 13:return "N";
			case 14:return "O";
			case 15:return "P";
			case 16:return "Q";
			case 17:return "R";
			case 18:return "S";
			case 19:return "T";
			case 20:return "U";
			case 21:return "V";
			case 22:return "W";
			case 23:return "X";
			case 24:return "Y";
			default:return "Z";
		}
	}
	
	public int nextIntFromRange(int min, int max) {
		int i = 0;
		
		if(min > max) {
			i = this.nextInt(min);
		}else {
			i = this.nextInt(max);
		}
		
		if((i <= max && i >= min) || (i <= min && i >= max)) {
			if(min > max) {
				if(i <= min && i >= max){
					return i;
				}else{
					return this.nextIntFromRange(min, max);
				}
			}else{
				if(i <= max && i >= min){
					return i;
				}else{
					return this.nextIntFromRange(min, max);
				}
			}
		}else {
			return this.nextIntFromRange(min, max);
		}
	}
	
	public int nextIntNoZero() {
		int i = rand.nextInt();
		
		return i != 0 ? i : this.nextIntNoZero();
	}
	
	public int nextIntNoZero(int seed) {
		int i = rand.nextInt(seed);
		
		return i != 0 ? i : this.nextIntNoZero(seed);
	}

	public int nextInt() {
		return rand.nextInt();
	}

	public int nextInt(int seed) {
		return rand.nextInt(seed);
	}
	
	public boolean nextBoolean(int seed, int chance) {
		int i = this.nextInt(seed);
		return i <= chance;
	}
	
	public boolean nextBoolean(int chance) {
		return this.nextBoolean(100, chance);
	}
	
	public boolean nextBoolean() {
		return rand.nextBoolean();
	}
	
	public void nextBytes(byte[] buf) {
		rand.nextBytes(buf);
	}
	
	public double nextDoubleNoZero() {
		double i = rand.nextDouble();
		
		return i != 0 ? i : this.nextDoubleNoZero();
	}

	public double nextDouble() {
		return rand.nextDouble();
	}
	
	public float nextFloatNoZero() {
		float i = rand.nextFloat();
		
		return i != 0 ? i : this.nextFloatNoZero();
	}

	public float nextFloat() {
		return rand.nextFloat();
	}

	public double nextGaussian() {
		return rand.nextGaussian();
	}
	
	public long nextLongNoZero() {
		long i = rand.nextLong();
		
		return i != 0 ? i : this.nextLongNoZero();
	}

	public long nextLong() {
		return rand.nextLong();
	}
}
