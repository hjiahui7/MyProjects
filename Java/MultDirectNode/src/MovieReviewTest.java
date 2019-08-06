import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import student.TestCase;

/**
 * test the movie review class
 *
 * @author jiahui huang<hjiahui7>
 * @author Yusheng Cao<cyschris>
 * @version 2018.9.20
 */
public class MovieReviewTest extends TestCase {

    /**
     * Sets up the tests that follow. In general, used for initialization
     */
    public void setUp() {
        // Nothing Here
    }


    /**
     * test the entire class
     */
    @SuppressWarnings("static-access")
    public void testClass() {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        String[] arg = new String[3];
        arg[0] = "6";
        arg[1] = "P2SampleInput.txt";
        MovieReview test = new MovieReview();
        try {
            test.main(arg);
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        assertEquals(outContent.toString(),
            "|Darth Vader| not deleted because it does"
                + " not exist in the Reviewer database.\r\n"
                + "|Death Note| not deleted because it"
                + " does not exist in the Movie database.\r\n" + "Movies:\r\n"
                + "Total records: 0\r\n" + "Reviewers:\r\n"
                + "Total records: 0\r\n"
                + "There are no ratings in the database\r\n"
                + "Cannot list, reviewer |Darth Vader|"
                + " not found in the database.\r\n"
                + "Cannot list, movie |Death Note| not"
                + " found in the database.\r\n"
                + "Reviewer |Darth Vader| not found in the database.\r\n"
                + "Movie |Death Note| not found in the database.\r\n"
                + "Rating added: |Hayao Miyazaki|, |Spirited Away|, 10\r\n"
                + "Rating added: |Dr. Shaffer|, |Spirited Away|, 10\r\n"
                + "|Spirited Away| has been deleted"
                + " from the Movie database.\r\n"
                + "There are no ratings in the database\r\n"
                + "Rating added: |Dr. Shaffer|, |Spirited Away|, 10\r\n"
                + "Hayao Miyazaki: 0\r\n" + "Dr. Shaffer: 1\r\n"
                + "Spirited Away: 1:10\r\n" + "|Dr. Shaffer| has been deleted "
                + "from the Reviewer database.\r\n" + "Hayao Miyazaki: 0\r\n"
                + "Spirited Away: \r\n" + "|Hayao Miyazaki| has been deleted"
                + " from the Reviewer database.\r\n"
                + "|Spirited Away| has been "
                + "deleted from the Movie database.\r\n"
                + "Rating added: |John Doe|, " + "|Spirited Away|, 9\r\n"
                + "|John Doe| not deleted because"
                + " it does not exist in the Movie database.\r\n"
                + "|John Doe| has been deleted from the Reviewer database.\r\n"
                + "|Spirited Away| has been "
                + "deleted from the Movie database.\r\n"
                + "Bad score |0|. Scores must be between 1 and 10.\r\n"
                + "Rating added: |Darth Vader|, |Death Note|, 1\r\n"
                + "|Darth Vader| has been deleted"
                + " from the Reviewer database.\r\n"
                + "There are no ratings in the database\r\n"
                + "Rating added: |Darth Vader|, |Death Note|, 5\r\n"
                + "Rating added: |Darth Vader|, |Death Note|, 1\r\n"
                + "There is no reviewer similar to |Darth Vader|\r\n"
                + "There is no movie similar to |Death Note|\r\n"
                + "|Death Note| has been deleted from the Movie database.\r\n"
                + "Ratings for reviewer |Darth Vader|:\r\n"
                + "Rating added: |Darth Vader|, |Death Note|, 1\r\n"
                + "Rating added: |David Lynch|, |Twin Peaks Returns|, 10\r\n"
                + "Darth Vader: 4\r\n" + "David Lynch: 5\r\n"
                + "Death Note: 4:1\r\n" + "Twin Peaks Returns: 5:10\r\n"
                + "Movies:\r\n" + "|Death Note| 0\r\n"
                + "|Twin Peaks Returns| 1\r\n" + "Total records: 2\r\n"
                + "Reviewers:\r\n" + "|Darth Vader| 0\r\n"
                + "|David Lynch| 4\r\n" + "Total records: 2\r\n"
                + "There is no reviewer similar to |Darth Vader|\r\n"
                + "There is no movie similar to |Death Note|\r\n"
                + "Rating added: |David Lynch|, |Death Note|, 2\r\n"
                + "Rating added: |Dr. Shaffer|, |Batman Begins|, 10\r\n"
                + "Rating added: |Darth Vader|, |Twin Peaks Returns|, 1\r\n"
                + "Darth Vader: 4\r\n" + "David Lynch: 5\r\n"
                + "Dr. Shaffer: 6\r\n" + "Death Note: 4:1 5:2\r\n"
                + "Twin Peaks Returns: 4:1 5:10\r\n" + "Batman Begins: 6:10\r\n"
                + "Reviewer hash table size doubled to 12 slots.\r\n"
                + "Movie hash table size doubled to 12 slots.\r\n"
                + "Rating added: |Sergio Leone|, "
                + "|The Good, the Bad and the Ugly|, 10\r\n"
                + "Rating added: |Darth Vader|, "
                + "|The Good, the Bad and the Ugly|, 1\r\n"
                + "Rating added: |David Lynch|, "
                + "|The Good, the Bad and the Ugly|, 1\r\n"
                + "Ratings for reviewer |David Lynch|:\r\n"
                + "Death Note: 2\r\n" + "Twin Peaks Returns: 10\r\n"
                + "The Good, the Bad and the Ugly: 1\r\n"
                + "Ratings for reviewer |Darth Vader|:\r\n"
                + "Death Note: 1\r\n" + "Twin Peaks Returns: 1\r\n"
                + "The Good, the Bad and the Ugly: 1\r\n"
                + "The reviewer |David Lynch| "
                + "is similar to |Darth Vader| with score 3.33\r\n"
                + "Ratings for movie |The Good, the Bad and the Ugly|:\r\n"
                + "Darth Vader: 1\r\n" + "David Lynch: 1\r\n"
                + "Sergio Leone: 10\r\n" + "The movie |Death Note| "
                + "is similar to |"
                + "The Good, the Bad and the Ugly| with score 0.50\r\n"
                + "The movie |Death Note| " + "is similar to |"
                + "The Good, the Bad and the Ugly| with score 0.50\r\n"
                + "The movie |Death Note|"
                + " is similar to |The Good, the Bad and the Ugly|"
                + " with score 0.50\r\n" + "The movie |Death Note|"
                + " is similar to |The Good, the Bad and the Ugly|"
                + " with score 0.50\r\n" + "The movie |Death Note|"
                + " is similar to |The Good, the Bad and the Ugly|"
                + " with score 0.50\r\n" + "The movie |Death Note|"
                + " is similar to |The Good, the Bad and the Ugly|"
                + " with score 0.50\r\n" + "");
    }

}
