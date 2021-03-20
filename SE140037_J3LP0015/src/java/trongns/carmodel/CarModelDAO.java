/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trongns.carmodel;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import javax.naming.NamingException;
import trongns.util.MyConnection;

/**
 *
 * @author TrongNS
 */
public class CarModelDAO implements Serializable {

    private int totalCarModel = 0;

    /**
     * @return the totalCarModel
     */
    public int getTotalCarModel() {
        return totalCarModel;
    }

    public void countAllCarModel(String role) throws NamingException, SQLException {
        Connection con = null;
        PreparedStatement stm = null;
        ResultSet rs = null;

        try {
            con = MyConnection.makeConnection();
            if (con != null) {
                String sql = "SELECT COUNT(CarModelID) AS total "
                        + "FROM [CarModel] ";
                if (!"Admin".equals(role)) {
                    sql += "WHERE Quantity > 0";
                }
                stm = con.prepareStatement(sql);

                rs = stm.executeQuery();
                if (rs.next()) {
                    this.totalCarModel = rs.getInt("total");
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

    private int totalSearchedCarModel = 0;

    /**
     * @return the totalSearchedCarModel
     */
    public int getTotalSearchedCarModel() {
        return totalSearchedCarModel;
    }

    public void countCarModelWithConditions(String keyword, String type, int amountOfCar, Date rentalDate, Date returnDate, String role) throws NamingException, SQLException {
        Connection con = null;
        PreparedStatement stm = null;
        ResultSet rs = null;

        try {
            con = MyConnection.makeConnection();
            if (con != null) {
                String sql = "SELECT COUNT(CarModelID) AS total "
                        + "FROM [CarModel] ";
                if (!"Admin".equals(role)) {
                    sql += "WHERE Quantity > 0 ";
                    if (keyword != null && !keyword.trim().isEmpty()) {
                        sql += "AND CarName LIKE ? ";
                        if (type != null && !type.trim().isEmpty()) {
                            sql += "OR TypeId = ? ";
                        }
                        if (amountOfCar != 0) {
                            sql += "AND Quantity >= ? ";
                        }
                    } else {
                        if (type != null && !type.trim().isEmpty()) {
                            sql += "AND TypeId = ? ";
                        }
                        if (amountOfCar != 0) {
                            sql += "AND Quantity >= ? ";
                        }
                    }
                } else {
                    if (keyword != null && !keyword.trim().isEmpty()) {
                        sql += "WHERE CarName LIKE ? ";
                        if (type != null && !type.trim().isEmpty()) {
                            sql += "OR TypeId = ? ";
                        }
                        if (amountOfCar != 0) {
                            sql += "AND Quantity >= ? ";
                        }
                    } else {
                        if (type != null && !type.trim().isEmpty()) {
                            sql += "WHERE TypeId = ? ";
                            if (amountOfCar != 0) {
                                sql += "AND Quantity >= ? ";
                            }
                        } else {
                            if (amountOfCar != 0) {
                                sql += "WHERE Quantity >= ? ";
                            }
                        }
                    }
                }

                if (rentalDate != null && returnDate != null) {
                    sql += "AND CarModelID NOT IN ("
                            + "SELECT cm.CarModelID "
                            + "FROM CarModel AS cm INNER JOIN OrderDetail AS od ON cm.CarModelID = od.CarModelID "
                            + "AND cm.Quantity <= ("
                            + "SELECT SUM(Amount) "
                            + "FROM OrderDetail "
                            + "WHERE ((PickUpDate BETWEEN ? AND ?) "
                            + "OR (DropOffDate BETWEEN ? AND ?) "
                            + "OR (PickUpDate <= ? AND DropOffDate >= ?)) "
                            + "AND IsRenting = ? "
                            + "AND CarModelID = cm.CarModelID) "
                            + ")";
                }

                stm = con.prepareStatement(sql);
                if (keyword != null && !keyword.trim().isEmpty()) {
                    stm.setString(1, "%" + keyword + "%");
                    if (type != null && !type.trim().isEmpty()) {
                        stm.setInt(2, Integer.parseInt(type));
                        if (amountOfCar != 0) {
                            stm.setInt(3, amountOfCar);
                            if (rentalDate != null && returnDate != null) {
                                stm.setTimestamp(4, new Timestamp(rentalDate.getTime()));
                                stm.setTimestamp(5, new Timestamp(returnDate.getTime()));
                                stm.setTimestamp(6, new Timestamp(rentalDate.getTime()));
                                stm.setTimestamp(7, new Timestamp(returnDate.getTime()));
                                stm.setTimestamp(8, new Timestamp(rentalDate.getTime()));
                                stm.setTimestamp(9, new Timestamp(returnDate.getTime()));
                                stm.setBoolean(10, true);
                            }
                        } else {
                            if (rentalDate != null && returnDate != null) {
                                stm.setTimestamp(3, new Timestamp(rentalDate.getTime()));
                                stm.setTimestamp(4, new Timestamp(returnDate.getTime()));
                                stm.setTimestamp(5, new Timestamp(rentalDate.getTime()));
                                stm.setTimestamp(6, new Timestamp(returnDate.getTime()));
                                stm.setTimestamp(7, new Timestamp(rentalDate.getTime()));
                                stm.setTimestamp(8, new Timestamp(returnDate.getTime()));
                                stm.setBoolean(9, true);
                            }
                        }
                    } else {
                        if (amountOfCar != 0) {
                            stm.setInt(2, amountOfCar);
                            if (rentalDate != null && returnDate != null) {
                                stm.setTimestamp(3, new Timestamp(rentalDate.getTime()));
                                stm.setTimestamp(4, new Timestamp(returnDate.getTime()));
                                stm.setTimestamp(5, new Timestamp(rentalDate.getTime()));
                                stm.setTimestamp(6, new Timestamp(returnDate.getTime()));
                                stm.setTimestamp(7, new Timestamp(rentalDate.getTime()));
                                stm.setTimestamp(8, new Timestamp(returnDate.getTime()));
                                stm.setBoolean(9, true);
                            }
                        } else {
                            if (rentalDate != null && returnDate != null) {
                                stm.setTimestamp(2, new Timestamp(rentalDate.getTime()));
                                stm.setTimestamp(3, new Timestamp(returnDate.getTime()));
                                stm.setTimestamp(4, new Timestamp(rentalDate.getTime()));
                                stm.setTimestamp(5, new Timestamp(returnDate.getTime()));
                                stm.setTimestamp(6, new Timestamp(rentalDate.getTime()));
                                stm.setTimestamp(7, new Timestamp(returnDate.getTime()));
                                stm.setBoolean(8, true);
                            }
                        }
                    }
                } else {
                    if (type != null && !type.trim().isEmpty()) {
                        stm.setInt(1, Integer.parseInt(type));
                        if (amountOfCar != 0) {
                            stm.setInt(2, amountOfCar);
                            if (rentalDate != null && returnDate != null) {
                                stm.setTimestamp(3, new Timestamp(rentalDate.getTime()));
                                stm.setTimestamp(4, new Timestamp(returnDate.getTime()));
                                stm.setTimestamp(5, new Timestamp(rentalDate.getTime()));
                                stm.setTimestamp(6, new Timestamp(returnDate.getTime()));
                                stm.setTimestamp(7, new Timestamp(rentalDate.getTime()));
                                stm.setTimestamp(8, new Timestamp(returnDate.getTime()));
                                stm.setBoolean(9, true);
                            }
                        } else {
                            if (rentalDate != null && returnDate != null) {
                                stm.setTimestamp(2, new Timestamp(rentalDate.getTime()));
                                stm.setTimestamp(3, new Timestamp(returnDate.getTime()));
                                stm.setTimestamp(4, new Timestamp(rentalDate.getTime()));
                                stm.setTimestamp(5, new Timestamp(returnDate.getTime()));
                                stm.setTimestamp(6, new Timestamp(rentalDate.getTime()));
                                stm.setTimestamp(7, new Timestamp(returnDate.getTime()));
                                stm.setBoolean(8, true);
                            }
                        }
                    } else {
                        if (amountOfCar != 0) {
                            stm.setInt(1, amountOfCar);
                            if (rentalDate != null && returnDate != null) {
                                stm.setTimestamp(2, new Timestamp(rentalDate.getTime()));
                                stm.setTimestamp(3, new Timestamp(returnDate.getTime()));
                                stm.setTimestamp(4, new Timestamp(rentalDate.getTime()));
                                stm.setTimestamp(5, new Timestamp(returnDate.getTime()));
                                stm.setTimestamp(6, new Timestamp(rentalDate.getTime()));
                                stm.setTimestamp(7, new Timestamp(returnDate.getTime()));
                                stm.setBoolean(8, true);
                            }
                        } else {
                            if (rentalDate != null && returnDate != null) {
                                stm.setTimestamp(1, new Timestamp(rentalDate.getTime()));
                                stm.setTimestamp(2, new Timestamp(returnDate.getTime()));
                                stm.setTimestamp(3, new Timestamp(rentalDate.getTime()));
                                stm.setTimestamp(4, new Timestamp(returnDate.getTime()));
                                stm.setTimestamp(5, new Timestamp(rentalDate.getTime()));
                                stm.setTimestamp(6, new Timestamp(returnDate.getTime()));
                                stm.setBoolean(7, true);
                            }
                        }
                    }
                }

                rs = stm.executeQuery();

                if (rs.next()) {
                    this.totalSearchedCarModel = rs.getInt("total");
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

    public boolean checkNotAvailableCarBetweenDates(int carModelId, Date userRentalDate, Date userReturnDate) throws NamingException, SQLException {
        Connection con = null;
        PreparedStatement stm = null;
        ResultSet rs = null;

        try {
            con = MyConnection.makeConnection();
            if (con != null) {
                String sql = "SELECT cm.Quantity "
                        + "FROM CarModel AS cm INNER JOIN OrderDetail AS od ON cm.CarModelID = od.CarModelID "
                        + "AND cm.CarModelID = ? AND cm.Quantity <= ("
                        + "SELECT SUM(Amount) "
                        + "FROM OrderDetail "
                        + "WHERE ((PickUpDate BETWEEN ? AND ?) "
                        + "OR (DropOffDate BETWEEN ? AND ?) "
                        + "OR (PickUpDate <= ? AND DropOffDate >= ?)) "
                        + "AND IsRenting = ? "
                        + "AND CarModelID = cm.CarModelID) ";
                stm = con.prepareStatement(sql);
                stm.setInt(1, carModelId);
                stm.setTimestamp(2, new Timestamp(userRentalDate.getTime()));
                stm.setTimestamp(3, new Timestamp(userReturnDate.getTime()));
                stm.setTimestamp(4, new Timestamp(userRentalDate.getTime()));
                stm.setTimestamp(5, new Timestamp(userReturnDate.getTime()));
                stm.setTimestamp(6, new Timestamp(userRentalDate.getTime()));
                stm.setTimestamp(7, new Timestamp(userReturnDate.getTime()));
                stm.setBoolean(8, true);

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

    private CarModelDTO carModelDetail = null;

    /**
     * @return the carModelDetail
     */
    public CarModelDTO getCarModelDetail() {
        return carModelDetail;
    }

    public void loadCarModelDetail(int id, String role) throws NamingException, SQLException {
        Connection con = null;
        PreparedStatement stm = null;
        ResultSet rs = null;

        try {
            con = MyConnection.makeConnection();
            if (con != null) {
                String sql = "SELECT CarName, SmallImage, BigImage, Color, Seat, "
                        + "Fuel, TypeID, Quantity, Year, Price, "
                        + "Address, Description "
                        + "FROM [CarModel] "
                        + "WHERE CarModelID = ? ";
                if (!"Admin".equals(role)) {
                    sql += " AND Quantity > 0";
                }
                stm = con.prepareStatement(sql);
                stm.setInt(1, id);

                rs = stm.executeQuery();

                int seat, typeId, quantity, year;
                String carName, smallImageLink, bigImageLink, color, fuel, address, description;
                BigDecimal price;

                if (rs.next()) {
                    carName = rs.getString("CarName");
                    smallImageLink = rs.getString("SmallImage");
                    bigImageLink = rs.getString("BigImage");
                    color = rs.getString("Color");
                    seat = rs.getInt("Seat");
                    fuel = rs.getString("Fuel");
                    typeId = rs.getInt("TypeId");
                    quantity = rs.getInt("Quantity");
                    year = rs.getInt("Year");
                    price = rs.getBigDecimal("Price");
                    address = rs.getString("Address");
                    description = rs.getString("Description");

                    CarModelDTO dto = new CarModelDTO(id, carName, smallImageLink, bigImageLink, color, seat, fuel, typeId, quantity, year, price, address, description);
                    this.carModelDetail = dto;
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

    private ArrayList<CarModelDTO> listSearchedCarModel = null;

    /**
     * @return the listSearchedCarModel
     */
    public ArrayList<CarModelDTO> getListSearchedCarModel() {
        return listSearchedCarModel;
    }

    public void searchCarModelWithConditions(String keyword, String type, int amountOfCar, Date rentalDate, Date returnDate, String role, int pageSize, int pageNo) throws SQLException, NamingException, ParseException {
        Connection con = null;
        PreparedStatement stm = null;
        ResultSet rs = null;
        int offset = pageSize * (pageNo - 1);

        try {
            con = MyConnection.makeConnection();
            if (con != null) {
                String sql = "SELECT CarModelID, CarName, SmallImage, BigImage, Color, Seat, "
                        + "Fuel, TypeID, Quantity, Year, Price, "
                        + "Address, Description "
                        + "FROM [CarModel] ";
                if (!"Admin".equals(role)) {
                    sql += "WHERE Quantity > 0 ";
                    if (keyword != null && !keyword.trim().isEmpty()) {
                        sql += "AND CarName LIKE ? ";
                        if (type != null && !type.trim().isEmpty()) {
                            sql += "OR TypeId = ? ";
                        }
                        if (amountOfCar != 0) {
                            sql += "AND Quantity >= ? ";
                        }
                    } else {
                        if (type != null && !type.trim().isEmpty()) {
                            sql += "AND TypeId = ? ";
                        }
                        if (amountOfCar != 0) {
                            sql += "AND Quantity >= ? ";
                        }
                    }
                } else {
                    if (keyword != null && !keyword.trim().isEmpty()) {
                        sql += "WHERE CarName LIKE ? ";
                        if (type != null && !type.trim().isEmpty()) {
                            sql += "OR TypeId = ? ";
                        }
                        if (amountOfCar != 0) {
                            sql += "AND Quantity >= ? ";
                        }
                    } else {
                        if (type != null && !type.trim().isEmpty()) {
                            sql += "WHERE TypeId = ? ";
                            if (amountOfCar != 0) {
                                sql += "AND Quantity >= ? ";
                            }
                        } else {
                            if (amountOfCar != 0) {
                                sql += "WHERE Quantity >= ? ";
                            }
                        }
                    }
                }

                if (rentalDate != null && returnDate != null) {
                    sql += "AND CarModelID NOT IN ("
                            + "SELECT cm.CarModelID "
                            + "FROM CarModel AS cm INNER JOIN OrderDetail AS od ON cm.CarModelID = od.CarModelID "
                            + "AND cm.Quantity <= ("
                            + "SELECT SUM(Amount) "
                            + "FROM OrderDetail "
                            + "WHERE ((PickUpDate BETWEEN ? AND ?) "
                            + "OR (DropOffDate BETWEEN ? AND ?) "
                            + "OR (PickUpDate <= ? AND DropOffDate >= ?)) "
                            + "AND IsRenting = ? "
                            + "AND CarModelID = cm.CarModelID) "
                            + ")";
                }

                sql += "ORDER BY CreatedDate DESC "
                        + "OFFSET " + offset + " ROWS "
                        + "FETCH NEXT " + pageSize + " ROWS ONLY";

                stm = con.prepareStatement(sql);
                if (keyword != null && !keyword.trim().isEmpty()) {
                    stm.setString(1, "%" + keyword + "%");
                    if (type != null && !type.trim().isEmpty()) {
                        stm.setInt(2, Integer.parseInt(type));
                        if (amountOfCar != 0) {
                            stm.setInt(3, amountOfCar);
                            if (rentalDate != null && returnDate != null) {
                                stm.setTimestamp(4, new Timestamp(rentalDate.getTime()));
                                stm.setTimestamp(5, new Timestamp(returnDate.getTime()));
                                stm.setTimestamp(6, new Timestamp(rentalDate.getTime()));
                                stm.setTimestamp(7, new Timestamp(returnDate.getTime()));
                                stm.setTimestamp(8, new Timestamp(rentalDate.getTime()));
                                stm.setTimestamp(9, new Timestamp(returnDate.getTime()));
                                stm.setBoolean(10, true);
                            }
                        } else {
                            if (rentalDate != null && returnDate != null) {
                                stm.setTimestamp(3, new Timestamp(rentalDate.getTime()));
                                stm.setTimestamp(4, new Timestamp(returnDate.getTime()));
                                stm.setTimestamp(5, new Timestamp(rentalDate.getTime()));
                                stm.setTimestamp(6, new Timestamp(returnDate.getTime()));
                                stm.setTimestamp(7, new Timestamp(rentalDate.getTime()));
                                stm.setTimestamp(8, new Timestamp(returnDate.getTime()));
                                stm.setBoolean(9, true);
                            }
                        }
                    } else {
                        if (amountOfCar != 0) {
                            stm.setInt(2, amountOfCar);
                            if (rentalDate != null && returnDate != null) {
                                stm.setTimestamp(3, new Timestamp(rentalDate.getTime()));
                                stm.setTimestamp(4, new Timestamp(returnDate.getTime()));
                                stm.setTimestamp(5, new Timestamp(rentalDate.getTime()));
                                stm.setTimestamp(6, new Timestamp(returnDate.getTime()));
                                stm.setTimestamp(7, new Timestamp(rentalDate.getTime()));
                                stm.setTimestamp(8, new Timestamp(returnDate.getTime()));
                                stm.setBoolean(9, true);
                            }
                        } else {
                            if (rentalDate != null && returnDate != null) {
                                stm.setTimestamp(2, new Timestamp(rentalDate.getTime()));
                                stm.setTimestamp(3, new Timestamp(returnDate.getTime()));
                                stm.setTimestamp(4, new Timestamp(rentalDate.getTime()));
                                stm.setTimestamp(5, new Timestamp(returnDate.getTime()));
                                stm.setTimestamp(6, new Timestamp(rentalDate.getTime()));
                                stm.setTimestamp(7, new Timestamp(returnDate.getTime()));
                                stm.setBoolean(8, true);
                            }
                        }
                    }
                } else {
                    if (type != null && !type.trim().isEmpty()) {
                        stm.setInt(1, Integer.parseInt(type));
                        if (amountOfCar != 0) {
                            stm.setInt(2, amountOfCar);
                            if (rentalDate != null && returnDate != null) {
                                stm.setTimestamp(3, new Timestamp(rentalDate.getTime()));
                                stm.setTimestamp(4, new Timestamp(returnDate.getTime()));
                                stm.setTimestamp(5, new Timestamp(rentalDate.getTime()));
                                stm.setTimestamp(6, new Timestamp(returnDate.getTime()));
                                stm.setTimestamp(7, new Timestamp(rentalDate.getTime()));
                                stm.setTimestamp(8, new Timestamp(returnDate.getTime()));
                                stm.setBoolean(9, true);
                            }
                        } else {
                            if (rentalDate != null && returnDate != null) {
                                stm.setTimestamp(2, new Timestamp(rentalDate.getTime()));
                                stm.setTimestamp(3, new Timestamp(returnDate.getTime()));
                                stm.setTimestamp(4, new Timestamp(rentalDate.getTime()));
                                stm.setTimestamp(5, new Timestamp(returnDate.getTime()));
                                stm.setTimestamp(6, new Timestamp(rentalDate.getTime()));
                                stm.setTimestamp(7, new Timestamp(returnDate.getTime()));
                                stm.setBoolean(8, true);
                            }
                        }
                    } else {
                        if (amountOfCar != 0) {
                            stm.setInt(1, amountOfCar);
                            if (rentalDate != null && returnDate != null) {
                                stm.setTimestamp(2, new Timestamp(rentalDate.getTime()));
                                stm.setTimestamp(3, new Timestamp(returnDate.getTime()));
                                stm.setTimestamp(4, new Timestamp(rentalDate.getTime()));
                                stm.setTimestamp(5, new Timestamp(returnDate.getTime()));
                                stm.setTimestamp(6, new Timestamp(rentalDate.getTime()));
                                stm.setTimestamp(7, new Timestamp(returnDate.getTime()));
                                stm.setBoolean(8, true);
                            }
                        } else {
                            if (rentalDate != null && returnDate != null) {
                                stm.setTimestamp(1, new Timestamp(rentalDate.getTime()));
                                stm.setTimestamp(2, new Timestamp(returnDate.getTime()));
                                stm.setTimestamp(3, new Timestamp(rentalDate.getTime()));
                                stm.setTimestamp(4, new Timestamp(returnDate.getTime()));
                                stm.setTimestamp(5, new Timestamp(rentalDate.getTime()));
                                stm.setTimestamp(6, new Timestamp(returnDate.getTime()));
                                stm.setBoolean(7, true);
                            }
                        }
                    }
                }

                rs = stm.executeQuery();

                int carModelId, seat, typeId, quantity, year;
                String carName, smallImageLink, bigImageLink, color, fuel, address, description;
                BigDecimal price;

                while (rs.next()) {
                    carModelId = rs.getInt("CarModelId");
                    carName = rs.getString("CarName");
                    smallImageLink = rs.getString("SmallImage");
                    bigImageLink = rs.getString("BigImage");
                    color = rs.getString("Color");
                    seat = rs.getInt("Seat");
                    fuel = rs.getString("Fuel");
                    typeId = rs.getInt("TypeId");
                    quantity = rs.getInt("Quantity");
                    year = rs.getInt("Year");
                    price = rs.getBigDecimal("Price");
                    address = rs.getString("Address");
                    description = rs.getString("Description");

                    if (this.listSearchedCarModel == null) {
                        this.listSearchedCarModel = new ArrayList<>();
                    }

                    CarModelDTO dto = new CarModelDTO(carModelId, carName, smallImageLink, bigImageLink, color, seat, fuel, typeId, quantity, year, price, address, description);
                    this.listSearchedCarModel.add(dto);
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

    private int quantity = 0;

    /**
     * @return the quantity
     */
    public int getQuantity() {
        return quantity;
    }

    public void getQuantityOfCarModelById(int carModelID) throws NamingException, SQLException {
        Connection con = null;
        PreparedStatement stm = null;
        ResultSet rs = null;

        try {
            con = MyConnection.makeConnection();
            if (con != null) {
                String sql = "SELECT Quantity "
                        + "FROM [CarModel] "
                        + "WHERE CarModelID = ?";

                stm = con.prepareStatement(sql);
                stm.setInt(1, carModelID);

                rs = stm.executeQuery();

                if (rs.next()) {
                    this.quantity = rs.getInt("Quantity");
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
