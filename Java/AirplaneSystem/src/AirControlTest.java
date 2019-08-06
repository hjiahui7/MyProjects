import java.io.FileNotFoundException;
import student.TestCase;

/**
 * This class test the air control class
 *
 * @author jiahui huang<hjiahui7>
 * @author Yusheng Cao<cyschris>
 * @version 2018.11.22
 */
public class AirControlTest
    extends TestCase
{
    /**
     * Sets up the tests that follow. In general, used for initialization
     */
    public void setUp()
    {
        // Nothing Needed left it blank
    }


    /**
     * test the case which over laps happened
     */
    @SuppressWarnings("unused")
    public void testOverlaps()
    {

// (Airplane Air1 0 10 1 20 2 30 USAir 717 4)
// (Airplane Air2 100 1010 101 924 2 900 Delta 17 2)
// (Balloon B1 10 11 11 21 12 31 hot_air 15)
// (Bird pterodactyl 0 100 20 10 50 50 Dinosaur 1)

        AirObject obj1 = new AirObject(
            "add Airplane Air1 0 10 1 20 2 30 USAir 717 4".split(" "));
        AirObject obj2 = new AirObject(
            "add Airplane Air2 100 1010 101 924 2 900 Delta 17 2".split(" "));
        AirObject obj3 = new AirObject(
            "add Balloon B1 10 11 11 21 12 31 hot_air 15".split(" "));
        AirObject obj4 = new AirObject(
            "add Bird pterodactyl 0 100 20 10 50 50 Dinosaur 1".split(" "));
        AirObject[] objects = new AirObject[] { obj1, obj2, obj3, obj4 };
        for (int i = 0; i < objects.length; i++)
        {
            for (int j = i + 1; j < objects.length; j++)
            {
                AirObject first = objects[i];
                AirObject second = objects[j];
// System.out.println(first + " " + (Bintree.checkTwoAir(first,
// second) ? "overlaps" : "does not overlap") + " " + second);
            }
        }
        assertEquals(objects[1].getName(), "Air2");

    }


    /**
     * Get code coverage of the class declaration.
     *
     * @throws FileNotFoundException
     */
    public void testRInit()
        throws FileNotFoundException
    {
        AirControl recstore = new AirControl();
        String[] a = new String[1];
        a[0] = "P4SampleInput.txt";
        AirControl.main(a);
        assertNotNull(recstore);

    }
}
