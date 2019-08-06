/**
 * This class us a skip list and a binary tree to provide a base function of
 * database. It can add and delete data and show the data already stored.
 *
 * @author jiahui huang<hjiahui7>
 * @author Yusheng Cao<cyschris>
 * @version 2018.11.22
 */
public class Database {

    private SkipList<String, AirObject> skipList;
    private Bintree bintree;
    /**
     * the total size of box
     */
    public static final int TOTAL_BOX_SIZE = 3;


    /**
     * Constructor for ADatabase class
     */
    public Database() {
        skipList = new SkipList<String, AirObject>();
        bintree = new Bintree(TOTAL_BOX_SIZE);
    }


    /**
     * this method print all the data in the binary tree
     */
    public void printBintree() {
        StringBuilder sBuilder = new StringBuilder();
        sBuilder.append("Bintree dump:\n");
        sBuilder.append(bintree.toString());
        System.out.println(sBuilder.toString());
    }


    /**
     * this method add new data into the skip list and binary tree
     *
     * @param datas
     *            data about the new object
     */
    public void add(String[] datas) {
        if (skipList.find(datas[2]) != null) {
            System.out.println("Duplicate object names not permitted: |"
                + datas[2] + "|");
            return;
        }
        AirObject obj = new AirObject(datas);
        if (obj.getXwid() <= 0 || obj.getX() < 0 || obj.getYwid() <= 0 || obj
            .getY() < 0 || obj.getZwid() <= 0 || obj.getZ() < 0) {
            String s = "Bad box (" + obj.getX() + " " + obj.getY() + " " + obj
                .getZ() + " " + obj.getXwid() + " " + obj.getYwid() + " " + obj
                    .getZwid() + "). All widths must be positive.";
            System.out.println(s);
            return;
        }
        if (obj.getXwid() + obj.getX() > 1024 || obj.getYwid() + obj
            .getY() > 1024 || obj.getZwid() + obj.getZ() > 1024) {
            String s = "Bad box (" + obj.getX() + " " + obj.getY() + " " + obj
                .getZ() + " " + obj.getXwid() + " " + obj.getYwid() + " " + obj
                    .getZwid()
                + "). All boxes must be entirely within the world box.";
            System.out.println(s);
            return;
        }

        skipList.insert(obj.getName(), obj);
        bintree.insert(obj);
        System.out.println(obj.getName() + " has been added to the database");
    }


    // ----------------------------------------------------------
    /**
     * This class print specific object in the skipList;
     *
     * @param string
     *            name of the object
     */
    public void printObject(String string) {
        AirObject thisone = skipList.find(string);
        if (thisone != null) {
            System.out.println("Found: " + thisone.toString());
        }
        else {
            System.out.println("|" + string
                + "| does not exist in the database");
        }
    }


    /**
     * this class will print object in the skiplist between given range
     *
     * @param string
     *            first parameter
     * @param string2
     *            second parameter
     */
    public void rangePrint(String string, String string2) {
        StringBuilder s = new StringBuilder();
        if (string.compareTo(string2) > 0) {
            s.append("Error in rangeprint parameters: |").append(string).append(
                "| is not less than |").append(string2).append("|");
            System.out.println(s.toString());
            return;
        }
        s.append("Found these records in the range |").append(string).append(
            "| to |").append(string2).append("|\n");
        Object[] datas = skipList.getAllData();
        int i = 0;
        if (string.length() == 1 && string2.length() == 1) {
            for (; i < datas.length; i++) {
                AirObject temp = (AirObject)datas[i];
                if (temp.getName().substring(0, string.length()).compareTo(
                    string) >= 0 && temp.getName().substring(0, string2
                        .length()).compareTo(string2) <= 0) {
                    s.append(temp.toString() + "\n");
                }
            }
        }
        else {
            for (; i < datas.length; i++) {
                AirObject temp = (AirObject)datas[i];
                if (temp.getName().compareTo(string) >= 0 && temp.getName()
                    .compareTo(string2) <= 0) {
                    s.append(temp.toString() + "\n");
                }
            }
        }
        System.out.print(s.toString());
    }


    /**
     * this method will delete specific record in the binary tree and skiplist
     *
     * @param string
     *            the string to locate the record
     */
    public void delete(String string) {
        AirObject air = skipList.find(string);
        if (air == null) {
            System.out.println("Object |" + string + "| not in the database");
            return;
        }
        AirObject thisone = skipList.remove(string);
        bintree.remove(thisone);
        System.out.println("Deleted |" + string + "| from the database");
    }


    /**
     * this mehtod will print all the record in the skiplist
     */
    public void printSkipList() {
        System.out.println(skipList.toString());
    }


    /**
     * locate the intersect objects in the binary tree
     *
     * @param valueOf1
     *            x
     * @param valueOf2
     *            y
     * @param valueOf3
     *            z
     * @param valueOf4
     *            x width
     * @param valueOf5
     *            y width
     * @param valueOf6
     *            z width
     */
    public void intersect(
        Integer valueOf1,
        Integer valueOf2,
        Integer valueOf3,
        Integer valueOf4,
        Integer valueOf5,
        Integer valueOf6) {
        Box curBox = new Box(valueOf1, valueOf2, valueOf3, valueOf4, valueOf5,
            valueOf6);
        if (curBox.getXwid() <= 0 || curBox.getX() < 0 || curBox.getYwid() <= 0
            || curBox.getY() < 0 || curBox.getZwid() <= 0 || curBox
                .getZ() < 0) {
            String s = "Bad box (" + curBox.getX() + " " + curBox.getY() + " "
                + curBox.getZ() + " " + curBox.getXwid() + " " + curBox
                    .getYwid() + " " + curBox.getZwid()
                + "). All widths must be positive.";
            System.out.println(s);
            return;
        }
        if (curBox.getXwid() + curBox.getX() > 1024 || curBox.getYwid() + curBox
            .getY() > 1024 || curBox.getZwid() + curBox.getZ() > 1024) {
            String s = "Bad box (" + curBox.getX() + " " + curBox.getY() + " "
                + curBox.getZ() + " " + curBox.getXwid() + " " + curBox
                    .getYwid() + " " + curBox.getZwid()
                + "). All boxes must be entirely within the world box.";
            System.out.println(s);
            return;
        }
        StringBuilder s = new StringBuilder();
        s.append("The following objects intersect (" + valueOf1 + " " + valueOf2
            + " " + valueOf3 + " " + valueOf4 + " " + valueOf5 + " " + valueOf6
            + "):\n");
        s.append(bintree.intersect(curBox));
        System.out.println(s.toString());
    }


    /**
     * this method will print all collisions in the binary tree
     */
    public void collisions() {
        System.out.println("The following collisions exist in the database:");
        System.out.print(bintree.collisions());
    }

}
