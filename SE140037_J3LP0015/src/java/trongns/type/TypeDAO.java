/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trongns.type;

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
public class TypeDAO implements Serializable{
    private ArrayList<TypeDTO> typeList = null;

    /**
     * @return the typeList
     */
    public ArrayList<TypeDTO> getTypeList() {
        return typeList;
    }
    
    public void loadTypeList() throws NamingException, SQLException {
        Connection con = null;
        PreparedStatement stm = null;
        ResultSet rs = null;

        try {
            con = MyConnection.makeConnection();
            if (con != null) {
                String sql = "SELECT TypeID, TypeName, Seat, Image "
                        + "FROM [Type] ";
                stm = con.prepareStatement(sql);
                rs = stm.executeQuery();
                
                int typeId, seat;
                String typeName, imageLink;

                while (rs.next()) {
                    typeId = rs.getInt("TypeID");
                    typeName = rs.getString("TypeName");
                    seat = rs.getInt("Seat");
                    imageLink = rs.getString("Image");

                    if (this.typeList == null) {
                        this.typeList = new ArrayList<>();
                    }

                    TypeDTO dto = new TypeDTO(typeId, typeName, seat, imageLink);
                    this.typeList.add(dto);
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
    
    private String typeName = null;

    /**
     * @return the typeName
     */
    public String getTypeName() {
        return typeName;
    }
    
    public void getTypeNameById(int id) throws NamingException, SQLException {
        Connection con = null;
        PreparedStatement stm = null;
        ResultSet rs = null;

        try {
            con = MyConnection.makeConnection();
            if (con != null) {
                String sql = "SELECT TypeName "
                        + "FROM [Type] "
                        + "WHERE TypeID = ?";
                stm = con.prepareStatement(sql);
                stm.setInt(1, id);
                
                rs = stm.executeQuery();

                if (rs.next()) {
                    this.typeName = rs.getString("TypeName");
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
