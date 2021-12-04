package cat.jiu.core.util.helpers;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

import cat.jiu.core.JiuCore;
import cat.jiu.core.util.JiuUtils;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;

public final class OtherUtils {
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
//			JiuCore.instance.log.error(e.getMessage() + " is not Hexadecimal!");
			System.out.println(JiuUtils.day.getDate() + ": " + e.getMessage() + " is not Hexadecimal!");
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
	
	public <T> String toString(List<T> args) {
		List<String> v = new ArrayList<>();
		for(T t : args) {
			v.add(t+"");
		}
		return this.toString(v.toArray(new String[0]));
	}
	
	public String toString(int[] args) {
		List<Integer> l = new ArrayList<>();
		for(int i : args) {
			l.add(i);
		}
		return this.toString(l);
	}
	
	public String toString(long[] args) {
		List<Long> l = new ArrayList<>();
		for(long i : args) {
			l.add(i);
		}
		return this.toString(l);
	}
	
	public String toString(float[] args) {
		List<Float> l = new ArrayList<>();
		for(float i : args) {
			l.add(i);
		}
		return this.toString(l);
	}
	
	public String toString(double[] args) {
		List<Double> l = new ArrayList<>();
		for(double i : args) {
			l.add(i);
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
		
		for(int i =0; i < date.length; ++i) {
			for(int j =0; j < date[0].length; ++j) {
				date0[i][j] = da[k];
				k++;
			}
		}
		
		return date0;
	}
	
	public <K> boolean containItemStackValue(Map<K, ItemStack> strs, ItemStack str) {
		for(ItemStack stri : strs.values()) {
			if(JiuUtils.item.equalsStack(stri, str)) {
				return true;
			}
		}
		return false;
	}
	
	public <V> boolean containItemStackKey(Map<ItemStack, V> strs, ItemStack str) {
		for(ItemStack stri : strs.keySet()) {
			if(JiuUtils.item.equalsStack(stri, str)) {
				return true;
			}
		}
		return false;
	}
	
	public <K, V> boolean containKey(Map<K, V> strs, K str) {
		for(K stri : strs.keySet()) {
			if(stri.equals(str)) {
				return true;
			}
		}
		return false;
	}
	
	public <K, V> boolean containValue(Map<K, V> strs, V str) {
		for(V stri : strs.values()) {
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
	public <T> boolean containKey(T[] strs, T str) {
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
		for(String stri : strs) {
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
	public boolean containKey(List<String> strs, String str) {
		for(String stri : strs) {
			if(stri.equals(str)) {
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
		if(arg.equals("")) {
			return new String[] {"null"};
		}
		return arg.split("\\" + separator);
	}
	
	public <T> List<T> copyArrayToList(T[] list0){
		List<T> list = new ArrayList<T>();
		for(T o : list0) {
			list.add(o);
		}
		return list;
	}
	
	/**
	 * 
	 * @param list0 original list
	 * @return copy
	 * 
	 * @author small_jiu
	 */
	public <T> List<T> copyList(List<T> list0){
		List<T> list = new ArrayList<T>();
		for(T o : list0) {
			list.add(o);
		}
		return list;
	}
	
	public String upperCaseToFistLetter(String name) {
		return name.substring(0, 1).toUpperCase() + name.substring(1);
	}
	
	/**
	 * 
	 * @param array original array
	 * @return copy
	 * 
	 * @author small_jiu
	 */
	@SuppressWarnings("unchecked")
	public <T extends Object> T[][] copyArray(T[][] array){
		T[][] temp = (T[][]) new Object[array.length+1][array[0].length];
		
		for(int i = 0; i < array.length; ++i) {
			for(int j = 0; j < array[i].length; ++j) {
				temp[i][j] = array[i][j];
			}
		}
		return temp;
	}
	/*
	@SuppressWarnings("unchecked")
	public <T extends Object> T[] addArray(T[] array, T... e) {
		ArrayList<T> l = new ArrayList<T>();
		
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
	/**
	 * {@link Potion#getPotionFromResourceLocation(String)}
	 */
	@Nullable
    public Potion getRegisteredMobEffect(String id) {
        Potion potion = Potion.getPotionFromResourceLocation(id);
        
        if(potion == null) {
        	JiuCore.instance.log.fatal("Effect not found: " + id);
        	return null;
        }else {
        	return potion;
        }
    }
}
