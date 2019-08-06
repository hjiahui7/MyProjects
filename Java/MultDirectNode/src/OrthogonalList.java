/**
 * * create orthogonallist to store information
 *
 * @author jiahui huang<hjiahui7>
 * @author Yusheng Cao<cyschris>
 * @version 2018.9.20
 * @param <T>
 *            data
 */
public class OrthogonalList<T> {

    // ----------------------------------------------------------
    /**
     * get number of x insert
     *
     * @return the nmber
     */
    public int getNumberOfXInsert() {
        return numberOfXInsert;
    }


    // ----------------------------------------------------------
    /**
     * get number of y insert
     *
     * @return the nmber
     */
    public int getNumberOfYInsert() {
        return numberOfYInsert;
    }

    /**
     * the field for OrthogonalList
     */
    private Node<T> certer;
    private int numberOfX;
    private int numberOfXInsert;
    private int numberOfYInsert;
    private int numberOfY;
    private int size;


    /**
     * initialize the orthogonallist
     */
    public OrthogonalList() {
        numberOfX = 0;
        numberOfY = 0;
        numberOfXInsert = 0;
        numberOfYInsert = 0;
        size = 0;
        certer = new Node<T>(null, null, null, null, null, -1, -1);
    }


    /**
     * add new data in the given position
     *
     * @param review
     *            node which contain review data
     * @param movie
     *            node which contain movie data
     * @param dataInsert
     *            new data
     * @return a node array
     */
    @SuppressWarnings("rawtypes")
    public Node[] insert(Node<T> review, Node<T> movie, T dataInsert) {
        Node[] twoNode = new Node[2];
        if (review == null) {
            twoNode = addInNewRow(review, movie, dataInsert);
        }
        else {
            twoNode = addInThisRow(review, movie, dataInsert);
        }
        return twoNode;
    }


    /**
     * helper method to add the data into a new row
     *
     * @param review
     *            node which contain review data
     * @param movie
     *            node which contain movie data
     * @param dataInsert
     *            new data
     * @return Node array
     */
    @SuppressWarnings("unchecked")
    public Node<T>[] addInNewRow(Node<T> review, Node<T> movie, T dataInsert) {
        Node<T>[] twoNode = new Node[2];
        Node<T> cur = certer;
        while (cur.getRight() != null) {
            cur = cur.getRight();
        }
        Node<T> newCol = new Node<T>(null, cur, null, null, null,
            numberOfXInsert, -1);
        cur.setRight(newCol);
        Node<T> newNode = new Node<T>(dataInsert, null, null, newCol, null,
            numberOfXInsert, -1);
        twoNode[0] = newCol;
        twoNode[1] = findNeighbors(movie, newNode, dataInsert);
        newNode.setyId(twoNode[1].getyId());
        newNode.setUp(newCol);
        newCol.setDown(newNode);
        size++;
        numberOfX++;
        numberOfXInsert++;
        return twoNode;
    }


    /**
     * helper method to add the data into the current row
     *
     * @param review
     *            node which contain review data
     * @param movie
     *            node which contain movie data
     * @param dataInsert
     *            new data
     * @return return a node array
     */
    @SuppressWarnings("unchecked")
    public Node<T>[] addInThisRow(Node<T> review, Node<T> movie, T dataInsert) {
        Node<T> cur = review;
        Node<T>[] twoNode = new Node[2];
        twoNode[0] = review;
        if (movie == null) {
            while (cur.getDown() != null) {
                cur = cur.getDown();
            }
            Node<T> newNode = new Node<T>(dataInsert, null, null, cur, null,
                review.getxId(), this.numberOfYInsert);
            cur.setDown(newNode);
            twoNode[1] = findNeighbors(movie, newNode, dataInsert);
            size++;
        }
        else {
            while (cur != null) {
                if (cur.getyId() == movie.getyId()) {
                    twoNode[1] = findNeighbors(movie, cur, dataInsert);
                    break;
                }
                else if (movie.getyId() < cur.getyId()) {
                    Node<T> newNode = new Node<T>(dataInsert, null, null, cur
                        .getUp(), cur, review.getxId(), movie.getyId());
                    cur.getUp().setDown(newNode);
                    cur.setUp(newNode);
                    twoNode[1] = findNeighbors(movie, newNode, dataInsert);
                    size++;
                    break;
                }
                else if (cur.getDown() == null) {
                    Node<T> newOne = new Node<T>(dataInsert, null, null, cur,
                        null, review.getxId(), movie.getyId());
                    findNeighbors(movie, newOne, dataInsert);
                    newOne.getUp().setDown(newOne);
                    size++;
                    break;
                }
                cur = cur.getDown();
            }
        }
        return twoNode;
    }


    /**
     * helper method which locate nearby node
     *
     * @param movie
     *            node which contain movie data
     * @param dataInsert
     *            new data
     * @param newNodeInsert
     *            new node
     * @return the neighbor node
     */
    public Node<T> findNeighbors(
        Node<T> movie,
        Node<T> newNodeInsert,
        T dataInsert) {
        Node<T> curNode = movie;
        Node<T> movieNode = null;
        if (movie == null) {
            curNode = certer;
            while (curNode.getDown() != null) {
                curNode = curNode.getDown();
            }
            Node<T> newNode = new Node<T>(null, null, newNodeInsert, curNode,
                null, -1, numberOfYInsert);
            newNode.getUp().setDown(newNode);
            newNodeInsert.setLeft(newNode);
            numberOfY++;
            numberOfYInsert++;
            return newNode;
        }
        movieNode = movie;
        while (curNode != null) {
            if (curNode == newNodeInsert) {
                curNode.setData(dataInsert);
                break;
            }
            else if (newNodeInsert.getxId() < curNode.getxId()) {
                curNode.getLeft().setRight(newNodeInsert);
                newNodeInsert.setRight(curNode);
                newNodeInsert.setLeft(curNode.getLeft());
                curNode.setLeft(newNodeInsert);
                break;
            }
            else if (curNode.getRight() == null) {
                curNode.setRight(newNodeInsert);
                newNodeInsert.setLeft(curNode);
                break;
            }
            curNode = curNode.getRight();
        }
        return movieNode;
    }


