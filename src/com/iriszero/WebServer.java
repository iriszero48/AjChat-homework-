package com.iriszero;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public class WebServer
{
    private List<Thread> pool = new ArrayList<>();
    private ServerSocket ss;

    WebServer(String webPath, int port, int threadNum) throws IOException
    {
        ss = new ServerSocket(port);
        String webPage = Files.readString(Paths.get(webPath));
        int len = webPage.getBytes(StandardCharsets.UTF_8).length;
        byte[] http = ("HTTP/1.1 200 OK\r\n" +
                "Content-Length: " + len +
                "\r\nServer: iriszero/AjChat/1.0" +
                "\r\nContent-Type: text/html\r\n\r\n" +
                webPage).getBytes(StandardCharsets.UTF_8);
        IntStream.range(0, threadNum).forEach(i ->
                pool.add(new Thread(() ->
                {
                    byte[] buf = new byte[4096];
                    while (true)
                    {
                        try
                        {
                            Socket sock = ss.accept();
                            int bufLen = sock.getInputStream().read(buf);
                            System.out.println("<----------" +
                                    sock.getRemoteSocketAddress().toString() + "\n" +
                                    new String(buf,0, bufLen, StandardCharsets.UTF_8));
                            sock.getOutputStream().write(http);
                            sock.close();
                        }
                        catch (Exception e)
                        {
                            e.printStackTrace(System.err);
                        }
                    }
                })));
    }

    void Start()
    {
        pool.forEach(Thread::start);
    }

    void Wait()
    {
        pool.forEach(t ->
        {
            try
            {
                t.join();
            }
            catch (InterruptedException e)
            {
                e.printStackTrace(System.err);
            }
        });
    }
}
