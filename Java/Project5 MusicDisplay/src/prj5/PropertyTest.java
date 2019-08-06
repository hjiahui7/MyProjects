package prj5;

import static prj5.PropertyEnum.*;
import student.TestCase;

/**
 * Test class of PropertyEnum.
 * 
 * @author <Jiahui Huang> <hjiahui7>
 * @version 2017/12/05
 */
public class PropertyTest extends TestCase
{
    /**
     * Test method Property()
     */
    public void testProperty()
    {
        PropertyEnum temp = PropertyEnum.NONE;
        assertEquals(temp, PropertyEnum.NONE);

        assertEquals(NONE, PropertyEnum.NONE);
        PropertyEnum[] thisone = new PropertyEnum[]
        { NONE, HEARD, LIKE, HEARDTOTAL, LIKETOTAL,

            MC, CS, OTHERMAJOR, ENGINEERING,

            READING, SPORT, MUSIC, ART,

            SE, NE, US, OUS };
        assertEquals(NONE, thisone[0]);
        assertEquals(HEARD, thisone[1]);
        assertEquals(LIKE, thisone[2]);
        assertEquals(HEARDTOTAL, thisone[3]);
        assertEquals(LIKETOTAL, thisone[4]);
        assertEquals(MC, thisone[5]);
        assertEquals(CS, thisone[6]);
        assertEquals(OTHERMAJOR, thisone[7]);
        assertEquals(ENGINEERING, thisone[8]);
        assertEquals(READING, thisone[9]);
        assertEquals(SPORT, thisone[10]);
        assertEquals(MUSIC, thisone[11]);
        assertEquals(ART, thisone[12]);
        assertEquals(SE, thisone[13]);
        assertEquals(NE, thisone[14]);
        assertEquals(US, thisone[15]);
        assertEquals(OUS, thisone[16]);

    }
}
