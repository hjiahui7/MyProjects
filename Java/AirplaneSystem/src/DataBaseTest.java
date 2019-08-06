import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import acm.util.RandomGenerator;
import student.TestCase;

/**
 * This class test the database class
 *
 * @author jiahui huang<hjiahui7>
 * @author Yusheng Cao<cyschris>
 * @version 2018.11.22
 */
public class DataBaseTest extends TestCase {
    private Database testBase;


    /**
     * Sets up the tests that follow. In general, used for initialization
     */
    public void setUp() {
        testBase = new Database();
    }


    /**
     * test add method
     */
    public void testAdd() {
        String[] data = { "1", "2", "3", "4", "5", "6", "7", "8", "9", "0" };
        testBase.add(data);
        testBase.add(data);
        String[] data1 = new String[10];
        data1[1] = "0";
        data1[2] = "1";
        data1[3] = "-1";
        data1[4] = "1";
        data1[5] = "1";
        data1[6] = "1";
        data1[7] = "1";
        data1[8] = "1";
        data1[9] = "1";
        testBase.add(data1);

        data1 = new String[10];
        data1[1] = "0";
        data1[2] = "1";
        data1[3] = "1";
        data1[4] = "-1";
        data1[5] = "1";
        data1[6] = "1";
        data1[7] = "1";
        data1[8] = "1";
        data1[9] = "1";
        testBase.add(data1);

        data1 = new String[10];
        data1[1] = "0";
        data1[2] = "1";
        data1[3] = "1";
        data1[4] = "1";
        data1[5] = "-1";
        data1[6] = "1";
        data1[7] = "1";
        data1[8] = "1";
        data1[9] = "1";
        testBase.add(data1);

        data1 = new String[10];
        data1[1] = "0";
        data1[2] = "1";
        data1[3] = "1";
        data1[4] = "1";
        data1[5] = "1";
        data1[6] = "-1";
        data1[7] = "1";
        data1[8] = "1";
        data1[9] = "1";
        testBase.add(data1);

        data1 = new String[10];
        data1[1] = "0";
        data1[2] = "1";
        data1[3] = "1";
        data1[4] = "1";
        data1[5] = "1";
        data1[6] = "1";
        data1[7] = "-1";
        data1[8] = "1";
        data1[9] = "1";
        testBase.add(data1);

        data1 = new String[10];
        data1[1] = "0";
        data1[2] = "1";
        data1[3] = "1";
        data1[4] = "1";
        data1[5] = "1";
        data1[6] = "1";
        data1[7] = "1";
        data1[8] = "-1";
        data1[9] = "1";
        testBase.add(data1);

        String[] data2 = { "0", "1", "1", "1", "1", "1", "1", "1", "1", "1" };
        testBase.add(data2);
        String[] data3 = { "0", "3", "10", "1028", "1", "1", "1", "1", "1",
            "1" };
        testBase.add(data3);
        String[] data4 = { "0", "3", "11", "1", "1028", "1", "1", "1", "1",
            "1" };
        testBase.add(data4);
        String[] data5 = { "0", "3", "12", "1", "1", "1028", "1", "1", "1",
            "1" };
        testBase.add(data5);
        String output = systemOut().getHistory();
        assertEquals(output, "3 has been added to the database\r\n"
            + "Duplicate object names not permitted: |3|\r\n"
            + "Bad box (-1 1 1 1 1 1). All widths must be positive.\r\n"
            + "Bad box (1 -1 1 1 1 1). All widths must be positive.\r\n"
            + "Bad box (1 1 -1 1 1 1). All widths must be positive.\r\n"
            + "Bad box (1 1 1 -1 1 1). All widths must be positive.\r\n"
            + "Bad box (1 1 1 1 -1 1). All widths must be positive.\r\n"
            + "Bad box (1 1 1 1 1 -1). All widths must be positive.\r\n"
            + "1 has been added to the database\r\n"
            + "Bad box (1028 1 1 1 1 1). "
            + "All boxes must be entirely within the world box.\r\n"
            + "Bad box (1 1028 1 1 1 1). "
            + "All boxes must be entirely within the world box.\r\n"
            + "Bad box (1 1 1028 1 1 1). "
            + "All boxes must be entirely within the world box.\r\n" + "");

    }


    /**
     * test rangeprint method
     */
    public void testRangPrint() {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        String[] data = { "1", "2", "5", "4", "5", "6", "7", "8", "9", "0" };
        testBase.add(data);
        testBase.add(data);
        testBase.rangePrint("3", "2");
        testBase.rangePrint("2", "3");
        testBase.rangePrint("6", "9");
        testBase.rangePrint("0", "9");
        String[] data2 = { "1", "2", "20", "4", "5", "6", "7", "8", "9", "0" };
        testBase.add(data2);
        testBase.rangePrint("2", "333");
        testBase.rangePrint("2333", "2");
        testBase.rangePrint("19", "21");
        testBase.rangePrint("22", "24");
        assertEquals(outContent.toString(),
            "5 has been added to the database\r\n"
                + "Duplicate object names not permitted: |5|\r\n"
                + "Error in rangeprint parameters: |3| is not less "
                + "than |2|\r\n"
                + "Found these records in the range |2| to |3|\r\n"
                + "Found these records in the range |6| to |9|\r\n"
                + "Found these records in the range |0| to |9|\r\n"
                + "2 5 4 5 6 7 8 9 0\r\n"
                + "20 has been added to the database\r\n"
                + "Found these records in the range |2| to |333|\r\n"
                + "2 20 4 5 6 7 8 9 0\r\n"
                + "Error in rangeprint parameters: |2333| is not less "
                + "than |2|\r\n"
                + "Found these records in the range |19| to |21|\r\n"
                + "2 20 4 5 6 7 8 9 0\r\n"
                + "Found these records in the range |22| to |24|\r\n" + "");
    }


