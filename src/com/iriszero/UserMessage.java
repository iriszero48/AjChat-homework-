package com.iriszero;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class UserMessage
{
    public String Date;
    public String Username;
    public String Message;

    public UserMessage(String username, String message, String date)
    {
        Username=username;
        Message=message;
        Date=date;
    }

    public String ToString()
    {
        return "<b class=\"un\">" +
                URLDecoder.decode(
                        new String(Base64.getDecoder().decode(Username)),
                        StandardCharsets.UTF_8) +
                "</b>:&nbsp;&nbsp;&nbsp;&nbsp;" +
                Message +
                "<sub>&nbsp;&nbsp;&nbsp;&nbsp;" +
                Date + "</sub>";
    }
}
