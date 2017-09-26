package com.avit.common.http;



public class Utils
{
    public static boolean checkRequestMethod(String method)
    {
        if(method.equalsIgnoreCase(HttpRequest.METHOD_GET)||method.equalsIgnoreCase(HttpRequest.METHOD_DELETE)||
                method.equalsIgnoreCase(HttpRequest.METHOD_POST)||method.equalsIgnoreCase(HttpRequest.METHOD_POST))
        {
            return true;
        }
        return false;
    }
    
    public static boolean checkIsLong(String str)
    {
        try
        {
            Long.valueOf(str);
            return true;
        }
        catch (NumberFormatException e)
        {
            e.printStackTrace();
            return false;
        }
    }
    
    public static boolean checkIsInteger(String str)
    {
        try
        {
            Integer.valueOf(str);
            return true;
        }
        catch (NumberFormatException e)
        {
            e.printStackTrace();
            return false;
        }
    }
    public static boolean isEmpty(Object j)
    {
        if(j==null||j.equals(""))
        {
            return true;
        }
        return false;
    }



}
