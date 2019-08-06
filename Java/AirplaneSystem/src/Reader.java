import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * This class provide base function of reading data and command in the file and
 * process them in to created database
 *
 * @author jiahui huang<hjiahui7>
 * @author Yusheng Cao<cyschris>
 * @version 2018.11.22
 */
public class Reader
{

    private Database database;


    /**
     * constractor of the reader class
     *
     * @param fileName
     *            file name of the opening file
     * @throws FileNotFoundException
     */
    public Reader(String fileName)
        throws FileNotFoundException
    {
        database = new Database();
        read(fileName);
    }


    // ----------------------------------------------------------
    /**
     * read from the given file
     *
     * @param fileName
     *            file name of the opening file
     * @throws FileNotFoundException
     */
    public void read(String fileName)
        throws FileNotFoundException
    {
        @SuppressWarnings("resource")
        Scanner scanner = new Scanner(new File(fileName));
        while (scanner.hasNextLine())
        {
            String line = scanner.nextLine().replaceAll("\\s{1,}", " ").trim();
            if (line.startsWith("print skiplist"))
            {
                database.printSkipList();
            }
            else if (line.startsWith("print bintree"))
            {
                database.printBintree();
            }
            else if (line.startsWith("add"))
            {
                String[] datas = line.split(" ");
                database.add(datas);
            }
            else if (line.startsWith("print object"))
            {
                database.printObject(line.split(" ")[2]);
            }
            else if (line.startsWith("rangeprint"))
            {
                String[] s = line.split(" ");
                database.rangePrint(s[1], s[2]);
            }
            else if (line.startsWith("delete"))
            {
                String[] s = line.split(" ");
                database.delete(s[1]);
            }
            else if (line.startsWith("intersect"))
            {
                String[] s = line.split(" ");
                database.intersect(
                    Integer.valueOf(s[1]),
                    Integer.valueOf(s[2]),
                    Integer.valueOf(s[3]),
                    Integer.valueOf(s[4]),
                    Integer.valueOf(s[5]),
                    Integer.valueOf(s[6]));
            }
            else if (line.startsWith("collisions"))
            {
                database.collisions();
            }

        }
        scanner.close();
    }

}
