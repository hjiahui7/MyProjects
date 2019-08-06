import student.TestCase;

/**
 * test the memory manager class
 *
 * @author jiahui huang<hjiahui7>
 * @author Yusheng Cao<cyschris>
 * @version 2018.9.4
 */
public class MemManagerTest extends TestCase {

    /**
     * the field
     */
    private MemManager mem;


    /**
     * set up the test
     */
    public void setUp() {
        mem = new MemManager(16);
    }


    /**
     * test the insert method
     */
    public void testInsert() {
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

    }


    /**
     * test the remove method
     */
    public void testRemove() {
        byte[] testByte = new byte[10];
        for (int i = 0; i < 10; i++) {
            testByte[i] = 10;
        }
        Handle result1 = mem.insert(testByte, 10);
        Handle result2 = mem.insert(testByte, 10);
        Handle result3 = mem.insert(testByte, 10);
        Handle result4 = mem.insert(testByte, 10);
        Handle result5 = mem.insert(testByte, 64);
        assertEquals(mem.getSize(), 128);
        assertEquals(result1.getOffset(), 0);
        mem.remove(result2);
        assertEquals(result2.getOffset(), 16);
        assertEquals(mem.getSize(), 112);
        mem.remove(result4);
        assertEquals(mem.getSize(), 96);
        mem.remove(result1);
        assertEquals(mem.getSize(), 80);
        mem.remove(result3);
        assertEquals(mem.getSize(), 64);
        mem.remove(result5);
        assertEquals(mem.getSize(), 0);
        mem.remove(new Handle(24, 8));
    }


    /**
     * test the find space method
     */
    public void testFindSpace() {
        assertEquals(1, mem.findSpace(1));
        assertEquals(4, mem.findSpace(3));
        assertEquals(8, mem.findSpace(6));
        assertEquals(16, mem.findSpace(15));
        assertEquals(32, mem.findSpace(31));
        assertEquals(64, mem.findSpace(55));
        assertEquals(128, mem.findSpace(72));
        assertEquals(256, mem.findSpace(129));
        assertEquals(512, mem.findSpace(299));
        assertEquals(1024, mem.findSpace(678));
        assertEquals(1024, mem.findSpace(1024));
        assertEquals(4096, mem.findSpace(2056));
    }


    /**
     * test get method
     */
    public void testGet() {
        byte[] testByte = new byte[10];
        for (int i = 0; i < 10; i++) {
            testByte[i] = 10;
        }
        Handle result1 = mem.insert(testByte, 0);
        byte[] temp = new byte[10];
        assertEquals(10, mem.get(temp, result1, 10));
        for (int i = 0; i < temp.length; i++) {
            assertEquals(temp[i], testByte[i]);
        }
    }


    /**
     * test expand the pool
     */
    public void testExpandThePool() {
        mem.insert(new byte[128], 128);
        mem.printBlocks();
        String str = "Memory pool expanded to be 32 bytes."
            + "\nMemory pool expanded to be 64 bytes."
            + "\nMemory pool expanded to be 128 bytes."
            + "\nNo free blocks are available."
            + "\nMemory pool expanded to be 256 bytes." + "\n8: 200"
            + "\n16: 128 208" + "\n32: 224";
        mem.insert(new byte[8], 8);
        mem.insert(new byte[8], 8);
        mem.insert(new byte[8], 8);
        mem.insert(new byte[8], 8);
        mem.insert(new byte[8], 8);
        mem.insert(new byte[8], 8);
        mem.insert(new byte[8], 8);
        mem.insert(new byte[8], 8);
        mem.insert(new byte[8], 8);
        mem.remove(new Handle(128, 8));
        mem.remove(new Handle(136, 8));
        mem.printBlocks();
        assertEquals(str, systemOut().getHistory().trim());
    }


    /**
     * test Combine memory
     */
    public void testCombineMemory() {
        mem.insert(new byte[8], 8);
        mem.insert(new byte[8], 8);
        mem.insert(new byte[8], 8);
        mem.insert(new byte[8], 8);
        mem.insert(new byte[8], 8);
        mem.insert(new byte[8], 8);
        mem.insert(new byte[8], 8);
        mem.insert(new byte[8], 8);
        mem.remove(new Handle(24, 8));
        mem.remove(new Handle(32, 8));
        mem.remove(new Handle(40, 8));
        mem.remove(new Handle(48, 8));
        mem.remove(new Handle(56, 8));
        mem.remove(new Handle(16, 8));
        mem.remove(new Handle(8, 8));
        mem.remove(new Handle(0, 8));
        mem.insert(new byte[8], 8);
        mem.insert(new byte[8], 8);
        mem.insert(new byte[8], 8);
        mem.insert(new byte[8], 8);
        mem.insert(new byte[8], 8);
        mem.insert(new byte[8], 8);
        mem.insert(new byte[8], 8);
        mem.insert(new byte[8], 8);
        mem.remove(new Handle(24, 8));
        mem.remove(new Handle(56, 8));
        mem.remove(new Handle(16, 8));
        mem.remove(new Handle(0, 8));
        mem.remove(new Handle(32, 8));
        mem.remove(new Handle(40, 8));
        mem.remove(new Handle(8, 8));
        mem.remove(new Handle(48, 8));
        assertEquals(64, mem.getRemainMem());
        System.out.println("testCombineMemory");
    }

}
