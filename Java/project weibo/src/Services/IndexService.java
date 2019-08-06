package Services;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.servlet.http.HttpServletRequest;
import DataBase.MySQLConnector;

public class IndexService implements ServiceInterface
{

    private static MySQLConnector database;
    private ArrayList<Line> datas;


    public IndexService()
    {
        database = MySQLConnector.getInstance();
        datas = new ArrayList<Line>();
    }


    public String makeWeb(HttpServletRequest req)
        throws IOException,
        SQLException
    {
        // 1. 获取参数

        // 2. 访问数据库（业务逻辑）
        this.insertData();
        // 3. 生成页面
        return makeHtml();
    }


    private String makeHtml()
    {
        StringBuilder html = new StringBuilder();
        html.append("<!DOCTYPE html>\n" + "<html>\n" + "<head>\n"
            + "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\">\n"
            + "<title>Insert title here</title>\n" + "</head>\n" + "<body>\n"
            + "<form action=\"Publish\" method=\"post\">\n"
            + "name: <input type=\"text\" name=\"name\"> <br> time: <input\r\n"
            + "            type=\"text\" name=\"time\"> <br> message: <input\r\n"
            + "            type=\"text\" name=\"message\" /> <input type=\"submit\" value=\"Submit\" />\n"
            + "</form>\n");
        String data = makedata();
        if (data != null)
        {
            html.append("<div>\r\n" + data + "</div>");
        }
        html.append("</body>\r\n" + "</html>");
        return html.toString();
    }


    private String makedata()
    {
        StringBuilder str = new StringBuilder();
        try
        {
            for (int i = 0; i < datas.size(); i++)
            {
                Line thisline = datas.get(i);
                int number = thisline.getId();
                String name = thisline.getName();
                String time = thisline.getTime();
                String message = thisline.getMessage();
                int likeValue = thisline.getlikeValue();
                int dislikeValue = thisline.getDislikeValue();
                str.append("<div>\r\n").append(number).append(".").append(
                    "Name: " + name + "\n").append("Time: " + time + "\n")
                    .append("Message: " + message + "\r\n").append("\n").append(
                        "</div>");
                str.append("<div>\r\n");
                String temp = makeLikeAndMessageHtml(number, likeValue,
                    dislikeValue);
                if (temp != null)
                {
                    str.append(temp);
                }
                str.append("</div>");
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        datas.clear();
        return str.toString();
    }


    private String makeLikeAndMessageHtml(int number, int like, int dislike)
        throws SQLException
    {
        StringBuilder str = new StringBuilder();
        int id = number;
        str.append("<div>\r\n").append(
            "<form action=\"LikeService\" method=\"post\">\n").append(
                "<input type=\"hidden\" name = \"id\" value=\"" + id + "\">")
            .append("<input type=\"Submit\" value=\"Like\" name=\"Like\"/>"
                + "  ").append(like + " ").append("</form>\n").append(
                    "<form action=\"DislikeService\" method=\"post\">\n")
            .append(
                "<input type=\"Submit\" value=\"DisLike\" name=\"DisLike\"/>"
                    + " ").append(dislike + " ").append(
                        "<input type=\"hidden\" name = \"id\" value=\"" + id
                            + "\">").append("</form>\n").append("</div>")
            .append("<div>\r\n").append(
                "<form action=\"CommentService\" method=\"post\">\n").append(
                    "<input type=\"hidden\" name = \"id\" value=\"" + id
                        + "\">").append(
                            "Comment: <input type=\"text\" name=\"Comment\">")
            .append("<input type=\"submit\" value=\"Submit\" />").append(
                "</form>\n").append(makeComment(id)).append("</div>");
        return str.toString();
    }


    private String makeComment(int id) throws SQLException
    {
        ResultSet rs = database.select(
            "SELECT commentValue FROM Comment where Id_D = " + id + ";");
        StringBuilder str = new StringBuilder();
        while (rs.next())
        {
            str.append("<div>\r\n");
            str.append(rs.getString("commentValue"));
            str.append("</div>");
        }
        return str.toString();
    }


    private void insertData() throws SQLException
    {
        String sql = "SELECT * FROM DataForWeb;";
        ResultSet rs = database.select(sql);
        while (rs.next())
        {
            int id = rs.getInt("Id_D");
            String name = rs.getString("name");
            String time = rs.getString("time");
            String message = rs.getString("message");
            int likeValue = rs.getInt("likeValue");
            int dislikeValue = rs.getInt("dislikeValue");
            Line thisline = new Line(id, name, time, message, likeValue,
                dislikeValue);
            this.datas.add(thisline);
        }
    }
}
