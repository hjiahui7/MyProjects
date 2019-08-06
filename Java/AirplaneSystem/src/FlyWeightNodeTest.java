import student.TestCase;

/**
 * This class test the FlyWeightNode class
 *
 * @author jiahui huang<hjiahui7>
 * @author Yusheng Cao<cyschris>
 * @version 2018.11.23
 */
public class FlyWeightNodeTest extends TestCase {
    /**
     * test flyweight node
     */
    private FlyWeightNode testNode;

    /**
     * test airobject
     */
    private AirObject testObject;
    /**
     * test airobject
     */
    private AirObject testObjectother;
    /**
     * test box
     */
    private Box testBox;
    /**
     * test box
     */
    private Box testBoxlarge;


    /**
     * Sets up the tests that follow. In general, used for initialization
     */
    public void setUp() {
        Bintree tree;
        String[] objectbesic;
        String[] objectxsmall;
        String[] objectysmall;
        String[] objectzsmall;
        tree = new Bintree(3);
        testNode = new FlyWeightNode(tree);
        objectbesic = new String[11];
        objectxsmall = new String[11];
        objectysmall = new String[11];
        objectzsmall = new String[11];
        objectbesic[1] = "type1";
        objectbesic[2] = "name1";
        objectbesic[3] = "5";
        objectbesic[4] = "5";
        objectbesic[5] = "5";
        objectbesic[6] = "5";
        objectbesic[7] = "5";
        objectbesic[8] = "5";
        objectbesic[9] = "1thing1";
        objectbesic[10] = "1thing2";

        objectxsmall[1] = "type2";
        objectxsmall[2] = "name2";
        objectxsmall[3] = "1";
        objectxsmall[4] = "5";
        objectxsmall[5] = "5";
        objectxsmall[6] = "1";
        objectxsmall[7] = "5";
        objectxsmall[8] = "5";
        objectxsmall[9] = "2thing1";
        objectxsmall[10] = "2thing2";

        objectysmall[1] = "type2";
        objectysmall[2] = "name2";
        objectysmall[3] = "5";
        objectysmall[4] = "1";
        objectysmall[5] = "5";
        objectysmall[6] = "5";
        objectysmall[7] = "1";
        objectysmall[8] = "5";
        objectysmall[9] = "2thing1";
        objectysmall[10] = "2thing2";

        objectzsmall[1] = "type2";
        objectzsmall[2] = "name2";
        objectzsmall[3] = "5";
        objectzsmall[4] = "5";
        objectzsmall[5] = "1";
        objectzsmall[6] = "5";
        objectzsmall[7] = "5";
        objectzsmall[8] = "1";
        objectzsmall[9] = "2thing1";
        objectzsmall[10] = "2thing2";

        testObject = new AirObject(objectbesic);
        testObjectother = new AirObject(objectxsmall);
        testBox = new Box(5, 5, 5, 5, 5, 5);
        testBoxlarge = new Box(9, 9, 9, 9, 9, 9);

    }


    /**
     * test insert Method
     */
    public void testInsert() {
        Node newNode = testNode.insert(testObject, testBox, 0);
        assertEquals(newNode.toString(testBox, 1, new StringBuilder()), 1);
        testNode.insert(testObjectother, testBox, 0);
    }


    /**
     * test remove method
     */

    public void testRemove() {
        testNode.insert(testObject, testBox, 0);
        Node removeNode = testNode.remove(testObject);
        assertEquals(removeNode.toString(testBox, 2, new StringBuilder()), 1);
    }


    /**
     * test toString method
     */
    public void testToStirng() {
        testNode.insert(testObject, testBox, 0);
        assertEquals(testNode.toString(testBox, 1, new StringBuilder()), 1);
        testNode.insert(testObjectother, testBox, 0);
    }


    /**
     * test intersect method
     */
    public void testintersect() {
        assertEquals(testNode.intersect(testBoxlarge, testBox, null, 0), 1);
    }


    /**
     * test collision method
     */
    public void testColisions() {
        assertEquals(testNode.collisions(null), "");
    }


    /**
     * test getSize
     */
    public void testGetSize() {
        assertEquals(testNode.getSize(), 0);
    }


    /**
     * test GetData meothod
     */
    public void testGetData() {
        assertNull(testNode.getData());
    }
}
