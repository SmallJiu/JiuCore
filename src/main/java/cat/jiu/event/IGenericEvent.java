package cat.jiu.event;

import java.lang.reflect.Type;

public interface IGenericEvent<V> {
	Type[] getGenericType();
}
