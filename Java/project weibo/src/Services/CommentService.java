package Services;

import java.io.IOException;
import java.sql.SQLException;
import javax.servlet.http.HttpServletRequest;
import DataBase.MySQLConnector;

public class CommentService implements ServiceInterface
{

    @Override
    public String makeWeb(HttpServletRequest req)
        throws IOException,
        SQLException
    {
        if (req.getParameter("Comment") != null)
        {
            addComment(req);
            return PublishService.makeSuccess();
        }
        PublishService.makeWrong();
        return null;
    }


    private boolean addComment(HttpServletRequest req) throws SQLException
    {
        int id = Integer.valueOf(req.getParameter("id"));
        String comment = req.getParameter("Comment");
        System.out.println(comment);
        String sql = "insert into Comment (commentValue,Id_D) values(\""
            + comment + "\"," + id + ");";
        MySQLConnector.getInstance().update(sql);
        return true;
    }
}
