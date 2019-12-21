package com.iriszero;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class MySqlUserDatabase extends MySqlDatabase implements IUserDatabase
{

    MySqlUserDatabase(String username, String password, String url) throws ClassNotFoundException, SQLException
    {
        super(username, password, url);
    }

    @Override
    public String AddUser(String username, String password)
    {
        try
        {
            boolean exist = false;
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("select * from user");
            while (rs.next())
            {
                if (username.equals(rs.getString("username")))
                {
                    exist = true;
                    break;
                }
            }
            rs.close();
            stmt.close();
            if (exist) return "Invalid Username/Password";
            PreparedStatement ps = conn.prepareStatement("insert into user values (?,?)");
            ps.setString(1, username);
            ps.setString(2, password);
            ps.executeLargeUpdate();
            ps.close();
            return "true";
        }
        catch (Exception e)
        {
            e.printStackTrace(System.err);
            return "Invalid Username/Password";
        }
    }

    @Override
    public String CheckUser(String username, String password)
    {
        try
        {
            boolean succ = false;
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("select * from user");
            while (rs.next())
            {
                if (username.equals(rs.getString("username")) &&
                        password.equals(rs.getString("password")))
                {
                    succ = true;
                    break;
                }
            }
            rs.close();
            stmt.close();
            return succ ? "true" : "Invalid Username/Password";
        }
        catch (Exception e)
        {
            e.printStackTrace(System.err);
            return "Invalid Username/Password";
        }
    }
}
