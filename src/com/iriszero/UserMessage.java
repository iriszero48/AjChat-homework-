package com.iriszero;

import com.google.gson.Gson;

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
        return new Gson().toJson(this);
    }
}
