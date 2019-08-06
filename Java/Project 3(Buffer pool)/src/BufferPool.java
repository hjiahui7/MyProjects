
import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * create a bufferpool object. All accesses
 * to the file will be mediated by this object
 *
 * @author jiahui huang<hjiahui7>
 * @author Yusheng Cao<cyschris>
 * @version 2018.10.24
 */
public class BufferPool {
    /**
     * the total number we store
     */
    public static final int TOTAL_STORE = 1024;
    /**
     * each record size
     */
    public static final int EACH_RECORD = 4;
    /**
     * the buffer size
     */
    public static final int BUFFER_SIZE = 4096;
    private RandomAccess randomAccess;
    private DLList<Buffer> bufferQueue;
    private int capacity;
    private int deskWrite;
    private int deskRead;
    private int cacheCount;
    private int cacheMiss;


    // ----------------------------------------------------------
    /**
     * Create a new BufferPool object.
     *
     * @param numberOfBuffer
     *            number of buffer
     * @param fileName
     *            open file
     * @throws IOException
     */
    public BufferPool(int numberOfBuffer, String fileName) throws IOException {
        randomAccess = new RandomAccess(fileName);
        this.capacity = numberOfBuffer;
        bufferQueue = new DLList<Buffer>();
        deskWrite = 0;
        deskRead = 0;
        cacheCount = 0;
        cacheMiss = 0;
    }


    // ----------------------------------------------------------
    /**
     * get data according to the index
     *
     * @param index
     *            getdata according to the index
     * @return byte array which contain data
     */
    public byte[] getData(int index) {
        Buffer buffer = this.getBuffer(index);
        byte[] n = buffer.getData(index);
        return n;
    }


    // ----------------------------------------------------------
    /**
     * change the data according to the data
     *
     * @param index
     *            index which indecate the data place
     * @param data
     *            array which contain new data
     */
    public void setData(int index, byte[] data) {
        Buffer buffer = this.getBuffer(index);
        buffer.setData(data, index);
    }


    // ----------------------------------------------------------
    /**
     * check the bufferpool is full or not
     *
     * @return bufferpool status
     */
    public boolean isFull() {
        return this.bufferQueue.size() >= this.capacity;
    }


    // ----------------------------------------------------------
    /**
     * check the bufferpool is empty or not
     *
     * @return bufferpool status
     */
    public boolean isEmpty() {
        return this.bufferQueue.isEmpty();
    }


    // ----------------------------------------------------------
    /**
     * clear the bufferpool
     */
    public void close() {
        Iterator<Buffer> ite = bufferQueue.iterator();
        while (ite.hasNext()) {
            Buffer buffer = ite.next();
            if (buffer.isDirtyBit()) {
                this.writeByteData(buffer);
                deskWrite++;
            }
        }
    }


    /**
     * get the buffer according to the index
     *
     * @param index
     *            number which indecate the buffer location
     * @return the buffer
     */
    private Buffer getBuffer(int index) {
        int bufferId = this.getBufferId(index);
        Buffer buffer = getBufferById(bufferId);
        if (buffer == null) {
            buffer = addBuffer(bufferId);
            deskRead++;
            cacheMiss++;
        }
        else {
            if (this.bufferQueue.getFirst() != buffer) {
                this.bufferQueue.remove(buffer);
                this.bufferQueue.add(0, buffer);
                cacheCount++;
            }
        }
        return buffer;
    }


    /**
     * get the buffer according to the bufferid
     *
     * @param bufferId
     *            id which indecate the buffer location
     * @return the buffer
     */
    private Buffer getBufferById(int bufferId) {
        if (!isEmpty()) {
            Iterator<Buffer> ite = bufferQueue.iterator();
            while (ite.hasNext()) {
                Buffer buffer = ite.next();
                if (buffer.getId() == bufferId) {
                    return buffer;
                }
            }
        }
        return null;
    }


    /**
     * use index to calculate the buffer id
     *
     * @param index
     *            the buffer index
     * @return the id
     */
    public int getBufferId(int index) {
        return index / TOTAL_STORE;
    }


    /**
     * add new buffer to the pool according to the buffer id
     *
     * @param bufferId
     *            the bufferId
     * @return the added buffer
     */
    private Buffer addBuffer(int bufferId) {
        if (this.isFull()) {
            this.releaseLastBuffer();
        }
        Buffer buffer = new Buffer(this.randomAccess.readData(this
            .getBufferOffset(bufferId), BUFFER_SIZE), bufferId);
        this.bufferQueue.add(0, buffer);
        return buffer;
    }


    /**
     * clear the last buffer
     */
    private void releaseLastBuffer() {
        Buffer last = this.bufferQueue.removeLast();
        if (last.isDirtyBit()) {
            this.writeByteData(last);
            deskWrite++;
        }
    }


    /**
     * calculate the buffer offset
     *
     * @param bufferId
     *            buffer id
     * @return the offset
     */
    public int getBufferOffset(int bufferId) {
        return bufferId * BUFFER_SIZE;
    }


