/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trongns.feedback;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import javax.naming.NamingException;
import trongns.util.MyConnection;

/**
 *
 * @author TrongNS
 */
public class FeedbackDAO implements Serializable {

    public boolean insertFeedback(String orderId, int carModelId, String nameOfUser, String feedback, int rating) throws NamingException, SQLException {
        Connection con = null;
        PreparedStatement stm = null;

        try {
            con = MyConnection.makeConnection();
            if (con != null) {
                String sql = "INSERT INTO [Feedback] (OrderID, CarModelID, NameOfUser, Rating, Feedback, CreatedDate) "
                        + "VALUES (?,?,?,?,N'" + feedback + "',GETDATE())";
                stm = con.prepareStatement(sql);
                stm.setString(1, orderId);
                stm.setInt(2, carModelId);
                stm.setString(3, nameOfUser);
                stm.setInt(4, rating);

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
    
    public boolean updateFeedback(String orderId, int carModelId, String feedback, int rating) throws NamingException, SQLException {
        Connection con = null;
        PreparedStatement stm = null;

        try {
            con = MyConnection.makeConnection();
            if (con != null) {
                String sql = "UPDATE [Feedback] "
                        + "SET Feedback = N'" + feedback + "', Rating = ? "
                        + "WHERE OrderID = ? AND CarModelID = ?";
                stm = con.prepareStatement(sql);
                stm.setInt(1, rating);
                stm.setString(2, orderId);
                stm.setInt(3, carModelId);

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

    private FeedbackDTO orderDetailFeedback = null;

    /**
     * @return the orderDetailFeedback
     */
    public FeedbackDTO getOrderDetailFeedback() {
        return orderDetailFeedback;
    }

    public void loadFeedbackByOrderDetail(String orderId, int carModelId) throws NamingException, SQLException {
        this.orderDetailFeedback = null;
        Connection con = null;
        PreparedStatement stm = null;
        ResultSet rs = null;

        try {
            con = MyConnection.makeConnection();
            if (con != null) {
                String sql = "SELECT Rating, Feedback "
                        + "FROM [Feedback] "
                        + "WHERE OrderID = ? AND CarModelID = ?";
                stm = con.prepareStatement(sql);
                stm.setString(1, orderId);
                stm.setInt(2, carModelId);

                rs = stm.executeQuery();

                String feedback;
                int rating;

                if (rs.next()) {
                    rating = rs.getInt("Rating");
                    feedback = rs.getString("Feedback");

                    this.orderDetailFeedback = new FeedbackDTO(rating, feedback);
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
    
    public boolean checkExistedFeedback(String orderId, int carModelId) throws NamingException, SQLException {
        this.orderDetailFeedback = null;
        Connection con = null;
        PreparedStatement stm = null;
        ResultSet rs = null;

        try {
            con = MyConnection.makeConnection();
            if (con != null) {
                String sql = "SELECT Rating, Feedback "
                        + "FROM [Feedback] "
                        + "WHERE OrderID = ? AND CarModelID = ?";
                stm = con.prepareStatement(sql);
                stm.setString(1, orderId);
                stm.setInt(2, carModelId);

                rs = stm.executeQuery();

                String feedback;
                int rating;

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

    private ArrayList<FeedbackDTO> listCarModelFeedback = null;

    /**
     * @return the listCarModelFeedback
     */
    public ArrayList<FeedbackDTO> getListCarModelFeedback() {
        return listCarModelFeedback;
    }

    public void loadCarModelFeedback(int carModelId) throws NamingException, SQLException {
        Connection con = null;
        PreparedStatement stm = null;
        ResultSet rs = null;

        try {
            con = MyConnection.makeConnection();
            if (con != null) {
                String sql = "SELECT OrderID, NameOfUser, Rating, Feedback, CreatedDate "
                        + "FROM [Feedback] "
                        + "WHERE CarModelID = ? ";
                stm = con.prepareStatement(sql);
                stm.setInt(1, carModelId);

                rs = stm.executeQuery();

                String orderId;
                String nameOfUser;
                String feedback;
                int rating;
                Date createdDate;

                while (rs.next()) {
                    orderId = rs.getString("OrderID");
                    nameOfUser = rs.getString("NameOfUser");
                    rating = rs.getInt("Rating");
                    feedback = rs.getString("Feedback");
                    createdDate = rs.getTimestamp("CreatedDate");

                    if (this.listCarModelFeedback == null) {
                        this.listCarModelFeedback = new ArrayList<>();
                    }

                    this.listCarModelFeedback.add(new FeedbackDTO(orderId, carModelId, nameOfUser, rating, feedback, createdDate));
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
