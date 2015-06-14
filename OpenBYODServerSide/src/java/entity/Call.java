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
import java.util.ArrayList;
import java.util.List;
import jdbc.ConnectionFactory;
import reportUtils.ReportVO;

/**
 *
 * @author Castro
 */
public class Call {
    
    private int employee_idemployee;

    /**
     * Get the value of employee_idemployee
     *
     * @return the value of employee_idemployee
     */
    public int getEmployee_idemployee() {
        return employee_idemployee;
    }

    /**
     * Set the value of employee_idemployee
     *
     * @param employee_idemployee new value of employee_idemployee
     */
    public void setEmployee_idemployee(int employee_idemployee) {
        this.employee_idemployee = employee_idemployee;
    }

    private int contact_idcontact;

    /**
     * Get the value of contact_idcontact
     *
     * @return the value of contact_idcontact
     */
    public int getContact_idcontact() {
        return contact_idcontact;
    }

    /**
     * Set the value of contact_idcontact
     *
     * @param contact_idcontact new value of contact_idcontact
     */
    public void setContact_idcontact(int contact_idcontact) {
        this.contact_idcontact = contact_idcontact;
    }

    /**
     * Adds the call log into the table Call on the Database
     *
     * return TRUE if the call was saved successfully and FALSE if there was an
     * error
     */
    public boolean addCallLog() {

        boolean saved = false;

        try {
            Connection con = new ConnectionFactory().getConnectionOpenBYODDB();
            
            //prepares the SQL to be executed
            String sql = "INSERT INTO openbyodserver.call"
                    + "(time,employee_idemployee,contact_idcontact)"
                    + "VALUES (now(),?,?)";
            
            PreparedStatement stmt = con.prepareStatement(sql);
            
            //prepare the params
            stmt.setInt(1, this.getEmployee_idemployee());
            stmt.setInt(2, this.getContact_idcontact());

            //executes the SQL and closes the connection and the prepared statement
            stmt.execute();
            stmt.close();
            con.close();
            
            //turns 'saved' to TRUE as it worked
            saved = true;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return saved;
    }
    
    /**
     * Return a list to be shown on the reports page
     */
    
    public static List<ReportVO> getReportList(int month){
        List<ReportVO> reportList = new ArrayList<ReportVO>();

        try {
            Connection con = new ConnectionFactory().getConnectionOpenBYODDB();

            String sql =    "SELECT emp.name Nome, count(*) Quantidade" +
                            " FROM openbyodserver.call c, openbyodserver.employee emp" +
                            " where emp.idemployee = c.employee_idemployee and MONTH(c.time) = ?" +
                            " group by emp.name" +
                            " order by 2 desc";
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setInt(1, month); 
            ResultSet rs = stmt.executeQuery();

            //if it finds the user in the database, the result set is fullfilled then the user is authenticated
            while (rs.next()) {
                //Creates an object with the row from result set
                ReportVO resultSetRow = new ReportVO(rs.getInt("Quantidade"), rs.getString("Nome"));
                //adds the object to the list
                reportList.add(resultSetRow);
            }

            rs.close();
            stmt.close();
            con.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return reportList;
    }
}
