package com.example.gift;

import java.sql.*;

public class DB_Connection
{
    public static String SENDERS_EMAILID="giftstore202156@gmail.com";
    public static String SENDERS_PASSWORD="gift1234";
    public static Connection get_DBConnection()
    {
        Connection conn=null;
        try
        {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://10.0.2.2:3306/giftstore_db", "root", "");

        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return conn;
    }

}




