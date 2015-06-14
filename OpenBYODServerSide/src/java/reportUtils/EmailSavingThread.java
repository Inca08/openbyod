/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package reportUtils;

import com.mysql.jdbc.exceptions.MySQLIntegrityConstraintViolationException;
import entity.EmailEntity;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.List;
import jdbc.ConnectionFactory;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author Castro
 */
public class EmailSavingThread implements Runnable {

    private List<EmailEntity> emailToSaveList;

    public EmailSavingThread(List<EmailEntity> emailToSaveList) {
        this.emailToSaveList = emailToSaveList;
    }

    @Override
    public void run() {
        //Runs the list to search if the email is already in the database
        for (EmailEntity email : emailToSaveList) {
            this.insert(email);
        }
    }

    private void insert(EmailEntity email) {

        try {
            Connection con = new ConnectionFactory().getConnectionOpenBYODDB();

            //prepares the SQL to be executed
            String sql = "INSERT INTO openbyodserver.emailbackup "
                    + " (sent_date,message_id,email_subject,email_body,email_to,email_from) "
                    + " VALUES (now(),?,?,?,?,?)";

            PreparedStatement stmt = con.prepareStatement(sql);

            //prepare the params
            stmt.setString(1, email.getMessage_id());
            stmt.setString(2, email.getSubject());
            stmt.setString(3, email.getBody());
            stmt.setString(4, email.getTo());
            stmt.setString(5, email.getFrom());
            
            System.out.println("sql -> " + stmt.toString());

            //executes the SQL and closes the connection and the prepared statement
            stmt.execute();
            stmt.close();
            con.close();
        } catch (MySQLIntegrityConstraintViolationException mysqlExeception){
            System.out.println("Tryed to insert the same email.");
        } catch (Exception e) {
            System.out.println("Problems to save the emails.");
            e.printStackTrace();
        }
    }

}