    /**
     * remove the entire column
     *
     * @param indexY
     *            y index which indcate the column
     * @return remove status
     */
    public boolean removeCol(Node<T> indexY) {
        if (indexY == null) {
            return false;
        }
        Node<T> curNode = indexY;
        while (curNode != null) {
            Node<T> upNode = curNode.getUp();
            Node<T> downNode = curNode.getDown();
            if (downNode != null) {
                downNode.setUp(upNode);
            }
            upNode.setDown(downNode);
            Node<T> nextone = curNode.getRight();
            curNode = null;
            curNode = nextone;
            size--;
        }
        numberOfY--;
        size++;
        return true;
    }


    /**
     * remove the entire row
     *
     * @param indexX
     *            x index which indcate the column
     * @return remove status
     */
    public boolean removeRow(Node<T> indexX) {
        Node<T> curNode = indexX;
        if (indexX != null) {
            while (curNode != null) {
                Node<T> rightNode = curNode.getRight();
                Node<T> leftNode = curNode.getLeft();
                if (rightNode != null) {
                    rightNode.setLeft(leftNode);
                }
                leftNode.setRight(rightNode);
                Node<T> nextone = curNode.getDown();
                curNode = null;
                curNode = nextone;
                size--;
            }
            numberOfX--;
            size++;
            return true;
        }
        return false;

    }


    /**
     * get a array which contain all the index from a single column
     *
     * @param indexY
     *            y index which indexcatie the column
     * @return the index array
     */
    @SuppressWarnings("rawtypes")
    public Node[] getAllXNode(Node<T> indexY) {
        Node<T> curNode = indexY;
        Node[] temp = new Node[this.numberOfX];
        int count = 0;
        boolean find = false;
        curNode = curNode.getRight();
        while (curNode != null) {
            temp[count] = curNode;
            count++;
            find = true;
            curNode = curNode.getRight();
        }
        if (find) {
            Node[] xAxis = new Node[count];
            for (int i = 0; i < count; i++) {
                xAxis[i] = temp[i];
            }
            return xAxis;
        }
        return null;
    }


    // ----------------------------------------------------------
    /**
     * get center node
     *
     * @return node in the center
     */
    public Node<T> getCerter() {
        return certer;
    }


    /**
     * get a array which contain all the index from a single row
     *
     * @param indexX
     *            x index which indexcatie the row
     * @return the index array
     */
    @SuppressWarnings("rawtypes")
    public Node[] getAllYNode(Node<T> indexX) {
        Node<T> curNode = indexX;
        Node[] temp = new Node[this.numberOfY];
        int count = 0;
        boolean find = false;
        curNode = curNode.getDown();
        while (curNode != null) {
            temp[count] = curNode;
            find = true;
            curNode = curNode.getDown();
            count++;
        }
        if (find) {
            Node[] xAxis = new Node[count];
            for (int i = 0; i < count; i++) {
                xAxis[i] = temp[i];
            }
            return xAxis;
        }
        return null;
    }


    /**
     * get string to describe all the node in one y axix
     *
     * @param yIndex
     *            y number
     * @return string
     */
    public String toStringForOneCol(Node<T> yIndex) {
        Node<T> curNode = yIndex;
        curNode = curNode.getRight();
        StringBuilder str = new StringBuilder();
        boolean find = false;
        while (curNode != null) {
            if (find) {
                str.append(" ");
            }
            str.append(curNode.getxId()).append(":").append(curNode.getData());
            curNode = curNode.getRight();
            find = true;
        }
        return str.toString();
    }


    /**
     * check the list is empty or not
     *
     * @return true or false
     */
    public boolean isEmpty() {
        return size == 0;
    }


    /**
     * get data from node
     *
     * @param indexX
     *            x location
     * @param indexY
     *            y location
     * @return data
     */
    public T getData(Node<T> indexX, Node<T> indexY) {
        Node<T> curNode = indexX;
        while (curNode != null) {
            if (curNode.getyId() == indexY.getyId()) {
                return curNode.getData();
            }
            curNode = curNode.getDown();
        }
        return null;
    }


    /**
     * get the Y number
     *
     * @return Y number
     */
    public int getNumberOfY() {
        return numberOfY;
    }


    /**
     * get the Y number
     *
     * @return X number
     */
    public int getNumberOfX() {
        return numberOfX;
    }


    // ----------------------------------------------------------
    /**
     * get y header
     *
     * @param data
     *            data locate the collon
     * @return the header node
     */
    public Node<T> getYHeader(Node<T> data) {
        Node<T> cur = data;
        while (cur.getLeft() != null) {
            cur = cur.getLeft();
        }
        return cur;
    }


    /**
     * get y header
     *
     * @param data
     *            data locate the collon
     * @return the header node
     */
    public Node<T> getXHeader(Node<T> data) {
        Node<T> cur = data;
        while (cur.getUp() != null) {
            cur = cur.getUp();
        }
        return cur;
    }
}
