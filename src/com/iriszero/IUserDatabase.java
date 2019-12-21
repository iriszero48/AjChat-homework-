package com.iriszero;

public interface IUserDatabase
{
    String AddUser(String username, String password);
    String CheckUser(String username, String password);
}
