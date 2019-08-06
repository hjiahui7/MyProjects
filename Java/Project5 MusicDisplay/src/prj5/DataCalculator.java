package prj5;

import java.util.ArrayList;
import static prj5.PropertyEnum.*;

/**
 * the DataCalculator
 * 
 * @author <Jiahui Huang> <hjiahui7>
 * @version 2017/12/05
 */
public class DataCalculator
{
    /**
     * the field
     */
    private DLList<Person> person;
    private SongBook book;


    /**
     * DataCalculator
     * 
     * @param persons
     *            the person
     * @param book
     *            the book
     */
    public DataCalculator(DLList<Person> persons, SongBook book)
    {
        this.person = persons;
        this.book = book;
        calculate();
    }


    /**
     * Get the person
     * 
     * @return person
     */
    public DLList<Person> getPerson()
    {
        return person;
    }


    /**
     * Sort by genre
     */
    public void sortByGenre()
    {
        SongBook bookList = book;
        for (int i = 1; i < bookList.size(); i++)
        {
            int j = i;
            Song tempSong;
            while (j > 0 && ((Song)bookList.get(j - 1)).getGenre().compareTo(
                ((Song)bookList.get(j)).getGenre()) > 0)
            {
                tempSong = (Song)bookList.get(j);
                bookList.remove(j);
                bookList.add(j - 1, tempSong);
                j = j - 1;
            }
        }
    }


    /**
     * Sort by title
     */
    public void sortByTitle()
    {
        SongBook bookList = book;
        for (int i = 1; i < bookList.size(); i++)
        {
            int j = i;
            Song tempSong;
            while (j > 0 && ((Song)bookList.get(j - 1)).getTitle().compareTo(
                ((Song)bookList.get(j)).getTitle()) > 0)
            {
                tempSong = (Song)bookList.get(j);
                bookList.remove(j);
                bookList.add(j - 1, tempSong);
                j = j - 1;
            }
        }
    }


    /**
     * Sort by artist
     */
    public void sortByArtist()
    {
        SongBook bookList = book;
        for (int i = 1; i < bookList.size(); i++)
        {
            int j = i;
            Song tempSong;
            while (j > 0 && ((Song)bookList.get(j - 1)).getArtist().compareTo(
                ((Song)bookList.get(j)).getArtist()) > 0)
            {
                tempSong = (Song)bookList.get(j);
                bookList.remove(j);
                bookList.add(j - 1, tempSong);
                j = j - 1;
            }
        }
    }


    /**
     * Sort by data
     */
    public void sortByData()
    {
        SongBook bookList = book;
        for (int i = 1; i < bookList.size(); i++)
        {
            int j = i;
            Song tempSong;
            while (j > 0 && ((Song)bookList.get(j - 1)).getDate()
                - ((Song)bookList.get(j)).getDate() > 0)
            {
                tempSong = (Song)bookList.get(j);
                bookList.remove(j);
                bookList.add(j - 1, tempSong);
                j = j - 1;
            }
        }
    }


    /**
     * Return the book
     * 
     * @return returnBook
     */
    public SongBook returnBook()
    {
        return book;
    }


    /**
     * Calaulate method
     */
    public void calculate()
    {
        for (int i = 0; i < person.size(); i++)
        {
            Person boy = person.get(i);
            ArrayList<String> cc = boy.getChoice();

            for (int j = 0; j < cc.size(); j++)
            {
                Song song = book.get(j / 2);
                if (j % 2 == 0)
                {
                    if (cc.get(j).toLowerCase().equals("no") || cc.get(j)
                        .toLowerCase().equals("yes"))
                    {
                        song.addNumber(HEARDTOTAL, boy.getHobby(), 1);
                        song.addNumber(HEARDTOTAL, boy.getRegion(), 1);
                        song.addNumber(HEARDTOTAL, boy.getMajor(), 1);
                    }
                    if (cc.get(j).toLowerCase().equals("yes"))
                    {
                        song.addNumber(HEARD, boy.getHobby(), 1);
                        song.addNumber(HEARD, boy.getRegion(), 1);
                        song.addNumber(HEARD, boy.getMajor(), 1);
                    }
                }
                else
                {
                    if (cc.get(j).toLowerCase().equals("no") || cc.get(j)
                        .toLowerCase().equals("yes"))
                    {
                        song.addNumber(LIKETOTAL, boy.getHobby(), 1);
                        song.addNumber(LIKETOTAL, boy.getRegion(), 1);
                        song.addNumber(LIKETOTAL, boy.getMajor(), 1);
                    }
                    if (cc.get(j).toLowerCase().equals("yes"))
                    {
                        song.addNumber(LIKE, boy.getHobby(), 1);
                        song.addNumber(LIKE, boy.getRegion(), 1);
                        song.addNumber(LIKE, boy.getMajor(), 1);
                    }
                }
            }
        }
    }
}
