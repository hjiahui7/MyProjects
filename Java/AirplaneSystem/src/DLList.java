
import java.util.NoSuchElementException;

/**
 * This provides implementation for some of the LList methods.
 *
 * @author jiahui huang<hjiahui7>
 * @author Yusheng Cao<cyschris>
 * @version 2018.9.4
 * @param <E>
 *            The type of object the class will store
 */
public class DLList<E> implements Iterable<E> {
    private static class Node<E> {
        private Node<E> next;
        private Node<E> previous;
        private E data;


        /**
         * Creates a new node with the given data
         *
         * @param d
         *            the data to put inside the node
         */
        public Node(E d) {
            data = d;
        }


        /**
         * State
         * 
         * @param d
         *            new data
         *
         */
        public void setData(E d) {
            data = d;
        }


        /**
         * Sets the node after this node
         *
         * @param n
         *            the node after this one
         */
        public void setNext(Node<E> n) {
            next = n;
        }


        /**
         * Sets the node before this one
         *
         * @param n
         *            the node before this one
         */
        public void setPrevious(Node<E> n) {
            previous = n;
        }


        /**
         * Gets the next node
         *
         * @return the next node
         */
        public Node<E> next() {
            return next;
        }


        /**
         * Gets the node before this one
         *
         * @return the node before this one
         */
        public Node<E> previous() {
            return previous;
        }


        /**
         * Gets the data in the node
         *
         * @return the data in the node
         */
        public E getData() {
            return data;
        }
    }

    private int size;
    private Node<E> head;
    private Node<E> tail;


    /**
     * Create a new DLList object.
     */
    public DLList() {
        init();
    }


    /**
     * Initializes the object to have the head and tail nodes
     */
    private void init() {
        head = new Node<E>(null);
        tail = new Node<E>(null);
        head.setNext(tail);
        tail.setPrevious(head);
        size = 0;
    }


    /**
     * Checks if the array is empty
     *
     * @return true if the array is empty
     */
    public boolean isEmpty() {
        return size == 0;
    }


    /**
     * Gets the number of elements in the list
     *
     * @return the number of elements
     */
    public int size() {
        return size;
    }


    /**
     * Removes all of the elements from the list
     */
    public void clear() {
        init();
    }


    /**
     * Checks if the list contains the given object
     *
     * @param obj
     *            the object to check for
     * @return true if it contains the object
     */
    public boolean contains(E obj) {
        return lastIndexOf(obj) != -1;
    }


    /**
     * Gets the object at the given position
     *
     * @param index
     *            where the object is located
     * @return The object at the given position
     * @throws IndexOutOfBoundsException
     *             if there no node at the given index
     */
    public E get(int index) {
        return getNodeAtIndex(index).getData();
    }


    /**
     * Adds a element to the end of the list
     *
     * @param newEntry
     *            the element to add to the end
     */
    public void add(E newEntry) {
        add(size(), newEntry);
    }


    /**
     * Adds the object to the position in the list
     *
     * @param index
     *            where to add the object
     * @param obj
     *            the object to add
     * @throws IndexOutOfBoundsException
     *             if index is less than zero or greater than
     *             size
     * @throws IllegalArgumentException
     *             if obj is null
     */
    public void add(int index, E obj) {
        Node<E> nodeAfter;
        if (index == size) {
            nodeAfter = tail;
        }
        else {
            nodeAfter = getNodeAtIndex(index);
        }
        Node<E> addition = new Node<E>(obj);
        addition.setPrevious(nodeAfter.previous());
        addition.setNext(nodeAfter);
        nodeAfter.previous().setNext(addition);
        nodeAfter.setPrevious(addition);
        size++;
    }


    /**
     * gets the node at that index
     *
     * @param index
     * @return node at index
     */
    private Node<E> getNodeAtIndex(int index) {
        Node<E> current = head.next();
        for (int i = 0; i < index; i++) {
            current = current.next();
        }
        return current;
    }


    /**
     * Gets the last time the given object
     * is in the list
     *
     * @param obj
     *            the object to look for
     * @return the last position of it. -1
     *         If it is not in the list
     */
    public int lastIndexOf(E obj) {
        Node<E> current = tail.previous();
        for (int i = size() - 1; i >= 0; i--) {
            if (current.getData().equals(obj)) {
                return i;
            }
            current = current.previous();
        }
        return -1;
    }


    /**
     * Removes the first object in the list that .equals(obj)
     *
     * @param obj
     *            the object to remove
     * @return true if the object was found and removed
     */

    public E remove(E obj) {
        Node<E> current = head.next();
        while (!current.equals(tail)) {
            if (current.getData().equals(obj)) {
                current.previous().setNext(current.next());
                current.next().setPrevious(current.previous());
                size--;
                return current.getData();
            }
            current = current.next();
        }
        return null;
    }


    // ----------------------------------------------------------
    /**
     * remove the last node added
     *
     * @return the data
     */
    public E removeLast() {
        Node<E> nodeToBeRemoved = tail.previous();
        nodeToBeRemoved.previous().setNext(nodeToBeRemoved.next());
        nodeToBeRemoved.next().setPrevious(nodeToBeRemoved.previous());
        size--;
        return nodeToBeRemoved.getData();
    }


    /**
     * the Iterator
     *
     * @return Iterator
     */
    public Iterator<E> iterator() {
        return new DLListIterator<E>();
    }


    private class DLListIterator<A> implements Iterator<E> {
        private Node<E> current;


        /**
         * Creates a new DLListIterator
         */
        public DLListIterator() {
            current = head;
        }


        /**
         * Checks if there are more elements in the list
         *
         * @return true if there are more elements in the list
         */
        @Override
        public boolean hasNext() {
            return !current.next().equals(tail);
        }


        /**
         * Gets the next value in the list
         *
         * @return the next value
         * @throws NoSuchElementException
         *             if there are no nodes left in the list
         */
        @Override
        public E next() {
            E item = current.next().getData();
            current = current.next();
            return item;
        }


        /**
         * Removes the last object returned with next() from the list
         *
         * @throws IllegalStateException
         *             if next has not been called yet and if the
         *             element has already been removed
         */
        @Override
        public void remove() {
            current.previous().setNext(current.next());
            current.next().setPrevious(current.previous());
            size--;
        }


        /**
         * get current data sorted
         *
         * @return the data currently stored
         */
        public E getCurrent() {
            return current.getData();
        }


        /**
         * change the data store in here
         *
         * @param current
         *            changed data
         */
        public void setCurrent(E current) {
            this.current.setData(current);
        }


        /**
         * check the data have a previous node or not
         *
         * @return the result
         */
        @Override
        public boolean hasPre() {
            return !current.previous().equals(head);
        }


        /**
         * get the previous node
         *
         * @return the previous item
         */
        @Override
        public E previous() {
            E item = current.previous().getData();
            current = current.previous();
            return item;
        }


        /**
         * move the current pointer back to head
         */
        public void reset() {
            current = head;
        }


        /**
         * return the data which store in the last node
         *
         * @return the data
         */
        @Override
        public E getLast() {
            return tail.previous().getData();
        }


        /**
         * move the pointer to the specific location
         *
         * @param index
         *            the index which locate
         */
        @Override
        public void getIndex(int index) {
            reset();
            for (int i = 0; i <= index; i++) {
                if (hasNext()) {
                    next();
                }
            }
        }
    }


    // ----------------------------------------------------------
    /**
     * get the first data
     *
     * @return data in the first node
     */
    public E getFirst() {
        return head.next().getData();
    }
}
