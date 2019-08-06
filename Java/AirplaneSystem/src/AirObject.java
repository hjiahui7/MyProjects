/**
 * Air traffic control general object type interface All tracked objects must
 * have a bounding prism and a name
 *
 * @author jiahui huang<hjiahui7>
 * @author Yusheng Cao<cyschris>
 * @version 2018.11.22
 */

public class AirObject implements Comparable<AirObject> {
    private String type;
    private String name;
    private int x;
    private int y;
    private int z;
    private int xwid;
    private int ywid;
    private int zwid;
    private String[] lastThings;


    /**
     * Constructor for AirObject class
     *
     * @param things
     *            a string array which contain base information for the air
     *            object
     */
    public AirObject(String[] things) {
        type = things[1];
        name = things[2];
        x = Integer.valueOf(things[3]);
        y = Integer.valueOf(things[4]);
        z = Integer.valueOf(things[5]);
        xwid = Integer.valueOf(things[6]);
        ywid = Integer.valueOf(things[7]);
        zwid = Integer.valueOf(things[8]);
        lastThings = new String[things.length - 9];
        for (int i = 0; i < things.length - 9; i++) {
            lastThings[i] = things[i + 9];
        }
    }


    // ----------------------------------------------------------
    /**
     * Getter for x value of the airObject
     *
     * @return x value of the airObject
     */
    public int getX() {
        return x;
    }


    // ----------------------------------------------------------
    /**
     * Getter for y value of the airObject
     *
     * @return y value of the airObject
     */
    public int getY() {
        return y;
    }


    // ----------------------------------------------------------
    /**
     * Getter for z value of the airObject
     *
     * @return z value of the airObject
     */
    public int getZ() {
        return z;
    }


    // ----------------------------------------------------------
    /**
     * Getter for x wide of the airObject
     *
     * @return x wide of the airObject
     */
    public int getXwid() {
        return xwid;
    }


    // ----------------------------------------------------------
    /**
     * Getter for y wide of the airObject
     *
     * @return y wide of the airObject
     */
    public int getYwid() {
        return ywid;
    }


    // ----------------------------------------------------------
    /**
     * Getter for z wide of the airObject
     *
     * @return z wide of the airObject
     */
    public int getZwid() {
        return zwid;
    }


    // ----------------------------------------------------------
    /**
     * Getter for the string array which contain the information of last things
     *
     * @return string array which contain the information of last things
     */
    public String[] getLastThings() {
        return lastThings;
    }


    /**
     * Getter for name
     *
     * @return name
     */
    public String getName() {
        return name;
    }


    /**
     * Compare against a (name) String.
     *
     * @param it
     *            The String to compare to
     * @return Standard values for compareTo
     */
    public int compareTo(AirObject it) {
        return name.compareTo(it.getName());
    }


    /**
     * print out the object information
     *
     * @return the basic information of the air object
     */
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append(type + " " + name + " " + x + " " + y + " " + z + " " + xwid
            + " " + ywid + " " + zwid);
        for (int i = 0; i < lastThings.length; i++) {
            s.append(" ").append(lastThings[i]);
        }
        String temp = s.toString();
        return temp.substring(0, 1).toUpperCase() + temp.substring(1);
    }


    /**
     * compare this object with other objectF
     *
     * @param air
     *            other object need to be compared with
     * @return the result
     */
    @Override
    public boolean equals(Object air) {
        if (!(air instanceof AirObject)) {
            return false;
        }
        return ((AirObject)air).getName().equals(this.getName());
    }

}
