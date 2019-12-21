package com.iriszero;

class UserMessage
{
    String Date;
    String Username;
    String Message;

    UserMessage(String username, String message, String date)
    {
        Username=username;
        Message=message;
        Date=date;
    }

    String ToString()
    {
        return "<b class=\"un\">" + Username +
                "</b>:&nbsp;&nbsp;&nbsp;&nbsp;" +
                Message +
                "<sub>&nbsp;&nbsp;&nbsp;&nbsp;" +
                Date + "</sub>";
    }
}
