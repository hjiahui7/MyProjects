import student.TestCase;

/**
 * This class test the CheckSize class
 *
 * @author jiahui huang<hjiahui7>
 * @author Yusheng Cao<cyschris>
 * @version 2018.11.22
 */
public class CheckSizeTest extends TestCase {

    /**
     * data string array2
     */
    private String[] objectxsmall;
    /**
     * data string array3
     */
    private String[] objectysmall;
    /**
     * data string array4
     */
    private String[] objectzsmall;
    /**
     * test airobject1
     */
    private AirObject testObject;
    /**
     * test airobject2
     */
    private AirObject testObjectother;
    /**
     * test box1
     */
    private Box testBox;
    /**
     * test box2
     */
    private Box testBoxlarge;


    /**
     * Sets up the tests that follow. In general, used for initialization
     */
    public void setUp() {
        new CheckSize();
        String[] objectbesic;
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


    // ----------------------------------------------------------
    /**
     * test the check collision method
     */
    public void testCheckCollision() {
        assertTrue(CheckSize.checkCollision(testObject, testBoxlarge));
        testObjectother = new AirObject(objectxsmall);
        assertFalse(CheckSize.checkCollision(testObjectother, testBox));
        testObjectother = new AirObject(objectysmall);
        assertFalse(CheckSize.checkCollision(testObjectother, testBox));
        testObjectother = new AirObject(objectzsmall);
        assertFalse(CheckSize.checkCollision(testObjectother, testBox));
        testBoxlarge = new Box(1, 5, 5, 1, 5, 5);
        assertFalse(CheckSize.checkCollision(testObject, testBoxlarge));
        testBoxlarge = new Box(5, 1, 5, 5, 1, 5);
        assertFalse(CheckSize.checkCollision(testObject, testBoxlarge));
        testBoxlarge = new Box(5, 5, 1, 5, 5, 1);
        assertFalse(CheckSize.checkCollision(testObject, testBoxlarge));
    }


    /**
     * test the checkTwoAir method
     */
    public void testCheckTwoAir() {
        assertTrue(CheckSize.checkTwoAir(testObject, testObject));
        testObjectother = new AirObject(objectxsmall);
        assertFalse(CheckSize.checkTwoAir(testObject, testObjectother));
        assertFalse(CheckSize.checkTwoAir(testObjectother, testObject));
        testObjectother = new AirObject(objectysmall);
        assertFalse(CheckSize.checkTwoAir(testObject, testObjectother));
        assertFalse(CheckSize.checkTwoAir(testObjectother, testObject));
        testObjectother = new AirObject(objectzsmall);
        assertFalse(CheckSize.checkTwoAir(testObject, testObjectother));
        assertFalse(CheckSize.checkTwoAir(testObjectother, testObject));

    }


    /**
     * test the checkTwoBox method
     */
    public void testCheckTwoBox() {
        assertTrue(CheckSize.checkTwoBox(testBox, testBox));
        testBoxlarge = new Box(1, 5, 5, 1, 5, 5);
        assertFalse(CheckSize.checkTwoBox(testBox, testBoxlarge));
        assertFalse(CheckSize.checkTwoBox(testBoxlarge, testBox));
        testBoxlarge = new Box(5, 1, 5, 5, 1, 5);
        assertFalse(CheckSize.checkTwoBox(testBox, testBoxlarge));
        assertFalse(CheckSize.checkTwoBox(testBoxlarge, testBox));
        testBoxlarge = new Box(5, 5, 1, 5, 5, 1);
        assertFalse(CheckSize.checkTwoBox(testBox, testBoxlarge));
        assertFalse(CheckSize.checkTwoBox(testBoxlarge, testBox));
    }

}
