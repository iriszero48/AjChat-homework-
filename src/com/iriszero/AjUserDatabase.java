package com.iriszero;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class AjUserDatabase extends AjDatabase implements IUserDatabase
{
    private Map<String, String> data = new HashMap<>();

    AjUserDatabase(String path) throws IOException
    {
        super(path);
        Load();
    }

    private void Load() throws IOException
    {
        try
        {
            Files.readAllLines(Paths.get(path)).forEach(i ->
            {
                String[] up = i.split(" ");
                data.put(up[0], up[1]);
            });
        }
        catch (FileNotFoundException e)
        {
            if (!new File(path).createNewFile()) throw new IOException();
        }
    }

    @Override
    public String AddUser(String username, String password)
    {
        try
        {
            if (data.containsKey(username))
            {
                return "Invalid Username/Password";
            }
            else
            {
                data.put(username, password);
                AppendLine(username + " " + password);
            }
        }
        catch (IOException e)
        {
            return e.getMessage();
        }
        return "true";
    }

    @Override
    public String CheckUser(String username, String password)
    {
        return data.containsKey(username) && password.equals(data.get(username)) ? "true" : "Invalid Username/Password";
    }
}
