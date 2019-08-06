package K.VSave;

public interface KVStore<K, V>
{

    public V get(K key);


    public void put(K key, V value);
}
