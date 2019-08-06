import student.TestCase;

/**
 * This class test the DataNode class
 *
 * @author jiahui huang<hjiahui7>
 * @author Yusheng Cao<cyschris>
 * @version 2018.11.22
 */
public class DataNodeTest extends TestCase {
    /**
     * test data node
     */
    private DataNode testNode;
    /**
     * test binary tree
     */
    private Bintree tree;
    /**
     * test air object
     */
    private AirObject airobject;
    /**
     * test box
     */
    private Box box;
    /**
     * test string array
     */
    private String[] objectbesic;


    /**
     * Sets up the tests that follow. In general, used for initialization
     */
    public void setUp() {
        tree = new Bintree(3);
        testNode = new DataNode(tree);
        objectbesic = new String[11];
        objectbesic[1] = "type1";
        objectbesic[2] = "1";
        objectbesic[3] = "5";
        objectbesic[4] = "5";
        objectbesic[5] = "5";
        objectbesic[6] = "5";
        objectbesic[7] = "5";
        objectbesic[8] = "5";
        objectbesic[9] = "1thing1";
        objectbesic[10] = "1thing2";
        airobject = new AirObject(objectbesic);
        box = new Box(5, 5, 5, 5, 5, 5);
    }


    /**
     * test add data method
     */
    public void testAddData() {
        testNode.addData(airobject);
        testNode.addData(airobject);
        objectbesic = new String[11];
        objectbesic[1] = "type2";
        objectbesic[2] = "2";
        objectbesic[3] = "5";
        objectbesic[4] = "5";
        objectbesic[5] = "5";
        objectbesic[6] = "5";
        objectbesic[7] = "5";
        objectbesic[8] = "5";
        objectbesic[9] = "1thing2";
        objectbesic[10] = "1thing3";
        airobject = new AirObject(objectbesic);
        testNode.addData(airobject);
        assertEquals(testNode.getSize(), 3);
        objectbesic = new String[11];
        objectbesic[1] = "type2";
        objectbesic[2] = "0";
        objectbesic[3] = "5";
        objectbesic[4] = "5";
        objectbesic[5] = "5";
        objectbesic[6] = "5";
        objectbesic[7] = "5";
        objectbesic[8] = "5";
        objectbesic[9] = "1thing2";
        objectbesic[10] = "1thing3";
        airobject = new AirObject(objectbesic);
        testNode.addData(airobject);
        assertEquals(testNode.getSize(), 4);
    }


    /**
     * test the split add method
     */
    public void testSplitAdd() {
        InternalNode testInterNode = new InternalNode(tree);
        testNode.splitAdd(testInterNode, airobject, box, 1);
        testNode.addData(airobject);
        testNode.splitAdd(testInterNode, airobject, box, 1);
        assertEquals(testNode.getSize(), 1);
        assertEquals(testInterNode.toString(box, 0, new StringBuilder()), 3);

    }


    /**
     * test is Full method
     */
    public void testIsFull() {
        assertFalse(testNode.isFull(airobject));
        testNode.addData(airobject);
        testNode.addData(airobject);
        testNode.addData(airobject);
        testNode.addData(airobject);
        assertFalse(testNode.isFull(airobject));
        objectbesic[1] = "type2";
        objectbesic[2] = "name2";
        objectbesic[3] = "1";
        objectbesic[4] = "5";
        objectbesic[5] = "5";
        objectbesic[6] = "1";
        objectbesic[7] = "5";
        objectbesic[8] = "5";
        objectbesic[9] = "2thing1";
        objectbesic[10] = "2thing2";
        AirObject testObjectother = new AirObject(objectbesic);
        assertTrue(testNode.isFull(testObjectother));
    }


    /**
     * test the insert method
     */
    public void testInsert() {
        testNode.insert(airobject, box, 0);
        testNode.addData(airobject);
        testNode.addData(airobject);
        testNode.addData(airobject);
        testNode.addData(airobject);
        assertFalse(testNode.isFull(airobject));
        objectbesic[1] = "type2";
        objectbesic[2] = "name2";
        objectbesic[3] = "1";
        objectbesic[4] = "1";
        objectbesic[5] = "1";
        objectbesic[6] = "1";
        objectbesic[7] = "1";
        objectbesic[8] = "1";
        objectbesic[9] = "2thing1";
        objectbesic[10] = "2thing2";
        AirObject testObjectother = new AirObject(objectbesic);
        testNode.insert(airobject, box, 2);
        testNode.insert(testObjectother, box, 0);
        box = new Box(1, 1, 1, 1, 1, 1);
        testNode.insert(testObjectother, box, 0);
        assertFalse(testNode.isFull(airobject));
        assertEquals(testNode.getSize(), 6);
    }


    /**
     * test toString method
     */
    public void testToString() {
        assertEquals(testNode.toString(box, 1, new StringBuilder()), 1);
        assertEquals(testNode.toString(box, 5, new StringBuilder()), 1);
        testNode.addData(airobject);
        testNode.addData(airobject);
        testNode.addData(airobject);
        testNode.addData(airobject);
        assertEquals(testNode.toString(box, 5, new StringBuilder()), 1);

    }


    /**
     * test remove method
     */
    public void testRemove() {
        testNode.remove(airobject);
        testNode.addData(airobject);
        testNode.addData(airobject);
        assertEquals(testNode.getSize(), 2);
        testNode.remove(airobject);
        assertEquals(testNode.getSize(), 1);
    }


    /**
     * test intersect method
     */
    public void testIntersect() {
        DLList<AirObject> list = new DLList<AirObject>();
        Box interB = new Box(5, 5, 5, 5, 5, 5);
        testNode.intersect(interB, box, list, 0);
        testNode.addData(airobject);
        testNode.addData(airobject);
        testNode.intersect(interB, box, list, 0);
        interB = new Box(1, 1, 1, 1, 1, 1);
        testNode.intersect(interB, box, list, 0);
        assertEquals(list.size(), 1);
    }


    /**
     * test collisions method
     */
    public void testCollisions() {
        DLList<CollisionObject> list = new DLList<CollisionObject>();
        assertEquals(testNode.collisions(list), "");
        testNode.addData(airobject);
        testNode.addData(airobject);
        assertEquals(testNode.collisions(list),
            "(Type1 1 5 5 5 5 5 5 1thing1 1thing2) "
                + "and (Type1 1 5 5 5 5 5 5 1thing1 1thing2)\r\n" + "");
        CollisionObject obj = new CollisionObject(airobject, airobject);
        list.add(obj);
        assertEquals(testNode.collisions(list), "");
        objectbesic[1] = "type2";
        objectbesic[2] = "name2";
        objectbesic[3] = "1";
        objectbesic[4] = "5";
        objectbesic[5] = "5";
        objectbesic[6] = "1";
        objectbesic[7] = "5";
        objectbesic[8] = "5";
        objectbesic[9] = "2thing1";
        objectbesic[10] = "2thing2";
        AirObject testObjectother = new AirObject(objectbesic);
        testNode.addData(testObjectother);
        assertEquals(testNode.collisions(list), "");
        obj = new CollisionObject(airobject, testObjectother);
        list.add(obj);
        assertEquals(testNode.collisions(list), "");
    }


    /**
     * test all getters
     */
    public void testGetters() {
        assertEquals(testNode.getSize(), 0);
        testNode.addData(airobject);
        testNode.addData(airobject);
        assertEquals(testNode.getData().size(), 2);
    }

}
