
import java.io.IOException;
import student.TestCase;

/**
 * test bufferpool class
 *
 * @author jiahui huang<hjiahui7>
 * @author Yusheng Cao<cyschris>
 * @version 2018.9.4
 */
public class BufferPoolTest extends TestCase {
    private BufferPool bu;


    /**
     * the setUp method
     */
    public void setUp() throws IOException {
        VirtualSort.generateFile("4M.txt", "1000", 'a');
        bu = new BufferPool(10, "4M.txt");
    }


    /**
     * the testSetDataAndGetData method
     */
    public void testSetDataAndGetData() {
        byte[] data = { 1, 2, 3, 4 };
        bu.setData(0, data);
        byte[] geting = bu.getData(0);
        for (int i = 0; i < geting.length; i++) {
            assertEquals(data[i], geting[i]);
        }
    }


    /**
     * the testTsFull method
     */
    public void testTsFull() {
        assertFalse(bu.isFull());
        assertTrue(bu.isEmpty());
        for (int i = 0, j = 0; i < 10; i++, j += 4096) {
            byte[] data = { 1, 2, 3, 4 };
            bu.setData(j, data);
        }
        assertTrue(bu.isFull());
        assertFalse(bu.isEmpty());
    }


    /**
     * the testGetBuffer method
     */
    public void testGetBuffer() {
        byte[] data = { 1, 2, 3, 4 };
        for (int i = 0, j = 0; i < 10; i++, j += 4096) {
            bu.setData(j, data);
        }

        byte[] geting = bu.getData(0);
        assertEquals(data[0], geting[0]);
        assertEquals(data[1], geting[1]);
        assertEquals(data[2], geting[2]);
        assertEquals(data[3], geting[3]);
        geting = bu.getData(4096);
        assertEquals(data[0], geting[0]);
        assertEquals(data[1], geting[1]);
        assertEquals(data[2], geting[2]);
        assertEquals(data[3], geting[3]);
        geting = bu.getData(4096 * 2);
        assertEquals(data[0], geting[0]);
        assertEquals(data[1], geting[1]);
        assertEquals(data[2], geting[2]);
        assertEquals(data[3], geting[3]);
        geting = bu.getData(4096 * 3);
        assertEquals(data[0], geting[0]);
        assertEquals(data[1], geting[1]);
        assertEquals(data[2], geting[2]);
        assertEquals(data[3], geting[3]);
    }


    /**
     * the testGetingDeskInfor method
     */
    public void testGetingDeskInfor() {
        assertEquals(0, bu.getDeskRead());
        assertEquals(0, bu.getCacheMiss());
        assertEquals(0, bu.getDeskWrite());
        assertEquals(0, bu.getCacheCount());
    }


    /**
     * test get buffer offset
     */
    public void testGetBufferOffset() {
        int bufferId = 2;
        assertEquals(8192, this.bu.getBufferOffset(bufferId));
    }

}
