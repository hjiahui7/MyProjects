import student.TestCase;

/**
 * test the hash table
 *
 * @author jiahui huang<hjiahui7>
 * @author Yusheng Cao<cyschris>
 * @version 2018.9.4
 */
public class HashTableTest extends TestCase {

    /**
     * field
     */
    private HashTable<String, Handle> table;
    private String name;
    private Handle handle;


    /**
     * the set up for HashTable class
     */
    public void setUp() {
        table = new HashTable<String, Handle>(10);
        name = "a";
        handle = new Handle(0, 10);
    }


    /**
     * test the add method
     */
    public void testAdd() {
        assertEquals(table.toString(), "Total records: 0");
        assertTrue(table.add(name, handle));
        assertEquals(table.getSize(), 1);
        assertFalse(table.add(name, handle));
        assertTrue(table.add("k", handle));
        assertTrue(table.remove("k"));
        assertEquals(table.getSize(), 1);
        assertTrue(table.add("k", handle));
        assertFalse(table.add("k", handle));
        assertNull(table.getData(0));
        assertEquals(table.getHashTableSize(), 10);
        assertEquals(table.toString(), "|a| 7\r\n" + "|k| 8\r\n"
            + "Total records: 2");

    }


    /**
     * test the ensurecapacity method
     */
    public void testEnsureCapacity() {
        assertTrue(table.add("Case closed", handle));
        assertTrue(table.add("Toy Story", handle));
        assertTrue(table.add("One Piece", handle));
        assertTrue(table.remove("One Piece"));
        assertTrue(table.remove("Toy Story"));
        assertTrue(table.remove("Case closed"));
        assertTrue(table.add("a", handle));
        assertTrue(table.add("as", handle));
        assertTrue(table.add("aad", handle));
        assertTrue(table.add("aada", handle));
        assertTrue(table.add("aasa", handle));
        assertTrue(table.add("aasazz", handle));
    }


    /**
     * test the FindSlot method
     */
    public void testFindSlot() {
        assertEquals(7, table.findSlot("a", table.getTable()));
        table.add("a", handle);
        table.remove("a");
        assertEquals(7, table.findSlot("a", table.getTable()));
        table.add("a", handle);
        table.add("a", handle);
        table.add("b", handle);
        table.add("c", handle);
        table.add("d", handle);
        table.add("e", handle);
        table.add("g", handle);
    }


    /**
     * test the remove method
     */
    public void testRemove() {
        assertTrue(table.add("a", handle));
        assertTrue(table.add("b", handle));
        assertTrue(table.add("c", handle));
        assertTrue(table.add("d", handle));
        assertTrue(table.add("e", handle));
        assertTrue(table.add("f", handle));
        assertTrue(table.add("g", handle));
        assertTrue(table.add("k", handle));
        // System.out.println(table.getSize());
        assertTrue(table.remove("a"));
        assertTrue(table.remove("b"));
        assertTrue(table.remove("c"));
        assertTrue(table.remove("d"));
        assertTrue(table.remove("e"));
        assertFalse(table.remove("z"));
        assertTrue(table.remove("k"));
        // System.out.println(table.getSize());
        assertTrue(table.add("a", handle));
        assertTrue(table.add("k", handle));
        // System.out.println(table.getSize());
        assertFalse(table.add("a", handle));
        assertEquals(table.getSize(), 4);
    }


    /**
     * test to String method
     */
    public void testToString() {
        assertTrue(table.add("a", handle));
        assertTrue(table.add("b", handle));
        assertTrue(table.add("c", handle));
        assertTrue(table.remove("a"));
        assertTrue(table.remove("b"));
        assertTrue(table.remove("c"));
        assertTrue(table.add("d", handle));
        assertTrue(table.add("e", handle));
        assertTrue(table.add("f", handle));
        assertTrue(table.add("g", handle));
        assertTrue(table.add("h", handle));
        assertTrue(table.add("i", handle));
        assertTrue(table.remove("g"));
        assertTrue(table.remove("h"));
        assertTrue(table.remove("i"));
        String str = "|d| 0\n|e| 1\n|f| 2\nTotal records: 3";
        assertEquals(str, table.toString());
    }
}
