/**
 * 
 */
package prj5;

import student.TestCase;

/**
 * Test class of Reader
 * 
 * @author <Jiahui Huang> <hjiahui7>
 * @version 2017/12/05
 */
public class ReaderTest extends TestCase
{
    /**
     * field
     */
    private Reader r;


    /**
     * Set up
     */
    public void setUp()
    {
        r = new Reader("MusicSurveyData.csv", "SongList.csv");
        r = new Reader("MusicSurveyDataTest2.csv", "SongListTest2.csv");
        r = new Reader("MusicSurveyDataTest1.csv", "SongListTest1.csv");
    }


    /**
     * Test method ReadSongs()
     */
    public void testReadSongs()
    {
        SongBook book = r.readSongs("SongListTest1.csv");

        assertEquals(5, book.size());

        assertEquals("All These Things I've Done", book.get(0).getTitle());
        assertEquals("All You Need Is Love", book.get(1).getTitle());
        assertEquals("American Pie", book.get(2).getTitle());
        assertEquals("Anarchy in the UK", book.get(3).getTitle());
        assertEquals("Another One Bites the Dust", book.get(4).getTitle());
    }


    /**
     *Test method Error()
     */
    public void testError()
    {
        Exception result = null;
        try
        {
            r = new Reader("null.csv", "SongList.csv");
        }
        catch (Exception e)
        {
            result = e;
        }

        assertTrue(result instanceof Exception);
        try
        {
            r = new Reader("MusicSurveyData.csv", "null.csv");
        }
        catch (Exception e)
        {
            result = e;
        }

        assertTrue(result instanceof Exception);
    }
}
