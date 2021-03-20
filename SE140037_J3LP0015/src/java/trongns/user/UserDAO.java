/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trongns.user;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.Properties;
import java.util.Random;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.naming.NamingException;
import trongns.util.MyConnection;

/**
 *
 * @author TrongNS
 */
public class UserDAO implements Serializable {

    public boolean createNewAccount(String email, String password, String firstname, String lastname, String phone, String address, String code) throws NamingException, SQLException {
        Connection con = null;
        PreparedStatement stm = null;

        try {
            con = MyConnection.makeConnection();
            if (con != null) {
                String sql = "INSERT INTO [User](UserID, Password, FirstName, LastName, Phone, Address, IsAdmin, CreatedDate, IsActive, Code) "
                        + "VALUES(?,?,?,?,?,?,?,GETDATE(),?,?)";
                stm = con.prepareStatement(sql);
                stm.setString(1, email);
                stm.setString(2, password);
                stm.setString(3, firstname);
                stm.setString(4, lastname);
                stm.setString(5, phone);
                stm.setString(6, address);
                stm.setBoolean(7, false);
                stm.setBoolean(8, false);
                stm.setString(9, code);

                int row = stm.executeUpdate();
                if (row > 0) {
                    return true;
                }
            }
        } finally {
            if (stm != null) {
                stm.close();
            }
            if (con != null) {
                con.close();
            }
        }
        return false;
    }

    private UserDTO loginUser = null;

    /**
     * @return the loginUser
     */
    public UserDTO getLoginUser() {
        return loginUser;
    }

    public void checkLogin(String email, String password) throws NamingException, SQLException {
        Connection con = null;
        PreparedStatement stm = null;
        ResultSet rs = null;

        try {
            con = MyConnection.makeConnection();
            if (con != null) {
                String sql = "SELECT FirstName, LastName, Phone, Address, IsAdmin, IsActive "
                        + "FROM [User] "
                        + "WHERE UserID = ? AND Password = ?";
                stm = con.prepareStatement(sql);
                stm.setString(1, email);
                stm.setString(2, password);

                rs = stm.executeQuery();

                String fullname, phone, address, role, status;

                if (rs.next()) {
                    fullname = rs.getString("FirstName") + " " + rs.getString("Lastname");
                    phone = rs.getString("Phone");
                    address = rs.getString("Address");
                    if (rs.getBoolean("IsAdmin")) {
                        role = "Admin";
                    } else {
                        role = "Customer";
                    }
                    if (rs.getBoolean("IsActive")) {
                        status = "Active";
                    } else {
                        status = "Inactive";
                    }

                    this.loginUser = new UserDTO(email, password, fullname, phone, address, role, status);
                }
            }
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (stm != null) {
                stm.close();
            }
            if (con != null) {
                con.close();
            }
        }
    }

    public boolean checkExistAccount(String email) throws NamingException, SQLException {
        Connection con = null;
        PreparedStatement stm = null;
        ResultSet rs = null;

        try {
            con = MyConnection.makeConnection();
            if (con != null) {
                String sql = "SELECT FirstName, LastName "
                        + "FROM [User] "
                        + "WHERE UserID = ?";
                stm = con.prepareStatement(sql);
                stm.setString(1, email);

                rs = stm.executeQuery();

                if (rs.next()) {
                    return true;
                }
            }
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (stm != null) {
                stm.close();
            }
            if (con != null) {
                con.close();
            }
        }
        return false;
    }

    public boolean checkActiveAccount(String email) throws NamingException, SQLException {
        Connection con = null;
        PreparedStatement stm = null;
        ResultSet rs = null;

        try {
            con = MyConnection.makeConnection();
            if (con != null) {
                String sql = "SELECT IsActive "
                        + "FROM [User] "
                        + "WHERE UserID = ?";
                stm = con.prepareStatement(sql);
                stm.setString(1, email);

                rs = stm.executeQuery();

                if (rs.next()) {
                    if (rs.getBoolean("IsActive")) {
                        return true;
                    }
                }
            }
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (stm != null) {
                stm.close();
            }
            if (con != null) {
                con.close();
            }
        }
        return false;
    }

