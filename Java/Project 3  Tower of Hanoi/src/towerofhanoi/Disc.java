/**
 * 
 */
package towerofhanoi;

import java.awt.Color;
import java.util.Random;
import CS2114.Shape;

/**
 * the disc class will store the size of each disc and
 * it will be stored in linked stack class
 * 
 * @author <jiahui huang> <hjiahui7>
 * @version 2017/10/20
 */
public class Disc extends Shape implements Comparable<Disc>
{
    /**
     * the constructor for disc class with a parameter
     * it will call it super class shape
     * and random generate a color
     * 
     * @param width
     *            the width for disc
     */
    public Disc(int width)
    {
        super(0, 0, width, GameWindow.DISK_HEIGHT);
        Random random = new Random();
        int numberOfColor = 256;
        this.setBackgroundColor(new Color(random.nextInt(numberOfColor), random
            .nextInt(numberOfColor), random.nextInt(numberOfColor)));
    }


    /**
     * the compare to method will return three different value to determine
     * whether this disc is equal, larger, or smaller than other disc
     * 
     * @param otherDisc
     *            other disc which will be compared by this disc
     */
    @Override
    public int compareTo(Disc otherDisc)
    {
        if (otherDisc == null)
        {
            throw new IllegalArgumentException();
        }
        if (otherDisc.getWidth() > this.getWidth())
        {
            return -1;
        }
        else if (otherDisc.getWidth() == this.getWidth())
        {
            return 0;
        }
        else
        {
            return 1;
        }
    }


    /**
     * The toString method will return the width of each disc
     * 
     * @return it will return the width of each disc
     */
    public String toString()
    {
        int width = this.getWidth();
        return "" + width;
    }


    /**
     * The equals method will compare the two disc's width to determine they are
     * equal
     * 
     * @return true if equal and false if it is null or not equals
     * @param other
     *            the other disc which will be compared by this disc
     * 
     */
    public boolean equals(Object other)
    {
        if (other == null)
        {
            return false;
        }
        if (other == this)
        {
            return true;
        }
        if (this.getClass() == other.getClass())
        {
            Disc args = (Disc)other;
            if (args.getWidth() == this.getWidth())
            {
                return true;
            }

        }
        return false;
    }

}
