/**
 * create a hashTable object
 *
 * @author jiahui huang<hjiahui7>
 * @author Yusheng Cao<cyschris>
 * @version 2018.9.4
 * @param <K>
 *            key of data
 * @param <V>
 *            value of data
 */
public class HashTable<K, V> {

    /**
     * each slot in table which store the key and value and tomb
     *
     * @author jiahui huang<hjiahui7>
     * @author Yusheng Cao<cyschris>
     *
     * @param <K>
     *            the key
     * @param <V>
     *            the value
     */
    public static class Hash<K, V> {

        /**
         * field
         */
        private V value;
        private K key;
        private boolean tomb;


        /**
         * create new hash object
         * 
         * @param key
         *            key of data
         * 
         * @param value
         *            value of data
         */
        public Hash(K key, V value) {
            this.key = key;
            this.value = value;
            tomb = false;
        }


        /**
         * return the key value
         * 
         * @return value of key
         */
        public K getKey() {
            return key;
        }


        /**
         * change the key
         * 
         * @param key
         *            new key
         */
        public void setKey(K key) {
            this.key = key;
        }


        /**
         * change the value
         * 
         * @param value
         *            new value
         */
        public void setValue(V value) {
            V change = getValue();
            change = value;
            value = change;
        }


        /**
         * get the value in this hash
         * 
         * @return value of hash
         */
        public V getValue() {
            return value;
        }

    }

    /**
     * field
     */
    private Hash<K, V>[] table;
    private int size;
    private int hashTableSize;


    /**
     * create new hashTable
     * 
     * @param hashTableSize
     *            size of the new table
     */
    @SuppressWarnings("unchecked")
    public HashTable(int hashTableSize) {
        setTable(new Hash[hashTableSize]);
        this.hashTableSize = hashTableSize;
        size = 0;
    }


    /**
     * add new hash in the hash table
     * 
     * @param key
     *            key of the hash
     * @param value
     *            value of the hash
     * @return add status
     */
    public boolean add(K key, V value) {
        int contain = this.contains(key);
        if (contain != -1) {
            return false;
        }
        ensureCapacity();
        int index = this.findSlot(key, table);
        Hash<K, V> hash = new Hash<K, V>(key, value);
        this.table[index] = hash;
        size++;
        return true;
    }


    /**
     * get the size of the hash table
     * 
     * @return hash table size
     */
    public int getSize() {
        return size;
    }


    /**
     * remove the hash from hash table based on key
     * 
     * @param key
     *            key of the hash
     * @return remove status
     */
    public boolean remove(K key) {
        int index = contains(key);
        if (index == -1) {
            return false;
        }
        table[index].setKey(null);
        table[index].setValue(null);
        table[index].tomb = true;
        size--;
        return true;
    }


    /**
     * expend the size of hash table if necessary and copy all things from old
     * one
     */
    private void ensureCapacity() {
        if (size + 1 > hashTableSize / 2) {
            hashTableSize = hashTableSize * 2;
            @SuppressWarnings("unchecked")
            Hash<K, V>[] table2 = new Hash[hashTableSize];
            for (int i = 0; i < table.length; i++) {
                if (table[i] != null
                    && !table[i].tomb) {
                    int index = this.findSlot(table[i].getKey(), table2);
                    table2[index] = table[i];
                }
            }
            table = table2;
            System.out.println("Name hash table size doubled to "
                + hashTableSize + " slots.");
        }
    }


    /**
     * locate the position basd on the key value
     * 
     * @param key
     *            key of the hash
     * @param curTable
     *            current hash table which is working on
     * @return the index of the location
     */
    public int findSlot(K key, Hash<K, V>[] curTable) {
        int index = HashMethod.h(key, curTable.length);
        int index2 = index;
        for (int i = 1;; i++) {
            if (curTable[index2] != null && curTable[index2].tomb) {
                return index2;
            }
            else if (curTable[index2] == null) {
                return index2;
            }
            index2 = (index + i * i) % curTable.length;
        }
    }


    /**
     * use the key to locate the hash in the hashtable
     * 
     * @param key
     *            hash key
     * @return the location of the hash, -1 if not found
     */
    public int contains(K key) {
        int index = HashMethod.h(key, table.length);
        int index2 = index;
        for (int i = 1; this.table[index2] != null; i++) {
            if (!this.table[index2].tomb && this.table[index2].getKey().equals(
                key)) {
                return index2;
            }
            index2 = (index + i * i) % hashTableSize;
        }
        return -1;

    }


    /**
     * get the size of the hashtable
     * 
     * @return size of the table
     */
    public int getHashTableSize() {
        return hashTableSize;
    }


    /**
     * replace the table with provide table
     * 
     * @param table
     *            table provided
     */
    public void setTable(Hash<K, V>[] table) {
        this.table = table;
    }


    /**
     * get the current table which is working on
     * 
     * @return current table
     */
    public Hash<K, V>[] getTable() {
        return table;
    }


    /**
     * get the data based on the index
     * 
     * @param index
     *            index which locate the data
     * @return data located in the position of index
     */
    public Hash<K, V> getData(int index) {
        return this.table[index];
    }


    /**
     * print the table
     * 
     * @return the string which contain the table information
     */
    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        for (int i = 0; i < getHashTableSize(); i++) {
            if (table[i] != null 
                && !table[i].tomb) {
                str.append("|").append(table[i].getKey()).append("|").append(" "
                    + i).append("\n");
            }
        }
        str.append("Total records: ").append(this.getSize());
        return str.toString();
    }
}
