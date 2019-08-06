import java.io.FileNotFoundException;
import student.TestCase;

/**
 * This class test the InternalNode class
 *
 * @author jiahui huang<hjiahui7>
 * @author Yusheng Cao<cyschris>
 * @version 2018.11.22
 */
public class InternalNodeTest extends TestCase {
    /**
     * test internal node
     */
    private InternalNode testNode;

    /**
     * test airobject
     */
    private AirObject airobject;
    /**
     * test box
     */
    private Box box;


    /**
     * Sets up the tests that follow. In general, used for initialization
     */
    public void setUp() {
        Bintree tree;
        tree = new Bintree(1);
        testNode = new InternalNode(tree);
        String[] objectbesic;
        objectbesic = new String[11];
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
        airobject = new AirObject(objectbesic);
        box = new Box(5, 5, 5, 5, 5, 5);

    }


    /**
     * test the insert method
     */
    public void testInsert() {
        Node newNode = testNode.insert(airobject, box, 0);
        assertEquals(newNode.toString(box, 0, new StringBuilder()), 3);
    }


    /**
     * test to string method
     */
    public void testToString() {
        Node newNode = testNode.insert(airobject, box, 0);
        assertEquals(newNode.toString(box, 0, new StringBuilder()), 3);
        newNode = testNode.insert(airobject, box, 2);
        systemOut().clearHistory();
        StringBuilder s = new StringBuilder();
        assertEquals(newNode.toString(box, 2, s), 3);
        assertEquals("  I\r\n" + "    Leaf with 2 objects:\r\n"
            + "    (Type1 name1 5 5 5 5 5 5 1thing1 1thing2)\r\n"
            + "    (Type1 name1 5 5 5 5 5 5 1thing1 1thing2)\r\n"
            + "    Leaf with 2 objects:\r\n"
            + "    (Type1 name1 5 5 5 5 5 5 1thing1 1thing2)\r\n"
            + "    (Type1 name1 5 5 5 5 5 5 1thing1 1thing2)\r\n" + "", s
                .toString());
    }


