import student.TestCase;

/**
 * the test class for Memorypool
 *
 * @author jiahui huang<hjiahui7>
 * @author Yusheng Cao<cyschris>
 * @version 2018.9.4
 */
public class MemorypoolTest extends TestCase {

    /**
     * the field
     */
    private Memorypool pool;
    private MemManager mem;


    /**
     * the setup
     */
    public void setUp() {
        pool = new Memorypool(10);
        mem = new MemManager(16);
    }


    /**
     * test all
     */
    public void testAll() {
        assertEquals(pool.getSize(), 10);
        pool.setSize(100);
        assertEquals(pool.getSize(), 100);
        mem.get(new byte[10], new Handle(10, 10), 0);
        byte[] testByte = new byte[10];
        for (int i = 0; i < 10; i++) {
            testByte[i] = 10;
        }
        Handle result = mem.insert(testByte, 10);
        Handle testHandle = new Handle(0, 10);
        assertEquals(result.getOffset(), testHandle.getOffset());
        assertEquals(result.getSize(), testHandle.getSize());

        result = mem.insert(testByte, 10);
        testHandle = new Handle(16, 10);
        assertEquals(result.getOffset(), testHandle.getOffset());
        assertEquals(result.getSize(), testHandle.getSize());
        // test expandThePool
        result = mem.insert(testByte, 32);
        testHandle = new Handle(32, 32);
        assertEquals(result.getOffset(), testHandle.getOffset());
        assertEquals(result.getSize(), testHandle.getSize());
        assertEquals(mem.getPool().getSize(), 64);
        // test expandThePool
        result = mem.insert(testByte, 32);
        testHandle = new Handle(64, 32);
        assertEquals(result.getOffset(), testHandle.getOffset());
        assertEquals(result.getSize(), testHandle.getSize());
        assertEquals(mem.getPool().getSize(), 128);
        // test expandThePool
        result = mem.insert(testByte, 64);
        testHandle = new Handle(128, 64);
        assertEquals(result.getOffset(), testHandle.getOffset());
        assertEquals(result.getSize(), testHandle.getSize());
        // test get method
        assertEquals(mem.getSize(), 160);
        assertEquals(mem.getPool().getSize(), 256);
        byte[] test = new byte[64];
        mem.get(test, testHandle, 64);
        assertEquals(test.length, 64);
    }
}
