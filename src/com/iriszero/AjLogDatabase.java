package com.iriszero;

import java.io.IOException;
import java.util.List;

public class AjLogDatabase extends AjDatabase implements ILogDatabase
{
    List<String> logs;

    AjLogDatabase(String path) throws IOException
    {
        super(path);
        logs = LoadLines();
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
