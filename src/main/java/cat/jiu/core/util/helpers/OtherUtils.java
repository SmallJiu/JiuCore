package cat.jiu.core.util.helpers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.LongStream;

import javax.annotation.Nullable;

import org.apache.commons.lang3.Validate;

import com.google.common.collect.Lists;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import cat.jiu.core.api.IExecuteable;
import cat.jiu.core.util.JiuUtils;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;

public final class OtherUtils {
	public int tryExecute(IExecuteable executeable, int maxRetry) {
		int tryCount = 0;
		boolean isSuccess = false;
		Throwable lastException = null;
		do {
			try {
				isSuccess = executeable.execute(tryCount, lastException);
				if(!isSuccess) {
					lastException = null;
				}
			}catch(Exception e) {
				lastException = e;
			}
			if(!isSuccess) {
				tryCount++;
			}
		}while(!isSuccess && tryCount <= maxRetry);
		return tryCount;
	}
	
	public int[] perseInt(long value) {
		int[] result;
		if(value <= Integer.MAX_VALUE) {
			result = new int[] {(int) value};
		}else {
			int intNum = (int) (value / Integer.MAX_VALUE);
			if(value % Integer.MAX_VALUE > 0) {
				result = new int[intNum+1];
				for(int i = 0; i < result.length-1; i++) {
					result[i] = Integer.MAX_VALUE;
				}
				result[result.length-1] = (int) (value % Integer.MAX_VALUE);
			}else {
				result = new int[intNum];
				for(int i = 0; i < result.length; i++) {
					result[i] = Integer.MAX_VALUE;
				}
			}
		}
		return result;
	}
	
	private static final int MAX = 104857600;
	private int maxLength = MAX;
	public int setMaxLength(int max) {
		int old = this.maxLength;
		this.maxLength = max;
		return old;
	}
	public void resetMaxLength() {
		this.maxLength = MAX;
	}
	
	/**
	 * @param value max: 104857600 * 2147483647 + 2147483646 (225,179,983,411,150,846)
	 */
	public int[] perseInt(BigInteger value) {
		int[] result;
		if(JiuUtils.big_integer.lessOrEqual(value, BigIntegerUtils.INTEGER_MAX)) {
			result = new int[1];
			result[0] = value.intValue();
		}else {
			int intNum = value.divide(BigIntegerUtils.INTEGER_MAX).intValue();
			if(intNum >= this.maxLength) {
				intNum = this.maxLength;
			}
			if(JiuUtils.big_integer.greater(value.remainder(BigIntegerUtils.INTEGER_MAX), BigInteger.ZERO)) {
				result = new int[intNum+1];
				for(int i = 0; i < result.length-1; i++) {
					result[i] = Integer.MAX_VALUE;
				}
				result[result.length-1] = value.remainder(BigIntegerUtils.INTEGER_MAX).intValue();
			}else {
				result = new int[intNum];
				for(int i = 0; i < result.length; i++) {
					result[i] = Integer.MAX_VALUE;
				}
			}
		}
		return result;
	}
	
	/** 'A' - 'Z' */private static final List<Character> CHAR_LETTERS = Lists.newArrayList('A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z');
	/** "A" - "Z" */private static final List<String> STRING_LETTERS = Lists.newArrayList("A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z");
	public boolean containsLetter(char c) {
		return CHAR_LETTERS.contains(c);
	}
	public boolean containsLetter(String c) {
		return STRING_LETTERS.contains(c);
	}
	
	/** '0' - '9' */private static final List<Character> CHAR_NUMBERS = Lists.newArrayList('0', '1', '2', '3', '4', '5', '6', '7', '8', '9');
	/** "0" - "9" */private static final List<String> STRING_NUMBERS = Lists.newArrayList("0", "1", "2", "3", "4", "5", "6", "7", "8", "9");
	public boolean containsNumber(char num) {
		return CHAR_NUMBERS.contains(num);
	}
	public boolean containsNumber(String num) {
		return STRING_NUMBERS.contains(num);
	}
	
	public long getLong(String value) {
		long v = 0;
		try {
			v = Long.parseLong(value);
		}catch(Exception e) {
			for(int i = 0; i < value.length(); i++) {
				try {
					v += Long.parseLong(String.valueOf(value.charAt(i)));
				}catch(Exception e2) {
					v += value.charAt(i);
				}
			}
		}
		return v;
	}
	
	public BlockPos toPos(NBTTagCompound nbt) {
		if(nbt==null || nbt.getSize()==0) return null;
		return new BlockPos(nbt.getInteger("x"), nbt.getInteger("y"), nbt.getInteger("z"));
	}
	public NBTTagCompound toNBT(BlockPos pos) {
		NBTTagCompound nbt = new NBTTagCompound();
		nbt.setInteger("x", pos.getX());
		nbt.setInteger("y", pos.getY());
		nbt.setInteger("z", pos.getZ());
		return nbt;
	}
	
