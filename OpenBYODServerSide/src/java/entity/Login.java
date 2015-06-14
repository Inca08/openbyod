/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import jdbc.ConnectionFactory;

/**
 *
 * @author Castro
 */
public class Login {

    private String userid;
    private String password;

    /**
     * Get the value of password
     *
     * @return the value of password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Set the value of password
     *
     * @param password new value of password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Get the value of userid
     *
     * @return the value of userid
     */
    public String getUserid() {
        return userid;
    }

    /**
     * Set the value of userid
     *
     * @param userid new value of userid
     */
    public void setUserid(String userid) {
        this.userid = userid;
    }

    public int authenticateUser() throws SQLException {
        
        int idemployee = 0;
        
        try {
            Connection con = new ConnectionFactory().getConnectionOpenBYODDB();
            
            String sql = "SELECT * FROM openbyodserver.employee emp where emp.name = ? and emp.password = ?";
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setString(1, this.getUserid());
            stmt.setString(2, this.getPassword());
            ResultSet rs = stmt.executeQuery();
            
            //if it finds the user in the database, the result set is fullfilled then the user is authenticated
            if(rs.next()){
                idemployee = rs.getInt("idemployee");
            }
            
            rs.close();
            stmt.close();
            con.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        
        //return the id(it is working as a token) to the service
        return idemployee;
    }

}
