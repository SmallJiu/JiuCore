package cat.jiu.core.api.handler;

import java.util.List;

import com.google.common.collect.Lists;

public interface IBuilder<T> {
	T builder(Object... args);

	public static <T> IBuilder<List<T>> builds(int length, T full) {
		return args -> {
			List<T> s = Lists.newArrayList();
			for(int i = 0; i < length; i++) {
				s.add(full);
			}
			return s;
		};
	}

	public static IBuilder<List<String>> strings = args -> {
		if(args.length >= 2) {
			List<String> s = Lists.newArrayList();
			for(int i = 0; i < (int) args[0]; i++) {
				s.add((String) args[1]);
			}
			return s;
		}
		return null;
	};
	public static IBuilder<List<Boolean>> booleans = args -> {
		if(args.length >= 2) {
			List<Boolean> s = Lists.newArrayList();
			for(int i = 0; i < (int) args[0]; i++) {
				s.add((Boolean) args[1]);
			}
			return s;
		}
		return null;
	};
	public static IBuilder<List<Integer>> integers = args -> {
		if(args.length >= 2) {
			List<Integer> s = Lists.newArrayList();
			for(int i = 0; i < (int) args[0]; i++) {
				s.add((Integer) args[1]);
			}
			return s;
		}
		return null;
	};
	public static IBuilder<List<Long>> longs = args -> {
		if(args.length >= 2) {
			List<Long> s = Lists.newArrayList();
			for(int i = 0; i < (int) args[0]; i++) {
				s.add((Long) args[1]);
			}
			return s;
		}
		return null;

	};
	public static IBuilder<List<Short>> shorts = args -> {
		if(args.length >= 2) {
			List<Short> s = Lists.newArrayList();
			for(int i = 0; i < (int) args[0]; i++) {
				s.add((Short) args[1]);
			}
			return s;
		}
		return null;

	};
	public static IBuilder<List<Byte>> bytes = args -> {
		if(args.length >= 2) {
			List<Byte> s = Lists.newArrayList();
			for(int i = 0; i < (int) args[0]; i++) {
				s.add((Byte) args[1]);
			}
			return s;
		}
		return null;
	};
	public static IBuilder<List<Double>> doubles = args -> {
		if(args.length >= 2) {
			List<Double> s = Lists.newArrayList();
			for(int i = 0; i < (int) args[0]; i++) {
				s.add((Double) args[1]);
			}
			return s;
		}
		return null;
	};
	public static IBuilder<List<Float>> floats = args -> {
		if(args.length >= 2) {
			List<Float> s = Lists.newArrayList();
			for(int i = 0; i < (int) args[0]; i++) {
				s.add((Float) args[1]);
			}
			return s;
		}
		return null;
	};
}
