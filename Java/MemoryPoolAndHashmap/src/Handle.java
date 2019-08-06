// On my honor:
//
// - I have not used source code obtained from another student,
// or any other unauthorized source, either modified or
// unmodified.
//
// - All source code and documentation used in my program is
// either my original work, or was derived by me from the
// source code published in the textbook for this course.
//
// - I have not discussed coding details about this project with
// anyone other than my partner (in the case of a joint
// submission), instructor, ACM/UPE tutors or the TAs assigned
// to this course. I understand that I may discuss the concepts
// of this program with other students, and that another student
// may help me debug my program so long as neither of us writes
// anything during the discussion or modifies any computer file
// during the discussion. I have violated neither the spirit nor
// letter of this restriction.
/**
 * Create Handle class to store offset and size data.
 *
 * @author jiahui huang<hjiahui7>
 * @author Yusheng Cao<cyschris>
 * @version 2018.9.4
 */
public class Handle {

    private int offset;
    private int size;


    /**
     * create new handle
     * 
     * @param offset
     *            location of data
     * @param size
     *            size of data
     */
    public Handle(int offset, int size) {
        this.size = size;
        this.offset = offset;
    }


    /**
     * return the location of data
     * 
     * @return the location of data
     */
    public int getOffset() {
        return offset;
    }


    /**
     * change the location of data
     * 
     * @param offset
     *            the new location
     */
    public void setOffset(int offset) {
        this.offset = offset;
    }


    /**
     * get the size of the data
     * 
     * @return data size
     */
    public int getSize() {
        return size;
    }


    /**
     * set the size of the data
     * 
     * @param size
     *            new size of data
     */
    public void setSize(int size) {
        this.size = size;
    }
}
