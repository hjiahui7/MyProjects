package prj5;

import java.util.Iterator;
import static prj5.PropertyEnum.*;

/**
 * Class of SongBook
 * 
 * @author <Jiahui Huang> <hjiahui7>
 * @version 2017/12/05
 */
public class SongBook extends DLList<Song>
{
    /**
     * Constructor of SongBook.
     */
    public SongBook()
    {
        super();
    }


    /**
     * Return task report as a string
     * 
     * @return taskReport
     */
    public String taskReport()
    {
        Iterator<Song> p = super.iterator();
        StringBuilder c = new StringBuilder();
        while (p.hasNext())
        {
            Song current = (Song)p.next();
            c.append("Song Title: ");
            c.append(current.getTitle() + "\n");
            c.append("Song Artist: " + current.getArtist() + "\n");
            c.append("Song Genre: " + current.getGenre() + "\n");
            c.append("Song Year: " + String.valueOf(current.getDate()) + "\n");
            c.append("Heard" + "\n");
            c.append("reading:");

            c.append(current.getPercent(HEARD, READING));
            c.append(" art:");
            c.append(current.getPercent(HEARD, ART));
            c.append(" sports:");
            c.append(current.getPercent(HEARD, SPORT));
            c.append(" music:");
            c.append(current.getPercent(HEARD, MUSIC) + "\n");

            c.append("Likes" + "\n");
            c.append("reading:");
            c.append(current.getPercent(LIKE, READING));
            c.append(" art:");
            c.append(current.getPercent(LIKE, ART));
            c.append(" sports:");
            c.append(current.getPercent(LIKE, SPORT));
            c.append(" music:");
            c.append(current.getPercent(LIKE, MUSIC) + "\n");
            if (p.hasNext())
            {
                c.append("\n");
            }

        }
        return c.toString();
    }

}
