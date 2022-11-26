package cat.jiu.core.util;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.stream.Stream;

public class UnmodifiableCollection<E> implements Iterable<E> {
	private final Collection<E> c;
	
	public UnmodifiableCollection(Collection<E> co) {
		this.c = co;
	}
	
	public int size() {return this.c.size();}
	public boolean isEmpty() {return this.c.isEmpty();}
	public boolean contains(Object o) {return this.c.contains(o);}
	public Iterator<E> iterator() {return this.c.iterator();}
	public Object[] toArray() {return this.c.toArray();}
	public <T> T[] toArray(T[] a) {return this.c.toArray(a);}
	public boolean containsAll(Collection<?> c) {return this.c.containsAll(c);}
	public E get(int index) {
		if(index >= this.size()) throw new IndexOutOfBoundsException("Index: "+index+", Size: "+this.size());
		if(this.c instanceof List) return ((List<E>) this.c).get(index);
		
		int i = 0;
		for(E e : this.c) {
			if(i == index) return e;
			i++;
		}
		return null;
	}
	
	public void forEach(Consumer<? super E> action) {this.c.forEach(action);}
    public Spliterator<E> spliterator() {return this.c.spliterator();}
    
    public Stream<E> stream() {return this.c.stream();}
    public Stream<E> parallelStream() {return this.c.parallelStream();}
}
