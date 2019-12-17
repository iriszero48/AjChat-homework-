package com.iriszero;

public interface IUserDatabase
{
    public String AddUser(String username, String password);
    public String CheckUser(String username, String password);
}
