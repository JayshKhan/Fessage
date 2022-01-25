package com.fessage;
import java.sql.*;
import java.io.FileReader;
import java.util.Properties;

/**
 * @author Jaysh Khan
 */
public class DataBase {
    private final String classPath;
    private final Connection conn;
    private Properties properties;

    /**constructor it creates connection with dataBas
     * @param database
     * @throws java.lang.ClassNotFoundException
     * @throws java.sql.SQLException*/

    public DataBase(String database) throws ClassNotFoundException, SQLException {
        //getting config file
        try (FileReader file=new FileReader("config")){
            properties=new Properties();
            properties.load(file);
        } catch (Exception e) {
        }
        //reading config file
        String DIR = properties.getProperty("DATABASE_DIRECTORY");
        classPath=properties.getProperty("CLASSPATH");
        String User=properties.getProperty("DATABASE_USER");
        String Password =properties.getProperty("DATABASE_PASSWORD");
        System.out.println("DIR: "+DIR+" classPath: "+classPath);
        
        Class.forName(classPath);
        String location = DIR+database;
        this.conn= DriverManager.getConnection(location,User,Password);
    }
    public DataBase(String table,String user,String password) throws ClassNotFoundException, SQLException {
        try (FileReader file=new FileReader("config")){
            properties=new Properties();
            properties.load(file);
        } catch (Exception e) {
        }
        classPath=properties.getProperty("CLASSPATH");
        String DIR = properties.getProperty("DATABASE_DIRECTORY");
        
        
        Class.forName(classPath);
        String location = DIR+table;
        this.conn= DriverManager.getConnection(location,user,password);
    }

    /**
     * @param sql *  @return Results from the database
     * @return 
     * @throws java.sql.SQLException*/

    public ResultSet executeQuery(String sql) throws SQLException {
        Statement stmt = conn.createStatement();
        return stmt.executeQuery(sql);
    }
}