    public int sendMail(String from, String to, String subject, String message, String code) {
        try {
            Properties prop = System.getProperties();
            prop.setProperty("mail.transport.protocol", "smtp");
            prop.setProperty("mail.host", "smtp.gmail.com");
            prop.put("mail.smtp.auth", "true");
            prop.put("mail.smtp.port", "465");
            prop.put("mail.debug", "true");
            prop.put("mail.smtp.socketFactory.port", "465");
            prop.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
            prop.put("mail.smtp.socketFactory.fallback", "false");
            Authenticator auth = new SMTPAuthenticator();
            Session session = Session.getInstance(prop, auth);

            Message msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress(from));
            msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to, false));
            msg.setSubject(subject);
            msg.setText(message + "Please enter the following code to verify your email: " + code);

            msg.setHeader("MyMail", "Mr. Trong");
            msg.setSentDate(new Date());
            Transport.send(msg);
            return 0;
        } catch (MessagingException ex) {
            return -1;
        }
    }

    private class SMTPAuthenticator extends Authenticator {

        @Override
        public PasswordAuthentication getPasswordAuthentication() {
            String username = "trongnsse140037@fpt.edu.vn";
            String password = "Iwa020179";
            return new PasswordAuthentication(username, password);
        }
    }

    public boolean checkVerifyCode(String email, String code) throws NamingException, SQLException {
        Connection con = null;
        PreparedStatement stm = null;
        ResultSet rs = null;

        try {
            con = MyConnection.makeConnection();
            if (con != null) {
                String sql = "SELECT UserID, Code "
                        + "FROM [User] "
                        + "WHERE UserID = ? AND Code = ?";
                stm = con.prepareStatement(sql);
                stm.setString(1, email);
                stm.setString(2, code);

                rs = stm.executeQuery();

                if (rs.next()) {
                    return true;
                }
            }
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (stm != null) {
                stm.close();
            }
            if (con != null) {
                con.close();
            }
        }
        return false;
    }

    public boolean changeAccountStatus(String email) throws NamingException, SQLException {
        Connection con = null;
        PreparedStatement stm = null;

        try {
            con = MyConnection.makeConnection();
            if (con != null) {
                String sql = "UPDATE [User] "
                        + "SET IsActive = ? "
                        + "WHERE UserID = ?";
                stm = con.prepareStatement(sql);
                stm.setBoolean(1, true);
                stm.setString(2, email);

                int row = stm.executeUpdate();
                if (row > 0) {
                    return true;
                }
            }
        } finally {
            if (stm != null) {
                stm.close();
            }
            if (con != null) {
                con.close();
            }
        }
        return false;
    }
    
    public String generateRandomString() {
        String NUMBER_STR = "123456789";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            sb.append(NUMBER_STR.charAt(new Random().nextInt(NUMBER_STR.length())));
        }
        return sb.toString();
    }
    
    private UserDTO userInfo = null;

    /**
     * @return the userInfo
     */
    public UserDTO getUserInfo() {
        return userInfo;
    }
    
    public void getUserInfoByEmail(String email) throws NamingException, SQLException {
        Connection con = null;
        PreparedStatement stm = null;
        ResultSet rs = null;

        try {
            con = MyConnection.makeConnection();
            if (con != null) {
                String sql = "SELECT FirstName, LastName, Phone, Address, IsAdmin, IsActive "
                        + "FROM [User] "
                        + "WHERE UserID = ?";
                stm = con.prepareStatement(sql);
                stm.setString(1, email);

                rs = stm.executeQuery();

                String fullname, phone, address, role, status;

                if (rs.next()) {
                    fullname = rs.getString("FirstName") + " " + rs.getString("Lastname");
                    phone = rs.getString("Phone");
                    address = rs.getString("Address");

                    this.userInfo = new UserDTO(email, fullname, phone, address);
                }
            }
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (stm != null) {
                stm.close();
            }
            if (con != null) {
                con.close();
            }
        }
    }
    
    private String nameOfUser = null;

    /**
     * @return the nameOfUser
     */
    public String getNameOfUser() {
        return nameOfUser;
    }
    
    public void getUserFullname(String email) throws NamingException, SQLException {
        Connection con = null;
        PreparedStatement stm = null;
        ResultSet rs = null;

        try {
            con = MyConnection.makeConnection();
            if (con != null) {
                String sql = "SELECT FirstName, LastName "
                        + "FROM [User] "
                        + "WHERE UserID = ?";
                stm = con.prepareStatement(sql);
                stm.setString(1, email);

                rs = stm.executeQuery();

                if (rs.next()) {
                    this.nameOfUser = rs.getString("FirstName") + " " + rs.getString("LastName");
                }
            }
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (stm != null) {
                stm.close();
            }
            if (con != null) {
                con.close();
            }
        }
    }
}
