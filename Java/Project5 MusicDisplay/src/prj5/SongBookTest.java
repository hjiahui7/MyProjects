package prj5;

import student.TestCase;
import static prj5.PropertyEnum.*;

/**
 * Test class of SongBook
 * 
 * @author <Jiahui Huang> <hjiahui7>
 * @version 2017/12/05
 */
public class SongBookTest extends TestCase
{
    /**
     * field
     */
    private SongBook songs;


    /**
     * Set up
     */
    public void setUp()
    {
        songs = new SongBook();
        Song s1 = new Song("hjhdege", "hjh", "POP", 1997);
        s1.addNumber(HEARD, READING, 3);
        s1.addNumber(HEARDTOTAL, READING, 6);
        Song s2 = new Song("haha", "hjh", "POP", 1997);
        s2.addNumber(HEARD, ART, 1);
        s2.addNumber(HEARDTOTAL, ART, 2);
        songs.add(s1);
        songs.add(s2);
    }


    /**
     * Test method TaskReport()
     */
    public void testTaskReport()
    {
        String test = "Song Title: hjhdege" + "\n" + "Song Artist: hjh" + "\n"
            + "Song Genre: POP" + "\n" + "Song Year: 1997" + "\n" + "Heard"
            + "\n" + "reading:50 art:0 sports:0 music:0" + "\n" + "Likes" + "\n"
            + "reading:0 art:0 sports:0 music:0" + "\n" + "\n"
            + "Song Title: haha" + "\n" + "Song Artist: hjh" + "\n"
            + "Song Genre: POP" + "\n" + "Song Year: 1997" + "\n" + "Heard"
            + "\n" + "reading:0 art:50 sports:0 music:0" + "\n" + "Likes" + "\n"
            + "reading:0 art:0 sports:0 music:0" + "\n";
        System.out.print(test);
        assertEquals(test, songs.taskReport());
    }
}
