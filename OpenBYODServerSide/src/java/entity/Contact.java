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
import javax.json.Json;
import javax.json.JsonObject;
import jdbc.ConnectionFactory;

/**
 *
 * @author Castro
 */
public class Contact {

    private int idcontact;
    private int number_contact;
    private int employee_idemployee;
    private String name;

    /**
     * Get the value of idcontact
     *
     * @return the value of idcontact
     */
    public int getIdcontact() {
        return idcontact;
    }

    /**
     * Set the value of idcontact
     *
     * @param idcontact new value of idcontact
     */
    public void setIdcontact(int idcontact) {
        this.idcontact = idcontact;
    }

    /**
     * Get the value of number_contact
     *
     * @return the value of number_contact
     */
    public int getNumber_contact() {
        return number_contact;
    }

    public Contact(int contact_number, String name) {
        this.number_contact = contact_number;
        this.name = name;
    }

    public Contact() {
    }
    
    

    /**
     * Set the value of number_contact
     *
     * @param number_contact new value of number_contact
     */
    public void setNumber_contact(int number_contact) {
        this.number_contact = number_contact;
    }

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

    /**
     * Get the value of name
     *
     * @return the value of name
     */
    public String getName() {
        return name;
    }

    /**
     * Set the value of name
     *
     * @param name new value of name
     */
    public void setName(String name) {
        this.name = name;
    }

    public Contact(int idcontact, int contact_number, String name) {
        this.idcontact = idcontact;
        this.number_contact = contact_number;
        this.name = name;
    }

    /**
     * toString() implementation to return JSON representation {"a":"b"}
     *
     * @return JSON object
     */
    @Override
    public String toString() {
        try {
            JsonObject jsonobject = Json.createObjectBuilder()
                    .add("idcontact", this.idcontact)
                    .add("name", this.name)
                    .add("contact_number", this.number_contact).build();
            return jsonobject.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Return a list of contacts of a employee, based on the employee_idemployee
     *
     * @param employee_id id of the employee that is the owner of the contacts
     * list
     */
    public static List<Contact> getContactsList(int employee_id) {

        List<Contact> contactList = new ArrayList<Contact>();

        try {
            Connection con = new ConnectionFactory().getConnectionOpenBYODDB();

            String sql = "select contact.idcontact, contact.name, contact.number_contact "
                    + "FROM openbyodserver.contact contact where contact.employee_idemployee = ? order by contact.name asc";
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setInt(1, employee_id);
            ResultSet rs = stmt.executeQuery();

            //if it finds the user in the database, the result set is fullfilled then the user is authenticated
            while (rs.next()) {
                //Creates an object with the row from result set
                Contact resultSetContact = new Contact(rs.getInt("idcontact"), rs.getInt("number_contact"), rs.getString("name"));
                //adds the object to the list
                contactList.add(resultSetContact);
            }

            rs.close();
            stmt.close();
            con.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return contactList;
    }
    
    /**
     * Saves a new contact on the database
     * 
     * @return if the contact was saved successfully the method return TRUE, else FALSE
     */
    public boolean addNewContact() {

        boolean saved = false;

        try {
            Connection con = new ConnectionFactory().getConnectionOpenBYODDB();

            //prepares the SQL to be executed
            String sql = "INSERT INTO openbyodserver.contact"
                    + "(create_date,name,number_contact,employee_idemployee)"
                    + "VALUES (now(),?,?,?)";

            PreparedStatement stmt = con.prepareStatement(sql);

            //prepare the params
            stmt.setString(1, this.getName());
            stmt.setInt(2, this.getNumber_contact());
            stmt.setInt(3, this.getEmployee_idemployee());

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
}
