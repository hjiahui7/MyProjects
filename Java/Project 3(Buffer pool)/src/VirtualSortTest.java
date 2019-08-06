import student.TestCase;

/**
 * test the virtual sort class
 * 
 * @author CS3114 staff
 * @author jiahui huang<hjiahui7>
 * @author Yusheng Cao<cyschris>
 * @version 2018.10.29
 */

public class VirtualSortTest extends TestCase {
    private CheckFile fileChecker;


    /**
     * This method sets up the tests that follow.
     */
    public void setUp() {
        new VirtualSort();
        fileChecker = new CheckFile();
    }


    /**
     * This method tests the main functionality of Quicksort on an "ascii" file
     *
     * @throws Exception
     *             either a IOException or FileNotFoundException
     */
    public void testQuicksortAscii() throws Exception {

        VirtualSort.generateFile("input.txt", "10", 'a');
        VirtualSort.generateFile("input2.txt", "10", 'b');

        String[] args = new String[3];
        args[0] = "input.txt";
        args[1] = "1";
        args[2] = "statFileA.txt";
        VirtualSort.main(args);
        assertTrue(fileChecker.checkFile("input.txt"));

        args[0] = "input2.txt";
        args[1] = "1";
        args[2] = "statFileB.txt";
        VirtualSort.main(args);
        assertTrue(fileChecker.checkFile("input2.txt"));
    }

}
