/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trongns.discount;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import javax.naming.NamingException;
import trongns.util.MyConnection;

/**
 *
 * @author TrongNS
 */
public class DiscountDAO implements Serializable{
    private DiscountDTO discount = null;
    
    /**
     * @return the discount
     */
    public DiscountDTO getDiscount() {
        return discount;
    }
    
    public void checkAvailableDiscount(String code) throws ParseException, NamingException, SQLException {
        Connection con = null;
        PreparedStatement stm = null;
        ResultSet rs = null;
        
        String sql = "SELECT Code, [Percent] "
                + "FROM [Discount] "
                + "WHERE Code = ? AND CONVERT(DATE, ApplyDate) <= CONVERT(DATE, GETDATE()) AND CONVERT(DATE, ExpiryDate) >= CONVERT(DATE, GETDATE())";
        try {
            con = MyConnection.makeConnection();
            stm = con.prepareStatement(sql);
            stm.setString(1, code);
            rs = stm.executeQuery();
            
            int percent;
            
            if (rs.next()) {
                percent = rs.getInt("Percent");
                
                this.discount = new DiscountDTO(code, percent);
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
