package Services;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.servlet.http.HttpServletRequest;
import DataBase.MySQLConnector;

public class DislikeService implements ServiceInterface
{

    @Override
    public String makeWeb(HttpServletRequest req)
        throws IOException,
        SQLException
    {
        if (req.getParameter("DisLike") != null)
        {
            if (addDisLike(req))
            {
                return PublishService.makeSuccess();
            }
        }
        return PublishService.makeWrong();
    }


    private boolean addDisLike(HttpServletRequest req) throws SQLException
    {
        int id = Integer.valueOf(req.getParameter("id"));
        String sql = "SELECT dislikeValue FROM DataForWeb where Id_D =" + id
            + ";";
        ResultSet re = MySQLConnector.getInstance().select(sql);
        re.next();
        int dislikeNumber = re.getInt("dislikeValue") + 1;
        sql = "UPDATE DataForWeb SET dislikeValue=\"" + dislikeNumber
            + "\"   WHERE Id_D=\"" + id + "\";";
        MySQLConnector.getInstance().update(sql);
        return true;
    }
}
