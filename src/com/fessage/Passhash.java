package com.fessage;


import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.sql.ResultSet;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
/** @author Jaysh Khan */
public class Passhash {
    public String passHashOld(String Username,String Password) throws Exception {
        String passwordToHash = Password;
        String salt = getsaltFromDB(Username);
        String securePassword = get_SHA_256_SecurePassword(passwordToHash, salt);
        return securePassword;
    }
    public String[] passHashNew(String Password) throws Exception {
        String [] hash_salt = new String[2];
        String passwordToHash = Password;
        String salt;
        String securePassword;
        salt = getSalt();
        securePassword = get_SHA_256_SecurePassword(passwordToHash, salt);
        hash_salt[0]=securePassword;
        hash_salt[1]=salt;
        return hash_salt;
    }
    private static String get_SHA_256_SecurePassword(String passwordToHash,
                                                     String salt) {
        String generatedPassword = null;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(salt.getBytes());
            byte[] bytes = md.digest(passwordToHash.getBytes());
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < bytes.length; i++) {
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16)
                        .substring(1));
            }
            generatedPassword = sb.toString();
        } catch (NoSuchAlgorithmException ex) {
            JFrame j= new JFrame();
                JOptionPane.showMessageDialog(j, ex);
        }
        return generatedPassword;
    }
    // Add salt
    private static String getSalt() throws NoSuchAlgorithmException {
        SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
        byte[] salt = new byte[16];
        sr.nextBytes(salt);
        return salt.toString();
    }
    private static String getsaltFromDB(String Username) throws Exception {
        DataBase db = new DataBase("acp");
        String sql = "SELECT `Salt` FROM `user` WHERE `UserName` = '"+Username+"'";
        ResultSet rs = db.executeQuery(sql);
        if(rs.next())
            return rs.getString(1);
        else
            throw new Exception("Username Invalid");

    }
}