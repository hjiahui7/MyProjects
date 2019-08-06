package Services;

public class Line
{
    private int id;
    private String name;
    private String time;
    private String message;
    private int likeValue;
    private int dislikeValue;


    public Line(int id, String n, String t, String m, int l, int d)
    {
        this.id = id;
        name = n;
        time = t;
        message = m;
        likeValue = l;
        dislikeValue = d;
    }


    public int getId()
    {
        return id;
    }


    public String getName()
    {
        return this.name;
    }


    public String getTime()
    {
        return this.time;
    }


    public String getMessage()
    {
        return this.message;
    }


    public int getDislikeValue()
    {
        return this.dislikeValue;
    }


    public int getlikeValue()
    {
        return this.likeValue;
    }


    public void setName(String name)
    {
        this.name = name;
    }


    public void setTime(String time)
    {
        this.time = time;
    }


    public void setMessage(String message)
    {
        this.message = message;
    }


    public void setDislikeValue(int dislikeValue)
    {
        this.dislikeValue = dislikeValue;
    }


    public void setId(int id)
    {
        this.id = id;
    }


    public void setlikeValue(int likeValue)
    {
        this.likeValue = likeValue;
    }
}
