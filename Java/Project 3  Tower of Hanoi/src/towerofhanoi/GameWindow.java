package towerofhanoi;

import java.awt.Color;
import java.util.Observable;
import java.util.Observer;
import CS2114.Button;
import CS2114.Shape;
import CS2114.Window;
import CS2114.WindowSide;

/**
 * The game window class will have solve button
 * in this class, it will create three shapes and generate the title, gaps for
 * each disc and disc's height. When people clicks solve, the program will call
 * the method from model which is HanoiSolver and transfer the position to
 * view.In that case, the users will see the change from screen.
 * 
 * @author <jiahui huang> <hjiahui7>
 * @version 2017/10/20
 */
public class GameWindow implements Observer
{
    /**
     * The field for GameWindow
     */
    private HanoiSolver game;
    private Shape left;
    private Shape right;
    private Shape middle;
    private Window window;
    private final int DISK_GAP = 6;
    public final static int DISK_HEIGHT = 9;


    /**
     * The constructor for GameWindow with one parameter
     * In the constructor, three shapes will be initialize and the discs will be
     * added into left tower. And the buttons will be add.
     * 
     * @param game
     *            which we get from ProjectRunner
     */
    public GameWindow(HanoiSolver game)
    {
        this.game = game;
        window = new Window("Tower of Hanoi");
        game.addObserver(this);
        left = new Shape(108, 80, 5, 200, new Color(1, 1, 1));
        right = new Shape(290, 80, 5, 200, new Color(1, 1, 1));
        middle = new Shape(475, 80, 5, 200, new Color(1, 1, 1));
        window.addShape(left);
        window.addShape(right);
        window.addShape(middle);
        // We also need to add the base for three tower
        window.addShape(new Shape(59, 279, 97, 10, new Color(1, 1, 1)));
        window.addShape(new Shape(243, 279, 97, 10, new Color(1, 1, 1)));
        window.addShape(new Shape(427, 279, 97, 10, new Color(1, 1, 1)));
        // add the disc from the parameter of project runner
        for (int i = game.discs(); i > 0; i--)
        {
            int theSizeOfDisc = 15;
            Disc newdisc = new Disc(i * theSizeOfDisc);
            window.addShape(newdisc);
            game.getTower(Position.LEFT).push(newdisc);
            moveDisc(Position.LEFT);
        }
        Button solve = new Button("Solve");
        window.addButton(solve, WindowSide.SOUTH);
        solve.onClick(this, "clickedSolve");
    }


    /**
     * the updata method which will change the screen when people see
     * 
     * @param arg0
     *            the parameter which will be transfer from model
     * @param arg1
     *            the parameter which will be transfer from model
     */
    @Override
    public void update(Observable arg0, Object arg1)
    {
        if (arg1.getClass().equals(Position.class))
        {
            Position position = (Position)arg1;
            moveDisc(position);
            sleep();
        }
    }


    /**
     * the sleep method for thread
     */
    private void sleep()
    {
        try
        {
            Thread.sleep(500);
        }
        catch (Exception e)
        {
        }
    }


    /**
     * when people click the button solve it will run this method
     * 
     * @param button
     *            the solver button will call this method
     */
    public void clickedSolve(Button button)
    {
        button.disable();
        new Thread()
        {
            public void run()
            {
                game.solve();
            }
        }.start();
    }


    /**
     * The moveDisc method will change the disc from original position to
     * destination
     * 
     * @param position
     *            the position of the disc
     */
    private void moveDisc(Position position)
    {
        Disc currentDisc = game.getTower(position).peek();
        Shape currentPole = null;
        switch (position)
        {
            case LEFT:
                currentPole = left;
                break;
            case RIGHT:
                currentPole = right;
                break;
            case MIDDLE:
                currentPole = middle;
                break;
            default:
                currentPole = left;
        }
        int xPoint = currentPole.getX() - currentDisc.getWidth() / 2 + 2;
        int numberOfDiscs = game.getTower(position).size();
        int height = 279;
        int yPoint = height - (DISK_GAP + DISK_HEIGHT) * numberOfDiscs;
        currentDisc.moveTo(xPoint, yPoint);
    }

}
