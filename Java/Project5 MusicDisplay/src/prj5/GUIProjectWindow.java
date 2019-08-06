package prj5;

import java.awt.Color;
import CS2114.Button;
import CS2114.Shape;
import CS2114.TextShape;
import CS2114.Window;
import CS2114.WindowSide;
import static prj5.PropertyEnum.*;

/**
 * the GUIProjectWindow
 * 
 * @author <Jiahui Huang> <hjiahui7>
 * @version 2017/12/05
 */

public class GUIProjectWindow
{
    /**
     * field
     */
    private DataCalculator calculator;
    private Window window;
    private Button clickedPrevious;
    private Button clickedNext;
    private Button clickedArtist;
    private Button clickedTitle;
    private Button clickedDate;
    private Button clickedGenre;
    private Button clickedHobby;
    private Button clickedMajor;
    private Button clickedRegion;
    private Button clickedQuit;
    private int page;
    private int totalPage;
    private String describe;
    private String name;


    /**
     * GUIProjectWindow
     * 
     * @param theCalculator
     *            the parameter
     */
    public GUIProjectWindow(DataCalculator theCalculator)
    {
        calculator = theCalculator;
        window = new Window();
        window.setTitle("Project 5");
        window.setSize(1100, 800);
        clickedPrevious = new Button("¡û Prev");
        clickedPrevious.disable();
        clickedNext = new Button("¡ú Next");
        clickedArtist = new Button("Sort by Artist Name");
        clickedTitle = new Button("Sort by Song Title");
        clickedDate = new Button("Sort by Release Year");
        clickedGenre = new Button("Sort by Genre");
        clickedHobby = new Button("Represent Hobby");
        clickedMajor = new Button("Represent Major");
        clickedRegion = new Button("Represent Region");
        clickedQuit = new Button("Quit");

        clickedPrevious.onClick(this, "clickedPrevious");
        clickedNext.onClick(this, "clickedNext");
        clickedArtist.onClick(this, "clickedArtist");
        clickedTitle.onClick(this, "clickedTitle");
        clickedDate.onClick(this, "clickedDate");
        clickedGenre.onClick(this, "clickedGenre");
        clickedHobby.onClick(this, "clickedHobby");
        clickedMajor.onClick(this, "clickedMajor");
        clickedRegion.onClick(this, "clickedRegion");
        clickedQuit.onClick(this, "clickedQuit");

        window.addButton(clickedPrevious, WindowSide.NORTH);
        window.addButton(clickedArtist, WindowSide.NORTH);
        window.addButton(clickedTitle, WindowSide.NORTH);
        window.addButton(clickedDate, WindowSide.NORTH);
        window.addButton(clickedGenre, WindowSide.NORTH);
        window.addButton(clickedNext, WindowSide.NORTH);
        window.addButton(clickedHobby, WindowSide.SOUTH);
        window.addButton(clickedMajor, WindowSide.SOUTH);
        window.addButton(clickedRegion, WindowSide.SOUTH);
        window.addButton(clickedQuit, WindowSide.SOUTH);

        describe = "";
        name = "name";
        page = 1;
        if (calculator.returnBook().size() % 9 > 1 && calculator.returnBook()
            .size() % 9 < 9)
        {
            totalPage = 1 + calculator.returnBook().size() / 9;
        }

    }


