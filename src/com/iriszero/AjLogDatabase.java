package com.iriszero;

import java.io.IOException;
import java.util.List;

public class AjLogDatabase extends AjDatabase implements ILogDatabase
{
    private List<String> logs;

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
    public List<String> GetAllString()
    {
        return logs;
    }

    @Override
    public void Add(UserMessage msg)
    {
        try
        {
            AppendLine(msg.ToString());
        }
        catch (Exception ignored)
        {
        }
    }
}
