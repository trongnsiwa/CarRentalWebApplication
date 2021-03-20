/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trongns.cart;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.naming.NamingException;
import trongns.util.MyConnection;

/**
 *
 * @author TrongNS
 */
public class CartDAO implements Serializable {
    public boolean insertCustomerCart(CartDTO dto) throws NamingException, SQLException {
        Connection con = null;
        PreparedStatement stm = null;

        try {
            con = MyConnection.makeConnection();
            if (con != null) {
                String sql = "INSERT INTO Cart (UserID, CarModelID, Amount, UserRentalDate, UserRentalTime, UserReturnDate, UserReturnTime, RentDays, UserPrice) "
                        + "VALUES (?,?,?,?,?,?,?,?,?)";
                stm = con.prepareStatement(sql);
                stm.setString(1, dto.getUserId());
                stm.setInt(2, dto.getCarModelId());
                stm.setInt(3, dto.getAmount());
                stm.setString(4, dto.getUserRentalDate());
                stm.setInt(5, dto.getUserRentalTime());
                stm.setString(6, dto.getUserReturnDate());
                stm.setInt(7, dto.getUserReturnTime());
                stm.setInt(8, dto.getRentDays());
                stm.setBigDecimal(9, dto.getUserPrice());

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
    
    public boolean checkExistedCustomerCart(String userId) throws NamingException, SQLException {
        Connection con = null;
        PreparedStatement stm = null;
        ResultSet rs = null;

        try {
            con = MyConnection.makeConnection();
            if (con != null) {
                String sql = "SELECT CarModelID "
                        + "FROM Cart "
                        + "WHERE UserID = ?";
                stm = con.prepareStatement(sql);
                stm.setString(1, userId);
                
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
    
    public boolean deleteCustomerCart(String userId) throws NamingException, SQLException {
        Connection con = null;
        PreparedStatement stm = null;

        try {
            con = MyConnection.makeConnection();
            if (con != null) {
                String sql = "DELETE FROM Cart "
                        + "WHERE UserID = ?";
                stm = con.prepareStatement(sql);
                stm.setString(1, userId);

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
    
    private ArrayList<CartDTO> customerCart = null;

    /**
     * @return the customerCart
     */
    public ArrayList<CartDTO> getCustomerCart() {
        return customerCart;
    }
    
    public void getCartByUserId(String userId) throws NamingException, SQLException {
        Connection con = null;
        PreparedStatement stm = null;
        ResultSet rs = null;

        try {
            con = MyConnection.makeConnection();
            if (con != null) {
                String sql = "SELECT CarModelID, Amount, UserRentalDate, UserRentalTime, UserReturnDate, UserReturnTime, RentDays, UserPrice "
                        + "FROM Cart "
                        + "WHERE UserID = ?";
                stm = con.prepareStatement(sql);
                stm.setString(1, userId);
                
                rs = stm.executeQuery();
                
                int carModelId;
                int amount;
                String userRentalDate, userReturnDate;
                int userRentalTime, userReturnTime, rentDays;
                BigDecimal userPrice;
                
                while (rs.next()) {
                    if (this.customerCart == null) {
                        this.customerCart = new ArrayList<>();
                    }
                    
                    carModelId = rs.getInt("CarModelID");
                    amount = rs.getInt("Amount");
                    userRentalDate = rs.getString("UserRentalDate");
                    userRentalTime = rs.getInt("UserRentalTime");
                    userReturnDate = rs.getString("UserReturnDate");
                    userReturnTime = rs.getInt("UserReturnTime");
                    rentDays = rs.getInt("RentDays");
                    userPrice = rs.getBigDecimal("UserPrice");
                    
                    this.customerCart.add(new CartDTO(userId, carModelId, amount, userRentalDate, userRentalTime, userReturnDate, userReturnTime, rentDays, userPrice));
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
