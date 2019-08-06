/**
 * This is a 3 dimensional binary tree which in use of store information for
 * this project. It represent the three dimensional world that is 1024 unit in
 * each dimension each object is stored in every leaf node of the tree.
 *
 * @author jiahui huang<hjiahui7>
 * @author Yusheng Cao<cyschris>
 * @version 2018.11.22
 */
public class Bintree {
    private Node root;
    /**
     * the largest size of this binary tree can't be changed
     */
    public static final int DIMENSION = 1024;
    /**
     * the basic node which contain the information
     */
    private final Node flyWeight = new FlyWeightNode(this);


    // ----------------------------------------------------------
    /**
     * Create a new Binary tree object make the flyweight node as root
     *
     * @param boxSize
     *            the size of the box
     */
    public Bintree(int boxSize) {
        root = flyWeight;
    }


    // ----------------------------------------------------------
    /**
     * add new airObject into the binary tree.
     *
     * @param data
     *            new airObject
     */
    public void insert(AirObject data) {
        root = root.insert(data, getStartingBox(), 1);
    }


    // ----------------------------------------------------------
    /**
     * get the starting location and basic maximum running area
     *
     * @return the box object which contain related information
     */
    private Box getStartingBox() {
        return new Box(0, 0, 0, DIMENSION, DIMENSION, DIMENSION);
    }


    // ----------------------------------------------------------
    /**
     * remove the airObject from the binary tree
     *
     * @param data
     *            the airObject which need to removed
     */
    public void remove(AirObject data) {
        root = root.remove(data);
    }


    // ----------------------------------------------------------
    /**
     * based on the current box, the dimension type and the forward or backward
     * status to generate a new Box to describe the new status of the air space
     *
     * @param curBox
     *            the current box
     * @param type
     *            dimension type
     * @param left
     *            forward or backward
     * @return the new Box
     */
    public Box getNewBox(Box curBox, String type, boolean left) {
        Box result = null;
        if (type.equals("x")) {
            if (left) {
                // check if is left side or right side
                result = new Box(curBox.getX(), curBox.getY(), curBox.getZ(),
                    curBox.getXwid() / 2, curBox.getYwid(), curBox.getZwid());
            }
            else {
                result = new Box(curBox.getX() + curBox.getXwid() / 2, curBox
                    .getY(), curBox.getZ(), curBox.getXwid() / 2, curBox
                        .getYwid(), curBox.getZwid());
            }
        }
        else if (type.equals("y")) {
            if (left) {
                // check if is up side or down side
                result = new Box(curBox.getX(), curBox.getY(), curBox.getZ(),
                    curBox.getXwid(), curBox.getYwid() / 2, curBox.getZwid());
            }
            else {
                result = new Box(curBox.getX(), curBox.getY() + curBox.getYwid()
                    / 2, curBox.getZ(), curBox.getXwid(), curBox.getYwid() / 2,
                    curBox.getZwid());
            }
        }
        else if (type.equals("z")) {
            if (left) {
                // check if is forward side or back side
                result = new Box(curBox.getX(), curBox.getY(), curBox.getZ(),
                    curBox.getXwid(), curBox.getYwid(), curBox.getZwid() / 2);
            }
            else {
                result = new Box(curBox.getX(), curBox.getY(), curBox.getZ()
                    + curBox.getZwid() / 2, curBox.getXwid(), curBox.getYwid(),
                    curBox.getZwid() / 2);
            }
        }
        else {
            throw new IllegalArgumentException("Bad type of box: " + type);
        }
        return result;
    }


    // ----------------------------------------------------------
    /**
     * print out all the class information
     *
     * @return the information string
     */
    public String toString() {

        StringBuilder s = new StringBuilder();
        int count = root.toString(getStartingBox(), 1, s);
        s.append(count).append(" bintree nodes printed");
        return s.toString();
    }


    /**
     * based on the given box, the level integer(dimension type) and the forward
     * or backward status to generate a new Box to describe the new status of
     * the air space
     *
     * @param curBox
     *            the current box
     * @param level
     *            level integer(dimension type)
     * @param left
     *            the forward or backward status
     * @return the result box
     */
    public Box getNewBox(Box curBox, int level, boolean left) {
        if (level % 3 == 1) {
            // X
            return getNewBox(curBox, "x", left);
        }
        if (level % 3 == 2) {
            // Y
            return getNewBox(curBox, "y", left);
        }
        return getNewBox(curBox, "z", left);

    }


    /**
     * find all the intersect of the given box in the list
     *
     * @param intersectBox
     *            the compared box
     * @return the string of all the intersected box
     */
    public String intersect(Box intersectBox) {
        DLList<AirObject> list = new DLList<>();
        StringBuilder s = new StringBuilder();
        int count = root.intersect(intersectBox, getStartingBox(), list, 1);
        for (AirObject each : list) {
            s.append(each + "\n");
        }
        s.append(count + " nodes were visited in the bintree");
        return s.toString();
    }


    /**
     * print all the collisions in the list
     *
     * @return all the collisions
     */
    public String collisions() {
        StringBuilder s = new StringBuilder();
        DLList<CollisionObject> list = new DLList<>();
        s.append(root.collisions(list));
        return s.toString();
    }


    /**
     * get the fly weight node
     * 
     * @return fly weight node
     */
    public Node getFlyWeight() {
        return flyWeight;
    }

}
