package com.iriszero;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.IntStream;

public class ChatServer
{
    private Lock lock = new ReentrantLock();
    private ILogDatabase ld;
    private IUserDatabase ud;
    private List<Client> clients = new ArrayList<>();
    private ServerSocket ws;
    private int threadNum;

    ChatServer(int port, int threadNum, IUserDatabase userDatabase, ILogDatabase logDatabase) throws IOException
    {
        ws = new ServerSocket(port);
        ld = logDatabase;
        ud = userDatabase;
        this.threadNum = threadNum;
    }

    private void UpdateMessage(UserMessage msg)
    {
        lock.lock();
        ld.Add(msg);
        byte[] data = WebSocket.Encode(msg.ToString());
        clients.forEach((x) ->
        {
            if (x.outputStream != null)
            {
                try
                {
                    x.Write(data);
                }
                catch (IOException e)
                {
                    e.printStackTrace(System.err);
                }
            }
        });
        lock.unlock();
    }

    private void SyncMessage(String msg, int id)
    {
        try
        {
            clients.get(id).Write(WebSocket.Encode(msg));
        }
        catch (IOException e)
        {
            e.printStackTrace(System.err);
        }
    }

    private String CheckUser(String[] up) throws IOException
    {
        if (up.length != 3 || !up[2].matches("^[A-Za-z0-9]{32}$"))
        {
            return "Invalid Username/Password";
        }
        try
        {
            if (Base64.getDecoder().decode(up[1]).length > 30)
            {
                return "Invalid Username/Password";
            }
        }
        catch (Exception e)
        {
            e.printStackTrace(System.err);
        }
        if (up[0].equals("l")) return ud.CheckUser(up[1],up[2]);
        if (up[0].equals("r")) return ud.AddUser(up[1],up[2]);
        return "Invalid Username/Password";
    }

    void Start()
    {
        IntStream.range(0, threadNum).forEach(id -> clients.add(new Client(id)));
        clients.forEach(Client::Start);
    }

    void Wait()
    {
        clients.forEach(Client::Wait);
    }

    class Client
    {
        private Thread thread;
        private OutputStream outputStream = null;

        Client(int id)
        {
            String end = new String(
                    new byte[]{0x03, (byte) 0xef, (byte) 0xbf, (byte) 0xbd},
                    StandardCharsets.UTF_8);
            thread = new Thread(() ->
            {
                while (true)
                {
                    try
                    {
                        Socket sock = ws.accept();
                        InputStream inputStream = sock.getInputStream();
                        outputStream = sock.getOutputStream();
                        byte[] buf = new byte[4096];
                        int len;
                        StringBuilder request = new StringBuilder();
                        while ((len = inputStream.read(buf, 0, 4096)) == 4096)
                            request.append(new String(buf, 0, 4096));
                        request.append(new String(buf, 0, len));
                        if (request.toString().contains("Upgrade: websocket"))
                        {
                            outputStream.write((
                                    "HTTP/1.1 101 Switching Protocols\r\n" +
                                            "Upgrade: websocket\r\n" +
                                            "Connection: Upgrade\r\n" +
                                            "Sec-WebSocket-Accept: " +
                                            WebSocket.SecWebSocketAccept(
                                                    request
                                                            .toString()
                                                            .split("Sec-WebSocket-Key: ")[1]
                                                            .split("\r\n")[0]) +
                                            "\r\n\r\n").getBytes());
                            len = inputStream.read(buf, 0, 4096);
                            String[] up = WebSocket.Decode(buf, len).split(" ");
                            System.out.println(String.join(" ", up));
                            String msg = CheckUser(up);
                            outputStream.write(WebSocket.Encode(msg));
                            if (msg.equals("") && up[0].equals("l"))
                            {
                                ld.GetAll().forEach(x -> SyncMessage(x.ToString(), id));
                                while (true)
                                {
                                    len = inputStream.read(buf, 0, 4096);
                                    String message = WebSocket.Decode(buf, len)
                                            .replace("<", "&lt;")
                                            .replace(">", "&gt;")
                                            .replace(" ", "&nbsp;");
                                    if (!message.equals(end))
                                    {
                                        UserMessage send = new UserMessage(URLDecoder.decode(
                                                new String(Base64.getDecoder().decode(up[1])),
                                                StandardCharsets.UTF_8),message,new java.util.Date().toString());
                                        System.out.println(send.ToString());
                                        UpdateMessage(send);
                                    }
                                }
                            }
                            sock.close();
                            outputStream = null;
                        }
                        else
                        {
                            sock.close();
                            outputStream = null;
                        }
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace(System.err);
                    }
                }
            });
        }

        void Write(byte[] data) throws IOException
        {
            outputStream.write(data);
        }

        void Start()
        {
            thread.start();
        }

        void Wait()
        {
            try
            {
                thread.join();
            }
            catch (InterruptedException e)
            {
                e.printStackTrace(System.err);
            }
        }
    }
}
