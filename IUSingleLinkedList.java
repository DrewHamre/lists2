import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.NoSuchElementException;

/**
 * Single-linked node implementation of IndexedUnsortedList.
 * An Iterator with working remove() method is implemented, but
 * ListIterator is unsupported.
 * 
 * @author Drew Hamre
 * 
 * @param <T> type to store
 */
public class IUSingleLinkedList<T> implements IndexedUnsortedList<T> {
	private Node<T> head, tail;
	private int size;
	private int modCount;
	
	/** Creates an empty list */
	public IUSingleLinkedList() {
		head = tail = null;
		size = 0;
		modCount = 0;
	}

	@Override
	public void addToFront(T element) {
		Node<T> newNode = new Node<T>(element);
        newNode.setNext(head);
        if (isEmpty()) {
            tail = newNode;
        }
        head = newNode;
		size++;
        modCount++;
	}

	@Override
	public void addToRear(T element) {
		Node<T> newNode = new Node<T>(element);
        if (head == null) {
            tail = null;
        } else {
            tail.setNext(newNode);
            tail = newNode;
        }
        size++;
        modCount++;
	}

	@Override
	public void add(T element) {
		addToRear(element);
	}

	@Override
	public void addAfter(T target, T element) {
		Node<T> targetNode = head;
		while (targetNode != null && !targetNode.getElement().equals(target)) {
			targetNode = targetNode.getNext();
		}
		if (targetNode == null) {
			throw new NoSuchElementException();
		}
		Node<T> newNode = new Node<T>(element);
		newNode.setNext(targetNode.getNext());
		targetNode.setNext(newNode);
		if (targetNode == tail) {
			tail = newNode;
		}
		size++;
	}

	@Override
	public void add(int index, T element) {
		// TODO 
		
	}

	@Override
	public T removeFirst() {
		if (isEmpty()) {
            throw new NoSuchElementException();
        }
        T retVal = head.getElement();
        head = head.getNext();
        size--;
        modCount++;
        return retVal;
	}

	@Override
	public T removeLast() {
		if (isEmpty()) {
            throw new NoSuchElementException();
        }
        T retVal = tail.getElement();

        size--;
        modCount++;
        return retVal;
	}

	@Override
	public T remove(T element) {
		if (isEmpty()) {
			throw new NoSuchElementException();
		}
		
		boolean found = false;
		Node<T> previous = null;
		Node<T> current = head;
		
		while (current != null && !found) {
			if (element.equals(current.getElement())) {
				found = true;
			} else {
				previous = current;
				current = current.getNext();
			}
		}
		
		if (!found) {
			throw new NoSuchElementException();
		}
		
		if (size() == 1) { //only node
			head = tail = null;
		} else if (current == head) { //first node
			head = current.getNext();
		} else if (current == tail) { //last node
			tail = previous;
			tail.setNext(null);
		} else { //somewhere in the middle
			previous.setNext(current.getNext());
		}
		
		size--;
		modCount++;
		
		return current.getElement();
	}

	@Override
	public T remove(int index) {
		if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException();
        }
        T retVal;
        if (head == tail) {
            retVal = head.getElement();
            head = tail = null;
        } else if (index == 0) {
            retVal = head.getElement();
            head = head.getNext();
        } else {
            Node<T> currentNode = head;
            for (int i = 0; i < index - 1; i++) {
                currentNode = currentNode.getNext();
            }
            retVal = currentNode.getNext().getElement();
            if (currentNode.getNext() == tail) {
                tail = currentNode;
            }
            currentNode.setNext(currentNode.getNext().getNext());
        }
		size--;
        modCount++;
        return retVal;
	}

	@Override
	public void set(int index, T element) {
		if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException();
        }
		
	}

	@Override
	public T get(int index) {
		// TODO 
		return null;
	}

	@Override
	public int indexOf(T element) {
		// TODO 
		return 0;
	}

	@Override
	public T first() {
        T retVal = head.getElement();
		if (isEmpty()) {
            throw new NoSuchElementException();
        }
        return retVal;
	}

	@Override
	public T last() {
        T retVal = tail.getElement();
		if (isEmpty()) {
            throw new NoSuchElementException();
        }
        return retVal;
	}

	@Override
	public boolean contains(T target) {
		// TODO 
		return false;
	}

	@Override
	public boolean isEmpty() {
		return (head == null);
	}

	@Override
	public int size() {
        return size;
	}

	@Override
	public Iterator<T> iterator() {
		return new SLLIterator();
	}

	@Override
	public ListIterator<T> listIterator() {
		throw new UnsupportedOperationException();
	}

	@Override
	public ListIterator<T> listIterator(int startingIndex) {
		throw new UnsupportedOperationException();
	}

	/** Iterator for IUSingleLinkedList */
	private class SLLIterator implements Iterator<T> {
		private Node<T> nextNode;
		private int iterModCount;
        private boolean canRemove;
		
		/** Creates a new iterator for the list */
		public SLLIterator() {
			nextNode = head;
			iterModCount = modCount;
            canRemove = false;
		}

		@Override
		public boolean hasNext() {
            if (iterModCount != modCount) {
                throw new ConcurrentModificationException();
            }
            return nextNode != null;
		}

		@Override
		public T next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            T retVal = nextNode.getElement();
            nextNode = nextNode.getNext();
            canRemove = true;
            return retVal;
		}
		
		@Override
		public void remove() {
			canRemove = false;
            if (iterModCount != modCount) {
                throw new ConcurrentModificationException();
            }
            if (!canRemove) {
                throw new IllegalStateException();
            }
            if (head.getNext() == nextNode) {
                head = nextNode;
                if (head == null) {
                    tail = null;
                }
            } else {
                Node<T> prevPrevNode = head;
                while (prevPrevNode.getNext().getNext() != nextNode) {
                    prevPrevNode = prevPrevNode.getNext();
                }
                prevPrevNode.setNext(nextNode);
                if (nextNode == null) {
                    tail = prevPrevNode;
                }
            }
            size--;
            modCount++;
            iterModCount++;
		}
	}
}