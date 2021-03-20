/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trongns.carfeature;

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
public class CarFeatureDAO implements Serializable{
    private ArrayList<Integer> carFeatureList = null;

    /**
     * @return the carFeatureList
     */
    public ArrayList<Integer> getCarFeatureList() {
        return carFeatureList;
    }
    
    public void getCarFeatureListByCarModelId(int carModelId) throws NamingException, SQLException {
        Connection con = null;
        PreparedStatement stm = null;
        ResultSet rs = null;

        try {
            con = MyConnection.makeConnection();
            if (con != null) {
                String sql = "SELECT FeatureID "
                        + "FROM [CarFeature] "
                        + "WHERE CarModelID = ?";
                stm = con.prepareStatement(sql);
                stm.setInt(1, carModelId);
               
                int featureId;
                
                rs = stm.executeQuery();
                while (rs.next()) {
                    featureId = rs.getInt("FeatureID");
                    
                    if (this.carFeatureList == null) {
                        this.carFeatureList = new ArrayList<>();
                    }
                    
                    this.carFeatureList.add(featureId);
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
