package jaims_development_studio.jaims.api.util;

import java.util.Collection;
import java.util.List;

import javax.swing.event.EventListenerList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.ForwardingList;

public class ObservableList<E> extends ForwardingList<E> {
	
	private static Logger			LOG	= LoggerFactory.getLogger(ObservableList.class);
	private final List<E>			delegate;
	private final EventListenerList	eventListenerList;
	
	public ObservableList(List<E> list) {
		delegate = list;
		eventListenerList = new EventListenerList();
	}
	
	@Override
	protected List<E> delegate() {
		return delegate;
	}
	
	public void addListChangeListener(ListChangeListener listener) {
		eventListenerList.add(ListChangeListener.class, listener);
	}
	
	public void removeListChangeListener(ListChangeListener listener) {
		eventListenerList.remove(ListChangeListener.class, listener);
	}
	
	private void fireListChangeEvent(ListChangeEvent event) {
		LOG.debug("Firing ListChangeEvent");
		Object[] listeners = eventListenerList.getListenerList();
		for (int i = 0; i < listeners.length; i++)
			if (listeners[i] == ListChangeListener.class)
				switch (event.getCode()) {
					case ListChangeEvent.ELEMENT_ADDED:
						((ListChangeListener) listeners[i + 1]).emlementAdded(event);
						break;
					case ListChangeEvent.ELEMENT_REMOVED:
						((ListChangeListener) listeners[i + 1]).elementRemoved(event);
						break;
					default:
						LOG.warn("Invalid Code in ListChangeEvent");
						break;
				}
	}
	
	@Override
	public boolean add(E element) {
		boolean result = super.add(element);
		fireListChangeEvent(new ListChangeEvent(element, ListChangeEvent.ELEMENT_ADDED));
		return result;
	}

	@Override
	public void add(int index, E element) {
		super.add(index, element);
		fireListChangeEvent(new ListChangeEvent(element, ListChangeEvent.ELEMENT_ADDED));
	}
	
	
	@Override
	public boolean addAll(Collection<? extends E> collection) {
		boolean result = true;
		for (E e : collection)
			if (!add(e))
				result = false;
		return result;
	}

	@Override
	public boolean addAll(int index, Collection<? extends E> elements) {
		for (E e : elements)
			add(index, e);
		return true;
	}

	@Override
	public boolean remove(Object object) {
		boolean result = super.remove(object);
		fireListChangeEvent(new ListChangeEvent(object, ListChangeEvent.ELEMENT_REMOVED));
		return result;
	}

	@Override
	public E remove(int index) {
		E result = super.remove(index);
		fireListChangeEvent(new ListChangeEvent(result, ListChangeEvent.ELEMENT_REMOVED));
		return result;
	}

	@Override
	public boolean removeAll(Collection<?> collection) {
		boolean result = true;
		for (Object o : collection)
			if (!remove(o))
				result = false;
		return result;
	}
	
}
