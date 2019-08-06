package prj5;

import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;
import static prj5.PropertyEnum.*;

/**
 * Class of Reader.
 * 
 * @author <Jiahui Huang> <hjiahui7>
 * @version 2017/12/05
 */
public class Reader
{
    /**
     * field
     */
    private SongBook book;
    private DLList<Person> people;
    private Scanner scanner;
    private DataCalculator calculator;


    /**
     * Constructor of Reader
     * 
     * @param musicSurvey
     * @param SongList
     */
    public Reader(String musicSurvey, String SongList)
    {
        book = readSongs(SongList);
        people = readPeople(musicSurvey);
        calculator = new DataCalculator(people, book);
        new GUIProjectWindow(calculator);
    }


    /**
     * Get the book
     * 
     * @return book
     */
    public SongBook getBook()
    {
        return calculator.returnBook();
    }


    /**
     * Get calculator
     * 
     * @return getCalculator()
     */
    public DataCalculator getCalculator()
    {
        return calculator;
    }


    /**
     * Scan the file, add songs to songbook, and return the book.
     * 
     * @param fileName
     * @return fileName
     */
    public SongBook readSongs(String fileName)
    {
        SongBook bookOfReading = new SongBook();
        try
        {
            scanner = new Scanner(new File(fileName));
        }
        catch (Exception e)
        {
            System.out.println("invalid SongFile");
        }

        scanner.nextLine();
        while (scanner.hasNextLine())
        {
            String cur = scanner.nextLine();
            String[] data = cur.split(",", -1);
            Song temp = new Song(data[0], data[1], data[3], Integer.valueOf(
                data[2]));
            bookOfReading.add(temp);
        }
        scanner.close();
        return bookOfReading;
    }


    /**
     * Scan the file, and return people.
     * 
     * @param fileName
     * @return readPeople
     */
    public DLList<Person> readPeople(String fileName)
    {
        DLList<Person> peoples = new DLList<Person>();
        try
        {
            scanner = new Scanner(new File(fileName));
        }
        catch (Exception e)
        {
            System.out.println("invalid PeopleFile");
        }
        scanner.nextLine();
        while (scanner.hasNextLine())
        {
            String cur = scanner.nextLine();
            String[] p = cur.split(",", -1);
            ArrayList<String> choice = new ArrayList<String>();
            for (int i = 5; i < p.length; i++)
            {
                choice.add(p[i]);
            }

            Person newBody = new Person(toProperty(p[4]), toProperty(p[2]),
                toProperty(p[3]), choice);
            peoples.add(newBody);
        }
        scanner.close();
        return peoples;
    }


    /**
     * toProperty
     * 
     * @param a
     * @return toProperty
     */
    public PropertyEnum toProperty(String a)
    {
        switch (a)
        {
            case "Math or CMDA":
                return MC;
            case "Computer Science":
                return CS;
            case "Other Engineering":
                return ENGINEERING;
            case "Other":
                return OTHERMAJOR;

            case "Southeast":
                return SE;
            case "Outside of United States":
                return OUS;
            case "Northeast":
                return NE;
            case "United States (other than Southeast or Northwest)":
                return US;

            case "music":
                return MUSIC;
            case "reading":
                return READING;
            case "sports":
                return SPORT;
            case "art":
                return ART;

            default:
                return NONE;
        }
    }
}
