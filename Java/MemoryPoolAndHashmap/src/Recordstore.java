
import java.io.FileNotFoundException;

/**
 * create a record store to manage the data
 *
 * @author jiahui huang<hjiahui7>
 * @author Yusheng Cao<cyschris>
 * @version 2018.9.4
 */

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

public class Recordstore {

    /**
     * main method which run the program
     * 
     * @param args
     *            the running file
     * @throws FileNotFoundException
     *             throw when file is not found
     */
    public static void main(String[] args) throws FileNotFoundException {
        int memory = Integer.valueOf(args[0]);
        int hashSize = Integer.valueOf(args[1]);
        String fileName = args[2];
        Reader reader = new Reader(memory, hashSize);
        reader.reader(fileName);
    }

}
