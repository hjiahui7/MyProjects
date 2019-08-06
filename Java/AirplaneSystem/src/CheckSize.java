
/**
 * This is a helper class which provide method to compare two objects based on
 * different rules of comparison
 *
 * @author jiahui huang<hjiahui7>
 * @author Yusheng Cao<cyschris>
 * @version 2018.11.22
 */
public class CheckSize {

    /**
     * the constructor for CheckSize
     */
    public CheckSize() {
        // nothing..
    }


    /**
     * compare a airObject object and a box object to see if the airobject's
     * dimension come across the dimension which record in the given box or
     * otherwise the box come across the airobject dimension if didn't come
     * across return true otherwise return false
     *
     * @param air
     *            the airobject which compared
     * @param curBox
     *            the box object which indicate the range
     * @return the result
     */
    public static boolean checkCollision(AirObject air, Box curBox) {
        return curBox.getX() < air.getX() + air.getXwid() && air.getX() < curBox
            .getX() + curBox.getXwid() && curBox.getY() < air.getY() + air
                .getYwid() && air.getY() < curBox.getY() + curBox.getYwid()
            && curBox.getZ() < air.getZ() + air.getZwid() && air.getZ() < curBox
                .getZ() + curBox.getZwid();
    }


    // ----------------------------------------------------------
    /**
     * compare a airObject object and one other airObject object to see if the
     * both airobject's dimension come across the dimension which record in the
     * second airObject and vice versa. if didn't come across return true
     * otherwise return false
     *
     * @param air
     *            the airobject which compared
     * @param curBox
     *            the airobject which indicate range
     * @return result
     */
    public static boolean checkTwoAir(AirObject air, AirObject curBox) {
        return curBox.getX() < air.getX() + air.getXwid() && air.getX() < curBox
            .getX() + curBox.getXwid() && curBox.getY() < air.getY() + air
                .getYwid() && air.getY() < curBox.getY() + curBox.getYwid()
            && curBox.getZ() < air.getZ() + air.getZwid() && air.getZ() < curBox
                .getZ() + curBox.getZwid();
    }


    /**
     * compare a box object and one other box object to see if the box's
     * dimension come across the dimension which record in the second box. and
     * vice versa. if didn't come across return true otherwise return false
     *
     * @param air
     *            the box which compared
     * @param curBox
     *            the box which indicate range
     * @return result
     */
    public static boolean checkTwoBox(Box air, Box curBox) {
        return curBox.getX() < air.getX() + air.getXwid() && air.getX() < curBox
            .getX() + curBox.getXwid() && curBox.getY() < air.getY() + air
                .getYwid() && air.getY() < curBox.getY() + curBox.getYwid()
            && curBox.getZ() < air.getZ() + air.getZwid() && air.getZ() < curBox
                .getZ() + curBox.getZwid();

    }
}
