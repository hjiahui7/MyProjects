package towerofhanoi;

import student.TestCase;

/**
 * the class for TowerTest for testing the method in tower
 * 
 * @author <jiahui huang> <hjiahui7>
 * @version 2017/10/20
 */
public class TowerTest extends TestCase
{
    /**
     * the field for TowerTest
     */
    private Tower tower1;


    /**
     * The setUp for TowerTest
     */
    public void setUp()
    {
        tower1 = new Tower(Position.LEFT);
    }


    /**
     * The method for testPosition
     */
    public void testPosition()
    {
        assertEquals(Position.LEFT, tower1.position());
    }


    /**
     * The method for testPush
     */
    public void testPush()
    {
        Disc d = null;
        tower1.push(new Disc(2));
        assertEquals(tower1.size(), 1);
        tower1.push(new Disc(1));
        assertEquals(tower1.size(), 2);
        Exception thrown = null;
        try
        {
            tower1.push(d);
        }
        catch (Exception e)
        {
            thrown = e;
        }
        assertNotNull(thrown);
        assertTrue(thrown instanceof IllegalArgumentException);
        try
        {
            tower1.push(new Disc(3));
        }
        catch (Exception e)
        {
            thrown = e;
        }
        assertNotNull(thrown);
        assertTrue(thrown instanceof IllegalStateException);
    }
}
