/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package restful;

import entity.EmailEntity;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

/**
 * REST Web Service
 *
 * @author Castro
 */
@Path("email")
public class EmailResource {

    @Context
    private UriInfo context;
    private static final Logger LOG = Logger.getLogger(EmailResource.class.getName());
    

    /**
     * Creates a new instance of EmailResource
     */
    public EmailResource() {
    }

    /**
     * Retrieves representation of an instance of restful.EmailResource
     * @return an instance of java.lang.String
     */
    @GET
    @Produces("application/json")
    @Path("/inbox")
    public String getInbox(@HeaderParam("employee_id") int employee_id) {
        
        if(employee_id != 0){
            LOG.info("Lista de emails para employee ID --> " + employee_id);
        }
        
        List<EmailEntity> inboxList = new ArrayList<EmailEntity>();
        
        inboxList = EmailEntity.getEmailInbox(employee_id);        
                
        return inboxList.toString();
    }
    
    /**
     * Sends an email to the email server in order to send the email to a client
     * @return an instance of Response
     */
    @POST
    @Path("/send")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response sendEmail(EmailEntity email, @HeaderParam("employee_id") int employee_id){
        
        if (employee_id != 0 && !email.equals(null)) {
            LOG.info(" Email recebibo .. Assunto --> " + email.getSubject() + " de --> " +
                    email.getFrom() + " body --> " + email.getBody());
        }
        
        Response response = Response.status(Response.Status.BAD_REQUEST).build();
        
        if(email.sendEmail(employee_id,email)){
            response = Response.status(Response.Status.CREATED).build();
        }
        
        //if the email was successfully sent the return will be a HTTP 202-CREATED, else HTTP 400-BAD_REQUEST
        return response;
    }
}
