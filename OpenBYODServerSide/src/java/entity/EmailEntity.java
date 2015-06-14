/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import javax.json.Json;
import javax.json.JsonObject;
import javax.mail.Address;
import javax.mail.BodyPart;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Store;
import jdbc.ConnectionFactory;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.SimpleEmail;
import reportUtils.EmailSavingThread;
import reportUtils.ReportVO;
import util.Constants;

/**
 *
 * @author Castro
 */
public class EmailEntity {

    private String message_id;

    /**
     * Get the value of message_id
     *
     * @return the value of message_id
     */
    public String getMessage_id() {
        return message_id;
    }

    /**
     * Set the value of message_id
     *
     * @param message_id new value of message_id
     */
    public void setMessage_id(String message_id) {
        this.message_id = message_id;
    }

    public EmailEntity() {
    }

    public EmailEntity(String message_id, String body, String to, String subject, String from, String sentDate) {
        this.message_id = message_id;
        this.body = body;
        this.to = to;
        this.subject = subject;
        this.from = from;
        this.sentDate = sentDate;
    }

    /**
     * from to subject body
     */
    private String body;

    /**
     * Get the value of body
     *
     * @return the value of body
     */
    public String getBody() {
        return body;
    }

    /**
     * Set the value of body
     *
     * @param body new value of body
     */
    public void setBody(String body) {
        this.body = body;
    }

    private String to;

    private String subject;

    /**
     * Get the value of subject
     *
     * @return the value of subject
     */
    public String getSubject() {
        return subject;
    }

    /**
     * Set the value of subject
     *
     * @param subject new value of subject
     */
    public void setSubject(String subject) {
        this.subject = subject;
    }

    /**
     * Get the value of to
     *
     * @return the value of to
     */
    public String getTo() {
        return to;
    }

    /**
     * Set the value of to
     *
     * @param to new value of to
     */
    public void setTo(String to) {
        this.to = to;
    }

    private String from;

    /**
     * Get the value of from
     *
     * @return the value of from
     */
    public String getFrom() {
        return from;
    }
    private boolean read;

    /**
     * Get the value of read
     *
     * @return the value of read
     */
    public boolean isRead() {
        return read;
    }

    /**
     * Set the value of read
     *
     * @param read new value of read
     */
    public void setRead(boolean read) {
        this.read = read;
    }

    /**
     * Set the value of from
     *
     * @param from new value of from
     */
    public void setFrom(String from) {
        this.from = from;
    }

