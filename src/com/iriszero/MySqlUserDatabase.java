package com.iriszero;

public class MySqlUserDatabase extends MySqlDatabase implements IUserDatabase
{
    public MySqlUserDatabase(String user, String password, String url)
    {
        super(user, password, url);
    }

    @Override
    public String AddUser(String username, String password)
    {
        return null;
    }

    @Override
    public String CheckUser(String username, String password)
    {
        return null;
    }
}
