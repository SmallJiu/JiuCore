package cat.jiu.core.util;

import java.util.Random;

public class JiuRandom {
	private Random rand;
	public Random getRandom() {return rand;}
	
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
		this.rand.setSeed(seed);
	}
	
	public void setSeed(Random rand) {
		this.rand = rand;
	}
	
	public String nextIP() {
		return JiuUtils.other.addJoins(0, ".", this.nextIntNoZero(254), this.nextInt(254), this.nextInt(254), this.nextIntNoZero(254));
	}

	public String nextMAC(boolean toUpperCase) {
		StringBuilder x = new StringBuilder();
		for(int i = 0; i < 12; ++i) {
			x.append(this.nextNumberOrLetter(toUpperCase, 25, 9));
		}
		return x.toString();
	}
	
	public String nextHexadecima(int size, boolean has0x, boolean toUpperCase) {
		if(size < 1 || size > 7) size = 7;
		String x = has0x ? "0x" : "";
		StringBuilder hex = new StringBuilder();
		
		for(int i = 0; i < size; ++i) {
			hex.append(this.nextNumberOrLetter(toUpperCase, 5, 9));
		}
		
		return x + hex.toString();
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
		int letterSeed0 = letterSeed > 25 ? 25 : letterSeed <= 0 ? 3 : letterSeed;

		if(i) {
			return this.nextLetter(letterSeed0, toUpperCase);
		}else {
			return Integer.toString(this.nextInt(numberseed));
		}
	}
	
	public String nextLetter(boolean toUpperCase) {
		return this.nextLetter(25, toUpperCase);
	}

	public String nextLetter(int seed, boolean toUpperCase) {
		int i = this.nextInt(seed > 25 ? 25 : seed <= 0 ? 3 : seed);
		return JiuUtils.other.toChar(i, toUpperCase);
	}
	
	public int nextIntFromRange(int min, int max) {
		if(min == max) return max; 
		if(min > max) {
			int temp_0 = min;
			int temp_1 = max;
			min = temp_1;
			max = temp_0; 
		}
		
		while(true) {
			int i = this.nextInt(max);
			if(i <= max && i >= min) return i;
		}
	}
	
	public int nextIntNoZero() {
		while(true) {
			int i = rand.nextInt();
			if(i != 0) return i;
		}
	}
	
	public int nextIntNoZero(int seed) {
		while(true) {
			int i = rand.nextInt(seed);
			if(i != 0) return i;
		}
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
	
	public void nextBytesNoZero(byte[] buf) {
		byte[] buff = buf;
		rand.nextBytes(buff);
		for(int i = 0; i < buff.length; i++) {
			byte b = buff[i];
			if(b!=0) {
				buf[i] = b;
			}else {
				buf[i] = (byte) this.nextIntNoZero(Byte.MAX_VALUE);
			}
		}
	}
	
	public void nextBytes(byte[] buf) {
		rand.nextBytes(buf);
	}
	
	public double nextDoubleNoZero() {
		while(true) {
			double i = rand.nextDouble();
			if(i != 0) return i;
		}
	}

	public double nextDouble() {
		return rand.nextDouble();
	}
	
	public float nextFloatNoZero() {
		while(true) {
			float i = rand.nextFloat();
			if(i != 0) return i;
		}
	}

	public float nextFloat() {
		return rand.nextFloat();
	}

	public double nextGaussian() {
		return rand.nextGaussian();
	}
	
	public long nextLongNoZero() {
		while(true) {
			long i = rand.nextLong();
			if(i != 0) return i;
		}
	}

	public long nextLong() {
		return rand.nextLong();
	}
	
	@Override
	public JiuRandom clone() {
		return new JiuRandom(rand);
	}
	
	@Override
	public int hashCode() {
		return this.rand.hashCode();
	}
	
	@Override
	public String toString() {
		return getClass().getName() + "@" + Integer.toHexString(this.rand.hashCode());
	}
	
	@Override
	public boolean equals(Object obj) {
		return this.rand.equals(obj);
	}
}
