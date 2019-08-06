import student.TestCase;

/**
 * This class test the collision object class
 *
 * @author jiahui huang<hjiahui7>
 * @author Yusheng Cao<cyschris>
 * @version 2018.11.22
 */
public class CollisionObjectTest
    extends TestCase
{
    /**
     * test collision object
     */
    private CollisionObject test;
    /**
     * test air object1
     */
    private AirObject       testObject;
    /**
     * test air object2
     */
    private AirObject       testObjectother;
    /**
     * test string array data
     */
    private String[]        objectbesic;


    /**
     * Sets up the tests that follow. In general, used for initialization
     */
    public void setUp()
    {
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

        testObject = new AirObject(objectbesic);
        objectbesic[2] = "name2";
        testObjectother = new AirObject(objectbesic);
        test = new CollisionObject(testObject, testObjectother);
    }


    /**
     * test all getters
     */
    public void testGetters()
    {
        AirObject a = test.getA();
        assertEquals(a.getName(), "name1");
        AirObject b = test.getB();
        assertEquals(b.getName(), "name2");
    }


    /**
     * test equals method
     */
    public void testEuqals()
    {
        assertTrue(test.equal(test));
        // same collisionobject
        CollisionObject same = new CollisionObject(testObject, testObjectother);
        assertTrue(test.equal(same));
        // switch a and b
        same = new CollisionObject(testObjectother, testObject);
        assertTrue(test.equal(same));
        // not same
        CollisionObject dif =
            new CollisionObject(testObjectother, testObjectother);
        assertFalse(test.equal(dif));
        dif = new CollisionObject(testObject, testObject);
        assertFalse(test.equal(dif));
        objectbesic[1] = "type3";
        objectbesic[2] = "name3";
        objectbesic[3] = "3";
        objectbesic[4] = "3";
        objectbesic[5] = "3";
        objectbesic[6] = "3";
        objectbesic[7] = "3";
        objectbesic[8] = "3";
        objectbesic[9] = "1thing3";
        objectbesic[10] = "1thing3";
        testObject = new AirObject(objectbesic);
        dif = new CollisionObject(testObject, testObject);
        assertFalse(test.equal(dif));

    }

}
