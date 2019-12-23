package com.iriszero;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;

public class MySqlLogDatabase extends MySqlDatabase implements ILogDatabase
{
    MySqlLogDatabase(String username, String password, String url) throws ClassNotFoundException, SQLException
    {
        super(username, password, url);
    }

    @Override
    public List<UserMessage> GetLast(long n)
    {
        try
        {
            List<UserMessage> res = new LinkedList<>();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("select * from log order by id desc limit " + n);
            while (rs.next())
            {
                res.add(
                        new UserMessage(
                                rs.getString("username"),
                                rs.getString("message"),
                                rs.getString("date")));
            }
            rs.close();
            stmt.close();
            return res;
        }
        catch (Exception e)
        {
            e.printStackTrace(System.err);
            return new LinkedList<>();
        }
    }

    @Override
    public List<String> GetAllString()
    {
        try
        {
            List<String> res = new LinkedList<>();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("select * from log order by id");
            while (rs.next())
            {
                res.add(
                        new UserMessage(
                                rs.getString("username"),
                                rs.getString("message"),
                                rs.getString("date")).ToString());
            }
            rs.close();
            stmt.close();
            return res;
        }
        catch (Exception e)
        {
            e.printStackTrace(System.err);
            return new LinkedList<>();
        }
    }

    @Override
    public void Add(UserMessage msg)
    {
        try
        {
            PreparedStatement ps = conn.prepareStatement(
                    "insert into log (username, message, date) values (?,?,?)");
            ps.setString(1, msg.Username);
            ps.setString(2, msg.Message);
            ps.setString(3, msg.Date);
            ps.executeLargeUpdate();
            ps.close();
        }
        catch (Exception e)
        {
            e.printStackTrace(System.err);
        }
    }
}
