package towerofhanoi;

/**
 * the main method for project runner
 * it will call the main program stack
 * 
 * @author <jiahui huang> <hjiahui7>
 * @version 2017/10/20
 */
import student.TestCase;

/**
 * the test methods for HanoiSolver class
 * 
 * @author <jiahui huang> <hjiahui7>
 * @version 2017/10/20
 */
public class HanoiSolverTest extends TestCase
{
    /**
     * the field for HanoiSolverTest
     */
    private HanoiSolver solver1;


    /**
     * The setUp for HanoiSolverTest
     */
    public void setUp()
    {
        solver1 = new HanoiSolver(5);
    }


    /**
     * The method for HanoiSolverTest
     */
    public void testDiscs()
    {
        assertEquals(5, solver1.discs());
    }


    /**
     * The method for testGetTower
     */
    public void testGetTower()
    {
        solver1.getTower(Position.LEFT).push(new Disc(3));
        assertEquals(1, solver1.getTower(Position.LEFT).size());
        solver1.getTower(Position.RIGHT).push(new Disc(1));
        assertEquals(1, solver1.getTower(Position.LEFT).size());
        solver1.getTower(Position.MIDDLE).push(new Disc(1));
        assertEquals(1, solver1.getTower(Position.LEFT).size());
        solver1.getTower(Position.OTHER).push(new Disc(2));
        solver1.getTower(Position.OTHER).push(new Disc(1));
        assertEquals(3, solver1.getTower(Position.LEFT).size());
    }


    /**
     * The method for testToString
     */
    public void testToString()
    {
        solver1.getTower(Position.LEFT).push(new Disc(2));
        String a = solver1.toString();
        assertEquals("[2][][]", a);
        solver1.getTower(Position.RIGHT).push(new Disc(1));
        solver1.getTower(Position.MIDDLE).push(new Disc(1));
        solver1.getTower(Position.OTHER).push(new Disc(1));
        a = solver1.toString();
        assertEquals("[1, 2][1][1]", a);
    }


    /**
     * The method for testSolveTowers
     */
    public void testSolveTowers()
    {
        solver1.getTower(Position.LEFT).push(new Disc(5));
        solver1.getTower(Position.LEFT).push(new Disc(4));
        solver1.getTower(Position.LEFT).push(new Disc(3));
        solver1.getTower(Position.LEFT).push(new Disc(2));
        solver1.getTower(Position.LEFT).push(new Disc(1));
        solver1.solve();
        assertEquals(5, solver1.getTower(Position.RIGHT).size());
    }
}
