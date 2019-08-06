package prj5;

import java.util.HashMap;

/**
 * Class of Song
 * 
 * @author <Jiahui Huang> <hjiahui7>
 * @version 2017/12/05
 */
public class Song
{
    /**
     * field
     */
    private String name;
    private String author;
    private int year;
    private String genre;
    private HashMap<PropertyEnum, Integer> map;
    private int[] numberHeard;
    private int[] numberLike;
    private int[] numberHeardTotal;
    private int[] numberLikeTotal;
    private int[] numberLikePer;
    private int[] numberHeardPer;


    /**
     * Constructor of Song
     * 
     * @param name name
     * @param author author
     * @param genre genre
     * @param year year
     */
    public Song(String name, String author, String genre, int year)
    {
        this.name = name; 
        this.author = author;
        this.year = year;
        this.genre = genre;
        map = new HashMap<PropertyEnum, Integer>();
        map.put(PropertyEnum.US, 1);
        map.put(PropertyEnum.NE, 2);
        map.put(PropertyEnum.SE, 3);
        map.put(PropertyEnum.OUS, 4);

        map.put(PropertyEnum.MC, 5);
        map.put(PropertyEnum.ENGINEERING, 6);
        map.put(PropertyEnum.CS, 7);
        map.put(PropertyEnum.OTHERMAJOR, 8);

        map.put(PropertyEnum.READING, 9);
        map.put(PropertyEnum.ART, 10);
        map.put(PropertyEnum.SPORT, 11);
        map.put(PropertyEnum.MUSIC, 12);

        numberHeard = new int[13];
        numberLike = new int[13];
        numberHeardTotal = new int[13];
        numberLikeTotal = new int[13];
        numberLikePer = new int[13];
        numberHeardPer = new int[13];

        numberLikePer[0] = numberLikePer[0] + 1;
        numberHeardPer[0] = numberHeardPer[0] + 1;
        numberLikePer[0] = 0;
        numberHeardPer[0] = 0;
    }


    /**
     * Get the title
     * 
     * @return (String) the title of current song object.
     */
    public String getTitle()
    {
        return name;
    }


    /**
     * Get the artist
     * 
     * @return (String) the artist of current song object.
     */
    public String getArtist()
    {
        return author;
    }


    /**
     * Get the date
     * 
     * @return (int) the date of current song object.
     */
    public int getDate()
    {
        return year;
    }


    /**
     * Get the genre
     * 
     * @return (String) the genre of current song object.
     */
    public String getGenre()
    {
        return genre;
    }


    /**
     * Get the percent
     * @param p1
     *            REGION, MAJOR, HOBBY;
     * @param p2
     *            HEARD, LIKE, HEARDTOTAL, LIKETOTAL;
     * @return the number of the current value;
     */
    public int getPercent(PropertyEnum p1, PropertyEnum p2)
    {
        int result = 0;
        switch (p1)
        {
            case HEARD:
                if (numberHeardTotal[map.get(p2)] == 0)
                {
                    result = 0;
                    break;
                }
                else
                {
                    result = numberHeard[map.get(p2)] * 100
                        / numberHeardTotal[map.get(p2)];
                    numberHeardPer[map.get(p2)] = result;
                }
                break;
            case LIKE:
                if (numberLikeTotal[map.get(p2)] == 0)
                {
                    result = 0; 
                }
                else
                {
                    result = numberLike[map.get(p2)] * 100 / numberLikeTotal[map
                        .get(p2)];
                    numberLikePer[map.get(p2)] = result;
                }
                break;

            default:
                result = -1;
        }

        return result;
    }


    /**
     * Add the number of values
     * @param p1
     *            REGION, MAJOR, HOBBY;
     * @param p2
     *            HEARD, LIKE, HEARDTOTAL, LIKETOTAL;
     * @param x
     *            the number to add;
     * @return the number of the current value;
     */
    public int addNumber(PropertyEnum p1, PropertyEnum p2, int x)
    {
        if (p2 == PropertyEnum.NONE)
        {
            return -1;
        }
        int result = 0;
        switch (p1)
        {
            case HEARD:
                numberHeard[map.get(p2)] += x;
                result = numberHeard[map.get(p2)];
                break;
            case LIKE:
                numberLike[map.get(p2)] += x;
                result = numberLike[map.get(p2)];
                break;
            case HEARDTOTAL:
                numberHeardTotal[map.get(p2)] += x;
                result = numberHeardTotal[map.get(p2)];
                break;
            case LIKETOTAL:
                numberLikeTotal[map.get(p2)] += x;
                result = numberLikeTotal[map.get(p2)];
                break;
            default:
                result = -1;
        }

        return result;
    }

}
