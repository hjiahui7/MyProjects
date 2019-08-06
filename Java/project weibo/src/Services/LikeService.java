package Services;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.servlet.http.HttpServletRequest;
import DataBase.MySQLConnector;

public class LikeService implements ServiceInterface
{

    @Override
    public String makeWeb(HttpServletRequest req)
        throws IOException,
        SQLException
    {
        if (req.getParameter("Like") != null)
        {
            addLike(req);
            return PublishService.makeSuccess();
        }
        return PublishService.makeWrong();
    }


    private boolean addLike(HttpServletRequest req) throws SQLException
    {
        int id = Integer.valueOf(req.getParameter("id"));
        String sql = "SELECT likeValue FROM DataForWeb where Id_D =" + id + ";";
        ResultSet re = MySQLConnector.getInstance().select(sql);
        re.next();
        int likeNumber = re.getInt("likeValue") + 1;
        sql = "UPDATE DataForWeb SET likeValue=\"" + likeNumber
            + "\"   WHERE Id_D=\"" + id + "\";";
        MySQLConnector.getInstance().update(sql);
        return true;
    }

}