	public String toStringPath(ResourceLocation loc) {
		return "./assets/" + loc.getNamespace() + "/" + loc.getPath();
	}
	
	public String formatTextColor(String str, char formatChar) {
		StringBuilder sb = new StringBuilder();
		for(int i = 0; i < str.length(); i++) {
			char ch = str.charAt(i);
			if(ch == formatChar && i+1 < str.length()) {
				if(isFormatChar(str.charAt(i+1))) {
					sb.append("§");
					continue;
				}
			}
			sb.append(ch);
		}
		return sb.toString();
	}
	private boolean isFormatChar(char c) {
		for(TextFormatting format : TextFormatting.values()) {
			if(format.formattingCode == c) return true;
		}
		return false;
	}
	
	@Nullable
	public File getFileFromResourceLocation(ResourceLocation rl) {
		return new File(".assets" + File.pathSeparator + rl.getNamespace() + File.pathSeparator + rl.getPath().replace('/', File.pathSeparatorChar));
	}
	
	public boolean resourceFileExistsInAssets(ResourceLocation location) {
		File f = this.getFileFromResourceLocation(location);
		return f==null ? false : f.exists();
	}
	
	public double[] reverseOrder(double... array) {
		array = DoubleStream.of(array)
					.boxed()
					.sorted(Comparator.reverseOrder())
					.mapToDouble(Double::doubleValue)
					.toArray();
		return array;
	}
	
	public long[] reverseOrder(long... array) {
		array = LongStream.of(array)
					.boxed()
					.sorted(Comparator.reverseOrder())
					.mapToLong(Long::longValue)
					.toArray();
		return array;
	}
	
	public int[] reverseOrder(int... array) {
		array = IntStream.of(array)
					.boxed()
					.sorted(Comparator.reverseOrder())
					.mapToInt(Integer::intValue)
					.toArray();
		return array;
	}
	
	public Class<?>[] getCallingStack() {
		return securityManager.getStackClasses();
	}
	static final CoreSecurityManager securityManager = new CoreSecurityManager();
	static class CoreSecurityManager extends SecurityManager {
        Class<?>[] getStackClasses() {
            return super.getClassContext();
        }
    }
	
	public BigInteger lettersToNumber(int defaultNum, String value) {
		value = value.toLowerCase();
		StringBuilder s = new StringBuilder(defaultNum);
		for(char charr : value.toCharArray()) {
			if(this.containsLetter(charr)) {
				s.append((int)charr);
			}
		}
		return JiuUtils.big_integer.create(s.toString());
	}
	
	public String toChar(int value, boolean toUpper) {
		String result = "a-z";
		switch(value) {
			case 0: result = "a";
			case 1: result = "b";
			case 2: result = "c";
			case 3: result = "d";
			case 4: result = "e";
			case 5: result = "f";
			case 6: result = "g";
			case 7: result = "h";
			case 8: result = "i";
			case 9: result = "j";
			case 10: result = "k";
			case 11: result = "l";
			case 12: result = "m";
			case 13: result = "n";
			case 14: result = "o";
			case 15: result = "p";
			case 16: result = "q";
			case 17: result = "r";
			case 18: result = "s";
			case 19: result = "t";
			case 20: result = "u";
			case 21: result = "v";
			case 22: result = "w";
			case 23: result = "x";
			case 24: result = "y";
			case 25: result = "z";
		}
		return toUpper ? result.toUpperCase() : result;
	}
	
	@Deprecated
	public int toNumber(char value) {
		return value;
	}
	
	public String toLangKey(String modid, String... args) {
		String[] arg = new String[args.length+1];
		arg[0] = modid;
		for(int i = 1; i < arg.length; i++) {
			arg[i] = args[i-1];
		}
		return this.addJoins(".", (Object[])arg);
	}
	
	public String toString(Object... args) {
		return this.addJoins("", args);
	}
	
	public String addJoins(CharSequence delimiter, Object... args) {
		return this.addJoins(0, delimiter, "", "", args);
	}
	
	public String addJoins(CharSequence delimiter, CharSequence prefix, CharSequence suffix, Object... args) {
		return this.addJoins(0, delimiter, prefix, suffix, args);
	}

	public String toString(long format, Object... args) {
		return this.addJoins(format, "", args);
	}
	
	public String addJoins(long format, CharSequence delimiter, Object... args) {
		return this.addJoins(format, delimiter, "", "", args);
	}
	
