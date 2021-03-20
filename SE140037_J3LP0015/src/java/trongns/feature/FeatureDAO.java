/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trongns.feature;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.naming.NamingException;
import trongns.util.MyConnection;

/**
 *
 * @author TrongNS
 */
public class FeatureDAO implements Serializable {

    private FeatureDTO feature;

    /**
     * @return the feature
     */
    public FeatureDTO getFeature() {
        return feature;
    }

    public void getFeatureById(int featureId) throws NamingException, SQLException {
        Connection con = null;
        PreparedStatement stm = null;
        ResultSet rs = null;

        try {
            con = MyConnection.makeConnection();
            if (con != null) {
                String sql = "SELECT FeatureName "
                        + "FROM [Feature] "
                        + "WHERE FeatureID = ?";
                stm = con.prepareStatement(sql);
                stm.setInt(1, featureId);
               
                String featureName;
                
                rs = stm.executeQuery();
                if (rs.next()) {
                    featureName = rs.getString("FeatureName");
                    this.feature = new FeatureDTO(featureId, featureName);
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
