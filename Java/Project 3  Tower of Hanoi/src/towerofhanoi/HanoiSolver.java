package towerofhanoi;

import java.util.Observable;

/**
 * The HanoiSolver class contains a method to solve the HanoiTower
 * 
 * @author <jiahui huang> <hjiahui7>
 * @version 2017/10/20
 */
public class HanoiSolver extends Observable
{
    /**
     * The field for HanoiSolver
     */
    private Tower left;
    private Tower middle;
    private Tower right;
    private int numDiscs;


    /**
     * the constructor for HanoiSolver
     * 
     * @param numDiscs
     *            the number of disc in the left tower
     */
    public HanoiSolver(int numDiscs)
    {
        this.numDiscs = numDiscs;
        left = new Tower(Position.LEFT);
        middle = new Tower(Position.MIDDLE);
        right = new Tower(Position.RIGHT);
    }


    /**
     * the number of disc
     * 
     * @return the number of discs
     */
    public int discs()
    {
        return numDiscs;
    }


    /**
     * It will return the tower in left, middle, or right
     * 
     * @param pos
     *            the position means one of three towers
     * @return the tower we will use
     */
    public Tower getTower(Position pos)
    {
        Tower tower = null;
        switch (pos)
        {
            case LEFT:
                tower = left;
                break;
            case RIGHT:
                tower = right;
                break;
            case MIDDLE:
                tower = middle;
                break;
            default:
                tower = left;
        }
        return tower;
    }


    /**
     * the ToString method will help for test case to make the tower is loading
     * discs correctly
     * 
     * @return the string of discs's size
     */
    public String toString()
    {
        return left.toString() + middle.toString() + right.toString();
    }


    /**
     * the move method for solveTowers method
     * it will move the disc and change the screen, therefore it is the module
     * for view which is GameWindow
     * 
     * @param source
     *            the original point
     * @param destination
     *            the destination point from source
     */
    private void move(Tower source, Tower destination)
    {
        Disc transfer = source.pop();
        destination.push(transfer);
        this.setChanged();
        this.notifyObservers(destination.position());
    }


    /**
     * the main method for solving the HanoiTower question
     * it will have four parameter's recursion
     * 
     * @param currentDiscs
     *            the number of discs
     * @param startPole
     *            the original position which will be removed
     * @param tempPole
     *            the support tower
     * @param endPole
     *            the destination tower
     */
    private void solveTowers(
        int currentDiscs,
        Tower startPole,
        Tower tempPole,
        Tower endPole)
    {
        if (currentDiscs == 1)
        {
            move(startPole, endPole);
        }
        else
        {
            solveTowers(currentDiscs - 1, startPole, endPole, tempPole);
            move(startPole, endPole);
            solveTowers(currentDiscs - 1, tempPole, startPole, endPole);
        }
    }


    /**
     * solve method will call the solveTowers method
     */
    public void solve()
    {
        solveTowers(numDiscs, left, middle, right);
    }
}
