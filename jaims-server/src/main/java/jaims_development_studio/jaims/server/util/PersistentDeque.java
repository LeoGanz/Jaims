package jaims_development_studio.jaims.server.util;

import java.util.Collection;
import java.util.Iterator;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.TimeUnit;

import org.hibernate.collection.internal.PersistentBag;

@SuppressWarnings("rawtypes")
public class PersistentDeque extends PersistentBag implements BlockingDeque {

	private static final long serialVersionUID = 1L;
	
	@Override
	public int remainingCapacity() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int drainTo(Collection c) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int drainTo(Collection c, int maxElements) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Object removeFirst() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object removeLast() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object pollFirst() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object pollLast() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object getFirst() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object getLast() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object peekFirst() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object peekLast() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object pop() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Iterator descendingIterator() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void addFirst(Object e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addLast(Object e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean offerFirst(Object e) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean offerLast(Object e) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void putFirst(Object e) throws InterruptedException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void putLast(Object e) throws InterruptedException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean offerFirst(Object e, long timeout, TimeUnit unit) throws InterruptedException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean offerLast(Object e, long timeout, TimeUnit unit) throws InterruptedException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Object takeFirst() throws InterruptedException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object takeLast() throws InterruptedException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object pollFirst(long timeout, TimeUnit unit) throws InterruptedException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object pollLast(long timeout, TimeUnit unit) throws InterruptedException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean removeFirstOccurrence(Object o) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean removeLastOccurrence(Object o) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean offer(Object e) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void put(Object e) throws InterruptedException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean offer(Object e, long timeout, TimeUnit unit) throws InterruptedException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Object remove() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object poll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object take() throws InterruptedException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object poll(long timeout, TimeUnit unit) throws InterruptedException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object element() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object peek() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void push(Object e) {
		// TODO Auto-generated method stub
		
	}

}
