/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trongns.ordercardetail;

import java.io.Serializable;
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
public class OrderCarDetailDAO implements Serializable{
     public boolean insertOrderCarDetail(OrderCarDetailDTO dto) throws NamingException, SQLException {
        Connection con = null;
        PreparedStatement stm = null;

        try {
            con = MyConnection.makeConnection();
            if (con != null) {
                String sql = "INSERT INTO OrderCarDetail (OrderID, CarModelID, LicensePlates) "
                        + "VALUES (?,?,?)";
                stm = con.prepareStatement(sql);
                stm.setString(1, dto.getOrderId());
                stm.setInt(2, dto.getCarModelId());
                stm.setString(3, dto.getLicensePlates());

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
     
     private ArrayList<String> listLiciensePlates = null;

    /**
     * @return the listLiciensePlates
     */
    public ArrayList<String> getListLiciensePlates() {
        return listLiciensePlates;
    }
     
     public void loadLiciensePlatesList(String orderId, int carModelId) throws NamingException, SQLException {
        Connection con = null;
        PreparedStatement stm = null;
        ResultSet rs = null;

        try {
            con = MyConnection.makeConnection();
            if (con != null) {
                String sql = "SELECT LicensePlates "
                        + "FROM [OrderCarDetail] "
                        + "WHERE OrderID = ? AND CarModelID = ?";
                stm = con.prepareStatement(sql);
                stm.setString(1, orderId);
                stm.setInt(2, carModelId);

                rs = stm.executeQuery();

                while (rs.next()) {
                    if (this.listLiciensePlates == null) {
                        this.listLiciensePlates = new ArrayList<>();
                    }
                    
                    this.listLiciensePlates.add(rs.getString("LicensePlates"));
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
