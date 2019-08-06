package com.yihaomen.test;

import java.io.Reader;
import java.util.List;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import com.yihaomen.mybatis.inter.IUserOperation;
import com.yihaomen.mybatis.model.Article;
import com.yihaomen.mybatis.model.User;

public class Test
{
    private static SqlSessionFactory sqlSessionFactory;
    private static Reader reader;

    static
    {
        try
        {
            reader = Resources.getResourceAsReader("Configuration.xml");
            sqlSessionFactory = new SqlSessionFactoryBuilder().build(reader);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }


    public static SqlSessionFactory getSession()
    {
        return sqlSessionFactory;
    }


    public void getUserList(String userName)
    {
        SqlSession session = sqlSessionFactory.openSession();
        try
        {
            IUserOperation userOperation = session.getMapper(
                IUserOperation.class);
            List<User> users = userOperation.selectUsers(userName);
            for (User user : users)
            {
                System.out.println(user.getId() + ":" + user.getUserName() + ":"
                    + user.getUserAddress());
            }

        }
        finally
        {
            session.close();
        }
    }


    /**
     * 删除数据，删除一定要 commit.
     * 
     * @param id
     */
    public void deleteUser(int id)
    {
        SqlSession session = sqlSessionFactory.openSession();
        try
        {
            IUserOperation userOperation = session.getMapper(
                IUserOperation.class);
            userOperation.deleteUser(id);

        }
        finally
        {
            session.close();
        }
    }


    public static void main(String[] args)
    {
        SqlSession session = sqlSessionFactory.openSession();
        try
        {
            IUserOperation userOperation = session.getMapper(
                IUserOperation.class);
            int number = userOperation.getCount();
            System.out.println(number);
        }
        finally
        {
            session.close();
        }
    }
}
