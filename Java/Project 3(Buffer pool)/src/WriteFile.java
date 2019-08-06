
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * write file provide output file of the result.
 *
 * @author jiahui huang<hjiahui7>
 * @author Yusheng Cao<cyschris>
 * @version 2018.10.24
 */
public class WriteFile {

    private BufferPool bu;
    private int blockNumber;
    private String filename;
    private int bufferNumber;
    private long startTime;
    private String type;


    // ----------------------------------------------------------

    /**
     * /**
     * Create a new WriteFile object.
     * we need to show the speed of our project
     * running and hits and counts
     *
     * @param bu
     *            new bufferpool
     * @param name
     *            name
     * @param buffer
     *            buffernumber
     * @param startTime
     *            new starttime
     * @param filename
     *            the file name we create
     */
    public WriteFile(
        BufferPool bu,
        String name,
        int buffer,
        long startTime,
        String filename) {
        this.bu = bu;
        this.filename = filename;
        type = name;
        blockNumber = bu.getBlockSize();
        bufferNumber = buffer;
        this.startTime = startTime;
    }


    // ----------------------------------------------------------
    /**
     * write data
     *
     * @throws IOException
     */
    public void writerData() throws IOException {
        File fout = new File(filename);
        FileWriter out = new FileWriter(fout, true);
        StringBuilder baseInfor = new StringBuilder();
        baseInfor.append("\"").append(type).append("\" file, ").append(
            blockNumber).append(" blocks, ").append(bufferNumber).append(
                " buffers:\n");
        out.write(baseInfor.toString());
        out.write("Cache Hits: " + bu.getCacheCount() + "\r\n");
        out.write("Cache Miss: " + bu.getCacheMiss() + "\r\n");
        out.write("Disk Reads: " + bu.getDeskRead() + "\r\n");
        out.write("Disk Writes: " + bu.getDeskWrite() + "\r\n");
        out.write("Time: " + (System.currentTimeMillis() - startTime) + " ms");
        out.write("\r\n" + "\r\n");
        out.close();
    }
}