    /**
     * Set the value of x, y, and datas.
     */
    public void setData()
    {

        int maxNumberOfAdd = 9;
        int totalNumberOfBook = calculator.returnBook().size();
        int startIndex = 9 * (page - 1);
        int normalJump = 50;
        int rangeMin = 100;
        int rangeMax = 155;
        int yPostion = 100;
        int width = 10;
        if (page == totalPage)
        {
            maxNumberOfAdd = totalNumberOfBook % 9;
        }
        for (int i = 1; i <= maxNumberOfAdd; i++)
        {
            Song cur = calculator.returnBook().get(startIndex);
            PropertyEnum cur1 = READING;
            PropertyEnum cur2 = ART;
            PropertyEnum cur3 = SPORT;
            PropertyEnum cur4 = MUSIC;
            if (describe.equals("Major"))
            {
                cur1 = CS;
                cur2 = ENGINEERING;
                cur3 = MC;
                cur4 = OTHERMAJOR;
            }
            if (describe.equals("Region"))
            {
                cur1 = SE;
                cur2 = NE;
                cur3 = US;
                cur4 = OUS;
            }
            int xOneHeard = cur.getPercent(HEARD, cur1) * 120 / 100;
            int xTwoHeard = cur.getPercent(HEARD, cur2) * 120 / 100;
            int xThreeHeard = cur.getPercent(HEARD, cur3) * 120 / 100;
            int xFourHeard = cur.getPercent(HEARD, cur4) * 120 / 100;

            int xOneLike = cur.getPercent(LIKE, cur1) * 120 / 100;
            int xTwoLike = cur.getPercent(LIKE, cur2) * 120 / 100;
            int xThreeLike = cur.getPercent(LIKE, cur3) * 120 / 100;
            int xFourLike = cur.getPercent(LIKE, cur4) * 120 / 100;

            Shape square1 = new Shape(rangeMin + normalJump - xOneHeard,
                yPostion, xOneHeard, width, Color.PINK);
            window.addShape(square1);
            Shape square8 = new Shape(rangeMax, yPostion, xOneLike, width,
                Color.PINK);
            window.addShape(square8);

            Shape square2 = new Shape(rangeMin + normalJump - xTwoHeard,
                yPostion + 10, xTwoHeard, width, Color.blue);
            window.addShape(square2);
            Shape square5 = new Shape(rangeMax, yPostion + 10, xTwoLike, width,
                Color.blue);
            window.addShape(square5);

            Shape square3 = new Shape(rangeMin - xThreeHeard + normalJump,
                yPostion + 20, xThreeHeard, width, Color.ORANGE);
            window.addShape(square3);
            Shape square6 = new Shape(rangeMax, yPostion + 20, xThreeLike,
                width, Color.ORANGE);
            window.addShape(square6);

            Shape square4 = new Shape(rangeMin + normalJump - xFourHeard,
                yPostion + 30, xFourHeard, width, Color.green);
            window.addShape(square4);
            Shape square7 = new Shape(rangeMax, yPostion + 30, xFourLike, width,
                Color.green);
            window.addShape(square7);

            if (i % 3 == 0)
            {
                rangeMin = 100;
                yPostion = yPostion + 200;
                rangeMax = 155;
                width = 10;
            }
            else
            {
                rangeMin = rangeMin + 270;
                rangeMax = rangeMax + 270;
            }
            startIndex++;
        }
    }


    /**
     * Add name onto the window.
     */
    public void addName()
    {
        SongBook book = calculator.returnBook();
        int xPosition = 80;
        int yPosition = 50;

        int maxNumberOfAdd = 9;
        int totalNumberOfBook = calculator.returnBook().size();
        int startIndex = 9 * (page - 1);
        if (page == totalPage)
        {
            maxNumberOfAdd = totalNumberOfBook % 9;
        }

        for (int i = 1; i <= maxNumberOfAdd; i++)
        {
            String string1 = book.get(startIndex).getTitle();
            String string2 = book.get(startIndex).getArtist();

            TextShape text1 = new TextShape(xPosition, yPosition, string1,
                Color.black);
            text1.setBackgroundColor(Color.white);
            TextShape text2 = null;
            if (name.equals("name"))
            {
                text2 = new TextShape(xPosition, yPosition + 20, "by "
                    + string2, Color.black);
            }
            else if (name.equalsIgnoreCase("date"))
            {
                int a = book.get(startIndex).getDate();
                text2 = new TextShape(xPosition, yPosition + 20, a + "",
                    Color.black);
            }
            else
            {
                string2 = book.get(startIndex).getGenre();
                text2 = new TextShape(xPosition, yPosition + 20, string2,
                    Color.black);
            }
            text2.setBackgroundColor(Color.white);
            window.addShape(text1);
            window.addShape(text2);
            if (i % 3 == 0)
            {
                xPosition = 80;
                yPosition = yPosition + 200;
            }
            else
            {
                xPosition = xPosition + 270;
            }
            startIndex++;
        }
    }


    /**
     * Add shapes onto the window.
     */
    public void addShape()
    {
        int xPosition = 150;
        int yPostion = 100;
        int length = 5;
        int width = 40;
        int maxNumberOfAdd = 9;
        int totalNumberOfBook = calculator.returnBook().size();
        if (page == totalPage)
        {
            maxNumberOfAdd = totalNumberOfBook % 9;
        }

        for (int i = 1; i <= maxNumberOfAdd; i++)
        {
            Shape square = new Shape(xPosition, yPostion, length, width,
                Color.black);
            window.addShape(square);
            if (i % 3 == 0)
            {
                xPosition = 150;
                yPostion = yPostion + 200;
            }
            else
            {
                xPosition = xPosition + 270;
            }

        }
    }


