package towerofhanoi;

import java.util.EmptyStackException;
import student.TestCase;

/**
 * the class for LinkedStackTest for testing the method in LinkedStack
 * 
 * @author <jiahui huang> <hjiahui7>
 * @version 2017/10/20
 */
public class LinkedStackTest extends TestCase
{
    /**
     * the field for LinkedStackTest
     */
    private LinkedStack<String> stack;
    private LinkedStack<String> stackEmpty;


    /**
     * The setUp for LinkedStackTest
     */
    public void setUp()
    {
        stack = new LinkedStack<String>();
        stack.push("1");
        stack.push("2");
        stack.push("3");
        stackEmpty = new LinkedStack<String>();
        new LinkedStack<String>();

    }


    /**
     * The method for testSize
     */
    public void testSize()
    {
        assertEquals(0, stackEmpty.size());
    }


    /**
     * The method for testClear
     */
    public void testClear()
    {
        stack.clear();
        assertEquals(0, stack.size());
    }


    /**
     * The method for testIsEmpty
     */
    public void testIsEmpty()
    {
        assertFalse(stack.isEmpty());
        stack.clear();
        assertTrue(stack.isEmpty());
    }


    /**
     * The method for testPeek
     */
    public void testPeek()
    {
        assertEquals("3", stack.peek());
        Exception thrown = null;
        try
        {
            stackEmpty.peek();
        }
        catch (Exception e)
        {
            thrown = e;
        }
        assertTrue(thrown instanceof EmptyStackException);
    }


    /**
     * The method for testPop
     */
    public void testPop()
    {
        assertEquals("3", stack.pop());
        Exception thrown = null;
        try
        {
            stackEmpty.pop();
        }
        catch (Exception e)
        {
            thrown = e;
        }
        assertTrue(thrown instanceof EmptyStackException);
    }


    /**
     * The method for testToString
     */
    public void testToString()
    {
        stackEmpty.push("1");
        assertEquals("[1]", stackEmpty.toString());
        stackEmpty.push("2");
        assertEquals("[2, 1]", stackEmpty.toString());
    }
}
