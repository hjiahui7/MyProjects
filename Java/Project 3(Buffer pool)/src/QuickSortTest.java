
import java.io.IOException;
import student.TestCase;

/**
 * test the QuickSort class
 *
 * @author jiahui huang<hjiahui7>
 * @author Yusheng Cao<cyschris>
 * @version 2018.9.4
 */
public class QuickSortTest extends TestCase {
    private QuickSort qsf;
    private BufferPool testPool;


    /**
     * the setUp method
     * 
     * @throws IOException
     * 
     */
    public void setUp() throws IOException {
        VirtualSort.generateFile("4MA.txt", "1000", 'a');
        VirtualSort.generateFile("4MB.txt", "1000", 'b');
        testPool = new BufferPool(10, "4MA.txt");
        qsf = new QuickSort(testPool);
    }


    /**
     * test get pivot
     */
    public void testGetPivot() {
        int left = 0;
        int right = 10;
        assertEquals(5, qsf.findpivot(left, right));
    }


    /**
     * test compare
     */
    public void testCompare() {
        short left = 5;
        short same = 6;
        short right = 6;
        assertEquals(-1, qsf.compareto(left, right));
        assertEquals(0, qsf.compareto(right, same));
        assertEquals(1, qsf.compareto(right, left));
    }


    /**
     * test same file
     * 
     * @throws Exception
     */
    public void testSameFile() throws Exception {
        VirtualSort.generateFile("4MA.txt", "1000", 'a');
        CheckFile c = new CheckFile();
        testPool = new BufferPool(10, "4MA.txt");
        qsf = new QuickSort(testPool);
        qsf.startSort();
        assertTrue(c.checkFile("4MA.txt"));

        // test same file
        qsf.startSort();
        assertTrue(c.checkFile("4MA.txt"));
    }


    /**
     * the test method
     */
    public void testQuickForAscill() throws Exception {
        CheckFile c = new CheckFile();
        testPool = new BufferPool(10, "4MA.txt");
        qsf = new QuickSort(testPool);
        assertFalse(c.checkFile("4MA.txt"));
        qsf.startSort();
        assertTrue(c.checkFile("4MA.txt"));

        testPool = new BufferPool(10, "4MB.txt");
        qsf = new QuickSort(testPool);
        qsf = new QuickSort(testPool);
        qsf.startSort();
        assertTrue(c.checkFile("4MB.txt"));
    }


    /**
     * the test method
     */
    public void testQuickForBinary() throws Exception {
        VirtualSort.generateFile("4MB.txt", "1000", 'b');
        CheckFile c = new CheckFile();
        testPool = new BufferPool(10, "4MB.txt");
        qsf = new QuickSort(testPool);
        qsf.startSort();
        assertTrue(c.checkFile("4MB.txt"));
    }
}
