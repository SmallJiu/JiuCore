package cat.jiu.core.util.helpers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;

import org.apache.commons.lang3.Validate;

import com.google.common.collect.Lists;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import cat.jiu.core.util.JiuUtils;

import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;

public final class OtherUtils {
	public <T> boolean isEmpty(T[] arg) {
		return arg == null || arg.length == 0;
	}
	public <T> boolean isEmpty(List<T> arg) {
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
		return new ArrayList<T>(Arrays.asList(fillObj));
	}
	
	public ITextComponent createTextComponent(String arg, Object... objs) {
		return new TextComponentTranslation(arg, objs);
	}
	
	public ITextComponent createTextComponent(String arg, TextFormatting color, Object... objs) {
		ITextComponent text = new TextComponentTranslation(arg, objs);
		return text.setStyle(text.getStyle().setColor(color)); 
	}
	
	public long parseTick(int s, int tick) {
		return this.parseTick(0, s, tick);
	}
	
	public long parseTick(int m, int s, int tick) {
		return this.parseTick(0, m, s, tick);
	}
	
	public long parseTick(int h, int m, int s, int tick) {
		return this.parseTick(0, h, m, s, tick);
	}
	
	public long parseTick(int day, int h, int m, int s, int tick) {
		return (((((((day*24)+h)*60)+m)*60)+s)*20)+tick;
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
	
	public Integer[] toArray(int[] args) {
		Integer[] arg = new Integer[args.length];
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
	
	public String upperCaseToFirstLetter(String arg) {
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
