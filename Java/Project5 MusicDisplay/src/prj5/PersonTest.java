package prj5;

import static prj5.PropertyEnum.*;
import java.util.ArrayList;
import student.TestCase;

/**
 * Test Class of Person
 * 
 * @author <Jiahui Huang> <hjiahui7>
 * @version 2017/12/05
 */
public class PersonTest extends TestCase
{
    /**
     * field
     */
    private Person jiahui;
    private ArrayList<String> choice;


    /**
     * Set up
     */
    public void setUp()
    {
        choice = new ArrayList<String>();
        choice.add("Yes");
        choice.add("No");
        choice.add("No");
        jiahui = new Person(ART, MC, NE, choice);
    }


    /**
     * Test method ToString()
     */
    public void testToString()
    {
        String test = jiahui.toString();
        String test2 = "[ART, MC, NE, Yes, No, No]";
        assertEquals(test, test2);
    }


    /**
     * Test method IsEmpty()
     */
    public void testIsEmpty()
    {
        assertFalse(jiahui.isEmpty());
        choice.clear();
        assertTrue(jiahui.isEmpty());
    }


    /**
     * Test method GetNumberOfChoice()
     */
    public void testGetNumberOfChoice()
    {
        assertEquals(3, jiahui.getNumberOfChoice());
    }
}
