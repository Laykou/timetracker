package com.jci.bbc.timetracker.utils;

import com.google.common.base.Charsets;
import com.google.common.hash.Hashing;


public class Utils
{
    public static String hashPassword(String password)
    {
        return Hashing.sha1().hashString(password, Charsets.UTF_8).toString();
    }
}
