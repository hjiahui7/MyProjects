/**
 * This is a Node which store data in a linked list
 *
 * @author jiahui huang<hjiahui7>
 * @author Yusheng Cao<cyschris>
 * @version 2018.11.22
 */
public class DataNode extends Node {

    private DLList<AirObject> data;


    /**
     * Contractor of the DataNode class
     *
     * @param tree
     *            a binary tree object
     */
    public DataNode(Bintree tree) {
        super(tree);
        data = new DLList<AirObject>();
    }


    /**
     * add data to the Node
     *
     * @param d
     *            new AirObject need to be added
     */
    public void addData(AirObject d) {
        Iterator<AirObject> iterator = data.iterator();
        int count = 0;
        boolean find = false;
        while (iterator.hasNext()) {
            AirObject temp = iterator.next();
            if (temp.compareTo(d) > 0) {
                data.add(count, d);
                find = true;
                break;
            }
            count++;
        }
        if (!find) {
            data.add(count, d);
        }
    }


    // ----------------------------------------------------------
    /**
     * add all the data in the list into internal node
     *
     * @param internalNode
     *            the node which all the dat a in the list add into
     * @param d
     *            a airobject
     * @param curBox
     *            current box
     * @param level
     *            level number
     */
    public void splitAdd(
        InternalNode internalNode,
        AirObject d,
        Box curBox,
        int level) {
        internalNode.insert(d, curBox, level);
        for (int i = 0; i < data.size(); i++) {
            internalNode.insert(this.data.get(i), curBox, level);
        }
    }


    /**
     * create a internal Node and add airobject into it if the airobject has no
     * collision with the currentbox and then return it, if collision happened
     * do nothing and return this node.
     *
     * @param d
     *            airobject need to be added
     * @param curBox
     *            range of the air space
     * @param level
     *            level number
     * @return the new internalNode
     */
    public Node insert(AirObject d, Box curBox, int level) {
        if (CheckSize.checkCollision(d, curBox)) {
            if (!isFull(d)) {
                addData(d);
                return this;
            }
            InternalNode internalNode = new InternalNode(getTree());
            splitAdd(internalNode, d, curBox, level);
            return internalNode;
        }
        return this;
    }


    /**
     * print all the data in this node
     *
     * @param curBox
     *            current box
     * @param level
     *            level number
     * @return the result
     */
    @Override
    public int toString(Box curBox, int level, StringBuilder s) {
        for (int i = 0; i < (level - 1) * 2; i++) {
            s.append(" ");
        }
        s.append("Leaf with ").append(data.size()).append(" objects:\n");
        for (int i = 0; i < data.size(); i++) {
            for (int j = 0; j < (level - 1) * 2; j++) {
                s.append(" ");
            }
            AirObject object = data.get(i);
            String string = object.toString();
            s.append("(").append(string).append(")\n");
        }
        return 1;
    }


    /**
     * check the node is full after add a new airobject
     *
     * @param newData
     *            new AirObject
     * @return is full true or not
     */
    public boolean isFull(AirObject newData) {
        if (data.size() < 3) {
            return false;
        }
        DLList<AirObject> copy = new DLList<AirObject>();
        copy.add(newData);
        for (AirObject object : data) {
            copy.add(object);
        }
        for (int i = 0; i < copy.size(); i++) {
            for (int j = i + 1; j < copy.size(); j++) {
                if (!CheckSize.checkTwoAir(copy.get(i), copy.get(j))) {
                    return true;
                }
            }
        }
        return false;
    }


    /**
     * remove the given airobject from the node
     *
     * @param air
     *            given airobject
     * @return thenode
     */
    @Override
    public Node remove(AirObject air) {
        data.remove(air);
        if (data.isEmpty()) {
            return getTree().getFlyWeight();
        }
        return this;
    }


    /**
     * find intersect between the given box and data list in this node
     *
     * @param intersectBox
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
        Box intersectBox,
        Box curBox,
        DLList<AirObject> list,
        int level) {
        for (int j = 0; j < data.size(); j++) {
            if (CheckSize.checkCollision(data.get(j), intersectBox)) {
                if (!list.contains(data.get(j))) {
                    list.add(data.get(j));
                }
            }
        }
        return 1;
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
        if (data.size() < 2) {
            return "";
        }
        for (int i = 0; i < data.size(); i++) {
            for (int j = i + 1; j < data.size(); j++) {
                if (CheckSize.checkTwoAir(data.get(i), data.get(j))) {
                    CollisionObject coll = new CollisionObject(data.get(i), data
                        .get(j));
                    boolean find = false;
                    for (CollisionObject each : list) {
                        if (each.equal(coll)) {
                            find = true;
                        }
                    }
                    if (!find) {
                        list.add(coll);
                        s.append("(").append(data.get(i)).append(") and (")
                            .append(data.get(j)).append(")\n");
                    }
                }
            }
        }
        return s.toString();
    }


    /**
     * get the size of the data
     *
     * @return data size
     */
    @Override
    public int getSize() {
        return data.size();
    }


    /**
     * get the data list
     *
     * @return data
     */
    @Override
    public DLList<AirObject> getData() {
        return data;
    }

}
