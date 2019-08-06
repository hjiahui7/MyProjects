package DataBase;

import java.sql.*;

public class MySQLConnector
{

    public static void main(String[] args) throws SQLException
    {
        MySQLConnector.getInstance().init();
        ResultSet re = MySQLConnector.getInstance().select(
            "SELECT Id_D, name, time, message FROM DataForWeb");
        re.next();
        int a = re.getInt("Id_D");
        re.next();
        a = re.getInt("Id_D");
        System.out.println(a);
    }

    // JDBC 驱动名及数据库 URL
    static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    static final String DB_URL =
        "jdbc:mysql://localhost:3306/kvstorage?serverTimezone=GMT";

    // 数据库的用户名与密码，需要根据自己的设置
    static final String USER = "root";
    static final String PASS = "";
    private Connection conn = null;
    private Statement stmt = null;
    private Statement stmt2 = null;
    private static MySQLConnector instance = null;


    public MySQLConnector()
    {
        init();
    }


    private void makeTable() throws SQLException
    {
        System.out.println("Making table....");
        String sql =
            "CREATE TABLE DataForWeb (Id_D int(255) not null AUTO_INCREMENT,name varchar(20),"
                + " time varchar(20), message varchar(255), likeValue int, dislikeValue int, primary key(Id_D));";
        PreparedStatement pstmt = null;
        pstmt = conn.prepareStatement(sql);
        pstmt.executeUpdate();
        sql = "CREATE TABLE Comment" + " (Id_C int not null AUTO_INCREMENT"
            + ", commentValue varchar(2000)" + ", Id_D int, primary key(Id_C)"
            + ");";
        pstmt = conn.prepareStatement(sql);
        pstmt.executeUpdate();
        System.out.println("Success!!");
    }


    public void close() throws SQLException
    {
        stmt.close();
        conn.close();
    }


    public static MySQLConnector getInstance()
    {
        if (instance != null)
        {
            return instance;
        }
        instance = new MySQLConnector();
        return instance;
    }


    private boolean init()
    {
        if (this.conn != null)
        {
            return true;
        }
        try
        {
            Class.forName(JDBC_DRIVER);
            System.out.println("connect to data base...");
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            System.out.println("Success....");
            System.out.println(" initial Statement object...");
            stmt = conn.createStatement();
            stmt2 = conn.createStatement();
            System.out.println("Success....");
            // makeTable();
        }
        catch (SQLException se)
        {
            se.printStackTrace();
            return false;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return false;
        }
        return true;
    }


    public ResultSet select(String sql)
    {
        if (!this.init())
        {
            return null;
        }
        ResultSet rs = null;
        try
        {
            rs = stmt.executeQuery(sql);
        }
        catch (SQLException e)
        {
            System.out.println("to string error");
            e.printStackTrace();
        }
        return rs;
    }


    public boolean update(String sql)
    {
        if (!this.init())
        {
            return false;
        }
        boolean result = false;
        try
        {
            result = stmt.execute(sql);
        }
        catch (SQLException e)
        {
            System.out.println("to string error");
            e.printStackTrace();
        }
        return result;
    }
}
