import java.io.IOException;

/**
 * we use buffer pool to access the file,
 * then the use quick sort to sort entire file using buffer pool
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

/**
 * The class containing the main method, the entry point of the application.
 *
 *
 * @author jiahui huang<hjiahui7>
 * @author Yusheng Cao<cyschris>
 * @version 2018.9.4
 */
public class VirtualSort {

    /**
     * This method is used to generate a file of a certain size, containing a
     * specified number of records.
     *
     * @param filename
     *            the name of the file to create/write to
     * @param blockSize
     *            the size of the file to generate
     * @param format
     *            the format of file to create
     * @throws IOException
     *             throw if the file is not open and proper
     */
    public static void generateFile(
        String filename,
        String blockSize,
        char format)
        throws IOException {
        FileGenerator generator = new FileGenerator();
        String[] inputs = new String[3];
        inputs[0] = "-" + format;
        inputs[1] = filename;
        inputs[2] = blockSize;
        generator.generateFile(inputs);
    }


    /**
     * main function, provide entry point of the application
     * 
     * @param args
     *            the parameter
     * @throws Exception
     *             the exception
     */
    public static void main(String[] args) throws Exception {
        // generateFile("Binary.txt", "1000", 'a');
        BufferPool bu = new BufferPool(Integer.valueOf(args[1]), args[0]);
        // BufferPool bu = new BufferPool(10, "4M.txt");
        QuickSort qsf = new QuickSort(bu);
        long startTime = System.currentTimeMillis();
        qsf.startSort();
        new WriteFile(bu, args[0], Integer.valueOf(args[1]), startTime, args[2]
            .replaceAll(".txt", "")).writerData();
    }
}
