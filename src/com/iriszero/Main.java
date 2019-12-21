package com.iriszero;

public class Main
{
    public static void main(String[] argv)
    {
        try
        {
            IUserDatabase ud = null;
            ILogDatabase ld = null;
            if (argv.length == 7)
            {
                ud = new AjUserDatabase(argv[5]);
                ld = new AjLogDatabase(argv[6]);
            }
            else if (argv.length == 8)
            {
                ud = new MySqlUserDatabase(argv[5], argv[6], argv[7]);
                ld = new MySqlLogDatabase(argv[5], argv[6], argv[7]);
            }
            else
            {
                System.err.println("./main IndexFilePath IndexPort IndexThreadNum WebSocketPort WebSocketThreadNum UserDatabasePath LogDatabasePath");
                System.err.println("./main IndexFilePath IndexPort IndexThreadNum WebSocketPort WebSocketThreadNum MySqlUser MySqlPassword MySqlUserDatabaseUrl");
                System.exit(1);
            }
            WebServer ws = new WebServer(argv[0], Integer.parseInt(argv[1]), Integer.parseInt(argv[2]));
            ChatServer cs = new ChatServer(Integer.parseInt(argv[3]), Integer.parseInt(argv[4]), ud, ld);
            ws.Start();
            cs.Start();
            ws.Wait();
            cs.Wait();
        }
        catch (Exception e)
        {
            e.printStackTrace(System.err);
        }
    }
}

