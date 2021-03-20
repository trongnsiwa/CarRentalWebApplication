/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trongns.car;

import java.io.Serializable;
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
public class CarDAO implements Serializable {

    private ArrayList<CarDTO> carList = null;

    /**
     * @return the carList
     */
    public ArrayList<CarDTO> getCarList() {
        return carList;
    }

    public void loadCarByCarModelId(int carModelId, int amount, Date rentalDate, Date returnDate) throws NamingException, SQLException {
        this.carList = null;
        Connection con = null;
        PreparedStatement stm = null;
        ResultSet rs = null;

        try {
            con = MyConnection.makeConnection();
            if (con != null) {
                String sql = "SELECT TOP (?) LicensePlates "
                        + "FROM Car "
                        + "WHERE CarModelID = ? ";
                if (rentalDate != null && returnDate != null) {
                    sql += "AND LicensePlates NOT IN ("
                            + "SELECT LicensePlates "
                            + "FROM OrderCarDetail "
                            + "WHERE OrderId IN ("
                            + "SELECT OrderId "
                            + "FROM OrderDetail "
                            + "WHERE ((PickUpDate BETWEEN ? AND ?) "
                            + "OR (DropOffDate BETWEEN ? AND ?) "
                            + "OR (PickUpDate <= ? AND DropOffDate >= ?)) "
                            + "AND IsRenting = ? "
                            + "AND CarModelID = ?)"
                            + ") ";
                }
                stm = con.prepareStatement(sql);
                stm.setInt(1, amount);
                stm.setInt(2, carModelId);

                if (rentalDate != null && returnDate != null) {
                    stm.setTimestamp(3, new Timestamp(rentalDate.getTime()));
                    stm.setTimestamp(4, new Timestamp(returnDate.getTime()));
                    stm.setTimestamp(5, new Timestamp(rentalDate.getTime()));
                    stm.setTimestamp(6, new Timestamp(returnDate.getTime()));
                    stm.setTimestamp(7, new Timestamp(rentalDate.getTime()));
                    stm.setTimestamp(8, new Timestamp(returnDate.getTime()));
                    stm.setBoolean(9, true);
                    stm.setInt(10, carModelId);
                }

                rs = stm.executeQuery();

                String licensePlates;

                while (rs.next()) {
                    licensePlates = rs.getString("LicensePlates");

                    if (this.carList == null) {
                        this.carList = new ArrayList<>();
                    }

                    CarDTO dto = new CarDTO(carModelId, licensePlates);
                    this.carList.add(dto);
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
