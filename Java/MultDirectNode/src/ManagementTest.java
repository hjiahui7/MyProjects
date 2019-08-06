import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import student.TestCase;

/**
 * test class for review managemment method
 *
 * @author jiahui huang<hjiahui7>
 * @author Yusheng Cao<cyschris>
 * @version 2018.9.4
 */
public class ManagementTest extends TestCase {

    private Management test;


    /**
     * set up the test
     */
    public void setUp() {
        test = new Management(10);
    }


    /**
     * test add and delete methods
     */
    public void testAdddelete() {

        assertFalse(test.add("rew1", "mov1", 100));
        assertFalse(test.add("rew1", "mov1", -1));
        assertTrue(test.add("rew1", "mov1", 1));
        assertTrue(test.add("rew1", "mov2", 2));
        assertTrue(test.add("rew2", "mov2", 1));

        assertTrue(test.deleteReviewer("rew1"));
        assertFalse(test.deleteReviewer("rew1"));
        assertTrue(test.deleteMovie("mov1"));
        assertFalse(test.deleteMovie("mov1"));
        systemOut().clearHistory();
    }


    /**
     * test list methods
     */
    public void testList() {
        assertTrue(test.add("rew1", "mov1", 1));
        assertTrue(test.add("rew1", "mov2", 2));
        assertTrue(test.add("rew2", "mov1", 1));
        assertTrue(test.add("rew2", "mov2", 2));
        systemOut().clearHistory();
    }


    /**
     * test printHashTable
     */
    public void testPrintHashTable() {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        test.printHashTable("movie");
        assertEquals(outContent.toString(), "Movies:\r\n"
            + "Total records: 0\r\n");
        test.printHashTable("reviewer");
        assertEquals(outContent.toString(), "Movies:\r\n"
            + "Total records: 0\r\n" + "Reviewers:\r\n" + "Total records: 0\r\n"
            + "");
        systemOut().clearHistory();
    }


    /**
     * test print rates
     */
    public void testPrintRatings() {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        // no data
        test.printRatings();
        assertEquals(outContent.toString(),
            "There are no ratings in the database\r\n" + "");
        // no reviewer
        assertTrue(test.add("rew1", "mov1", 1));
        assertTrue(test.add("rew1", "mov2", 2));
        assertTrue(test.add("rew2", "mov1", 1));
        assertTrue(test.add("rew2", "mov2", 2));
        test.printRatings();
        assertEquals(outContent.toString(),
            "There are no ratings in the database\r\n"
                + "Rating added: |rew1|, |mov1|, 1\r\n"
                + "Rating added: |rew1|, |mov2|, 2\r\n"
                + "Rating added: |rew2|, |mov1|, 1\r\n"
                + "Rating added: |rew2|, |mov2|, 2\r\n" + "rew1: 0\r\n"
                + "rew2: 1\r\n" + "mov1: 0:1 1:1\r\n" + "mov2: 0:2 1:2\r\n"
                + "");
        // no movie
        test.deleteMovie("mov1");
        test.deleteMovie("mov2");
        test.printRatings();
        assertEquals(outContent.toString(),
            "There are no ratings in the database\r\n"
                + "Rating added: |rew1|, |mov1|, 1\r\n"
                + "Rating added: |rew1|, |mov2|, 2\r\n"
                + "Rating added: |rew2|, |mov1|, 1\r\n"
                + "Rating added: |rew2|, |mov2|, 2\r\n" + "rew1: 0\r\n"
                + "rew2: 1\r\n" + "mov1: 0:1 1:1\r\n" + "mov2: 0:2 1:2\r\n"
                + "|mov1| has been deleted from the Movie database.\r\n"
                + "|mov2| has been deleted from the Movie database.\r\n"
                + "There are no ratings in the database\r\n");
        systemOut().clearHistory();
    }


