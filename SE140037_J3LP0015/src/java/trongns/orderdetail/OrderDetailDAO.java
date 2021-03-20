/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trongns.orderdetail;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import javax.naming.NamingException;
import trongns.util.MyConnection;

/**
 *
 * @author TrongNS
 */
public class OrderDetailDAO implements Serializable{
    public boolean insertOrderDetail(OrderDetailDTO dto) throws NamingException, SQLException {
        Connection con = null;
        PreparedStatement stm = null;

        try {
            con = MyConnection.makeConnection();
            if (con != null) {
                String sql = "INSERT INTO OrderDetail (OrderID, CarModelID, CarName, Image, Amount, SubTotal, PickUpDate, DropOffDate, Days, IsRenting) "
                        + "VALUES (?,?,?,?,?,?,?,?,?,?)";
                stm = con.prepareStatement(sql);
                stm.setString(1, dto.getOrderId());
                stm.setInt(2, dto.getCarModelId());
                stm.setString(3, dto.getCarName());
                stm.setString(4, dto.getImageLink());
                stm.setInt(5, dto.getAmount());
                stm.setBigDecimal(6, dto.getSubTotal());
                stm.setTimestamp(7, new Timestamp(dto.getPickUpDate().getTime()));
                stm.setTimestamp(8, new Timestamp(dto.getDropOffDate().getTime()));
                stm.setInt(9, dto.getDays());
                stm.setBoolean(10, dto.isRenting());

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
    
    private ArrayList<OrderDetailDTO> listDropOffCar = null;

    /**
     * @return the listDropOffCar
     */
    public ArrayList<OrderDetailDTO> getListDropOffCar() {
        return listDropOffCar;
    }
    
    public void loadDropOffCarList() throws NamingException, SQLException {
        Connection con = null;
        PreparedStatement stm = null;
        ResultSet rs = null;

        try {
            con = MyConnection.makeConnection();
            if (con != null) {
                String sql = "SELECT OrderID, CarModelID "
                        + "FROM [OrderDetail] "
                        + "WHERE DropOffDate < GETDATE() AND IsRenting = ?";
                stm = con.prepareStatement(sql);
                stm.setBoolean(1, true);

                rs = stm.executeQuery();
                
                String orderId;
                int carModelId;

                while (rs.next()) {
                    orderId = rs.getString("OrderID");
                    carModelId = rs.getInt("CarModelID");
                    
                    if (this.listDropOffCar == null) {
                        this.listDropOffCar = new ArrayList<>();
                    }
                    
                    OrderDetailDTO dto = new OrderDetailDTO();
                    dto.setOrderId(orderId);
                    dto.setCarModelId(carModelId);
                    this.listDropOffCar.add(dto);
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
    
    private HashMap<Integer, Integer> listCarModelBetweenDates = null;
    
    /**
     * @return the listCarModelBetweenDates
     */
    public HashMap<Integer, Integer> getListCarModelBetweenDates() {
        return listCarModelBetweenDates;
    }
    
    public void loadCarModelBetweenDates(Date rentalDate, Date returnDate) throws NamingException, SQLException {
        this.listCarModelBetweenDates = null;
        Connection con = null;
        PreparedStatement stm = null;
        ResultSet rs = null;

        try {
            con = MyConnection.makeConnection();
            if (con != null) {
                String sql = "SELECT CarModelID, Amount "
                        + "FROM OrderDetail "
                        + "WHERE ((PickUpDate BETWEEN ? AND ?) "
                        + "OR (DropOffDate BETWEEN ? AND ?) "
                        + "OR (PickUpDate <= ? AND DropOffDate >= ?)) "
                        + "AND IsRenting = ?";
                stm = con.prepareStatement(sql);
                stm.setTimestamp(1, new Timestamp(rentalDate.getTime()));
                stm.setTimestamp(2, new Timestamp(returnDate.getTime()));
                stm.setTimestamp(3, new Timestamp(rentalDate.getTime()));
                stm.setTimestamp(4, new Timestamp(returnDate.getTime()));
                stm.setTimestamp(5, new Timestamp(rentalDate.getTime()));
                stm.setTimestamp(6, new Timestamp(returnDate.getTime()));
                stm.setBoolean(7, true);
                
                rs = stm.executeQuery();
                
                int carModelId, amount;
                
                while (rs.next()) {
                    carModelId = rs.getInt("CarModelID");
                    amount = rs.getInt("Amount");
                    
                    if (this.listCarModelBetweenDates == null) {
                        this.listCarModelBetweenDates = new HashMap<>();
                    }
                    
                    if (this.listCarModelBetweenDates.containsKey(carModelId)) {
                        amount += this.listCarModelBetweenDates.get(carModelId);
                    }
                    
                    this.listCarModelBetweenDates.put(carModelId, amount);
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
    
    private int amountOfCarModel = 0;

    /**
     * @return the amountOfCarModel
     */
    public int getAmountOfCarModel() {
        return amountOfCarModel;
    }
    
    public void getCarModelAmount(int carModelId, String orderId) throws NamingException, SQLException {
        Connection con = null;
        PreparedStatement stm = null;
        ResultSet rs = null;

        try {
            con = MyConnection.makeConnection();
            if (con != null) {
                String sql = "SELECT Amount "
                        + "FROM [OrderDetail] "
                        + "WHERE OrderID = ? AND CarModelID = ?";
                stm = con.prepareStatement(sql);
                stm.setString(1, orderId);
                stm.setInt(2, carModelId);

                rs = stm.executeQuery();

                if (rs.next()) {
                    this.amountOfCarModel = rs.getInt("Amount");
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

    public boolean updateRentingStatus(String orderId, int carModelId, boolean renting) throws NamingException, SQLException {
        Connection con = null;
        PreparedStatement stm = null;
        ResultSet rs = null;

        try {
            con = MyConnection.makeConnection();
            if (con != null) {
                String sql = "UPDATE OrderDetail "
                        + "SET IsRenting = ? "
                        + "WHERE OrderID = ? AND CarModelID = ?";
                stm = con.prepareStatement(sql);
                stm.setBoolean(1, renting);
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
    
     public boolean updateAllRentingStatusOfOrder(String orderId, boolean renting) throws NamingException, SQLException {
        Connection con = null;
        PreparedStatement stm = null;
        ResultSet rs = null;

        try {
            con = MyConnection.makeConnection();
            if (con != null) {
                String sql = "UPDATE OrderDetail "
                        + "SET IsRenting = ? "
                        + "WHERE OrderID = ?";
                stm = con.prepareStatement(sql);
                stm.setBoolean(1, renting);
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
    
    private ArrayList<OrderDetailDTO> listHistoryOrderDetals = null;

    /**
     * @return the listHistoryOrderDetals
     */
    public ArrayList<OrderDetailDTO> getListHistoryOrderDetals() {
        return listHistoryOrderDetals;
    }
    
    public void loadListOrderDetailByOrderId(String orderId) throws SQLException, NamingException {
        this.listHistoryOrderDetals = null;
        Connection con = null;
        PreparedStatement stm = null;
        ResultSet rs = null;
        
        try {
            con = MyConnection.makeConnection();
            if (con != null) {
                String sql = "SELECT CarModelID, CarName, Image, Amount, SubTotal, PickUpDate, DropOffDate, Days, IsRenting  "
                        + "FROM [OrderDetail] "
                        + "WHERE OrderId = ?";
                stm = con.prepareStatement(sql);
                stm.setString(1, orderId);

                rs = stm.executeQuery();

                int carModelId, amount, days;
                String carName, imageLink;
                BigDecimal subTotal;
                Date pickUpDate, dropOffDate;
                boolean renting;
                
                while (rs.next()) {
                    carModelId = rs.getInt("CarModelID");
                    carName = rs.getString("CarName");
                    imageLink = rs.getString("Image");
                    amount = rs.getInt("Amount");
                    subTotal = rs.getBigDecimal("SubTotal");
                    pickUpDate = rs.getTimestamp("PickUpDate");
                    dropOffDate = rs.getTimestamp("DropOffDate");
                    days = rs.getInt("Days");
                    renting = rs.getBoolean("IsRenting");

                    if (this.listHistoryOrderDetals == null) {
                        this.listHistoryOrderDetals = new ArrayList<>();
                    }

                    OrderDetailDTO dto = new OrderDetailDTO(orderId, carModelId, carName, imageLink, amount, subTotal, pickUpDate, dropOffDate, days);
                    dto.setRenting(renting);
                    this.listHistoryOrderDetals.add(dto);
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
    
    public boolean checkBeforePickUpDate(String orderId) throws SQLException, NamingException {
        Connection con = null;
        PreparedStatement stm = null;
        ResultSet rs = null;
        
        try {
            con = MyConnection.makeConnection();
            if (con != null) {
                String sql = "SELECT CarModelID  "
                        + "FROM [OrderDetail] "
                        + "WHERE OrderID = ? AND PickUpDate > GETDATE() AND IsRenting = ?";
                stm = con.prepareStatement(sql);
                stm.setString(1, orderId);
                stm.setBoolean(2, true);

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
}
