package cat.jiu.core.util.helpers;

import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;

import org.apache.commons.lang3.Validate;

import com.google.common.collect.Lists;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import cat.jiu.core.JiuCore;
import cat.jiu.core.util.JiuUtils;
import cat.jiu.core.util.Time;

import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;

public final class OtherUtils {
	public BigInteger letterToNumber(int ii, String value) {
		value = value.toLowerCase();
		StringBuilder s = new StringBuilder(ii);
		for(int i = 0; i < value.length(); i++) {
			char charr = value.charAt(i);
			if(JiuCore.CHAR_LETTERS.contains(charr)) {
				s.append(toNumber(charr));
			}
		}
		return JiuUtils.big_integer.create(s.toString());
	}
	private String toNumber(char value) {
		switch(value) {
			default: return "-1";
			case 'a': return "01";
			case 'b': return "02";
			case 'c': return "03";
			case 'd': return "04";
			case 'e': return "05";
			case 'f': return "06";
			case 'g': return "07";
			case 'h': return "08";
			case 'i': return "09";
			case 'j': return "10";
			case 'k': return "11";
			case 'l': return "12";
			case 'm': return "13";
			case 'n': return "14";
			case 'o': return "15";
			case 'p': return "16";
			case 'q': return "17";
			case 'r': return "18";
			case 's': return "19";
			case 't': return "20";
			case 'u': return "21";
			case 'v': return "22";
			case 'w': return "23";
			case 'x': return "24";
			case 'y': return "25";
			case 'z': return "26";
		}
	}
	
	public String toLangKey(String modid, String... args) {
		String[] arg = new String[args.length+1];
		arg[0] = modid;
		for(int i = 1; i < arg.length; i++) {
			arg[i] = args[i-1];
		}
		return this.addJoins(".", (Object[])arg);
	}
	
	public String addJoins(CharSequence delimiter, Object... args) {
		return this.addJoins(10L, delimiter, args);
	}
	
	public String addJoins(long format, CharSequence delimiter, Object... args) {
		StringJoiner j = new StringJoiner(delimiter);
		for(int i = 0; i < args.length; i++) {
			if(args[i] == null) continue;
			StringBuilder str = new StringBuilder(args[i].toString());
			if(format > 9)
				if(args[i].getClass() == Long.class && (long)args[i] < format) str.insert(0, this.format((long)args[i], format));
				else if(args[i].getClass() == Integer.class && (int)args[i] < format) str.insert(0, this.format((int)args[i], format));
				else if(args[i].getClass() == Short.class && (short)args[i] < format) str.insert(0, this.format((short)args[i],format));
				else if(args[i].getClass() == Byte.class && (byte)args[i] < format) str.insert(0, this.format((byte)args[i], format));
			
			j.add(str);
		}
		return j.toString();
	}
	
	private String format(long num, long f) {
		StringBuilder s = new StringBuilder();
		if(num < 10) s.append("0");
		for(int i = 0; i < Long.toString(f).length()-2; i++) {
			s.append("0");
		}
		return s.toString();
	}
	
	public boolean isEmpty(Object[] arg) {
		return arg == null || arg.length == 0;
	}
	public boolean isEmpty(List<?> arg) {
		return arg == null || arg.size() == 0;
	}
	public boolean isEmpty(JsonObject arg) {
		return arg == null || arg.size() == 0;
	}
	public boolean isEmpty(JsonArray arg) {
		return arg == null || arg.size() == 0;
	}
	public boolean isEmpty(ItemStack arg) {
		return arg == null || arg.isEmpty();
	}
	
	public <T> ArrayList<T> createArrayListWithSize(int size, T fill) {
		Validate.notNull(fill);
		@SuppressWarnings("unchecked")
		T[] fillObj = (T[]) new Object[size];
		Arrays.fill(fillObj, fill);
		return Lists.newArrayList(fillObj);
	}
	
	public ITextComponent createTextComponent(String arg, Object... objs) {
		return new TextComponentTranslation(arg, objs);
	}
	
	public ITextComponent createTextComponent(String arg, TextFormatting color, Object... objs) {
		ITextComponent text = new TextComponentTranslation(arg, objs);
		return text.setStyle(text.getStyle().setColor(color)); 
	}
	
