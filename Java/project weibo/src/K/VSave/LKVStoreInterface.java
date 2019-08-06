package K.VSave;

public interface LKVStoreInterface<K, V> extends KVStore<K, V>
{
    public boolean isEmpty();
    public int size();
    public void clear();
    public V getFirst(K key);
    public V getLast(K key);
    public void addFirst(K key, V value);
    public void addLast(K key, V value);
    public boolean removeFirst(K key);
    public boolean removeLast(K key);
    public boolean contains(K key);
    @Override
    default public V get(K key)
    {
        return getFirst(key);
    }
    @Override
    default public void put(K key, V value)
    {
        addLast(key, value);
    }
}
