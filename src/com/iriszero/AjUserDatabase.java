package com.iriszero;

import java.io.*;
import java.util.Arrays;
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

    void Load() throws IOException
    {
        try
        {
            BufferedReader br = new BufferedReader(new FileReader(path));
            String line;
            while ((line = br.readLine()) != null)
            {
                String[] up = line.split(" ");
                data.put(up[0], up[1]);
            }
            br.close();
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
            if (!data.containsKey(username)) AppendLine(username + " " + password);
        }
        catch (IOException e)
        {
            return e.getMessage();
        }
        return "";
    }

    @Override
    public String CheckUser(String username, String password)
    {
        return data.containsKey(username) && password.equals(data.get(username)) ? "" :"Invalid Username/Password";
    }
}