	@Deprecated
	public long parseTick(int s, int tick) {
		return this.parseTick(0, s, tick);
	}

	@Deprecated
	public long parseTick(int m, int s, int tick) {
		return this.parseTick(0, m, s, tick);
	}

	@Deprecated
	public long parseTick(int h, int m, int s, int tick) {
		return this.parseTick(0, h, m, s, tick);
	}
	
	/**
	 * @see Time#parseTick(long, long, long, long, long)
	 */
	@Deprecated
	public long parseTick(int day, int h, int m, int s, int tick) {
		return Time.parseTick(day, h, m, s, tick);
	}
	
	public boolean runBatFile(String file, boolean canEjectWindow) {
		String out = canEjectWindow ? "/b " : "";
		return this.runSystemCommand("cmd /c start " + out + file);
	}
	
	public boolean runSystemCommand(String cmd) {
		Runtime rt = Runtime.getRuntime();
		Process ps = null;
		
		try {
			ps = rt.exec(cmd);
			ps.waitFor();
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}
		
		boolean lag = ps.exitValue() == 0;
		ps.destroy();
		ps = null;
		
		return lag;
	}
	
	@SuppressWarnings("unchecked")
	public <T extends Number> T[] toNumberArray(Class<T> num, String[] strs) {
		if(num == Long.class) {
			Long[] numss = new Long[strs.length];
			for (int i = 0; i < strs.length; i++) {
				numss[i] = Long.parseLong(strs[i]);
			}
			return (T[]) numss;
		}else if(num == Integer.class) {
			Integer[] numss = new Integer[strs.length];
			for (int i = 0; i < strs.length; i++) {
				numss[i] = Integer.parseInt(strs[i]);
			}
			return (T[]) numss;
		}else if(num == Short.class) {
			Short[] numss = new Short[strs.length];
			for (int i = 0; i < strs.length; i++) {
				numss[i] = Short.parseShort(strs[i]);
			}
			return (T[]) numss;
		}else if(num == Byte.class) {
			Byte[] numss = new Byte[strs.length];
			for (int i = 0; i < strs.length; i++) {
				numss[i] = Byte.parseByte(strs[i]);
			}
			return (T[]) numss;
		}else if(num == Double.class) {
			Double[] numss = new Double[strs.length];
			for (int i = 0; i < strs.length; i++) {
				numss[i] = Double.parseDouble(strs[i]);
			}
			return (T[]) numss;
		}else if(num == Float.class) {
			Float[] numss = new Float[strs.length];
			for (int i = 0; i < strs.length; i++) {
				numss[i] = Float.parseFloat(strs[i]);
			}
			return (T[]) numss;
		}
		return null;
	}
	
	public long[] toArray(Long[] args) {
		long[] arg = new long[args.length];
		for (int i = 0; i < arg.length; i++) {
			arg[i] = args[i];
		}
		return arg;
	}
	
	public int[] toArray(Integer[] args) {
		int[] arg = new int[args.length];
		for (int i = 0; i < arg.length; i++) {
			arg[i] = args[i];
		}
		return arg;
	}
	
	public short[] toArray(Short[] args) {
		short[] arg = new short[args.length];
		for (int i = 0; i < arg.length; i++) {
			arg[i] = args[i];
		}
		return arg;
	}
	
	public byte[] toArray(Byte[] args) {
		byte[] arg = new byte[args.length];
		for (int i = 0; i < arg.length; i++) {
			arg[i] = args[i];
		}
		return arg;
	}
	
	public double[] toArray(Double[] args) {
		double[] arg = new double[args.length];
		for (int i = 0; i < arg.length; i++) {
			arg[i] = args[i];
		}
		return arg;
	}
	
	public float[] toArray(Float[] args) {
		float[] arg = new float[args.length];
		for (int i = 0; i < arg.length; i++) {
			arg[i] = args[i];
		}
		return arg;
	}
	
	public Long[] toArray(long[] args) {
		Long[] arg = new Long[args.length];
		for (int i = 0; i < arg.length; i++) {
			arg[i] = args[i];
		}
		return arg;
	}
	
	public Integer[] toArray(int[] args) {
		Integer[] arg = new Integer[args.length];
		for (int i = 0; i < arg.length; i++) {
			arg[i] = args[i];
		}
		return arg;
	}
	
