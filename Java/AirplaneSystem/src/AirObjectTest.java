import java.util.Arrays;
import student.TestCase;

/**
 * This class test the air control class
 *
 * @author jiahui huang<hjiahui7>
 * @author Yusheng Cao<cyschris>
 * @version 2018.11.22
 */
public class AirObjectTest extends TestCase {
    /**
     * test airobject1
     */
    private AirObject testObject1;
    /**
     * test airobject2
     */
    private AirObject testObject2;
    /**
     * test airobject3
     */
    private AirObject testObjectSameName;


    /**
     * Sets up the tests that follow. In general, used for initialization
     */
    public void setUp() {
        String[] objectInfo1;
        String[] objectInfo2;
        objectInfo1 = new String[11];
        objectInfo2 = new String[11];
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

        objectInfo2[1] = "type2";
        objectInfo2[2] = "name2";
        objectInfo2[3] = "7";
        objectInfo2[4] = "8";
        objectInfo2[5] = "9";
        objectInfo2[6] = "10";
        objectInfo2[7] = "11";
        objectInfo2[8] = "12";
        objectInfo2[9] = "2thing1";
        objectInfo2[10] = "2thing2";

        testObject1 = new AirObject(objectInfo1);
        testObjectSameName = new AirObject(objectInfo1);
        testObject2 = new AirObject(objectInfo2);
    }


    // ----------------------------------------------------------
    /**
     * test all the getter method in the AirObject class
     */
    public void testGetterMethods() {
        assertEquals(testObject1.getName(), "name1");
        assertEquals(testObject1.getX(), 1);
        assertEquals(testObject1.getY(), 2);
        assertEquals(testObject1.getZ(), 3);
        assertEquals(testObject1.getXwid(), 4);
        assertEquals(testObject1.getYwid(), 5);
        assertEquals(testObject1.getZwid(), 6);
        String[] things = new String[2];
        things[0] = "1thing1";
        things[1] = "1thing2";
        assertTrue(Arrays.equals(testObject1.getLastThings(), things));
    }


    /**
     * test the compareTo method which different object
     */
    public void testCompareTo() {
        assertEquals(testObject1.compareTo(testObjectSameName), 0);
        assertEquals(testObject1.compareTo(testObject2), -1);
    }


    /**
     * test the equals method which different object
     */
    public void testEquals() {
        assertTrue(testObject1.equals(testObjectSameName));
        assertFalse(testObject1.equals(testObject2));
        String air = "";
        assertFalse(testObject1.equals(air));
    }


    /**
     * test the to String method
     */
    public void testtoString() {
        assertEquals(testObject1.toString(),
            "Type1 name1 1 2 3 4 5 6 1thing1 1thing2");
        assertEquals(testObject2.toString(),
            "Type2 name2 7 8 9 10 11 12 2thing1 2thing2");
    }

}
