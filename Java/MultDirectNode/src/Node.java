/**
 * create a base unit to store inforamtion which contain data location and all
 * the four nodes connected to it.
 *
 * @author jiahui huang<hjiahui7>
 * @author Yusheng Cao<cyschris>
 * @version 2018.9.20
 *
 * @param <T>
 */
public class Node<T> {

    /**
     * the field for node
     */
    private T data;
    private Node<T> left;
    private Node<T> right;
    private Node<T> up;
    private Node<T> down;
    private int xId;
    private int yId;


    /**
     * constractor create the node
     * 
     * @param data
     *            the data store in this node
     * @param left
     *            the node on the left
     * @param right
     *            the node on the right
     * @param up
     *            the node above
     * @param down
     *            the node below
     * @param xId
     *            the x position of current node
     * @param yId
     *            the y position of current node
     */
    public Node(
        T data,
        Node<T> left,
        Node<T> right,
        Node<T> up,
        Node<T> down,
        int xId,
        int yId) {
        this.data = data;
        this.left = left;
        this.right = right;
        this.up = up;
        this.down = down;
        this.xId = xId;
        this.yId = yId;
    }


    /**
     * get the data store in this node
     * 
     * @return the data
     */
    public T getData() {
        return data;
    }


    /**
     * change the data store in this node
     * 
     * @param data
     *            new data
     */
    public void setData(T data) {
        this.data = data;
    }


    /**
     * get the left node
     * 
     * @return node on the left
     */
    public Node<T> getLeft() {
        return left;
    }


    /**
     * set the node on the left
     * 
     * @param left
     *            new node on the left
     */
    public void setLeft(Node<T> left) {
        this.left = left;
    }


    /**
     * get the right node
     * 
     * @return node on the right
     */
    public Node<T> getRight() {
        return right;
    }


    /**
     * set the node on the right
     * 
     * @param right
     *            new node on the right
     */
    public void setRight(Node<T> right) {
        this.right = right;
    }


    /**
     * get the top node
     * 
     * @return node on the top
     */
    public Node<T> getUp() {
        return up;
    }


    /**
     * set the node on the top
     * 
     * @param up
     *            new node on the top
     */
    public void setUp(Node<T> up) {
        this.up = up;
    }


    /**
     * get the down node
     * 
     * @return node below
     */
    public Node<T> getDown() {
        return down;
    }


    /**
     * set the node below
     * 
     * @param down
     *            new node below
     */
    public void setDown(Node<T> down) {
        this.down = down;
    }


    /**
     * get the x index
     * 
     * @return x index
     */
    public int getxId() {
        return xId;
    }


    /**
     * change the x index
     * 
     * @param xId
     *            x index
     */
    public void setxId(int xId) {
        this.xId = xId;
    }


    /**
     * get the y index
     * 
     * @return y index
     */
    public int getyId() {
        return yId;
    }


    /**
     * change the y index
     * 
     * @param yId
     *            new y index
     */
    public void setyId(int yId) {
        this.yId = yId;
    }

}