	public Short[] toArray(short[] args) {
		Short[] arg = new Short[args.length];
		for (int i = 0; i < arg.length; i++) {
			arg[i] = args[i];
		}
		return arg;
	}
	
	public Byte[] toArray(byte[] args) {
		Byte[] arg = new Byte[args.length];
		for (int i = 0; i < arg.length; i++) {
			arg[i] = args[i];
		}
		return arg;
	}
	
	public Double[] toArray(double[] args) {
		Double[] arg = new Double[args.length];
		for (int i = 0; i < arg.length; i++) {
			arg[i] = args[i];
		}
		return arg;
	}
	
	public Float[] toArray(float[] args) {
		Float[] arg = new Float[args.length];
		for (int i = 0; i < arg.length; i++) {
			arg[i] = args[i];
		}
		return arg;
	}
	
	public Integer hexadecimalToNumber(String seed) {
		Integer x = null;
		if(seed.startsWith("0x")) {
			x = Integer.parseInt(seed.substring(2), 16);
		}else {
			x = Integer.parseInt(seed,16);
		}
		return x;
	}
	
	public String toString(String[] args) {
		return this.toString(args, null, null, null);
	}
	
	public String toString(String[] args, CharSequence delimiter) {
		return this.toString(args, delimiter, "", "");
	}
	
	public String toString(String[] args, CharSequence start, CharSequence end) {
		return this.toString(args, ",", start, end);
	}
	
	public String toString(String[] args, CharSequence delimiter, CharSequence start, CharSequence end) {
		if(args == null || args.length == 0) {
			return "null";
		}
		StringJoiner j = start != null && delimiter != null && end != null ? new StringJoiner(delimiter, start, end) : new StringJoiner(",");
		for(String arg : args) {
			j.add(arg);
		}
		return j.toString();
	}
	
	public <T> String toString(List<T> args) {
		if(args.size() == 0) {
			return "null";
		}
		List<String> v = Lists.newArrayList();
		for(T t : args) {
			v.add(t.toString());
		}
		return this.toString(v.toArray(new String[v.size()]));
	}
	
	public <T extends Number> String toString(T[] args) {
		if(args.length == 0) {
			return "null";
		}
		List<T> l = Lists.newArrayList();
		for(T i : args) {
			l.add(i);
		}
		return this.toString(l);
	}
	
	public String toString(List<ItemStack> args, int qwq) {
		if(args.size() == 0) {
			return "null";
		}
		List<String> l = Lists.newArrayList();
		for(ItemStack i : args) {
			l.add(JiuUtils.item.toString(i));
		}
		return this.toString(l);
	}
	
	public String toString(ItemStack[] args) {
		if(args.length == 0) {
			return "null";
		}
		List<String> l = Lists.newArrayList();
		for(ItemStack i : args) {
			l.add(JiuUtils.item.toString(i));
		}
		return this.toString(l);
	}
	
	/**
	 * 
	 * @param date original array
	 * @param da original array
	 * @param start 
	 * @return original array copy
	 * @throws ArrayIndexOutOfBoundsException Use the length of the 0th array to build the array.If some array length less than this length, will throw.
	 *
	 * @author small_jiu
	 */
	public <T> T[][] one2two(T[][] date, T[] da, int start) throws ArrayIndexOutOfBoundsException {
		int k = start;
		
		T[][] date0 = date.clone();
		
		for(int i = 0; i < date.length; ++i) {
			for(int j = 0; j < date[0].length; ++j) {
				date0[i][j] = da[k];
				k++;
			}
		}
		
		return date0;
	}
	
	public <K> boolean containItemStackValue(Map<K, ItemStack> strs, ItemStack str, boolean checkAmout) {
		if(strs.isEmpty()) {
			return false;
		}
		for(ItemStack stri : strs.values()) {
			if(JiuUtils.item.equalsStack(stri, str, checkAmout)) {
				return true;
			}
		}
		return false;
	}
	
