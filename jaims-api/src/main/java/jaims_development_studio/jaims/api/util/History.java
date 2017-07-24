package jaims_development_studio.jaims.api.util;

import java.util.AbstractCollection;
import java.util.Iterator;
import java.util.LinkedList;

public class History<E> extends AbstractCollection<E> {
	
	private final int			maxCapacity;
	private final LinkedList<E>	list;
	private E					save;
	private int					position;
	
	public History(int maxCapacity) {
		this.maxCapacity = maxCapacity;
		list = new LinkedList<>();
	}
	
	public int getMaxCapacity() {
		return maxCapacity;
	}
	
	public E up(E potentialSaveElement) {
		if (position++ == 0)
			save = potentialSaveElement;
		
		if (position > size()) {
			position = size();
			return null;
		}

		return list.get(size() - position); //since size is 1 more than the elements index and position is already incremented -1 is not needed
	}
	
	public E down() {
		if (position-- == 0) {
			position = 0;
			return save;
		}

		if (position == 0)
			return save;

		return list.get(size() - position);
	}
	
	@Override
	public boolean add(E e) {
		boolean result = list.add(e);
		
		if ((maxCapacity >= 0) && (size() > maxCapacity))
			list.removeFirst();
		
		position = 0;
		
		return result;
	}

	@Override
	public Iterator<E> iterator() {
		return list.iterator();
	}
	
	@Override
	public int size() {
		return list.size();
	}

}
