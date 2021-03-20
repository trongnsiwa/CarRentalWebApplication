/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trongns.order;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import javax.naming.NamingException;
import trongns.util.MyConnection;

/**
 *
 * @author TrongNS
 */
public class OrderDAO implements Serializable {

    public boolean insertOrder(OrderDTO dto) throws SQLException, NamingException {
        Connection con = null;
        PreparedStatement stm = null;

        try {
            con = MyConnection.makeConnection();
            if (con != null) {
                String sql = "INSERT INTO [Order](OrderID, UserID, RentalDate, DiscountCode, TotalPrice, IsActivate) "
                        + "VALUES(?,?,?,?,?,?)";
                stm = con.prepareStatement(sql);
                stm.setString(1, dto.getOrderId());
                stm.setString(2, dto.getUserId());
                stm.setTimestamp(3, new Timestamp(dto.getRentalDate().getTime()));
                stm.setString(4, dto.getDiscountCode());
                stm.setBigDecimal(5, dto.getTotalPrice());
                stm.setBoolean(6, dto.isActivate());

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

    public boolean checkExistedOrderId(String orderId) throws NamingException, SQLException {
        Connection con = null;
        PreparedStatement stm = null;
        ResultSet rs = null;

        try {
            con = MyConnection.makeConnection();
            if (con != null) {
                String sql = "SELECT UserId "
                        + "FROM [Order] "
                        + "WHERE OrderID = ?";
                stm = con.prepareStatement(sql);
                stm.setString(1, orderId);

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

    private ArrayList<String> listUserOrderId = null;

    /**
     * @return the listUserOrderId
     */
    public ArrayList<String> getListUserOrderId() {
        return listUserOrderId;
    }

    public void loadUserOrderIdList(String userId) throws NamingException, SQLException {
        Connection con = null;
        PreparedStatement stm = null;
        ResultSet rs = null;

        try {
            con = MyConnection.makeConnection();
            if (con != null) {
                String sql = "SELECT OrderID "
                        + "FROM [Order] "
                        + "WHERE UserID = ?";
                stm = con.prepareStatement(sql);
                stm.setString(1, userId);

                rs = stm.executeQuery();

                while (rs.next()) {
                    if (this.listUserOrderId == null) {
                        this.listUserOrderId = new ArrayList<>();
                    }

                    this.listUserOrderId.add(rs.getString("OrderID"));
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

    private ArrayList<OrderDTO> listSearchedOrders = null;

    /**
     * @return the listSearchedOrders
     */
    public ArrayList<OrderDTO> getListSearchedOrders() {
        return listSearchedOrders;
    }

    public void searchOrdersWithConditions(String userId, String keyword, Date rentalDate, int pageSize, int pageNo) throws SQLException, NamingException {
        Connection con = null;
        PreparedStatement stm = null;
        ResultSet rs = null;
        int offset = pageSize * (pageNo - 1);

        try {
            boolean haveKeyword = false;
            con = MyConnection.makeConnection();
            if (con != null) {
                String sql = "SELECT DISTINCT r.OrderID, r.RentalDate, r.DiscountCode, r.TotalPrice, r.IsActivate "
                        + "FROM [Order] AS r ";
                if (keyword != null && !keyword.trim().isEmpty()) {
                    sql += "FULL OUTER JOIN [OrderDetail] AS rd "
                            + "ON r.OrderID = rd.OrderID ";
                    haveKeyword = true;
                }
                sql += "WHERE r.UserID = ? ";
                if (rentalDate != null) {
                    sql += "AND (CONVERT(date, r.RentalDate) = ? ";
                    if (haveKeyword) {
                        sql += "OR rd.CarName LIKE ? )";
                    } else {
                        sql += ")";
                    }
                } else {
                    if (haveKeyword) {
                        sql += "AND rd.CarName LIKE ? ";
                    }
                }

                sql += "ORDER BY r.IsActivate DESC, r.RentalDate DESC "
                        + "OFFSET " + offset + " ROWS "
                        + "FETCH NEXT " + pageSize + " ROWS ONLY";

                stm = con.prepareStatement(sql);
                stm.setString(1, userId);
                if (rentalDate != null) {
                    stm.setTimestamp(2, new Timestamp(rentalDate.getTime()));
                    if (haveKeyword) {
                        stm.setString(3, "%" + keyword + "%");
                    }
                } else {
                    if (haveKeyword) {
                        stm.setString(2, "%" + keyword + "%");
                    }
                }

                rs = stm.executeQuery();

                String orderId, discountCode;
                Date urentalDate;
                BigDecimal totalPrice;

                while (rs.next()) {
                    orderId = rs.getString("OrderID");
                    urentalDate = rs.getTimestamp("RentalDate");
                    discountCode = rs.getString("DiscountCode");
                    totalPrice = rs.getBigDecimal("TotalPrice");

                    if (this.listSearchedOrders == null) {
                        this.listSearchedOrders = new ArrayList<>();
                    }

                    OrderDTO dto = new OrderDTO(orderId, userId, urentalDate, discountCode, totalPrice, true);
                    this.listSearchedOrders.add(dto);
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

    private int totalOrders = 0;

    /**
     * @return the totalOrders
     */
    public int getTotalOrders() {
        return totalOrders;
    }

    public void countAllOrdersByUserId(String userId) throws NamingException, SQLException {
        Connection con = null;
        PreparedStatement stm = null;
        ResultSet rs = null;

        try {
            con = MyConnection.makeConnection();
            if (con != null) {
                String sql = "SELECT COUNT(OrderID) AS total "
                        + "FROM [Order] "
                        + "WHERE UserId = ? ";
                stm = con.prepareStatement(sql);
                stm.setString(1, userId);
                rs = stm.executeQuery();
                if (rs.next()) {
                    this.totalOrders = rs.getInt("total");
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

    private int totalSearchedOrders = 0;

    /**
     * @return the totalSearchedOrders
     */
    public int getTotalSearchedOrders() {
        return totalSearchedOrders;
    }

    public void countOrdersWithConditions(String userId, String keyword, Date rentalDate) throws NamingException, SQLException {
        Connection con = null;
        PreparedStatement stm = null;
        ResultSet rs = null;

        try {
            boolean haveKeyword = false;
            con = MyConnection.makeConnection();
            if (con != null) {
                String sql = "SELECT COUNT(DISTINCT r.OrderID) AS total "
                        + "FROM [Order] AS r ";
                if (keyword != null && !keyword.trim().isEmpty()) {
                    sql += "FULL OUTER JOIN [OrderDetail] AS rd "
                            + "ON r.OrderID = rd.OrderID ";
                    haveKeyword = true;
                }
                sql += "WHERE r.UserID = ? ";
                if (rentalDate != null) {
                    sql += "AND (CONVERT(date, r.RentalDate) = ? ";
                    if (haveKeyword) {
                        sql += "OR rd.CarName LIKE ? )";
                    } else {
                        sql += ")";
                    }
                } else {
                    if (haveKeyword) {
                        sql += "AND rd.CarName LIKE ? ";
                    }
                }

                stm = con.prepareStatement(sql);
                stm.setString(1, userId);
                if (rentalDate != null) {
                    stm.setTimestamp(2, new Timestamp(rentalDate.getTime()));
                    if (haveKeyword) {
                        stm.setString(3, "%" + keyword + "%");
                    }
                } else {
                    if (haveKeyword) {
                        stm.setString(2, "%" + keyword + "%");
                    }
                }

                rs = stm.executeQuery();
                if (rs.next()) {
                    this.totalSearchedOrders = rs.getInt("total");
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

    public boolean updateActivateStatusOfOrder(String orderId, boolean activate) throws NamingException, SQLException {
        Connection con = null;
        PreparedStatement stm = null;

        try {
            con = MyConnection.makeConnection();
            if (con != null) {
                String sql = "UPDATE [Order] "
                        + "SET IsActivate = ? "
                        + "WHERE orderID = ?";
                stm = con.prepareStatement(sql);
                stm.setBoolean(1, activate);
                stm.setString(2, orderId);

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
}
