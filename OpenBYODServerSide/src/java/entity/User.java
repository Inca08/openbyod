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
public class User {
    
    private String user_email;

    private String user_email_pass;

    /**
     * Get the value of user_email_pass
     *
     * @return the value of user_email_pass
     */
    public String getUser_email_pass() {
        return user_email_pass;
    }

    /**
     * Set the value of user_email_pass
     *
     * @param user_email_pass new value of user_email_pass
     */
    public void setUser_email_pass(String user_email_pass) {
        this.user_email_pass = user_email_pass;
    }

    /**
     * Get the value of user_email
     *
     * @return the value of user_email
     */
    public String getUser_email() {
        return user_email;
    }

    /**
     * Set the value of user_email
     *
     * @param user_email new value of user_email
     */
    public void setUser_email(String user_email) {
        this.user_email = user_email;
    }
    private int employee_id;

    /**
     * Get the value of user_id
     *
     * @return the value of user_id
     */
    public int getEmployee_id() {
        return employee_id;
    }

    /**
     * Set the value of user_id
     *
     * @param employee_id new value of user_id
     */
    public void setEmployee_id(int employee_id) {
        this.employee_id = employee_id;
    }

    
    /**
     * Search information about the user, with his ID.
     * 
     */
    public User getUserInfo(int employee_id){
        User return_user = new User();
        
        try {
            Connection con = new ConnectionFactory().getConnectionOpenBYODDB();

            String sql = "select employee.email, employee.email_password "
                    + "FROM openbyodserver.employee employee where employee.idemployee = ?";
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setInt(1, employee_id);
            ResultSet rs = stmt.executeQuery();

            //if it finds the user in the database, the result set is fullfilled then the user is authenticated
            while (rs.next()) {
                //Creates an object with the row from result set
                return_user.setEmployee_id(employee_id);
                return_user.setUser_email(rs.getString("email"));
                return_user.setUser_email_pass(rs.getString("email_password"));
            }

            rs.close();
            stmt.close();
            con.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        
        return return_user;
    } 
}
