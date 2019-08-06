/**
 * This is a helper class which create a object which contain two airobject a
 * and b which caused collision.
 *
 * @author jiahui huang<hjiahui7>
 * @author Yusheng Cao<cyschris>
 * @version 2018.11.22
 */
public class CollisionObject {

    /**
     * first airObject
     */
    private AirObject a;
    /**
     * second airObject
     */
    private AirObject b;


    /**
     * Constructor for collisionobject class
     *
     * @param a
     *            first airobject
     * @param b
     *            second airobject
     */
    public CollisionObject(AirObject a, AirObject b) {
        this.a = a;
        this.b = b;
    }


    /**
     * getter method for the first airobject
     *
     * @return first airobject
     */
    public AirObject getA() {
        return a;
    }


    /**
     * getter method for the second airobject
     *
     * @return second airobject
     */
    public AirObject getB() {
        return b;
    }


    // ----------------------------------------------------------
    /**
     * test this collison object is equal to given collisionobject
     *
     * @param obj
     *            other collision object
     * @return true if equals
     */
    public boolean equal(CollisionObject obj) {
        if (this == obj) {
            return true;
        }
        return (this.getA().compareTo(obj.getA()) == 0 && this.getB().compareTo(
            obj.getB()) == 0) || (this.getA().compareTo(obj.getB()) == 0 && this
                .getB().compareTo(obj.getA()) == 0);

    }
}
