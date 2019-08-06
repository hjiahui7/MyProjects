import student.TestCase;

/**
 * test class for hash method
 *
 * @author jiahui huang<hjiahui7>
 * @author Yusheng Cao<cyschris>
 * @version 2018.9.4
 */
public class HashMethodTest extends TestCase {
    /**
     * Sets up the tests that follow.
     */
    public void setUp() {
        // Nothing Here
    }


    /** 
     * Test the hash function
     */
    public void testh() {

        HashMethod myHash = new HashMethod();
        assertEquals(myHash.h("aaaabbbb", 101), 75);
        assertEquals(myHash.h("aaaabbb", 101), 1640219587 % 101);
    }
}
