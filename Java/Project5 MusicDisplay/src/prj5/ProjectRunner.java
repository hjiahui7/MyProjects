package prj5;

/**
 * Project runner.
 * 
 * @author <Jiahui Huang> <hjiahui7>
 * @version 2017/12/05
 */
public class ProjectRunner {
    public static void main(String[] args) {
        if (args.length > 0) {
            String a1 = args[0];
            String a2 = args[1];
            new Reader(a1, a2);

        }
        else {
            new Reader("MusicSurveyData2017F.csv", "SongList2017F.csv");
        }
    }

}
