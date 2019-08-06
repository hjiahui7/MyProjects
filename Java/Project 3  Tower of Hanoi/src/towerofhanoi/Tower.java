package towerofhanoi;

/**
 * the Tower class will store discs because it extends LinkedStack
 * 
 * @author <jiahui huang> <hjiahui7>
 * @version 2017/10/20
 */
public class Tower extends LinkedStack<Disc>
{
    /**
     * the field for Tower
     */
    private Position position;


    /**
     * a constructor for Tower class with one parameter which will indicate the
     * tower is one of three towers
     * 
     * @param position
     *            which refers to a tower
     */
    public Tower(Position position)
    {
        super();
        this.position = position;
    }


    /**
     * get position
     * 
     * @return the current position
     */
    public Position position()
    {
        return position;
    }


    /**
     * check if the disc is correct and then it will be added
     * 
     * @param disc
     *            it will push a disc in this tower
     */
    @Override
    public void push(Disc disc)
    {
        if (disc == null)
        {
            throw new IllegalArgumentException();
        }
        if (this.isEmpty() || disc.compareTo(this.peek()) == -1)
        {
            super.push(disc);
        }
        else
        {
            throw new IllegalStateException();
        }
    }
}
