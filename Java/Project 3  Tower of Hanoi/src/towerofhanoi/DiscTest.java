package towerofhanoi;

import student.TestCase;

/**
 * the test methods for Disc class
 * 
 * @author <jiahui huang> <hjiahui7>
 * @version 2017/10/20
 */
public class DiscTest extends TestCase
{
    /**
     * The field for DiscTest
     */
    private Disc disc1;
    private Disc disc2;
    private Disc disc3;
    private Disc disc4;
    private Disc disc5;


    /**
     * The setUp for DiscTest
     */
    public void setUp()
    {
        disc1 = new Disc(2);
        disc2 = null;
        disc3 = new Disc(2);
        disc4 = new Disc(3);
        disc5 = new Disc(1);
    }


    /**
     * the test method for method of CompareTo
     */
    public void testCompareTo()
    {
        assertEquals(0, disc1.compareTo(disc3));
        assertEquals(-1, disc1.compareTo(disc4));
        assertEquals(1, disc1.compareTo(disc5));
        Exception thrown = null;
        try
        {
            disc1.compareTo(disc2);
        }
        catch (Exception e)
        {
            thrown = e;
        }
        assertTrue(thrown instanceof IllegalArgumentException);
    }


    /**
     * the test method for method of Disc
     */
    public void testDisc()
    {
        assertEquals("1", disc5.toString());
    }


    /**
     * the test method for method of Equals
     */
    public void testEquals()
    {
        assertFalse(disc1.equals(disc2));
        assertTrue(disc1.equals(disc1));
        assertFalse(disc1.equals(1));
        assertFalse(disc1.equals(disc4));
        assertTrue(disc1.equals(disc3));
    }
}
