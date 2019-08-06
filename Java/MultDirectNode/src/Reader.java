import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * create a base unit to store inforamtion which contain data location and all
 * the four nodes connected to it.
 *
 * @author jiahui huang<hjiahui7>
 * @author Yusheng Cao<cyschris>
 * @version 2018.9.20
 */
public class Reader {

    private Management manage;


    /**
     * get information from the file
     *
     * @param hashSize
     *            size of the hash map
     * @param fileName
     *            file name which contain the information
     * @throws FileNotFoundException
     */
    public Reader(int hashSize, String fileName) throws FileNotFoundException {
        manage = new Management(hashSize);
        read(fileName);
    }


    /**
     * open the file and store all data
     *
     * @param fileName
     *            file name
     * @throws FileNotFoundException
     */
    public void read(String fileName) throws FileNotFoundException {
        Scanner scanner = new Scanner(new File(fileName));
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine().replaceAll("\\s{1,}", " ").trim();
            if (line.startsWith("delete reviewer")) {
                String reviewers = line.replaceFirst("delete reviewer", "")
                    .trim();
                manage.deleteReviewer(reviewers);
            }
            else if (line.startsWith("delete movie")) {
                String movie = line.replaceFirst("delete movie", "").trim();
                manage.deleteMovie(movie);
            }
            else if (line.startsWith("add")) {
                String[] data = line.replaceFirst("add", "").trim().split(
                    "<SEP>");
                String reviewers = data[0].trim();
                String movie = data[1].trim();
                int score = Integer.valueOf(data[2].trim());
                this.manage.add(reviewers, movie, score);
            }
            else if (line.startsWith("print hashtable")) {
                String name = line.replaceFirst("print hashtable", "").trim();
                manage.printHashTable(name);
            }
            else if (line.startsWith("print ratings")) {
                manage.printRatings();
            }
            else if (line.startsWith("list reviewer")) {
                String reviewer = line.replaceFirst("list reviewer", "").trim();
                manage.listReviewers(reviewer);
            }
            else if (line.startsWith("list movie")) {
                String movie = line.replaceFirst("list movie", "").trim();
                manage.listMovies(movie);
            }
            else if (line.startsWith("similar reviewer")) {
                String reviewer = line.replaceFirst("similar reviewer", "")
                    .trim();
                manage.similarReviewers(reviewer);
            }
            else if (line.startsWith("similar movie")) {
                String movie = line.replaceFirst("similar movie", "").trim();
                manage.similarMovie(movie);
            }
        }
        scanner.close();
    }

}
