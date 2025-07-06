package cat.jiu.event;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Collections;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class EventBus {
	private final ConcurrentHashMap<Class<? extends IEvent>, CopyOnWriteArrayList<Listener>> listeners = new ConcurrentHashMap<>();
	
	public void register(Object o) {
		boolean isStatic = o.getClass() == Class.class;
		
		Class<? extends Annotation> annotation = this.getSubscribeAnnotation();
		
		for(Method method : (isStatic ? (Class<?>)o : o.getClass()).getDeclaredMethods()) {
			if (isStatic && !Modifier.isStatic(method.getModifiers()))
                continue;
            else if (!isStatic && Modifier.isStatic(method.getModifiers()))
                continue;
			
			if(method.getParameterTypes().length==1
			&& method.isAnnotationPresent(annotation)) {
				Class<?> parameterType = method.getParameterTypes()[0];
				if(IEvent.class.isAssignableFrom(parameterType)) {
					@SuppressWarnings("unchecked")
					Class<? extends IEvent> listenerType = (Class<? extends IEvent>) parameterType;
					Listener listener = new Listener(o, method);
					if(this.listeners.containsKey(listenerType)) {
						if(!this.listeners.get(listenerType).contains(listener)) {
							this.listeners.get(listenerType).add(listener);
						}
					}else{
						this.listeners.put(listenerType, new CopyOnWriteArrayList<>(Collections.singletonList(listener)));
					}
				}
			}
		}
	}
	
	public void post(IEvent event) {
		CopyOnWriteArrayList<Listener> events = this.listeners.get(event.getClass());
		if(events==null) return;
		
		for(Listener listener : events) {
			try {
				if(listener.filter == null || Arrays.equals(listener.filter, ((IGenericEvent<?>)listener).getGenericType())) {
					listener.method.invoke(listener.object, event);
				}
			}catch(Exception e2) {
				e2.printStackTrace();
			}
		}
	}
	
	protected Class<? extends Annotation> getSubscribeAnnotation() {
		return SubscribeEvent.class;
	}
	
	public void unregister(Object o) {
		this.listeners.values().forEach(list -> list.removeIf(e -> e.object.equals(o)));
		this.listeners.entrySet().removeIf(e -> e.getValue().isEmpty());
	}
	
	static class Listener {
		final Object object;
		final Method method;
		final Type[] filter;
		public Listener(Object object, Method method) {
			this.object = object;
			this.method = method;
			this.method.setAccessible(true);
			Type[] filter = null;
			if(IGenericEvent.class.isAssignableFrom(method.getParameterTypes()[0])) {
				Type type = method.getGenericParameterTypes()[0];
	            if (type instanceof ParameterizedType) {
	                filter = ((ParameterizedType)type).getActualTypeArguments();
	            }
			}
			this.filter = filter;
		}
	}
}
