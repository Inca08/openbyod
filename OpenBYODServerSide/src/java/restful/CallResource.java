/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package restful;

import entity.Call;
import entity.Login;
import java.util.logging.Logger;
import javax.ws.rs.Consumes;
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
@Path("call")
public class CallResource {

    @Context
    private UriInfo context;
    
    private static final Logger LOG = Logger.getLogger(CallResource.class.getName());

    /**
     * Creates a new instance of CallResource
     */
    public CallResource() {
    }


    /**
     * PUT method for updating or creating an instance of CallResource
     * @param content representation for the resource
     * @return an HTTP response with content of the updated or created resource.
     */
    @POST
    @Path("/add")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addCallLog(Call call){
        
        if(!call.equals(null)){
            LOG.info("Gravou chamada para contato id --> " + call.getContact_idcontact() + " e id de employee -> " + call.getEmployee_idemployee() + " <--");
        }
        
        Response response = Response.status(Response.Status.BAD_REQUEST).build();
        
        if(call.addCallLog()){
            response = Response.status(Response.Status.CREATED).build();
        }
        
        return response;
    }
    
}
