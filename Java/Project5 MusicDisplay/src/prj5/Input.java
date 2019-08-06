package prj5;

import java.io.FileNotFoundException;

public class Input
{
    /**
     * Class of Input.
     * 
     * @author <Jiahui Huang> <hjiahui7>
     * @version 2017/12/05
     * @throws FileNotFoundException
     */
    public static void main(String[] args) throws FileNotFoundException
    {
        Reader rr = new Reader(args[0], args[1]);
        rr.getCalculator().sortByGenre();
        System.out.println(rr.getBook().taskReport());

        // System.out.println("\n");
        rr.getCalculator().sortByTitle();
        System.out.println(rr.getBook().taskReport());
    }

}
