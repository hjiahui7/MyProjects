import student.TestCase;

/**
 * This class test the binTree class
 *
 * @author jiahui huang<hjiahui7>
 * @author Yusheng Cao<cyschris>
 * @version 2018.11.22
 */
public class BintreeTest
    extends TestCase
{
    private Bintree   testTree;
    private AirObject testObject1;


    /**
     * Sets up the tests that follow. In general, used for initialization
     */
    public void setUp()
    {
        testTree = new Bintree(1);
        String[] objectInfo1;
        objectInfo1 = new String[11];
        objectInfo1[1] = "type1";
        objectInfo1[2] = "name1";
        objectInfo1[3] = "1";
        objectInfo1[4] = "2";
        objectInfo1[5] = "3";
        objectInfo1[6] = "4";
        objectInfo1[7] = "5";
        objectInfo1[8] = "6";
        objectInfo1[9] = "1thing1";
        objectInfo1[10] = "1thing2";
        testObject1 = new AirObject(objectInfo1);
    }


    /**
     * test the insert method
     */
    public void testInsert()
    {
        testTree.insert(testObject1);
        assertEquals(
            testTree.toString(),
            "Leaf with 1 objects:\r\n"
                + "(Type1 name1 1 2 3 4 5 6 1thing1 1thing2)\r\n"
                + "1 bintree nodes printed");
    }


    /**
     * test the remove method
     */
    public void testRemove()
    {
        testTree.insert(testObject1);
        assertEquals(
            testTree.toString(),
            "Leaf with 1 objects:\r\n"
                + "(Type1 name1 1 2 3 4 5 6 1thing1 1thing2)\r\n"
                + "1 bintree nodes printed");
        testTree.remove(testObject1);
        assertEquals(testTree.toString(), "E\r\n" + "1 bintree nodes printed");

    }


    /**
     * test the get new box method
     */
    public void testGetNewBox()
    {
        Box curBox = new Box(1, 2, 3, 4, 5, 6);
        String type = "x";
        boolean left = true;
        Box result = testTree.getNewBox(curBox, type, left);
        assertEquals(result.getX(), 1);
        assertEquals(result.getY(), 2);
        assertEquals(result.getZ(), 3);
        assertEquals(result.getXwid(), 2);
        assertEquals(result.getYwid(), 5);
        assertEquals(result.getZwid(), 6);

        curBox = new Box(1, 2, 3, 4, 5, 6);
        type = "x";
        left = false;
        result = testTree.getNewBox(curBox, type, left);
        assertEquals(result.getX(), 3);
        assertEquals(result.getY(), 2);
        assertEquals(result.getZ(), 3);
        assertEquals(result.getXwid(), 2);
        assertEquals(result.getYwid(), 5);
        assertEquals(result.getZwid(), 6);
        result = testTree.getNewBox(curBox, 1, left);
        assertEquals(result.getX(), 3);
        assertEquals(result.getY(), 2);
        assertEquals(result.getZ(), 3);
        assertEquals(result.getXwid(), 2);
        assertEquals(result.getYwid(), 5);
        assertEquals(result.getZwid(), 6);

        curBox = new Box(1, 2, 3, 4, 5, 6);
        type = "y";
        left = false;
        result = testTree.getNewBox(curBox, type, left);
        assertEquals(result.getX(), 1);
        assertEquals(result.getY(), 4);
        assertEquals(result.getZ(), 3);
        assertEquals(result.getXwid(), 4);
        assertEquals(result.getYwid(), 2);
        assertEquals(result.getZwid(), 6);
        result = testTree.getNewBox(curBox, 2, left);
        assertEquals(result.getX(), 1);
        assertEquals(result.getY(), 4);
        assertEquals(result.getZ(), 3);
        assertEquals(result.getXwid(), 4);
        assertEquals(result.getYwid(), 2);
        assertEquals(result.getZwid(), 6);

        curBox = new Box(1, 2, 3, 4, 5, 6);
        type = "y";
        left = true;
        result = testTree.getNewBox(curBox, type, left);
        assertEquals(result.getX(), 1);
        assertEquals(result.getY(), 2);
        assertEquals(result.getZ(), 3);
        assertEquals(result.getXwid(), 4);
        assertEquals(result.getYwid(), 2);
        assertEquals(result.getZwid(), 6);

        curBox = new Box(1, 2, 3, 4, 5, 6);
        type = "z";
        left = true;
        result = testTree.getNewBox(curBox, type, left);
        assertEquals(result.getX(), 1);
        assertEquals(result.getY(), 2);
        assertEquals(result.getZ(), 3);
        assertEquals(result.getXwid(), 4);
        assertEquals(result.getYwid(), 5);
        assertEquals(result.getZwid(), 3);
        result = testTree.getNewBox(curBox, 0, left);
        assertEquals(result.getX(), 1);
        assertEquals(result.getY(), 2);
        assertEquals(result.getZ(), 3);
        assertEquals(result.getXwid(), 4);
        assertEquals(result.getYwid(), 5);
        assertEquals(result.getZwid(), 3);

        curBox = new Box(1, 2, 3, 4, 5, 6);
        type = "z";
        left = false;
        result = testTree.getNewBox(curBox, type, left);
        assertEquals(result.getX(), 1);
        assertEquals(result.getY(), 2);
        assertEquals(result.getZ(), 6);
        assertEquals(result.getXwid(), 4);
        assertEquals(result.getYwid(), 5);
        assertEquals(result.getZwid(), 3);

        try
        {
            result = testTree.getNewBox(curBox, "a", left);
        }
        catch (IllegalArgumentException anIllegalArgumentException)
        {
            assertEquals(
                anIllegalArgumentException.getMessage(),
                "Bad type of box: a");
        }

    }


    /**
     * test intersection metion
     */
    public void testIntersection()
    {
        Box interBox = new Box(1, 1, 1, 1, 1, 1);
        assertEquals(
            testTree.intersect(interBox),
            "1 nodes were visited in the bintree");
        testTree.insert(testObject1);
        assertEquals(
            testTree.intersect(interBox),
            "1 nodes were visited in the bintree");

    }


    /**
     * test collisions method
     */
    public void testCollisions()
    {
        assertEquals(
            testTree.collisions(),
            "");
    }

}
