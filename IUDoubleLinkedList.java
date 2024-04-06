import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.NoSuchElementException;

/**
 * Double-linked list implementation of IndexedUnsortedList 
 * supporting a LitIterator as well as a basic Iterator.
 * @author Drew Hamre
 */
public class IUDoubleLinkedList<T> implements IndexedUnsortedList<T> {
    private Node<T> head; 
    private Node<T> tail;
    private int size;
    private int modCount;

    /**
     * Initialize a double-linked list
     */
    public IUDoubleLinkedList() {
        head = tail = null;
        size = 0;
        modCount = 0;
    }

    @Override
    public void addToFront(T element) {
        Node<T> newNode = new Node<T>(element);
        newNode.setNext(head);
        // We need to know if we're adding to an empty list till we get to the next point.
        // So...
        if (isEmpty()) {
            tail = newNode;
        } else {
            head.setPrevious(newNode);
        }
        head = newNode;
        size++;
        modCount++;
    }

    @Override
    public void addToRear(T element) {
        Node<T> newNode = new Node<T>(element);
        newNode.setPrevious(tail);
        if (isEmpty()) {
            head = newNode;
        } else {
            tail.setNext(newNode);
            newNode.setPrevious(tail);
        }
        tail = newNode;
        size++;
        modCount++;
    }

    @Override
    public void add(T element) {
        addToRear(element);
    }

    @Override
    public void addAfter(T element, T target) {
		Node<T> targetNode = head;
		while (targetNode != null && !targetNode.getElement().equals(target)) {
			targetNode = targetNode.getNext();
		}
		if (targetNode == null) {
			throw new NoSuchElementException();
		}
		Node<T> newNode = new Node<T>(element);
         if(targetNode.getNext() != null) targetNode.getNext().setPrevious(newNode);
		newNode.setNext(targetNode.getNext());
		targetNode.setNext(newNode);
        if(head != null) {
                newNode.setPrevious(targetNode);
        } else {
                head = newNode;
        }
		if (targetNode == tail) {
			tail = newNode;
		}
		size++;
	}

