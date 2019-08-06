import student.TestCase;

/**
 * This class test the skiplist class
 *
 * @author jiahui huang<hjiahui7>
 * @author Yusheng Cao<cyschris>
 * @version 2018.11.22
 */
public class SkipListTest
    extends TestCase
{
    private SkipList<String, Integer> list;


    /**
     * Sets up the tests that follow. In general, used for initialization
     */
    public void setUp()
    {
        list = new SkipList<String, Integer>();
    }


    /**
     * test insert method
     */
    public void testInsert()
    {
        list.insert("string1", 1);
        list.insert("string2", 2);
        list.insert("string3", 3);
        list.insert("string4", 4);
        list.insert("string5", 5);
        assertTrue(list.find("string2").equals(2));
    }


    /**
     * test find method
     */
    public void testfind()
    {
        assertNull(list.find("string"));
        list.insert("string1", 1);
        list.insert("string2", 2);
        assertTrue(list.find("string2").equals(2));
    }


    /**
     * test contains method
     */
    public void testContains()
    {
        list.insert("string1", 1);
        list.insert("string2", 2);
        list.insert("string3", 3);
        list.insert("string4", 4);
        list.insert("string5", 5);
        assertTrue(list.contains("string1").element().equals(1));
        assertNull(list.contains("string9"));
    }


    /**
     * test remove method
     */
    public void testRemove()
    {
        list.insert("string1", 1);
        list.insert("string2", 2);
        list.insert("string3", 3);
        list.insert("string4", 4);
        list.insert("string5", 5);
        assertNull(list.remove(""));
        assertTrue(list.remove("string1").equals(1));
        assertTrue(list.remove("string5").equals(5));
        assertTrue(list.remove("string3").equals(3));
        assertTrue(list.remove("string4").equals(4));
        assertTrue(list.remove("string2").equals(2));
    }

}
