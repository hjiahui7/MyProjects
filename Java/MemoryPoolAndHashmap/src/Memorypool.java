/**
 * create a array to hold the data work as a memory pool
 *
 * @author jiahui huang<hjiahui7>
 * @author Yusheng Cao<cyschris>
 * @version 2018.9.4
 */
public class Memorypool {

    /**
     * the field
     */
    private byte[] memory;
    private int size;


    /**
     * create new memory pool
     * 
     * @param size
     *            size of the memory pool
     */
    public Memorypool(int size) {
        memory = new byte[size];
        this.size = size;
    }


    /**
     * get the size of the memory
     * 
     * @return the size
     */
    public int getSize() {
        return size;
    }


    /**
     * change the size of the memory pool
     * 
     * @param size
     *            new size
     */
    public void setSize(int size) {
        this.size = size;
    }


    /**
     * expend the pool by double it size
     */
    public void expandThePool() {
        byte[] newPool = new byte[size * 2];
        for (int i = 0; i < size; i++) {
            newPool[i] = memory[i];
        }
        memory = newPool;
        size *= 2;
        StringBuilder str = new StringBuilder();
        str.append("Memory pool expanded to be " + size + " bytes.");
        System.out.println(str);
    }


    /**
     * insert the data in the memory pool
     * 
     * @param data
     *            new data
     * @param offset
     *            location of the pool
     */
    public void insert(byte[] data, int offset) {
        for (int i = 0; i < data.length; i++) {
            memory[i + offset] = data[i];
        }
    }


    /**
     * get the data based on the offset
     * 
     * @param length
     *            size of the location
     * @param offset
     *            location of the array
     * @param array
     *            array which contain the data
     * @return the array which contain the data
     */
    public byte[] get(int length, int offset, byte[] array) {
        for (int i = 0; i < length; i++) {
            array[i] = memory[offset + i];
        }
        return array;
    }
}