	public String addJoins(long format, CharSequence delimiter, CharSequence prefix, CharSequence suffix, Object... args) {
		StringJoiner j = new StringJoiner(delimiter);
		for(int i = 0; i < args.length; i++) {
			if(args[i] == null) {
				j.add("null");
				continue;
			}
			StringBuilder str = new StringBuilder(args[i].toString());
			if(format > 9)
				if(args[i] instanceof Number) {
					if(args[i] instanceof Long && (long)args[i] < format) str.insert(0, this.format((long)args[i], format));
					else if(args[i] instanceof Integer && (int)args[i] < format) str.insert(0, this.format((int)args[i], format));
					else if(args[i] instanceof Short && (short)args[i] < format) str.insert(0, this.format((short)args[i],format));
					else if(args[i] instanceof Byte && (byte)args[i] < format) str.insert(0, this.format((byte)args[i], format));
					else if(args[i] instanceof BigInteger && JiuUtils.big_integer.less((BigInteger) args[i], BigInteger.valueOf(format))) str.insert(0, this.format((BigInteger)args[i], format));
				}
			j.add(str);
		}
		return j.toString();
	}
	
	private StringBuilder format(BigInteger num, long f) {
		StringBuilder s = new StringBuilder();
		if(JiuUtils.big_integer.less(num, BigInteger.TEN)) s.append("0");
		for(int i = 0; i < Long.toString(f).length()-2; i++) {
			s.append(0);
		}
		return s;
	}
	
	private StringBuilder format(long num, long f) {
		StringBuilder s = new StringBuilder();
		if(num < 10) s.append("0");
		for(int i = 0; i < Long.toString(f).length()-2; i++) {
			s.append(0);
		}
		return s;
	}
	
	public boolean isEmpty(Object[] arg) {
		return arg == null || arg.length == 0;
	}
	public boolean isEmpty(List<?> arg) {
		return arg == null || arg.isEmpty();
	}
	public boolean isEmpty(Map<?,?> arg) {
		return arg == null || arg.isEmpty();
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
	
	@SuppressWarnings("unchecked")
	public <E> ArrayList<E> createArrayListWithSize(int size, E fill) {
		Validate.notNull(fill);
		Object[] fillObj = new Object[size];
		Arrays.fill(fillObj, fill);
		return Lists.newArrayList((E[])fillObj);
	}
	
	public ITextComponent createTextComponent(String arg, Object... objs) {
		return new TextComponentTranslation(arg, objs);
	}
	
	public ITextComponent createTextComponent(String arg, TextFormatting color, Object... objs) {
		ITextComponent text = new TextComponentTranslation(arg, objs);
		return text.setStyle(text.getStyle().setColor(color)); 
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

	@SuppressWarnings("unchecked")
	public <T extends Number, E extends Number> T[] toNumberArray(T type, E[] args) {
		T[] arg = (T[]) new Object[args.length];
		for(int i = 0; i < args.length; i++) {
			arg[i] = (T) args[i];
		}
		return arg;
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
			x = Integer.parseInt(seed, 16);
		}
		return x;
	}
	
	public <T> String toString(List<T> args) {
		if(args.size() == 0) {
			return "null";
		}
		List<String> v = Lists.newArrayList();
		for(T t : args) {
			v.add(t.toString());
		}
		return this.toString(v.toArray());
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
	
	public void moveFile(File source, File target) {
		if(source==null || target==null || !source.exists()) return;
		
		target.mkdirs();
		if(!target.exists()) {
			try {
				target.createNewFile();
			}catch(IOException e) {
				e.printStackTrace();
			}
		}
		
		try(FileInputStream is = new FileInputStream(source);
			FileOutputStream os = new FileOutputStream(target)) {
			byte[] buf = new byte[4096];
			int read = 0;
			while((read = is.read(buf)) != -1) {
				os.write(buf, 0, read);
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public String formatBytes(long bytes, int length) {
		String s = "";
		if(bytes >= TB) {
			s = String.valueOf(bytes / TB);
			return s + "." + subString(String.valueOf(bytes).substring(s.length()), length) + " TB";
		}
		if(bytes >= GB) {
			s = String.valueOf(bytes / GB);
			return s + "." + subString(String.valueOf(bytes).substring(s.length()), length) + " GB";
		}
		if(bytes >= MB) {
			s = String.valueOf(bytes / MB);
			return s + "." + subString(String.valueOf(bytes).substring(s.length()), length) + " MB";
		}
		if(bytes >= KB) {
			s = String.valueOf(bytes / KB);
			return s + "." + subString(String.valueOf(bytes).substring(s.length()), length) + " KB";
		}
		if(bytes < 1024) {
			return bytes + " B";
		}
		return "NaN";
	}
	
	private String subString(String str, int endlength) {
		if(str.length() > endlength) {
			return str.substring(0, endlength);
		}else {
			return str;
		}
	}
	
	public static final long KB = (long) Math.pow(1024, 2);
	public static final long MB = (long) Math.pow(1024, 3);
	public static final long GB = (long) Math.pow(1024, 4);
	public static final long TB = (long) Math.pow(1024, 5);
	
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
