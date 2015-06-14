/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package restful;


import entity.Contact;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

/**
 * REST Web Service
 *
 * @author Castro
 */
@Path("contact")
public class ContactResource {

    @Context
    private UriInfo context;
    private static final Logger LOG = Logger.getLogger(ContactResource.class.getName());
    
    

    /**
     * Creates a new instance of ContactResource
     */
    public ContactResource() {
    }

    /**
     * Retrieves representation of an instance of restful.ContactResource
     * @return an instance of java.lang.String
     */
    @GET
    @Produces("application/json")
    @Path("list")
    public String getContactsList(@HeaderParam("employee_id") int employee_id) {
        
        if(employee_id != 0){
            LOG.info("Lista de contatos para employee ID --> " + employee_id);
        }
        
        List<Contact> contactList = new ArrayList<>();
        
        contactList = Contact.getContactsList(employee_id);
        
        return contactList.toString();
    }
    
    @POST
    @Path("/add")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addContact(Contact contact){
        
        if (!contact.equals(null)) {
            LOG.info("Contato salvo para employee ID -> " + contact.getEmployee_idemployee());
            LOG.info("Contato salvo numero -> " + contact.getNumber_contact());
            LOG.info("Contato salvo nome -> " + contact.getName());
        }
        
        Response response = Response.status(Response.Status.BAD_REQUEST).build();
        
        if(contact.addNewContact()){
            response = Response.status(Response.Status.CREATED).build();
        }
        
        return response;
    }
}
