package s25.cs151.application.Models;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectDB {
    private Connection connection;
    public ConnectDB(String url){
        try{
            connection = DriverManager.getConnection(url);
            System.out.println("Successfully Connected");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Connection getConnection(){
        return connection;
    }

    public void closeConnection(){
        try{
            if(connection!=null){
                System.out.println("Connection Closed!");
                connection.close();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
