package com.iriszero;

import java.util.List;

public interface ILogDatabase
{
    List<UserMessage> GetLast(long n);
    List<UserMessage> GetAll();
    void Add(UserMessage msg);
}
