package K.VSave;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class LKVStore<K, V>
    implements LKVStoreInterface<K, V>, Iterable<K>
{

    private class Node
    {
        private Node prev;
        private Node next;
        private K key;
        private V value;


        public Node()
        {
            // todo
        }


        public Node(K key, V value)
        {
            this.key = key;
            this.value = value;
        }


        private void linkWith(Node nextNode)
        {
            this.next = nextNode;
            nextNode.prev = this;
        }


        private void insertAfter(Node current)
        {
            Node tmp = current.next;
            current.linkWith(this);
            linkWith(tmp);
            size++;
        }


        private void remove()
        {
            prev.linkWith(next);
            size--;
        }

    }

    private int size;
    private Node head;
    private Node tail;


    public LKVStore()
    {
        head = new Node();
        tail = new Node();
        clear();
    }


    public boolean isEmpty()
    {
        return size == 0;
    }


    public int size()
    {
        return size;
    }


    public void clear()
    {
        size = 0;
        head.next = tail;
        tail.prev = head;
    }


    public V getFirst(K key)
    {
        for (Node current = head.next; current != tail; current = current.next)
        {
            if (current.key.equals(key))
            {
                return current.value;
            }
        }
        return null;
    }


    public V getLast(K key)
    {
        V answer = null;
        for (Node curr = head.next; curr != tail; curr = curr.next)
        {
            if (curr.key.equals(key))
            {
                answer = curr.value;
            }
        }
        return answer;
    }


    public void addFirst(K key, V value)
    {
        Node node = new Node(key, value);
        node.insertAfter(head);
    }


    public void addLast(K key, V value)
    {
        Node node = new Node(key, value);
        if (size == 0)
        {
            node.insertAfter(head);
        }
        else
        {
            node.insertAfter(tail.prev);
        }

    }


    public boolean removeFirst(K key)
    {
        for (Node curr = head.next; curr != tail; curr = curr.next)
        {
            if (curr.key.equals(key))
            {
                curr.remove();
                return true;
            }
        }
        return false;
    }


    public boolean removeLast(K key)
    {
        for (Node current = tail.prev; current != head; current = current.prev)
        {
            if (current.key.equals(key))
            {
                current.remove();
                return true;
            }
        }
        return false;
    }


    public boolean contains(K key)
    {
        for (Node curr = head.next; curr != tail; curr = curr.next)
        {
            if (curr.key.equals(key))
            {
                return true;
            }
        }
        return false;
    }


    public Iterator<K> iterator()
    {
        return new KVStoreIterator();
    }


    public String toString()
    {
        StringBuilder builder = new StringBuilder();
        Iterator<K> i = iterator();
        builder.append("[");
        while (i.hasNext())
        {
            builder.append(i.next());
            builder.append(i.hasNext() ? ", " : "");
        }
        builder.append("]");
        return builder.toString();
    }


    private class KVStoreIterator implements Iterator<K>
    {
        private Node curr;


        public KVStoreIterator()
        {
            curr = head.next;
        }


        public boolean hasNext()
        {
            boolean result = true;
            if (curr == tail)
            {
                result = false;
            }
            return result;

        }


        public K next()
        {

            if (hasNext())
            {
                K keyvalue = curr.key;
                curr = curr.next;
                return keyvalue;
            }
            else
            {
                throw new NoSuchElementException();
            }
        }
    }

}
