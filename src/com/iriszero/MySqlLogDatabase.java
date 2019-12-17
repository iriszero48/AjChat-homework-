package com.iriszero;

import java.util.List;

public class MySqlLogDatabase extends MySqlDatabase implements ILogDatabase
{
    public MySqlLogDatabase(String user, String password, String url)
    {
        super(user, password, url);
    }

    @Override
    public List<UserMessage> GetLast(long n)
    {
        return null;
    }

    @Override
    public List<UserMessage> GetAll()
    {
        return null;
    }

    @Override
    public void Add(UserMessage msg)
    {

    }
}
