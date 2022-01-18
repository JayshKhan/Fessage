/**
 @author Jaysh Khan
 */
package com.fessage;
//it is not implimented yet

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Random;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class User {
    private String name;
    private int id;
    private int limit;
    
    public int loginUser(String userName,String PasswordHash) throws SQLException, ClassNotFoundException
    {
        int suceessLogin=0;  //0 if not match, 1 if everything is good 2 if password doesnot match//3 means account banned
        String sql = String.format("SELECT * FROM user where username = '%s'", userName);
        DataBase db = new DataBase("acp");
        ResultSet rs= db.executeQuery(sql);
        if(rs.next())
        {
            suceessLogin=2;
            int userId  = rs.getInt("Id");
            String _userName = rs.getString("UserName");
            String _passwordHash = rs.getString("PasswordHash");
            int roleId = rs.getInt("RoleId");
            String roleCode = rs.getString("RoleCode");
            String email = rs.getString("Email");
            String phone = rs.getString("PhoneNumber");
            int attempts = rs.getInt("WrongLoginAttempts");
            if(attempts>3)
            {
                return 3;
            }
            if(_passwordHash.equals(PasswordHash))
            {
                this.id=userId;
                this.name=_userName;
                suceessLogin=1;
            }
            else{
                attempts=attempts+1;
                String query=String.format("UPDATE `user` SET `WrongLoginAttempts`='%s' WHERE `UserName` = '%s'",attempts,userName);
                System.out.println(query);
                db.executeQuery(query);
            }
        }
        return suceessLogin;
    }
    public void UserSignup(String _email, String _Username, String PassHash, String PassSalt) throws ClassNotFoundException, SQLException {
        String OTP=generateOTP();
        String sqlInsert = "INSERT INTO `acp`.`user`(`FirstName`,`MiddleName`,`LastName`,`Email`,`UserName`,`PasswordText`,`PasswordHash`,`Salt`,`WrongLoginAttempts`,`PasswordResetLink`,`PhoneNumber`,`EmailConfirmed`,`PhoneConfirmed`" +
                ",`Status`,`RoleId`,`RoleCode`,`IsPrivate`,`IsLoggedIn`,`FirstLogin`" +
                ",`CompanyId`,`Department`,`BirthDate`,`Gender`,`CreatedDate`,`CreatedBy`" +
                ",`LastModifiedDate`,`LastModifiedBy`,`SoftDelete`,`CreditCardId`,`SubscriptionId`" +
                ",`IsSystemCreated`,`OTP`,`AddressLine1`,`AddressLine2`,`CountryCode`,`StateOrRegion`" +
                ",`CountyOrDistrict`,`CityOrTown`,`ZipOrPostalCode`,`ContactPersonName`,`ContactPersonMobile`" +
                ",`Latitude`,`Longitude`)" +
                "VALUES('"+_Username+"','','','" + _email + "','" + _Username + "','','" + PassHash + "','" + PassSalt + "',0,'','00',0,1,0,0,'STUDENT',0,0,0,1,'DEV','1978-1-05','Male'," +
                "CURDATE(),'Jaysh',null,'',0,0,0,1,'" + OTP + "','House 11,street 1','gulberg coloney','PK','Punjab','Rawalpindi','Wah Cantt','40474','Basharat Hussain','0300123456',33.7715,72.7511);";
        DataBase db = new DataBase("acp");
        try{
        ResultSet rs= db.executeQuery(sqlInsert);
        
        this.name=_Username;
        }catch (SQLException ex)
        {
          JFrame j= new JFrame();
                JOptionPane.showMessageDialog(j, ex);
        }
    }
    private String generateOTP() {
        Random rand = new Random();
        return String.format("%04d%n", rand.nextInt(10000));
    }
    
//    public User(String name, int id, int limit) {
//        this.name = name;
//        this.id = id;
//        this.limit = limit;
//    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }
    
    
}
