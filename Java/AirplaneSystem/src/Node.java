/**
 * Besic abstract class for all three kinds of nodes in this class
 *
 * @author jiahui huang<hjiahui7>
 * @author Yusheng Cao<cyschris>
 * @version 2018.11.22
 */
public abstract class Node {
    /**
     * tree which provide the data management
     */
    private final Bintree tree;


    /**
     * Contractor of Node Class
     *
     * @param tree
     *            required binary tree
     */
    public Node(Bintree tree) {
        this.tree = tree;
    }


    /**
     * insert new data form a airobject into the node
     *
     * @param data
     *            the airobject contain data
     * @param curBox
     *            current box contain range
     * @param level
     *            level number
     * @return node
     */
    public abstract Node insert(AirObject data, Box curBox, int level);


    /**
     * print the node information
     *
     * @param curBox
     *            current box contain range
     * @param level
     *            level number
     * @param s
     *            the string builder that will store all the datas
     * @return the number of node we visit
     */
    public abstract int toString(Box curBox, int level, StringBuilder s);


    /**
     * remove airobject from the node
     *
     * @param air
     *            the airobject need to be removed
     * @return the new node after remove
     */
    public abstract Node remove(AirObject air);


    /**
     * find intersect between the given box and data list in this node
     *
     * @param intersect
     *            box contain intersect range
     * @param curBox
     *            box contain current range
     * @param list
     *            the DLLIST contain airObject
     * @param level
     *            level number
     * @return a number of the node that we visit
     */
    public abstract int intersect(
        Box intersect,
        Box curBox,
        DLList<AirObject> list,
        int level);


    /**
     * find all the collistions between data and list and then print them out
     *
     * @param list
     *            DLLIst contain CollisionObject
     * @return result
     */
    public abstract String collisions(DLList<CollisionObject> list);


    /**
     * get the size of the item in node
     *
     * @return node size
     */
    public abstract int getSize();


    /**
     * get the data list
     *
     * @return data
     */
    public abstract DLList<AirObject> getData();


    /**
     * get tree
     * 
     * @return tree
     */
    public Bintree getTree() {
        return tree;
    }

}
