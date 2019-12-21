package com.iriszero;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

class MySqlDatabase
{
    private static final String JdbcClassName = "com.mysql.cj.jdbc.Driver";

    Connection conn;

    MySqlDatabase(String username, String password, String url) throws ClassNotFoundException, SQLException
    {
        Class.forName(JdbcClassName);
        conn = DriverManager.getConnection(url, username, password);
    }
}
