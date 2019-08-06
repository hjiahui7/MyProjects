import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import student.TestCase;

/**
 * test the record store class
 *
 * @author jiahui huang<hjiahui7>
 * @author Yusheng Cao<cyschris>
 * @version 2018.9.4
 */
public class RecordstoreTest extends TestCase {
    /**
     * the field
     */
    private Recordstore recstore;


    /**
     * read the file
     * 
     * @param path
     *            input path
     * @return a string
     * @throws IOException
     *             exception
     */
    static String readFile(String path) throws IOException {
        byte[] encoded = Files.readAllBytes(Paths.get(path));
        return new String(encoded);
    }


    /***
     * Test syntax: Sample Input/Output* Comparing output in a file** @throws
     * Exception
     */
    @SuppressWarnings("static-access")
    public void testSampleInputFile() throws Exception {
        String[] args = new String[3];
        args[0] = "32";
        args[1] = "10";
        args[2] = "P1sampleInputText.txt";
        recstore.main(args);
        assertFuzzyEquals(readFile("P1sampleOutput.txt"), systemOut()
            .getHistory());
    }

}