    /**
     * test remove
     * 
     * @throws FileNotFoundException
     */
    public void testRemove() {
        Node newNode = testNode.insert(airobject, box, 0);
        Box testBoxlarge = new Box(1, 5, 5, 1, 5, 5);
        newNode = testNode.insert(airobject, testBoxlarge, 1);
        newNode = testNode.insert(airobject, box, 1);
        systemOut().clearHistory();
        StringBuilder s = new StringBuilder();
        newNode.toString(box, 1, s);
        assertEquals(s.toString(), "I\r\n" + "  Leaf with 2 objects:\r\n"
            + "  (Type1 name1 5 5 5 5 5 5 1thing1 1thing2)\r\n"
            + "  (Type1 name1 5 5 5 5 5 5 1thing1 1thing2)\r\n"
            + "  Leaf with 2 objects:\r\n"
            + "  (Type1 name1 5 5 5 5 5 5 1thing1 1thing2)\r\n"
            + "  (Type1 name1 5 5 5 5 5 5 1thing1 1thing2)\r\n" + "");
        newNode = testNode.remove(airobject);
        systemOut().clearHistory();
        s = new StringBuilder();
        newNode.toString(box, 2, s);
        assertEquals(s.toString(), "  Leaf with 1 objects:\r\n"
            + "  (Type1 name1 5 5 5 5 5 5 1thing1 1thing2)\r\n" + "");
        systemOut().clearHistory();
        Database database = new Database();
        database.add("add drone d1 0 0 0 1 1 1 Droners 3".split(" "));
        database.add("add drone d2 0 512 512 1 1 1 Droners 3".split(" "));
        database.add("add drone d5 0 10 0 1 1 1 Droners 3".split(" "));
        database.add("add drone d8 0 11 0 1 1 1 Droners 3".split(" "));
        database.add("add drone d9 30 555 0 1 1 1 Droners 3".split(" "));
        database.add("add drone d3 512 0 0 1 1 1 Droners 3".split(" "));
        database.add("add drone d4 512 512 0 1 1 1 Droners 3".split(" "));
        database.add("add drone d6 516 515 0 1 1 1 Droners 3".split(" "));
        database.add("add drone d7 518 520 0 1 1 1 Droners 3".split(" "));
        systemOut().clearHistory();
        database.delete("d2");
        database.delete("d1");
        database.add("add drone d10 0 400 0 1 100 1 Droners 3".split(" "));
        database.delete("d5");
        database.delete("d8");
        database.delete("d9");
        database.delete("d10");
        database.delete("d3");
        database.delete("d4");
        database.delete("d6");
        database.delete("d7");

        database.add("add drone d1 0 0 0 1 1 1 Droners 3".split(" "));
        database.add("add drone d2 0 512 512 1 1 1 Droners 3".split(" "));
        database.add("add drone d5 0 10 0 1 1 1 Droners 3".split(" "));
        database.add("add drone d8 0 11 0 1 1 1 Droners 3".split(" "));
        database.add("add drone d9 30 555 0 1 1 1 Droners 3".split(" "));

        database.add("add drone d3 512 0 0 1 1 1 Droners 3".split(" "));
        database.add("add drone d4 512 512 0 1 1 1 Droners 3".split(" "));
        database.add("add drone d6 516 515 0 1 1 1 Droners 3".split(" "));
        database.add("add drone d7 518 520 0 1 1 1 Droners 3".split(" "));
        database.delete("d2");
        database.delete("d9");
        database.delete("d1");
        database.delete("d5");
        database.delete("d8");
        database.delete("d3");
        database.delete("d4");
        database.delete("d6");
        database.delete("d7");
        systemOut().clearHistory();
        database.add("add drone d1 0 0 0 1 1 1 Droners 3".split(" "));
        database.add("add drone d2 0 512 512 1 1 1 Droners 3".split(" "));
        database.add("add drone d5 0 10 0 1 1 1 Droners 3".split(" "));
        database.add("add drone d8 0 11 0 1 1 1 Droners 3".split(" "));
        database.add("add drone d9 30 555 0 1 1 1 Droners 3".split(" "));
        database.add("add drone d3 512 0 0 1 1 1 Droners 3".split(" "));
        database.add("add drone d4 512 512 0 1 1 1 Droners 3".split(" "));
        database.add("add drone d6 516 515 0 1 1 1 Droners 3".split(" "));
        database.add("add drone d7 518 520 0 1 1 1 Droners 3".split(" "));
        database.delete("d1");
        database.delete("d5");
        database.delete("d2");
        database.delete("d8");
        database.delete("d9");
        database.delete("d3");
        systemOut().clearHistory();
        database.printBintree();
        assertEquals("Bintree dump:\r\n" + "Leaf with 3 objects:\r\n"
            + "(Drone d4 512 512 0 1 1 1 Droners 3)\r\n"
            + "(Drone d6 516 515 0 1 1 1 Droners 3)\r\n"
            + "(Drone d7 518 520 0 1 1 1 Droners 3)\r\n"
            + "1 bintree nodes printed\r\n" + "", systemOut().getHistory());
    }


    /**
     * test intersect method
     */
    public void testIntersect() {
        DLList<AirObject> list = new DLList<AirObject>();
        Box intersect = new Box(1, 1, 1, 1, 1, 1);
        assertEquals(testNode.intersect(intersect, box, list, 0), 1);
        assertEquals(testNode.intersect(box, intersect, list, 0), 1);
        assertEquals(testNode.intersect(box, box, list, 0), 3);
    }


    /**
     * test collisions method
     */
    public void testCollision() {
        DLList<CollisionObject> list = new DLList<CollisionObject>();
        assertEquals(testNode.collisions(list), "");
        CollisionObject obj = new CollisionObject(airobject, airobject);
        list.add(obj);
        testNode.insert(airobject, box, 0);
        assertEquals(testNode.collisions(list), "");
    }


    /**
     * test the rest getter method
     */
    public void testGetter() {
        assertEquals(testNode.getSize(), 0);
        assertNull(testNode.getData());
    }

}
