package com.fessage;
import java.sql.*;

/**
 * @author Jaysh Khan
 */
public class DataBase {
    private final String classPath = "org.mariadb.jdbc.Driver";
    private final Connection conn;

    /**constructor it creates connection with dataBas
     * @param database
     * @throws java.lang.ClassNotFoundException
     * @throws java.sql.SQLException*/

    public DataBase(String database) throws ClassNotFoundException, SQLException {
        Class.forName(classPath);
        String location = "jdbc:mariadb://localhost:3306/"+database+"";
        this.conn= DriverManager.getConnection(
               location,"root","");
    }
    public DataBase(String table,String user,String password) throws ClassNotFoundException, SQLException {
        Class.forName(classPath);
        String location = "jdbc:mariadb://localhost:3306/"+table+"";
        this.conn= DriverManager.getConnection(location,user,password);
    }

    /**
     * @param sql *  @return Results from the database
     * @throws java.sql.SQLException*/

    public ResultSet executeQuery(String sql) throws SQLException {
        Statement stmt = conn.createStatement();
        return stmt.executeQuery(sql);
    }
}
