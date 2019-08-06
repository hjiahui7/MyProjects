import java.util.NoSuchElementException;

/**
 * create a Iterator for Double Link List
 *
 * @author jiahui huang<hjiahui7>
 * @author Yusheng Cao<cyschris>
 * @version 2018.9.4
 *
 * @param <E>
 *            the data
 */
public interface Iterator<E> {
    /**
     * check the next
     * 
     * @return true or false
     */
    public boolean hasNext();


    /**
     * Gets the next value in the list
     *
     * @return the next value
     * @throws NoSuchElementException
     *             if there are no nodes left in the list
     */
    public E next();


    /**
     * check the data have a previous node or not
     * 
     * @return the result
     */
    public boolean hasPre();


    /**
     * get the previous node
     * 
     * @return the previous item
     */
    public E previous();


    /**
     * Removes the last object returned with next() from the list
     *
     * @throws IllegalStateException
     *             if next has not been called yet and if the
     *             element has already been removed
     */
    public void remove();


    /**
     * get current data sorted
     * 
     * @return the data currently stored
     */
    public E getCurrent();


    /**
     * change the data store in here
     * 
     * @param current
     *            changed data
     */
    public void setCurrent(E current);


    /**
     * move the current pointer back to head
     */
    public void reset();


    /**
     * return the data which store in the last node
     * 
     * @return the data
     */
    public E getLast();


    /**
     * move the pointer to the specific location
     * 
     * @param index
     *            the index which locate
     */
    public void getIndex(int index);
}
