import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import student.TestCase;

/**
 * create a reader class to test the reader class
 *
 * @author jiahui huang<hjiahui7>
 * @author Yusheng Cao<cyschris>
 * @version 2018.9.4
 */
public class ReaderTest extends TestCase {

    /**
     * the field
     */
    private Reader reader;


    /**
     * the setUp for reader test
     */
    public void setUp() {
        reader = new Reader(32, 10);
    }


    /**
     * Read contents of a file into a string
     * the string
     * 
     * @param path
     *            the input path
     * @return a string
     * @throws IOException
     *             the exception
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
        new Recordstore().main(args);
        assertFuzzyEquals(readFile("P1sampleOutput.txt"), systemOut()
            .getHistory());
    }


    /**
     * test add method
     */
    public void testAdd() {
        assertTrue(reader.add("add banaa"));
        assertFalse(reader.add("add banaa"));
    }


    /**
     * test UpdateAdd method
     */
    public void testUpdateAdd() {
        assertFalse(reader.updateAdd("no exit"));
        assertTrue(reader.add("add banaa"));
        assertTrue(reader.updateAdd("banaa<SEP>name<SEP>value"));
        assertFalse(reader.updateAdd("banaa<SEP>name<SEP>value"));
        assertTrue(reader.updateAdd("banaa<SEP>name2<SEP>value2"));
        assertTrue(reader.updateAdd("banaa<SEP>name2<SEP>changevalue2"));
    }


    /**
     * test UpdateAdd method
     */
    public void testUpdateDelete() {
        assertFalse(reader.updateRemove("no exit"));
        assertTrue(reader.add("add banaa"));
        assertFalse(reader.updateRemove("banaa<SEP>name"));
        assertTrue(reader.updateAdd("banaa<SEP>name<SEP>value"));
        assertTrue(reader.updateRemove("banaa<SEP>name"));

        assertTrue(reader.updateAdd("banaa<SEP>name<SEP>value"));
        assertTrue(reader.updateAdd("banaa<SEP>name2<SEP>value2"));
        assertTrue(reader.updateRemove("banaa<SEP>name2"));
    }


    /**
     * test delete method
     */
    public void testDelete() {
        assertFalse(reader.delete("babaa"));
        assertTrue(reader.add("add banaa"));
        assertTrue(reader.delete("delete banaa"));
    }

}