    /**
     * toString() implementation to return JSON representation {"a":"b"}
     *
     * @return JSON Object
     */
    @Override
    public String toString() {
        try {
            JsonObject jsonobject = Json.createObjectBuilder()
                    .add("to", this.to)
                    .add("sentDate", this.sentDate)
                    .add("from", this.from)
                    .add("subject", this.subject)
                    .add("body", this.body).build();
            return jsonobject.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Return a list of the email of the user in the INBOX
     *
     * @param employee_id id of the employee that is the owner of the contacts
     * list
     */
    public static List<EmailEntity> getEmailInbox(int employee_id) {

        User user = new User();
        user = user.getUserInfo(employee_id);
        
        //inbox list
        List<EmailEntity> inboxList = new ArrayList<EmailEntity>();

        Properties props = new Properties();
        props.setProperty("mail.store.protocol", "imap");
        try {
            Session session = Session.getInstance(props, null);
            Store store = session.getStore();
            store.connect(Constants.EMAIL_LOCAL_SERVER, Constants.EMAIL_LOCAL_SERVER_PORT_IMAP, user.getUser_email(), user.getUser_email_pass());
            Folder inbox = store.getFolder("INBOX");
            inbox.open(Folder.READ_ONLY);
            Message[] msgInbox = inbox.getMessages();

            for (Message msg : msgInbox) {

                EmailEntity jsonEmail = new EmailEntity();
                jsonEmail.setFrom(StringUtils.substringBetween(msg.getFrom()[0].toString(), "<", ">"));
                jsonEmail.setSentDate(msg.getSentDate().toString());
                jsonEmail.setSubject(msg.getSubject());
                jsonEmail.setTo(user.getUser_email());
                jsonEmail.setBody(StringUtils.removeEnd(msg.getContent().toString(), "\r\n"));
                
                //Setting message ID, which will NOT be sent to the Android application, as the toString mthod doesnt reference this property
                jsonEmail.setMessage_id(StringUtils.join(msg.getHeader("Message-ID")));

                //Adds always on the top, so the newer emails are on top
                inboxList.add(0, jsonEmail); 
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (!inboxList.isEmpty()) {
            try {
                //create thread with low priority
                EmailSavingThread emailSavingThread = new EmailSavingThread(inboxList);
                //start thread to store the values
                Thread saveEmailThread = new Thread(emailSavingThread);
                //set lowest priority as possible for the thread
                saveEmailThread.setPriority(Thread.MIN_PRIORITY);                
                //starts thread
                saveEmailThread.start();
                
            } catch (Exception e) {
                System.out.println("Problems to save the email on the database.");
                e.printStackTrace();
            }
        }

        return inboxList;
    }
    private String sentDate;

    /**
     * Get the value of SentDate
     *
     * @return the value of SentDate
     */
    public String getSentDate() {
        return sentDate;
    }

    /**
     * Set the value of SentDate
     *
     * @param SentDate new value of SentDate
     */
    public void setSentDate(String SentDate) {
        this.sentDate = SentDate;
    }

    /**
     * Saves a new contact on the database
     *
     * @return if the contact was saved successfully the method return TRUE,
     * else FALSE
     */
    public boolean sendEmail(int employee_id, EmailEntity emailToSend) {

        User user = new User();
        user = user.getUserInfo(employee_id);

        boolean sent = false;

        try {
            //TRY TO SEND THE EMAIL
            Email email = new SimpleEmail();
            email.setHostName(Constants.EMAIL_LOCAL_SERVER);
            email.setSmtpPort(Constants.EMAIL_LOCAL_SERVER_PORT_SMTP);
            email.setAuthenticator(new DefaultAuthenticator(user.getUser_email(), user.getUser_email_pass()));
            email.setSSLOnConnect(false);
            email.setFrom(emailToSend.getFrom());
            email.setSubject(emailToSend.getSubject());
            email.setMsg(emailToSend.getBody());
            email.addTo(emailToSend.getTo());
            email.send();
            
            emailToSend.setMessage_id(email.getMimeMessage().getMessageID());
            
            System.out.println("Message-ID -> " + this.getMessage_id());
            System.out.println("To -> " + this.getTo());
            System.out.println("From -> " + this.getFrom());
            System.out.println("Subject -> " + this.getSubject());
            System.out.println("Body -> " + this.getBody());

            //turns 'sent' to TRUE as it worked
            sent = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        //if it sent the email, save in the database for the email report
        if (sent) {
            try {
                List<EmailEntity> emailToSaveList = new ArrayList<EmailEntity>();
                emailToSaveList.add(emailToSend);
                //create thread with low priority
                EmailSavingThread emailSavingThread = new EmailSavingThread(emailToSaveList);
                //start thread to store the values
                Thread saveEmailThread = new Thread(emailSavingThread);
                //set lowest priority as possible for the thread
                saveEmailThread.setPriority(Thread.MIN_PRIORITY);                
                //starts thread
                saveEmailThread.start();
                
            } catch (Exception e) {
                System.out.println("Problems to save the email on the database.");
                e.printStackTrace();
            }

        }

        return sent;
    }
    
    /**
     * Return a list to be shown on the reports page
     */
    
    public static List<ReportVO> getEmailSentReportList(int month){
        List<ReportVO> reportList = new ArrayList<ReportVO>();

        try {
            Connection con = new ConnectionFactory().getConnectionOpenBYODDB();

            String sql =    "SELECT emailbackup.email_from, count(*) sent_emails" +
                            " FROM openbyodserver.emailbackup emailbackup, employee emp" +
                            " where emailbackup.email_from = emp.email and MONTH(emailbackup.sent_date) = ?" +
                            " group by emailbackup.email_from" +
                            " order by 2 desc";
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setInt(1, month); 
            ResultSet rs = stmt.executeQuery();

            //if it finds the user in the database, the result set is fullfilled then the user is authenticated
            while (rs.next()) {
                //Creates an object with the row from result set
                ReportVO resultSetRow = new ReportVO(rs.getInt("sent_emails"), rs.getString("email_from"));
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
    
    public static List<ReportVO> getEmailReceivedReportList(int month){
        List<ReportVO> reportList = new ArrayList<ReportVO>();

        try {
            Connection con = new ConnectionFactory().getConnectionOpenBYODDB();

            String sql =    "SELECT emailbackup.email_to, count(*) received_emails" +
                            " FROM openbyodserver.emailbackup emailbackup, employee emp" +
                            " where emailbackup.email_to = emp.email and MONTH(emailbackup.sent_date) = ?" +
                            " group by emailbackup.email_to" +
                            " order by 2 desc";
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setInt(1, month); 
            ResultSet rs = stmt.executeQuery();

            //if it finds the user in the database, the result set is fullfilled then the user is authenticated
            while (rs.next()) {
                //Creates an object with the row from result set
                ReportVO resultSetRow = new ReportVO(rs.getInt("received_emails"), rs.getString("email_to"));
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
    
    public static List<EmailEntity> getEmailKeyWordsReportList(int month, String keyWords){
        List<EmailEntity> reportList = new ArrayList<EmailEntity>();

        try {
            Connection con = new ConnectionFactory().getConnectionOpenBYODDB();

            String sql =    "select * from openbyodserver.emailbackup emailbackup where emailbackup.message_id in (select emailbackup.message_id from openbyodserver.emailbackup emailbackup WHERE MONTH(emailbackup.sent_date) = ? AND MATCH(emailbackup.email_body) AGAINST (?));";
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setInt(1, month);
            stmt.setString(2, keyWords);
            
            System.out.println("sql executed -> " + stmt.toString());
            
            ResultSet rs = stmt.executeQuery();
            
            

            //if it finds the user in the database, the result set is fullfilled then the user is authenticated
            while (rs.next()) {
                //Creates an object with the row from result set
                EmailEntity resultSetRow = new EmailEntity(rs.getString("message_id"),
                                                           rs.getString("email_body"),
                                                           rs.getString("email_to"),
                                                           rs.getString("email_subject"),
                                                           rs.getString("email_from"),
                                                           rs.getString("sent_date"));
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