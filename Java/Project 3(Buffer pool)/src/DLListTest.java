
// On my honor:
//
// - I have not used source code obtained from another student,
// or any other unauthorized source, either modified or
// unmodified.
//
// - All source code and documentation used in my program is
// either my original work, or was derived by me from the
// source code published in the textbook for this course.
//
// - I have not discussed coding details about this project with
// anyone other than my partner (in the case of a joint
// submission), instructor, ACM/UPE tutors or the TAs assigned
// to this course. I understand that I may discuss the concepts
// of this program with other students, and that another student
// may help me debug my program so long as neither of us writes
// anything during the discussion or modifies any computer file
// during the discussion. I have violated neither the spirit nor
// letter of this restriction.
import student.TestCase;

/**
 * test the DLList class
 *
 * @author jiahui huang<hjiahui7>
 * @author Yusheng Cao<cyschris>
 * @version 2018.9.4
 */
public class DLListTest extends TestCase {
    /**
     * the list we will use
     */
    private DLList<String> list;
    private Iterator<String> iter;


    /**
     * run before every test case
     */
    @Override
    public void setUp() {
        list = new DLList<String>();
        iter = list.iterator();
    }


    /**
     * Tests the add method. Ensures that it adds the object is added at the end
     * and
     * the size is increased
     */
    public void testAdd() {
        assertEquals(0, list.size());
        list.add("A");
        assertEquals(1, list.size());
        list.add("B");
        assertEquals(2, list.size());
        assertEquals("B", list.get(1));

    }


    /**
     * Tests that objects can be added at the beginning and end and that they
     * are
     * placed correctly
     */
    public void testAddIndex() {
        list.add("B");
        list.add(0, "A");
        assertEquals("A", list.get(0));
        assertEquals(2, list.size());
        list.add(2, "D");
        assertEquals("D", list.get(2));
        list.add(2, "C");
        assertEquals("C", list.get(2));
    }


    /**
     * Tests removing a object changes the size appropiately and that you can
     * remove
     * the first and last elements
     */
    public void testRemoveObj() {
        list.remove(null);
        list.add("A");
        list.add("B");
        list.remove("A");
        assertEquals("B", list.get(0));
        assertEquals(1, list.size());
        list.add("C");
        list.remove("C");
        assertEquals("B", list.get(0));
    }


    /**
     * Test contains when it does and does not contain the object
     */
    public void testContains() {
        assertFalse(list.contains("A"));
        list.add("A");
        assertTrue(list.contains("A"));
        assertFalse(list.contains("B"));
        list.add("B");
        assertTrue(list.contains("B"));
    }


    /**
     * Test lastIndexOf when the list is empty, when the object is not in the
     * list,
     * and when it is at the beginning or end
     */
    public void testLastIndexOf() {
        assertEquals(-1, list.lastIndexOf("A"));
        list.add("A");
        assertEquals(0, list.lastIndexOf("A"));
        list.add("A");
        assertEquals(1, list.lastIndexOf("A"));
        list.add("B");
        assertEquals(1, list.lastIndexOf("A"));
        assertEquals(2, list.lastIndexOf("B"));
        list.add("A");
        assertEquals(3, list.lastIndexOf("A"));
    }


    /**
     * Tests isEmpty when empty and full
     */
    public void testIsEmpty() {
        assertTrue(list.isEmpty());
        list.add("A");
        assertFalse(list.isEmpty());
    }


    /**
     * Ensures that all of the objects are cleared and the size is changed
     */
    public void testClear() {
        list.add("A");
        list.clear();
        assertEquals(0, list.size());
        assertFalse(list.contains("A"));
    }


    /**
     * remove last
     */
    public void testRemoveLast() {
        list.add("A");
        list.add("A");
        list.add("A");
        list.add("A");
        list.add("A");
        assertEquals(list.getFirst(), "A");
        assertEquals(list.removeLast(), "A");
    }


    /**
     * test the literator class is functional enougj
     */
    public void testIterator() {
        list.add("A");
        assertTrue(iter.hasNext());
        iter.next();
        assertFalse(iter.hasNext());
        assertEquals(iter.getCurrent(), "A");
        assertFalse(iter.hasPre());
        assertFalse(iter.hasNext());
        list.add("B");
        iter.next();
        assertTrue(iter.hasPre());
        assertEquals(iter.getLast(), "B");
        iter.getIndex(2);
        assertEquals(iter.getCurrent(), "B");
        iter.remove();
        iter.setCurrent("C");
        assertEquals(iter.getCurrent(), "C");
        assertEquals(iter.previous(), "A");
    }

}
