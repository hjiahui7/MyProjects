import student.TestCase;

/**
 * test class for list class
 *
 * @author jiahui huang<hjiahui7>
 * @author Yusheng Cao<cyschris>
 * @version 2018.9.4
 */
public class NodeTest
    extends TestCase
{
    private Node<String> test;
    private Node<String> left;
    private Node<String> right;
    private Node<String> up;
    private Node<String> down;


    /**
     * set up the test
     */
    public void setUp()
    {
        test = new Node<String>("test", null, null, null, null, 0, 0);
        left = new Node<String>("left", null, null, null, null, 0, 0);
        right = new Node<String>("right", null, null, null, null, 0, 0);
        up = new Node<String>("up", null, null, null, null, 0, 0);
        down = new Node<String>("down", null, null, null, null, 0, 0);
    }


    /**
     * test get data
     */
    public void testGetData()
    {
        assertEquals(test.getData(), "test");
        test.setData("another");
        assertEquals(test.getData(), "another");
    }


    /**
     * test get and set(node)
     */
    public void testLocateOtherNode()
    {
        assertNull(test.getDown());
        assertNull(test.getLeft());
        assertNull(test.getRight());
        assertNull(test.getUp());
        test.setDown(down);
        test.setUp(up);
        test.setLeft(left);
        test.setRight(right);
        assertEquals(test.getRight(), right);
        assertEquals(test.getDown(), down);
        assertEquals(test.getLeft(), left);
        assertEquals(test.getUp(), up);

    }

    /**
     * test get and set(id)
     */
    public void testId()
    {
        assertEquals(test.getxId(), 0);
        assertEquals(test.getyId(), 0);
        test.setyId(1);
        test.setxId(2);
        assertEquals(test.getxId(), 2);
        assertEquals(test.getyId(), 1);
    }

}
