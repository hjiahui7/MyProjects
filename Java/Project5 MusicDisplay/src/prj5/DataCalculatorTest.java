/**
 * 
 */
package prj5;

import student.TestCase;
import static prj5.PropertyEnum.*;
import java.util.ArrayList;

/**
 * Test Class of DataCalculator
 * 
 * @author <Jiahui Huang> <hjiahui7>
 * @version 2017/12/05
 */

public class DataCalculatorTest extends TestCase
{
    /**
     * field
     */
    private Reader r;


    /**
     * setup
     */
    public void setUp()
    {
        r = new Reader("MusicSurveyDataTest2.csv", "SongListTest2.csv");
    }


    /**
     * Test method Calculate()
     */
    public void testCalculate()
    {
        DataCalculator d = r.getCalculator();

        SongBook book = d.returnBook();

        assertEquals(6, d.getPerson().size());
        assertEquals(MC, d.getPerson().get(0).getMajor());

        assertEquals(1, book.size());
        assertEquals(2, book.get(0).addNumber(HEARD, SPORT, 0));
        r = new Reader("MusicSurveyData.csv", "SongList.csv");

        d = r.getCalculator();

    }


    /**
     * Test method SortByData()
     */
    public void testSortByData()
    {
        SongBook book2 = new SongBook();
        book2.add(new Song("bb121", "hdsa", "none", 1));
        book2.add(new Song("bb121", "fa", "none", 3));
        book2.add(new Song("a121", "abvc", "none", 5));
        book2.add(new Song("a121", "asd", "none", 4));
        book2.add(new Song("zzz", "bfd", "none", 6));
        book2.add(new Song("ccc", "zxc", "none", 3));
        book2.add(new Song("yyz", "asd", "none", 65));
        book2.add(new Song("zzz", "dsa", "none", 2));
        book2.add(new Song("yya", "f", "none", 1));
        book2.add(new Song("aaa", "a", "none", 6));

        ArrayList<String> choice = new ArrayList<String>();
        choice.add("Yes");
        for (int i = 0; i <= 9; i++)
        {
            choice.add("yes");
        }
        Person per = new Person(ART, MC, NE, choice);
        DLList<Person> pers = new DLList<Person>();
        pers.add(per);
        DataCalculator sec = new DataCalculator(pers, book2);
        sec.sortByData();
        SongBook book3 = sec.returnBook();
        for (int i = 0; i < book3.size() - 1; i++)
        {
            assertTrue(book3.get(i).getDate() - book3.get(i + 1)
                .getDate() <= 0);
        }
        sec.sortByArtist();
        for (int i = 0; i < book2.size() - 1; i++)
        {
            assertTrue(book2.get(i).getArtist().compareTo(book2.get(i + 1)
                .getArtist()) <= 0);
        }
    }


    /**
     * Test method SortByData2()
     */
    public void testSortByData2()
    {

        SongBook book2 = new SongBook();
        book2.add(new Song("bb121", "hdsa", "none", 10));
        book2.add(new Song("bb121", "fa", "none", 9));
        book2.add(new Song("a121", "abvc", "none", 8));
        book2.add(new Song("a121", "asd", "none", 7));
        book2.add(new Song("zzz", "bfd", "none", 6));
        book2.add(new Song("ccc", "zxc", "none", 5));
        book2.add(new Song("yyz", "asd", "none", 4));
        book2.add(new Song("zzz", "dsa", "none", 3));
        book2.add(new Song("yya", "f", "none", 2));
        book2.add(new Song("aaa", "a", "none", 1));

        ArrayList<String> choice = new ArrayList<String>();
        choice.add("Yes");
        for (int i = 0; i <= 9; i++)
        {
            choice.add("yes");
        }
        Person per = new Person(ART, MC, NE, choice);
        DLList<Person> pers = new DLList<Person>();
        pers.add(per);
        DataCalculator sec = new DataCalculator(pers, book2);
        sec.sortByData();
        SongBook book3 = sec.returnBook();
        int i = 0;
        assertTrue(book3.get(i).getDate() - book3.get(i + 1).getDate() <= 0);

    }


    /**
     * Test method SortByTitle()
     */
    public void testSortByTitle()
    {
        SongBook book2 = new SongBook();
        book2.add(new Song("bb121", "jiahui", "none", 2312312));
        book2.add(new Song("bb121", "jiahui", "none", 2312312));
        book2.add(new Song("a121", "jiahui", "none", 2312312));
        book2.add(new Song("a121", "jiahui", "none", 2312312));
        book2.add(new Song("zzz", "jiahui", "none", 2312312));
        book2.add(new Song("ccc", "jiahui", "none", 2312312));
        book2.add(new Song("yyz", "jiahui", "none", 2312312));
        book2.add(new Song("zzz", "jiahui", "none", 2312312));
        book2.add(new Song("yya", "jiahui", "none", 2312312));
        book2.add(new Song("aaa", "jiahui", "none", 2312312));

        ArrayList<String> choice = new ArrayList<String>();
        choice.add("Yes");
        for (int i = 0; i <= 9; i++)
        {
            choice.add("yes");
        }
        Person per = new Person(ART, MC, NE, choice);
        DLList<Person> pers = new DLList<Person>();
        pers.add(per);
        DataCalculator sec = new DataCalculator(pers, book2);
        sec.sortByTitle();
        for (int i = 0; i < book2.size() - 1; i++)
        {
            assertTrue(book2.get(i).getTitle().compareTo(book2.get(i + 1)
                .getTitle()) <= 0);
        }
    }


    /**
     * Test method SortByGenre()
     */
    public void testSortByGenre()
    {
        SongBook book2 = new SongBook();
        book2.add(new Song("jiahui", "none", "bb121", 2312312));
        book2.add(new Song("jiahui", "none", "bb121", 2312312));
        book2.add(new Song("jiahui", "none", "a121", 2312312));
        book2.add(new Song("jiahui", "none", "a121", 2312312));
        book2.add(new Song("jiahui", "none", "zzz", 2312312));
        book2.add(new Song("jiahui", "none", "ccc", 2312312));
        book2.add(new Song("jiahui", "none", "yyz", 2312312));
        book2.add(new Song("jiahui", "none", "zzz", 2312312));
        book2.add(new Song("jiahui", "none", "yya", 2312312));
        book2.add(new Song("jiahui", "none", "aaa", 2312312));

        ArrayList<String> choice = new ArrayList<String>();
        choice.add("Yes");
        for (int i = 0; i <= 9; i++)
        {
            choice.add("yes");
        }
        Person per = new Person(ART, MC, NE, choice);
        DLList<Person> pers = new DLList<Person>();
        pers.add(per);
        DataCalculator sec = new DataCalculator(pers, book2);
        sec.sortByGenre();
        for (int i = 0; i < book2.size() - 1; i++)
        {
            assertTrue(book2.get(i).getGenre().compareTo(book2.get(i + 1)
                .getGenre()) <= 0);
        }
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
        for (int z = 0; z < 17; z++)
        {
            String.valueOf(thisone[z]);
        }
    }

}
