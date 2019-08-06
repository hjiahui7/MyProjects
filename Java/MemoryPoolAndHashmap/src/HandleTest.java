import student.TestCase;

/**
 * the test class for Handle
 *
 * @author jiahui huang<hjiahui7>
 * @author Yusheng Cao<cyschris>
 * @version 2018.9.4
 */
public class HandleTest extends TestCase {

    /**
     * the field
     */
    private Handle handle;


    /**
     * the setup for test class
     */
    public void setUp() {
        handle = new Handle(1, 1);
    }


    /**
     * test all
     */
    public void testAll() {
        assertEquals(1, handle.getOffset());
        assertEquals(1, handle.getSize());
        handle.setOffset(2);
        assertEquals(2, handle.getOffset());
        handle.setSize(2);
        assertEquals(2, handle.getSize());
    }
}
