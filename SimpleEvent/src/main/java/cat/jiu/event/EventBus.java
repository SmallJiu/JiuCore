package cat.jiu.event;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class EventBus {
	private final ConcurrentHashMap<Class<? extends IEvent>, ListenerList> listeners = new ConcurrentHashMap<>();
	public final String name;
	protected ExceptionInvoker exceptionInvoker;
	public EventBus(String name) {
		this.name = name;
	}

	public void invokeException(IEvent event, Listener listener, Exception exception) {
		if (this.exceptionInvoker!=null) {
			this.exceptionInvoker.invoke(event, listener, exception);
		}else {
			System.err.println(String.format("[Error] Post event has error, event: %s, method: %s", event.getClass().getSimpleName(), listener.getListenerName()));
			exception.printStackTrace();
		}
	}
	public interface ExceptionInvoker {
		void invoke(IEvent event, Listener listener, Exception exception);
	}
	
	public void register(Object target) {
		for (ListenerList list : this.listeners.values()) {
			if (list.registered(target)) {
				return;
			}
		}
		if (target.getClass() == Class.class) {
			registerClass((Class<?>) target);
		} else {
			registerObject(target);
		}
	}

	private void registerClass(Class<?> clazz) {
		Arrays.stream(clazz.getDeclaredMethods()).
				filter(m->Modifier.isStatic(m.getModifiers())).
				filter(m->m.isAnnotationPresent(SubscribeEvent.class)).
				forEach(m->registerListener(clazz, getEventType(m, clazz), m));
	}
	private void registerObject(Object obj) {
		HashSet<Class<?>> classes = new HashSet<>();
		typesFor(obj.getClass(), classes);
		Arrays.stream(obj.getClass().getDeclaredMethods()).
				filter(m->!Modifier.isStatic(m.getModifiers())).
				forEach(m -> classes.stream().
						map(c -> this.getDeclMethod(c, m)).
						filter(rm -> rm.isPresent() && rm.get().isAnnotationPresent(SubscribeEvent.class)).
						findFirst().
						ifPresent(rm->registerListener(obj, getEventType(m, obj), rm.get())));
	}

	private Optional<Method> getDeclMethod(Class<?> clz, Method in) {
		try {
			return Optional.of(clz.getDeclaredMethod(in.getName(), in.getParameterTypes()));
		} catch (NoSuchMethodException nse) {
			return Optional.empty();
		}
	}

	private void typesFor(Class<?> clz, Set<Class<?>> visited) {
		if (clz.getSuperclass() == null) return;
		typesFor(clz.getSuperclass(),visited);
		Arrays.stream(clz.getInterfaces()).forEach(i->typesFor(i, visited));
		visited.add(clz);
	}

	private void registerListener(Object target, Class<? extends IEvent> eventType, Method real) {
		EventPriority priority = real.getAnnotation(SubscribeEvent.class).priority();
		Listener listener = new Listener(target, real);

		if(this.listeners.containsKey(eventType)) {
			this.listeners.get(eventType).register(priority, listener);
		}else{
			this.listeners.put(eventType, new ListenerList(this, priority, listener));
		}
	}

	@SuppressWarnings("unchecked")
	private static Class<? extends IEvent> getEventType(Method method, Object target) {
		String targetName;
		if (target == Class.class) {
			targetName = target.toString();
		}else {
			targetName = target.getClass().toString();
		}
		Class<?>[] parameterTypes = method.getParameterTypes();
		if (parameterTypes.length != 1) {
			throw new IllegalArgumentException(
					"Method " + targetName + ":" + method + " has @SubscribeEvent annotation. " +
							"It has " + parameterTypes.length + " arguments, " +
							"but event handler methods require a single argument only."
			);
		}

		Class<?> eventType = parameterTypes[0];
		if (!IEvent.class.isAssignableFrom(eventType)) {
			throw new IllegalArgumentException(
					"Method " + targetName + ":" + method + " has @SubscribeEvent annotation, " +
							"but takes an argument that is not an Event subtype : " + eventType);
		}
		return (Class<? extends IEvent>) eventType;
	}

	public void post(IEvent event) {
		ListenerList listeners = this.listeners.get(event.getClass());
		if(listeners!=null) {
			listeners.post(event);
		}
	}
	
	public void unregister(Object o) {
		this.listeners.values().forEach(l -> l.unregister(o));
		this.listeners.entrySet().removeIf(e -> e.getValue().listeners.isEmpty());
	}
	
	public static class Listener {
		protected final Object object;
		protected final Method method;
		protected final Type[] filter;
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
		public String getListenerName() {
			String targetName;
			if (this.object == Class.class) {
				targetName = this.object.toString();
			}else {
				targetName = this.object.getClass().toString();
			}

			if (this.filter != null) {
				StringJoiner typeJoiner = new StringJoiner(", ", "<", ">");
				for (Type type : this.filter) {
					typeJoiner.add(type.getTypeName());
				}
				return String.format("%s:%s%s", targetName, this.method.getName(), typeJoiner);
			}else {
				return String.format("%s:%s", targetName, this.method.getName());
			}
		}
	}

	static class ListenerList {
		protected final EventBus bus;
		public final CopyOnWriteArrayList<CopyOnWriteArrayList<Listener>> listeners;
		public ListenerList(EventBus bus, EventPriority priority, Listener listener) {
			this.bus = bus;
			this.listeners = new CopyOnWriteArrayList<>();
			for (int i = 0; i < EventPriority.values().length; i++) {
				this.listeners.add(new CopyOnWriteArrayList<>());
			}
			this.register(priority, listener);
		}

		public void register(EventPriority priority, Listener listener) {
			this.listeners.get(priority.ordinal()).add(listener);
		}
		public void unregister(Object o) {
			this.listeners.forEach(list -> list.removeIf(e -> e.object.equals(o)));
		}
		public boolean registered(Object o) {
			for (CopyOnWriteArrayList<Listener> list : this.listeners) {
				for (Listener listener : list) {
					if (listener.object.equals(o)) {
						return true;
					}
				}
			}
			return false;
		}

		public void post(IEvent event) {
			for (CopyOnWriteArrayList<Listener> list : this.listeners) {
				for(Listener listener : list) {
					try {
						if(listener.filter == null || Arrays.equals(listener.filter, ((IGenericEvent<?>)listener).getGenericType())) {
							listener.method.invoke(listener.object, event);
						}
					}catch(Exception e) {
						this.bus.invokeException(event, listener, e);
					}
				}
			}
		}
	}
}
