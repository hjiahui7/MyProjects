import student.TestCase;

/**
 * test class for list class
 *
 * @author jiahui huang<hjiahui7>
 * @author Yusheng Cao<cyschris>
 * @version 2018.9.4
 */
public class OrthogonalListTest extends TestCase {

    private OrthogonalList<String> test;
    private Node<String> review;
    private Node<String> review1;
    private Node<String> movie;
    private Node<String> movie1;


    /**
     * set up the test
     */
    public void setUp() {
        test = new OrthogonalList<String>();
        review = new Node<String>("null reviewer", null, null, null, null, 0,
            0);
        movie = new Node<String>("review1", null, null, null, null, 0, 0);
    }


    /**
     * test insert
     */
    @SuppressWarnings("unchecked")
    public void testInsert() {
        assertTrue(test.isEmpty());
        assertEquals(test.getNumberOfXInsert(), 0);
        assertEquals(test.getNumberOfYInsert(), 0);
        test.insert(null, movie, "test1");
        assertFalse(test.isEmpty());
        Node<String>[] twoindex = test.insert(null, movie, "test2");
        test.insert(review, null, "test3");
        test.insert(review, null, "test4");
        test.insert(review, movie, "test5");
        review1 = twoindex[0];
        movie1 = twoindex[1];
        systemOut().clearHistory();
        assertEquals(1, review1.getxId());
        assertEquals(0, movie1.getyId());
        systemOut().clearHistory();
    }


    // ----------------------------------------------------------
    /**
     * test remove
     */
    @SuppressWarnings("unchecked")
    public void testRemove() {
        test.insert(review, movie, "test1");
        test.insert(review1, movie, "test2");
        test.insert(review, movie1, "test3");
        Node<String>[] twoindex = test.insert(review1, movie1, "test4");
        review1 = twoindex[0];
        movie1 = twoindex[1];
        assertEquals(2, test.getNumberOfX());
        assertEquals(1, review1.getxId());
        assertEquals(1, movie1.getyId());
        systemOut().clearHistory();
    }


    /**
     * test get Data
     */
    @SuppressWarnings("unchecked")
    public void testToString() {
        test.insert(null, movie, "test1");
        test.insert(null, movie, "test2");
        test.insert(review, null, "test3");
        test.insert(review, null, "test4");
        Node<String>[] twoindex = test.insert(review, movie, "test5");
        assertEquals(test.getData(review, movie), "null reviewer");

        review1 = twoindex[0];
        movie1 = twoindex[1];
        assertEquals(0, review1.getxId());
        assertEquals(0, movie1.getyId());
    }


    /**
     * test insert
     */
    @SuppressWarnings("unchecked")
    public void testInsert2() {
        assertFalse(test.removeCol(null));
        assertFalse(test.removeRow(null));
        Node<String>[] revi = test.insert(null, null, "1");
        Node<String> r1review = revi[0];
        Node<String> r2review = revi[1];
        Node<String>[] revi2 = test.insert(null, null, "1");
        Node<String> review2 = revi2[0];
        Node<String> movie2 = revi2[1];
        test.insert(review2, movie2, "1");
        test.removeCol(r2review);
        revi = test.insert(null, null, "1");
        r1review = revi[0];
        r2review = revi[1];
        revi2 = test.insert(null, null, "1");
        review2 = revi2[0];
        movie2 = revi2[1];
        test.removeRow(r1review);
        Node<String> review3 = new Node<String>("reviewer", null, null, null,
            null, 3, -1);
        Node<String> movie3 = new Node<String>("mov", null, null, null, null,
            -1, 7);
        test.insert(null, movie, "2");
        test.insert(review, null, "3");
        test.insert(review, null, "4");
        test.insert(review, movie, "5");
        test.insert(null, movie, "1");
        test.insert(null, r2review, "2");
        test.insert(r1review, null, "3");
        test.insert(review2, null, "4");
        test.insert(review3, movie3, "5");
        test.insert(review3, movie, "5");
        assertEquals(6, test.getNumberOfX());
        assertEquals(7, test.getNumberOfY());
        Node<String>[] twoindex = test.insert(review3, movie3, "5");
        review1 = twoindex[0];
        movie1 = twoindex[1];
        assertEquals(6, test.getNumberOfX());
        assertEquals(3, review1.getxId());
        assertEquals(7, movie1.getyId());
        systemOut().clearHistory();
        assertEquals("3:5", test.toStringForOneCol(movie3));
    }


    /**
     * test getAllXNode
     */
    @SuppressWarnings("unchecked")
    public void testGetAllXNode() {
        Node<String> center = test.getCerter();
        assertEquals(center.getxId(), -1);
        assertEquals(center.getyId(), -1);
        Node<String>[] twonode = test.insert(null, null, "5");
        Node<String>[] list = test.getAllXNode(twonode[1]);
        for (int i = 0; i < list.length; i++) {
            assertEquals(list[i].getxId(), i);
        }
        test.removeRow(twonode[0]);
        assertNull(test.getAllXNode(twonode[1]));
    }


    /**
     * test GetAllYNode
     */
    @SuppressWarnings("unchecked")
    public void testGetAllYNode() {
        Node<String>[] twonode = test.insert(null, null, "5");
        Node<String>[] list = test.getAllYNode(twonode[0]);
        for (int i = 0; i < list.length; i++) {
            assertEquals(list[i].getyId(), i);
        }
        test.removeCol(twonode[1]);
        assertNull(test.getAllYNode(twonode[0]));
    }


    /**
     * test ToStringForOneCol
     */
    @SuppressWarnings("unchecked")
    public void testToStringForOneCol() {
        Node<String>[] twonode = test.insert(null, null, "5");
        test.insert(null, twonode[1], "5");
        assertEquals("0:5 1:5", test.toStringForOneCol(twonode[1]));
    }


    /**
     * test get data
     */
    @SuppressWarnings("unchecked")
    public void testGetData() {
        Node<String>[] twonode = test.insert(null, null, "5");
        test.insert(null, twonode[1], "5");
        assertEquals("5", test.getData(twonode[0], twonode[1]));
        test.removeCol(twonode[1]);
        assertNull(test.getData(null, twonode[0]));
    }


    /**
     * test get header
     */
    @SuppressWarnings("unchecked")
    public void testGetHeader() {
        Node<String>[] twonode = test.insert(null, null, "5");
        Node<String> node1 = new Node<String>("5", twonode[1], null, twonode[0],
            null, 0, 0);
        assertEquals(0, test.getXHeader(node1).getxId());
        assertEquals(0, test.getYHeader(node1).getyId());
    }

}