    @Override 
    public void add(int index, T element) {
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException();
        }
        Node<T> newNode = new Node<T>(element);
        Node<T> prevNode = head;
        int currentIndex = 0;
        while (currentIndex < index && prevNode.getNext() != null) {
            prevNode = prevNode.getNext();
            currentIndex++;
        }
        if (isEmpty()) { // no elements
            head = tail = newNode;
        } else if (index == 0) { 
            newNode.setNext(head);
            head.setPrevious(newNode);
            head = newNode;
        } else {
            if (prevNode.getNext() != null) {
                prevNode.getNext().setPrevious(newNode);
            }       
            newNode.setNext(prevNode.getNext());
            newNode.setPrevious(prevNode);
            prevNode.setNext(newNode);
            if (prevNode == tail) {
                tail = newNode;
            }
        }
        size++;
        modCount++;
    }

    @Override
    public T removeFirst() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        } 
        // USING THE ITERATOR!
        ListIterator<T> lit = listIterator();
        T retVal = lit.next();
        lit.remove();
        return retVal;

        // OLD METHOD
        // T retVal = head.getElement();
        // head = head.getNext();
        // if(head == null) {
        //     tail = null;
        // } else {
        //     head.setPrevious(null);
        // }
        // size--;
        // modCount++;
        // return retVal;
    }

    @Override
    public T removeLast() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        ListIterator<T> lit = listIterator();
        T retVal = null;
        while (lit.hasNext()) {
            retVal = lit.next();
        }
        lit.remove();
        return retVal;
    }

    @Override
    public T remove(T element) {
        // FINALLY IMPLEMENTING LISTITERATOR TO DO METHODS MORE EFFICIENTLY!
        ListIterator<T> lit = listIterator();
        T retVal = null;
        boolean foundIt = false;
        while (lit.hasNext() && !foundIt) {
            retVal = lit.next();
            if (lit.equals(element)) {
                foundIt = true;
                lit.remove();
            }
        }
        if (!foundIt) {
            throw new NoSuchElementException();
        }
        return retVal;
    }

    @Override
    public T remove(int index) {
        // special case 0: empty list
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException();
        }
        T retVal = null;
        // special case 1: single element list
        if (head == tail) {
            retVal = head.getElement();
            head = tail = null;
        // special case 2: removing head
        } else if (index == 0) {       
            retVal = head.getElement();
            head = head.getNext();
            head.setPrevious(null);
        } else { 
            Node<T> currentNode = head;
            for (int i = 0; i < index; i++) {
                currentNode = currentNode.getNext();
            }
            retVal = currentNode.getElement();
            if (currentNode == tail) {
                tail = currentNode.getPrevious();
            } else {
                currentNode.getNext().setPrevious(currentNode.getPrevious());
            }
            currentNode.getPrevious().setNext(currentNode.getNext());
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
        Node<T> targetNode = head;
        for (int i = 0; i < index; i++) {
            targetNode = targetNode.getNext();
        } 
        targetNode.setElement(element);
        modCount++;
    }

    @Override
    public T get(int index) { // USING LIST ITERATOR FOR EFFICIENCY 
        if (index == size) {
            throw new IndexOutOfBoundsException();
        }
        ListIterator<T> lit = listIterator(index);
        return lit.next();
    }

    @Override
    public int indexOf(T element) {
        int index = -1;
        int currentIndex = 0;
        Node<T> targetNode = head;
        while (index < 0 && targetNode != null) {
            if (targetNode.getElement().equals(element)) {
                index = currentIndex;
            } else {
                targetNode = targetNode.getNext();
                currentIndex++;
            }
        }
        return index;
    }

    @Override
    public T first() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        return head.getElement();
    }

    @Override
    public T last() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        return tail.getElement();
    }
    
    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return head == null;
    }

    @Override
    public boolean contains(T target) {
        return indexOf(target) > -1;
    }
    
    @Override
    public Iterator<T> iterator() {
        return new DLLIterator(0);
    }

    @Override
    public ListIterator<T> listIterator() {
        return new DLLIterator(0);
    }

    @Override
    public ListIterator<T> listIterator(int startingIndex) {
        return new DLLIterator(startingIndex);
    }

    /**
     * Acts as both a basic Iterator and ListIterator for IUDLL
     */
    private class DLLIterator implements ListIterator<T> {
        private Node<T> nextNode;
        private int nextIndex;
        private int iterModCount;
        private Node<T> lastReturnedNode;

        /**
         * Initialize iterator before given starting index.
         * @param startingIndex
         */
        public DLLIterator(int startingIndex) {
            if (startingIndex < 0 || startingIndex > size) {
                throw new IndexOutOfBoundsException();
            }
            nextNode = head;
            for (int i = 0; i < startingIndex; i++) {
                nextNode = nextNode.getNext();
            }
            nextIndex = startingIndex;
            iterModCount = modCount;
            lastReturnedNode = null;
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
            lastReturnedNode = nextNode;
            nextNode = nextNode.getNext();
            nextIndex++;
            return retVal;
        }

        @Override
        public boolean hasPrevious() {
            if (iterModCount != modCount) {
                throw new ConcurrentModificationException();
            }
            return nextNode != head;
        }

        @Override
        public T previous() {
            if (!hasPrevious()) {
                throw new NoSuchElementException();
            }
            if (nextNode != null) {
                nextNode = nextNode.getPrevious();
            } else {
                nextNode = tail;
            }
            nextIndex--;
            lastReturnedNode = nextNode;
            return nextNode.getElement();
        }

        @Override
        public int nextIndex() {
            if (iterModCount != modCount) {
                throw new ConcurrentModificationException();
            }
            return nextIndex;
        }

        @Override
        public int previousIndex() {
            if (iterModCount != modCount) {
                throw new ConcurrentModificationException();
            }
            return nextIndex - 1;
        }

        @Override
        public void remove() { 
            if (iterModCount != modCount) {
                throw new ConcurrentModificationException();
            }
            if (lastReturnedNode == null) {
                throw new IllegalStateException();
            }
            if (lastReturnedNode == head) {
                head = lastReturnedNode.getNext();
            } else {
                lastReturnedNode.getPrevious().setNext(lastReturnedNode.getNext());
            }
            if (lastReturnedNode == tail) {
                tail = lastReturnedNode.getPrevious(); 
            } else {
                lastReturnedNode.getNext().setPrevious(lastReturnedNode.getPrevious());
            }
            if (lastReturnedNode != nextNode) { // This means last move was next, now removing node to the left
                nextIndex--;
            } else { // last move was previous, now removing node to the right... nextNode.
                nextNode = nextNode.getNext();
            }
            lastReturnedNode = null;
            size--;
            modCount++;
            iterModCount++;
        }

        @Override
        public void set(T e) {
            if (iterModCount != modCount) {
                throw new ConcurrentModificationException();
            }
            if (lastReturnedNode == null) {
                throw new IllegalStateException();
            }
            lastReturnedNode.setElement(e);
            modCount++;
            iterModCount++;
        }

        @Override
        public void add(T e) {
            if (iterModCount != modCount) {
                throw new ConcurrentModificationException();
            }
            Node<T> newNode = new Node<T>(e);
            if (size == 0) {
                head = tail = newNode;
            } else if (nextNode == head) {
                newNode.setNext(head);
                head.setPrevious(newNode);
                head = newNode; 
            } else if (nextNode == null) {
                newNode.setPrevious(tail);
                tail.setNext(newNode);
                tail = newNode;
            } else {
                newNode.setPrevious(nextNode.getPrevious());
                nextNode.getPrevious().setNext(newNode);
                nextNode.setPrevious(newNode);
            }
            size++;
            modCount++;
            iterModCount++;
            nextIndex++;
        }
    }
}