    /**
     * Give describtions and add them onto the Window.
     */
    public void addDescribtion(String name)
    {
        if (!this.checkIsEmpty())
        {
            int xPosition = 800;
            int yPostion = 400;
            int length = 210;
            int width = 210;
            TextShape text1 = new TextShape(xPosition + 20, yPostion + 120,
                "Heard");
            TextShape text2 = new TextShape(xPosition + 150, yPostion + 120,
                "Like");
            text1.setBackgroundColor(Color.white);
            text2.setBackgroundColor(Color.white);
            window.addShape(text1);
            window.addShape(text2);
            Shape square1 = new Shape(xPosition + 100, yPostion + 100, 5, 60,
                Color.black);
            window.addShape(square1);
            TextShape text3 = new TextShape(xPosition + 25, yPostion + 15, name
                + " Data");
            text3.setBackgroundColor(Color.white);
            String cur1 = "Read";
            String cur2 = "Art";
            String cur3 = "Sport";
            String cur4 = "Music";
            if (describe.equals("Major"))
            {
                cur1 = "CS";
                cur2 = "Engineering";
                cur3 = "Math or Cmda";
                cur4 = "others";
            }
            if (describe.equals("Region"))
            {
                cur1 = "Southeast";
                cur2 = "Northeast";
                cur3 = "United States";
                cur4 = "Outside of United States";
            }
            TextShape text4 = new TextShape(xPosition + 25, yPostion + 30, cur1,
                Color.PINK);
            text4.setBackgroundColor(Color.white);
            TextShape text5 = new TextShape(xPosition + 25, yPostion + 45, cur2,
                Color.blue);
            text5.setBackgroundColor(Color.white);
            TextShape text6 = new TextShape(xPosition + 25, yPostion + 60, cur3,
                Color.orange);
            text6.setBackgroundColor(Color.white);
            TextShape text7 = new TextShape(xPosition + 25, yPostion + 75, cur4,
                Color.green);
            text7.setBackgroundColor(Color.white);
            window.addShape(text3);
            window.addShape(text4);
            window.addShape(text5);
            window.addShape(text6);
            window.addShape(text7);
            Shape square2 = new Shape(xPosition, yPostion, length, width,
                Color.black);
            square2.setBackgroundColor(Color.white);
            window.addShape(square2);
        }
    }


    /**
     * Button of clickedNext.
     * 
     * @param button
     */
    public void clickedNext(Button button)
    {

        if (!checkIsEmpty())
        {
            window.removeAllShapes();
            page++;
            updata();
            clickedPrevious.enable();
            if (page == totalPage)
            {
                clickedNext.disable();
            }
            clickedPrevious.enable();
        }
    }


    /**
     * Button of clickedPrevious.
     * 
     * @param button
     *            clickedPrevious
     */
    public void clickedPrevious(Button button)
    {
        if (!checkIsEmpty())
        {
            window.removeAllShapes();
            page--;
            updata();
            if (page == 1)
            {
                clickedPrevious.disable();
            }
            clickedNext.enable();
        }

    }


    /**
     * Button of clickedArtist.
     * 
     * @param button
     *            Artist
     */
    public void clickedArtist(Button button)
    {
        if (!checkIsEmpty())
        {
            name = "name";
            calculator.sortByArtist();
            window.removeAllShapes();
            updata();
        }
    }


    /**
     * Button of clickedTitle.
     * 
     * @param button
     *            Title
     */
    public void clickedTitle(Button button)
    {
        if (!checkIsEmpty())
        {
            name = "name";
            calculator.sortByTitle();
            window.removeAllShapes();
            updata();
        }
    }


    public boolean checkIsEmpty()
    {
        return describe.equals("");
    }


    /**
     * Button of clickedDate.
     * 
     * @param button
     *            Date
     */
    public void clickedDate(Button button)
    {
        if (!checkIsEmpty())
        {
            name = "date";
            calculator.sortByData();
            window.removeAllShapes();
            updata();
        }
    }


    /**
     * Button of clickedGenre.
     * 
     * @param button
     *            Genre
     */
    public void clickedGenre(Button button)
    {
        if (!checkIsEmpty())
        {
            name = "genre";
            calculator.sortByGenre();
            window.removeAllShapes();
            updata();
        }
    }


    /**
     * Button of clickedHobby.
     * 
     * @param button
     *            Hobby
     */
    public void clickedHobby(Button button)
    {
        window.removeAllShapes();
        describe = "Hobby";
        updata();

    }


    /**
     * Button of clickedMajor.
     * 
     * @param button
     *            Major
     */
    public void clickedMajor(Button button)
    {

        window.removeAllShapes();
        describe = "Major";
        updata();
    }


    /**
     * Button of clickedRegion.
     * 
     * @param button
     *            Region
     */
    public void clickedRegion(Button button)
    {
        window.removeAllShapes();
        describe = "Region";
        updata();
    }


    /**
     * Button of clickedQuit.
     * 
     * @param button
     *            Quit
     */
    public void clickedQuit(Button button)
    {
        System.exit(0);
    }


    /**
     * Updata
     */
    public void updata()
    {

        if (describe.equals("Hobby"))
        {
            addDescribtion("Hobby");
            setData();
        }
        if (describe.equals("Major"))
        {
            addDescribtion("Major");
            setData();
        }
        if (describe.equals("Region"))
        {
            addDescribtion("Region");
            setData();
        }
        addShape();
        addName();
    }
}
