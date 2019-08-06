package towerofhanoi;

/**
 * The ProjectRunner class will run the whole program
 * 
 * @author <jiahui huang> <hjiahui7>
 * @version 2017/10/20
 */
public class ProjectRunner
{
    /**
     * the constructor for ProjectRunner
     */
    public ProjectRunner()
    {
        // empty...
    }


    /**
     * the main method in towerofhanoi, it can take a parameter or just run
     * a default one, the parameter is the number of disc people will see in
     * window
     * 
     * @param args
     *            the number of discs
     */
    public static void main(String[] args)
    {
        if (args.length == 1)
        {
            int discs = Integer.parseInt(args[0]);
            GameWindow game = new GameWindow(new HanoiSolver(discs));

        }
        else
        {
            GameWindow game = new GameWindow(new HanoiSolver(10));
        }

    }
}
