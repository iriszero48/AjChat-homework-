package com.iriszero;

import java.util.List;

public interface ILogDatabase
{
    List<UserMessage> GetLast(long n);
    List<String> GetAllString();
    void Add(UserMessage msg);
}