	public <V> boolean containItemStackKey(Map<ItemStack, V> strs, ItemStack str, boolean checkAmout) {
		if(strs.isEmpty()) {
			return false;
		}
		for(ItemStack stri : strs.keySet()) {
			if(JiuUtils.item.equalsStack(stri, str, checkAmout)) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 
	 * @param strs original list
	 * @param str contain key
	 * @return if is the key, return 'true', else return 'false'.
	 * 
	 * @author small_jiu
	 */
	public <T> boolean containKey(T[] strs, T str) {
		if(strs.length == 0) {
			return false;
		}
		for(T stri : strs) {
			if(stri.equals(str)) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 
	 * @param strs original list
	 * @param str contain key
	 * @return if is the key, return 'true', else return 'false'.
	 * 
	 * @author small_jiu
	 */
	public boolean containKey(String[] strs, String str) {
		if(strs.length == 0) {
			return false;
		}
		for(String stri : strs) {
			if(stri.equals(str)) {
				return true;
			}
		}
		return false;
	}
	
	public boolean containKey(int[] strs, int str) {
		if(strs.length == 0) {
			return false;
		}
		for(int stri : strs) {
			if(stri == str) {
				return true;
			}
		}
		return false;
	}
	
	public boolean containKey(long[] strs, long str) {
		if(strs.length == 0) {
			return false;
		}
		for(long stri : strs) {
			if(stri == str) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 
	 * @param args original list
	 * @return copy
	 * @throws ArrayIndexOutOfBoundsException Use the length of the 0th array to build the array.If some array length less than this length, will throw.
	 * 
	 * @author small_jiu
	 */
	public String[][] more2two(String[]... args) throws ArrayIndexOutOfBoundsException {
		String[][] arg = new String[args.length][args[0].length];
		int hang = arg.length;
		
		for(int i = 0; i < hang; ++i) {
			for(int j = 0; j < arg[i].length; ++j) {
				arg[i][j] = args[i][j];
			}
		}
		return arg;
	}
	
	/**
	 * 
	 * @param args original list
	 * @return copy
	 * @throws ArrayIndexOutOfBoundsException Use the length of the 0th array to build the array.If some array length less than this length, will throw.
	 * 
	 * @author small_jiu
	 */
	public int[][] more2two(int[]... args) throws ArrayIndexOutOfBoundsException {
		int[][] arg = new int[args.length][args[0].length];
		int hang = arg.length;
		
		for(int i = 0; i < hang; ++i) {
			for(int j = 0; j < arg[i].length; ++j) {
				arg[i][j] = args[i][j];
			}
		}
		return arg;
	}
	
	/**
	 * 
	 * @param arg original arg
	 * @param separator split symbol
	 * @return use 'split symbol' to split back the list
	 * 
	 * @author small_jiu
	 */
	public String[] custemSplitString(String arg, String separator){
		if(arg.isEmpty() || arg.equals("")) {
			return new String[] {"null"};
		}
		return arg.split("\\" + separator);
	}
	
	public String upperFirst(String arg) {
		return arg.substring(0, 1).toUpperCase() + arg.substring(1);
	}
	
	/*
	@SuppressWarnings("unchecked")
	public <T extends Object> T[] addArray(T[] array, T... e) {
		ArrayList<T> l = Lists.newArrayList();
		
		for(T t : array) {
			l.add(t);
		}
		
		for(T t : e) {
			l.add(t);
		}
		array = l.toArray(array);
		return l.toArray(array);
		
//		int leg = array.length + e.length;
//		T[] temp = (T[]) new Object[(leg + 1)];
//		
//		for(int i = 0; i < array.length; ++i) {
//			temp[i] = array[i];
//		}
//		
//		for(int i = array.length; i < leg; ++i) {
//			temp[i] = array[i];
//		}
//		
//		return temp;
	}
	
	@SuppressWarnings("unchecked")
	public <T extends Object> T[][] addArray(T[][] array, T[]... e) {
		int leg = array.length;
		int l = e[0].length;
		T[][] temp = (T[][]) new Object[(leg + 1)][l];
		
		for(int k = 0; k < array.length; ++k) {
			for(int v = 0; v < array[0].length; ++v) {
				temp[k][v] = array[k][v];
			}
		}
		
		for(int k = 0; k < e.length; ++k) {
			for(int v = 0; v < e[0].length; ++v) {
				temp[k][v] = e[k][v];
			}
		}
		array = temp;
		return temp;
	}
	*/
}
