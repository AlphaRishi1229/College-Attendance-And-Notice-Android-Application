package com.example.anandinarvekar.pillaiattendanceandnoticeapplication;

import android.annotation.SuppressLint;
import android.os.StrictMode;
import android.util.Log;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionHelper {

    String DB, DBUserName, DBPassword;

    @SuppressLint("NewApi")
    public Connection connection()
    {
        DB = "collegesystem";
        DBUserName = "SystemServerAdmin";
        DBPassword = "Rishivg@2910";

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        java.sql.Connection connection = null;
        String ConnectionURL = null;

        try {
            Class.forName("net.sourceforge.jtds.jdbc.Driver");
            ConnectionURL = "jdbc:jtds:sqlserver://collegemanagements.database.windows.net:1433"+";databaseName="+DB+";user="+DBUserName+";password="+DBPassword+";";
            connection = DriverManager.getConnection(ConnectionURL);
        }
        catch (SQLException se)
        {
            Log.e("SQL Excpetion Occured ", se.getMessage());
        }
        catch (ClassNotFoundException e)
        {
            Log.e("Class Not Found ", e.getMessage());
        }
        catch (Exception e)
        {
            Log.e("Other Exceptions", e.getMessage());
        }
        return connection;
    }
}

