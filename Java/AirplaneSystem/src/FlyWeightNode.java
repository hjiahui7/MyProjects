/**
 * This class test the InternalNode class
 *
 * @author jiahui huang<hjiahui7>
 * @author Yusheng Cao<cyschris>
 * @version 2018.11.22
 */
public class FlyWeightNode extends Node {

    /**
     * Contractor of FlyWeightNode Class
     *
     * @param tree
     *            required binary tree
     */
    public FlyWeightNode(Bintree tree) {
        super(tree);
    }


    /**
     * print the node contentF
     */
    @Override
    public int toString(Box curBox, int level, StringBuilder s) {
        for (int i = 0; i < (level - 1) * 2; i++) {
            s.append(" ");
        }
        s.append("E\n");
        return 1;
    }


    /**
     * add data to a new dataNode
     *
     * @param data
     *            airobject plane information
     * @param curBox
     *            current box
     * @param level
     *            the level
     * @return node after inserting
     */
    @Override
    public Node insert(AirObject data, Box curBox, int level) {
        if (CheckSize.checkCollision(data, curBox)) {
            Node newData = new DataNode(getTree());
            return newData.insert(data, curBox, level + 1);
        }
        return this;
    }


    /**
     * there is no remove for this node so just get itself
     *
     * @param air
     *            the airobject
     * @return this node
     */
    @Override
    public Node remove(AirObject air) {
        return this;
    }


    /**
     * track the intersect by assending the binary tree's size of travel
     *
     * @param intersect
     *            the box of intersect
     * @param curBox
     *            the current box
     * @param list
     *            the list contain airobject information
     */
    @Override
    public int intersect(
        Box intersect,
        Box curBox,
        DLList<AirObject> list,
        int level) {
        return 1;
    }


    /**
     * track collisions since there are no collision return ""
     *
     * @param list
     *            the lsit contain collisionobjet information
     * @return ""
     */
    @Override
    public String collisions(DLList<CollisionObject> list) {
        return "";
    }


    /**
     * track size since there is no size for this node return o
     *
     * @return 0
     */
    @Override
    public int getSize() {
        return 0;
    }


    /**
     * track data store in the node since there is no data return null
     *
     * @return null
     */
    @Override
    public DLList<AirObject> getData() {
        return null;
    }
}
