/**
 * This class create a internalNode which is the basic node of the binary tree
 * internoad is not directey created, it's created by other kinds of node
 *
 * @author jiahui huang<hjiahui7>
 * @author Yusheng Cao<cyschris>
 * @version 2018.11.23
 */
public class InternalNode extends Node {

    private Node left;
    private Node right;


    /**
     * Contractor of FlyWeightNode Class
     *
     * @param tree
     *            required binary tree
     */
    public InternalNode(Bintree tree) {
        super(tree);
        left = tree.getFlyWeight();
        right = tree.getFlyWeight();
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
    public Node insert(AirObject data, Box curBox, int level) {
        Box leftBox = getTree().getNewBox(curBox, level, true);
        Box rightBox = getTree().getNewBox(curBox, level, false);
        left = left.insert(data, leftBox, level + 1);
        right = right.insert(data, rightBox, level + 1);
        return this;
    }


    /**
     * print the node information
     *
     * @param curBox
     *            current box contain range
     * @param level
     *            level number
     * @return stirng contain data
     */
    @Override
    public int toString(Box curBox, int level, StringBuilder s) {
        int count = 1;
        for (int i = 0; i < (level - 1) * 2; i++) {
            s.append(" ");
        }
        s.append("I\n");
        count = count + left.toString(getTree().getNewBox(curBox, level, true),
            level + 1, s);
        count = count + right.toString(getTree().getNewBox(curBox, level,
            false), level + 1, s);
        return count;
    }


    /**
     * remove airobject from the node
     *
     * @param air
     *            the airobject need to be removed
     * @return the new node after remove
     */
    @Override
    public Node remove(AirObject air) {
        this.left = left.remove(air);
        this.right = right.remove(air);
        if (left instanceof InternalNode || right instanceof InternalNode) {
            return this;
        }
        else if (left != getTree().getFlyWeight() && right == getTree()
            .getFlyWeight()) {
            return left;
        }
        else if (left == getTree().getFlyWeight() && right != getTree()
            .getFlyWeight()) {
            return right;
        }
        Node temp = checkMerge(left, right);
        if (temp == null) {
            return this;
        }
        return temp;
    }


    // ----------------------------------------------------------
    /**
     * Check the merge in the binarg tree
     *
     * @param leftNode
     *            left node in the binary tree
     * @param rightNode
     *            right node in the binary tree
     * @return new dataNode after the process
     */
    public Node checkMerge(Node leftNode, Node rightNode) {
        DataNode temp = new DataNode(getTree());
        for (AirObject each : leftNode.getData()) {
            temp.addData(each);
        }
        for (AirObject each : rightNode.getData()) {
            if (!temp.getData().contains(each)) {
                if (temp.isFull(each)) {
                    return null;
                }
                temp.addData(each);
            }
        }
        return temp;

    }


    /**
     * find intersect between the given box and data list in this node
     *
     * @param insectBox
     *            box contain intersect range
     * @param curBox
     *            box contain current range
     * @param list
     *            the DLLIST contain airObject
     * @param level
     *            level number
     */
    @Override
    public int intersect(
        Box insectBox,
        Box curBox,
        DLList<AirObject> list,
        int level) {
        int count = 1;
        Box leftBox = getTree().getNewBox(curBox, level, true);
        Box rightBox = getTree().getNewBox(curBox, level, false);
        if (CheckSize.checkTwoBox(insectBox, leftBox)) {
            count = count + left.intersect(insectBox, leftBox, list, level + 1);
        }
        if (CheckSize.checkTwoBox(insectBox, rightBox)) {
            count = count + right.intersect(insectBox, rightBox, list, level
                + 1);
        }
        return count;
    }


    /**
     * find all the collistions between data and list and then print them out
     *
     * @param list
     *            DLLIst contain CollisionObject
     * @return result
     */
    @Override
    public String collisions(DLList<CollisionObject> list) {
        StringBuilder s = new StringBuilder();
        s.append(left.collisions(list));
        s.append(right.collisions(list));
        return s.toString();
    }


    /**
     * get the size of the data
     *
     * @return data size
     */
    @Override
    public int getSize() {
        return 0;
        // internal Node do not have size
    }


    /**
     * get the data list
     *
     * @return data
     */
    @Override
    public DLList<AirObject> getData() {
        return null;
        // internal node do not have node
    }

}