    /**
     * test delete method
     */
    public void testDelete() {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        testBase.delete("");
        String[] data = { "1", "2", "5", "4", "5", "6", "7", "8", "9", "0" };
        testBase.add(data);
        testBase.delete("5");
        assertEquals(outContent.toString(), "Object || not in the database\r\n"
            + "5 has been added to the database\r\n"
            + "Deleted |5| from the database\r\n" + "");
    }


    /**
     * test printObject method
     */
    public void testPrintObject() {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        testBase.printObject("");
        String[] data = { "1", "2", "5", "4", "5", "6", "7", "8", "9", "0" };
        testBase.add(data);
        testBase.printObject("5");
        assertEquals(outContent.toString(),
            "|| does not exist in the database\r\n"
                + "5 has been added to the database\r\n"
                + "Found: 2 5 4 5 6 7 8 9 0\r\n" + "");
    }


    /**
     * test print skiplist method
     */
    public void testPrintSkipList() {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        testBase.printSkipList();
        Database aDatabase = new Database();
        RandomGenerator.setNextInts(1);
        aDatabase.add("add drone d1 0 0 0 1 1 1 Droners 3".split(" "));
        systemOut().clearHistory();
        aDatabase.printSkipList();
        assertEquals(systemOut().getHistory(), "SkipList dump:\r\n"
            + "Node has depth 1, Value (null)\r\n"
            + "Node has depth 1, Value (Drone d1 0 0 0 1 1 1 Droners 3)\r\n"
            + "1 skiplist nodes printed\r\n" + "");
    }


    /**
     * test intersect method
     */
    public void testIntersect() {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        testBase.intersect(-1, 1, 1, 1, 1, 1);
        testBase.intersect(1, -1, 1, 1, 1, 1);
        testBase.intersect(1, 1, -1, 1, 1, 1);
        testBase.intersect(1, 1, 1, -1, 1, 1);
        testBase.intersect(1, 1, 1, 1, -1, 1);
        testBase.intersect(1, 1, 1, 1, 1, -1);

        testBase.intersect(1025, 1, 1, 1, 1, 1);
        testBase.intersect(1, 1025, 1, 1, 1, 1);
        testBase.intersect(1, 1, 1025, 1, 1, 1);
        testBase.intersect(1, 1, 1, 1025, 1, 1);
        testBase.intersect(1, 1, 1, 1, 1025, 1);
        testBase.intersect(1, 1, 1, 1, 1, 1025);

        testBase.intersect(1, 1, 1, 1, 1, 1);

        assertEquals(outContent.toString(),
            "Bad box (-1 1 1 1 1 1). All widths must be positive.\r\n"
                + "Bad box (1 -1 1 1 1 1). All widths must be positive.\r\n"
                + "Bad box (1 1 -1 1 1 1). All widths must be positive.\r\n"
                + "Bad box (1 1 1 -1 1 1). All widths must be positive.\r\n"
                + "Bad box (1 1 1 1 -1 1). All widths must be positive.\r\n"
                + "Bad box (1 1 1 1 1 -1). All widths must be positive.\r\n"
                + "Bad box (1025 1 1 1 1 1). All boxes must be entirely "
                + "within the world box.\r\n"
                + "Bad box (1 1025 1 1 1 1). All boxes must be entirely "
                + "within the world box.\r\n"
                + "Bad box (1 1 1025 1 1 1). All boxes must be entirely "
                + "within the world box.\r\n"
                + "Bad box (1 1 1 1025 1 1). All boxes must be entirely "
                + "within the world box.\r\n"
                + "Bad box (1 1 1 1 1025 1). All boxes must be entirely "
                + "within the world box.\r\n"
                + "Bad box (1 1 1 1 1 1025). All boxes must be entirely "
                + "within the world box.\r\n"
                + "The following objects intersect (1 1 1 1 1 1):\r\n"
                + "1 nodes were visited in the bintree\r\n" + "");
    }


    /**
     * test collisions method
     */
    public void testCollisions() {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        testBase.collisions();
        assertEquals(outContent.toString(),
            "The following collisions exist in the database:\r\n" + "");
    }


    /**
     * test printbintree metod
     */
    public void testprintBintree() {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        testBase.printBintree();
        assertEquals(outContent.toString(), "Bintree dump:\r\n" + "E\r\n"
            + "1 bintree nodes printed\r\n" + "");
    }

}
