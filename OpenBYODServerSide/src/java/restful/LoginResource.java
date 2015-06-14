/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package restful;

import entity.Login;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
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
@Path("login")
public class LoginResource {

    @Context
    private UriInfo context;
    private static final Logger LOG = Logger.getLogger(LoginResource.class.getName());
    

    /**
     * Creates a new instance of LoginResource
     */
    public LoginResource() {
    }


    /**
     * PUT method for updating or creating an instance of LoginResource
     * @param content representation for the resource
     * @return an HTTP response with content of the updated or created resource.
     */
    @PUT
    @Consumes("application/json")
    public void putJson(String content) {
    }
    
    /**
     * POST method to authenticate the user inside the application 
     * 
     * @return 
     */
    @POST
    @Path("/authenticate")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response authenticate(Login login){
        
        if (!login.equals(null)) {
            LOG.info("Login para user --> " + login.getUserid() + " senha --> " + login.getPassword());
        }
        
        Response response = Response.status(Response.Status.FORBIDDEN).build();
        
        try {
            
            int idemployee = login.authenticateUser();
            
            //search for the user in the database
            if(idemployee != 0){
                //if the user was found, it sends the user id to be used in the next requests
                response = Response.status(Response.Status.ACCEPTED).entity(idemployee).build();
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(LoginResource.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        //return 202 - ACCPETED if the user was found in the databse with the right password
        //return 401 - UNAUTHORIZED if the user and passowrd were not found in the database
        return response;
    }
}