    // ----------------------------------------------------------
    /**
     * get teh number of record
     *
     * @return number of record
     */
    public long numberOfRecord() {
        long size = randomAccess.getDataLength();
        return size;
    }


    /**
     * use buffer to write data
     *
     * @param buffer
     *            the buffer contain data
     */
    private void writeByteData(Buffer buffer) {
        randomAccess.writeData(buffer.getRecordArray(), buffer.getId()
            * BUFFER_SIZE);
    }


    // ----------------------------------------------------------
    /**
     * get the desk write
     *
     * @return DeskWrite
     */
    public int getDeskWrite() {
        return deskWrite;
    }


    // ----------------------------------------------------------
    /**
     * get the cache count
     *
     * @return CacheCount
     */
    public int getCacheCount() {
        return cacheCount;
    }


    // ----------------------------------------------------------
    /**
     * get number of cache misss
     *
     * @return CacheMiss
     */
    public int getCacheMiss() {
        return cacheMiss;
    }


    // ----------------------------------------------------------
    /**
     * get number of desk read
     *
     * @return DeskRead
     */
    public int getDeskRead() {
        return deskRead;
    }


    // ----------------------------------------------------------
    /**
     * get the block size
     *
     * @return BlockSize
     */
    public int getBlockSize() {
        return (int)(numberOfRecord() / BUFFER_SIZE);
    }


    /**
     * create a buffer object
     *
     * @author jiahui huang<hjiahui7>
     * @author Yusheng Cao<cyschris>
     * @version 2018.10.24
     */
    public class Buffer {

        private byte[] recordArray;
        private boolean dirtyBit;
        private int id;
        private int maxCount;


        // ----------------------------------------------------------
        /**
         * Create a new Buffer object.
         *
         * @param datas
         *            a byte array
         * @param id
         *            id of the buffer
         */
        public Buffer(byte[] datas, int id) {
            dirtyBit = false;
            recordArray = datas;
            this.id = id;
            this.maxCount = this.recordArray.length;
        }


        // ----------------------------------------------------------
        /**
         * return the value of id
         *
         * @return id value
         */
        public int getId() {
            return id;
        }


        // ----------------------------------------------------------
        /**
         * get the record array.
         *
         * @return the sotred array
         */
        public byte[] getRecordArray() {
            return recordArray;
        }


        // ----------------------------------------------------------
        /**
         * check the dirgy bit status
         *
         * @return status
         */
        public boolean isDirtyBit() {
            return dirtyBit;
        }


        // ----------------------------------------------------------
        /**
         * change dirty bit status
         *
         * @param dirtyBit
         *            new status
         */
        public void setDirtyBit(boolean dirtyBit) {
            this.dirtyBit = dirtyBit;
        }


        // ----------------------------------------------------------
        /**
         * change data in the record array by using the index
         *
         * @param data
         *            new data array
         * @param index
         *            index
         */
        public void setData(byte[] data, int index) {
            index = (index * BufferPool.EACH_RECORD) % this.maxCount;
            for (int i = 0; i < BufferPool.EACH_RECORD; i++) {
                recordArray[i + index] = data[i];
            }
            this.setDirtyBit(true);
        }


        // ----------------------------------------------------------
        /**
         * get data according to the index
         *
         * @param index
         *            index which locate the
         * @return data in the buffer
         */
        public byte[] getData(int index) {
            index = (index * BufferPool.EACH_RECORD) % this.maxCount;
            byte[] result = { recordArray[index], recordArray[index + 1],
                recordArray[index + 2], recordArray[index + 3] };
            return result;
        }
    }


    /**
     * create a random access file class to access the file
     *
     * @author jiahui huang<hjiahui7>
     * @author Yusheng Cao<cyschris>
     * @version 2018.10.24
     */
    class RandomAccess {
        private RandomAccessFile writer;
        private long dataLength;


        // ----------------------------------------------------------
        /**
         * Create a new RandomAccess object.
         *
         * @param fileName
         *            the open file
         * @throws IOException
         */
        public RandomAccess(String fileName) throws IOException {
            writer = new RandomAccessFile(fileName, "rw");
            dataLength = writer.length();
        }


        // ----------------------------------------------------------
        /**
         * get the lenght of data
         *
         * @return DataLength
         */
        public long getDataLength() {
            return dataLength;
        }


        // ----------------------------------------------------------
        /**
         * read data and output the data
         *
         * @param pos
         *            the position at beginning
         * @param size
         *            size of the data
         * @return array which contain the data
         */
        public byte[] readData(int pos, int size) {
            byte[] buf = new byte[size];
            try {
                writer.seek(pos);
                writer.read(buf);
            }
            catch (IOException e) {
                e.printStackTrace();
            }
            return buf;
        }


        // ----------------------------------------------------------
        /**
         * write data into the bufferpool
         *
         * @param data
         *            new data
         * @param pos
         *            start place
         */
        public void writeData(byte[] data, int pos) {
            try {
                writer.seek(pos);
                writer.write(data);
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
