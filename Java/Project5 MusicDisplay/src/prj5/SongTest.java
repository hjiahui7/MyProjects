/**
 * 
 */
package prj5;

import static prj5.PropertyEnum.*;
import student.TestCase;

/**
 * Test class of Song
 * 
 * @author <Jiahui Huang> <hjiahui7>
 * @version 2017/12/05
 */
public class SongTest extends TestCase
{
    /**
     * field
     */
    private Song s;


    /**
     * Set up
     */
    public void setUp()
    {
        s = new Song("hjhdege", "hjh", "POP", 1997);
    }


    /**
     * Test method Basic()
     */
    public void testBasic()
    {
        assertEquals("hjhdege", s.getTitle());
        assertEquals("hjh", s.getArtist());
        assertEquals(1997, s.getDate());
        assertEquals("POP", s.getGenre());
    }


    /**
     * Test method ALL()
     */
    public void testALL()
    {
        s.addNumber(HEARD, US, 43);

        s.addNumber(LIKE, US, 2);
 
        s.addNumber(HEARDTOTAL, US, 50);

        s.addNumber(LIKETOTAL, US, 23);

        assertEquals(43, s.addNumber(HEARD, US, 0));
        assertEquals(2, s.addNumber(LIKE, US, 0)); 
        assertEquals(50, s.addNumber(HEARDTOTAL, US, 0));
        assertEquals(23, s.addNumber(LIKETOTAL, US, 0));

        assertEquals(86, s.getPercent(HEARD, US));
        assertEquals(8, s.getPercent(LIKE, US));

        assertEquals(-1, s.addNumber(NONE, US, 35));
        assertEquals(-1, s.getPercent(NONE, US));
        assertEquals(0, s.getPercent(LIKE, OUS)); 

        assertEquals(-1, s.getPercent(NONE, OUS));
        assertEquals(-1, s.getPercent(NONE, OUS));

    }

}
