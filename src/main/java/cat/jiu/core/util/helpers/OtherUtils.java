package cat.jiu.core.util.helpers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

import org.apache.commons.lang3.Validate;

import com.google.common.collect.Lists;

import cat.jiu.core.JiuCore;
import cat.jiu.core.util.JiuUtils;

import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;

public final class OtherUtils {
	public <T> ArrayList<T> createArrayListWithSize(int size, T fill){
		Validate.notNull(fill);
		Object[] fillObj = new Object[size];
		Arrays.fill(fillObj, fill);
		return new ArrayList<T>(Arrays.asList(fill));
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
	
	@Deprecated
	public String formatNumber(long value) {
		return JiuUtils.big_integer.format(value, 3);
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
	
	public String numberToHexadecimal(Integer seed) {
		return Integer.toHexString(seed);
	}
	
	public Integer hexadecimalToNumber(String seed, boolean checkTheMCWorldSize) {
		Integer x = null;
		try {
			if(seed.indexOf("0x") == 0) {
				x = Integer.parseInt(seed.substring(2), 16);
			}else {
				x = Integer.parseInt(seed,16);
			}
		} catch (NumberFormatException e) {
			e.printStackTrace();
			JiuCore.instance.log.error(e.getMessage() + " is not Hexadecimal!");
		}
		
		if(checkTheMCWorldSize && x > 30000000) {
			x = 25000000;
		}
		
		return x;
	}
	
	public String toString(String[] args) {
		if(args.length == 0) {
			return "null";
		}
		StringBuilder v = new StringBuilder();
		for(int i = 0; i < args.length; ++i) {
			if(i == args.length - 1) {
				v.append(args[i]);
			}else {
				StringBuilder v1 = new StringBuilder();
				v1.append(args[i]);
				v1.append(",");
				v.append(v1);
			}
		}
		return v.toString();
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
	
	@Deprecated
	public <K, V> boolean containKey(Map<K, V> strs, K str) {
		if(strs.isEmpty()) {
			return false;
		}
		return strs.containsKey(str);
	}
	@Deprecated
	public <K, V> boolean containValue(Map<K, V> strs, V str) {
		if(strs.isEmpty()) {
			return false;
		}
		return strs.containsValue(str);
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
	
	@Deprecated
	public boolean containKey(List<Integer> strs, int str) {
		if(strs.isEmpty()) {
			return false;
		}
		return strs.contains(str);
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

	@Deprecated
	public boolean containKey(List<Long> strs, long str) {
		if(strs.isEmpty()) {
			return false;
		}
		return strs.contains(str);
	}
	
	@Deprecated
	public boolean containKey(List<String> strs, String str) {
		if(strs.isEmpty()) {
			return false;
		}
		return strs.contains(str);
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
	
	@Deprecated
	public <T> List<T> copyArrayToList(T[] args){
		return Lists.newArrayList(args);
	}
	@Deprecated
	public <E> List<E> copyList(List<E> args){
		return Lists.newArrayList(args);
	}
	
	public String upperCaseToFirstLetter(String arg) {
		return arg.substring(0, 1).toUpperCase() + arg.substring(1);
	}
	
	/**
	 * 
	 * @param array original array
	 * @return copy
	 * 
	 * @author small_jiu
	 */
	@Deprecated
	public <T> T[][] copyArray(T[][] array){
		return array.clone();
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
	@Deprecated
	@Nullable
    public Potion getRegisteredMobEffect(String id) {
        return JiuUtils.entity.getRegisteredMobEffect(id);
    }
}
