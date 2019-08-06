package towerofhanoi;

import stack.StackInterface;
import java.util.EmptyStackException;

/**
 * the class for LinkedStack for store the data
 * 
 * @author <jiahui huang> <hjiahui7>
 * @version 2017/10/20
 * @param <T>
 */
public class LinkedStack<T> implements StackInterface<T>
{
    /**
     * the field for LinkedStack
     */
    private Node topNode;
    private int size;


    /**
     * the constructor for LinkedStack
     */
    public LinkedStack()
    {
        topNode = null;
        size = 0;
    }


    /**
     * it will return the size of discs in this stack
     * 
     * @return the size in this stack
     */
    public int size()
    {
        return size;
    }


    /**
     * the clear method will remove all elements
     */
    @Override
    public void clear()
    {
        size = 0;
        topNode = null;
    }


    /**
     * to check whether the stack is empty
     * 
     * @return true or false to show empty or not
     */
    @Override
    public boolean isEmpty()
    {
        return this.size() == 0;
    }


    /**
     * it will return the data in the firstNode
     * 
     * @return the data in the firstNode
     */
    @Override
    public T peek()
    {
        if (size() == 0)
        {
            throw new EmptyStackException();
        }
        return topNode.getData();
    }


    /**
     * remove the node and get data
     * 
     * @return the data and get the firstNode's data
     */
    @Override
    public T pop()
    {
        if (size() == 0)
        {
            throw new EmptyStackException();
        }
        Node current = topNode;
        topNode = topNode.getNextNode();
        size--;
        return current.getData();
    }


    /**
     * it will push the data in a new node
     * 
     * @param newdata
     *            which will be added
     */
    @Override
    public void push(T newdata)
    {
        Node newBody = new Node(newdata);
        newBody.setNextNode(topNode);
        topNode = newBody;
        size++;
    }


    /**
     * it will show the size of discs in each tower
     * 
     * @return the string of disc in the tower
     */
    public String toString()
    {
        Node current = topNode;
        StringBuilder outPut = new StringBuilder();
        outPut.append("[");
        boolean firstItem = true;

        while (current != null)
        {
            if (firstItem)
            {
                firstItem = false;
                outPut.append(String.valueOf(current.getData()));
                current = current.next;
                continue;
            }
            outPut.append(", ");
            outPut.append(String.valueOf(current.getData()));
            current = current.next;
        }
        outPut.append("]");

        return outPut.toString();
    }


    /**
     * this is a private class node
     * 
     * @author jiahui huang
     *
     */
    private class Node
    {
        /**
         * field for node
         */
        Node next;
        T data;


        /**
         * create a node has the data
         * 
         * @param data
         *            next Node
         */
        public Node(T data)
        {
            this.data = data;
        }


        /**
         * get next node of current node
         * 
         * @return the next node
         */
        public Node getNextNode()
        {
            return next;
        }


        /**
         * get the data in this node
         * 
         * @return data we will use
         */
        public T getData()
        {
            return data;
        }


        /**
         * set the next node
         * 
         * @param nextNode
         *            the next of current one
         */
        public void setNextNode(Node nextNode)
        {
            this.next = nextNode;
        }
    }

}
