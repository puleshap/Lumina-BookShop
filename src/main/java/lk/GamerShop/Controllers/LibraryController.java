package lk.GamerShop.Controllers;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lk.GamerShop.Annotations.IsCustomer;
import lk.GamerShop.service.LibraryService;

@IsCustomer
@Path("/library")
public class LibraryController {

    @Path("/get")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response  getLibrary(@Context HttpServletRequest request){

        String reponse = new LibraryService().getLibrary(request);
        return Response.ok().entity(reponse).build();
    }

}
