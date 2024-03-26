import java.util.Iterator;
import java.util.ListIterator;
import java.util.NoSuchElementException;
import org.w3c.dom.Node;

/**
 * Double-linked list implementation of INdexedUnsortedList 
 * supporting a LitIterator as well as a basic Iterator.
 * @author Drew Hamre
 */
public class IUDoubleLinkedList<T> implements IndexedUnsortedList<T> {
    private Node<T> head; 
    private Node<T> tail;
    private int size;
    private int modCount;

    /**
     * Initialize a double-linked list DONE
     */
    public IUDoubleLinkedList() {
        head = tail = null;
        size = 0;
        modCount = 0;
    }

    // DIFFERENT METHODS FROM SINGLE LINKED BELOW \/\/\/\/\/\/\/

    /**
     * DONE
     */
    @Override
    public void addToFront(T element) {
        Node<T> newNode = new Node<T>(element);
        newNode.setNextNode(head);
        // We need to know if we're adding to an empty list till we get to the next point.
        // So...
        if (isEmpty()) {
            tail = newNode;
        } else {
            head.setPreviousNode(newNode);
        }
        head = newNode;
        size++;
        modCount++;

    }

    @Override
    public void addToRear(T element) {

    }

    @Override
    public void add (T element) {

    }

    @Override
    public void addAfter(T element, T target) {

    }

    @Override
    public void add(int index, T element) {

    }

    @Override
    /**
     * DONE
     */
    public T removeFirst() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        } 
        T retVal = head.getElement();
        head = head.getNextNode();
        if(head == null) {
            tail = null;
        } else {
            head.setPreviousNode(null);
        }
        size--;
        modCount++;
        return retVal;
    }

    @Override
    public T removeLast() {

    }

    @Override
    public T remove(T element) {

    }

    @Override
    public T remove(int index) {
        // YOUR TURN ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        // special case 0: empty list
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException();
        }
        T retVal;

        // general case: middle of a 3+ element list
        // special case 1: head of a 2+ element list
        // special case 2: tail of a 2+ element list

        // special case 3: single element list
        if (head == tail) {
            retVal = head.getElement();
            head = tail = null;

        } else if (index == 0) {         // HEAD END (Cant be the tail)
            retVal = head.getElement();
            head = head.getNextNode;     // We know there is a next node because we determined it isn't a single list in special case 3.
            head.setPreviousNode(null);

        } else { // If not at the head end... \/\/\/\/

            Node<T> currentNode = head;
            for (int i = 0; i < index; i++) {
                currentNode = currrentNode.getNextNode();
            }
            retVal = currentNode.getElement();
            if (currentNode == tail) {
                tail = currentNode.getPreviousNode();
            } else {
                currentNode.getNextNode().setPreviousNode(currentNode.getPreviousNode());
            }
            currentNode.getPreviousNode().setNextNode(currentNode.getNextNode());
        }
        
        size--;
        modCount++;
        return retVal;
        }

    public void set(int index, T element) {

    }

    public T get(int index) {

    }

    public int indexOf(T element) {

    }

    public T first() {

    }

    public T last() {

    }
    
    // SAME AS SINGLE LINKED BELOW \/\/\/\/\/\/\/\/\/\/\/\/\/\/\/
    @Override
    /**
     * DONE
     */
    public int size() {
        return size;
    }

    @Override
    /**
     * DONE
     */
    public boolean isEmpty() {
        return head == null;
    }

    /**
     * DONE
     */
    public boolean contains(T target) {
        return indexOf(target) > -1;
    }

    // QUICK ADDED METHODS
    
    @Override
    public Iterator<T> iterator() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'iterator'");
    }

    @Override
    public ListIterator<T> listIterator() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'listIterator'");
    }

    @Override
    public ListIterator<T> listIterator(int startingIndex) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'listIterator'");
    }
}