    /**
     * test list movies
     */
    public void testListMovies() {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        test.listMovies("mov1");
        assertEquals(outContent.toString(),
            "Cannot list, movie |mov1| not found in the database.\r\n");
        assertTrue(test.add("rew1", "mov1", 1));
        test.listMovies("mov1");
        assertEquals(outContent.toString(),
            "Cannot list, movie |mov1| not found in the database.\r\n"
                + "Rating added: |rew1|, |mov1|, 1\r\n"
                + "Ratings for movie |mov1|:\r\n" + "rew1: 1\r\n" + "");
        systemOut().clearHistory();

    }


    /**
     * test list reviewers
     */
    public void testlistReviewers() {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        test.listReviewers("rew1");
        assertEquals(outContent.toString(),
            "Cannot list, reviewer |rew1| not found in the database.\r\n");
        assertTrue(test.add("rew1", "mov1", 1));
        test.listReviewers("rew1");
        assertEquals(outContent.toString(),
            "Cannot list, reviewer |rew1| not found in the database.\r\n"
                + "Rating added: |rew1|, |mov1|, 1\r\n"
                + "Ratings for reviewer |rew1|:\r\n" + "mov1: 1\r\n" + "");
        systemOut().clearHistory();
    }


    /**
     * test similarMovie
     */
    public void testsimilarMovei() {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        test.similarMovie("mov1");
        assertEquals(outContent.toString(),
            "Movie |mov1| not found in the database.\r\n" + "");
        // orgional movie data
        assertTrue(test.add("rew1", "mov1", 3));
        test.similarMovie("mov1");
        assertEquals(outContent.toString(),
            "Movie |mov1| not found in the database.\r\n"
                + "Rating added: |rew1|, |mov1|, 3\r\n"
                + "There is no movie similar to |mov1|\r\n" + "");
        // different reviewer same movie lower score
        assertTrue(test.add("rew2", "mov1", 2));
        test.similarMovie("mov1");
        assertEquals(outContent.toString(),
            "Movie |mov1| not found in the database.\r\n"
                + "Rating added: |rew1|, |mov1|, 3\r\n"
                + "There is no movie similar to |mov1|\r\n"
                + "Rating added: |rew2|, |mov1|, 2\r\n"
                + "There is no movie similar to |mov1|\r\n" + "");

        ByteArrayOutputStream outContent1 = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent1));
        // same reviewer different movie same score
        assertTrue(test.add("rew1", "mov2", 3));
        test.similarMovie("mov1");
        assertEquals(outContent1.toString(),
            "Rating added: |rew1|, |mov2|, 3\r\n"
                + "The movie |mov2| is similar to |mov1| with score 0.00\r\n"
                + "");

        // same reviewer different movie higher score
        outContent1 = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent1));
        assertTrue(test.add("rew1", "mov2", 4));
        test.similarMovie("mov1");
        assertEquals(outContent1.toString(),
            "Rating added: |rew1|, |mov2|, 4\r\n"
                + "The movie |mov2| is similar to |mov1| with score 1.00\r\n"
                + "");

        // different rev different movie higher score
        outContent1 = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent1));
        assertTrue(test.add("rew3", "mov2", 4));
        test.similarMovie("mov1");
        assertEquals(outContent1.toString(),
            "Rating added: |rew3|, |mov2|, 4\r\n"
                + "The movie |mov2| is similar to |mov1| with score 1.00\r\n");

        // different rev different movie lower score
        outContent1 = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent1));
        assertTrue(test.add("rew3", "mov3", 2));
        test.similarMovie("mov1");
        assertEquals(outContent1.toString(),
            "Rating added: |rew3|, |mov3|, 2\r\n"
                + "The movie |mov2| is similar to |mov1| with score 1.00\r\n");

        // different rev different movie lower score
        outContent1 = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent1));
        assertTrue(test.add("rew4", "mov4", 2));
        test.similarMovie("mov2");
        assertEquals(outContent1.toString(),
            "Rating added: |rew4|, |mov4|, 2\r\n"
                + "The movie |mov1| is similar to |mov2| with score 1.00\r\n"
                + "");
        systemOut().clearHistory();
    }


    /**
     * the test method for similarReviewers
     */
    public void testsimilarReviewers() {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        test.similarReviewers("rew2");
        assertEquals(outContent.toString(),
            "Reviewer |rew2| not found in the database.\r\n");
        // basic
        outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        assertTrue(test.add("rew2", "mov2", 2));
        test.similarReviewers("rew2");
        assertEquals(outContent.toString(),
            "Rating added: |rew2|, |mov2|, 2\r\n"
                + "There is no reviewer similar to |rew2|\r\n" + "");
        // different mov
        outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        assertTrue(test.add("rew2", "mov3", 2));
        test.similarReviewers("rew2");
        assertEquals(outContent.toString(),
            "Rating added: |rew2|, |mov3|, 2\r\n"
                + "There is no reviewer similar to |rew2|\r\n" + "");
        // different rev
        outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        assertTrue(test.add("rew1", "mov2", 2));
        test.similarReviewers("rew2");
        assertEquals(outContent.toString(),
            "Rating added: |rew1|, |mov2|, 2\r\n"
                + "The reviewer |rew1| is similar to |rew2| with score 0.00\r\n"
                + "");
        // different rev
        outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        assertTrue(test.add("rew1", "mov2", 3));
        test.similarReviewers("rew2");
        assertEquals(outContent.toString(),
            "Rating added: |rew1|, |mov2|, 3\r\n"
                + "The reviewer |rew1| is similar to |rew2| with score 1.00\r\n"
                + "");

        // different rev
        outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        assertTrue(test.add("rew3", "mov2", 3));
        assertTrue(test.add("rew3", "mov1", 3));
        test.similarReviewers("rew2");
        assertEquals(outContent.toString(),
            "Rating added: |rew3|, |mov2|, 3\r\n"
                + "Rating added: |rew3|, |mov1|, 3\r\n"
                + "The reviewer |rew1| is similar to |rew2| with score 1.00\r\n"
                + "");

        // different rev
        outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        assertTrue(test.add("rew1", "mov1", 3));
        test.similarReviewers("rew1");
        assertEquals(outContent.toString(),
            "Rating added: |rew1|, |mov1|, 3\r\n"
                + "The reviewer |rew3| is similar to |rew1| with score 0.00\r\n"
                + "");
        systemOut().clearHistory();
    }


    /**
     * test similarReviewers2
     */
    public void testsimilarReviewers2() {
        test.add("R1", "M1", 1);
        assertTrue(test.deleteReviewer("R1"));
        test.printRatings();
        assertTrue(test.deleteMovie("M1"));
        test.printRatings();
        test.add("R1", "M1", 1);
        test.add("R2", "M2", 1);
        assertTrue(test.deleteReviewer("R1"));
        test.similarMovie("M2");
        assertTrue(test.deleteReviewer("R2"));
        assertTrue(test.deleteMovie("M1"));
        assertTrue(test.deleteMovie("M2"));
        test.add("R1", "M1", 1);
        test.add("R2", "M1", 1);
        test.add("R1", "M2", 1);
        test.add("R2", "M2", 1);
        systemOut().clearHistory();
        test.similarReviewers("R2");
        assertEquals("The reviewer |R1| is similar to |R2| with score 0.00\r\n",
            systemOut().getHistory());
    }


    /**
     * test similarReviewers3
     */
    public void testsimilarReviewers3() {
        test.add("R1", "M1", 1);
        test.add("R2", "M2", 1);
        assertTrue(test.deleteMovie("M1"));
        systemOut().clearHistory();
        test.similarReviewers("R2");
        assertEquals("There is no reviewer similar to |R2|\r\n" + "",
            systemOut().getHistory());
        systemOut().clearHistory();
    }


    /**
     * test similarReviewers3
     */
    public void testRating2() {
        test.add("R1", "M1", 1);
        test.add("R2", "M2", 1);
        assertTrue(test.deleteMovie("M1"));
        test.listReviewers("R1");
        assertTrue(test.deleteReviewer("R1"));
        test.add("R1", "M1", 1);
        test.add("R2", "M2", 1);
        assertTrue(test.deleteReviewer("R1"));
        systemOut().clearHistory();
        test.listMovies("M1");
        assertEquals("Ratings for movie |M1|:\r\n", systemOut().getHistory());
        systemOut().clearHistory();
    }

}
