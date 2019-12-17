package com.iriszero;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class AjDatabase
{
    private Lock lock = new ReentrantLock();
    String path;

    AjDatabase(String path)
    {
        this.path = path;
    }

    List<String> LoadLines() throws IOException
    {
        try
        {
            return Files.readAllLines(Paths.get(path), StandardCharsets.UTF_8);
        }
        catch (IOException e)
        {
            if (!new File(path).createNewFile()) throw new IOException();
            return new LinkedList<>();
        }
    }

    void AppendLine(String data) throws IOException
    {
        lock.lock();
        try
        {
            FileWriter fw = new FileWriter(new File(path), true);
            fw.write(data + "\n");
            fw.close();
        }
        catch (IOException e)
        {
            if (!new File(path).createNewFile()) throw new IOException();
            Files.writeString(Paths.get(path), data + "\n");
        }
        lock.unlock();
    }
}
