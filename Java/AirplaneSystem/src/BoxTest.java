import student.TestCase;

/**
 * This class test the box class
 *
 * @author jiahui huang<hjiahui7>
 * @author Yusheng Cao<cyschris>
 * @version 2018.11.22
 */
public class BoxTest
    extends TestCase
{
    /**
     * test box
     */
    private Box testBox1;


    /**
     * Sets up the tests that follow. In general, used for initialization
     */
    public void setUp()
    {
        testBox1 = new Box(1, 2, 3, 4, 5, 6);
    }


    /**
     * test all getter method
     */
    public void testGetter()
    {
        assertEquals(testBox1.getX(), 1);
        assertEquals(testBox1.getY(), 2);
        assertEquals(testBox1.getZ(), 3);
        assertEquals(testBox1.getXwid(), 4);
        assertEquals(testBox1.getYwid(), 5);
        assertEquals(testBox1.getZwid(), 6);
    }


    /**
     * test tostring method
     */
    public void testToString()
    {
        assertEquals(
            testBox1.toString(),
            "Box(x=1, y=2, z=3, xwid=4, ywid=5, zwid=6)");
    }

}
