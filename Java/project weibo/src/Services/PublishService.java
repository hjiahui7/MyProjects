package Services;

import java.io.IOException;
import java.sql.SQLException;
import javax.servlet.http.HttpServletRequest;
import DataBase.MySQLConnector;

public class PublishService implements ServiceInterface
{
    public String makeWeb(HttpServletRequest req)
        throws IOException,
        SQLException
    {
        String result = null;
        if (req.getParameter("name") != null)
        {
            addInformation(req);
            result = makeSuccess();
        }
        else
        {
            result = makeWrong();
        }
        return result;
    }


    private boolean addInformation(HttpServletRequest req)
    {
        String name = new String(req.getParameter("name"));
        String time = new String(req.getParameter("time"));
        String message = new String(req.getParameter("message"));
        if (!FMChecking.checkName(name) || !FMChecking.checkTime(time))
        {
            return false;
        }
        else
        {
            try
            {
                addInfor(name, time, message);
            }
            catch (ClassNotFoundException | SQLException e)
            {
                e.printStackTrace();
            }
        }
        return true;
    }


    private static void addInfor(String name, String time, String message)
        throws ClassNotFoundException,
        SQLException
    {
        StringBuilder str = new StringBuilder();
        str.append("insert into dataForWeb (name, time, message) VALUES (")
            .append("\"").append(name).append("\"" + ", ").append("\"").append(
                time).append("\"").append(", ").append("\"").append(message)
            .append("\"").append(");");
        MySQLConnector.getInstance().update(str.toString());
    }


    public static String makeWrong()
    {
        String docType = "<!DOCTYPE html>\n";
        String title = "Wrong";
        return (docType + "<html>\n" + "<head><title>\n" + title
            + "</title></head>\n" + "<body>\n" + "<h1>"
            + "Error, your input is wrong, please input correctlyl!!!!!!!\n"
            + "</h1>\n" + "<form action=\"IndexService\" method=\"post\">\n"
            + "<input type=\"Submit\" value=\"OK\" />\n" + "</form>\n"
            + "</body\n>" + "</html>");
    }


    public static String makeSuccess()
    {
        String docType = "<!DOCTYPE html>\n";
        String title = "Success!!!!!";
        return (docType + "<html>\n" + "<head><title>\n" + title
            + "</title></head>\n" + "<body>\n" + "<h1>" + "Success!!!!!\n"
            + "</h1>\n" + "<form action=\"IndexService\" method=\"post\">\n"
            + "<input type=\"Submit\" value=\"OK\" />\n" + "</form>\n"
            + "</body\n>" + "</html>");
    }
}
