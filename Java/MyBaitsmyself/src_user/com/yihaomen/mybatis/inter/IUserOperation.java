package com.yihaomen.mybatis.inter;

import java.util.List;
import com.yihaomen.mybatis.model.Article;
import com.yihaomen.mybatis.model.User;

public interface IUserOperation
{
    public User selectUserByID(int id);


    public List<User> selectUsers(String userName);


    public void addUser(User user);


    public void updateUser(User user);


    public List<Article> getUserArticles(int id);


    public void deleteUser(int id);


    public int getCount();

}
