/**
 * This is a helper class which create object which contain the x y and z
 * location, and x y z width. It easier for the binary tree class too process
 * information.
 *
 * @author jiahui huang<hjiahui7>
 * @author Yusheng Cao<cyschris>
 * @version 2018.11.22
 */

public class Box
{

    private int x;
    private int y;
    private int z;
    private int xwid;
    private int ywid;
    private int zwid;


    /**
     * Represent the basic information of the box
     *
     * @return the string which contain the information
     */
    @Override
    public String toString()
    {
        return "Box(x=" + x + ", y=" + y + ", z=" + z + ", xwid=" + xwid
            + ", ywid=" + ywid + ", zwid=" + zwid + ")";
    }


    // ----------------------------------------------------------
    /**
     * Constructor for box class
     *
     * @param x
     *            the x location
     * @param y
     *            the y location
     * @param z
     *            the z location
     * @param xwid
     *            x width
     * @param ywid
     *            y width
     * @param zwid
     *            z width
     */
    public Box(int x, int y, int z, int xwid, int ywid, int zwid)
    {
        this.x = x;
        this.y = y;
        this.z = z;
        this.xwid = xwid;
        this.ywid = ywid;
        this.zwid = zwid;
    }


    /**
     * getter method for the X
     *
     * @return x value
     */
    public int getX()
    {
        return x;
    }


    /**
     * getter method for the y
     *
     * @return y value
     */
    public int getY()
    {
        return y;
    }


    /**
     * getter method for the z
     *
     * @return z value
     */
    public int getZ()
    {
        return z;
    }


    /**
     * getter method for the X width
     *
     * @return X width
     */
    public int getXwid()
    {
        return xwid;
    }


    /**
     * getter method for the y width
     *
     * @return y width
     */
    public int getYwid()
    {
        return ywid;
    }


    /**
     * getter method for the z width
     *
     * @return z width
     */
    public int getZwid()
    {
        return zwid;
    }
}
