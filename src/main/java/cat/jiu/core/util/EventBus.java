package cat.jiu.core.util;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Collections;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import cat.jiu.core.api.Event;

public class EventBus {
	private final ConcurrentHashMap<Class<? extends net.minecraftforge.fml.common.eventhandler.Event>, CopyOnWriteArrayList<Listener>> listeners = new ConcurrentHashMap<>();
	
	public void register(Object o) {
		boolean isStatic = o.getClass() == Class.class;
		
		for(Method method : (isStatic ? (Class<?>)o : o.getClass()).getDeclaredMethods()) {
			if (isStatic && !Modifier.isStatic(method.getModifiers()))
                continue;
            else if (!isStatic && Modifier.isStatic(method.getModifiers()))
                continue;
			if(method.isAnnotationPresent(Event.class)) {
				Class<?>[] parameterTypes = method.getParameterTypes();
				if (parameterTypes.length != 1) {
                    throw new IllegalArgumentException(
                        "Method " + method + " has @Event annotation, but requires " + parameterTypes.length +
                        " arguments.  Event handler methods must require a single argument."
                    );
                }
				
				Class<?> parameterType = method.getParameterTypes()[0];
				if(!net.minecraftforge.fml.common.eventhandler.Event.class.isAssignableFrom(parameterType)) {
					throw new IllegalArgumentException("Method " + method + " has @Event annotation, but takes a argument that is not an Event " + parameterType);
				}
				
				Listener linstener = new Listener(o, method);
				
				@SuppressWarnings("unchecked")
				Class<? extends net.minecraftforge.fml.common.eventhandler.Event> listenerType = (Class<? extends net.minecraftforge.fml.common.eventhandler.Event>) parameterType;
				if(this.listeners.containsKey(listenerType)) {
					if(!this.listeners.get(listenerType).contains(linstener)) {
						this.listeners.get(listenerType).add(linstener);
					}
				}else{
					this.listeners.put(listenerType, new CopyOnWriteArrayList<>(Collections.singletonList(linstener)));
				}
			}
		}
	}
	
	public void unregister(Object o) {
		this.listeners.values().forEach(list -> list.removeIf(e -> e.object.equals(o)));
		this.listeners.entrySet().removeIf(e -> e.getValue().isEmpty());
	}
	
	public void post(net.minecraftforge.fml.common.eventhandler.Event event) {
		CopyOnWriteArrayList<Listener> events = listeners.get(event.getClass());
		if(events==null) {
			return;
		}
		for(Listener listener : events) {
			try {
				listener.method.invoke(listener.object, event);
				if(event.isCanceled()) {
					break;
				}
			}catch(Exception e2) {
				e2.printStackTrace();
			}
		}
	}
	
	class Listener {
		final Object object;
		final Method method;
		public Listener(Object object, Method method) {
			this.object = object;
			this.method = method;
			this.method.setAccessible(true);
		}
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((method == null) ? 0 : method.hashCode());
			result = prime * result + ((object == null) ? 0 : object.hashCode());
			return result;
		}
		@Override
		public boolean equals(Object obj) {
			if(this == obj)
				return true;
			if(obj == null)
				return false;
			if(getClass() != obj.getClass())
				return false;
			Listener other = (Listener) obj;
			if(method == null) {
				if(other.method != null)
					return false;
			}else if(!method.equals(other.method))
				return false;
			if(object == null) {
				if(other.object != null)
					return false;
			}else if(!object.equals(other.object))
				return false;
			return true;
		}
	}
}
