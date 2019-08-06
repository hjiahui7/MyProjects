import java.util.Random;

/**
 * This is a class provide function as a skip list.
 *
 * @author jiahui huang<hjiahui7>
 * @author Yusheng Cao<cyschris>
 * @version 2018.11.22
 * @param <K>
 *            key
 * @param <E>
 *            data
 */
public class SkipList<K extends Comparable<K>, E> {

    private SkipNode<K, E> head;
    private int level;
    private int size;
    private Random ran;


    // ----------------------------------------------------------
    /**
     * constractor of the skip list
     */
    public SkipList() {
        head = new SkipNode<K, E>(null, null, 0);
        level = -1;
        size = 0;
        ran = new Random();
    }


    // ----------------------------------------------------------
    /**
     * base on a key to find the related data
     *
     * @param key
     *            key to locate data
     * @return the related data
     */
    public E find(K key) {
        SkipNode<K, E> thisone = contains(key);
        if (thisone != null) {
            return thisone.element();
        }
        return null;
    }


    // ----------------------------------------------------------
    /**
     * check if the key contain in the list
     *
     * @param key
     *            key need to be found
     * @return the skipNode
     */
    public SkipNode<K, E> contains(K key) {
        SkipNode<K, E> x = head; // Dummy header node
        for (int i = level; i >= 0; i--) {
            while ((x.forward[i] != null) && (x.forward[i].key().compareTo(
                key) < 0)) {
                x = x.forward[i]; // Go one last step
            }
        }
        x = x.forward[0];
        if ((x != null) && (x.key().compareTo(key) == 0)) {
            return x;
        }
        return null;
    }


    // ----------------------------------------------------------
    /**
     * remove the key from the skip list
     *
     * @param key
     *            the key need to be removed
     * @return removed data
     */
    @SuppressWarnings("unchecked")
    public E remove(K key) {
        E data = null;
        SkipNode<K, E> thisone = contains(key);
        if (thisone != null) {
            SkipNode<K, E>[] update = new SkipNode[level + 1];
            SkipNode<K, E> x = head; // Start at header node
            for (int i = level; i >= 0; i--) { // Find insert position
                while ((x.forward[i] != null) && (x.forward[i].key().compareTo(
                    key) < 0)) {
                    x = x.forward[i];
                }
                update[i] = x; // Track end at level i
            }
            x = x.forward[0];
            data = x.element();
            if (x.forward[0] != null) {
                for (int i = 0; i < x.forward.length; i++) {
                    update[i].forward[i] = x.forward[i];
                }
            }
            else {
                for (int i = 0; i < update.length; i++) {
                    update[i].forward[i] = null;
                }
            }
            size--;
        }
        else {
            return null;
        }
        return data;
    }


    // ----------------------------------------------------------
    /**
     * get a random level number
     *
     * @return random level number
     */
    int randomLevel() {
        int lev = 0;
        while (Math.abs(ran.nextInt()) % 2 != 0) {
            lev++;
        }
        return lev;
    }


    // ----------------------------------------------------------
    /**
     * add a new element and key into the skip list
     *
     * @param key
     *            new key
     * @param elem
     *            new element match the key
     */
    @SuppressWarnings("unchecked")
    public void insert(K key, E elem) {
        int newLevel = randomLevel(); // New node's level
        if (newLevel > level) // If new node is deeper
        {
            adjustHead(newLevel); // adjust the header
        }
        // Track end of level
        SkipNode<K, E>[] update = new SkipNode[level + 1];
        SkipNode<K, E> x = head; // Start at header node
        for (int i = level; i >= 0; i--) { // Find insert position
            while ((x.forward[i] != null) && (x.forward[i].key().compareTo(
                key) < 0)) {
                x = x.forward[i];
            }
            update[i] = x; // Track end at level i
        }
        x = new SkipNode<K, E>(key, elem, newLevel);
        for (int i = 0; i <= newLevel; i++) { // Splice into list
            x.forward[i] = update[i].forward[i]; // Who x points to
            update[i].forward[i] = x; // Who points to x
        }
        size++; // Increment dictionary size
    }


    /**
     * based on the given new level number change the head of the skip list
     *
     * @param newLevel
     *            new Level number
     */
    private void adjustHead(int newLevel) {
        SkipNode<K, E> temp = head;
        head = new SkipNode<K, E>(null, null, newLevel);
        for (int i = 0; i <= level; i++) {
            head.forward[i] = temp.forward[i];
        }
        level = newLevel;
    }


    /**
     * print all the data in the skip list
     *
     * @return the string contain all the data
     */
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append("SkipList dump:\n");
        SkipNode<K, E> x = head;
        int count = 0;
        while (x != null) {
            s.append("Node has depth " + x.forward.length).append(", Value (");
            if (x.element() != null) {
                s.append(x.element().toString());
                s.append(")");
            }
            else {
                s.append("null)");
            }
            s.append("\n");
            x = x.forward[0];
            count++;
        }
        s.append((count - 1) + " skiplist nodes printed");
        return s.toString();
    }


    /**
     * get a array of all the data in the skip list
     *
     * @return element array conatin all the data
     */
    @SuppressWarnings("unchecked")
    public E[] getAllData() {
        int count = 0;
        E[] datas = (E[])new Object[size];
        SkipNode<K, E> x = head; // Dummy header node
        while (x.forward[0] != null) {
            x = x.forward[0];
            datas[count] = x.element();
            count++;
        }
        return datas;
    }


    /**
     * basic skip node of the skip list
     *
     * @author jiahui huang<hjiahui7>
     * @author Yusheng Cao<cyschris>
     * @version 2018.11.22
     * @param <K>
     *            key
     * @param <E>
     *            data
     */
    public static class SkipNode<K extends Comparable<K>, E> {
        private KVPair<K, E> rec;
        /**
         * new skip node
         */
        private SkipNode<K, E>[] forward;


        // ----------------------------------------------------------
        /**
         * get the element
         *
         * @return element data
         */
        public E element() {
            return rec.value();
        }


        // ----------------------------------------------------------
        /**
         * get the key value
         *
         * @return key value
         */
        public K key() {
            return rec.key();
        }


        // ----------------------------------------------------------
        /**
         * Create a new SkipNode object.
         *
         * @param key
         *            new key
         * @param elem
         *            related element
         * @param level
         *            related level
         */
        @SuppressWarnings("unchecked")
        public SkipNode(K key, E elem, int level) {
            rec = new KVPair<K, E>(key, elem);
            forward = new SkipNode[level + 1];
            for (int i = 0; i < level; i++) {
                forward[i] = null;
            }
        }

    }


    /**
     * basic skip pair of the skip list
     *
     * @author jiahui huang<hjiahui7>
     * @author Yusheng Cao<cyschris>
     * @version 2018.11.22
     * @param <K>
     *            key
     * @param <E>
     *            data
     */
    public static class KVPair<K, E> {

        /**
         * field
         */
        private E value;
        private K key;


        /**
         * create new hash object
         *
         * @param key
         *            key of data
         * @param value
         *            value of data
         */
        public KVPair(K key, E value) {
            this.key = key;
            this.value = value;
        }


        /**
         * return the key value
         *
         * @return value of key
         */
        public K key() {
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
         * get the value in this hash
         *
         * @return value of hash
         */
        public E value() {
            return value;
        }

    }

}
