
/**
 * provide a basic function of a quicksort to sorting the data in the
 * buffer pool, sort the file (in ascending order).
 *
 * @author jiahui huang<hjiahui7>
 * @author Yusheng Cao<cyschris>
 * @version 2018.10.24
 */
public class QuickSort {
    /**
     * variable
     */
    private long numberOfRecord;
    private BufferPool bufferPool;


    // ----------------------------------------------------------
    /**
     * Create a new QuickSort object.
     *
     * @param bufferPool
     *            new buffer pool
     */
    public QuickSort(BufferPool bufferPool) {
        this.bufferPool = bufferPool;
        numberOfRecord = bufferPool.numberOfRecord() / 4;
    }


    /**
     * start sorting
     */
    public void startSort() {
        int left = 0;
        int right = (int)numberOfRecord - 1;
        sort(left, right);
        bufferPool.close();
    }


    /**
     * provide basic sorting
     *
     * @param leftdata
     *            left int
     * @param rightdata
     *            right int
     */
    public void sort(int leftdata, int rightdata) {
        if (leftdata < rightdata) {
            if ((rightdata - leftdata) < 9) {
                this.insertionSort(leftdata, rightdata + 1);
            }
            else {
                this.quicksort(leftdata, rightdata);
            }
        }
    }


    /**
     * check the bufferpool if everythingIsTheSame
     *
     * @param left
     *            left int
     * @param right
     *            right int
     * @return the status
     */
    private boolean everythingIsTheSame(int left, int right) {
        int firstData = getData(left).getKey();
        for (int i = left + 1; i <= right; i++) {
            if (getData(i).getKey() != firstData) {
                return false;
            }
        }
        return true;
    }


    // ----------------------------------------------------------
    /**
     * use the quick sort to start sorting
     *
     * @param left
     *            first data
     * @param right
     *            second data
     */
    public void quicksort(int left, int right) {
        int pivotindex = findpivot(left, right);
        swap(pivotindex, right);
        if (everythingIsTheSame(left, right)) {
            return;
        }
        int k = partition(left, right - 1, right);
        swap(k, right);
        if ((right - k) > 1) {
            sort(k + 1, right);
        }
        if ((k - left) > 1) {
            sort(left, k - 1);
        }
    }


    // ----------------------------------------------------------
    /**
     * locate the pivot point
     *
     * @param left
     *            left data
     * @param right
     *            right data
     * @return pivot point location
     */
    public int findpivot(int left, int right) {
        // return new Random().nextInt((right - left) + 1) + left;
        return (right + left) / 2;
    }


    // ----------------------------------------------------------
    /**
     * partition the array
     *
     * @param left
     *            left data
     * @param right
     *            right data
     * @param pivot
     *            pivot point
     * @return the new left data
     */
    public int partition(int left, int right, int pivot) {
        short pivotData = this.getData(pivot).getKey();
        while (left <= right) {
            while (this.compareto(this.getData(left).getKey(), pivotData) < 0) {
                left++;
            }
            while ((right >= left) && (compareto(this.getData(right).getKey(),
                pivotData) >= 0)) {
                right--;
            }
            if (right > left) {
                swap(left, right);
            }
        }
        return left;
    }


    // ----------------------------------------------------------
    /**
     * swap the two daat
     *
     * @param index1
     *            index of data1
     * @param index2
     *            index of data2
     */
    public void swap(int index1, int index2) {
        Record<Short, byte[]> data1 = getData(index1);
        Record<Short, byte[]> data2 = getData(index2);
        this.setData(index2, data1);
        this.setData(index1, data2);
    }


    // ----------------------------------------------------------
    /**
     * perform a insertion sort
     *
     * @param left
     *            left data location
     * @param right
     *            right data location
     */
    public void insertionSort(int left, int right) {
        for (int i = left + 1; i < right; i++) {
            for (int j = i; (j > 0) && (compareto(getData(j).getKey(), getData(j
                - 1).getKey()) < 0); j--) {
                swap(j, j - 1);
            }
        }
    }


    // ----------------------------------------------------------
    /**
     * set the new data
     *
     * @param index1
     *            new index
     * @param data
     *            new data
     */
    public void setData(int index1, Record<Short, byte[]> data) {
        byte[] buffer = new byte[4];
        buffer[1] = (byte)(data.key & 0xFF);
        buffer[0] = (byte)((data.key >> 8) & 0xFF);
        buffer[2] = data.value[0];
        buffer[3] = data.value[1];
        bufferPool.setData(index1, buffer);
    }


    /**
     * get the data
     *
     * @param index
     *            the index which locate the data place
     * @return the data
     */
    public Record<Short, byte[]> getData(int index) {
        byte[] data = bufferPool.getData(index);
        short key = (short)((data[0] & 0xFF) << 8 | data[1] & 0xFF);
        byte[] value = { data[2], data[3] };
        Record<Short, byte[]> record = new Record<Short, byte[]>(key, value);
        return record;
    }


    // ----------------------------------------------------------
    /**
     * compare the data
     *
     * @param data1
     *            first data
     * @param data2
     *            second data
     * @return the difference
     */
    public int compareto(short data1, short data2) {
        return data1 - data2;
    }


    /**
     * the record class for quick sort class
     *
     * @author jiahui huang<hjiahui7>
     * @author Yusheng Cao<cyschris>
     * @version 2018.10.24
     * @param <K>
     *            the key
     * @param <V>
     *            the value
     */
    class Record<K, V> {
        /**
         * the field
         */
        private K key;
        private V value;


        /**
         * the constructor for record
         *
         * @param key
         *            the key
         * @param value
         *            the value
         */
        public Record(K key, V value) {
            this.key = key;
            this.value = value;
        }


        /**
         * get the key
         *
         * @return the key value
         */
        public K getKey() {
            return key;
        }

    }
}
